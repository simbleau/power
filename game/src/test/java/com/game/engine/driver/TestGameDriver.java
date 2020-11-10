package com.game.engine.driver;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.game.engine.cache.Cache;
import com.game.engine.cache.LRUCache;
import com.game.engine.display.DisplaySettings;
import com.game.engine.display.mock.MockDisplaySettings;
import com.game.engine.driver.mock.MockDriverSettings;
import com.game.engine.game.AbstractGame;
import com.game.engine.game.AbstractPlane;
import com.game.engine.game.mock.MockPlane;
import com.game.engine.rendering.common.AbstractRenderer;

/**
 * Test {@link GameDriver}.
 * 
 * @author Spencer Imbleau
 * @version November 2020
 */
public class TestGameDriver {

	/**
	 * An arbitrary amount of time to use for a duration-based test.
	 */
	private static final int TEST_SECONDS = 2;

	/**
	 * An arbitrary amount of deviation of total updates to still be considered
	 * correct.
	 */
	private static final int ACCEPTABLE_UPDATE_DEVIATION = 1;

	/**
	 * An arbitrary amount of deviation of total frames to still be considered
	 * correct.
	 */
	private static final int ACCEPTABLE_FRAME_DEVIATION = 1;

	/**
	 * An arbitrary set of driver settings for testing.
	 */
	private static final DriverSettings TEST_DRIVER_SETTINGS = new MockDriverSettings();

	/**
	 * An arbitrary set of settings for display.
	 */
	private static final DisplaySettings TEST_DISPLAY_SETTINGS = new MockDisplaySettings();

	/** 
	 * An arbitrary plane for testing.
	 */
	private static final AbstractPlane TEST_PLANE = new MockPlane();
	
	/**
	 * An arbitrary cache for testing.
	 */
	private static final Cache TEST_CACHE = new LRUCache(0);

	/**
	 * A buffer to count total updates by the {@link #TEST_GAME}.
	 */
	private static int updates = 0;

	/**
	 * A buffer to count total frames rendered by the {@link #TEST_GAME}.
	 */
	private static int frames = 0;

	/**
	 * A game which counts updates and frames for testing analysis.
	 */
	private static final AbstractGame TEST_GAME = new AbstractGame(0, 0, TEST_PLANE) {
		@Override
		public void update(GameDriver driver) {
			super.update(driver);
			updates++;
		}
		@Override
		public void stage(GameDriver driver, AbstractRenderer renderer) {
			super.stage(driver, renderer);
			frames++;
		}
	};

	/**
	 * Buffer for testing.
	 */
	private GameDriver d;

	/**
	 * Initialize buffers for testing.
	 */
	@Before
	public void init() {
		d = new GameDriver(TEST_DRIVER_SETTINGS, TEST_CACHE, TEST_GAME);
		updates = 0;
		frames = 0;
	}

	/**
	 * Dispose of used buffers for testing.
	 */
	@After
	public void dispose() {
		if (d.getDisplay() != null) {
			d.getDisplay().close();
		}
		d.stop();
	}

	/**
	 * Test {@link GameDriver#GameDriver(DriverSettings, Cache, AbstractGame)}.
	 */
	@Test
	public void testConstructor() {
		// Test
		Assert.assertFalse(d.isRunning());
		Assert.assertEquals(TEST_DRIVER_SETTINGS, d.settings);
		Assert.assertEquals(TEST_CACHE, d.cache);
		Assert.assertEquals(TEST_GAME, d.game);
	}

	/**
	 * Test {@link GameDriver#getDisplay()}.
	 */
	@Test
	public void testGetDisplay() {
		// Test
		Assert.assertNull(d.getDisplay());
		d.start(TEST_DISPLAY_SETTINGS);
		Assert.assertNotNull(d.getDisplay());
	}

	/**
	 * Test {@link GameDriver#getFps()}.
	 */
	@Test
	public void testGetFps() {
		Assert.assertEquals(0, d.getFps());
		d.start(TEST_DISPLAY_SETTINGS);
		try {
			// Give an appropriate amount of time to determine an FPS count
			Thread.sleep(TEST_SECONDS * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			Assert.fail("Could not test FPS due to interruption.");
		}

		// Test we are near the target goal
		if (d.settings.isFpsRestricted()) {
			int delta = Math.abs(d.getFps() - d.settings.getFPSGoal()); // Deviation from fps goal to true fps
			Assert.assertTrue(delta <= ACCEPTABLE_UPDATE_DEVIATION);
		} else {
			// Test frames were rendered
			Assert.assertTrue(d.getFps() > 0);
		}
	}

	/**
	 * Test {@link GameDriver#getInput()}.
	 */
	@Test
	public void testGetInput() {
		// Test
		Assert.assertNull(d.getInput());
		d.start(TEST_DISPLAY_SETTINGS);
		Assert.assertNotNull(d.getInput());
	}

	/**
	 * Test {@link GameDriver#isRunning()}.
	 */
	@Test
	public void testIsRunning() {
		Assert.assertFalse(d.isRunning());
		d.start(TEST_DISPLAY_SETTINGS);
		Assert.assertTrue(d.isRunning());
		d.stop();
		Assert.assertFalse(d.isRunning());
	}

	/**
	 * Test {@link GameDriver#start(DisplaySettings)}.
	 */
	@Test
	public void testStart() {
		d.start(TEST_DISPLAY_SETTINGS);
		Assert.assertTrue(d.isRunning());
		try {
			Thread.sleep(TEST_SECONDS * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			Assert.fail("Could not test run due to interruption.");
		}
		d.stop();

		// Test the expected amount of updates were performed
		int updateDelta = Math.abs((TEST_SECONDS * TEST_DRIVER_SETTINGS.getTicksPerSecond()) - updates);
		Assert.assertTrue(updateDelta <= ACCEPTABLE_UPDATE_DEVIATION);

		// Test the expected amount of frames were rendered
		int frameDelta = Math.abs((TEST_SECONDS * TEST_DRIVER_SETTINGS.getFPSGoal()) - frames);
		Assert.assertTrue(frameDelta <= ACCEPTABLE_FRAME_DEVIATION);
	}

	/**
	 * Test {@link GameDriver#stop()}.
	 */
	@Test
	public void testStop() {
		d.start(TEST_DISPLAY_SETTINGS);
		d.stop();
		Assert.assertFalse(d.isRunning());
	}

}
