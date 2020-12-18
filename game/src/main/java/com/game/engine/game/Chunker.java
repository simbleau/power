package com.game.engine.game;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.game.engine.camera.AbstractCamera;
import com.game.engine.driver.GameDriver;
import com.game.engine.logger.PowerLogger;

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
		this.rows = plane.width / Chunk.SIZE + (plane.width % Chunk.SIZE == 0 ? 0 : 1);
		this.columns = plane.height / Chunk.SIZE + (plane.height % Chunk.SIZE == 0 ? 0 : 1);
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
		for (AbstractGameObject obj : this.plane.levelObjects) {
			if (obj.chunkRow() < 0 || obj.chunkRow() >= this.rows || obj.chunkColumn() < 0
					|| obj.chunkColumn() >= this.columns) {
				PowerLogger.LOGGER.warning(obj.getClass().getName() + " cannot be added at " + obj.x() + "," + obj.y()
						+ " because chunk " + obj.chunkRow() + "," + obj.chunkColumn() + " does not exist.");
				this.flagGLTrash(obj);
				continue;
			}
			this.chunks[obj.chunkRow()][obj.chunkColumn()].addGameObject(obj);
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
		Iterator<Chunk> lastViewableChunks = viewableIterator();
		while (lastViewableChunks.hasNext()) {
			Chunk c = lastViewableChunks.next();
			if (!currentlyViewableChunks.contains(c)) {
				// Disappeared chunk - Needs disposal
				lastViewableChunks.remove();
				if (driver.getDisplay().isGL()) {
					// Dispose all chunk objects
					c.chunkObjects.forEach(obj -> this.trashedObjects.add(obj));
				}
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
	 * @return an iterator of the current viewable chunks
	 */
	public Iterator<Chunk> viewableIterator() {
		return this.viewableChunks.iterator();
	}

	/**
	 * @return an iterator of the currently loading objects for dynamic memory
	 *         handling such as in OpenGL
	 */
	public Iterator<AbstractGameObject> loadingIterator() {
		return this.loadingObjects.iterator();
	}

	/**
	 * @return an iterator of the currently trashed objects for dynamic memory
	 *         handling such as in OpenGL
	 */
	public Iterator<AbstractGameObject> trashedIterator() {
		return this.trashedObjects.iterator();
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
	 * Flag this object as having components or resources needing to be disposed by
	 * OpenGL.
	 *
	 * @param obj - an object
	 */
	public void flagGLTrash(AbstractGameObject obj) {
		this.trashedObjects.add(obj);
	}
}
