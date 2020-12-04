package com.game.demos;

import java.awt.Dimension;

import org.junit.Assert;

import com.game.demos.gfxtest.GFXTestGame;
import com.game.demos.gfxtest.GFXTestPlane;
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

/**
 * A demo game that forces OpenGL mode rendering.
 * 
 * @author Spencer Imbleau
 * @version December 2020
 */
public class OpenGLDriver {
	/**
	 * Start a OpenGL-Mode demo
	 * @param args - <Resolution width> <Resolution height> <Ticks per second>
	 *             [Frames per second]
	 */
	public static void main(String[] args) {
		Assert.assertTrue(args.length == 4 || args.length == 3);

		int resX = Integer.parseInt(args[0]);
		int resY = Integer.parseInt(args[1]);
		int tps = Integer.parseInt(args[2]);
		int fps = (args.length == 4) ? Integer.parseInt(args[3]) : -1;

		// Setup the game driver settings
		GameDriverFactory gdF = new GameDriverFactory();
		gdF.setTps(tps);
		if (args.length == 4) {
			gdF.setFps(fps);
		}
		gdF.setCache(new LRUCache(1000));
		gdF.setGame(new GFXTestGame(new GFXTestPlane()));

		// Setup the display settings
		DisplaySettingsFactory dsF = new DisplaySettingsFactory();
		dsF.setMode(RenderMode.OPENGL);
		dsF.setResX(resX);
		dsF.setResY(resY);

		// Start the game
		GameDriver driver = gdF.get();
		driver.start(dsF.get());
	}
}
