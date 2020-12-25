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
	 * The graphic mode
	 */
	private RenderMode preferredMode;

	/**
	 * The preferred resolution for the game canvas
	 */
	private Dimension preferredResolution;

	/**
	 * The camera used for rendering
	 */
	private AbstractCamera preferredCamera;

	/**
	 * Construct display settings
	 *
	 * @param resolution - the preferred default resolution to use
	 * @param mode       - the graphic mode to use
	 * @param camera     - the camera to use
	 */
	public DisplaySettings(RenderMode mode, Dimension resolution, AbstractCamera camera) {
		this.preferredMode = mode;
		this.preferredResolution = resolution;
		this.preferredCamera = camera;
	}

	/**
	 * @return the preferred graphic mode
	 */
	public RenderMode getPreferredMode() {
		return this.preferredMode;
	}

	/**
	 * Set the preferred graphic mode
	 *
	 * @param mode - a render mode
	 */
	public void setPreferredMode(RenderMode mode) {
		this.preferredMode = mode;
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
	 * @return the preferred camera
	 */
	public AbstractCamera getPreferredCamera() {
		return this.preferredCamera;
	}

	/**
	 * Sets the preferred camera
	 *
	 * @param camera - The camera to set as preferred
	 */
	public void setPreferredCamera(AbstractCamera camera) {
		this.preferredCamera = camera;
	}

}
