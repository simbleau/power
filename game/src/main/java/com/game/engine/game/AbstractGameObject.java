package com.game.engine.game;

import com.game.engine.coordinates.CoordinateMatrix;
import com.game.engine.driver.GameDriver;
import com.game.engine.graphics.common.GLObject;
import com.game.engine.graphics.common.Renderable;
import com.game.engine.rendering.common.AbstractRenderer;
import com.jogamp.opengl.GL2;

/**
 * A game object
 *
 * @author Spencer Imbleau
 * @version December 2020
 */
public abstract class AbstractGameObject implements Updateable, Renderable, GLObject {

	/**
	 * The position of the object, which is attached to the plane. Do not directly
	 * modify the position in subclass implementations besides at initialization.
	 * Instead, use {@link #move(GameDriver, double, double)}.
	 *
	 * @see #move(GameDriver, double, double)
	 */
	// TODO - Abstract the position to a new class for better modiciation/reading
	protected CoordinateMatrix position = CoordinateMatrix.create(0, 0);

	/**
	 * The width of the object
	 */
	protected int width = 0;

	/**
	 * The height of the object
	 */
	protected int height = 0;

	/**
	 * @return the x coordinate of the object
	 */
	public double x() {
		return this.position.x();
	}

	/**
	 * @return the y coordinate of the object
	 */
	public double y() {
		return this.position.y();
	}

	/**
	 * @return the pixel width of the object
	 */
	public int width() {
		return this.width;
	}

	/**
	 * @return the pixel height of the object
	 */
	public int height() {
		return this.height;
	}

	/**
	 * Initialize this object.
	 * 
	 * @param driver - the driver for the game
	 */
	public abstract void init(GameDriver driver);

	@Override
	public abstract void alloc(GL2 gl);

	@Override
	public abstract void refresh(GL2 gl);

	@Override
	public abstract void dispose(GL2 gl);

	@Override
	public abstract void update(GameDriver driver);

	@Override
	public abstract void stage(GameDriver driver, AbstractRenderer renderer);

	/**
	 * Move this object's position to another coordinate.
	 *
	 * @param driver - the driver for the game
	 * @param x      - the x co-ordinate to move to
	 * @param y      - the y co-ordinate to move to
	 */
	public void move(GameDriver driver, double x, double y) {
		// Check if it needs to be marked for deletion/loading before moving
		if (driver.getDisplay().isGL() && driver.game.plane.isChunked()) {
			Chunker chunker = ((AbstractChunkedPlane) driver.game.plane).chunker;
			Chunk from = chunker.chunks[(int) this.position.x() / Chunk.SIZE][(int) this.position.y() / Chunk.SIZE];
			Chunk to = chunker.chunks[(int) x / Chunk.SIZE][(int) y / Chunk.SIZE];
			if (chunker.viewableChunks.contains(from) && !chunker.viewableChunks.contains(to)) {
				// Trash this object, it's going somewhere not viewable, but was previously
				chunker.flagGLTrash(this);
			}
			if (!chunker.viewableChunks.contains(from) && chunker.viewableChunks.contains(to)) {
				// Load this object, it's going somewhere viewable, but wasn't previously
				chunker.flagGLLoad(this);
			}
		}

		this.position.set(x, y);
	}
}
