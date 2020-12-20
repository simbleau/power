package com.game.engine.graphics.obj.fonts;

import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * An intermediate object which stores the data necessary to read a
 * {@link BufferedImage} to its respective {@link Glyph} values in the order it
 * should be read by a {@link GlyphReader}.
 * 
 * @author Spencer Imbleau
 * @version December 2020
 */
public class GlyphSheet {

	/**
	 * The image with pixel data which backs the mapping of this glyph sheet to
	 * keycodes.
	 */
	public final BufferedImage image;

	/**
	 * The order which glyphs appear in the {@link #image}.
	 */
	private Queue<Integer> keycodeSequence;

	/**
	 * Construct a glyph sheet with an empty sequence of glyphs. Note that a glyph
	 * sheet with no keycodes is effectively useless. A developer should add
	 * supported keycodes shortly after this initialization.
	 *
	 * @param image - the image which backs this glyph sheet
	 */
	public GlyphSheet(BufferedImage image) {
		this.image = image;
		this.keycodeSequence = new LinkedList<>();
	}

	/**
	 * Construct a glyph sheet.
	 *
	 * @param image         - the image which backs this glyph sheet
	 * @param glyphSequence - the sequence of glyphs in this glyph sheet
	 */
	public GlyphSheet(BufferedImage image, Queue<Integer> glyphSequence) {
		this.image = image;
		this.keycodeSequence = glyphSequence;
	}

	/**
	 * Add a keycode to the sequence of this glyph sheet
	 * 
	 * @param keycode - integer keycode which comes next in this glyph sheet
	 */
	public void add(Integer keycode) {
		this.keycodeSequence.add(keycode);
	}

	/**
	 * @return an iterator of this glyph sheet's supported keycodes
	 */
	public Iterator<Integer> keycodeIterator() {
		return this.keycodeSequence.iterator();
	}

}
