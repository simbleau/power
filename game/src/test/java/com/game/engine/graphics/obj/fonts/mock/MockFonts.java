package com.game.engine.graphics.obj.fonts.mock;

import java.nio.file.Paths;

import com.game.engine.cache.Cache;
import com.game.engine.cache.LRUCache;
import com.game.engine.graphics.obj.fonts.Font;
import com.game.engine.graphics.obj.fonts.Glyph;
import com.game.engine.graphics.obj.fonts.GlyphReader;
import com.game.engine.graphics.obj.fonts.GlyphSheet;

/**
 * Mock fonts for testing.
 * 
 * @author Spencer Imbleau
 * @version December 2020
 */
public class MockFonts {

	/**
	 * A cache to retrieve glyph sheet images.
	 */
	private static final Cache TEST_CACHE = new LRUCache(100);

	/**
	 * The path for the glyph sheet image for {@link #FONT_ASCII}.
	 */
	private static final String FONT_ASCII_SHEET_PATH = Paths.get("src", "test", "resources", "font_ascii.png")
			.toString();

	/**
	 * The path for the glyph sheet image for {@link #FONT_256}.
	 */
	private static final String FONT_256_SHEET_PATH = Paths.get("src", "test", "resources", "font_256.png").toString();

	/**
	 * A mock font of the ascii printable range (32->127).
	 */
	public static final Font FONT_ASCII;

	/**
	 * A mock font of the first 256 ordinal characters.
	 */
	public static final Font FONT_256;

	// Load all fonts for public use
	static {
		// Load FONT_ASCII
		GlyphSheet fontAsciiGlyphSheet = new GlyphSheet(TEST_CACHE.fetch(FONT_ASCII_SHEET_PATH).getBufferedImage());
		for (int asciiChar = 32; asciiChar < 127; asciiChar++) { // This font features the ASCII range 32->126
			fontAsciiGlyphSheet.add(Glyph.keycodeOf((char) asciiChar));
		}
		FONT_ASCII = GlyphReader.read(fontAsciiGlyphSheet);

		// Load FONT_256
		GlyphSheet font256GlyphSheet = new GlyphSheet(TEST_CACHE.fetch(FONT_256_SHEET_PATH).getBufferedImage());
		for (int asciiChar = 0; asciiChar < 256; asciiChar++) { // This font features the ASCII range 32->126
			font256GlyphSheet.add(Glyph.keycodeOf((char) asciiChar));
		}
		FONT_256 = GlyphReader.read(font256GlyphSheet);
	}
}
