package com.game.demos;

import java.util.Optional;

import com.game.engine.cache.Cache;
import com.game.engine.driver.DriverSettings;
import com.game.engine.driver.GameDriver;
import com.game.engine.game.AbstractGame;

/**
 * A factory pattern which generates a GameDriver.
 * 
 * @author Spencer I
 * @version December 2020
 */
public class GameDriverFactory {

	/**
	 * The stage for ticks per second.
	 */
	Optional<Integer> tps = Optional.empty();

	/**
	 * The stage for a desired frames per second goal.
	 */
	Optional<Integer> fps = Optional.empty();

	/**
	 * The stage for a cache.
	 */
	Optional<Cache> cache = Optional.empty();

	/**
	 * The stage for a game.
	 */
	Optional<AbstractGame> game = Optional.empty();

	/**
	 * Stage a restricted ticks per second for use by this factory.
	 * 
	 * @param tps - the ticks per second to be staged.
	 */
	public void setTps(int tps) {
		this.tps = Optional.of(tps);
	}

	/**
	 * Stage a restricted frames per second goal for use by this factory.
	 * 
	 * @param fps - the desired frames per second goal to be staged.
	 */
	public void setFps(int fps) {
		this.fps = Optional.of(fps);
		;
	}

	/**
	 * Stage a cache for use by this factory.
	 * 
	 * @param cache - the cache to be staged.
	 */
	public void setCache(Cache cache) {
		this.cache = Optional.of(cache);
		;
	}

	/**
	 * Stage a game for use by this factory.
	 * 
	 * @param game - the game to be staged.
	 */
	public void setGame(AbstractGame game) {
		this.game = Optional.of(game);
		;
	}

	/**
	 * @return driver settings generated from staged values
	 * @throws IllegalArgumentException if required stages are empty
	 */
	public GameDriver get() {
		// Ensure all necessary stages are set
		if (!this.tps.isPresent()) {
			throw new IllegalArgumentException("TPS not staged.");
		}
		if (!this.game.isPresent()) {
			throw new IllegalArgumentException("Game not staged.");
		}

		// Setup the game driver settings
		DriverSettings driverSettings;
		if (this.fps.isPresent()) {
			driverSettings = new DriverSettings(tps.get(), fps.get());
		} else {
			driverSettings = new DriverSettings(tps.get());
		}

		// Start the game
		return new GameDriver(driverSettings, cache.get(), game.get());
	}
}
