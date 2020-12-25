package com.game.engine.rendering.opengl;

import com.game.engine.camera.AbstractCamera;
import com.game.engine.coordinates.CoordinateMatrix;
import com.game.engine.graphics.common.RenderRequest;
import com.game.engine.rendering.common.AbstractProcessor;
import com.game.engine.rendering.common.AbstractRenderer;
import com.jogamp.opengl.GL2;

/**
 * An implementation of {@link AbstractProcessor} using OpenGL bindings from the
 * JOGL library for drawing functions
 *
 * @author Spencer Imbleau
 * @version June 2020
 */
public class JOGLProcessor extends AbstractProcessor {

	/**
	 * Initialize a GL graphic processor
	 *
	 * @param renderer - the renderer
	 */
	public JOGLProcessor(AbstractRenderer renderer) {
		super(renderer);
	}

	@Override
	public JOGLRenderer getRenderer() {
		return (JOGLRenderer) this.renderer;
	}

	/**
	 * Draw an arbitrary request on the game scene
	 *
	 * @param gl      - the gl context
	 * @param request - the request to draw
	 */
	public void draw(GL2 gl, RenderRequest request) {
		AbstractCamera camera = this.renderer.getCamera();
		CoordinateMatrix requestMatrix = CoordinateMatrix.create(request.x, request.y).transform(camera);

		request.drawable.draw(this, gl, requestMatrix.x(), requestMatrix.y(), camera.zoom(), camera.zoom());
	}

	/**
	 * Draw an arbitrary overlay request on the game scene
	 *
	 * @param gl      - the gl context
	 * @param request - the request to draw
	 */
	public void drawUI(GL2 gl, RenderRequest request) {
		request.drawable.draw(this, gl, request.x, request.y, 1f, 1f);
	}
}
