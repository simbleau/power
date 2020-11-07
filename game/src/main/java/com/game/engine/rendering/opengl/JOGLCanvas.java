package com.game.engine.rendering.opengl;

import java.util.Iterator;

import com.game.engine.graphics.common.RenderRequest;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;

/**
 * A canvas handled by the {@link JOGLRenderer} for OpenGL rendering
 *
 * @author Spencer Imbleau
 * @version June 2020
 * @see JOGLRenderer
 */
@SuppressWarnings("serial")
public class JOGLCanvas extends GLCanvas implements GLEventListener {

	/**
	 * The graphic processor for this canvas.
	 */
	private JOGLProcessor processor;

	/**
	 * Initialize an OpenGL canvas.
	 *
	 * @param capabilities - the GL capabilities for the canvas
	 * @param processor    - the processor used to generate graphics
	 */
	public JOGLCanvas(GLCapabilities capabilities, JOGLProcessor processor) {
		super(capabilities);
		this.addGLEventListener(this);
		this.processor = processor;
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		// Disable lighting
		gl.glDisable(GL2.GL_LIGHTING);
		// Clear color is black
		gl.glClearColor(0, 0, 0, 1);
		// This disables vsync
		gl.setSwapInterval(0);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// Do nothing
	}

	@Override
	public synchronized void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		// Clear screen
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

		// Draw render requests
		Iterator<RenderRequest> renderIterator = this.processor.iterator();
		while (renderIterator.hasNext()) {
			RenderRequest request = renderIterator.next();
			switch (request.level) {
			case UI_PLUGIN:
			case UI_OVERLAY:
			case UI:
				this.processor.drawUI(gl, request);
				break;
			default:
				this.processor.draw(gl, request);
				break;
			}
		}

		// Clear requests
		this.processor.reset();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(0, width, height, 0, -1, 1);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
	}

}
