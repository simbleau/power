package com.game.engine.game;

import com.game.engine.driver.GameDriver;
import com.game.engine.graphics.common.Renderable;
import com.game.engine.rendering.common.AbstractRenderer;

/**
 * An abstract game handler
 *
 * @version June 2020
 * @author Spencer Imbleau
 */
public abstract class AbstractGame implements Updateable, Renderable {

	/**
	 * The minimum resolution width for this game without rendering issues.
	 */
	public final int MIN_RESOLUTION_WIDTH;

	/**
	 * The minimum resolution height for this game without rendering issues.
	 */
	public final int MIN_RESOLUTION_HEIGHT;

	/**
	 * The current plane
	 *
	 * @see AbstractPlane
	 */
	protected AbstractPlane plane;

	/**
	 * Initialize a game
	 *
	 * @param minWidth  - minimum width to display the game without render issues
	 * @param minHeight - minimum height to display the game without render issues
	 * @param plane     - the plane for the game
	 */
	public AbstractGame(final int minWidth, final int minHeight, AbstractPlane plane) {
		this.MIN_RESOLUTION_WIDTH = minWidth;
		this.MIN_RESOLUTION_HEIGHT = minHeight;
		this.plane = plane;
	}

	/**
	 * Initialization of the game
	 *
	 * @param driver - the driver for the game
	 */
	public void init(GameDriver driver) {
		this.plane.init(driver);
	}

	@Override
	public void update(GameDriver driver) {
		this.plane.update(driver);
	}

	@Override
	public void stage(GameDriver driver, AbstractRenderer renderer) {
		this.plane.stage(driver, renderer);
	}

	/**
	 * Returns the current plane of the game
	 *
	 * @return the current plane of the game
	 */
	public AbstractPlane getPlane() {
		return this.plane;
	}

	/**
	 * Sets the plane
	 *
	 * @param plane - the plane to set
	 */
	public void setPlane(AbstractPlane plane) {
		this.plane = plane;
	}

}
