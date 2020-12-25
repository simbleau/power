package com.game.demos.artifacts;

import com.game.engine.driver.GameDriver;
import com.game.engine.game.AbstractGame;
import com.game.engine.game.AbstractPlane;
import com.game.engine.rendering.common.AbstractRenderer;

/**
 * A mock demo game
 *
 * @author Spencer Imbleau
 * @version December 2020
 */
public class DemoGame extends AbstractGame {

	/**
	 * Construct a demo game
	 *
	 * @param plane - the plane for the game
	 */
	public DemoGame(AbstractPlane plane) {
		super(480, 360, plane);
	}

	@Override
	public void init(GameDriver driver) {
		super.init(driver);
	}

	@Override
	public void update(GameDriver driver) {
		super.update(driver);
		driver.getDisplay().setFrameTitle(driver.getDisplay().getRenderer().getMode() + " - FrameTime : "
				+ driver.getFrameTime().toMillis() + "ms");
	}

	@Override
	public void stage(GameDriver driver, AbstractRenderer renderer) {
		super.stage(driver, renderer);
	}
	
}