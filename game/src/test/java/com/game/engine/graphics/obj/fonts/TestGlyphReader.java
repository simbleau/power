package com.game.engine.graphics.obj.fonts;

import java.awt.image.BufferedImage;
import java.nio.file.Paths;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.game.engine.cache.Cache;
import com.game.engine.cache.LRUCache;
import com.game.engine.logger.PowerLogger;

/**
 * Test {@link GlyphReader}.
 * 
 * @author Spencer Imbleau
 * @version December 2020
 */
public class TestGlyphReader {

	/**
	 * A path to an arbitrary glyph sheet image.
	 */
	private static final String TEST_FONT_PATH = Paths.get("src", "test", "resources", "font.png").toString();

	/**
	 * A cache to retrieve the glyph sheet image.
	 */
	private static final Cache TEST_CACHE = new LRUCache(0);

	/**
	 * Initialize the font image into cache.
	 */
	@BeforeClass
	public static void init() {
		PowerLogger.start();
		TEST_CACHE.fetch(TEST_FONT_PATH);
	}

	/**
	 * Dispose of resources.
	 */
	@AfterClass
	public static void cleanup() {
		PowerLogger.stop();
	}

	/**
	 * Tests {@link GlyphReader#read(GlyphSheet)}.
	 */
	@Test
	public void testRead() {
		BufferedImage glyphImage = TEST_CACHE.fetch(TEST_FONT_PATH).getBufferedImage();
		GlyphSheet glyphSheet = new GlyphSheet(glyphImage);
		for (int keycode = 32; keycode < 127; keycode++) { // This font features the ASCII range 32->126
			glyphSheet.add(keycode);
		}
		Font font = GlyphReader.read(glyphSheet);
		System.out.println("Size: " + font.size);
		System.out.println("Glyphs: " + font.glyphs.size());
		System.out.println("All printable glyphs: ");
		font.glyphs.values().forEach(glyph -> {
			char glyphChar = (char) glyph.keycode;
			System.out.println("Keycode: " + glyph.keycode + " (" + glyph.offset + "->" + (glyph.width + glyph.offset)
					+ ") : '" + glyphChar + "'");
		});
	}
}
