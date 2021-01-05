package com.game.demos.artifacts;

import java.util.Random;

import com.game.engine.driver.GameDriver;
import com.game.engine.game.AbstractGameObject;
import com.game.engine.game.AbstractMotionGameObject;
import com.game.engine.graphics.common.Drawable;
import com.game.engine.graphics.common.RenderRequest;
import com.game.engine.graphics.obj.Line;
import com.game.engine.physics2D.Collision;
import com.game.engine.rendering.common.AbstractRenderer;
import com.game.engine.rendering.common.RenderLevel;
import com.jogamp.opengl.GL2;

/**
 * A mock demo line
 * 
 * @author Spencer Imbleau
 * @version December 2020
 */
public class DemoLine extends AbstractMotionGameObject {

	/**
	 * A default speed of the object
	 */
	private static double DEFAULT_SPEED = 0.05d;

	/**
	 * Some good ol' RNG.
	 */
	private static Random rng = new Random();

	/**
	 * The drawable graphic object.
	 */
	private Drawable drawable;

	/**
	 * Construct a demo line
	 *
	 * @param size - the size for the drawable
	 */
	public DemoLine(int size) {
		this.speed = DEFAULT_SPEED;
		this.direction = rng.nextDouble() * (2 * Math.PI);
		this.width = size;
		this.height = size;
		this.drawable = null;
	}

	@Override
	public void init(GameDriver driver) {
		int dx = this.width - 1; // -1 for the origin
		int dy = this.height - 1; // -1 for the origin
		this.drawable = new Line(dx, dy, 0xff000000 | rng.nextInt(0xffffff));
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
				this.turnCCW(Math.PI / driver.settings.getTicksPerSecond());
			} else {
				this.turnCW(Math.PI / driver.settings.getTicksPerSecond());
			}
		}
	}

	@Override
	public void stage(GameDriver driver, AbstractRenderer renderer) {
		RenderRequest request = this.drawable.asRequest(RenderLevel.WORLD_OBJECTS, this.position.x.asInt(),
				this.position.y.asInt());
		renderer.stage(request);
	}

}
