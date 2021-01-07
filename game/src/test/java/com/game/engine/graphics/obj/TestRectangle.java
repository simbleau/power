package com.game.engine.graphics.obj;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.game.engine.graphics.obj.util.GraphicTestUtil;
import com.game.engine.rendering.common.RenderMode;

/**
 * Test {@link Rectangle}.
 *
 * @author Spencer Imbleau
 * @version December 2020
 */
public class TestRectangle {

	/**
	 * The test width for the drawable.
	 */
	private static final int TEST_WIDTH = GraphicTestUtil.DRAWABLE_MAX_WIDTH;

	/**
	 * The test height for the drawable.
	 */
	private static final int TEST_HEIGHT = GraphicTestUtil.DRAWABLE_MAX_HEIGHT;

	/**
	 * The color we use to draw and confirm correct drawing.
	 */
	private static final int TEST_COLOR = 0xff00ff00;

	/**
	 * Tests {@link Rectangle#Rectangle(int, int, int)}.
	 */
	@Test
	public void testConstructor() {
		Rectangle r = new Rectangle(TEST_WIDTH, TEST_HEIGHT, TEST_COLOR);
		Assert.assertEquals(TEST_WIDTH, r.getWidth());
		Assert.assertEquals(TEST_HEIGHT, r.getHeight());
		Assert.assertEquals(TEST_COLOR, r.getColor());
	}

	/**
	 * Tests the pixel accuracy of all drawables being rendered in
	 * {@link RenderMode#SAFE} mode.
	 */
	@Test
	public void testSafeRender() {
		for (int w = 0; w < GraphicTestUtil.DRAWABLE_MAX_WIDTH; w++) {
			for (int h = 0; h < GraphicTestUtil.DRAWABLE_MAX_HEIGHT; h++) {
				// Get drawable
				Rectangle drawable = new Rectangle(w, h, TEST_COLOR);
				System.out.println("Rectangle dimensions: " + drawable.getWidth() + "x" + drawable.getHeight());
				System.out.println(new String(new char[80]).replace("\0", "-"));

				// Retrieve render
				BufferedImage render = GraphicTestUtil.getSafeRender(drawable, w, h);

				// Run tests on the result
				testRender(render, w, h);
			}
		}
	}

	/**
	 * Tests the pixel accuracy of all drawables being rendered in
	 * {@link RenderMode#OPENGL} mode.
	 */
	@Test
	public void testOpenGLRender() {
		for (int w = 0; w < GraphicTestUtil.DRAWABLE_MAX_WIDTH; w++) {
			for (int h = 0; h < GraphicTestUtil.DRAWABLE_MAX_HEIGHT; h++) {
				// Get drawable
				Rectangle drawable = new Rectangle(w, h, TEST_COLOR);
				System.out.println("Rectangle dimensions: " + drawable.getWidth() + "x" + drawable.getHeight());
				System.out.println(new String(new char[80]).replace("\0", "-"));

				// Retrieve render
				BufferedImage render = GraphicTestUtil.getOpenGLRender(drawable, w, h);

				// Run tests on the result
				testRender(render, w, h);
			}
		}
	}

	/**
	 * Helper method to test the render of a drawable.
	 *
	 * @param render - a rendered drawable
	 * @param dW     - the drawable's width
	 * @param dH     - the drawable's height
	 */
	private static final void testRender(BufferedImage render, int dW, int dH) {
		boolean[][] pixelMap = GraphicTestUtil.mapPixels(render, TEST_COLOR);

		// Print pixel map
		System.out.println("Pixel map");
		System.out.println(GraphicTestUtil.mapToString(pixelMap));

		// Collect the rendered pixels
		List<GraphicTestUtil.PixelPosition> pixels = new ArrayList<>();
		for (int row = 0; row < pixelMap.length; row++) {
			for (int col = 0; col < pixelMap[row].length; col++) {
				if (pixelMap[row][col]) {
					pixels.add(new GraphicTestUtil.PixelPosition(col, row));
				}
			}
		}

		// Make sure the correct bound of pixels were drawn
		System.out.println("Assertion of drawing");
		System.out.println("[" + dW + "x" + dH + "]->" + pixels.size() + "px");
		Assert.assertTrue(pixels.size() > 0);
		System.out.println();

		// Analyse the rendered pixels for accuracy
		// Check all pixels are on the perimeter of a rectangle
		System.out.println("Pixel verification");
		for (GraphicTestUtil.PixelPosition pixel : pixels) {
			boolean shouldBeRendered = false;
			// Assert the pixel is valid
			if (pixel.y == 0 || pixel.y == dH - 1) {
				shouldBeRendered = true;
			} else if (pixel.x == 0 || pixel.x == dW - 1) {
				shouldBeRendered = true;
			} else {
				shouldBeRendered = false;
			}

			System.out.println("(" + pixel.x + ", " + pixel.y + ")->" + shouldBeRendered);
			if (shouldBeRendered) {
				Assert.assertTrue(pixelMap[pixel.y][pixel.x]);
			} else {
				Assert.assertFalse(pixelMap[pixel.y][pixel.x]);
			}
		}
		System.out.println();

		// Check that the drawable is a closed entity
		System.out.println("Trace gap test");
		Assert.assertTrue(GraphicTestUtil.doGapsExist(pixels));
		System.out.println();
	}

}
