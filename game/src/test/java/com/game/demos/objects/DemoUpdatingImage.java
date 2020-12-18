package com.game.demos.objects;

import java.nio.IntBuffer;
import java.nio.file.Paths;
import java.util.Random;

import com.game.engine.driver.GameDriver;
import com.game.engine.game.AbstractGameObject;
import com.game.engine.game.AbstractPlane;
import com.game.engine.graphics.common.RenderRequest;
import com.game.engine.graphics.obj.Image;
import com.game.engine.rendering.common.AbstractRenderer;
import com.game.engine.rendering.common.RenderLevel;
import com.jogamp.opengl.GL2;

/**
 * A mock demo updating image
 * 
 * @author Spencer Imbleau
 * @version December 2020
 */
public class DemoUpdatingImage extends AbstractGameObject {

	/**
	 * The speed of the object
	 */
	protected static int SPEED = 5;

	/**
	 * Some good ol' RNG.
	 */
	protected static Random rng = new Random();

	/**
	 * The drawable graphic object.
	 */
	protected Image drawable;

	/**
	 * Construct a demo image
	 * 
	 * @param size - the size for the drawable
	 */
	public DemoUpdatingImage(int size) {
		this.width = size;
		this.height = size;
		this.drawable = null;
	}

	@Override
	public void init(GameDriver driver) {
		Image img = driver.cache.fetch(Paths.get("src", "test", "resources", "pumpkin.png").toString());
		float sx = (float) this.width / img.getWidth();
		float sy = (float) this.height / img.getHeight();
		this.drawable = img.resize(sx, sy);
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
		AbstractPlane parent = driver.game.getPlane();

		double dx = SPEED * rng.nextDouble() * ((rng.nextBoolean()) ? 1 : -1);
		double dy = SPEED * rng.nextDouble() * ((rng.nextBoolean()) ? 1 : -1);

		if (dx + dy != 0) {
			this.move(driver, Math.max(0, Math.min(this.x() + dx, parent.width - this.width)),
					Math.max(0, Math.min(this.y() + dy, parent.height - this.height)));
		}

		IntBuffer pbo = this.drawable.getPBO();
		while (pbo.hasRemaining()) {
			pbo.mark();
			int color = pbo.get();
			pbo.reset();
			pbo.put(color + 0x00000001);
		}
		pbo.flip();
		this.drawable.flagGLRefresh();
	}

	@Override
	public void stage(GameDriver driver, AbstractRenderer renderer) {
		RenderRequest request = this.drawable.asRequest(RenderLevel.WORLD_OBJECTS, (int) this.x(), (int) this.y());
		renderer.stage(request);
	}

}
