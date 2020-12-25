package com.game.engine.rendering.cpu;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.game.engine.display.GameDisplay;
import com.game.engine.graphics.common.RenderRequest;
import com.game.engine.rendering.common.AbstractRenderer;

/**
 * A CPU based renderer which has no hardware acceleration. Used primarily for
 * safe rendering in the event OpenGL is not detected.
 *
 * @author Spencer Imbleau
 * @version June 2020
 */
public class CPURenderer extends AbstractRenderer {

	/**
	 * The processor for CPU rendering
	 */
	private CPUProcessor processor;

	/**
	 * The canvas rendered to
	 */
	private CPUCanvas canvas;

	/**
	 * Initialize a CPU renderer
	 *
	 * @param display - the display to render for
	 */
	public CPURenderer(GameDisplay display) {
		super(display);

		// Initialize the graphics processor
		this.processor = new CPUProcessor(this);
	}

	@Override
	public void init() {
		// Create canvas
		this.canvas = new CPUCanvas(this.processor);

		// Resize the buffered image to the canvas size
		this.processor.resize(1, 1);
	}

	@Override
	public void render() {
		// Sort the render & overlay requests
		this.processor.sort();

		// Clear pixel map
		for (int i = 0; i < this.processor.pixels.length; i++) {
			this.processor.pixels[i] = 0xff000000;
		}

		// Draw background
		// TODO

		// Draw render requests
		for (RenderRequest request : this.processor.requests()) {
			switch (request.level) {
			case UI_PLUGIN:
			case UI_OVERLAY:
			case UI:
				this.processor.drawUI(request);
				break;
			default:
				this.processor.draw(request);
				break;
			}
		}

		// Clear requests
		this.processor.reset();

		// Update canvas
		Graphics graphics = this.canvas.getBufferStrategy().getDrawGraphics();
		graphics.drawImage(this.processor.image, // Image to draw
				0, // From X
				0, // From Y
				this.canvas.getWidth(), // To X
				this.canvas.getHeight(), // To Y
				null);
		this.canvas.getBufferStrategy().show();

		// Take screenshot if requested
		if (this.processor.getRenderer().isScreenshotRequested()) {
			// Take screenshot
			BufferedImage copy = new BufferedImage(this.processor.image.getWidth(), this.processor.image.getHeight(),
					this.processor.image.getType());
			Graphics g = copy.getGraphics();
			g.drawImage(this.processor.image, 0, 0, null);
			g.dispose();
			this.processor.getRenderer().setScreenshot(copy);
		}
	}

	@Override
	public void stage(RenderRequest request) {
		this.processor.stage(request);
	}

	@Override
	public CPUCanvas getCanvas() {
		return this.canvas;
	}

	@Override
	public CPUProcessor getProcessor() {
		return this.processor;
	}
}
