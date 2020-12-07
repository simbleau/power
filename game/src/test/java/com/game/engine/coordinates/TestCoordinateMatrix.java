package com.game.engine.coordinates;

import org.junit.Assert;
import org.junit.Test;

import com.game.engine.camera.AbstractCamera;
import com.game.engine.driver.GameDriver;

/**
 * Test a {@link CoordinateMatrix}.
 * 
 * @author Spencer Imbleau
 * @version October 2020
 */
public class TestCoordinateMatrix {

	/**
	 * A test X coordinate.
	 */
	public static double TEST_X = 5d;

	/**
	 * A test Y coordinate.
	 */
	public static double TEST_Y = 3d;

	/**
	 * Required accuracy for testing equivalence in float values.
	 */
	private static double TEST_DELTA = 0.05d;

	/**
	 * Test {@link CoordinateMatrix#create(double, double)}.
	 */
	@Test
	public void testCreate() {
		// Initialize values
		CoordinateMatrix m = CoordinateMatrix.create(TEST_X, TEST_Y);
		double expected[][] = { { TEST_X, TEST_Y } };

		// Test matrix construction
		Assert.assertArrayEquals(expected, m.getArray());
		Assert.assertEquals(1, m.getNumRows());
		Assert.assertEquals(2, m.getNumCols());
	}

	/**
	 * Test {@link CoordinateMatrix#rotate(double)} and
	 * {@link CoordinateMatrix#rotateEquals(double)}.
	 */
	@Test
	public void testRotate() {
		// Initialize Values
		CoordinateMatrix m = CoordinateMatrix.create(1d, 0d);

		// Test 0 degree rotation
		Assert.assertEquals(m.x(), m.rotate(0).x(), TEST_DELTA);
		Assert.assertEquals(m.y(), m.rotate(0).y(), TEST_DELTA);
		Assert.assertEquals(m.x(), CoordinateMatrix.create(m.x(), m.y()).rotateEquals(0).x(), TEST_DELTA);
		Assert.assertEquals(m.y(), CoordinateMatrix.create(m.x(), m.y()).rotateEquals(0).y(), TEST_DELTA);
		// Test 360 degree rotation
		Assert.assertEquals(m.x(), m.rotate(2 * Math.PI).x(), TEST_DELTA);
		Assert.assertEquals(m.y(), m.rotate(2 * Math.PI).y(), TEST_DELTA);
		Assert.assertEquals(m.x(), CoordinateMatrix.create(m.x(), m.y()).rotateEquals(2 * Math.PI).x(), TEST_DELTA);
		Assert.assertEquals(m.y(), CoordinateMatrix.create(m.x(), m.y()).rotateEquals(2 * Math.PI).y(), TEST_DELTA);

		// Test 90 degree rotation
		CoordinateMatrix m2 = CoordinateMatrix.create(0d, 1d);
		Assert.assertEquals(m2.x(), m.rotate(Math.PI / 2).x(), TEST_DELTA);
		Assert.assertEquals(m2.y(), m.rotate(Math.PI / 2).y(), TEST_DELTA);
		Assert.assertEquals(m2.x(), CoordinateMatrix.create(m.x(), m.y()).rotateEquals(Math.PI / 2).x(), TEST_DELTA);
		Assert.assertEquals(m2.y(), CoordinateMatrix.create(m.x(), m.y()).rotateEquals(Math.PI / 2).y(), TEST_DELTA);

		// Test 180 degree rotation
		CoordinateMatrix m3 = CoordinateMatrix.create(-1d, 0d);
		Assert.assertEquals(m3.x(), m.rotate(Math.PI).x(), TEST_DELTA);
		Assert.assertEquals(m3.y(), m.rotate(Math.PI).y(), TEST_DELTA);
		Assert.assertEquals(m3.x(), CoordinateMatrix.create(m.x(), m.y()).rotateEquals(Math.PI).x(), TEST_DELTA);
		Assert.assertEquals(m3.y(), CoordinateMatrix.create(m.x(), m.y()).rotateEquals(Math.PI).y(), TEST_DELTA);

		// Test 270 degree rotation
		CoordinateMatrix m4 = CoordinateMatrix.create(0d, -1d);
		Assert.assertEquals(m4.x(), m.rotate(3 * Math.PI / 2).x(), TEST_DELTA);
		Assert.assertEquals(m4.y(), m.rotate(3 * Math.PI / 2).y(), TEST_DELTA);
		Assert.assertEquals(m4.x(), CoordinateMatrix.create(m.x(), m.y()).rotateEquals(3 * Math.PI / 2).x(),
				TEST_DELTA);
		Assert.assertEquals(m4.y(), CoordinateMatrix.create(m.x(), m.y()).rotateEquals(3 * Math.PI / 2).y(),
				TEST_DELTA);
	}

