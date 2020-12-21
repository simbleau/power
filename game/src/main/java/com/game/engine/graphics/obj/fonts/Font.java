package com.game.engine.graphics.obj.fonts;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import com.game.engine.graphics.obj.Label;

/**
 * A font which can be used to bake images with desired text on it, or as a
 * {@link Glyph} cache to render {@link Label}s dynamically.
 * 
 * @author Spencer Imbleau
 * @version December 2020
 * @see Label
 */
public class Font {

	/**
	 * A buffer containing all of the glyph's pixel data.
	 */
	public final IntBuffer pbo;

	/**
	 * A map containing all of the glyphs in this font. You should use a Map
	 * subclass which has O(1) contains and search as these are the most common
	 * operations used in font rendering. A good example is a HashMap.
	 * 
	 * @see HashMap
	 */
	public final Map<Integer, Glyph> glyphs;

	/**
	 * The size of the font, i.e. the pixel height.
	 */
	public final int size;

	/**
	 * Construct a font.
	 * 
	 * @param size     - the size of the font
	 * @param glyphPBO - the pixel buffer object containing pixel data for the
	 *                 glyphs
	 * @param glyphs   - the glyph map
	 */
	public Font(int size, IntBuffer glyphPBO, HashMap<Integer, Glyph> glyphs) {
		this.size = size;
		this.pbo = glyphPBO;
		this.glyphs = glyphs;
	}

	/**
	 * Returns whether a keycode is a renderable glyph.
	 * 
	 * @param keycode - the keycode for a glyph
	 * @return true if the glyph is renderable, false otherwise
	 * @see KeyCode
	 */
	public boolean isGlyphRenderable(Integer keycode) {
		return this.glyphs.containsKey(keycode);
	}

	/**
	 * Bakes an image of the keycodes provided.
	 * 
	 * @param keycodeSequence - a sequence of keycodes to bake into an image
	 * @param argb            - an ARGB formatted color for the glyph colors
	 * @return A new image of the keycode sequence provided
	 */
	public BufferedImage bakeImage(int[] keycodeSequence, int argb) {
		// Calculate the width this image will be
		int imgWidth = 0;

		// Check if there's any keycodes to render
		if (keycodeSequence.length == 0) {
			return new BufferedImage(imgWidth, this.size, BufferedImage.TYPE_INT_ARGB);
		}

		// Calculate the image width
		int invalidGlyphWidth = (this.size / 2);
		for (int i = 0; i < keycodeSequence.length; i++) {
			int keycode = keycodeSequence[i];
			if (this.isGlyphRenderable(keycode)) {
				// Add the width of the glyph
				imgWidth += this.glyphs.get(keycode).width;
			} else {
				// If not a keycode is not a valid glyph, we draw a box half the font size wide
				// Hence, add half the font size
				imgWidth += invalidGlyphWidth;
			}
		}

		// Draw the keycode sequence into a buffered image
		BufferedImage buf = new BufferedImage(imgWidth, this.size, BufferedImage.TYPE_INT_ARGB);
		final int[] pixels = ((DataBufferInt) buf.getRaster().getDataBuffer()).getData();
		final int glyphSheetWidth = this.pbo.remaining() / this.size;

		int x = 0;
		for (int i = 0; i < keycodeSequence.length; i++) {
			int keycode = keycodeSequence[i];
			if (this.isGlyphRenderable(keycode)) {
				// Draw the glyph
				Glyph glyph = this.glyphs.get(keycode);

				// Draw the character as from the glyph sheet image
				for (int y = 0; y < this.size; y++) {
					int yGlyphStart = y * glyphSheetWidth;
					int yImgStart = y * imgWidth;
					for (int xi = 0; xi < glyph.width; xi++) {
						// Only render white pixels
						if (this.pbo.get((xi + glyph.offset) + yGlyphStart) == Glyph.DATA_COLOR) {
							pixels[yImgStart + x + xi] = argb;
						}
					}
				}
				x += glyph.width;
			} else {
				// Draw a box, since we don't have a character map for this.
				// Vertical Bars
				for (int yi = 0; yi < this.size; yi++) {
					int yStart = yi * imgWidth;
					pixels[yStart + x] = argb;
					pixels[yStart + x + invalidGlyphWidth - 1] = argb;
				}
				// Horizontal Bars
				int yFloorStart = (this.size - 1) * imgWidth;
				for (int xi = 0; xi < invalidGlyphWidth; xi++) {
					pixels[x + xi] = argb;
					pixels[yFloorStart + x + xi] = argb;
				}
				x += invalidGlyphWidth;
			}
		}

		return buf;
	}

	/**
	 * Bakes an image of the text provided.
	 * 
	 * @param text - a sequence characters
	 * @param argb - an ARGB formatted color for the glyph colors
	 * @return A new image of the keycode sequence provided
	 */
	public BufferedImage bakeImage(String text, int argb) {
		return this.bakeImage(Glyph.keycodesOf(text), argb);
	}

}
