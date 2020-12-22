package com.game.engine.rendering.opengl;

import com.game.engine.game.AbstractGameObject;
import com.game.engine.game.AbstractPlane;
import com.game.engine.logger.PowerLogger;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

/**
 * An OpenGL Event Listener which loads and trashes objects which take up VRAM
 * for an {@link AbstractPlane} by initializing objects on {@link JOGLCanvas}
 * initialization and disposing of objects on {@link JOGLCanvas} disposal.
 * 
 * 
 * @author Spencer Imbleau
 * @version December 2020
 */
public class JOGLPlaneMemoryListener implements GLEventListener {

	/**
	 * The plane for this listener.
	 */
	AbstractPlane plane;

	/**
	 * Initialize a listener for a plane which helps load and remove objects in
	 * OpenGL.
	 * 
	 * @param plane - a plane
	 */
	public JOGLPlaneMemoryListener(AbstractPlane plane) {
		this.plane = plane;
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		// Allocate VRAM for all objects
		PowerLogger.LOGGER.finest("Allocating VRAM of all objects from " + this.plane.getClass().getSimpleName());
		for (AbstractGameObject obj : this.plane.objects()) {
			obj.alloc(gl);
		}
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		// Dispose VRAM for all objects
		PowerLogger.LOGGER.finest("Disposing VRAM of all objects from " + this.plane.getClass().getSimpleName());
		for (AbstractGameObject obj : this.plane.objects()) {
			obj.dispose(gl);
		}
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		// Do nothing
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		// Do nothing
	}

}
