package com.game.engine.graphics.obj;

import com.game.engine.graphics.common.Drawable;
import com.game.engine.graphics.common.RenderRequest;
import com.game.engine.graphics.request.RectangleRequest;
import com.game.engine.rendering.common.RenderLevel;
import com.game.engine.rendering.cpu.CPUProcessor;
import com.game.engine.rendering.opengl.JOGLProcessor;
import com.jogamp.opengl.GL2;

/**
 * An graphics object which contains information describing a rectangle
 *
 * @version June 2020
 * @author Spencer Imbleau
 */
public class Rectangle implements Drawable {

	/**
	 * The width of the rectangle
	 */
	private int width;

	/**
	 * The height of the rectangle
	 */
	private int height;

	/**
	 * The ARGB color of the rectangle
	 */
	private int argb;

	/**
	 * Initializes a rectangle
	 *
	 * @param width  - width of the rectangle
	 * @param height - height of the rectangle
	 * @param argb   - the ARGB color of the rectangle
	 */
	public Rectangle(int width, int height, int argb) {
		this.width = width;
		this.height = height;
		this.argb = argb;
	}

	/**
	 * @return the width of the rectangle
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * Set a new width for the rectangle
	 *
	 * @param width - the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height of the rectangle
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * Set a new height for the rectangle
	 *
	 * @param height - the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return the ARGB color of the rectangle
	 */
	public int getColor() {
		return this.argb;
	}

	/**
	 * Set the color of the rectangle
	 * 
	 * @param argb - the ARGB color to set
	 */
	public void setColor(int argb) {
		this.argb = argb;
	}

	@Override
	public void draw(CPUProcessor processor, int x, int y, double sx, double sy) {
		int width = (int) (this.width * sx);
		int height = (int) (this.height * sy);

		// Don't render off-screen rectangles
		if (x < -width)
			return;
		if (x > processor.getImage().getWidth())
			return;
		if (y < -height)
			return;
		if (y > processor.getImage().getHeight())
			return;

		// In the next steps we specify the bounds which are visible on screen
		int xStart = 0;
		int yStart = 0;
		if (x < 0)
			xStart -= x;
		if (y < 0)
			yStart -= y;
		if (width + x > processor.getImage().getWidth())
			width -= width + x - processor.getImage().getWidth();
		if (height + y > processor.getImage().getHeight())
			height -= height + y - processor.getImage().getHeight();

		// Draw
		for (int yi = yStart; yi < height; yi++) {
			processor.setPixel(x, y + yi, this.argb);
			processor.setPixel(x + (width - 1), y + yi, this.argb);
		}
		for (int xi = xStart; xi < width; xi++) {
			processor.setPixel(x + xi, y, this.argb);
			processor.setPixel(x + xi, y + (height - 1), this.argb);
		}
	}

	@Override
	public void draw(JOGLProcessor processor, GL2 gl, double x, double y, double sx, double sy) {
		gl.glBegin(GL2.GL_LINE_LOOP);
		float a = (this.argb >> 24 & 0xff) / 255f;
		float r = (this.argb >> 16 & 0xff) / 255f;
		float g = (this.argb >> 8 & 0xff) / 255f;
		float b = (this.argb & 0xff) / 255f;
		gl.glColor4f(r, g, b, a);
		gl.glVertex2d(x, y);
		gl.glVertex2d(x + this.width * sx, y);
		gl.glVertex2d(x + this.width * sx, y + this.height * sy);
		gl.glVertex2d(x, y + this.height * sy);
		gl.glEnd();
	}

	@Override
	public RenderRequest asRequest(RenderLevel level, int x, int y) {
		return new RectangleRequest(this, level, x, y);
	}

	@Override
	public RenderRequest asRequest(RenderLevel level, int depth, int x, int y) {
		return new RectangleRequest(this, level, depth, x, y);
	}

}
