package com.game.engine.graphics.obj;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.game.engine.graphics.obj.util.GraphicTestUtil;
import com.game.engine.rendering.common.RenderMode;

/**
 * Test {@link Ellipse}.
 *
 * @author Spencer Imbleau
 * @version December 2020
 */
public class TestEllipse {

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
	 * The acceptable deviation, in pixels, for an drawable pixel to be from the
	 * true intended position.
	 */
	private static final double ACCEPTABLE_DEVIANCE = 1d; // Only allowed 1 pixel of error

	/**
	 * Tests {@link Ellipse#Ellipse(int, int, int)}.
	 */
	@Test
	public void testConstructor() {
		Ellipse e = new Ellipse(TEST_WIDTH, TEST_HEIGHT, TEST_COLOR);
		Assert.assertEquals(TEST_WIDTH, e.getWidth());
		Assert.assertEquals(TEST_HEIGHT, e.getHeight());
		Assert.assertEquals(TEST_COLOR, e.getColor());
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
				Ellipse drawable = new Ellipse(w, h, TEST_COLOR);
				System.out.println("Ellipse dimensions: " + drawable.getWidth() + "x" + drawable.getHeight());
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
				Ellipse drawable = new Ellipse(w, h, TEST_COLOR);
				System.out.println("Ellipse dimensions: " + drawable.getWidth() + "x" + drawable.getHeight());
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
	 * @param width  - a drawable's width
	 * @param height - a drawable's height
	 */
	private static final void testRender(BufferedImage render, int width, int height) {
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
		System.out.println("\t[" + width + "x" + height + "]->" + pixels.size() + "px");
		Assert.assertTrue(pixels.size() > 0);
		System.out.println();

		// Analyse the rendered pixels for accuracy
		// Ensure all rendered pixels deviate an acceptable amount
		double rx = (double) width / 2; // x radius
		double ry = (double) height / 2; // y radius
		double h = 0 + rx; // x-focus
		double k = 0 + ry; // y-focus
		System.out.println("Deviation of pixel (x,y) to ellipse with focus(" + h + "," + k + ")");
		pixels.forEach(pixel -> {
			double deviance = pixelDeviation(pixel.x, pixel.y, h, k, rx, ry);
			System.out.println("\td(" + pixel.x + ", " + pixel.y + ")=" + deviance);
			// Too close inside of the ellipse
			Assert.assertTrue(deviance >= -ACCEPTABLE_DEVIANCE);
			// Too far out of the ellipse
			Assert.assertTrue(deviance <= ACCEPTABLE_DEVIANCE);
		});
		System.out.println();

		// Check that the drawable is a gap-less entity
		System.out.println("Trace gap test");
		Assert.assertTrue(GraphicTestUtil.doGapsExist(pixels));
		System.out.println();
	}

	/**
	 * Helper method to returns the deviation of a point to the ellipse's perimeter.
	 *
	 * @param x  - the x co-ordinate
	 * @param y  - the y co-ordinate
	 * @param h  - the ellipse x-origin
	 * @param k  - the ellipse y-origin
	 * @param rx - the ellipse x-radius
	 * @param ry - the ellipse y-radius
	 * @return the deviation magnitude from a point to its ellipse
	 */
	private static double pixelDeviation(int x, int y, double h, double k, double rx, double ry) {
		if (rx == 0) {
			if (y < k) { // y is above cy
				return Math.max(0, (k - ry) - y);
			} else { // y is below cy
				return Math.max(0, y - (k + ry));
			}
		} else if (ry == 0) {
			if (x < h) { // x is left of cx
				return Math.max(0, (h - rx) - x);
			} else { // x is right of cx
				return Math.max(0, x - (h + rx));
			}
		} else {
			// (x-h)^2/rx^2 + (y-k)^2/ry^2 <= 1
			// If in the inequation, results comes less than 1 then the point lies within,
			// else if it comes exact 1 then the point lies on the ellipse, and if the
			// inequation is unsatisfied then point lies outside of the ellipse.
			double d = (Math.pow((x - h), 2) / Math.pow(rx, 2)) + (Math.pow((y - k), 2) / Math.pow(ry, 2));
			return 1 - d;
		}
	}
}
