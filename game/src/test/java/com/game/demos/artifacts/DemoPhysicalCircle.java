package com.game.demos.artifacts;

import java.util.Random;

import com.game.engine.driver.GameDriver;
import com.game.engine.game.AbstractGameObject;
import com.game.engine.game.AbstractMotionGameObject;
import com.game.engine.game.Position2D;
import com.game.engine.graphics.common.Drawable;
import com.game.engine.graphics.common.RenderRequest;
import com.game.engine.graphics.obj.Ellipse;
import com.game.engine.logger.PowerLogger;
import com.game.engine.maths.Matrix;
import com.game.engine.physics2D.Collision;
import com.game.engine.physics2D.PhysicsComponent;
import com.game.engine.physics2D.common.Collidable;
import com.game.engine.physics2D.sat.SATCircle;
import com.game.engine.rendering.common.AbstractRenderer;
import com.game.engine.rendering.common.RenderLevel;
import com.jogamp.opengl.GL2;

public class DemoPhysicalCircle extends AbstractMotionGameObject {

	/**
	 * A default speed of the object
	 */
	private static double DEFAULT_SPEED = 0.5d;

	/**
	 * Some good ol' RNG.
	 */
	private static Random rng = new Random();

	/**
	 * The drawable graphic object.
	 */
	private Drawable drawable;

	/**
	 * The size of this circle.
	 */
	private int size;

	/**
	 * The physical body.
	 */
	private SATCircle body;

	/**
	 * Construct a demo ellipse
	 * 
	 * @param size - the size for the drawable
	 */
	public DemoPhysicalCircle(int size) {
		this.speed = DEFAULT_SPEED;
		this.direction = rng.nextDouble() * (2 * Math.PI);
		this.size = size;
		this.width = size;
		this.height = size;
		this.drawable = null;
		this.body = null;
	}

	@Override
	public DemoPhysicalCircle clone() throws CloneNotSupportedException {
		DemoPhysicalCircle clone = new DemoPhysicalCircle(this.size);
		return clone;
	}

	@Override
	public void init(GameDriver driver) {
		// Initialize renderables
		this.drawable = new Ellipse(width, height, 0xff000000 | rng.nextInt(0xffffff));

		// Initialize physics
		PhysicsComponent physics = new PhysicsComponent(this);
		this.body = new SATCircle(this, this.size / 2, this.width / 2, this.height / 2);
		physics.collidables.add(body);
		physics.responders.add(this::handle);
		this.setPhysics(physics);
	}

	@Override
	public void alloc(GL2 gl) {
		this.drawable.alloc(gl);
	}

	@Override
	public void refresh(GL2 gl) {
		this.drawable.refresh(gl);
	}

	@Override
	public void dispose(GL2 gl) {
		this.drawable.dispose(gl);
	}

	@Override
	public void update(GameDriver driver) {
		super.update(driver);
		if (this.speed != 0) {
			if (rng.nextBoolean()) {
				this.turnCCW(2 * Math.PI / driver.settings.getTicksPerSecond());
			} else {
				this.turnCW(2 * Math.PI / driver.settings.getTicksPerSecond());
			}
		}
	}

	@Override
	public void stage(GameDriver driver, AbstractRenderer renderer) {
		// Render the drawable
		RenderRequest request = this.drawable.asRequest(RenderLevel.WORLD_OBJECTS, this.position.x.asInt(),
				this.position.y.asInt());
		renderer.stage(request);

		// Render the physical body for debugging
		this.body.stage(driver, renderer);
	}

	private double reverseDirection(double dir) {
		if (dir < 0) {
			return dir + Math.PI;
		} else {
			return dir - Math.PI;
		}
	}

	/**
	 * Handle collision events for this object.
	 *
	 * @param collision - a collision
	 */
	public void handle(Collision collision) {
		PowerLogger.LOGGER.fine(this.getClass().getSimpleName() + " received a collision from "
				+ collision.force.getClass().getSimpleName());
		PowerLogger.LOGGER
				.finer("\tDirection: " + collision.direction + "\\" + Math.toDegrees(collision.direction) + "°");

		double dx = this.position.x() - collision.force.position.x();
		double dy = this.position.y() - collision.force.position.y();
		double radiiSum = (this.size / 2) * 2;
		// let radii_sum = c1.radius + c2.radius;
		double length = Math.max(1, Math.sqrt(dx * dx + dy * dy));
		double unitX = dx / length;
		double unitY = dy / length;

		this.position.set(
				collision.force.position.x() + (radiiSum + 1) * unitX,
				collision.force.position.y() + (radiiSum + 1) * unitY);
//
//	        c1.x = c2.x + (radii_sum + 1) * unit_x;
//	        c1.y = c2.y + (radii_sum + 1) * unit_y;
//
//		double velocityX = Math.cos(this.direction) * this.speed;
//		double velocityY = Math.sin(this.direction) * this.speed;

//		double vx = this.d
//	
//		
//		double normalx = dy;
//		double normaly = -dx;

		PowerLogger.LOGGER.finer("\tMagnitude: " + collision.magnitude);
	}

}
