package com.game.engine.rendering.cpu;

import java.awt.Canvas;
import java.awt.Graphics;
import java.util.Iterator;

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
	private Canvas canvas;

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

		//Resize the buffered image to the canvas size
		this.processor.resize(1, 1);
	}

	@Override
	public void render() {
		// Sort the render & overlay requests
		this.processor.sort();

		// Clear pixel map
		for (int i = 0; i < this.processor.pixels.length; i++) {
			this.processor.pixels[i] = 0xff00ff00;
		}

		// Draw background
		// TODO

		// Draw render requests
		Iterator<RenderRequest> renderIterator = this.processor.iterator();
		while (renderIterator.hasNext()) {
			RenderRequest request = renderIterator.next();
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
	}

	@Override
	public Canvas getCanvas() {
		return this.canvas;
	}

	@Override
	public void stage(RenderRequest request) {
		this.processor.stage(request);
	}
}
