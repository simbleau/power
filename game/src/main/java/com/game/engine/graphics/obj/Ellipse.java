package com.game.engine.graphics.obj;

import com.game.engine.graphics.common.Drawable;
import com.game.engine.graphics.common.RenderRequest;
import com.game.engine.graphics.request.EllipseRequest;
import com.game.engine.rendering.common.RenderLevel;
import com.game.engine.rendering.cpu.CPUProcessor;
import com.game.engine.rendering.opengl.JOGLProcessor;
import com.jogamp.opengl.GL2;

/**
 * An graphics object which contains information describing an ellipse inside a
 * rectangle.
 *
 * @version December 2020
 * @author Spencer Imbleau
 */
public class Ellipse implements Drawable {

	/**
	 * The acceptable error for circle arclength accuracy.
	 */
	private static final double ACCEPTABLE_ERROR = 0.33;

	/**
	 * The width of the ellipse.
	 */
	private int width;

	/**
	 * The height of the ellipse.
	 */
	private int height;

	/**
	 * The color of the ellipse.
	 */
	private int argb;

	/**
	 * Initialize an ellipse.
	 *
	 * @param width  - the width
	 * @param height - the height
	 * @param argb   - the argb color of the ellipse
	 */
	public Ellipse(int width, int height, int argb) {
		this.width = width;
		this.height = height;
		this.argb = argb;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * @param width - a new width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * @param height - a new height
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
	 * Helper algorithm to plot a ellipse bounded by a rectangle using the bresenham
	 * algorithm. Found in chapter 2.5 of <i>A Rasterizing Algorithm for Drawing
	 * Curves</i> (Wein, 2012).
	 *
	 * @param processor - the CPU processor
	 * @param x0        - starting x co-ordinate
	 * @param y0        - starting y co-ordinate
	 * @param x1        - ending x co-ordinate
	 * @param y1        - ending y co-ordinate
	 * @see <a href="http://members.chello.at/easyfilter/bresenham.pdf">A
	 *      Rasterizing Algorithm for Drawing Curves</a>
	 */
	private void plotEllipseRect(CPUProcessor processor, int x0, int y0, int x1, int y1) {
		int a = Math.abs(x1 - x0), b = Math.abs(y1 - y0), b1 = b & 1; // diameter
		double dx = 4 * (1.0 - a) * b * b, dy = 4 * (b1 + 1) * a * a; // error increment
		double err = dx + dy + b1 * a * a, e2; // error of 1.step
		if (x0 > x1) {
			x0 = x1;
			x1 += a;
		} // if called with swapped points
		if (y0 > y1) { // exchange them
			y0 = y1;
		}
		y0 += (b + 1) / 2;
		y1 = y0 - b1; // starting pixel
		a = 8 * a * a;
		b1 = 8 * b * b;
		do {
			processor.setPixel(x1, y0, this.argb); // I. Quadrant
			processor.setPixel(x0, y0, this.argb); // II. Quadrant
			processor.setPixel(x0, y1, this.argb); // III. Quadrant
			processor.setPixel(x1, y1, this.argb); // IV. Quadrant
			e2 = 2 * err;
			if (e2 <= dy) {
				y0++;
				y1--;
				err += dy += a;
			} // y step
			if (e2 >= dx || 2 * err > dy) {
				x0++;
				x1--;
				err += dx += b1;
			} // x
		} while (x0 <= x1);
		while (y0 - y1 <= b) { // to early stop of flat ellipses a=1
			processor.setPixel(x0 - 1, y0, this.argb); // finish tip of ellipse
			processor.setPixel(x1 + 1, y0++, this.argb);
			processor.setPixel(x0 - 1, y1, this.argb);
			processor.setPixel(x1 + 1, y1--, this.argb);
		}
	}

	@Override
	public void draw(CPUProcessor processor, int x, int y, double sx, double sy) {
		// The scaled dimensions
		int sw = (int) (this.width * sx);
		int sh = (int) (this.height * sy);
		plotEllipseRect(processor, x, y, Math.max(x, x + sw - 1), Math.max(y, y + sh - 1));
	}

	@Override
	public void draw(JOGLProcessor processor, GL2 gl, double x, double y, double sx, double sy) {
		gl.glBegin(GL2.GL_LINE_LOOP);
		float a = (this.argb >> 24 & 0xff) / 255f;
		float r = (this.argb >> 16 & 0xff) / 255f;
		float g = (this.argb >> 8 & 0xff) / 255f;
		float b = (this.argb & 0xff) / 255f;
		gl.glColor4f(r, g, b, a);

		// The scaled radii
		double srx = (this.width / 2d) * sx;
		double sry = (this.height / 2d) * sy;

		// Calculate the amount of vertices needed using the delta angle between
		// adjacent vertices
		double da = Math.acos(2 * (1 - ACCEPTABLE_ERROR / srx) * (1 - ACCEPTABLE_ERROR / sry) - 1);
		int vertices = (int) Math.ceil(2 * Math.PI / da);

		// Arclength between verticies, in radians
		double vertexRadian = 2 * Math.PI / vertices;
		for (int i = 0; i < vertices; i++) {
			double theta = vertexRadian * i;
			double xi = srx * Math.cos(theta) + x + srx;
			double yi = sry * Math.sin(theta) + y + sry;
			gl.glVertex2d(xi, yi);
		}
		gl.glEnd();
	}

	@Override
	public RenderRequest asRequest(RenderLevel level, int x, int y) {
		return new EllipseRequest(this, level, x, y);
	}

	@Override
	public RenderRequest asRequest(RenderLevel level, int depth, int x, int y) {
		return new EllipseRequest(this, level, depth, x, y);
	}

}
