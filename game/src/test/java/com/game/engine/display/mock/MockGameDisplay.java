package com.game.engine.display.mock;

import com.game.engine.display.DisplaySettings;
import com.game.engine.display.GameDisplay;
import com.game.engine.driver.GameDriver;
import com.game.engine.driver.mock.MockGameDriver;

/**
 * An arbitrary game display for testing.
 * 
 * @author Spencer Imbleau
 * @version November 2020
 */
public class MockGameDisplay extends GameDisplay {
	
	/**
	 * An arbitrary game driver for testing.
	 */
	private static final GameDriver DRIVER = new MockGameDriver();

	/**
	 * An arbitrary display settings for testing.
	 */
	private static final DisplaySettings DISPLAY_SETTINGS = new MockDisplaySettings();
	
	/**
	 * Construct an arbitrary game display.
	 */
	public MockGameDisplay() {
		super(DRIVER, DISPLAY_SETTINGS);
	}

}
