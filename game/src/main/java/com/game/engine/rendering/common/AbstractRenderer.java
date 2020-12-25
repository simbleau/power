package com.game.engine.rendering.common;

import java.awt.Canvas;
import java.awt.image.BufferedImage;

import com.game.engine.camera.AbstractCamera;
import com.game.engine.graphics.common.RenderRequest;

/**
 * A graphics renderer
 *
 * @author Spencer Imbleau
 * @version June 2020
 */
public abstract class AbstractRenderer {

	/**
	 * The camera used to render.
	 */
	private AbstractCamera camera;

	/**
	 * Whether a screenshot has been requested.
	 */
	private volatile boolean screenshotRequested;

	/**
	 * A buffer to place screenshots to be received by other threads.
	 *
	 * @see #getScreenshot()
	 */
	private volatile BufferedImage screenshot;

	/**
	 * Initialize the renderer
	 *
	 * @param camera - the camera used for rendering
	 */
	public AbstractRenderer(AbstractCamera camera) {
		this.camera = camera;
	}

	/**
	 * Initialize the renderer.
	 * 
	 * @throws Exception if the renderer could not be initialized due to graphic
	 *                   incompatibility.
	 */
	public abstract void init() throws Exception;

	/**
	 * Render the game screen
	 */
	public abstract void render();

	/**
	 * Stage a render request for the next render call
	 *
	 * @param request - the request to render
	 */
	public abstract void stage(RenderRequest request);

	/**
	 * @return the renderer's canvas
	 */
	public abstract Canvas getCanvas();

	/**
	 * @return the renderer's processor
	 */
	public abstract AbstractProcessor getProcessor();

	/**
	 * @return the graphic mode this renderer is performing
	 */
	public abstract RenderMode getMode();

	/**
	 * Set the camera to be used for rendering.
	 *
	 * @param camera - a camera to render with
	 */
	public void setCamera(AbstractCamera camera) {
		this.camera = camera;
	}

	/**
	 * @return the camera being used for rendering
	 */
	public AbstractCamera getCamera() {
		return this.camera;
	}

	/**
	 * @return true if a screenshot has been requested, false otherwise
	 */
	public boolean isScreenshotRequested() {
		return this.screenshotRequested;
	}

	/**
	 * Request a screenshot to be taken.
	 */
	public synchronized void requestScreenshot() {
		this.screenshotRequested = true;
	}

	/**
	 * @return whether the renderer has created an available screenshot
	 */
	public synchronized boolean hasScreenshot() {
		return this.screenshot != null;
	}

	/**
	 * Reads the screenshot buffer and flushes any pending screenshot requests.
	 *
	 * @return a screenshot if it exists, or null
	 */
	public synchronized BufferedImage getScreenshot() {
		this.screenshotRequested = false;
		return this.screenshot;
	}

	/**
	 * Load a screenshot into the screenshot buffer.
	 *
	 * @param screenshot - the screenshot to load
	 */
	public synchronized void setScreenshot(BufferedImage screenshot) {
		this.screenshot = screenshot;
		this.screenshotRequested = false;
	}

}
