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
	public void draw(CPUProcessor processor, int x, int y, double sx, double sy) {
		int dx = (int) (this.dx * sx);
		int dy = (int) (this.dy * sy);

		if (dx == 0 && dy == 0) {
			processor.setPixel(x, y, this.argb);
			return;
		}

		int xDirection = dx > 0 ? 1 : -1;
		int yDirection = dy > 0 ? 1 : -1;

		int err = Math.abs(dx) - Math.abs(dy);
		int err2;

		int dxi = 0;
		int dyi = 0;
		while (Math.abs(dxi) <= Math.max(1, Math.abs(dx)) && Math.abs(dyi) <= Math.max(1, Math.abs(dy))) {
			int nextX = x + dxi;
			int nextY = y + dyi;

			// Set pixel
			processor.setPixel(nextX, nextY, this.argb);

			// Move towards the end of our line
			// (Uses int operations which are cheaper computationally)
			err2 = err * 2;

			if (err2 > -1 * Math.abs(dy)) {
				err -= Math.abs(dy);
				dxi += xDirection;
			}

			if (err2 < Math.abs(dx)) {
				err += Math.abs(dx);
				dyi += yDirection;
			}
		}
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
