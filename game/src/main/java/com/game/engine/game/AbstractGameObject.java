package com.game.engine.game;

import com.game.engine.driver.GameDriver;
import com.game.engine.graphics.common.Renderable;
import com.game.engine.rendering.common.AbstractRenderer;

/**
 * A game object
 *
 * @author Spencer Imbleau
 * @version June 2020
 */
public abstract class AbstractGameObject implements Updateable, Renderable {

	/**
	 * The x coordinate for the object
	 */
	protected float x;

	/**
	 * The y coordinate for the object
	 */
	protected float y;

	/**
	 * The width of the object
	 */
	protected int width;

	/**
	 * The height of the object
	 */
	protected int height;

	/**
	 * @return the x coordinate of the object
	 */
	public float x() {
		return this.x;
	}

	/**
	 * @return the y coordinate of the object
	 */
	public float y() {
		return this.y;
	}

	/**
	 * @return the pixel width of the object
	 */
	public abstract int getWidth();

	/**
	 * @return the pixel height of the object
	 */
	public abstract int getHeight();

	@Override
	public abstract void update(GameDriver driver);

	@Override
	public abstract void stage(GameDriver driver, AbstractRenderer renderer);

}
