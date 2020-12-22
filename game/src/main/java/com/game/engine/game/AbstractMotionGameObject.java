package com.game.engine.game;

import com.game.engine.coordinates.Position;
import com.game.engine.driver.GameDriver;

public abstract class AbstractMotionGameObject extends AbstractGameObject {

	/**
	 * The speed of this object, measured in pixels per millisecond.
	 */
	protected double speed = 0d;
	
	/**
	 * The direction of this object, measured in radians.
	 */
	protected double direction = 0d;
	
	
	public double tickDx(GameDriver driver) {
		// TODO
		return 0;
	}
	
	public double tickDy(GameDriver driver) {
		//TODO
		return 0;
	}
}
