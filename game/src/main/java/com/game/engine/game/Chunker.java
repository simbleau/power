package com.game.engine.game;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.game.engine.camera.AbstractCamera;
import com.game.engine.coordinates.CoordinateMatrix;
import com.game.engine.driver.GameDriver;

/**
 * An intermediary responsible for determines which chunks are being updated and
 * rendered.
 *
 * The algorithm implemented takes an {@link AbstractPlane} and breaks it into
 * {@link Chunk}s of size {@link Chunk#SIZE}.
 *
 * This allows collision detection to occurs only on updateable chunks and their
 * neighboring chunks for n*O(log(n)) time efficiency and a render time
 * efficiency of O(log(n)) time.
 *
 * @author Spencer Imbleau
 * @version June 2020
 * @see Chunk
 * @see AbstractPlane
 */
public class Chunker {

	/**
	 * The plane being chunked
	 */
	public final AbstractPlane plane;

	/**
	 * The amount of rows of chunks in this chunker's {@link #plane}
	 */
	private int rows;

	/**
	 * The amount of columns of chunks in this chunker's {@link #plane}
	 */
	private int columns;

	/**
	 * All chunks in the {@link #plane}
	 */
	private Chunk[][] chunks;

	/**
	 * The current chunks
	 */
	private List<Chunk> currentChunks;

	/**
	 * Initialize a chunker
	 *
	 * @param plane - the plane to chunk
	 */
	public Chunker(AbstractPlane plane) {
		// Set the plane
		this.plane = plane;

		// Initialize a buffer of chunks
		this.rows = plane.getWidth() / Chunk.SIZE + (plane.getWidth() % Chunk.SIZE == 0 ? 0 : 1);
		this.columns = plane.getHeight() / Chunk.SIZE + (plane.getHeight() % Chunk.SIZE == 0 ? 0 : 1);
		this.chunks = new Chunk[rows][columns];

		// Initialize a buffer for current chunks
		this.currentChunks = new ArrayList<Chunk>();
	}

	/**
	 * Initialize a chunker.
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

		// Initialize a buffer for current chunks
		this.currentChunks = new ArrayList<Chunk>();

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
	 * Rebuild the current chunks
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

		// Clear the current chunks
		this.currentChunks.clear();

		// Build the current visible chunks
		AbstractCamera camera = driver.getDisplay().settings.getCamera();
		Dimension resolution = driver.getDisplay().getRenderer().getCanvas().getSize();
		CoordinateMatrix start = CoordinateMatrix.create(camera.x(), camera.y());
		CoordinateMatrix end = start.translate(resolution.width / camera.zoom(), resolution.height / camera.zoom());

		for (int x = (int) start.x() / Chunk.SIZE; x < end.x() / Chunk.SIZE; x++) {
			for (int y = (int) start.y() / Chunk.SIZE; y < end.y() / Chunk.SIZE; y++) {
				if (x >= 0 && x < this.rows && y >= 0 && y < this.columns) {
					this.currentChunks.add(this.chunks[x][y]);
				}
			}
		}
	}

	/**
	 * @return an iterator of the current viewable chunks
	 */
	public Iterator<Chunk> iterator() {
		return this.currentChunks.iterator();
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
