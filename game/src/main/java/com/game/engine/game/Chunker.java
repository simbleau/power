package com.game.engine.game;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.game.engine.camera.AbstractCamera;
import com.game.engine.driver.GameDriver;
import com.game.engine.logger.PowerLogger;
import com.jogamp.opengl.GL2;

/**
 * An intermediary responsible for helping determine which chunks should be
 * updated and rendered. A chunker takes an {@link AbstractChunkedPlane} and
 * breaks it into {@link Chunk}s of size {@link Chunk#SIZE}.
 *
 * A chunker aims to alleviate cpu-load in collision calculations and rendering
 * by giving insights to the neightbors of chunk i, such that all of chunk i's
 * objects are only able to be impacted by the neighbors of chunk i. A chunker
 * also aims to handle memory allocation for objects.
 *
 * @author Spencer Imbleau
 * @version November 2020
 * @see Chunk
 * @see AbstractChunkedPlane
 */
public class Chunker {

	/**
	 * The plane being chunked
	 */
	public final AbstractChunkedPlane plane;

	/**
	 * The amount of rows of chunks in this chunker's {@link #plane}
	 */
	protected int rows;

	/**
	 * The amount of columns of chunks in this chunker's {@link #plane}
	 */
	protected int columns;

	/**
	 * All chunks in the {@link #plane}
	 */
	public final Chunk[][] chunks;

	/**
	 * The current chunks which are viewable to the player.
	 */
	protected HashSet<Chunk> viewableChunks;

	/**
	 * A queue of objects which were trashed (no longer in view) and need cleanup by
	 * OpenGL.
	 */
	protected Queue<AbstractGameObject> trashedObjects;

	/**
	 * A queue of objects which were newly visible and need resources from OpenGL.
	 */
	protected Queue<AbstractGameObject> loadingObjects;

	/**
	 * Initialize a chunker
	 *
	 * @param plane - the plane to chunk
	 */
	public Chunker(AbstractChunkedPlane plane) {
		// Set the plane
		this.plane = plane;

		// Initialize a buffer of chunks
		this.rows = plane.height / Chunk.SIZE + (plane.height % Chunk.SIZE == 0 ? 0 : 1);
		this.columns = plane.width / Chunk.SIZE + (plane.width % Chunk.SIZE == 0 ? 0 : 1);
		this.chunks = new Chunk[rows][columns];

		// Initialize a buffer for currently viewable chunks
		this.viewableChunks = new HashSet<Chunk>();
		this.trashedObjects = new ConcurrentLinkedQueue<>();
		this.loadingObjects = new ConcurrentLinkedQueue<>();
	}

	/**
	 * Initialize a chunker by initializing all chunks for a plane.
	 *
	 * @param driver - the game driver
	 */
	public void init(GameDriver driver) {
		// Clear all chunk lists
		this.viewableChunks.clear();
		this.trashedObjects.clear();
		this.loadingObjects.clear();

		// Fill the chunk buffer
		for (int row = 0; row < this.rows; row++) {
			for (int col = 0; col < this.columns; col++) {
				this.chunks[row][col] = new Chunk(this.plane, row, col);
			}
		}

		// Initialize neighbor chunks
		for (int row = 0; row < this.rows; row++) {
			for (int col = 0; col < this.columns; col++) {
				Chunk chunk = this.chunks[row][col];

				// Find where neighbors exist
				boolean chunkAbove = false;
				boolean chunkBelow = false;
				boolean chunkLeft = false;
				boolean chunkRight = false;
				if (row - 1 >= 0) {
					chunkAbove = true;
				}
				if (row + 1 < this.rows) {
					chunkBelow = true;
				}
				if (col - 1 >= 0) {
					chunkLeft = true;
				}
				if (col + 1 < this.columns) {
					chunkRight = true;
				}

				// Populate neighbors
				// Top, Bottom, Left, Right
				if (chunkAbove) {
					chunk.addNeighbor(this.chunks[row - 1][col]);
				}
				if (chunkBelow) {
					chunk.addNeighbor(this.chunks[row + 1][col]);
				}
				if (chunkLeft) {
					chunk.addNeighbor(this.chunks[row][col - 1]);
				}
				if (chunkRight) {
					chunk.addNeighbor(this.chunks[row][col + 1]);
				}
				// Top-left, Top-right, Bottom-left, Bottom-right
				if (chunkAbove) {
					if (chunkLeft) {
						chunk.addNeighbor(this.chunks[row - 1][col - 1]);
					}
					if (chunkRight) {
						chunk.addNeighbor(this.chunks[row - 1][col + 1]);
					}
				}
				if (chunkBelow) {
					if (chunkLeft) {
						chunk.addNeighbor(this.chunks[row + 1][col - 1]);
					}
					if (chunkRight) {
						chunk.addNeighbor(this.chunks[row + 1][col + 1]);
					}
				}
			}
		}
	}

