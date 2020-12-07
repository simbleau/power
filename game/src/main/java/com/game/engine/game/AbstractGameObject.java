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
	protected double x = 0;

	/**
	 * The y coordinate for the object
	 */
	protected double y = 0;

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
		return this.x;
	}

	/**
	 * @return the y coordinate of the object
	 */
	public double y() {
		return this.y;
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
	public abstract void update(GameDriver driver);

	@Override
	public abstract void stage(GameDriver driver, AbstractRenderer renderer);

}
