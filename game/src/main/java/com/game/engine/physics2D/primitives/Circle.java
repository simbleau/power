package com.game.engine.physics2D.primitives;

import com.game.engine.maths.Vector2D;
import com.game.engine.physics2D.common.RigidBody;

/**
 * TODO: Document
 */
@SuppressWarnings("javadoc")
public class Circle {

	private double radius;
	
	private RigidBody body = null;
	
	public Circle(double radius) {
		this.radius = radius;
	}

	public double getRadius() {
		return radius;
	}
	
	public Vector2D getCenter() {
		return body.getCenter();
	}
}
