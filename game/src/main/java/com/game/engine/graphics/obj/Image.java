package com.game.engine.graphics.obj;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.nio.IntBuffer;

import com.game.engine.graphics.common.Drawable;
import com.game.engine.graphics.common.RenderRequest;
import com.game.engine.graphics.request.ImageRequest;
import com.game.engine.rendering.common.RenderLevel;
import com.game.engine.rendering.cpu.CPUProcessor;
import com.game.engine.rendering.opengl.JOGLProcessor;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

/**
 * An graphics object which contains information describing an image
 *
 * @version June 2020
 * @author Spencer Imbleau
 */
public class Image implements Drawable {

	/**
	 * The buffered image
	 */
	protected BufferedImage buf;

	/**
	 * The width of the image
	 */
	protected int width;

	/**
	 * The height of the image
	 */
	protected int height;

	/**
	 * The texture ID for OpenGL.
	 */
	protected int texId = 0;

	/**
	 * The PBO buffer ID for OpenGL.
	 */
	protected int pboId = 0;

	/**
	 * The Pixel Buffer Object where pixel data is stored.
	 */
	protected IntBuffer pbo;

	/**
	 * Whether the PBO has been updated since the last render call.
	 */
	protected boolean pboUpdated = false;

	/**
	 * Initializes an image
	 *
	 * @param buf - the buffered image
	 */
	public Image(BufferedImage buf) {
		// Standard
		this.buf = buf;
		this.width = buf.getWidth();
		this.height = buf.getHeight();
		this.pbo = IntBuffer.wrap(buf.getRGB(0, 0, width, height, null, 0, width));

		// OpenGL
		this.texId = 0;
		this.pboId = 0;
		this.pboUpdated = false;
	}

	/**
	 * @return the width of the image
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * Sets the image width
	 *
	 * @param width - the width to set
	 */
	public void setWidth(int width) {
		//TODO implement this
		throw new UnsupportedOperationException("This is not implemented yet");
	}

	/**
	 * @return the height of the image
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets the image height
	 *
	 * @param height - the height to set
	 */
	public void setHeight(int height) {
		//TODO implement this
		throw new UnsupportedOperationException("This is not implemented yet");
	}

	/**
	 * Gets the writable pixel buffer object backing this image. Modifications to
	 * the PBO will modify the image. Ensure if you are modifying the PBO to call
	 * {@link #flagGLRefresh()} after, or the pixels will not update properly on
	 * OpenGL. Pixels are stored in 0xAARRGGBB format.
	 *
	 * @return the pixel buffer object of this image
	 */
	public IntBuffer getPBO() {
		return this.pbo;
	}

	/**
	 * @return the buffered image
	 */
	public BufferedImage getBufferedImage() {
		return this.buf;
	}

	/**
	 * Writes over the current buffered image with a new buffered image
	 *
	 * @param buf - a buffered image
	 */
	public void setBufferedImage(BufferedImage buf) {
		//TODO implement this
		throw new UnsupportedOperationException("This is not implemented yet");
	}

	/**
	 * @param sx - the scale x-axis factor
	 * @param sy - the scale y-axis factor
	 * @return a new image, scaled accordingly
	 */
	public Image resize(double sx, double sy) {
		int sWidth = (int) (this.width * sx);
		int sHeight = (int) (this.height * sy);
		BufferedImage buf = new BufferedImage(sWidth, sHeight, BufferedImage.TYPE_INT_ARGB);
		int[] sPixels = ((DataBufferInt) buf.getRaster().getDataBuffer()).getData();

		// Resize pixels
		for (int yi = 0; yi < sHeight; yi++) {
			int yWidth = this.width * (int) (yi / sy);
			int syWidth = yi * sWidth;
			for (int xi = 0; xi < sWidth; xi++) {
				sPixels[syWidth + xi] = this.pbo.get((int) (xi / sx) + yWidth);
			}
		}

		return new Image(buf);
	}

	@Override
	public void alloc(GL2 gl) {
		if (this.texId == 0) {
			// Generate texture object
			int[] texIds = new int[1];
			gl.glGenTextures(1, texIds, 0);
			this.texId = texIds[0];

			// Bind the texture object
			gl.glBindTexture(GL2.GL_TEXTURE_2D, this.texId);

			// Clamp texture so it doesn't repeat
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP);

			// Bind texture to PBO
			gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_RGBA8, this.buf.getWidth(), this.buf.getHeight(), 0,
					GL2.GL_BGRA, GL2.GL_UNSIGNED_INT_8_8_8_8_REV, this.pbo);

