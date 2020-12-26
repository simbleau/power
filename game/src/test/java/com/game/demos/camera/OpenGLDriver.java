package com.game.demos.camera;

import com.game.demos.artifacts.DemoGame;
import com.game.demos.util.DemoDriverSupport;
import com.game.engine.camera.DevCamera;
import com.game.engine.display.DisplaySettingsFactory;
import com.game.engine.driver.GameDriver;
import com.game.engine.driver.GameDriverFactory;
import com.game.engine.game.AbstractGame;
import com.game.engine.game.Chunk;
import com.game.engine.rendering.common.RenderMode;

/**
 * A demo driver to test camera affecting coordinates.
 *
 * @author Spencer Imbleau
 * @version December 2020
 */
public class OpenGLDriver {

	/**
	 * The game for this demo.
	 */
	private static final AbstractGame DEMO_GAME = new DemoGame(new TestCameraPlane());

	/**
	 * Start an OpenGL-Mode demo.
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
		dsF.setMode(RenderMode.OPENGL);
		dsF.setResolutionWidth(480);
		dsF.setResolutionHeight(360);
		dsF.setCamera(new DevCamera(0, 0, 0, 0, 1));

		// Start the game
		GameDriver driver = gdF.get();
		driver.init(dsF.get());
		driver.start();
	}
}
