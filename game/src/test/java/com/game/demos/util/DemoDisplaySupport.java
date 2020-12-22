package com.game.demos.util;

import com.game.engine.camera.AbstractCamera;
import com.game.engine.camera.DevCamera;
import com.game.engine.display.DisplaySettings;
import com.game.engine.display.DisplaySettingsFactory;
import com.game.engine.rendering.common.RenderMode;

/**
 * Utility functions to generate a {@link DisplaySettings} for demos.
 *
 * @author Spencer Imbleau
 * @version December 2020
 * @see DisplaySettings
 */
public class DemoDisplaySupport {

	/**
	 * The default resolution width for demo display settings.
	 */
	public static final int DEFAULT_RES_WIDTH = 720;

	/**
	 * The default resolution height for demo display settings.
	 */
	public static final int DEFAULT_RES_HEIGHT = 480;

	/**
	 * The default camera for demo display settings.
	 */
	public static final AbstractCamera DEFAULT_CAMERA = new DevCamera(0, 0, DEFAULT_RES_WIDTH, DEFAULT_RES_HEIGHT, 1);

	/**
	 * The default render mode for demo display settings.
	 */
	public static final RenderMode DEFAULT_RENDER_MODE = RenderMode.OPENGL;

	/**
	 * Create display settings with default parameters.
	 *
	 * @return a generated display settings
	 */
	public static DisplaySettings getDefault() {
		// Setup the display settings
		DisplaySettingsFactory dsF = new DisplaySettingsFactory();
		dsF.setMode(DEFAULT_RENDER_MODE);
		dsF.setResolutionWidth(DEFAULT_RES_WIDTH);
		dsF.setResolutionHeight(DEFAULT_RES_HEIGHT);
		dsF.setCamera(DEFAULT_CAMERA);

		// Return the generated display settings
		DisplaySettings displaySettings = dsF.get();
		return displaySettings;
	}

	/**
	 * Create display settings with Safe rendering and default parameters.
	 *
	 * @return a generated display settings
	 */
	public static DisplaySettings getDefaultSafe() {
		// Setup the display settings
		DisplaySettingsFactory dsF = new DisplaySettingsFactory();
		dsF.setMode(RenderMode.SAFE);
		dsF.setResolutionWidth(DEFAULT_RES_WIDTH);
		dsF.setResolutionHeight(DEFAULT_RES_HEIGHT);
		dsF.setCamera(DEFAULT_CAMERA);

		// Return the generated display settings
		DisplaySettings displaySettings = dsF.get();
		return displaySettings;
	}

	/**
	 * Create display settings with OpenGL rendering and default parameters.
	 *
	 * @return a generated display settings
	 */
	public static DisplaySettings getDefaultOpenGL() {
		// Setup the display settings
		DisplaySettingsFactory dsF = new DisplaySettingsFactory();
		dsF.setMode(RenderMode.OPENGL);
		dsF.setResolutionWidth(DEFAULT_RES_WIDTH);
		dsF.setResolutionHeight(DEFAULT_RES_HEIGHT);
		dsF.setCamera(DEFAULT_CAMERA);

		// Return the generated display settings
		DisplaySettings displaySettings = dsF.get();
		return displaySettings;
	}
}