	/**
	 * Test {@link CoordinateMatrix#rotate(double, double, double)} and
	 * {@link CoordinateMatrix#rotateEquals(double, double, double)}.
	 */
	@Test
	public void testAnchorRotate() {
		// Initialize Values
		CoordinateMatrix m = CoordinateMatrix.create(1d, 0d);

		// Test 0 degree rotation around (1, 1)
		Assert.assertEquals(m.x(), m.rotate(0, 1, 1).x(), TEST_DELTA);
		Assert.assertEquals(m.y(), m.rotate(0, 1, 1).y(), TEST_DELTA);
		Assert.assertEquals(m.x(), CoordinateMatrix.create(m.x(), m.y()).rotateEquals(0, 1, 1).x(), TEST_DELTA);
		Assert.assertEquals(m.y(), CoordinateMatrix.create(m.x(), m.y()).rotateEquals(0, 1, 1).y(), TEST_DELTA);
		// Test 360 degree rotation around (1, 1)
		Assert.assertEquals(m.x(), m.rotate(2 * Math.PI, 1, 1).x(), TEST_DELTA);
		Assert.assertEquals(m.y(), m.rotate(2 * Math.PI, 1, 1).y(), TEST_DELTA);
		Assert.assertEquals(m.x(), CoordinateMatrix.create(m.x(), m.y()).rotateEquals(2 * Math.PI, 1, 1).x(),
				TEST_DELTA);
		Assert.assertEquals(m.y(), CoordinateMatrix.create(m.x(), m.y()).rotateEquals(2 * Math.PI, 1, 1).y(),
				TEST_DELTA);

		// Test 90 degree rotation around (1, 1)
		CoordinateMatrix m2 = CoordinateMatrix.create(2d, 1d);
		Assert.assertEquals(m2.x(), m.rotate(Math.PI / 2, 1, 1).x(), TEST_DELTA);
		Assert.assertEquals(m2.y(), m.rotate(Math.PI / 2, 1, 1).y(), TEST_DELTA);
		Assert.assertEquals(m2.x(), CoordinateMatrix.create(m.x(), m.y()).rotateEquals(Math.PI / 2, 1, 1).x(),
				TEST_DELTA);
		Assert.assertEquals(m2.y(), CoordinateMatrix.create(m.x(), m.y()).rotateEquals(Math.PI / 2, 1, 1).y(),
				TEST_DELTA);

		// Test 180 degree rotation around (1, 1)
		CoordinateMatrix m3 = CoordinateMatrix.create(1d, 2d);
		Assert.assertEquals(m3.x(), m.rotate(Math.PI, 1, 1).x(), TEST_DELTA);
		Assert.assertEquals(m3.y(), m.rotate(Math.PI, 1, 1).y(), TEST_DELTA);
		Assert.assertEquals(m3.x(), CoordinateMatrix.create(m.x(), m.y()).rotateEquals(Math.PI, 1, 1).x(), TEST_DELTA);
		Assert.assertEquals(m3.y(), CoordinateMatrix.create(m.x(), m.y()).rotateEquals(Math.PI, 1, 1).y(), TEST_DELTA);

		// Test 270 degree rotation around (1, 1)
		CoordinateMatrix m4 = CoordinateMatrix.create(0d, 1d);
		Assert.assertEquals(m4.x(), m.rotate(3 * Math.PI / 2, 1, 1).x(), TEST_DELTA);
		Assert.assertEquals(m4.y(), m.rotate(3 * Math.PI / 2, 1, 1).y(), TEST_DELTA);
		Assert.assertEquals(m4.x(), CoordinateMatrix.create(m.x(), m.y()).rotateEquals(3 * Math.PI / 2, 1, 1).x(),
				TEST_DELTA);
		Assert.assertEquals(m4.y(), CoordinateMatrix.create(m.x(), m.y()).rotateEquals(3 * Math.PI / 2, 1, 1).y(),
				TEST_DELTA);
	}

