package com.game.engine.camera;

import com.game.engine.coordinates.CoordinateMatrix;

/**
 * The user's visible area.
 * 
 * @author Spencer Imbleau
 * @version December 2020
 */
public class Viewport {
	
	/**
	 * The origin of the camera, also the top-left point of the camera.
	 */
	protected CoordinateMatrix origin;

	/**
	 * The width of the viewport.
	 */
	protected int width;

	/**
	 * The height of the viewport.
	 */
	protected int height;

	/**
	 * Initialize a viewport with dimensions.
	 * 
	 * @param x      - starting displacement on the X axis
	 * @param y      - starting displacement on the Y axis
	 * @param width  - width of the viewport
	 * @param height - height of the viewport
	 */
	public Viewport(double x, double y, int width, int height) {
		this.origin = CoordinateMatrix.create(x, y);
		this.width = width;
		this.height = height;
	}

	/**
	 * @return the viewport's displacement on the X-axis
	 */
	public double x() {
		return this.origin.x();
	}

	/**
	 * @return the viewport's displacement on the Y-axis
	 */
	public double y() {
		return this.origin.y();
	}

	/**
	 * @return the viewport's width
	 */
	public int width() {
		return this.width;
	}

	/**
	 * @return the viewport's height
	 */
	public int height() {
		return this.height;
	}

	/**
	 * @param width  - a new width for the viewport
	 * @param height - a new height for the viewport
	 */
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * @param x - an x co-ordinate
	 * @param y - a y co-ordinate
	 * @return true if the viewport can see the given point, false otherwise
	 */
	public boolean canSee(double x, double y) {
		// Too far west
		if (this.origin.x() > x) {
			return false;
		}
		// Too far east
		if (x > this.origin.x() + this.width) {
			return false;
		}
		// Too far north
		if (this.origin.y() > y) {
			return false;
		}
		// To far south
		if (y > this.origin.y() + this.height) {
			return false;
		}
		return true;
	}
}
