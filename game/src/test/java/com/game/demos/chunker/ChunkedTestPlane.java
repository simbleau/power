package com.game.demos.chunker;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Random;

import com.game.engine.camera.AbstractCamera;
import com.game.engine.driver.GameDriver;
import com.game.engine.game.AbstractChunkedPlane;
import com.game.engine.game.AbstractMotionGameObject;
import com.game.engine.game.Chunk;
import com.game.engine.logger.PowerLogger;
import com.game.engine.rendering.common.AbstractRenderer;

@SuppressWarnings("javadoc")
public class ChunkedTestPlane extends AbstractChunkedPlane {

	static Random rng = new Random();

	static final int OBJ_SIZE = 3 * Chunk.SIZE / 5;

	static final AbstractMotionGameObject TEST_OBJ = new VerboseDemoUpdatingImage(OBJ_SIZE);

	static final int CHUNKS_WIDE = 3;

	static final int CHUNKS_HIGH = 3;

	private int chunkIndex = (CHUNKS_WIDE * CHUNKS_HIGH / 2);

	public ChunkedTestPlane() {
		super(Chunk.SIZE * 3, Chunk.SIZE * 3);

		// Make the object motionless
		TEST_OBJ.setSpeed(0);

		// Add object to the map
		this.objects.add(TEST_OBJ);
	}

	@Override
	public void init(GameDriver driver) {
		super.init(driver);
		this.lookAtPlane(driver);
	}

	@Override
	public void update(GameDriver driver) {
		// Perform a deep clone of our old chunks
		HashSet<Chunk> lastChunks = new HashSet<>();
		for (Chunk chunk : this.chunker.viewableChunks()) {
			lastChunks.add(chunk);
		}

		// Do a regular update
		super.update(driver);

		// Perform a deep clone of our current chunks
		HashSet<Chunk> currentChunks = new HashSet<>();
		for (Chunk chunk : this.chunker.viewableChunks()) {
			currentChunks.add(chunk);
		}

		// Log all disappeared chunks
		for (Chunk c : lastChunks) {
			if (!currentChunks.contains(c)) {
				PowerLogger.LOGGER.fine("Chunk disappeared: " + c.row + ", " + c.column);
			}
		}
		// Log all new chunks
		for (Chunk c : currentChunks) {
			if (!lastChunks.contains(c)) {
				PowerLogger.LOGGER.fine("Chunk appeared: " + c.row + ", " + c.column);
			}
		}

		// Handle input
		if (driver.getInput().isKeyDown(KeyEvent.VK_SPACE)) {
			TEST_OBJ.move(driver,
					rng.nextInt(this.chunker.getRows()) * Chunk.SIZE + (Chunk.SIZE / 2) - (TEST_OBJ.width() / 2),
					rng.nextInt(this.chunker.getColumns()) * Chunk.SIZE + (Chunk.SIZE / 2) - (TEST_OBJ.height() / 2));
		}

		if (driver.getInput().isKeyUp(KeyEvent.VK_UP)) {
			lookAtPlane(driver);
		}

		if (driver.getInput().isKeyUp(KeyEvent.VK_DOWN)) {
			lookAtChunk(driver, this.chunkIndex);
		}

		if (driver.getInput().isKeyUp(KeyEvent.VK_RIGHT)) {
			this.chunkIndex = Math.min(this.chunker.getRows() * this.chunker.getColumns() - 1, this.chunkIndex + 1);
			lookAtChunk(driver, this.chunkIndex);
		}

		if (driver.getInput().isKeyUp(KeyEvent.VK_LEFT)) {
			this.chunkIndex = Math.max(0, this.chunkIndex - 1);
			lookAtChunk(driver, this.chunkIndex);
		}
	}

	@Override
	public void stage(GameDriver driver, AbstractRenderer renderer) {
		super.stage(driver, renderer);
	}

	private void lookAtPlane(GameDriver driver) {
		AbstractCamera cam = driver.getDisplay().getRenderer().getCamera();
		if (cam.viewport.width() > cam.viewport.height()) {
			cam.setZoom((double) cam.viewport.height() / (this.height));
		} else {
			cam.setZoom((double) cam.viewport.width() / (this.width));
		}
		cam.lookAt(this.width / 2, this.height / 2);
	}

	private void lookAtChunk(GameDriver driver, int index) {
		index = Math.max(0, index) % (this.chunker.getRows() * this.chunker.getColumns());
		int x = index % this.chunker.getRows();
		int y = index / this.chunker.getRows();

		AbstractCamera cam = driver.getDisplay().getRenderer().getCamera();
		if (cam.viewport.width() > cam.viewport.height()) {
			cam.setZoom((double) cam.viewport.height() / Chunk.SIZE);
		} else {
			cam.setZoom((double) cam.viewport.width() / Chunk.SIZE);
		}

		cam.lookAt(x * Chunk.SIZE + Chunk.SIZE / 2, y * Chunk.SIZE + Chunk.SIZE / 2);
	}
}
