package com.game.engine.physics2D;

import com.game.engine.maths.Vector2D;

public class Ray {

	private Vector2D origin;
	private Vector2D direction;
	
	public Ray(Vector2D origin, Vector2D direction) {
		this.origin = origin;
		this.direction = direction;
		this.direction.normalize();
	}

	public Vector2D getOrigin() {
		return this.origin;
	}

	public Vector2D getDirection() {
		return this.direction;
	}
	
	
}
