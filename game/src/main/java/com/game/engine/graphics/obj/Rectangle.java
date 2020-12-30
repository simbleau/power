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
	public void alloc(GL2 gl) {
		// TODO Allocate retained OpenGL memory
	}

	@Override
	public void flagGLRefresh() {
		// TODO Flag this for an OpenGL refresh
	}

	@Override
	public boolean needsGLRefresh() {
		// TODO Determine if we need to refresh retained OpenGL memory
		return false;
	}

	@Override
	public void refresh(GL2 gl) {
		// TODO Refresh retained OpenGL memory
	}

	@Override
	public void dispose(GL2 gl) {
		// TODO Dispose of retained OpenGL memory
	}

	/**
	 * Helper algorithm to plot a rectangle.
	 *
	 * @param processor - the CPU processor
	 * @param x0        - starting x co-ordinate
	 * @param y0        - starting y co-ordinate
	 * @param x1        - ending x co-ordinate
	 * @param y1        - ending y co-ordinate
	 */
	private void plotRect(CPUProcessor processor, int x0, int y0, int x1, int y1) {
		// Plot first pixel
		processor.setPixel(x0, y0, this.argb);

		// If the width & height are <= 1, it's just a pixel, so return
		if (x0 == x1 && y0 == y1) {
			return;
		}

		// Draw
		int sx = x0 < x1 ? 1 : -1;
		int sy = y0 < y1 ? 1 : -1;

		// Vertical bars
		int y = y0;
		while (true) {
			if (y == y1) {
				break;
			}
			y += sy;
			processor.setPixel(x0, y, this.argb);
			processor.setPixel(x1, y, this.argb);
		}
		// Horizontal bars
		int x = x0;
		while (true) {
			if (x == x1) {
				break;
			}
			x += sx;
			processor.setPixel(x, y0, this.argb);
			processor.setPixel(x, y1, this.argb);
		}
	}

	@Override
	public void draw(CPUProcessor processor, int x, int y, double sx, double sy) {
		int sw = (int) (this.width * sx);
		int sh = (int) (this.height * sy);
		plotRect(processor, x, y, Math.max(x, x + sw - 1), Math.max(y, y + sh - 1));
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
