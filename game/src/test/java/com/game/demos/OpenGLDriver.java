package com.game.demos;

import org.junit.Assert;

import com.game.demos.gfxtest.GFXTestGame;
import com.game.demos.gfxtest.GFXTestPlane;
import com.game.engine.cache.LRUCache;
import com.game.engine.driver.GameDriver;
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

		int resWidth = Integer.parseInt(args[0]);
		int resHeight = Integer.parseInt(args[1]);
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
		dsF.setResolutionWidth(resWidth);
		dsF.setResolutionHeight(resHeight);

		// Start the game
		GameDriver driver = gdF.get();
		driver.start(dsF.get());
	}
}
