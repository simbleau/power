package com.game.engine.graphics.obj.fonts;

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
	 * Convert a keycode to a character.
	 *
	 * @param keycode   - the keycode to convert
	 * @param shiftDown - whether shift is held
	 * @return a character describing the keycode
	 */
	public static char characterOf(int keycode, boolean shiftDown) {
		// Numerics
		if (keycode >= 48 && keycode <= 57) {
			if (!shiftDown) {
				// Parse the number
				return (char) ((keycode - 48) + 0x30);
			} else {
				// Parse the character
				if (keycode == 49) {
					return '!';
				} else if (keycode == 50) {
					return '@';
				} else if (keycode == 51) {
					return '#';
				} else if (keycode == 52) {
					return '$';
				} else if (keycode == 53) {
					return '%';
				} else if (keycode == 54) {
					return '^';
				} else if (keycode == 55) {
					return '&';
				} else if (keycode == 56) {
					return '*';
				} else if (keycode == 57) {
					return '(';
				} else if (keycode == 48) {
					return ')';
				} else
					return ' ';
			}
		}
		// Space
		else if (keycode == 32) {
			return ' ';
		}
		// Letters
		else if (keycode >= 65 && keycode <= 90) {
			if (shiftDown) {
				// Uppercase
				return (char) ((keycode - 65) + 0x41);
			} else {
				// Lowercase
				return (char) ((keycode - 65) + 0x61);
			}
		}
		// ~, `
		else if (keycode == 192) {
			if (shiftDown) {
				return '~';
			} else {
				return '`';
			}
		}
		// -, _
		else if (keycode == 45) {
			if (shiftDown) {
				return '_';
			} else {
				return '-';
			}
		}
		// +, =
		else if (keycode == 61) {
			if (shiftDown) {
				return '+';
			} else {
				return '=';
			}
		}
		// {, [
		else if (keycode == 91) {
			if (shiftDown) {
				return '{';
			} else {
				return '[';
			}
		}
		// }, ]
		else if (keycode == 93) {
			if (shiftDown) {
				return '}';
			} else {
				return ']';
			}
		}
		// \, |
		else if (keycode == 92) {
			if (shiftDown) {
				return '|';
			} else {
				return '\\';
			}
		}
		// ;, :
		else if (keycode == 59) {
			if (shiftDown) {
				return ':';
			} else {
				return ';';
			}
		}
		// ', "
		else if (keycode == 222) {
			if (shiftDown) {
				return '"';
			} else {
				return '\'';
			}
		}
		// <, ,
		else if (keycode == 44) {
			if (shiftDown) {
				return '<';
			} else {
				return ',';
			}
		}
		// >, .
		else if (keycode == 46) {
			if (shiftDown) {
				return '>';
			} else {
				return '.';
			}
		}
		// ?, /
		else if (keycode == 47) {
			if (shiftDown) {
				return '?';
			} else {
				return '/';
			}
		} else {
			return '?'; // Invalid char
		}
	}

	/**
	 * Convert a java char value to a keycode value.
	 * 
	 * @param character - the character to convert
	 * @return a keycode integer value
	 */
	public static int keycodeOf(char character) {
		int keycode = (int) character;
		return keycode;
	}

	/**
	 * Convert a java char array value to a keycode int array.
	 * 
	 * @param text - a string of characters
	 * @return a keycode integer array
	 */
	public static int[] keycodesOf(String text) {
		return text.chars().toArray();
	}

	@Override
	public int hashCode() {
		return this.keycode;
	}

}
