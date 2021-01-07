package com.game.engine.physics2D.primitives;

import com.game.engine.maths.Vector2D;

/**
 * An axis-aligned bounding box body for
 *
 * @author Spencer Imbleau
 * @version January 2021
 */
public class AABB {

	/**
	 * TODO: Document
	 */
	private Vector2D size = new Vector2D();
	
	/**
	 * TODO: Document
	 */
	@SuppressWarnings("unused")
	private Vector2D center = new Vector2D();

	/**
	 * TODO: Document
	 */
	@SuppressWarnings("javadoc")
	public AABB(Vector2D min, Vector2D max) {
		this.size = max.clone().minus(min);
		this.center = this.size.clone().scale(0.5d).plus(min);
	}

}
