package com.game.demos.chunkallegiance;

import com.game.demos.artifacts.DemoGame;
import com.game.demos.util.DemoDisplaySupport;
import com.game.demos.util.DemoDriverSupport;
import com.game.engine.cache.LRUCache;
import com.game.engine.camera.DevCamera;
import com.game.engine.display.DisplaySettingsFactory;
import com.game.engine.driver.GameDriver;
import com.game.engine.driver.GameDriverFactory;
import com.game.engine.game.AbstractGame;
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
	 * The game for this demo.
	 */
	private static final AbstractGame DEMO_GAME = new DemoGame(new ChunkAllegianceTestPlane());

	/**
	 * Start a Safe-Mode demo
	 * 
	 * @param args - CLI args
	 */
	public static void main(String[] args) {
		GameDriver driver = DemoDriverSupport.getDefault(DEMO_GAME);
		driver.init(DemoDisplaySupport.getDefaultOpenGL());
		driver.start();
	}
}
