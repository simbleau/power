package com.game.engine.physics2D.common;

import com.game.engine.maths.Vector2D;

/**
 * TODO: Document
 *
 * @author Spencer Imbleau
 * @version January 2021
 */
@SuppressWarnings("javadoc")
public class RigidBody {

	private Vector2D center = new Vector2D();
	
	private double rotation = 0d;
	

	public Vector2D getCenter() {
		return center;
	}

	public void setCenter(Vector2D center) {
		this.center = center;
	}

	public double getRotation() {
		return rotation;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}
}
