package com.game.engine.rendering.cpu;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import com.game.engine.camera.AbstractCamera;
import com.game.engine.coordinates.CoordinateMatrix;
import com.game.engine.graphics.common.RenderRequest;
import com.game.engine.rendering.common.AbstractProcessor;
import com.game.engine.rendering.common.AbstractRenderer;

/**
 * An implementation of {@link AbstractProcessor} using the CPU for rendering
 * functions
 *
 * @author Spencer Imbleau
 * @version June 2020
 */
public class CPUProcessor extends AbstractProcessor {

	/**
	 * The buffered image for the game
	 */
	protected BufferedImage image;

	/**
	 * The frame buffer pixel map (pixels are stored in ARGB)
	 */
	protected int[] pixels;

	/**
	 * Initialize the CPU graphic processor
	 *
	 * @param renderer - the renderer to process the graphics for
	 */
	public CPUProcessor(AbstractRenderer renderer) {
		super(renderer);
	}

	/**
	 * Resize the image used for rendering
	 *
	 * @param width  - the width of the new image
	 * @param height - the height of the new image
	 */
	public synchronized void resize(int width, int height) {
		this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		this.pixels = ((DataBufferInt) this.image.getRaster().getDataBuffer()).getData();
	}

	/**
	 * Draw an arbitrary request on the game scene
	 *
	 * @param request - the request to draw
	 */
	public void draw(RenderRequest request) {
		AbstractCamera camera = this.renderer.display.settings.getCamera();
		CoordinateMatrix requestMatrix = CoordinateMatrix.create(request.x, request.y).transform(camera);

		request.drawable.draw(this, (int) requestMatrix.x(), (int) requestMatrix.y(), camera.zoom(), camera.zoom());
	}

	/**
	 * Draw an arbitrary overlay request on the game scene
	 *
	 * @param request - the request to draw
	 */
	public void drawUI(RenderRequest request) {
		request.drawable.draw(this, request.x, request.y, 1d, 1d);
	}

	/**
	 * @return the buffered image
	 */
	public BufferedImage getImage() {
		return this.image;
	}

	/**
	 * @return the raster pixels for the {@link #image}
	 */
	public int[] getPixels() {
		return this.pixels;
	}

	/**
	 * Set a pixel's color
	 *
	 * @param x    - The x coordinate
	 * @param y    - The y coordinate
	 * @param argb - An ARGB color value (i.e. 0xff00ff00 = Green)
	 */
	public void setPixel(int x, int y, int argb) {
		// Don't change pixels out of bounds
		if (x < 0 || x >= this.image.getWidth() || y < 0 || y >= this.image.getHeight()) {
			return;
		}

		// Get our alpha value
		int alpha = ((argb >> 24) & 0xff);

		if (alpha == 0x00) {
			// 0x00 (0) is fully transparent
			return;
		} else if (alpha == 0xff) {
			// 0xff (255) is fully opaque
			this.pixels[x + y * this.image.getWidth()] = argb;
		} else {
			// Pixel blending
			// Get current pixel color
			int index = x + y * this.image.getWidth();
			int pixelColor = pixels[index];

			// Find new red
			int redOld = ((pixelColor >> 16) & 0xff);
			int redVal = ((argb >> 16) & 0xff);
			int redNew = redOld - (int) ((redOld - redVal) * (alpha / 255f));

			// Find new green
			int greenOld = ((pixelColor >> 8) & 0xff);
			int greenVal = ((argb >> 8) & 0xff);
			int greenNew = greenOld - (int) ((greenOld - greenVal) * (alpha / 255f));

			// Find new blue
			int blueOld = (pixelColor & 0xff);
			int blueVal = (argb & 0xff);
			int blueNew = blueOld - (int) ((blueOld - blueVal) * (alpha / 255f));

			// Format the color to be ARGB
			int newColor = (0xff000000 | redNew << 16 | greenNew << 8 | blueNew);

			// Load it
			this.pixels[x + y * this.image.getWidth()] = newColor;
		}

	}
}
