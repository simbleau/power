package com.game.demos.font;

import com.game.demos.artifacts.DemoGame;
import com.game.demos.chunker.ChunkedTestPlane;
import com.game.demos.gfxtest.GFXTestPlane;
import com.game.demos.util.DemoDisplaySupport;
import com.game.demos.util.DemoDriverSupport;
import com.game.engine.cache.LRUCache;
import com.game.engine.camera.StationaryCamera;
import com.game.engine.display.DisplaySettingsFactory;
import com.game.engine.driver.GameDriver;
import com.game.engine.driver.GameDriverFactory;
import com.game.engine.game.AbstractGame;
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
	 * The game for this demo.
	 */
	private static final AbstractGame DEMO_GAME = new DemoGame(new FontTestPlane());

	/**
	 * Start a Safe-Mode demo
	 *
	 * @param args - CLI args
	 */
	public static void main(String[] args) {
		// Setup the game driver settings
		GameDriverFactory gdF = new GameDriverFactory();
		gdF.setTps(DemoDriverSupport.DEFAULT_TPS);
		gdF.setCache(DemoDriverSupport.DEFAULT_CACHE);
		gdF.setGame(DEMO_GAME);

		// Setup the display settings
		DisplaySettingsFactory dsF = new DisplaySettingsFactory();
		dsF.setMode(RenderMode.SAFE);
		dsF.setResolutionWidth(500);
		dsF.setResolutionHeight(500);
		dsF.setCamera(new StationaryCamera(0, 0, 500, 500, 1));

		// Start the game
		GameDriver driver = gdF.get();
		driver.start(dsF.get());

		// Specific to this demo
		driver.getDisplay().setResizable(false);
		driver.setInput(new VerboseMouseKeyboard(driver));
	}
}
