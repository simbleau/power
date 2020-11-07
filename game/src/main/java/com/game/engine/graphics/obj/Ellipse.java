package com.game.engine.graphics.obj;

import com.game.engine.coordinates.CoordinateMatrix;
import com.game.engine.graphics.common.Drawable;
import com.game.engine.graphics.common.RenderRequest;
import com.game.engine.graphics.request.EllipseRequest;
import com.game.engine.rendering.common.RenderLevel;
import com.game.engine.rendering.cpu.CPUProcessor;
import com.game.engine.rendering.opengl.JOGLProcessor;
import com.jogamp.opengl.GL2;

/**
 * An graphics object which contains information describing an ellipse
 *
 * @version July 2020
 * @author Spencer Imbleau
 */
public class Ellipse implements Drawable {

	/**
	 * The x-radius
	 */
	private int rx;

	/**
	 * The y-radius
	 */
	private int ry;

	/**
	 * The color of the ellipse
	 */
	private int argb;

	/**
	 * Initialize an ellipse
	 *
	 * @param rx   - the x-radius
	 * @param ry   - the y-radius
	 * @param argb - the argb color of the ellipse
	 */
	public Ellipse(int rx, int ry, int argb) {
		this.rx = rx;
		this.ry = ry;
		this.argb = argb;
	}

	/**
	 * @return the x-radius
	 */
	public int getRx() {
		return this.rx;
	}

	/**
	 * @param rx - a new x-radius
	 */
	public void setRx(int rx) {
		this.rx = rx;
	}

	/**
	 * @return the y-radius
	 */
	public int getRy() {
		return this.ry;
	}

	/**
	 * @param ry - a new y-radius
	 */
	public void setRy(int ry) {
		this.ry = ry;
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
		// TODO
	}

	@Override
	public void draw(JOGLProcessor processor, GL2 gl, double cx, double cy, double sx, double sy) {
		gl.glBegin(GL2.GL_LINE_LOOP);
		float a = (this.argb >> 24 & 0xff) / 255f;
		float r = (this.argb >> 16 & 0xff) / 255f;
		float g = (this.argb >> 8 & 0xff) / 255f;
		float b = (this.argb & 0xff) / 255f;
		gl.glColor4f(r, g, b, a);

		// Initialize point which is rotated to create ellipse
		CoordinateMatrix init = CoordinateMatrix.create(this.rx, this.ry).scale(sx, sy);

		// An optimized, estimate formula for determining circumference of the
		// ellipse
		int circumference = (int) (2 * Math.PI * Math.sqrt(((init.x() * init.x()) + (init.y() * init.y())) / 2));
		// Determine amount of verticies for optimized fidelity
		int verticies = Math.max(20, circumference / 20);
		// Arclength between verticies, in radians
		float vertexRadian = (float) (2 * Math.PI / verticies);
		for (int i = 0; i < verticies; i++) {
			float theta = vertexRadian * i;
			CoordinateMatrix xy = init.scale((float) Math.cos(theta), (float) Math.sin(theta)).translate(cx, cy);
			gl.glVertex2d(xy.x(), xy.y());
		}
		gl.glEnd();
	}

	@Override
	public RenderRequest asRequest(RenderLevel level, int cx, int cy) {
		return new EllipseRequest(this, level, cx, cy);
	}

	@Override
	public RenderRequest asRequest(RenderLevel level, int depth, int cx, int cy) {
		return new EllipseRequest(this, level, depth, cx, cy);
	}

}