			// Unbind texture object
			gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);

			// Generate a new buffer object
			int[] pboIds = new int[1];
			gl.glGenBuffers(1, pboIds, 0);
			this.pboId = pboIds[0];

			// This chunk seems unnecessary?
			gl.glBindBuffer(GL2.GL_PIXEL_UNPACK_BUFFER, this.pboId); // Bind the buffer object
			long size = this.buf.getWidth() * this.buf.getHeight() * Buffers.SIZEOF_INT;
			gl.glBufferData(GL2.GL_PIXEL_UNPACK_BUFFER, size, this.pbo, GL2.GL_STREAM_DRAW); // Bind PBO to texture

			// Unbind
			gl.glBindBuffer(GL2.GL_PIXEL_UNPACK_BUFFER, 0);
		}
	}

	@Override
	public void flagGLRefresh() {
		this.pboUpdated = true;
	}

	@Override
	public boolean needsGLRefresh() {
		return this.pboUpdated;
	}

	@Override
	public void refresh(GL2 gl) {
		if (this.pboUpdated) {
			// Bind the texture
			gl.glEnable(GL2.GL_TEXTURE_2D);
			gl.glBindTexture(GL2.GL_TEXTURE_2D, this.texId);

			// Update the texture if there was a change
			gl.glTexSubImage2D(GL2.GL_TEXTURE_2D, 0, 0, 0, this.buf.getWidth(), this.buf.getHeight(), GL2.GL_BGRA,
					GL2.GL_UNSIGNED_INT_8_8_8_8_REV, this.pbo);

			// Unbind texture
			gl.glDisable(GL2.GL_TEXTURE_2D);
			gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);

			this.pboUpdated = false;
		}
	}

	@Override
	public void dispose(GL2 gl) {
		if (this.texId != 0) {
			int[] textures = { this.texId };
			gl.glDeleteTextures(textures.length, textures, 0);

			int[] buffers = { this.pboId };
			gl.glDeleteBuffers(buffers.length, buffers, 0);

			this.pboId = 0;
			this.texId = 0;
		}
	}

	@Override
	public void draw(CPUProcessor processor, int x, int y, double sx, double sy) {
		int width = (int) (this.width * sx);
		int height = (int) (this.height * sy);

		// Don't render off-screen images
		if (x < -width)
			return;
		if (x > processor.getImage().getWidth())
			return;
		if (y < -height)
			return;
		if (y > processor.getImage().getHeight())
			return;

		// In the next steps we specify the bounds which are visible on screen
		int xStart = 0;
		int yStart = 0;
		int xEnd = width;
		int yEnd = height;
		if (x < 0) {
			xStart -= x;
		}
		if (y < 0) {
			yStart -= y;
		}
		if (xEnd + x > processor.getImage().getWidth()) {
			xEnd -= xEnd + x - processor.getImage().getWidth();
		}
		if (yEnd + y > processor.getImage().getHeight()) {
			yEnd -= yEnd + y - processor.getImage().getHeight();
		}

		// Draw
		for (int yi = yStart; yi < yEnd; yi++) {
			int syi = (int) (yi / sy);
			int syWidth = syi * this.width;
			for (int xi = xStart; xi < xEnd; xi++) {
				int sxi = (int) (xi / sx);
				processor.setPixel(xi + x, yi + y, this.pbo.get(sxi + syWidth));
			}
		}
	}

	@Override
	public void draw(JOGLProcessor processor, GL2 gl, double x, double y, double sx, double sy) {
		// Enable blending
		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

		// Bind the texture
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, this.texId);

		// Draw quad
		gl.glColor3f(1, 1, 1);
		gl.glBegin(GL2.GL_QUADS);

		gl.glTexCoord2i(0, 0);
		gl.glVertex2d(x, y);

		gl.glTexCoord2i(0, 1);
		gl.glVertex2d(x, y + this.height * sy);

		gl.glTexCoord2i(1, 1);
		gl.glVertex2d(x + this.width * sx, y + this.height * sy);

		gl.glTexCoord2i(1, 0);
		gl.glVertex2d(x + this.width * sx, y);

		gl.glEnd();

		// Disable blending and texture
		gl.glDisable(GL2.GL_TEXTURE_2D);
		gl.glDisable(GL2.GL_BLEND);

		// Unbind texture
		gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
	}

	@Override
	public RenderRequest asRequest(RenderLevel level, int x, int y) {
		return new ImageRequest(this, level, x, y);
	}

	@Override
	public RenderRequest asRequest(RenderLevel level, int depth, int x, int y) {
		return new ImageRequest(this, level, depth, x, y);
	}

}
