package com.game.engine.graphics.obj.fonts;

import java.awt.image.BufferedImage;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Test;

import com.game.engine.cache.LRUCache;

/**
 * Test {@link GlyphSheet}.
 * 
 * @author Spencer Imbleau
 * @version December 2020
 */
public class TestGlyphSheet {

	/**
	 * An arbitrary glyph sheet image.
	 */
	private static final BufferedImage TEST_GLYPHSHEET_IMAGE = new LRUCache(0)
			.fetch(Paths.get("src", "test", "resources", "font_ascii.png").toString()).getBufferedImage();

	/**
	 * An arbitrary glyph sequence.
	 */
	private static final Queue<Integer> TEST_GLYPH_SEQUENCE = IntStream.range(0, 10).boxed()
			.collect(Collectors.toCollection(LinkedList::new));

	/**
	 * Tests {@link GlyphSheet#GlyphSheet(BufferedImage)}.
	 */
	@Test
	public void testConstructor1() {
		GlyphSheet glyphSheet = new GlyphSheet(TEST_GLYPHSHEET_IMAGE);

		Assert.assertEquals(TEST_GLYPHSHEET_IMAGE, glyphSheet.image);
		Assert.assertNotNull(glyphSheet.keycodeIterator());
		Assert.assertFalse(glyphSheet.keycodeIterator().hasNext());
	}

	/**
	 * Tests {@link GlyphSheet#GlyphSheet(BufferedImage, Queue)}.
	 */
	@Test
	public void testConstructor2() {
		GlyphSheet glyphSheet = new GlyphSheet(TEST_GLYPHSHEET_IMAGE, TEST_GLYPH_SEQUENCE);

		Assert.assertEquals(TEST_GLYPHSHEET_IMAGE, glyphSheet.image);
		Assert.assertNotNull(glyphSheet.keycodeIterator());
		Assert.assertTrue(glyphSheet.keycodeIterator().hasNext());
		Iterator<Integer> i = glyphSheet.keycodeIterator();
		TEST_GLYPH_SEQUENCE.forEach(keycode -> Assert.assertEquals(keycode, i.next()));
	}

	/**
	 * Tests {@link GlyphSheet#add(Integer)}.
	 */
	@Test
	public void testAdd() {
		// Buffer values
		GlyphSheet glyphSheet = new GlyphSheet(TEST_GLYPHSHEET_IMAGE);

		// Add
		TEST_GLYPH_SEQUENCE.forEach(keycode -> glyphSheet.add(keycode));

		// Test equivalence
		Iterator<Integer> i = glyphSheet.keycodeIterator();
		TEST_GLYPH_SEQUENCE.forEach(keycode -> Assert.assertEquals(keycode, i.next()));
	}
}
