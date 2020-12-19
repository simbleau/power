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
	private static final Cache TEST_CACHE = new LRUCache(1);

	/**
	 * The path for the glyph sheet image for {@link #FONT_1}.
	 */
	private static final String FONT_1_SHEET_PATH = Paths.get("src", "test", "resources", "font.png").toString();

	/**
	 * A mock font.
	 */
	public static final Font FONT_1;

	// Load all fonts for public use
	static {
		// Load font 1
		GlyphSheet font1GlyphSheet = new GlyphSheet(TEST_CACHE.fetch(FONT_1_SHEET_PATH).getBufferedImage());
		for (int asciiChar = 32; asciiChar < 127; asciiChar++) { // This font features the ASCII range 32->126
			font1GlyphSheet.add(Glyph.keycodeOf((char) asciiChar));
		}
		FONT_1 = GlyphReader.read(font1GlyphSheet);
	}
}
