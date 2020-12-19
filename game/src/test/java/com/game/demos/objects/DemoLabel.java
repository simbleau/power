package com.game.demos.objects;

import java.util.Random;

import com.game.engine.driver.GameDriver;
import com.game.engine.game.AbstractGameObject;
import com.game.engine.game.AbstractPlane;
import com.game.engine.graphics.common.Drawable;
import com.game.engine.graphics.common.RenderRequest;
import com.game.engine.graphics.obj.Label;
import com.game.engine.graphics.obj.fonts.Font;
import com.game.engine.graphics.obj.fonts.mock.MockFonts;
import com.game.engine.logger.PowerLogger;
import com.game.engine.rendering.common.AbstractRenderer;
import com.game.engine.rendering.common.RenderLevel;
import com.jogamp.opengl.GL2;

/**
 * A mock demo label
 * 
 * @author Spencer Imbleau
 * @version December 2020
 */
public class DemoLabel extends AbstractGameObject {

	/**
	 * A demo font.
	 */
	private static final Font TEST_FONT = MockFonts.FONT_1;

	/**
	 * Some demo text to render.
	 */
	private static final String TEST_TEXT = "Test123!";

	/**
	 * A color to render the text.
	 */
	private static final int TEST_ARGB = 0xffffffff; // Solid yellow
	
	/**
	 * The speed of the object
	 */
	private static int SPEED = 0;

	/**
	 * Some good ol' RNG.
	 */
	private static Random rng = new Random();

	/**
	 * The drawable graphic object.
	 */
	private Drawable drawable;
	
	/**
	 * Construct a demo ellipse
	 * 
	 * @param size - the size for the drawable
	 */
	public DemoLabel(int size) {
		this.width = size;
		this.height = size;
		this.drawable = null;
	}

	@Override
	public void init(GameDriver driver) {
		Label label = new Label(TEST_FONT, TEST_TEXT, TEST_ARGB);
		if (label.getWidth() > label.getHeight()) {
			PowerLogger.LOGGER.info("wider");
			float sx = (float) this.width / label.getWidth();
			this.drawable = label.resize(sx, sx);
			this.height = (int) (sx * label.getHeight());
		} else {
			PowerLogger.LOGGER.info("taller");
			float sy = (float) this.height / label.getHeight();
			this.drawable = label.resize(sy, sy);
			this.width = (int) (sy * label.getWidth());
		}
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
	}

	@Override
	public void stage(GameDriver driver, AbstractRenderer renderer) {
		RenderRequest request = this.drawable.asRequest(RenderLevel.WORLD_OBJECTS, (int) this.x(), (int) this.y());
		renderer.stage(request);
	}

}