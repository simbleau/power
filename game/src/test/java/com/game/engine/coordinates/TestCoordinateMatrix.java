package com.game.engine.coordinates;

import org.junit.Assert;
import org.junit.Test;

import com.game.engine.camera.AbstractCamera;
import com.game.engine.driver.GameDriver;
import com.game.engine.maths.Matrix2D;

/**
 * Test a {@link Matrix2D}.
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
	 * Test {@link Matrix2D#create(double, double)}.
	 */
	@Test
	public void testCreate() {
		// Initialize values
		Matrix2D m = Matrix2D.create(TEST_X, TEST_Y);
		double expected[][] = { { TEST_X, TEST_Y } };

		// Test matrix construction
		Assert.assertArrayEquals(expected, m.getArray());
		Assert.assertEquals(1, m.getNumRows());
		Assert.assertEquals(2, m.getNumCols());
	}

	/**
	 * Test {@link Matrix2D#set(double, double)}.
	 */
	@Test
	public void testSet() {
		// Initialize Values
		Matrix2D m = Matrix2D.create(TEST_X, TEST_Y);
		double inverseX = -m.x();
		double inverseY = -m.y();

		// Test set
		m.set(inverseX, inverseY);
		Assert.assertEquals(inverseX, m.x(), TEST_DELTA);
		Assert.assertEquals(inverseY, m.y(), TEST_DELTA);
	}

	/**
	 * Test {@link Matrix2D#setX(double)}.
	 */
	@Test
	public void testSetX() {
		// Initialize Values
		Matrix2D m = Matrix2D.create(TEST_X, TEST_Y);
		double inverseX = -m.x();

		// Test set
		m.setX(inverseX);
		Assert.assertEquals(inverseX, m.x(), TEST_DELTA);
	}

	/**
	 * Test {@link Matrix2D#setY(double)}.
	 */
	@Test
	public void testSetY() {
		// Initialize Values
		Matrix2D m = Matrix2D.create(TEST_X, TEST_Y);
		double inverseY = -m.y();

		// Test set
		m.setY(inverseY);
		Assert.assertEquals(inverseY, m.y(), TEST_DELTA);
	}

	/**
	 * Test {@link Matrix2D#rotate(double)} and
	 * {@link Matrix2D#rotateEquals(double)}.
	 */
	@Test
	public void testRotate() {
		// Initialize Values
		Matrix2D m = Matrix2D.create(1d, 0d);

		// Test 0 degree rotation
		Assert.assertEquals(m.x(), m.rotate(0).x(), TEST_DELTA);
		Assert.assertEquals(m.y(), m.rotate(0).y(), TEST_DELTA);
		Assert.assertEquals(m.x(), Matrix2D.create(m.x(), m.y()).rotateEquals(0).x(), TEST_DELTA);
		Assert.assertEquals(m.y(), Matrix2D.create(m.x(), m.y()).rotateEquals(0).y(), TEST_DELTA);
		// Test 360 degree rotation
		Assert.assertEquals(m.x(), m.rotate(2 * Math.PI).x(), TEST_DELTA);
		Assert.assertEquals(m.y(), m.rotate(2 * Math.PI).y(), TEST_DELTA);
		Assert.assertEquals(m.x(), Matrix2D.create(m.x(), m.y()).rotateEquals(2 * Math.PI).x(), TEST_DELTA);
		Assert.assertEquals(m.y(), Matrix2D.create(m.x(), m.y()).rotateEquals(2 * Math.PI).y(), TEST_DELTA);

		// Test 90 degree rotation
		Matrix2D m2 = Matrix2D.create(0d, 1d);
		Assert.assertEquals(m2.x(), m.rotate(Math.PI / 2).x(), TEST_DELTA);
		Assert.assertEquals(m2.y(), m.rotate(Math.PI / 2).y(), TEST_DELTA);
		Assert.assertEquals(m2.x(), Matrix2D.create(m.x(), m.y()).rotateEquals(Math.PI / 2).x(), TEST_DELTA);
		Assert.assertEquals(m2.y(), Matrix2D.create(m.x(), m.y()).rotateEquals(Math.PI / 2).y(), TEST_DELTA);

		// Test 180 degree rotation
		Matrix2D m3 = Matrix2D.create(-1d, 0d);
		Assert.assertEquals(m3.x(), m.rotate(Math.PI).x(), TEST_DELTA);
		Assert.assertEquals(m3.y(), m.rotate(Math.PI).y(), TEST_DELTA);
		Assert.assertEquals(m3.x(), Matrix2D.create(m.x(), m.y()).rotateEquals(Math.PI).x(), TEST_DELTA);
		Assert.assertEquals(m3.y(), Matrix2D.create(m.x(), m.y()).rotateEquals(Math.PI).y(), TEST_DELTA);

		// Test 270 degree rotation
		Matrix2D m4 = Matrix2D.create(0d, -1d);
		Assert.assertEquals(m4.x(), m.rotate(3 * Math.PI / 2).x(), TEST_DELTA);
		Assert.assertEquals(m4.y(), m.rotate(3 * Math.PI / 2).y(), TEST_DELTA);
		Assert.assertEquals(m4.x(), Matrix2D.create(m.x(), m.y()).rotateEquals(3 * Math.PI / 2).x(),
				TEST_DELTA);
		Assert.assertEquals(m4.y(), Matrix2D.create(m.x(), m.y()).rotateEquals(3 * Math.PI / 2).y(),
				TEST_DELTA);
	}

	/**
	 * Test {@link Matrix2D#rotate(double, double, double)} and
	 * {@link Matrix2D#rotateEquals(double, double, double)}.
	 */
	@Test
	public void testAnchorRotate() {
		// Initialize Values
		Matrix2D m = Matrix2D.create(1d, 0d);

		// Test 0 degree rotation around (1, 1)
		Assert.assertEquals(m.x(), m.rotate(0, 1, 1).x(), TEST_DELTA);
		Assert.assertEquals(m.y(), m.rotate(0, 1, 1).y(), TEST_DELTA);
		Assert.assertEquals(m.x(), Matrix2D.create(m.x(), m.y()).rotateEquals(0, 1, 1).x(), TEST_DELTA);
		Assert.assertEquals(m.y(), Matrix2D.create(m.x(), m.y()).rotateEquals(0, 1, 1).y(), TEST_DELTA);
		// Test 360 degree rotation around (1, 1)
		Assert.assertEquals(m.x(), m.rotate(2 * Math.PI, 1, 1).x(), TEST_DELTA);
		Assert.assertEquals(m.y(), m.rotate(2 * Math.PI, 1, 1).y(), TEST_DELTA);
		Assert.assertEquals(m.x(), Matrix2D.create(m.x(), m.y()).rotateEquals(2 * Math.PI, 1, 1).x(),
				TEST_DELTA);
		Assert.assertEquals(m.y(), Matrix2D.create(m.x(), m.y()).rotateEquals(2 * Math.PI, 1, 1).y(),
				TEST_DELTA);

		// Test 90 degree rotation around (1, 1)
		Matrix2D m2 = Matrix2D.create(2d, 1d);
		Assert.assertEquals(m2.x(), m.rotate(Math.PI / 2, 1, 1).x(), TEST_DELTA);
		Assert.assertEquals(m2.y(), m.rotate(Math.PI / 2, 1, 1).y(), TEST_DELTA);
		Assert.assertEquals(m2.x(), Matrix2D.create(m.x(), m.y()).rotateEquals(Math.PI / 2, 1, 1).x(),
				TEST_DELTA);
		Assert.assertEquals(m2.y(), Matrix2D.create(m.x(), m.y()).rotateEquals(Math.PI / 2, 1, 1).y(),
				TEST_DELTA);

		// Test 180 degree rotation around (1, 1)
		Matrix2D m3 = Matrix2D.create(1d, 2d);
		Assert.assertEquals(m3.x(), m.rotate(Math.PI, 1, 1).x(), TEST_DELTA);
		Assert.assertEquals(m3.y(), m.rotate(Math.PI, 1, 1).y(), TEST_DELTA);
		Assert.assertEquals(m3.x(), Matrix2D.create(m.x(), m.y()).rotateEquals(Math.PI, 1, 1).x(), TEST_DELTA);
		Assert.assertEquals(m3.y(), Matrix2D.create(m.x(), m.y()).rotateEquals(Math.PI, 1, 1).y(), TEST_DELTA);

		// Test 270 degree rotation around (1, 1)
		Matrix2D m4 = Matrix2D.create(0d, 1d);
		Assert.assertEquals(m4.x(), m.rotate(3 * Math.PI / 2, 1, 1).x(), TEST_DELTA);
		Assert.assertEquals(m4.y(), m.rotate(3 * Math.PI / 2, 1, 1).y(), TEST_DELTA);
		Assert.assertEquals(m4.x(), Matrix2D.create(m.x(), m.y()).rotateEquals(3 * Math.PI / 2, 1, 1).x(),
				TEST_DELTA);
		Assert.assertEquals(m4.y(), Matrix2D.create(m.x(), m.y()).rotateEquals(3 * Math.PI / 2, 1, 1).y(),
				TEST_DELTA);
	}

	/**
	 * Test {@link Matrix2D#scale(double)} and
	 * {@link Matrix2D#scaleEquals(double)}.
	 */
	@Test
	public void testScale() {
		// Initialize Values
		Matrix2D m = Matrix2D.create(1d, 1d);

		// Scale x2
		double s2 = 2d;
		Matrix2D m2 = Matrix2D.create(m.x() * s2, m.y() * s2);
		Assert.assertEquals(m2.x(), m.scale(s2).x(), TEST_DELTA);
		Assert.assertEquals(m2.y(), m.scale(s2).y(), TEST_DELTA);
		Assert.assertEquals(m2.x(), Matrix2D.create(m.x(), m.y()).scaleEquals(s2).x(), TEST_DELTA);
		Assert.assertEquals(m2.y(), Matrix2D.create(m.x(), m.y()).scaleEquals(s2).y(), TEST_DELTA);

		// Scale x0.5
		double s3 = 0.5d;
		Matrix2D m3 = Matrix2D.create(m.x() * s3, m.y() * s3);
		Assert.assertEquals(m3.x(), m.scale(s3).x(), TEST_DELTA);
		Assert.assertEquals(m3.y(), m.scale(s3).y(), TEST_DELTA);
		Assert.assertEquals(m3.x(), Matrix2D.create(m.x(), m.y()).scaleEquals(s3).x(), TEST_DELTA);
		Assert.assertEquals(m3.y(), Matrix2D.create(m.x(), m.y()).scaleEquals(s3).y(), TEST_DELTA);
	}

	/**
	 * Test {@link Matrix2D#scale(double, double)} and
	 * {@link Matrix2D#scaleEquals(double, double)}.
	 */
	@Test
	public void testAxialScale() {
		// Initialize Values
		Matrix2D m = Matrix2D.create(1d, 1d);

		// Scale x by 2
		double sx2 = 2d;
		Matrix2D m2 = Matrix2D.create(m.x() * sx2, m.y());
		Assert.assertEquals(m2.x(), m.scale(sx2, 1d).x(), TEST_DELTA);
		Assert.assertEquals(m2.y(), m.scale(sx2, 1d).y(), TEST_DELTA);
		Assert.assertEquals(m2.x(), Matrix2D.create(m.x(), m.y()).scaleEquals(sx2, 1d).x(), TEST_DELTA);
		Assert.assertEquals(m2.y(), Matrix2D.create(m.x(), m.y()).scaleEquals(sx2, 1d).y(), TEST_DELTA);

		// Scale y by 2
		double sy3 = 2d;
		Matrix2D m3 = Matrix2D.create(m.x(), m.y() * sy3);
		Assert.assertEquals(m3.x(), m.scale(1d, sy3).x(), TEST_DELTA);
		Assert.assertEquals(m3.y(), m.scale(1d, sy3).y(), TEST_DELTA);
		Assert.assertEquals(m3.x(), Matrix2D.create(m.x(), m.y()).scaleEquals(1d, sy3).x(), TEST_DELTA);
		Assert.assertEquals(m3.y(), Matrix2D.create(m.x(), m.y()).scaleEquals(1d, sy3).y(), TEST_DELTA);

		// Scale x by 2, y by 2
		double sx4 = 2d;
		double sy4 = 2d;
		Matrix2D m4 = Matrix2D.create(m.x() * sx4, m.y() * sy4);
		Assert.assertEquals(m4.x(), m.scale(sx4, sy4).x(), TEST_DELTA);
		Assert.assertEquals(m4.y(), m.scale(sx4, sy4).y(), TEST_DELTA);
		Assert.assertEquals(m4.x(), Matrix2D.create(m.x(), m.y()).scaleEquals(sx4, sy4).x(), TEST_DELTA);
		Assert.assertEquals(m4.y(), Matrix2D.create(m.x(), m.y()).scaleEquals(sx4, sy4).y(), TEST_DELTA);
	}

	/**
	 * Test {@link Matrix2D#translate(double, double)} and
	 * {@link Matrix2D#translateEquals(double, double)}.
	 */
	@Test
	public void testTranslate() {
		// Initialize Values
		Matrix2D m = Matrix2D.create(1d, 1d);

		// Translate x by 2
		double tx2 = 2d;
		Matrix2D m2 = Matrix2D.create(m.x() + tx2, m.y());
		Assert.assertEquals(m2.x(), m.translate(tx2, 0).x(), TEST_DELTA);
		Assert.assertEquals(m2.y(), m.translate(tx2, 0).y(), TEST_DELTA);
		Assert.assertEquals(m2.x(), Matrix2D.create(m.x(), m.y()).translateEquals(tx2, 0).x(), TEST_DELTA);
		Assert.assertEquals(m2.y(), Matrix2D.create(m.x(), m.y()).translateEquals(tx2, 0).y(), TEST_DELTA);

		// Translate y by 2
		double ty3 = 2d;
		Matrix2D m3 = Matrix2D.create(m.x(), m.y() + ty3);
		Assert.assertEquals(m3.x(), m.translate(0, ty3).x(), TEST_DELTA);
		Assert.assertEquals(m3.y(), m.translate(0, ty3).y(), TEST_DELTA);
		Assert.assertEquals(m3.x(), Matrix2D.create(m.x(), m.y()).translateEquals(0, ty3).x(), TEST_DELTA);
		Assert.assertEquals(m3.y(), Matrix2D.create(m.x(), m.y()).translateEquals(0, ty3).y(), TEST_DELTA);

		// Translate x by -5, y by -3
		double tx4 = -5d;
		double ty4 = -3d;
		Matrix2D m4 = Matrix2D.create(m.x() + tx4, m.y() + ty4);
		Assert.assertEquals(m4.x(), m.translate(tx4, ty4).x(), TEST_DELTA);
		Assert.assertEquals(m4.y(), m.translate(tx4, ty4).y(), TEST_DELTA);
		Assert.assertEquals(m4.x(), Matrix2D.create(m.x(), m.y()).translateEquals(tx4, ty4).x(), TEST_DELTA);
		Assert.assertEquals(m4.y(), Matrix2D.create(m.x(), m.y()).translateEquals(tx4, ty4).y(), TEST_DELTA);
	}

	/**
	 * Test {@link Matrix2D#translateX(double)} and
	 * {@link Matrix2D#translateXEquals(double)}.
	 */
	@Test
	public void testTranslateX() {
		// Initialize Values
		Matrix2D m = Matrix2D.create(1d, 1d);

		// Translate x by 2
		double tx = 2d;
		Matrix2D m2 = Matrix2D.create(m.x() + tx, m.y());
		Assert.assertEquals(m2.x(), m.translateX(tx).x(), TEST_DELTA);
		Assert.assertEquals(m2.x(), Matrix2D.create(m.x(), m.y()).translateXEquals(tx).x(), TEST_DELTA);
	}

	/**
	 * Test {@link Matrix2D#translateY(double)} and
	 * {@link Matrix2D#translateYEquals(double)}.
	 */
	@Test
	public void testTranslateY() {
		// Initialize Values
		Matrix2D m = Matrix2D.create(1d, 1d);

		// Translate y by 2
		double ty = 2d;
		Matrix2D m2 = Matrix2D.create(m.x(), m.y() + ty);
		Assert.assertEquals(m2.y(), m.translateY(ty).y(), TEST_DELTA);
		Assert.assertEquals(m2.y(), Matrix2D.create(m.x(), m.y()).translateYEquals(ty).y(), TEST_DELTA);
	}

	/**
	 * Test {@link Matrix2D#transform(AbstractCamera)} and
	 * {@link Matrix2D#transformEquals(AbstractCamera)}.
	 */
	@Test
	public void testTransform() {
		// Initialize values
		Matrix2D m = Matrix2D.create(TEST_X, TEST_Y);
		double dx = 5d, dy = 3d, zoom = 2d;
		AbstractCamera c = new AbstractCamera(dx, dy, 0, 0, zoom) {
			@Override
			public void update(GameDriver driver) {
				// Do nothing - No update
			}
		};

		// Test transform
		double x2 = (TEST_X - dx) * zoom, y2 = (TEST_Y - dy) * zoom;
		Matrix2D m2 = Matrix2D.create(x2, y2);
		Assert.assertEquals(m2, m.transform(c));
		Assert.assertEquals(m2, m.transformEquals(c));

	}

	/**
	 * Test {@link Matrix2D#x()}.
	 */
	@Test
	public void testX() {
		Matrix2D m = Matrix2D.create(TEST_X, TEST_Y);
		Assert.assertEquals(TEST_X, m.x(), TEST_DELTA);
	}

	/**
	 * Test {@link Matrix2D#y()}.
	 */
	@Test
	public void testY() {
		Matrix2D m = Matrix2D.create(TEST_X, TEST_Y);
		Assert.assertEquals(TEST_Y, m.y(), TEST_DELTA);
	}

}
