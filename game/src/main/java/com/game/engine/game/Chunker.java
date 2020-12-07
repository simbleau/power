package com.game.engine.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.game.engine.camera.AbstractCamera;
import com.game.engine.coordinates.CoordinateMatrix;
import com.game.engine.driver.GameDriver;

/**
 * An intermediary responsible for helping determine which chunks should be
 * updated and rendered. A chunker takes an {@link AbstractChunkedPlane} and
 * breaks it into {@link Chunk}s of size {@link Chunk#SIZE}.
 *
 * A chunker aims to alleviate cpu-load in collision calculations and rendering
 * by giving insights to the neightbors of chunk i, such that all of chunk i's
 * objects are only able to be impacted by the neighbors of chunk i.
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
	protected List<Chunk> viewableChunks;

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

		// Initialize a buffer for current chunks
		this.viewableChunks = new ArrayList<Chunk>();
	}

	/**
	 * Initialize a chunker by initializing all chunks for a plane.
	 *
	 * @param driver - the game driver
	 */
	public void init(GameDriver driver) {
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

		// Build the chunks
		for (AbstractGameObject object : this.plane.levelObjects) {
			int chunkX = (int) object.x / Chunk.SIZE;
			int chunkY = (int) object.y / Chunk.SIZE;
			this.chunks[chunkX][chunkY].addObject(object);
		}
	}

	/**
	 * Update the current chunks to those visible by a given camera.
	 * 
	 * @param camera - the camera
	 */
	public void update(AbstractCamera camera) {
		// Clear the current chunks
		this.viewableChunks.clear();

		// Build the current visible chunks
		CoordinateMatrix start = CoordinateMatrix.create(camera.viewport.x(), camera.viewport.y());
		CoordinateMatrix end = start.translate(camera.viewport.width() / camera.zoom(),
				camera.viewport.height() / camera.zoom());

		// Declare the boundaries for chunks that may be in view
		int fromRow = (int) start.x() / Chunk.SIZE;
		int toRow = (int) end.x() / Chunk.SIZE;
		int fromColumn = (int) start.y() / Chunk.SIZE;
		int toColumn = (int) end.y() / Chunk.SIZE;

		for (int row = fromRow; row <= toRow; row++) {
			for (int col = fromColumn; col <= toColumn; col++) {
				if (row >= 0 && row < this.rows && col >= 0 && col < this.columns) {
					this.viewableChunks.add(this.chunks[row][col]);
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
}
