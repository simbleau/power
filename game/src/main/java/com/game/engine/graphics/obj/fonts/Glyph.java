package com.game.engine.graphics.obj.fonts;

import java.awt.event.KeyEvent;
import javafx.scene.input.KeyCode;

/**
 * Information about a glyph in a font.
 * 
 * @author Spencer Imbleau
 * @version December 2020
 */
public class Glyph {
	
	/**
	 * The color which denotes the pixel data of a relative glyph.
	 */
	public static final int DATA_COLOR = 0xffffffff; // Solid white

	/**
	 * The keycode value of the glyph.
	 * 
	 * @see KeyCode
	 */
	public final int keycode;

	/**
	 * The offset where the glyph's pixel data starts in a {@link Font}'s
	 * {@link Font#pbo}.
	 */
	public final int offset;

	/**
	 * The width of the glyph's pixel data in a {@link Font}'s {@link Font#pbo}.
	 */
	public final int width;

	/**
	 * Create a glyph information object.
	 * 
	 * @param keycode - the keycode of this glyph
	 * @param offset  - the offset of the glyph's pixel data
	 * @param width   - the width of the glyph's pixel data
	 */
	public Glyph(int keycode, int offset, int width) {
		this.keycode = keycode;
		this.offset = offset;
		this.width = width;
	}

	/**
	 * Convert a java char value to a keycode value.
	 * 
	 * @param character - the character to convert
	 * @return a keycode integer value
	 */
	public static int keycodeOf(char character) {
		return KeyEvent.getExtendedKeyCodeForChar(character);
	}

	/**
	 * Convert a java char array value to a keycode int array.
	 * 
	 * @param text - a string of characters
	 * @return a keycode integer array
	 */
	public static int[] keycodesOf(String text) {
		return text.chars().map(KeyEvent::getExtendedKeyCodeForChar).toArray();
	}

	@Override
	public int hashCode() {
		return this.keycode;
	}

}
