package com.game.engine.camera;

import com.game.engine.driver.GameDriver;
import com.game.engine.game.Updateable;

/**
 * An abstract camera
 *
 * @author Spencer Imbleau
 * @version June 2020
 */
public abstract class AbstractCamera implements Updateable {

	/**
	 * The displacement on the X axis
	 */
	protected double x;

	/**
	 * The displacement on the Y axis
	 */
	protected double y;

	/**
	 * The magnification of the XY axis
	 */
	protected double zoom;

	/**
	 * Initialize a camera.
	 *
	 * @param x    - starting x displacement
	 * @param y    - starting y displacement
	 * @param zoom - the zoom factor
	 */
	protected AbstractCamera(double x, double y, double zoom) {
		this.x = x;
		this.y = y;
		this.zoom = zoom;
	}

	/**
	 * @return the camera's x coordinate
	 */
	public double x() {
		return this.x;
	}

	/**
	 * @return the camera's y coordinate
	 */
	public double y() {
		return this.y;
	}

	/**
	 * @return the camera's zoom
	 */
	public double zoom() {
		return this.zoom;
	}

	@Override
	public abstract void update(GameDriver driver);

}