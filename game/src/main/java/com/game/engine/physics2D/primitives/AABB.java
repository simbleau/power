package com.game.engine.physics2D.primitives;

import com.game.engine.maths.Vector2D;
import com.game.engine.physics2D.common.RigidBody;

/**
 * An axis-aligned bounding box body for
 *
 * @author Spencer Imbleau
 * @version January 2021
 */
@SuppressWarnings("javadoc")
public class AABB {

	protected Vector2D size = new Vector2D();
	
	protected Vector2D halfSize = new Vector2D();
	
	protected RigidBody rigidBody = null;

	public AABB(Vector2D min, Vector2D max) {
		this.size = max.clone().minus(min);
		this.halfSize = this.size.clone().scale(0.5d);
	}
	
	public Vector2D getMin() {
		return this.rigidBody.getCenter().clone().minus(this.halfSize);
	}
	
	public Vector2D getMax() {
		return this.rigidBody.getCenter().clone().plus(this.halfSize);
	}

	public Vector2D getSize() {
		return size;
	}

	public Vector2D getHalfSize() {
		return halfSize;
	}

	public RigidBody getRigidBody() {
		return rigidBody;
	}
}
