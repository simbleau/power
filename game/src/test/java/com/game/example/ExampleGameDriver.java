package com.game.example;

import com.game.engine.driver.GameDriver;
import com.game.engine.game.AbstractGame;
import com.game.engine.game.AbstractPlane;
import com.game.engine.rendering.common.RenderMode;

import java.awt.Dimension;

import com.game.engine.cache.Cache;
import com.game.engine.cache.LRUCache;
import com.game.engine.camera.AbstractCamera;
import com.game.engine.camera.DevCamera;
import com.game.engine.display.DisplaySettings;
import com.game.engine.driver.DriverSettings;

@SuppressWarnings("javadoc")
public class ExampleGameDriver {

	public static void main(String[] args) {
		// Setup the game driver settings
		int tps = 30;
		int fps = 60;
		DriverSettings driverSettings = DriverSettings.create(tps, // Ticks per second
				fps // Restricted FPS
		);
		driverSettings.unrestrictFPS();

		// Setup the display settings
		Dimension resolution = new Dimension(720, 600);
		RenderMode mode = RenderMode.OPENGL;
		AbstractCamera camera = new DevCamera();
		DisplaySettings displaySettings = DisplaySettings.create(resolution, // Game resolution
				mode, // Rendering mode
				camera);

		// Setup the game
		AbstractPlane plane = new ExamplePlane();
		AbstractGame game = new ExampleGame(plane);
		Cache cache = new LRUCache(1000);
		// Start the game
		GameDriver driver = new GameDriver(driverSettings, cache, game);
		driver.start(displaySettings);
	}
}
