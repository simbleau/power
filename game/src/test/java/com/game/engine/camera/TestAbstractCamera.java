package com.game.engine.camera;

import java.util.Random;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.game.engine.display.DisplaySettings;
import com.game.engine.display.GameDisplay;
import com.game.engine.display.mock.MockDisplaySettings;
import com.game.engine.driver.GameDriver;
import com.game.engine.driver.mock.MockGameDriver;
import com.game.engine.game.AbstractGameObject;
import com.game.engine.game.mock.MockGameObject;

/**
 * Tests the core functions of {@link AbstractCamera}.
 * 
 * @author Spencer Imbleau
 * @version December 2020
 */
public class TestAbstractCamera {

	/**
	 * Arbitrary test driver settings.
	 */
	private static final GameDriver TEST_DRIVER = new MockGameDriver();

	/**
	 * Arbitrary test display settings.
	 */
	private static final DisplaySettings TEST_DISPLAY_SETTINGS = new MockDisplaySettings();

	/**
	 * Required accuracy for testing equivalence in float values.
	 */
	private static double TEST_DELTA = 0.05d;

	/**
	 * A buffer {@link GameDisplay} object for tests.
	 */
	private AbstractCamera c;

	/**
	 * Setup the test bed.
	 */
	@BeforeClass
	public static void setup() {
		TEST_DRIVER.init(TEST_DISPLAY_SETTINGS);
		TEST_DRIVER.start();
	}

	/**
	 * Setup the test bed.
	 */
	@AfterClass
	public static void dipose() {
		TEST_DRIVER.stop();
	}

	/**
	 * Restart the camera
	 */
	@Before
	public void before() {
		// Initialize values for tests
		this.c = new AbstractCamera(0, 0, 100, 100, 1) {
		};
		TEST_DRIVER.getDisplay().getRenderer().setCamera(this.c);
	}

	/**
	 * Test {@link AbstractCamera#lookAt(AbstractGameObject)} and
	 * {@link AbstractCamera#lookAt(double, double)}.
	 */
	@Test
	public void testLookAt() {
		Random rng = new Random();

		// Test looking at a point
		double x = rng.nextDouble() * TEST_DRIVER.game.getPlane().width;
		double y = rng.nextDouble() * TEST_DRIVER.game.getPlane().height;
		this.c.lookAt(x, y);
		Assert.assertEquals(x - (this.c.viewport.width / this.c.zoom / 2), this.c.viewport.x(), TEST_DELTA);
		Assert.assertEquals(y - (this.c.viewport.height / this.c.zoom / 2), this.c.viewport.y(), TEST_DELTA);

		// Test looking at an object
		x = rng.nextDouble() * TEST_DRIVER.game.getPlane().width;
		y = rng.nextDouble() * TEST_DRIVER.game.getPlane().height;
		AbstractGameObject obj = new MockGameObject(x, y);
		this.c.lookAt(obj);
		Assert.assertEquals(x - (this.c.viewport.width / this.c.zoom / 2), this.c.viewport.x(), TEST_DELTA);
		Assert.assertEquals(y - (this.c.viewport.height / this.c.zoom / 2), this.c.viewport.y(), TEST_DELTA);
	}

	/**
	 * Test {@link AbstractCamera#setZoom(double)}.
	 */
	@Test
	public void testSetZoom() {
		Random rng = new Random();
		double zoom = rng.nextDouble();

		// Test setter
		this.c.setZoom(zoom);
		Assert.assertEquals(zoom, this.c.zoom(), TEST_DELTA);
	}

	/**
	 * Test {@link AbstractCamera#magnify(double)}.
	 */
	@Test
	public void testMagnify() {
		Random rng = new Random();
		double magnification = rng.nextDouble();

		// Test magnify
		double currentZoom = this.c.zoom();
		this.c.magnify(magnification);
		Assert.assertEquals(currentZoom + magnification, this.c.zoom(), TEST_DELTA);
	}

	/**
	 * Test {@link AbstractCamera#translate(double, double)}.
	 */
	@Test
	public void testTranslate() {
		Random rng = new Random();

		// Test translating over a dx, dy
		double dx = rng.nextDouble() * TEST_DRIVER.game.getPlane().width;
		double dy = rng.nextDouble() * TEST_DRIVER.game.getPlane().height;
		double x = this.c.viewport.x();
		double y = this.c.viewport.y();

		this.c.translate(dx, dy);
		Assert.assertEquals(x + dx, this.c.viewport.x(), TEST_DELTA);
		Assert.assertEquals(y + dy, this.c.viewport.y(), TEST_DELTA);
	}

	/**
	 * Test {@link AbstractCamera#translateX(double)}.
	 */
	@Test
	public void testTranslateX() {
		Random rng = new Random();

		// Test translating over a dx
		double dx = rng.nextDouble() * TEST_DRIVER.game.getPlane().width;
		double x = this.c.viewport.x();

		this.c.translateX(dx);
		Assert.assertEquals(x + dx, this.c.viewport.x(), TEST_DELTA);
	}

	/**
	 * Test {@link AbstractCamera#translateY(double)}.
	 */
	@Test
	public void testTranslateY() {
		Random rng = new Random();

		// Test translating over a dy
		double dy = rng.nextDouble() * TEST_DRIVER.game.getPlane().height;
		double y = this.c.viewport.y();

		this.c.translateY(dy);
		Assert.assertEquals(y + dy, this.c.viewport.y(), TEST_DELTA);
	}

}
