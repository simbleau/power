package com.game.demos;

import java.awt.Dimension;
import java.util.Optional;

import org.junit.Assert;

import com.game.engine.cache.Cache;
import com.game.engine.cache.LRUCache;
import com.game.engine.camera.AbstractCamera;
import com.game.engine.camera.DevCamera;
import com.game.engine.display.DisplaySettings;
import com.game.engine.driver.DriverSettings;
import com.game.engine.driver.GameDriver;
import com.game.engine.game.AbstractGame;
import com.game.engine.game.AbstractPlane;
import com.game.engine.rendering.common.RenderMode;

public class DisplaySettingsFactory {

	Optional<Integer> resX = Optional.empty();

	Optional<Integer> resY = Optional.empty();

	Optional<RenderMode> mode = Optional.empty();

	public void setResX(int resX) {
		this.resX = Optional.of(resX);
	}

	public void setResY(int resY) {
		this.resY = Optional.of(resY);
	}

	public void setMode(RenderMode mode) {
		this.mode = Optional.of(mode);
	}

	/**
	 * @return a DisplaySettyings fitting the given parameters
	 */
	public DisplaySettings get() {
		Assert.assertTrue(this.resX.isPresent());
		Assert.assertTrue(this.resY.isPresent());
		Assert.assertTrue(this.mode.isPresent());

		// Setup the display settings
		Dimension res = new Dimension(resX.get(), resY.get());
		AbstractCamera cam = new DevCamera();
		return DisplaySettings.create(res, mode.get(), cam);
	}
}
