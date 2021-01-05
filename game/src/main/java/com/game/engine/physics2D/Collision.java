package com.game.engine.physics2D;

import com.game.engine.game.AbstractGameObject;

/**
 * This object encapsulates a collision event information to the invoked
 * physical object.
 *
 * @author Spencer Imbleau
 * @version January 2021
 */
public class Collision {

	/**
	 * The force which invoked this collision.
	 */
	public final AbstractGameObject force;

	/**
	 * The directional theta, in radians, of the force.
	 */
	public final double direction;

	/**
	 * The power of the force.
	 */
	public final double magnitude;

	/**
	 * Create a collision event.
	 *
	 * @param force     - the object which caused the collision
	 * @param theta     - directional theta, in radians, of the force
	 * @param magnitude - the power of the force
	 */
	public Collision(AbstractGameObject force, double theta, double magnitude) {
		this.force = force;
		this.direction = theta;
		this.magnitude = magnitude;
	}

}
