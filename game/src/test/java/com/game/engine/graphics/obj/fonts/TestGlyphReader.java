package com.game.engine.graphics.obj.fonts;

import java.awt.image.BufferedImage;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Test;

import com.game.engine.cache.Cache;
import com.game.engine.cache.LRUCache;

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
	private static final String TEST_FONT_PATH = Paths.get("src", "test", "resources", "font_ascii.png").toString();

	/**
	 * A cache to retrieve the glyph sheet image.
	 */
	private static final Cache TEST_CACHE = new LRUCache(0);

	/**
	 * Tests {@link GlyphReader#read(GlyphSheet)}.
	 */
	@Test
	public void testRead() {
		BufferedImage glyphImage = TEST_CACHE.fetch(TEST_FONT_PATH).getBufferedImage();
		GlyphSheet glyphSheet = new GlyphSheet(glyphImage);

		// The characters in this glyph sheet image are the literal map of ASCII 32->126
		Integer[] asciiRange = IntStream.range(32, 127).boxed().toArray(Integer[]::new);

		// Add them to the glyph map
		Arrays.stream(asciiRange).forEach(keycode -> glyphSheet.add(keycode));

		// Read the glyph sheet
		Font font = GlyphReader.read(glyphSheet);

		// Check all glyphs are accounted for during read
		Assert.assertEquals(asciiRange.length, font.glyphs.size());
	}

}
