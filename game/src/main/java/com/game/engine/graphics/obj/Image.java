package com.game.engine.graphics.obj;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import com.game.engine.graphics.common.Drawable;
import com.game.engine.graphics.common.RenderRequest;
import com.game.engine.graphics.request.ImageRequest;
import com.game.engine.rendering.common.RenderLevel;
import com.game.engine.rendering.cpu.CPUProcessor;
import com.game.engine.rendering.opengl.JOGLProcessor;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

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
	 * The pixel data of this image in ARGB format
	 */
	protected int[] pixels;

	/**
	 * A texture which may be initialized for OpenGL rendering.
	 */
	protected Texture texture;

	/**
	 * Initializes an image
	 *
	 * @param buf - the buffered image
	 */
	public Image(BufferedImage buf) {
		this.width = buf.getWidth();
		this.height = buf.getHeight();
		this.pixels = buf.getRGB(0, 0, width, height, null, 0, width);
		this.buf = buf;
		this.texture = null;
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
		this.width = width;
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
		this.height = height;
	}

	/**
	 * @return the ARGB pixel data of this image
	 */
	public int[] getPixels() {
		return this.pixels;
	}

	/**
	 * Writes over the current pixel data with new pixel data
	 *
	 * @param pixels - pixel data for this image
	 */
	public void setPixels(int[] pixels) {
		this.pixels = pixels;
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
	 * @param image - a buffered image
	 */
	public void setBufferedImage(BufferedImage image) {
		this.buf = image;
	}

	/**
	 * @param sx - the scale x-axis factor
	 * @param sy - the scale y-axis factor
	 * @return a new image, scaled accordingly
	 */
	public Image resize(float sx, float sy) {
		int sWidth = (int) (this.width * sx);
		int sHeight = (int) (this.height * sy);
		BufferedImage buf = new BufferedImage(sWidth, sHeight, BufferedImage.TYPE_INT_ARGB);
		int[] sPixels = ((DataBufferInt) buf.getRaster().getDataBuffer()).getData();

		// Resize pixels
		for (int yi = 0; yi < sHeight; yi++) {
			int yWidth = this.width * (int) (yi / sy);
			int syWidth = yi * sWidth;
			for (int xi = 0; xi < sWidth; xi++) {
				sPixels[syWidth + xi] = this.pixels[(int) (xi / sx) + yWidth];
			}
		}

		return new Image(buf);
	}

	/**
	 * Initialize the texture for manual loading
	 *
	 * @param profile - the GL profile
	 */
	public void initTexture(GLProfile profile) {
		this.texture = AWTTextureIO.newTexture(profile, this.buf, false);
	}

	/**
	 * @param profile - a profile to initialize the texture if not already
	 * @return a texture object from this image
	 */
	public Texture getTexture(GLProfile profile) {
		// Singleton
		if (this.texture == null) {
			initTexture(profile);
		}
		return this.texture;
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
				processor.setPixel(xi + x, yi + y, this.pixels[sxi + syWidth]);
			}
		}
	}

	@Override
	public void draw(JOGLProcessor processor, GL2 gl, double x, double y, double sx, double sy) {
		Texture tex = getTexture(gl.getGLProfile());
		tex.enable(gl);
		tex.bind(gl);

		gl.glBegin(GL2.GL_QUADS);
		TextureCoords texCoords = tex.getImageTexCoords();

		gl.glColor3f(1, 1, 1); // Make sure the image is full alpha
		gl.glVertex2d(x, y);
		gl.glTexCoord2f(texCoords.left(), texCoords.bottom());
//		gl.glTexCoord2f(texCoords.left(), texCoords.top());
		gl.glVertex2d(x, y + this.height * sy);
//		gl.glTexCoord2f(texCoords.right(), texCoords.top());
		gl.glTexCoord2f(texCoords.right(), texCoords.bottom());
		gl.glVertex2d(x + this.width * sx, y + this.height * sy);
//		gl.glTexCoord2f(texCoords.right(), texCoords.bottom());
		gl.glTexCoord2f(texCoords.right(), texCoords.top());
		gl.glVertex2d(x + this.width * sx, y);
//		gl.glTexCoord2f(texCoords.left(), texCoords.bottom());
		gl.glTexCoord2f(texCoords.left(), texCoords.top());
		gl.glEnd();

		tex.disable(gl);
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
