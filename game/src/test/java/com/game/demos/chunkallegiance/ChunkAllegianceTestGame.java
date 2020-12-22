package com.game.demos.chunkallegiance;

import com.game.engine.driver.GameDriver;
import com.game.engine.game.AbstractGame;
import com.game.engine.game.AbstractPlane;
import com.game.engine.rendering.common.AbstractRenderer;

@SuppressWarnings("javadoc")
public class ChunkAllegianceTestGame extends AbstractGame {

	public ChunkAllegianceTestGame(AbstractPlane plane) {
		super(480, 360, plane);
	}

	@Override
	public void init(GameDriver driver) {
		super.init(driver);
	}

	@Override
	public void update(GameDriver driver) {
		super.update(driver);
		driver.getDisplay().setFrameTitle(driver.getDisplay().settings.getRenderingMode() + " - FrameTime : "
				+ driver.getFrameTime().toMillis() + "ms");
	}

	@Override
	public void stage(GameDriver driver, AbstractRenderer renderer) {
		super.stage(driver, renderer);
	}

}