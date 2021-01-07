package com.game.engine.display;

import java.awt.Dimension;
import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.game.engine.camera.AbstractCamera;
import com.game.engine.camera.DevCamera;
import com.game.engine.camera.mock.MockCamera;
import com.game.engine.rendering.common.RenderMode;

/**
 * Test a {@link DisplaySettings}.
 * 
 * @author Spencer Imbleau
 * @version October 2020
 */
public class TestDisplaySettings {

	/**
	 * An arbitrary graphic mode for testing.
	 */
	private static final RenderMode TEST_PREFERRED_MODE = RenderMode.SAFE;

	/**
	 * An arbitrary resolution for testing.
	 */
	private static final Dimension TEST_PREFERRED_RESOLUTION = new Dimension(480, 360);

	/**
	 * An arbitrary camera for testing.
	 */
	private static final AbstractCamera TEST_PREFERRED_CAMERA = new MockCamera(0, 0, 0, 0, 1);

	/**
	 * A buffer {@link DisplaySettings} object for tests.
	 */
	private DisplaySettings s;

	/**
	 * Initialize test values before each unit test.
	 */
	@Before
	public void init() {
		this.s = new DisplaySettings(TEST_PREFERRED_MODE, TEST_PREFERRED_RESOLUTION, TEST_PREFERRED_CAMERA);
	}

	/**
	 * Test
	 * {@link DisplaySettings#DisplaySettings(RenderMode, Dimension, AbstractCamera)}.
	 */
	@Test
	public void testCreate() {
		// Test create
		Assert.assertEquals(TEST_PREFERRED_MODE, s.getPreferredMode());
		Assert.assertEquals(TEST_PREFERRED_RESOLUTION, s.getPreferredResolution());
		Assert.assertEquals(TEST_PREFERRED_CAMERA, s.getPreferredCamera());
	}

	/**
	 * Test {@link DisplaySettings#getPreferredResolution()}.
	 */
	@Test
	public void testGetPreferredResolution() {
		Assert.assertEquals(TEST_PREFERRED_RESOLUTION, s.getPreferredResolution());
	}

	/**
	 * Test {@link DisplaySettings#getPreferredMode()}.
	 */
	@Test
	public void testGetPreferredMode() {
		Assert.assertEquals(TEST_PREFERRED_MODE, s.getPreferredMode());
	}

	/**
	 * Test {@link DisplaySettings#getPreferredCamera()}.
	 */
	@Test
	public void testGetPreferredCamera() {
		Assert.assertEquals(TEST_PREFERRED_CAMERA, s.getPreferredCamera());
	}

	/**
	 * Test {@link DisplaySettings#setPreferredResolution(int, int)}.
	 */
	@Test
	public void testSetPreferredResolution() {
		// Initialize values
		Random r = new Random();
		int width = r.nextInt();
		int height = r.nextInt();

		// Test set
		s.setPreferredResolution(width, height);
		Assert.assertEquals(width, s.getPreferredResolution().width);
		Assert.assertEquals(height, s.getPreferredResolution().height);
	}

	/**
	 * Test {@link DisplaySettings#setPreferredMode(RenderMode)}.
	 */
	@Test
	public void testSetPreferredMode() {
		// Test set
		for (RenderMode mode : RenderMode.values()) {
			s.setPreferredMode(mode);
			Assert.assertEquals(mode, s.getPreferredMode());
		}
	}

	/**
	 * Test {@link DisplaySettings#setPreferredCamera(AbstractCamera)}.
	 */
	@Test
	public void testSetPreferredCamera() {
		Assert.assertEquals(TEST_PREFERRED_CAMERA, s.getPreferredCamera());

		DevCamera testCam = new DevCamera(0, 0, 0, 0, 1);
		s.setPreferredCamera(testCam);

		Assert.assertEquals(testCam, s.getPreferredCamera());
	}
}
