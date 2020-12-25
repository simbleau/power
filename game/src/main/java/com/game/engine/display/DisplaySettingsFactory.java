package com.game.engine.display;

import java.awt.Dimension;
import java.util.Optional;

import com.game.engine.camera.AbstractCamera;
import com.game.engine.rendering.common.RenderMode;

/**
 * A factory pattern which generates a GameDriver.
 * 
 * @author Spencer I
 * @version December 2020
 */
public class DisplaySettingsFactory {

	/**
	 * The stage for resolution width.
	 */
	Optional<Integer> resWidth = Optional.empty();

	/**
	 * The stage for resolution height.
	 */
	Optional<Integer> resHeight = Optional.empty();

	/**
	 * The stage for a rendering mode.
	 */
	Optional<RenderMode> mode = Optional.empty();

	/**
	 * The stage for a camera.
	 */
	Optional<AbstractCamera> camera = Optional.empty();

	/**
	 * Stage a resolution width for use by this factory.
	 * 
	 * @param resWidth - the resolution width to be staged.
	 */
	public void setResolutionWidth(int resWidth) {
		this.resWidth = Optional.of(resWidth);
	}

	/**
	 * Stage a resolution height for use by this factory.
	 * 
	 * @param resHeight - the resolution height to be staged.
	 */
	public void setResolutionHeight(int resHeight) {
		this.resHeight = Optional.of(resHeight);
	}

	/**
	 * Stage a render mode for use by this factory.
	 * 
	 * @param mode - the render mode to be staged.
	 */
	public void setMode(RenderMode mode) {
		this.mode = Optional.of(mode);
	}

	/**
	 * Stage a camera for use by this factory.
	 * 
	 * @param camera - the camera to be staged.
	 */
	public void setCamera(AbstractCamera camera) {
		this.camera = Optional.of(camera);
	}

	/**
	 * @return display settings generated from staged values
	 * @throws IllegalArgumentException if required stages are empty
	 */
	public DisplaySettings get() throws IllegalArgumentException {
		// Ensure all necessary stages are set
		if (!this.resWidth.isPresent()) {
			throw new IllegalArgumentException("Resolution width not staged.");
		}
		if (!this.resHeight.isPresent()) {
			throw new IllegalArgumentException("Resolution height not staged.");
		}
		if (!this.mode.isPresent()) {
			throw new IllegalArgumentException("RenderMode not staged.");
		}
		if (!this.camera.isPresent()) {
			throw new IllegalArgumentException("Camera not staged.");
		}

		// Setup the display settings
		Dimension res = new Dimension(this.resWidth.get(), this.resHeight.get());
		return new DisplaySettings(this.mode.get(), res, this.camera.get());
	}
}
