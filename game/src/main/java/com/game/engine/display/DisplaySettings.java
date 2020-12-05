package com.game.engine.display;

import java.awt.Dimension;

import com.game.engine.camera.AbstractCamera;
import com.game.engine.rendering.common.RenderMode;

/**
 * A container for game-specific display settings
 *
 * @author Spencer Imbleau
 * @version June 2020
 */
public class DisplaySettings {

	/**
	 * The preferred resolution for the game canvas
	 */
	private Dimension preferredResolution;

	/**
	 * The rendering mode
	 */
	private RenderMode mode;

	/**
	 * The camera used for rendering
	 */
	private AbstractCamera camera;

	/**
	 * Construct display settings
	 *
	 * @param resolution - the preferred default resolution to use
	 * @param mode       - the rendering mode to use
	 * @param camera     - the camera to use
	 */
	public DisplaySettings(Dimension resolution, RenderMode mode, AbstractCamera camera) {
		this.preferredResolution = resolution;
		this.mode = mode;
		this.camera = camera;
	}

	/**
	 * @return the preferred resolution setting
	 */
	public Dimension getPreferredResolution() {
		return this.preferredResolution;
	}

	/**
	 * Set the preferred resolution setting.
	 *
	 * @param width  - the amount of pixels wide for the game
	 * @param height = the amount of pixels high for the game
	 */
	public void setPreferredResolution(int width, int height) {
		this.preferredResolution = new Dimension(width, height);
	}

	/**
	 * @return the rendering mode
	 */
	public RenderMode getRenderingMode() {
		return this.mode;
	}

	/**
	 * Set the rendering mode
	 *
	 * @param mode - the rendering mode for display
	 */
	public void setRenderingMode(RenderMode mode) {
		this.mode = mode;
	}

	/**
	 * @return the camera
	 */
	public AbstractCamera getCamera() {
		return this.camera;
	}

	/**
	 * Sets the camera
	 *
	 * @param camera - The camera to set
	 */
	public void setCamera(AbstractCamera camera) {
		this.camera = camera;
	}

}
