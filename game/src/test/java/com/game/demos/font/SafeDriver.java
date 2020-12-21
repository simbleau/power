package com.game.demos.font;

import com.game.engine.cache.LRUCache;
import com.game.engine.camera.StationaryCamera;
import com.game.engine.display.DisplaySettingsFactory;
import com.game.engine.driver.GameDriver;
import com.game.engine.driver.GameDriverFactory;
import com.game.engine.game.Chunk;
import com.game.engine.rendering.common.RenderMode;

/**
 * A demo driver to test font rendering.
 * 
 * @author Spencer Imbleau
 * @version December 2020
 */
public class SafeDriver {

	/**
	 * Start a Safe-Mode demo
	 * 
	 * @param args - CLI args
	 */
	public static void main(String[] args) {
		int resWidth = Chunk.SIZE;
		int resHeight = Chunk.SIZE;
		int tps = 30;

		// Setup the game driver settings
		GameDriverFactory gdF = new GameDriverFactory();
		gdF.setTps(tps);
		gdF.setCache(new LRUCache(10));
		gdF.setGame(new FontTestGame(new FontTestPlane()));

		// Setup the display settings
		DisplaySettingsFactory dsF = new DisplaySettingsFactory();
		dsF.setMode(RenderMode.SAFE);
		dsF.setResolutionWidth(resWidth);
		dsF.setResolutionHeight(resHeight);
		dsF.setCamera(new StationaryCamera(0, 0, resWidth, resHeight, 1));

		// Start the game
		GameDriver driver = gdF.get();
		driver.start(dsF.get());

		// Make it not resizable
		driver.getDisplay().setResizable(false);
		driver.setInput(new VerboseMouseKeyboard(driver));
	}
}
