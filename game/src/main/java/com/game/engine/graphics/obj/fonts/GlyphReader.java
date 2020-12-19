package com.game.engine.graphics.obj.fonts;

import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Iterator;

import com.game.engine.logger.PowerLogger;

/**
 * A basic glyph reader which reads a glyph sheet similar to a sprite sheet. The
 * glyph sheet image should be an image where the beginning of a glyph is
 * denoted by a {@link #START_PIXEL} on the top row of pixels, and the end of a
 * glyph is denoted by an {@link #END_PIXEL} on the top row of pixels. It reads
 * the top row of pixels to delineate the offsets and widths of glyphs to
 * construct the font. Pixels belonging to the actual glyph should be the same
 * color as {@link Glyph#DATA_COLOR}.
 * 
 * @author Spencer Imbleau
 * @version December 2020
 */
public class GlyphReader {

	/**
	 * A pixel which denotes the start of a glyph in a glyph sheet image
	 */
	private static final int START_PIXEL = 0xff0000ff; // Solid blue

	/**
	 * A pixel which denotes the end of a glyph in a glyph sheet image
	 */
	private static final int END_PIXEL = 0xffffff00; // Solid yellow

	/**
	 * Construct a {@link Font} from a {@link GlyphSheet} source.
	 * 
	 * @param glyphSheet - the source for the font
	 * @return a generated font
	 */
	public static Font read(GlyphSheet glyphSheet) {
		// The height, in pixels, of our font
		int fontSize = glyphSheet.image.getHeight();
		// Construct a pixel buffer which contains our glyphs
		int glyphSheetWidth = glyphSheet.image.getWidth();
		int glyphSheetHeight = glyphSheet.image.getHeight();
		IntBuffer pbo = IntBuffer
				.wrap(glyphSheet.image.getRGB(0, 0, glyphSheetWidth, glyphSheetHeight, null, 0, glyphSheetWidth));
		// A buffer map which will store our glyphs for the font
		HashMap<Integer, Glyph> glyphMap = new HashMap<>();

		// The keycodes we will be putting in our font
		Iterator<Integer> glyphKeycodes = glyphSheet.keycodeIterator();

		// If we don't have any keycodes, we're done
		if (!glyphKeycodes.hasNext()) {
			PowerLogger.LOGGER.warning(
					"Reading in a GlyphSheet, no keycodes were provided. A Font with no Glyphs will be generated");
			return new Font(fontSize, pbo, glyphMap);
		}

		// Search the top row of pixels in the font image delineating pixels
		int glyphOffset = 0;
		for (int i = 0; i < glyphSheetWidth; i++) {
			// Get the next pixel
			int pixel = pbo.get();

			// Get glyph offsets
			if (pixel == START_PIXEL) {
				glyphOffset = i;
			} else if (pixel == END_PIXEL) {
				if (!glyphKeycodes.hasNext()) {
					PowerLogger.LOGGER.warning("Glyph found, but no keycode to map it to.");
					PowerLogger.LOGGER.fine("--Glyph location: (" + glyphOffset + "->" + i + ")");
					continue;
				}
				int keycode = glyphKeycodes.next();
				int glyphWidth = i - glyphOffset;
				glyphMap.put(keycode, new Glyph(keycode, glyphOffset, glyphWidth));
			}
		}
		pbo.rewind();

		if (glyphKeycodes.hasNext()) {
			PowerLogger.LOGGER.warning("Reading in a GlyphSheet, extra keycodes were provided.");
		}

		return new Font(fontSize, pbo, glyphMap);
	}
}
