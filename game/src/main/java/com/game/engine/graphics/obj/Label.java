package com.game.engine.graphics.obj;

import java.util.Arrays;

import com.game.engine.graphics.common.RenderRequest;
import com.game.engine.graphics.obj.fonts.Font;
import com.game.engine.graphics.obj.fonts.Glyph;
import com.game.engine.graphics.request.LabelRequest;
import com.game.engine.rendering.common.RenderLevel;
import com.game.engine.rendering.cpu.CPUProcessor;
import com.game.engine.rendering.opengl.JOGLProcessor;
import com.jogamp.opengl.GL2;

/**
 * An graphics object which contains information describing a label.
 *
 * @version June 2020
 * @author Spencer Imbleau
 */
public class Label extends Image {

	/**
	 * The font of this label.
	 */
	private Font font;

	/**
	 * The keycodes represented on this label.
	 */
	private int[] keycodes;

	/**
	 * The color of the text.
	 */
	private int argb;

	/**
	 * Initializes a label.
	 *
	 * @param font - the font for this label
	 * @param text - the text on this label
	 * @param argb - the color of the label
	 */
	public Label(Font font, String text, int argb) {
		super(font.bakeImage(text, argb));
		this.font = font;
		this.keycodes = Glyph.keycodesOf(text);
		this.argb = argb;
	}

	/**
	 * Initializes a label.
	 *
	 * @param font     - the font for this label
	 * @param keycodes - the keycodes on this label
	 * @param argb     - the color of the label
	 */
	public Label(Font font, int[] keycodes, int argb) {
		super(font.bakeImage(keycodes, argb));
		this.font = font;
		this.keycodes = keycodes;
		this.argb = argb;
	}

	/**
	 * Initializes a label with no text.
	 *
	 * @param font - the font for this label
	 * @param argb - the color of the label
	 */
	public Label(Font font, int argb) {
		this(font, "", argb);
	}

	/**
	 * Returns the text representation of the keycodes as best as possible. Some
	 * characters are not in the range of the char primitive and instead will be
	 * printed as hexadecimal.
	 * 
	 * @return the string representation of the keycodes used to draw the label
	 */
	public String getText() {
		return Arrays.stream(this.keycodes)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
				.toString();
	}

	/**
	 * @return the keycodes used to draw the label
	 */
	public int[] getKeycodes() {
		return this.keycodes;
	}

	/**
	 * Change the text on this label.
	 * 
	 * @param keycodes - an array of keycodes to bake into this label
	 */
	public void setText(int[] keycodes) {
		super.setBufferedImage(font.bakeImage(keycodes, argb));
	}

	/**
	 * Change the text on this label.
	 * 
	 * @param text - the text to set for this label
	 */
	public void setText(String text) {
		super.setBufferedImage(font.bakeImage(text, argb));
	}

	@Override
	public void alloc(GL2 gl) {
		super.alloc(gl);
	}

	@Override
	public void refresh(GL2 gl) {
		super.refresh(gl);
	}

	@Override
	public void dispose(GL2 gl) {
		super.dispose(gl);
	}

	@Override
	public void flagGLRefresh() {
		super.flagGLRefresh();
	}

	@Override
	public boolean needsGLRefresh() {
		return super.needsGLRefresh();
	}

	@Override
	public void draw(CPUProcessor processor, int x, int y, double sx, double sy) {
		super.draw(processor, x, y, sx, sy);
	}

	@Override
	public void draw(JOGLProcessor processor, GL2 gl, double x, double y, double sx, double sy) {
		super.draw(processor, gl, x, y, sx, sy);
	}

	@Override
	public RenderRequest asRequest(RenderLevel level, int x, int y) {
		return new LabelRequest(this, level, x, y);
	}

	@Override
	public RenderRequest asRequest(RenderLevel level, int depth, int x, int y) {
		return new LabelRequest(this, level, depth, x, y);
	}

}
