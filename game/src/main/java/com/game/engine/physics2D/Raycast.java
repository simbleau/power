package com.game.engine.physics2D;

import com.game.engine.maths.Vector2D;

public class Raycast {

	private Vector2D point;
	
	private Vector2D normal;
	
	private double t;
	
	public Raycast(Vector2D point, Vector2D normal, double t) {
		this.point = point;
		this.normal = normal;
		this.t = t;
	}
	
}