	/**
	 * Test {@link CoordinateMatrix#scale(double)} and
	 * {@link CoordinateMatrix#scaleEquals(double)}.
	 */
	@Test
	public void testScale() {
		// Initialize Values
		CoordinateMatrix m = CoordinateMatrix.create(1d, 1d);

		// Scale x2
		double s2 = 2d;
		CoordinateMatrix m2 = CoordinateMatrix.create(m.x() * s2, m.y() * s2);
		Assert.assertEquals(m2.x(), m.scale(s2).x(), TEST_DELTA);
		Assert.assertEquals(m2.y(), m.scale(s2).y(), TEST_DELTA);
		Assert.assertEquals(m2.x(), CoordinateMatrix.create(m.x(), m.y()).scaleEquals(s2).x(), TEST_DELTA);
		Assert.assertEquals(m2.y(), CoordinateMatrix.create(m.x(), m.y()).scaleEquals(s2).y(), TEST_DELTA);

		// Scale x0.5
		double s3 = 0.5d;
		CoordinateMatrix m3 = CoordinateMatrix.create(m.x() * s3, m.y() * s3);
		Assert.assertEquals(m3.x(), m.scale(s3).x(), TEST_DELTA);
		Assert.assertEquals(m3.y(), m.scale(s3).y(), TEST_DELTA);
		Assert.assertEquals(m3.x(), CoordinateMatrix.create(m.x(), m.y()).scaleEquals(s3).x(), TEST_DELTA);
		Assert.assertEquals(m3.y(), CoordinateMatrix.create(m.x(), m.y()).scaleEquals(s3).y(), TEST_DELTA);
	}

	/**
	 * Test {@link CoordinateMatrix#scale(double, double)} and
	 * {@link CoordinateMatrix#scaleEquals(double, double)}.
	 */
	@Test
	public void testAxialScale() {
		// Initialize Values
		CoordinateMatrix m = CoordinateMatrix.create(1d, 1d);

		// Scale x by 2
		double sx2 = 2d;
		CoordinateMatrix m2 = CoordinateMatrix.create(m.x() * sx2, m.y());
		Assert.assertEquals(m2.x(), m.scale(sx2, 1d).x(), TEST_DELTA);
		Assert.assertEquals(m2.y(), m.scale(sx2, 1d).y(), TEST_DELTA);
		Assert.assertEquals(m2.x(), CoordinateMatrix.create(m.x(), m.y()).scaleEquals(sx2, 1d).x(), TEST_DELTA);
		Assert.assertEquals(m2.y(), CoordinateMatrix.create(m.x(), m.y()).scaleEquals(sx2, 1d).y(), TEST_DELTA);

		// Scale y by 2
		double sy3 = 2d;
		CoordinateMatrix m3 = CoordinateMatrix.create(m.x(), m.y() * sy3);
		Assert.assertEquals(m3.x(), m.scale(1d, sy3).x(), TEST_DELTA);
		Assert.assertEquals(m3.y(), m.scale(1d, sy3).y(), TEST_DELTA);
		Assert.assertEquals(m3.x(), CoordinateMatrix.create(m.x(), m.y()).scaleEquals(1d, sy3).x(), TEST_DELTA);
		Assert.assertEquals(m3.y(), CoordinateMatrix.create(m.x(), m.y()).scaleEquals(1d, sy3).y(), TEST_DELTA);

		// Scale x by 2, y by 2
		double sx4 = 2d;
		double sy4 = 2d;
		CoordinateMatrix m4 = CoordinateMatrix.create(m.x() * sx4, m.y() * sy4);
		Assert.assertEquals(m4.x(), m.scale(sx4, sy4).x(), TEST_DELTA);
		Assert.assertEquals(m4.y(), m.scale(sx4, sy4).y(), TEST_DELTA);
		Assert.assertEquals(m4.x(), CoordinateMatrix.create(m.x(), m.y()).scaleEquals(sx4, sy4).x(), TEST_DELTA);
		Assert.assertEquals(m4.y(), CoordinateMatrix.create(m.x(), m.y()).scaleEquals(sx4, sy4).y(), TEST_DELTA);
	}

