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
	 * An arbitrary resolution for testing.
	 */
	private static final Dimension TEST_PREFERRED_RESOLUTION = new Dimension(480, 360);

	/**
	 * An arbitrary render mode for testing.
	 */
	private static final RenderMode TEST_RENDER_MODE = RenderMode.SAFE;

	/**
	 * An arbitrary camera for testing.
	 */
	private static final AbstractCamera TEST_CAMERA = new MockCamera(0, 0, 0, 0, 1);

	/**
	 * A buffer {@link DisplaySettings} object for tests.
	 */
	private DisplaySettings s;

	/**
	 * Initialize test values before each unit test.
	 */
	@Before
	public void init() {
		this.s = new DisplaySettings(TEST_PREFERRED_RESOLUTION, TEST_RENDER_MODE, TEST_CAMERA);
	}

	/**
	 * Test
	 * {@link DisplaySettings#DisplaySettings(Dimension, RenderMode, AbstractCamera)}.
	 */
	@Test
	public void testCreate() {
		// Test create
		Assert.assertEquals(TEST_PREFERRED_RESOLUTION, s.getPreferredResolution());
		Assert.assertEquals(TEST_RENDER_MODE, s.getRenderingMode());
		Assert.assertEquals(TEST_CAMERA, s.getCamera());
	}

	/**
	 * Test {@link DisplaySettings#getPreferredResolution()}.
	 */
	@Test
	public void testGetPreferredResolution() {
		Assert.assertEquals(TEST_PREFERRED_RESOLUTION, s.getPreferredResolution());
	}

	/**
	 * Test {@link DisplaySettings#getRenderingMode()}.
	 */
	@Test
	public void testGetRenderingMode() {
		Assert.assertEquals(TEST_RENDER_MODE, s.getRenderingMode());
	}

	/**
	 * Test {@link DisplaySettings#getCamera()}.
	 */
	@Test
	public void testGetCamera() {
		Assert.assertEquals(TEST_CAMERA, s.getCamera());
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
	 * Test {@link DisplaySettings#setRenderingMode(RenderMode)}.
	 */
	@Test
	public void testSetRenderingMode() {
		// Test set
		for (RenderMode mode : RenderMode.values()) {
			s.setRenderingMode(mode);
			Assert.assertEquals(mode, s.getRenderingMode());
		}
	}

	/**
	 * Test {@link DisplaySettings#setCamera(AbstractCamera)}.
	 */
	@Test
	public void testSetCamera() {
		Assert.assertEquals(TEST_CAMERA, s.getCamera());

		DevCamera testCam = new DevCamera(0, 0, 0, 0, 1);
		s.setCamera(testCam);

		Assert.assertEquals(testCam, s.getCamera());
	}
}
