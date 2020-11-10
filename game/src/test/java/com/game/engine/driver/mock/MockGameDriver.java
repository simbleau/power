package com.game.engine.driver.mock;

import com.game.engine.cache.Cache;
import com.game.engine.cache.LRUCache;
import com.game.engine.driver.DriverSettings;
import com.game.engine.driver.GameDriver;
import com.game.engine.game.AbstractGame;
import com.game.engine.game.mock.MockGame;

/**
 * An arbitrary game driver for testing.
 * 
 * @author Spencer Imbleau
 * @version November 2020
 */
public class MockGameDriver extends GameDriver {
	
	/**
	 * An arbitrary driver settings for testing.
	 */
	private static final DriverSettings DRIVER_SETTINGS = new MockDriverSettings();
	
	/**
	 * An arbitrary cache for testing.
	 */
	private static final Cache CACHE = new LRUCache(10);
	
	/**
	 * An arbitrary game for testing.
	 */
	private static final AbstractGame GAME = new MockGame();
	
	/**
	 * Construct an arbitrary game driver.
	 */
	public MockGameDriver(){
		super(DRIVER_SETTINGS, CACHE, GAME);
	}

}
