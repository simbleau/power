package com.game.engine.graphics.obj;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.game.engine.graphics.obj.util.GraphicTestUtil;

/**
 * Test {@link Ellipse}.
 *
 * @author Spencer Imbleau
 * @version December 2020
 */
public class TestEllipse {

	/**
	 * The test x-radius for the drawable.
	 */
	private static final int TEST_RX = GraphicTestUtil.DRAWABLE_MAX_WIDTH / 2;

	/**
	 * The test y-radius for the drawable.
	 */
	private static final int TEST_RY = GraphicTestUtil.DRAWABLE_MAX_HEIGHT / 2;

	/**
	 * The color we use to draw and confirm correct drawing.
	 */
	private static final int TEST_COLOR = 0xff00ff00;

	/**
	 * The acceptable deviation, in pixels, for an drawable pixel to be from the
	 * true intended position.
	 */
	private static final double ACCEPTABLE_DEVIANCE = 0.99d;

	/**
	 * Tests {@link Ellipse#Ellipse(int, int, int)}.
	 */
	@Test
	public void testConstructor() {
		Ellipse e = new Ellipse(TEST_RX, TEST_RY, TEST_COLOR);
		Assert.assertEquals(TEST_RX, e.getRx());
		Assert.assertEquals(TEST_RY, e.getRy());
		Assert.assertEquals(TEST_COLOR, e.getColor());
	}

	/**
	 * Tests the pixel accuracy of all drawables being rendered in
	 * {@link RenderMode#SAFE} mode.
	 *
	 * @see #e
	 */
	@Test
	public void testSafeRender() {
		// Get drawable
		for (int w = 0; w < GraphicTestUtil.DRAWABLE_MAX_WIDTH / 2; w++) {
			for (int h = 0; h < GraphicTestUtil.DRAWABLE_MAX_HEIGHT / 2; h++) {
				// Get drawable
				Ellipse drawable = new Ellipse(w, h, TEST_COLOR);
				System.out.println("Ellipse Radii: " + drawable.getRx() + "x" + drawable.getRy());

				// Retrieve render
				BufferedImage render = GraphicTestUtil.getSafeRender(drawable, w * 2 + 1, h * 2 + 1);

				// Run tests on the result
				testRender(render, w, h);
			}
		}
	}

	/**
	 * Tests the pixel accuracy of all drawables being rendered in
	 * {@link RenderMode#OPENGL} mode.
	 *
	 * @see #e
	 */
	@Test
	public void testOpenGLRender() {
		for (int w = 0; w < GraphicTestUtil.DRAWABLE_MAX_WIDTH / 2; w++) {
			for (int h = 0; h < GraphicTestUtil.DRAWABLE_MAX_HEIGHT / 2; h++) {
				// Get drawable
				Ellipse drawable = new Ellipse(w, h, TEST_COLOR);
				System.out.println("Ellipse Radii: " + drawable.getRx() + "x" + drawable.getRy());

				// Retrieve render
				BufferedImage render = GraphicTestUtil.getOpenGLRender(drawable, w * 2 + 1, h * 2 + 1);

				// Run tests on the result
				testRender(render, w, h);
			}
		}
	}

	/**
	 * Helper method to test the render of a drawable.
	 *
	 * @param render - a rendered drawable
	 * @param rx     - the drawable's x-radius
	 * @param ry     - the drawable's y-radius
	 */
	private static final void testRender(BufferedImage render, int rx, int ry) {
		boolean[][] pixelMap = GraphicTestUtil.mapPixels(render, TEST_COLOR);

		// Print pixel map
		System.out.println("Pixel Map");
		System.out.println(GraphicTestUtil.mapToString(pixelMap));

		// Collect the rendered pixels
		List<GraphicTestUtil.PixelPosition> pixels = new ArrayList<>();
		for (int x = 0; x < pixelMap.length; x++) {
			for (int y = 0; y < pixelMap[x].length; y++) {
				if (pixelMap[x][y]) {
					pixels.add(new GraphicTestUtil.PixelPosition(x, y));
				}
			}
		}

		// Make sure the correct bound of pixels were drawn
		System.out.println("Assertion of drawing");
		System.out.println("[" + rx + "x" + ry + "]->" + pixels.size() + "px");
		if (rx > 0 && ry > 0) {
			Assert.assertTrue(pixels.size() > 0);
		} else {
			Assert.assertTrue(pixels.size() == 0);
		}
		System.out.println();

		// Analyse the rendered pixels for accuracy
		// Ensure all rendered pixels deviate an acceptable amount
		if (rx > 0 && ry > 0) {
			System.out.println("Deviances");
			int h = render.getWidth() / 2;
			int k = render.getHeight() / 2;
			System.out.println("origin: " + h + ", " + k);
			pixels.forEach(pixel -> {
				double deviance = ellipseDeviation(pixel.x, pixel.y, h, k, rx, ry);
				System.out.println("(" + pixel.x + ", " + pixel.y + ")->" + deviance);
				// Too close inside of the ellipse
				Assert.assertTrue(deviance >= 1.0d - ACCEPTABLE_DEVIANCE);
				// Too far out of the ellipse
				Assert.assertTrue(deviance <= 1.0d + ACCEPTABLE_DEVIANCE);
			});
			System.out.println();
		}

		// Check that the drawable is a closed entity
		Assert.assertTrue(GraphicTestUtil.isTraceClosed(pixels));
	}

	/**
	 * Helper method to returns the deviation of a point to the ellipse's perimeter.
	 *
	 * A point is in an ellipse if the region (disk) is bounded by the inequality:
	 *
	 *  (x-h)^2     (y-k)^2
	 * --------- + --------- <= 1
	 *    rx^2        ry^2
	 *
	 * where (h,k) is the origin of the ellipse. The deviation is how far the left
	 * side of this inequality is different from 1, being exactly on the edge of an
	 * ellipse.
	 *
	 * @param x  - the x co-ordinate
	 * @param y  - the y co-ordinate
	 * @param h  - the ellipse x-origin
	 * @param k  - the ellipse y-origin
	 * @param rx - the ellipse x-radius
	 * @param ry - the ellipse y-radius
	 * @return the deviation magnitude from a point to its ellipse
	 */
	private static final double ellipseDeviation(int x, int y, int h, int k, int rx, int ry) {
		double difference = (Math.pow(x - h, 2) / Math.pow(rx, 2)) + (Math.pow(y - k, 2) / Math.pow(ry, 2));
		double deviance = difference;
		return deviance;
	}
}
