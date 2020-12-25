package com.game.engine.game;

import java.awt.Dimension;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.game.engine.camera.AbstractCamera;
import com.game.engine.camera.mock.MockCamera;
import com.game.engine.display.DisplaySettings;
import com.game.engine.driver.GameDriver;
import com.game.engine.driver.mock.MockGameDriver;
import com.game.engine.game.mock.MockGameObject;
import com.game.engine.rendering.common.RenderMode;

/**
 * Test {@link Chunker}.
 * 
 * @author Spencer Imbleau
 * @version November 2020
 */
public class TestChunker {

	/**
	 * An arbitrary test driver.
	 */
	private static GameDriver TEST_DRIVER = new MockGameDriver();

	/**
	 * Buffer chunkers for testing.
	 */
	private static Chunker[] chunkers = new Chunker[9];

	/**
	 * Setup a new chunker for every test.
	 */
	@Before
	public void init() {
		// Make planes which should chunk to cover every possibility of neighbors
		// ranging from 1x1 to 3x3.

		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 3; c++) {
				// Create abstract plane with desired chunks
				int w = (r + 1) * Chunk.SIZE;
				int h = (c + 1) * Chunk.SIZE;
				AbstractChunkedPlane p = new AbstractChunkedPlane(w, h) {
				};
				chunkers[(r * 3) + c] = p.chunker;
				p.chunker.init(TEST_DRIVER);

				// Add an object in every chunk

				for (int ri = 0; ri <= r; ri++) {
					for (int ci = 0; ci <= c; ci++) {
						int x = ri * Chunk.SIZE + (Chunk.SIZE / 2);
						int y = ci * Chunk.SIZE + (Chunk.SIZE / 2);
						AbstractGameObject o = new MockGameObject(x, y);
						p.addGameObject(o);
					}

				}
			}
		}
	}

	/**
	 * Tests {@link Chunker#chunk(GameDriver)}.
	 */
	@Test
	public void testInit() {
		for (Chunker c : chunkers) {
			c.init(TEST_DRIVER);

			int expectedRows = c.plane.height / Chunk.SIZE + (c.plane.height % Chunk.SIZE == 0 ? 0 : 1);
			int expectedColumns = c.plane.width / Chunk.SIZE + (c.plane.width % Chunk.SIZE == 0 ? 0 : 1);

			Assert.assertEquals(expectedRows, c.getRows());
			Assert.assertEquals(expectedColumns, c.getColumns());
		}
	}

	/**
	 * Tests all chunks neighbor patterns are correct.
	 */
	@Test
	public void testNeighbors() {
		for (Chunker c : chunkers) {
			c.init(TEST_DRIVER);

			int maxRow = Math.max(0, c.getRows() - 1);
			int maxCol = Math.max(0, c.getColumns() - 1);

			// Transform 2D chunks to 1D chunks
			Chunk[] chunks = Arrays.stream(c.chunks).flatMap(Arrays::stream).toArray(Chunk[]::new);
			// Test on all chunks
			for (Chunk chunk : chunks) {
				// Ensure the correct amount of neighbors
				int expectedNeighbors; // Maximum neighbors

				// Case 1: Single chunk
				// x = 0 neighbors
				if (chunk.row == 0 && c.getRows() == 1 && chunk.column == 0 && c.getColumns() == 1) {
					expectedNeighbors = 0;
				}
				// Case 2: Skinny side row chunk
				// x
				// n = 1 neighbor
				// -
				else if ((chunk.row == maxRow || chunk.row == 0) && c.getColumns() == 1) {
					expectedNeighbors = 1;
				}
				// Case 3: Skinny side column chunk
				// xn- = 1 neighbor
				else if ((chunk.column == maxCol || chunk.column == 0) && c.getRows() == 1) {
					expectedNeighbors = 1;
				}
				// Case 4: Skinny middle column chunk
				// nxn = 2 neighbors
				else if (chunk.column != maxCol && chunk.column != 0 && c.getRows() == 1) {
					expectedNeighbors = 2;
				}
				// Case 5: Skinny middle row chunk
				// n
				// x = 2 neighbors
				// n
				else if (chunk.row != maxRow && chunk.row != 0 && c.getColumns() == 1) {
					expectedNeighbors = 2;
				}
				// Case 6: Corner chunk
				// xn-
				// nn- = 3 neighbors
				// ---
				else if ((chunk.column == 0 && chunk.row == 0) // Top left
						|| (chunk.column == maxCol && chunk.row == 0) // Top right
						|| (chunk.column == 0 && chunk.row == maxRow) // Bottom left
						|| (chunk.column == maxCol && chunk.row == maxRow) // Bottom right
				) {
					expectedNeighbors = 3;
				}
				// Case 7: Side chunk
				// nxn
				// nnn = 5 neighbors
				// ---
				else if ((chunk.column > 0 && chunk.column < maxCol && chunk.row == 0) // Top middle
						|| (chunk.row > 0 && chunk.row < maxRow && chunk.column == 0) // Left middle
						|| (chunk.row > 0 && chunk.row < maxRow && chunk.column == maxCol) // Right middle
						|| (chunk.column > 0 && chunk.column < maxCol && chunk.row == maxRow) // Bottom middle
				) {
					expectedNeighbors = 5;
				}
				// Case 8: Omnidirectionally locked chunk
				// nnn
				// nxn = 8 neighbors
				// nnn
				else {
					expectedNeighbors = 8;
				}
				Assert.assertEquals(expectedNeighbors, chunk.neighbors.size());

				// Test neighbors are actually neighbors (<= 1 row/column away)
				for (Chunk neighbor : chunk.neighbors) {
					// Ensure all neighbors are direct adjacent neighbors
					int dx = Math.abs(chunk.row - neighbor.row);
					int dy = Math.abs(chunk.column - neighbor.column);
					Assert.assertTrue(dx <= 1);
					Assert.assertTrue(dy <= 1);

					// Ensure the neighbor is not the chunk
					Assert.assertNotEquals(chunk, neighbor);
				}
			}
		}
	}

	/**
	 * Test {@link Chunker#chunk(GameDriver)}.
	 */
	@Test
	public void testChunk() {
		for (Chunker c : chunkers) {
			c.init(TEST_DRIVER);
			c.chunk(TEST_DRIVER);

			for (AbstractGameObject obj : c.plane.levelObjects) {
				int chunkRow = obj.chunkRow();
				int chunkColumn = obj.chunkColumn();
				Assert.assertTrue(c.chunks[chunkRow][chunkColumn].chunkObjects.contains(obj));
			}
		}
	}

	/**
	 * Test {@link Chunker#scan(GameDriver, AbstractCamera)}.
	 */
	@Test
	public void testScan() {
		for (Chunker c : chunkers) {
			c.init(TEST_DRIVER);

			// Test several cases
			// Case 1: Camera viewport covers entire plane
			AbstractCamera cam1 = new MockCamera(0, 0, c.plane.width, c.plane.height, 1);
			// Case 2: Camera viewport covers 1 chunk
			AbstractCamera cam2 = new MockCamera(0, 0, Chunk.SIZE, Chunk.SIZE, 1);
			// Case 3: Camera viewport covers 2+ chunks wide
			AbstractCamera cam3 = new MockCamera((c.rows / 2) * Chunk.SIZE, 0, Chunk.SIZE * 2, Chunk.SIZE, 1);
			// Case 4: Camera viewport covers 2+ chunks tall
			AbstractCamera cam4 = new MockCamera(0, (c.columns / 2) * Chunk.SIZE, Chunk.SIZE, Chunk.SIZE * 2, 1);
			// Case 5: Camera viewport covers 2+ chunks wide and tall
			AbstractCamera cam5 = new MockCamera((c.rows / 2) * Chunk.SIZE, (c.columns / 2) * Chunk.SIZE,
					Chunk.SIZE * 2, Chunk.SIZE * 2, 1);

			AbstractCamera cams[] = { cam1, cam2, cam3, cam4, cam5 };

			for (AbstractCamera cam : cams) {
				// Refresh visible chunks
				DisplaySettings ds = new DisplaySettings(RenderMode.SAFE, new Dimension(0, 0), cam);
				TEST_DRIVER.init(ds);
				TEST_DRIVER.start();
				c.scan(TEST_DRIVER, cam);

				// Declare the boundaries for chunks that may be in view
				int fromRow = (int) cam.viewport.y() / Chunk.SIZE;
				int toRow = (int) (cam.viewport.y() + cam.viewport.height() / cam.zoom()) / Chunk.SIZE;
				int fromColumn = (int) cam.viewport.x() / Chunk.SIZE;
				int toColumn = (int) (cam.viewport.x() + cam.viewport.width() / cam.zoom()) / Chunk.SIZE;

				// Check all chunks are within bounds
				for (Chunk next : c.viewableChunks()) {
					Assert.assertTrue(next.row >= fromRow);
					Assert.assertTrue(next.row <= toRow);
					Assert.assertTrue(next.column >= fromColumn);
					Assert.assertTrue(next.column <= toColumn);
				}
				TEST_DRIVER.stop();
			}
		}
	}
}
