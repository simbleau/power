package com.game.demos.camera;

import com.game.demos.artifacts.DemoGame;
import com.game.demos.util.DemoDisplaySupport;
import com.game.demos.util.DemoDriverSupport;
import com.game.engine.driver.GameDriver;
import com.game.engine.game.AbstractGame;

/**
 * A demo driver to test camera affecting coordinates.
 *
 * @author Spencer Imbleau
 * @version December 2020
 */
public class SafeDriver {

	/**
	 * The game for this demo.
	 */
	private static final AbstractGame DEMO_GAME = new DemoGame(new TestCameraPlane());

	/**
	 * Start a Safe-Mode demo.
	 *
	 * @param args - CLI args
	 */
	public static void main(String[] args) {
		GameDriver driver = DemoDriverSupport.getDefault(DEMO_GAME);
		driver.init(DemoDisplaySupport.getDefaultSafe());
		driver.start();
	}
}
