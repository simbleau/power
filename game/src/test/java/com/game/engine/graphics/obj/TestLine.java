package com.game.engine.graphics.obj;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.game.engine.graphics.obj.util.GraphicTestUtil;
import com.game.engine.graphics.obj.util.GraphicTestUtil.PixelPosition;

/**
 * Test {@link Line}.
 *
 * @author Spencer Imbleau
 * @version December 2020
 */
public class TestLine {

	/**
	 * The test delta x for the drawable.
	 */
	private static final int TEST_DX = GraphicTestUtil.DRAWABLE_MAX_WIDTH;

	/**
	 * The test delta y for the drawable.
	 */
	private static final int TEST_DY = GraphicTestUtil.DRAWABLE_MAX_HEIGHT;

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
	 * Tests {@link Line#Line(int, int, int)}.
	 */
	@Test
	public void testConstructor() {
		Line l = new Line(TEST_DX, TEST_DY, TEST_COLOR);
		Assert.assertEquals(TEST_DX, l.getDx());
		Assert.assertEquals(TEST_DY, l.getDy());
		Assert.assertEquals(TEST_COLOR, l.getColor());
	}

	/**
	 * Tests the pixel accuracy of all drawables being rendered in
	 * {@link RenderMode#SAFE} mode.
	 */
	@Test
	public void testSafeRender() {
		for (int dx = 0; dx < GraphicTestUtil.DRAWABLE_MAX_WIDTH; dx++) {
			for (int dy = 0; dy < GraphicTestUtil.DRAWABLE_MAX_HEIGHT; dy++) {
				// Get drawable
				Line drawable = new Line(dx, dy, TEST_COLOR);
				System.out.println("Line Slope: " + drawable.getDx() + "x" + drawable.getDy());

				// Retrieve render
				BufferedImage render = GraphicTestUtil.getSafeRender(drawable, dx, dy);

				// Run tests on the result
				testRender(render, drawable);
			}
		}
	}

	/**
	 * Tests the pixel accuracy of all drawables being rendered in
	 * {@link RenderMode#OPENGL} mode.
	 */
	@Test
	public void testOpenGLRender() {
		for (int dx = 0; dx < GraphicTestUtil.DRAWABLE_MAX_WIDTH; dx++) {
			for (int dy = 0; dy < GraphicTestUtil.DRAWABLE_MAX_HEIGHT; dy++) {
				// Get drawable
				Line drawable = new Line(dx, dy, TEST_COLOR);
				System.out.println("Line Slope: " + drawable.getDx() + "x" + drawable.getDy());

				// Retrieve render
				BufferedImage render = GraphicTestUtil.getOpenGLRender(drawable, dx, dy);

				// Run tests on the result
				testRender(render, drawable);
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
	private static final void testRender(BufferedImage render, Line line) {
		boolean[][] pixelMap = GraphicTestUtil.mapPixels(render, TEST_COLOR);

		// Print pixel map
		System.out.println("Pixel Map");
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
		System.out.println("[" + line.getDx() + "/" + line.getDy() + "]->" + pixels.size() + "px");
		if (line.getDx() > 0 && line.getDy() > 0) {
			Assert.assertTrue(pixels.size() == Math.max(line.getDx(), line.getDy()));
		} else {
			Assert.assertTrue(pixels.size() == 0);
		}
		System.out.println();

		// Analyse the rendered pixels for accuracy
		// Check all pixels are on the perimeter of a line
		if (line.getDx() > 0 && line.getDy() > 0) {
			System.out.println("Point Checking");
			for (GraphicTestUtil.PixelPosition pixel : pixels) {
				// Assert the pixel is valid
				double deviance = lineDeviation(pixel, line);
				System.out.println("(" + pixel.x + ", " + pixel.y + ")->" + deviance);
				// Too close inside of the ellipse
				Assert.assertTrue(deviance >= 1.0d - ACCEPTABLE_DEVIANCE);
				// Too far out of the ellipse
				Assert.assertTrue(deviance <= 1.0d + ACCEPTABLE_DEVIANCE);
			}
			System.out.println();
		}

		// Check that the drawable is a closed entity
		System.out.println("Object closedness");
		Assert.assertTrue(GraphicTestUtil.isTraceClosed(pixels));
	}

	/**
	 * Helper method to determine the deviation (distance) from a pixel and the
	 * line.
	 *
	 * @param p0 - the pixel
	 * @param l  - the line
	 * @return the distance from a pixel and a line
	 */
	private static double lineDeviation(PixelPosition p0, Line l) {
		PixelPosition p1 = new PixelPosition(0, 0);
		PixelPosition p2 = new PixelPosition(l.getDx(), l.getDy());
		int x0 = p0.x;
		int y0 = p0.y;
		int x1 = p1.x;
		int y1 = p1.y;
		int x2 = p2.x;
		int y2 = p2.y;
		// Distance formula using line normal
		//
		// |(x2-x1)(y1-y0)-(x1-x0)(y2-y1)|
		// -------------------------------
		// sqrt((x2-x1)^2+(y2-y1)^2)
		double distance = Math.abs((x2 - x1) * (y1 - y0) - (x1 - x0) * (y2 - y1))
				/ Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
		return distance;
	}
}