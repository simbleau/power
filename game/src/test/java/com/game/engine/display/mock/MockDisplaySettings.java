package com.game.engine.display.mock;

import java.awt.Dimension;

import com.game.engine.camera.AbstractCamera;
import com.game.engine.camera.mock.MockCamera;
import com.game.engine.display.DisplaySettings;
import com.game.engine.rendering.common.RenderMode;

/**
 * An arbitrary display settings for testing.
 * 
 * @author Spencer Imbleau
 * @version November 2020
 */
public class MockDisplaySettings extends DisplaySettings {

	/**
	 * An arbitrary resolution for testing.
	 */
	private static final Dimension RESOLUTION = new Dimension(480, 360);

	/**
	 * An arbitrary render mode for testing.
	 */
	private static final RenderMode MODE = RenderMode.SAFE;

	/**
	 * An arbitrary camera for testing.
	 */
	private static final AbstractCamera CAMERA = new MockCamera(0, 0, 0, 0, 1);

	/**
	 * Construct arbitrary display settings.
	 */
	public MockDisplaySettings() {
		super(RESOLUTION, MODE, CAMERA);
	}
}
