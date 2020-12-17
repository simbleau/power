package com.game.demos.objects;

import java.util.Random;

import com.game.engine.driver.GameDriver;
import com.game.engine.game.AbstractGameObject;
import com.game.engine.game.AbstractPlane;
import com.game.engine.graphics.common.Drawable;
import com.game.engine.graphics.common.RenderRequest;
import com.game.engine.graphics.obj.Line;
import com.game.engine.rendering.common.AbstractRenderer;
import com.game.engine.rendering.common.RenderLevel;
import com.jogamp.opengl.GL2;

/**
 * A mock demo line
 * 
 * @author Spencer Imbleau
 * @version December 2020
 */
public class DemoLine extends AbstractGameObject {

	/**
	 * The speed of the object
	 */
	private static int SPEED = 5;

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
		this.width = size;
		this.height = size;
		this.drawable = null;
	}

	@Override
	public void init(GameDriver driver) {
		this.drawable = new Line(this.width, this.height, 0xff000000 | rng.nextInt(0xffffff));
	}

	@Override
	public void alloc(GL2 gl) {
		this.drawable.alloc(gl);
	}

	@Override
	public void refresh(GL2 gl) {
		// Do nothing
	}

	@Override
	public void dispose(GL2 gl) {
		this.drawable.dispose(gl);
	}

	@Override
	public void update(GameDriver driver) {
		AbstractPlane parent = driver.game.getPlane();

		double dx = SPEED * rng.nextDouble() * ((rng.nextBoolean()) ? 1 : -1);
		double dy = SPEED * rng.nextDouble() * ((rng.nextBoolean()) ? 1 : -1);

		if (dx + dy != 0) {
			this.move(driver, Math.max(0, Math.min(this.x() + dx, parent.width - this.width)),
					Math.max(0, Math.min(this.y() + dy, parent.height - this.height)));
		}
	}

	@Override
	public void stage(GameDriver driver, AbstractRenderer renderer) {
		RenderRequest request = this.drawable.asRequest(RenderLevel.WORLD_OBJECTS, (int) this.x(), (int) this.y());
		renderer.stage(request);
	}

}