	/**
	 * Assign all level objects to chunks.
	 *
	 * @param driver - the driver for the game
	 */
	public void chunk(GameDriver driver) {
		// Clear the chunks
		for (Chunk[] row : this.chunks) {
			for (Chunk chunk : row) {
				chunk.clear();
			}
		}

		// Assign plane objects to chunks
		for (AbstractGameObject obj : this.plane.objects) {
			if (obj.position.chunkRow() < 0 || obj.position.chunkRow() >= this.rows || obj.position.chunkColumn() < 0
					|| obj.position.chunkColumn() >= this.columns) {
				PowerLogger.LOGGER.warning(obj.getClass().getName() + " cannot be added at " + obj.position.x() + ","
						+ obj.position.y() + " because chunk " + obj.position.chunkRow() + ","
						+ obj.position.chunkColumn() + " does not exist.");
				this.flagGLTrash(obj);
				continue;
			}
			this.chunks[obj.position.chunkRow()][obj.position.chunkColumn()].addGameObject(obj);
		}
	}

	/**
	 * Update the viewable chunk lists relative to the scanned area visible by a
	 * given camera.
	 * 
	 * @param driver - the game driver
	 * @param camera - the camera
	 */
	public void scan(GameDriver driver, AbstractCamera camera) {
		// Declare the bounds seen from start->end by the camera
		int fromRow = camera.viewport.closestChunkRow();
		int fromColumn = camera.viewport.closestChunkColumn();
		int toRow = camera.viewport.furthestChunkRow();
		int toColumn = camera.viewport.furthestChunkColumn();

		// Build a new list with the currently visible chunks
		HashSet<Chunk> currentlyViewableChunks = new HashSet<>(
				Math.abs(toRow - fromRow) * Math.abs(toColumn - fromColumn));
		for (int row = fromRow; row <= toRow; row++) {
			for (int col = fromColumn; col <= toColumn; col++) {
				if (row >= 0 && row < this.rows && col >= 0 && col < this.columns) {
					currentlyViewableChunks.add(this.chunks[row][col]);
				}
			}
		}

		// Compare the new list with the last list to determine chunks no longer in view
		Queue<Chunk> trashableChunks = new LinkedList<>();
		for (Chunk c : viewableChunks()) {
			if (!currentlyViewableChunks.contains(c)) {
				// Disappeared chunk - Needs disposal
				trashableChunks.add(c);
			}
		}
		// Trash the non-viewable chunks
		for (Chunk c : trashableChunks) {
			this.viewableChunks.remove(c);
			if (driver.getDisplay().isGL()) {
				// Dispose all chunk objects
				c.chunkObjects.forEach(obj -> this.trashedObjects.add(obj));
			}
		}

		// Compare the new list with the last list to determine chunks newly in view
		for (Chunk c : currentlyViewableChunks) {
			if (!this.viewableChunks.contains(c)) {
				// New chunk - Needs initialization
				this.viewableChunks.add(c);
				if (driver.getDisplay().isGL()) {
					// Load all chunk objects
					c.chunkObjects.forEach(obj -> this.loadingObjects.add(obj));
				}
			}
		}

	}

	/**
	 * @return an iterable of the current viewable chunks
	 */
	public Iterable<Chunk> viewableChunks() {
		return this.viewableChunks;
	}

	/**
	 * @return an iterable of the currently loading objects for dynamic memory
	 *         handling such as in OpenGL
	 */
	public Iterable<AbstractGameObject> loadingObjects() {
		return this.loadingObjects;
	}

	/**
	 * @return an iterable of the currently trashed objects for dynamic memory
	 *         handling such as in OpenGL
	 */
	public Iterable<AbstractGameObject> trashedObjects() {
		return this.trashedObjects;
	}

	/**
	 * @return the amount of rows of chunks
	 */
	public int getRows() {
		return this.rows;
	}

	/**
	 * @return the amount of columns of chunks
	 */
	public int getColumns() {
		return this.columns;
	}

	/**
	 * Flag this object as having components or resources needing to be allocated by
	 * OpenGL.
	 *
	 * @param obj - an object
	 */
	public void flagGLLoad(AbstractGameObject obj) {
		this.loadingObjects.add(obj);
	}

	/**
	 * Load an object via OpenGL.
	 *
	 * @param gl  - an OpenGL context
	 * @param obj - the object to load
	 */
	public void load(GL2 gl, AbstractGameObject obj) {
		obj.alloc(gl);
		this.loadingObjects.remove(obj);
	}

	/**
	 * Flag this object as having components or resources needing to be disposed by
	 * OpenGL.
	 *
	 * @param obj - an object
	 */
	public void flagGLTrash(AbstractGameObject obj) {
		this.trashedObjects.add(obj);
	}

	/**
	 * Trash an object via OpenGL.
	 *
	 * @param gl  - an OpenGL context
	 * @param obj - the object to trash
	 */
	public void trash(GL2 gl, AbstractGameObject obj) {
		obj.dispose(gl);
		this.trashedObjects.remove(obj);
	}
}
