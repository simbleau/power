package com.game.engine.graphics.obj;

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
 * @version December 2020
 * @author Spencer Imbleau
 */
public class Ellipse implements Drawable {

	/**
	 * The acceptable error for circle arclength accuracy.
	 */
	private static final double ACCEPTABLE_ERROR = 0.33;

	/**
	 * The x-radius.
	 */
	private int rx;

	/**
	 * The y-radius.
	 */
	private int ry;

	/**
	 * The color of the ellipse.
	 */
	private int argb;

	/**
	 * Initialize an ellipse.
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

	@Override
	public void draw(CPUProcessor processor, int x, int y, double sx, double sy) {
		// The scaled radii
		double srx = this.rx * sx;
		double sry = this.ry * sy;

		// Do not draw objects with a radius of 0
		if (srx < 1 || sry < 1) {
			return;
		}

		// Calculate the amount of vertices needed using the delta angle between
		// adjacent vertices
		double da = Math.acos(2 * (1 - ACCEPTABLE_ERROR / srx) * (1 - ACCEPTABLE_ERROR / sry) - 1);
		int vertices = (int) Math.ceil(2 * Math.PI / da);

		// Draw lines between all vertices
		int xi = (int) Math.round(srx * Math.cos(0) + x + srx);
		int yi = (int) Math.round(sry * Math.sin(0) + y + sry);
		double vertexRadian = 2 * Math.PI / vertices;
		for (int i = 1; i <= vertices; i++) {
			double theta = vertexRadian * i;
			int xi2 = (int) Math.round(srx * Math.cos(theta) + x + srx);
			int yi2 = (int) Math.round(sry * Math.sin(theta) + y + sry);
			int dx = xi2 - xi;
			int dy = yi2 - yi;
			Line l = new Line(dx, dy, this.argb);
			l.draw(processor, xi, yi, 1, 1);
			xi = xi2;
			yi = yi2;
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

		// The scaled radii
		double srx = this.rx * sx;
		double sry = this.ry * sy;

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
