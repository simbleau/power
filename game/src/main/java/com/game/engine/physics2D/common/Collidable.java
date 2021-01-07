package com.game.engine.physics2D.common;

import com.game.engine.physics2D.Collision;
import com.game.engine.physics2D.sat.SATCircle;

/**
 * A simulated tangible object which can collide with other simulated collidable
 * objects. The user of this interface has control over what objects can collide
 * with this object and how collision data is reported back to the user in
 * collision events. Collidables have many example uses, such as collision
 * detection and retrieving collision events.
 *
 * @author Spencer Imbleau
 * @version January 2021
 * @see Collision
 */
public interface Collidable {

	/**
	 * Determines whether this area collides with another area.
	 *
	 * @param c - a collidable area
	 * @return true if this collides with the object, false otherwise
	 */
	public default boolean collides(Collidable c) {
		if (c instanceof SATCircle) {
			return collides((SATCircle) c);
		} else {
			throw new UnsupportedOperationException(
					c.getClass().getSimpleName() + " has not implemented collision detection.");
		}
	}

	/**
	 * Determines whether this area collides with a circular body.
	 *
	 * @param c - a circular body
	 * @return true if this collides with the object, false otherwise
	 */
	public abstract boolean collides(SATCircle c);

	/**
	 * Return the collision incurred by two {@link Collidable} objects, or null, if
	 * no collision occurred.
	 *
	 * @param c - a collidable
	 * @return a collision, or null, if no collision occurred.
	 */
	public default Collision getCollision(Collidable c) {
		if (c instanceof SATCircle) {
			return getCollision((SATCircle) c);
		} else {
			throw new UnsupportedOperationException(
					c.getClass().getSimpleName() + " has not implemented collision retrieval.");
		}
	}
	
	/**
	 * Return the collision incurred by this object and a {@link SATCircle} objects, or null, if
	 * no collision occurred.
	 *
	 * @param c - a collidable
	 * @return a collision, or null, if no collision occurred.
	 */
	public abstract Collision getCollision(SATCircle c);

}
