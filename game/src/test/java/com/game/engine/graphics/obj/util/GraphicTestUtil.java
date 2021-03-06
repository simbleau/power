package com.game.engine.graphics.obj.util;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.List;

import org.junit.Assert;

import com.game.engine.cache.Cache;
import com.game.engine.cache.LRUCache;
import com.game.engine.camera.StationaryCamera;
import com.game.engine.display.DisplaySettings;
import com.game.engine.driver.DriverSettings;
import com.game.engine.driver.GameDriver;
import com.game.engine.game.AbstractGame;
import com.game.engine.game.AbstractPlane;
import com.game.engine.graphics.common.Drawable;
import com.game.engine.graphics.common.RenderRequest;
import com.game.engine.rendering.common.RenderLevel;
import com.game.engine.rendering.common.RenderMode;
import com.game.engine.rendering.cpu.CPURenderer;
import com.game.engine.rendering.opengl.JOGLRenderer;

/**
 * A utility class for retrieving a rendered {@link Drawable} to analyze in
 * testing.
 *
 * @author Spencer Imbleau
 * @version December 2020
 */
public class GraphicTestUtil {

	/**
	 * The width of a drawable should be for graphic testing.
	 */
	public static final int DRAWABLE_MAX_WIDTH = 10;

	/**
	 * The height a drawable should be for graphic testing.
	 */
	public static final int DRAWABLE_MAX_HEIGHT = 10;

	/**
	 * An arbitrary driver settings for testing.
	 */
	private static final DriverSettings TEST_DRIVER_SETTINGS = new DriverSettings(1);

	/**
	 * An arbitrary cache for testing.
	 */
	private static final Cache TEST_CACHE = new LRUCache(0);

	/**
	 * Generate a safe-mode render of a drawable and return a buffered image for
	 * pixel analysis testing.
	 *
	 * @param drawable - the drawable to render
	 * @param width    - the width of the drawable
	 * @param height   - the height of the drawable
	 * @return a buffered image for test analysis
	 */
	public static BufferedImage getSafeRender(Drawable drawable, int width, int height) {
		int pW = Math.max(1, width); // Plane Width
		int pH = Math.max(1, height); // Plane Height

		CPURenderer renderer = new CPURenderer(new StationaryCamera(0, 0, pW, pH, 1));
		renderer.init();
		renderer.getProcessor().resize(pW, pH);
		drawable.draw(renderer.getProcessor(), 0, 0, 1, 1);
		BufferedImage buf = renderer.getProcessor().getImage();
		return buf;
	}

	/**
	 * Generate an OpenGL-mode render of a drawable and return a buffered image for
	 * pixel analysis testing.
	 *
	 * @param drawable - the drawable to render
	 * @param width    - the width of the drawable
	 * @param height   - the height of the drawable
	 * @return a buffered image for test analysis
	 */
	public static BufferedImage getOpenGLRender(Drawable drawable, int width, int height) {
		int pW = Math.max(1, width); // Plane Width
		int pH = Math.max(1, height); // Plane Height

		JOGLRenderer renderer = new JOGLRenderer(new StationaryCamera(0, 0, pW, pH, 1));
		renderer.init();
		// Resize buffer
		renderer.getCanvas().reshape(0, 0, pW, pH);
		// Request the screenshot be made at the next render
		renderer.requestScreenshot();
		// Stage and render
		RenderRequest request = drawable.asRequest(RenderLevel.WORLD_OBJECTS, 0, 0);
		renderer.stage(request);
		renderer.render();
		// Retrieve screenshot
		Assert.assertTrue(renderer.hasScreenshot());
		BufferedImage buf = renderer.getScreenshot();
		return buf;
	}

	/**
	 * Map all pixels in a buffered image to a boolean array, wherein pixels with
	 * the given color are true, and all other pixels are false.
	 *
	 * @param image - the image to convert to a boolean array
	 * @param color - the color of a pixel which maps to true in the boolean array
	 * @return a boolean array wherein pixels with the given color are true and all
	 *         others are false
	 */
	public static boolean[][] mapPixels(BufferedImage image, int color) {
		int rows = image.getHeight();
		int columns = image.getWidth();
		// Collect the rendered pixels
		boolean[][] pixels = new boolean[rows][columns];
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < columns; col++) {
				int c = image.getRGB(col, row);
				if (c == color) {
					pixels[row][col] = true;
				}
			}
		}
		return pixels;
	}

	/**
	 * Return the string representation of a boolean map.
	 *
	 * @param map    - the boolean map
	 * @param width  - the width of the map
	 * @param height - the height of the map
	 * @return a string representation of a boolean map
	 */
	public static String mapToString(boolean[][] map) {
		StringBuilder sb = new StringBuilder();

		// Print result in ASCII
		for (int row = 0; row < map.length; row++) {
			sb.append('\t');
			for (int col = 0; col < map[row].length; col++) {
				sb.append(map[row][col] ? "X" : "-");
			}
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}

	/**
	 * Return whether an list of pixel positions is has gaps.
	 *
	 * @param trace - a list of pixel positions
	 * @return true if the trace is a comprehensive set of pixels with no gaps
	 */
	public static boolean doGapsExist(List<PixelPosition> trace) {
		if (trace.size() <= 1) {
			return true;
		}

		// O(n^2) algorithm - Could be optimized
		// Works by ensuring every pixel in the trace has a neighbor.
		// If any pixels do not have neighbors, they are isolated and a gap exists.
		for (PixelPosition p1 : trace) {
			System.out.print("\tFinding neighbor for (" + p1.x + "," + p1.y + ")...");
			boolean foundNeighbor = false;
			for (PixelPosition p2 : trace) {
				if (p1 == p2) {
					continue;
				}
				if (p1.neighbors(p2)) {
					System.out.println("(" + p2.x + "," + p2.y + ")");
					foundNeighbor = true;
					break;
				}
			}
			if (!foundNeighbor) {
				return false;
			}
		}
		return true;
	}

	/**
	 * A pixel position test object.
	 *
	 * @author Spencer Imbleau
	 * @version December 2020
	 */
	public static class PixelPosition {
		/**
		 * The x co-ordinate.
		 */
		public final int x;

		/**
		 * The y co-ordinate.
		 */
		public final int y;

		/**
		 * Hold a pixel position.
		 *
		 * @param x - the x co-ordinate
		 * @param y - the y co-ordinate
		 */
		public PixelPosition(final int x, final int y) {
			this.x = x;
			this.y = y;
		}

		/**
		 * Check whether another pixel neighbors this pixel. Pixels directly adjacent
		 * have a distance of 1, while pixels with a diagonal adjacency have a distance
		 * of sqrt(2). This algorithm simply checks that they are direct or diagonal
		 * neighbors.
		 *
		 * @param p - a pixel to check
		 * @return true if the pixels are neighbors, false otherwise
		 */
		public boolean neighbors(PixelPosition p) {
			double distanceToStart = Math.sqrt(Math.pow(p.x - this.x, 2) + Math.pow(p.y - this.y, 2));
			return distanceToStart <= Math.sqrt(2);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof PixelPosition) {
				PixelPosition p2 = (PixelPosition) obj;
				return this.x == p2.x && this.y == p2.y;
			} else {
				return false;
			}
		}

	}

}
