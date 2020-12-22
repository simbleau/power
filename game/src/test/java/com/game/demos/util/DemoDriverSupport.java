package com.game.demos.util;

import com.game.engine.cache.Cache;
import com.game.engine.cache.LRUCache;
import com.game.engine.driver.GameDriver;
import com.game.engine.driver.GameDriverFactory;
import com.game.engine.game.AbstractGame;

/**
 * Utility functions to generate a {@link GameDriver} for demos.
 *
 * @author Spencer Imbleau
 * @version December 2020
 * @see GameDriver
 */
public class DemoDriverSupport {

	/**
	 * The default ticks per second for demo game drivers.
	 */
	public static final Cache DEFAULT_CACHE = new LRUCache(1000);

	/**
	 * The default ticks per second for demo game drivers.
	 */
	public static final int DEFAULT_TPS = 30;

	/**
	 * Create a GameDriver for a game with default parameters.
	 *
	 * @param game - the game for this driver
	 * @return a game driver
	 */
	public static GameDriver getDefault(AbstractGame game) {
		// Setup the game driver settings
		GameDriverFactory gdF = new GameDriverFactory();
		gdF.setTps(DEFAULT_TPS);
		gdF.setCache(DEFAULT_CACHE);
		gdF.setGame(game);

		// Return the generated driver
		GameDriver driver = gdF.get();
		return driver;
	}
}
