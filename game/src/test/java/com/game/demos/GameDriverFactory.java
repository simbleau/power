package com.game.demos;

import java.util.Optional;

import org.junit.Assert;

import com.game.engine.cache.Cache;
import com.game.engine.driver.DriverSettings;
import com.game.engine.driver.GameDriver;
import com.game.engine.game.AbstractGame;

public class GameDriverFactory {

	Optional<Integer> tps = Optional.empty();

	Optional<Integer> fps = Optional.empty();

	Optional<Cache> cache = Optional.empty();

	Optional<AbstractGame> game = Optional.empty();

	public void setTps(int tps) {
		this.tps = Optional.of(tps);
	}

	public void setFps(int fps) {
		this.fps = Optional.of(fps);
		;
	}

	public void setCache(Cache cache) {
		this.cache = Optional.of(cache);
		;
	}

	public void setGame(AbstractGame game) {
		this.game = Optional.of(game);
		;
	}

	/**
	 * @return a GameDriver fitting the given parameters
	 */
	public GameDriver get() {
		Assert.assertTrue(this.tps.isPresent());
		Assert.assertTrue(this.game.isPresent());

		// Setup the game driver settings
		DriverSettings driverSettings;
		if (this.fps.isPresent()) {
			driverSettings = DriverSettings.create(tps.get(), fps.get());
		} else {
			driverSettings = DriverSettings.create(tps.get());
		}

		// Start the game
		return new GameDriver(driverSettings, cache.get(), game.get());
	}
}
