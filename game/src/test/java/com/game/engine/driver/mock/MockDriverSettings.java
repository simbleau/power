package com.game.engine.driver.mock;

import com.game.engine.driver.DriverSettings;

/**
 * An arbitrary driver settings for testing.
 * 
 * @author Spencer Imbleau
 * @version November 2020
 */
public class MockDriverSettings extends DriverSettings {
	
	/**
	 * An arbitrary ticks per second.
	 */
	private static int TICKS_PER_SECOND = 20;
	
	/**
	 * An arbitrary frames per second.
	 */
	private static int FRAMES_PER_SECOND = 60;
	
	/**
	 * Construct arbitrary driver settings.
	 */
	public MockDriverSettings() {
		super(TICKS_PER_SECOND, FRAMES_PER_SECOND);
	}
}
