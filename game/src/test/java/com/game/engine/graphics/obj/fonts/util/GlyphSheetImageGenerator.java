package com.game.engine.graphics.obj.fonts.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.junit.Assert;

import com.game.engine.input.MouseKeyboard;

/**
 * This class generates a glyph sheet image of the standard alphabet.
 *
 * @author Spencer Imbleau
 * @version December 2020
 * @see #ALPHABET
 */
public class GlyphSheetImageGenerator {

	/**
	 * The standard alphabet to generate
	 */
	private static final String ALPHABET;

	static {
		// Build the alphabet
		StringBuilder sb = new StringBuilder(MouseKeyboard.NUM_KEYS);
		for (char i = 0; i < MouseKeyboard.NUM_KEYS; i++) {
			sb.append(i);
		}
		ALPHABET = sb.toString();
	}

	/**
	 * Prints a detailed message of the runtime arguments for this program.
	 *
	 * @param args - the user-provided CLI arguments
	 */
	private static void printUsage(String[] args) {
		System.err.println("Usage: <save-path> <font-name> <font-size> <font-type>");
		System.err.println("Example: /home/spencer/fonts/font.png Arial 12 0");
		System.err.println("Your arguments: " + Arrays.toString(args));
	}

	/**
	 * Generate a font glyph sheet image using the arguments provided.
	 *
	 * @param args - Arguments for runtime.
	 * @see #printUsage(String[])
	 */
	public static void main(String[] args) {
		Assert.assertTrue(args.length == 4);
		try {
			Path savePath = Paths.get(args[0]);
			String fontName = args[1];
			int fontSize = Integer.parseInt(args[2]);
			int fontType = Integer.parseInt(args[3]);

			System.out.println("Save path: " + savePath.toAbsolutePath().toString());
			System.out.println("Font Name: " + fontName);
			System.out.println("Font Size: " + fontSize);
			System.out.println("Font Type: " + fontType);

			System.out.println();
			System.out.println("Working...");

			generate(savePath, fontName, fontSize, fontType);

			System.out.println("Done!");
			System.out.println("Glyph sheet image output to: " + savePath.toAbsolutePath().toString());

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println();
			printUsage(args);
		}
	}

	/**
	 * Generate and save a rasterized font glyph sheet image of the
	 * {@link #ALPHABET} and using the parameters provided.
	 *
	 * @param savePath - the path to save the glyph sheet image
	 * @param fontName - the name of the font
	 * @param fontSize - the size of the font
	 * @param fontType - the type of the font
	 * @throws IOException - if there are any font related issues
	 */
	public static void generate(Path savePath, String fontName, int fontSize, int fontType) throws IOException {
		if (!fontExist(fontName)) {
			System.out.println("Available fonts:");
			GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
			Font[] fonts = env.getAllFonts();
			for (Font f : fonts) {
				System.out.println(f.getName());
			}
			throw new IOException("Font: " + fontName + " does not exist");
		}
		// Get graphics objects
		Font font = new Font(fontName, fontType, fontSize);
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = img.createGraphics();
		FontRenderContext frc = graphics.getFontRenderContext();

		// Calculate sheet image size
		int width = calcWidth(font);
		GlyphVector vec = font.createGlyphVector(frc, ALPHABET);
		Rectangle bounds = vec.getPixelBounds(null, 0, 0);
		int height = bounds.height + 1;
		fontSize = (fontSize + calMaxFontSize(font, frc)) / 2;

		// Generate Buffered Image
		BufferedImage sheetImage = createFontImage(font, width, height, fontSize);
		File file = new File(savePath.toAbsolutePath().toString());
		ImageIO.write(sheetImage, "PNG", file);
	}

	/**
	 * Helper method to rasterize a font alphabet
	 *
	 * @param font     - the font to rasterize
	 * @param width    - the width of the font image sheet
	 * @param height   - the height of the font image sheet
	 * @param fontSize - the size of the font
	 * @return a rasterized font alphabet
	 */
	private static BufferedImage createFontImage(Font font, int width, int height, int fontSize) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();
		graphics.setColor(new Color(0, true));
		graphics.fillRect(0, 0, width, height);
		int x = 0, y = 0;
		graphics.setFont(font);
		FontMetrics metrics = new JPanel().getFontMetrics(font);
		for (char c = 0; c < ALPHABET.length(); c++) {
			y = 0;
			graphics.setColor(new Color(0f, 0f, 1f, 1f));
			graphics.drawLine(x, y, x, y);
			x++;
			y = 1;
			graphics.setColor(Color.WHITE);
			char character = ALPHABET.charAt(c);
			graphics.drawString(String.valueOf(character), x, y + fontSize);
			x += metrics.charWidth(character);
			y = 0;
			graphics.setColor(new Color(1f, 1f, 0f, 1f));
			graphics.drawLine(x, y, x, y);
			x++;
		}
		return image;
	}

	/**
	 * Helper method to return the max height of the alphabet
	 *
	 * @param font - the font to measure
	 * @param frc  - the font render context
	 * @return the max height of the alphabet
	 */
	private static int calMaxFontSize(Font font, FontRenderContext frc) {
		int max = 0;
		for (int i = 0; i < ALPHABET.length(); i++) {
			char character = ALPHABET.charAt(i);
			GlyphVector gv = font.createGlyphVector(frc, String.valueOf(character));
			max = Math.max(gv.getPixelBounds(null, 0, 0).height, max);
		}
		return max;
	}

	/**
	 * Helper method to return the width of the alphabet
	 *
	 * @param font - the font to measure
	 * @return the width of the alphabet
	 */
	private static int calcWidth(Font font) {
		int width = 0;
		FontMetrics metrics = new JPanel().getFontMetrics(font);
		for (char i = 0; i < ALPHABET.length(); i++) {
			char character = ALPHABET.charAt(i);
			width += metrics.charWidth(character) + 2;
		}
		return width;
	}

	/**
	 * Helper method to return whether a font exists
	 *
	 * @param name - a font name
	 * @return true if the font exists, false otherwise
	 */
	private static boolean fontExist(String name) {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Font[] fonts = env.getAllFonts();
		for (Font f : fonts) {
			if (f.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
}
