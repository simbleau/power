package com.game.demos.chunker;

import com.game.demos.objects.DemoUpdatingImage;
import com.game.engine.driver.GameDriver;
import com.game.engine.game.Chunk;
import com.game.engine.logger.PowerLogger;
import com.jogamp.opengl.GL2;

@SuppressWarnings("javadoc")
public class VerboseDemoUpdatingImage extends DemoUpdatingImage {

	public VerboseDemoUpdatingImage(int size) {
		super(size);
		this.speed = 0;
		int offsetX = (Chunk.SIZE / 2) - (this.width / 2);
		int offsetY = (Chunk.SIZE / 2) - (this.width / 2);
		this.position.set(Chunk.SIZE + offsetX, Chunk.SIZE + offsetY);
	}

	@Override
	public void alloc(GL2 gl) {
		super.alloc(gl);
		PowerLogger.LOGGER.fine("Allocating " + this.getClass().getSimpleName());
	}

	@Override
	public void dispose(GL2 gl) {
		super.dispose(gl);
		PowerLogger.LOGGER.fine("Disposed " + this.getClass().getSimpleName());
	}

	@Override
	public void move(GameDriver driver, double x, double y) {
		super.move(driver, x, y);
		PowerLogger.LOGGER.fine(
				"Object moved to: " + x + "," + y + " (" + (int) x / Chunk.SIZE + "," + (int) y / Chunk.SIZE + ")");
	}

}
