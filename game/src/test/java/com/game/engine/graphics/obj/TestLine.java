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
				System.out.println("Line slope: " + drawable.getDx() + "x" + drawable.getDy());
				System.out.println(new String(new char[80]).replace("\0", "-"));

				// Retrieve render
				int width = drawable.getDx() + 1;
				int height = drawable.getDy() + 1;
				BufferedImage render = GraphicTestUtil.getSafeRender(drawable, width, height);

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
				System.out.println("Line slope: " + drawable.getDx() + "x" + drawable.getDy());
				System.out.println(new String(new char[80]).replace("\0", "-"));

				// Retrieve render
				int width = drawable.getDx() + 1;
				int height = drawable.getDy() + 1;
				BufferedImage render = GraphicTestUtil.getOpenGLRender(drawable, width, height);

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
		System.out.println("\t[" + line.getDx() + "/" + line.getDy() + "]->" + pixels.size() + "px");
		int lineWidth = line.getDx() + 1;
		int lineHeight = line.getDy() + 1;
		Assert.assertTrue(pixels.size() == Math.max(lineWidth, lineHeight));
		System.out.println();

		// Analyse the rendered pixels for accuracy
		// Check all pixels are on the perimeter of a line
		PixelPosition p1 = new PixelPosition(0, 0); // Origin
		PixelPosition p2 = new PixelPosition(p1.x + line.getDx(), p1.y + line.getDy());
		System.out.println("Deviation of pixel (x,y) to line (" + p1.x + "," + p1.y + ")->(" + p2.x + "," + p2.y + ")");

		for (GraphicTestUtil.PixelPosition pixel : pixels) {
			// Assert the pixel is valid
			double deviance = pixelDeviation(pixel, p1, p2);
			System.out.println("\td(" + pixel.x + ", " + pixel.y + ")=" + deviance);
			// Too far from the line
			Assert.assertTrue(deviance >= -ACCEPTABLE_DEVIANCE);
			Assert.assertTrue(deviance <= ACCEPTABLE_DEVIANCE);
		}
		System.out.println();

		// Check that the drawable is a gap-less entity
		System.out.println("Trace gap test");
		Assert.assertTrue(GraphicTestUtil.doGapsExist(pixels));
		System.out.println();
	}

	/**
	 * Helper method to determine the deviation (distance) from a pixel p and a line
	 * (p1->p2).
	 *
	 * @param p0 - the pixel
	 * @param p1 - the start of a line
	 * @param p2 - the end of a line
	 * @return the distance from a pixel and a line
	 */
	private static double pixelDeviation(PixelPosition p, PixelPosition p1, PixelPosition p2) {
		int x = p.x;
		int y = p.y;
		int x1 = p1.x;
		int y1 = p1.y;
		int x2 = p2.x;
		int y2 = p2.y;
		if (p1.equals(p2)) {
			// Dx=dy=0, so distance is calculated using the distance formula
			//
			// sqrt((x-x1)^2+(y-y1)^2)
			double distance = Math.sqrt(Math.pow(x - x1, 2) + Math.pow(y - y1, 2));
			return distance;
		} else {
			// Distance formula using line normal
			//
			// |(x2-x1)(y1-y)-(x1-x)(y2-y1)|
			// -----------------------------
			// sqrt((x2-x1)^2+(y2-y1)^2)
			double distance = Math.abs((x2 - x1) * (y1 - y) - (x1 - x) * (y2 - y1))
					/ Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
			return distance;

		}
	}
}