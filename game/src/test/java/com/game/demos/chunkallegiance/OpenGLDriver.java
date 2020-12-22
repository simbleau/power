package com.game.demos.chunkallegiance;

import com.game.demos.artifacts.DemoGame;
import com.game.engine.cache.LRUCache;
import com.game.engine.camera.DevCamera;
import com.game.engine.display.DisplaySettingsFactory;
import com.game.engine.driver.GameDriver;
import com.game.engine.driver.GameDriverFactory;
import com.game.engine.game.Chunk;
import com.game.engine.rendering.common.RenderMode;

/**
 * A demo driver to test chunking.
 * 
 * @author Spencer Imbleau
 * @version December 2020
 */
public class OpenGLDriver {

	/**
	 * Start a OpenGL-Mode demo
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
		gdF.setCache(new LRUCache(100));
		gdF.setGame(new DemoGame(new ChunkAllegianceTestPlane()));

		// Setup the display settings
		DisplaySettingsFactory dsF = new DisplaySettingsFactory();
		dsF.setMode(RenderMode.OPENGL);
		dsF.setResolutionWidth(resWidth);
		dsF.setResolutionHeight(resHeight);
		dsF.setCamera(new DevCamera(0, 0, resWidth, resHeight, 1));

		// Start the game
		GameDriver driver = gdF.get();
		driver.start(dsF.get());
	}
}
