package com.game.engine.rendering.opengl;

import com.game.engine.game.AbstractChunkedPlane;
import com.game.engine.game.AbstractGameObject;
import com.game.engine.game.Chunk;
import com.game.engine.game.Chunker;
import com.game.engine.logger.PowerLogger;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

/**
 * An OpenGL Event Listener which loads and trashes objects which take up VRAM
 * for an {@link AbstractChunkedPlane}. It does this by querying a
 * {@link Chunker} attached to a {@link AbstractChunkedPlane} for any requests
 * to load or trash, and handling these requests, as well as on
 * {@link JOGLCanvas} initialization and {@link JOGLCanvas} disposal.
 * 
 * @author Spencer Imbleau
 * @version December 2020
 */
public class JOGLChunkedPlaneMemoryListener implements GLEventListener {

	/**
	 * The plane for this listener.
	 */
	protected AbstractChunkedPlane plane;

	/**
	 * Initialize a listener for a chunked plane which helps load and remove objects
	 * in OpenGL.
	 * 
	 * @param plane - a plane
	 */
	public JOGLChunkedPlaneMemoryListener(AbstractChunkedPlane plane) {
		this.plane = plane;
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		// Allocate VRAM for all objects
		PowerLogger.LOGGER
				.finest("Allocating VRAM of all viewable chunk objects from " + this.plane.getClass().getSimpleName());
		for (Chunk chunk : this.plane.chunker.viewableChunks()) {
			for (AbstractGameObject obj : chunk.objects()) {
				obj.alloc(gl);
			}
		}
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		// Dispose VRAM for all objects
		PowerLogger.LOGGER
				.finest("Disposing VRAM of all viewable chunk objects from " + this.plane.getClass().getSimpleName());
		for (Chunk chunk : this.plane.chunker.viewableChunks()) {
			for (AbstractGameObject obj : chunk.objects()) {
				obj.dispose(gl);
			}
		}
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		// Allocate VRAM for new chunks
		for (AbstractGameObject obj : this.plane.chunker.loadingObjects()) {
			this.plane.chunker.load(gl, obj);
		}

		// Destroy VRAM for trashed chunks
		for (AbstractGameObject obj : this.plane.chunker.trashedObjects()) {
			this.plane.chunker.trash(gl, obj);
		}
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		// Do nothing
	}

}
