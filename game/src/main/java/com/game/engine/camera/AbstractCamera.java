package com.game.engine.camera;

import java.awt.Canvas;

import com.game.engine.coordinates.CoordinateMatrix;
import com.game.engine.driver.GameDriver;
import com.game.engine.game.AbstractGameObject;
import com.game.engine.game.Updateable;

/**
 * An abstract camera
 *
 * @author Spencer Imbleau
 * @version June 2020
 */
public abstract class AbstractCamera implements Updateable {

	/**
	 * The minimum zoom allowed.
	 */
	protected static final double MIN_ZOOM = 0.1;

	/**
	 * The viewport of the camera.
	 */
	public final Viewport viewport;

	/**
	 * The magnification of the XY axis.
	 */
	protected double zoom;

	/**
	 * Initialize a camera.
	 *
	 * @param x      - starting x displacement
	 * @param y      - starting y displacement
	 * @param width  - width of the viewport
	 * @param height - height of the viewport
	 * @param zoom   - the zoom factor
	 */
	protected AbstractCamera(double x, double y, int width, int height, double zoom) {
		this.viewport = new Viewport(this, x, y, width, height);
		this.zoom = Math.max(MIN_ZOOM, zoom);
	}

	/**
	 * @return the camera's focus point (the center of the screen)
	 */
	public CoordinateMatrix focus() {
		return this.viewport.origin.translate(this.viewport.width / this.zoom / 2,
				this.viewport.height / this.zoom / 2);
	}

	/**
	 * Center the camera's view onto a point.
	 * 
	 * @param x - the x co-ordinate
	 * @param y - the x co-ordinate
	 */
	public void lookAt(double x, double y) {
		this.viewport.origin.set(x - (this.viewport.width / this.zoom / 2), y - (this.viewport.height / this.zoom / 2));
	}

	/**
	 * Center the camera's view onto an object.
	 * 
	 * @param obj - the object to look at
	 */
	public void lookAt(AbstractGameObject obj) {
		lookAt(obj.x(), obj.y());
	}

	/**
	 * @return the camera's zoom
	 */
	public double zoom() {
		return this.zoom;
	}

	/**
	 * Replace the zoom scale of this camera to a new scale.
	 * 
	 * @param zoom - the zoom scale
	 */
	public void setZoom(double zoom) {
		this.zoom = Math.max(MIN_ZOOM, zoom);
	}

	/**
	 * Adjust the zoom by a delta zoom factor.
	 * 
	 * @param dz - delta zoom factor to magnify the camera's zoom
	 */
	public void magnify(double dz) {
		this.zoom = Math.max(MIN_ZOOM, this.zoom + dz);
	}

	/**
	 * Translate the camera by a delta on both axes.
	 * 
	 * @param dx - delta x to translate the camera
	 * @param dy - delta y to translate the camera
	 */
	public void translate(double dx, double dy) {
		this.viewport.origin.translateEquals(dx, dy);
	}

	/**
	 * Translate the camera by a delta on the X axis.
	 * 
	 * @param dx - delta x to translate the camera
	 */
	public void translateX(double dx) {
		this.viewport.origin.translateXEquals(dx);
	}

	/**
	 * Translate the camera by a delta on the Y axis.
	 * 
	 * @param dy - delta y to translate the camera
	 */
	public void translateY(double dy) {
		this.viewport.origin.translateYEquals(dy);
	}

	@Override
	public void update(GameDriver driver) {
		// Resize viewport to canvas size
		Canvas canvas = driver.getDisplay().getRenderer().getCanvas();
		this.viewport.resize(canvas.getWidth(), canvas.getHeight());
	}

}