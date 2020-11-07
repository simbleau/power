package com.game.physics;

import com.game.engine.driver.GameDriver;
import com.game.engine.game.AbstractGame;
import com.game.engine.game.AbstractPlane;

@SuppressWarnings("javadoc")
public class ExampleGame extends AbstractGame {

	public ExampleGame(AbstractPlane plane) {
		super(480, 360, plane);
	}

	@Override
	public void update(GameDriver driver) {
		// TODO Auto-generated method stub
		super.update(driver);
	}

}
