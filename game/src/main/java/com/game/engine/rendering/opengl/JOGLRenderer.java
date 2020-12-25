package com.game.engine.rendering.opengl;

import com.game.engine.camera.AbstractCamera;
import com.game.engine.graphics.common.RenderRequest;
import com.game.engine.rendering.common.AbstractRenderer;
import com.game.engine.rendering.common.RenderMode;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;

/**
 * A renderer using OpenGL via the JOGL wrapper library
 *
 * @author Spencer Imbleau
 * @version June 2020
 */
public class JOGLRenderer extends AbstractRenderer {

	/**
	 * The processor for GL rendering
	 */
	private JOGLProcessor processor;

	/**
	 * The canvas rendered to
	 */
	private JOGLCanvas canvas;

	/**
	 * The version of OpenGL used
	 */
	private static final String GL_VERSION = GLProfile.GL2;

	/**
	 * Initialize an OpenGL renderer
	 *
	 * @param camera - the camera used for rendering
	 */
	public JOGLRenderer(AbstractCamera camera) {
		super(camera);

		// Initialize the graphics processor
		this.processor = new JOGLProcessor(this);
	}

	@Override
	public void init() {
		GLProfile.initSingleton();
		GLProfile profile = GLProfile.get(GL_VERSION);
		GLCapabilities capabilities = new GLCapabilities(profile);
		// To enable MSAA x4 Anti-aliasing
		// capabilities.setSampleBuffers(true);
		// capabilities.setNumSamples(4); //MSAA x4
		this.canvas = new JOGLCanvas(capabilities, this.processor);
	}

	@Override
	public void render() {
		this.processor.sort();
		this.canvas.display();
		this.processor.reset();
	}

	@Override
	public void stage(RenderRequest request) {
		this.processor.stage(request);
	}

	@Override
	public JOGLCanvas getCanvas() {
		return this.canvas;
	}

	@Override
	public JOGLProcessor getProcessor() {
		return this.processor;
	}

	@Override
	public RenderMode getMode() {
		return RenderMode.OPENGL;
	}

}