	/**
	 * Test {@link CoordinateMatrix#translate(double, double)} and
	 * {@link CoordinateMatrix#translateEquals(double, double)}.
	 */
	@Test
	public void testTranslate() {
		// Initialize Values
		CoordinateMatrix m = CoordinateMatrix.create(1d, 1d);

		// Translate x by 2
		double tx2 = 2d;
		CoordinateMatrix m2 = CoordinateMatrix.create(m.x() + tx2, m.y());
		Assert.assertEquals(m2.x(), m.translate(tx2, 0).x(), TEST_DELTA);
		Assert.assertEquals(m2.y(), m.translate(tx2, 0).y(), TEST_DELTA);
		Assert.assertEquals(m2.x(), CoordinateMatrix.create(m.x(), m.y()).translateEquals(tx2, 0).x(), TEST_DELTA);
		Assert.assertEquals(m2.y(), CoordinateMatrix.create(m.x(), m.y()).translateEquals(tx2, 0).y(), TEST_DELTA);

		// Translate y by 2
		double ty3 = 2d;
		CoordinateMatrix m3 = CoordinateMatrix.create(m.x(), m.y() + ty3);
		Assert.assertEquals(m3.x(), m.translate(0, ty3).x(), TEST_DELTA);
		Assert.assertEquals(m3.y(), m.translate(0, ty3).y(), TEST_DELTA);
		Assert.assertEquals(m3.x(), CoordinateMatrix.create(m.x(), m.y()).translateEquals(0, ty3).x(), TEST_DELTA);
		Assert.assertEquals(m3.y(), CoordinateMatrix.create(m.x(), m.y()).translateEquals(0, ty3).y(), TEST_DELTA);

		// Scale x by -5, y by -2
		double tx4 = -5d;
		double ty4 = -3d;
		CoordinateMatrix m4 = CoordinateMatrix.create(m.x() + tx4, m.y() + ty4);
		Assert.assertEquals(m4.x(), m.translate(tx4, ty4).x(), TEST_DELTA);
		Assert.assertEquals(m4.y(), m.translate(tx4, ty4).y(), TEST_DELTA);
		Assert.assertEquals(m4.x(), CoordinateMatrix.create(m.x(), m.y()).translateEquals(tx4, ty4).x(), TEST_DELTA);
		Assert.assertEquals(m4.y(), CoordinateMatrix.create(m.x(), m.y()).translateEquals(tx4, ty4).y(), TEST_DELTA);
	}

	/**
	 * Test {@link CoordinateMatrix#transform(AbstractCamera)} and
	 * {@link CoordinateMatrix#transformEquals(AbstractCamera)}.
	 */
	@Test
	public void testTransform() {
		// Initialize values
		CoordinateMatrix m = CoordinateMatrix.create(TEST_X, TEST_Y);
		double dx = 5d, dy = 3d, zoom = 2d;
		AbstractCamera c = new AbstractCamera(dx, dy, 0, 0, zoom) {
			@Override
			public void update(GameDriver driver) {
				// Do nothing - No update
			}
		};

		// Test transform
		double x2 = (TEST_X - dx) * zoom, y2 = (TEST_Y - dy) * zoom;
		CoordinateMatrix m2 = CoordinateMatrix.create(x2, y2);
		Assert.assertEquals(m2, m.transform(c));
		Assert.assertEquals(m2, m.transformEquals(c));

	}

	/**
	 * Test {@link CoordinateMatrix#x()}.
	 */
	@Test
	public void testX() {
		CoordinateMatrix m = CoordinateMatrix.create(TEST_X, TEST_Y);
		Assert.assertEquals(TEST_X, m.x(), TEST_DELTA);
	}

	/**
	 * Test {@link CoordinateMatrix#y()}.
	 */
	@Test
	public void testY() {
		CoordinateMatrix m = CoordinateMatrix.create(TEST_X, TEST_Y);
		Assert.assertEquals(TEST_Y, m.y(), TEST_DELTA);
	}

}
