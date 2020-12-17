package com.game.demos.chunker;

import com.game.demos.objects.DemoUpdatingImage;
import com.game.engine.driver.GameDriver;
import com.game.engine.game.Chunk;
import com.jogamp.opengl.GL2;

@SuppressWarnings("javadoc")
public class VerboseDemoUpdatingImage extends DemoUpdatingImage {

	public VerboseDemoUpdatingImage(int size) {
		super(size);
		int offsetX = (Chunk.SIZE / 2) - (this.width / 2);
		int offsetY = (Chunk.SIZE / 2) - (this.width / 2);
		this.position.set(Chunk.SIZE + offsetX, Chunk.SIZE + offsetY);
		SPEED = 0;
	}

	@Override
	public void alloc(GL2 gl) {
		super.alloc(gl);
		System.out.println("Allocated " + this.getClass().getSimpleName()); // TODO log this with a proper logger
	}

	@Override
	public void dispose(GL2 gl) {
		super.dispose(gl);
		System.out.println("Disposed " + this.getClass().getSimpleName()); // TODO log this with a proper logger
	}

	@Override
	public void move(GameDriver driver, double x, double y) {
		super.move(driver, x, y);
		driver.logger.fine(
				"Object moved to: " + x + "," + y + " (" + (int) x / Chunk.SIZE + "," + (int) y / Chunk.SIZE + ")");
	}

}
