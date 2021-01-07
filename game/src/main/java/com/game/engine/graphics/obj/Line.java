package com.game.engine.graphics.obj;

import com.game.engine.graphics.common.Drawable;
import com.game.engine.graphics.common.RenderRequest;
import com.game.engine.graphics.request.LineRequest;
import com.game.engine.rendering.common.RenderLevel;
import com.game.engine.rendering.cpu.CPUProcessor;
import com.game.engine.rendering.opengl.JOGLProcessor;
import com.jogamp.opengl.GL2;

/**
 * An graphics object which contains information describing a line
 *
 * @version June 2020
 * @author Spencer Imbleau
 */
public class Line implements Drawable {

	/**
	 * The delta y for this line
	 */
	private int dx;

	/**
	 * The delta y for this line
	 */
	private int dy;

	/**
	 * The length of this line
	 */
	private double length;

	/**
	 * The color of the box, in ARGB format
	 */
	private int argb;

	/**
	 * Initializes a line
	 *
	 * @param dx   - the run of this line
	 * @param dy   - the rise of this line
	 * @param argb - the color (ARGB format) of the line
	 */
	public Line(int dx, int dy, int argb) {
		this.dx = dx;
		this.dy = dy;
		this.length = Math.sqrt(dx * dx + dy * dy);
		this.argb = argb;
	}

	/**
	 * @return the run magnitude of the line
	 */
	public int getDx() {
		return this.dx;
	}

	/**
	 * Set the run magnitude of the line
	 *
	 * @param dx - the new x length
	 */
	public void setDx(int dx) {
		this.dx = dx;
		this.length = Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * @return the rise magnitude of the line
	 */
	public int getDy() {
		return this.dy;
	}

	/**
	 * Set the rise magnitude of the line
	 *
	 * @param dy - the new delta-y length
	 */
	public void setDy(int dy) {
		this.dy = dy;
		this.length = Math.sqrt(dx * dx + dy * dy);
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

	/**
	 * @return the length of the line
	 */
	public double getLength() {
		return length;
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
	 * Helper method to calculate the alpha of a pixel with a given calculated error
	 * from the line.
	 *
	 * @param error - the error from the line
	 * @return the color of a pixel given the error from its line
	 */
	int aaColor(float error) {
		int alpha = 0xff - (int) (0xff * error);
		int a = this.argb >> 24 & alpha;
		int rgb = this.argb & 0x00ffffff;
		return (int) a << 24 | rgb;
	}

	/**
	 * Helper algorithm to plot a line using the bresenham algorithm. Found in
	 * chapter 1.7 of <i>A Rasterizing Algorithm for Drawing Curves</i> (Wein,
	 * 2012).
	 *
	 * @param processor - the CPU processor
	 * @param x0        - starting x co-ordinate
	 * @param y0        - starting y co-ordinate
	 * @param x1        - ending x co-ordinate
	 * @param y1        - ending y co-ordinate
	 * @see <a href="http://members.chello.at/easyfilter/bresenham.pdf">A
	 *      Rasterizing Algorithm for Drawing Curves</a>
	 */
	private void plotLine(CPUProcessor processor, int x0, int y0, int x1, int y1) {
		int dx = Math.abs(x1 - x0), sx = x0 < x1 ? 1 : -1;
		int dy = -Math.abs(y1 - y0), sy = y0 < y1 ? 1 : -1;
		int err = dx + dy, e2; // error value e_xy
		while (true) {
			processor.setPixel(x0, y0, this.argb);
			e2 = 2 * err;
			if (e2 >= dy) { // e_xy+e_x > 0
				if (x0 == x1)
					break;
				err += dy;
				x0 += sx;
			}
			if (e2 <= dx) { // e_xy+e_y < 0
				if (y0 == y1)
					break;
				err += dx;
				y0 += sy;
			}
		}
	}

	/**
	 * Helper algorithm to plot a line using the bresenham algorithm and
	 * anti-aliasing. Found in chapter 7.1 of <i>A Rasterizing Algorithm for Drawing
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
	void plotLineAA(CPUProcessor processor, int x0, int y0, int x1, int y1) {
		int dx = Math.abs(x1 - x0), sx = x0 < x1 ? 1 : -1;
		int dy = -Math.abs(y1 - y0), sy = y0 < y1 ? 1 : -1;
		int err = dx + dy, e2, x2; // error value e_xy
		float ed = (dx - dy == 0) ? 1 : (float) Math.sqrt(dx * dx + dy * dy);

		while (true) { /* pixel loop */
			processor.setPixel(x0, y0, aaColor(Math.abs(err - dx - dy) / ed));
			e2 = err;
			x2 = x0;
			if (2 * e2 + dx >= 0) { /* x step */
				if (x0 == x1) {
					break;
				}
				if (e2 - dy < ed) {
					processor.setPixel(x0, y0 + sy, aaColor((e2 - dy) / ed));
				}
				err += dy;
				x0 += sx;
			}
			if (2 * e2 + dy <= 0) { /* y step */
				if (y0 == y1) {
					break;
				}
				if (dx - e2 < ed) {
					processor.setPixel(x2 + sx, y0 + sy, aaColor((dx - e2) / ed));
				}
				err += dx;
				y0 += sy;
			}
		}
	}

	@Override
	public void draw(CPUProcessor processor, int x, int y, double sx, double sy) {
		int sdx = (int) (this.dx * sx); // scaled dx
		int sdy = (int) (this.dy * sy); // scaled dy
		plotLine(processor, x, y, x + sdx, y + sdy);
	}

	@Override
	public void draw(JOGLProcessor processor, GL2 gl, double x, double y, double sx, double sy) {
		gl.glBegin(GL2.GL_LINES);
		float a = (this.argb >> 24 & 0xff) / 255f;
		float r = (this.argb >> 16 & 0xff) / 255f;
		float g = (this.argb >> 8 & 0xff) / 255f;
		float b = (this.argb & 0xff) / 255f;
		gl.glColor4f(r, g, b, a);
		gl.glVertex2d(x, y);
		gl.glVertex2d(x + this.dx * sx, y + this.dy * sy);
		gl.glEnd();
	}

	@Override
	public LineRequest asRequest(RenderLevel level, int x, int y) {
		return new LineRequest(this, level, x, y);
	}

	@Override
	public RenderRequest asRequest(RenderLevel level, int depth, int x, int y) {
		return new LineRequest(this, level, depth, x, y);
	}

}
