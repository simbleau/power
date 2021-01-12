package com.game.engine.physics2D.primitives;

import com.game.engine.maths.Vector2D;
import com.game.engine.physics2D.common.RigidBody;

/**
 * TODO: Document
 *
 * @author Spencer Imbleau
 * @version January 2021
 */
@SuppressWarnings("javadoc")
public class OBB extends AABB {

	private Vector2D size = new Vector2D();

	private Vector2D halfSize = new Vector2D();

	private RigidBody rigidBody = null;

	public OBB(Vector2D min, Vector2D max) {
		super(min, max);
	}

	@Override
	public Vector2D getMin() {
		// TODO rotate?
		return this.rigidBody.getCenter().clone().minus(this.halfSize);
	}

	@Override
	public Vector2D getMax() {
		// TODO rotate?
		return this.rigidBody.getCenter().clone().plus(this.halfSize);
	}

	public Vector2D[] getVertices() {
		// Collect all vertices
		Vector2D min = getMin();
		Vector2D max = getMax();
		Vector2D[] vertices = { min, new Vector2D(min.x(), min.y()), new Vector2D(max.x(), min.y()), max };
		
		// Rotate all points if we have rotations
		if (this.rigidBody.getRotation() != 0) {
			for (Vector2D vertex : vertices) {
				Vector2D center = this.rigidBody.getCenter();
				vertex.rotate(this.rigidBody.getRotation(), center.x(), center.y());
			}
		}
		return vertices;
	}
	
}
