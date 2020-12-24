package com.game.demos.artifacts;

import java.util.Random;

import com.game.engine.driver.GameDriver;
import com.game.engine.game.AbstractMotionGameObject;
import com.game.engine.graphics.common.Drawable;
import com.game.engine.graphics.common.RenderRequest;
import com.game.engine.graphics.obj.Image;
import com.game.engine.graphics.obj.Label;
import com.game.engine.graphics.obj.fonts.Font;
import com.game.engine.graphics.obj.fonts.mock.MockFonts;
import com.game.engine.rendering.common.AbstractRenderer;
import com.game.engine.rendering.common.RenderLevel;
import com.jogamp.opengl.GL2;

/**
 * A mock demo label
 * 
 * @author Spencer Imbleau
 * @version December 2020
 */
public class DemoLabel extends AbstractMotionGameObject {

	/**
	 * A demo font.
	 */
	private static final Font TEST_FONT = MockFonts.FONT_256;

	/**
	 * Some demo text to render.
	 */
	private static final String DEFAULT_TEXT = "%Test123!";

	/**
	 * A color to render the text.
	 */
	private static final int TEST_ARGB = 0xffffffff; // Solid yellow

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
	 * The size of the text
	 */
	private int size;

	/**
	 * The text on the label
	 */
	private String text;

	/**
	 * Construct a demo label with text.
	 * 
	 * @param text - the text for this label
	 * @param size - the size for the drawable
	 */
	public DemoLabel(String text, int size) {
		this.speed = DEFAULT_SPEED;
		this.direction = rng.nextDouble() * (2 * Math.PI);
		this.text = text;
		this.size = size;
		this.width = size;
		this.height = size;
		this.drawable = null;
	}

	/**
	 * Construct a demo label with default text.
	 * 
	 * @param size - the size for the drawable
	 */
	public DemoLabel(int size) {
		this(DEFAULT_TEXT, size);
	}

	@Override
	public void init(GameDriver driver) {
		Label label = new Label(TEST_FONT, this.text, TEST_ARGB);
		if (label.getWidth() > label.getHeight()) {
			float sx = (float) this.width / label.getWidth();
			this.drawable = label.resize(sx, sx);
			this.height = (int) (sx * label.getHeight());
		} else {
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
		RenderRequest request = this.drawable.asRequest(RenderLevel.WORLD_OBJECTS, (int) this.x(), (int) this.y());
		renderer.stage(request);
	}

	/**
	 * Change the text on this label.
	 * 
	 * @param text - the text for this label
	 */
	public void setText(String text) {
		// TODO resize a label with a label return type instead of an image return type
		Label label = new Label(TEST_FONT, text, TEST_ARGB);
		if (label.getWidth() > label.getHeight()) {
			float sx = (float) this.size / label.getWidth();
			Image l2 = label.resize(sx, sx);
			this.drawable = l2;
			this.width = l2.getWidth();
			this.height = l2.getHeight();
		} else {
			float sy = (float) this.size / label.getHeight();
			Image l2 = label.resize(sy, sy);
			this.drawable = l2;
			this.width = l2.getWidth();
			this.height = l2.getHeight();
		}
	}

}