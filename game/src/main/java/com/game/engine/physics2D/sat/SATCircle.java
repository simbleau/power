package com.game.engine.physics2D.sat;

import com.game.engine.driver.GameDriver;
import com.game.engine.game.AbstractGameObject;
import com.game.engine.graphics.common.RenderRequest;
import com.game.engine.graphics.obj.Ellipse;
import com.game.engine.physics2D.Collision;
import com.game.engine.physics2D.common.BoundingVolume;
import com.game.engine.physics2D.common.Collidable;
import com.game.engine.rendering.common.AbstractRenderer;
import com.game.engine.rendering.common.RenderLevel;

public class SATCircle implements Collidable, BoundingVolume {

	/**
	 * The physical object for this collidable.
	 */
	public final AbstractGameObject parent;

	/**
	 * The radius of this circle.
	 */
	protected double radius;

	/**
	 * The center x co-ordinate of this circle.
	 */
	protected double cx;

	/**
	 * The center y co-ordinate of this circle.
	 */
	protected double cy;

	public SATCircle(AbstractGameObject parent, int radius, int cx, int cy) {
		this.parent = parent;
		this.radius = radius;
		this.cx = cx;
		this.cy = cy;
	}

	/**
	 * Determines whether this circle collides with another circle. A collision
	 * incurs when the distance between their radii is greater than their respective
	 * radii as a sum.
	 */
	@Override
	public boolean collides(SATCircle o) {
		// Obtain distance between midpoints
		double dx = (this.parent.position.x() + this.cx) - (o.parent.position.x() + o.cx);
		double dy = (this.parent.position.y() + this.cy) - (o.parent.position.y() + o.cy);
		double distance = Math.sqrt(dx * dx + dy * dy);

		// Check for collision by calculating their intersection
		if (distance - this.radius - o.radius < 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int xMin() {
		return this.parent.position.x.asInt() + (int) (this.cx - this.radius);
	}

	@Override
	public int xMax() {
		return this.parent.position.x.asInt() + (int) (this.cx + this.radius);
	}

	@Override
	public int yMin() {
		return this.parent.position.y.asInt() + (int) (this.cy - this.radius);
	}

	@Override
	public int yMax() {
		return this.parent.position.y.asInt() + (int) (this.cy + this.radius);
	}

	public void stage(GameDriver driver, AbstractRenderer renderer) {
		Ellipse e = new Ellipse((int) radius * 2, (int) radius * 2, 0xff00ffff);
		RenderRequest r = e.asRequest(RenderLevel.WORLD_OVERLAY,
				this.parent.position.x.asInt() + (int) (this.cx - this.radius),
				this.parent.position.y.asInt() + (int) (this.cy - this.radius));
		renderer.stage(r);
	}

	/**
	 * Return the angle, in radians, between two object positions.
	 *
	 * @param p1 - an object with a position
	 * @param p2 - an object with a position
	 * @return the angle, in radians, between two objects
	 */
	public static double angleFrom(AbstractGameObject p1, AbstractGameObject p2) {
		return Math.atan2(p2.position.y() - p1.position.y(), p2.position.x() - p1.position.x());
	}

	@Override
	public Collision getCollision(SATCircle o) {
		// Obtain distance between midpoints
		double dx = (this.parent.position.x() + this.cx) - (o.parent.position.x() + o.cx);
		double dy = (this.parent.position.y() + this.cy) - (o.parent.position.y() + o.cy);
		double distance = Math.sqrt(dx * dx + dy * dy);

		// Convert the distance to amount of intersection
		double intersection = distance - this.radius - o.radius;
		if (intersection < 0) {
			double direction = angleFrom(o.parent, this.parent);
			double magnitude = Math.abs(intersection);
			Collision collision = new Collision(o.parent, direction, magnitude);
			return collision;
		} else {
			// No collision ocurred
			return null;
		}
	}

}
