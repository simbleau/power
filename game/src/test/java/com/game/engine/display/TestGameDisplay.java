package com.game.engine.display;

import java.lang.reflect.Field;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.game.engine.display.mock.MockDisplaySettings;
import com.game.engine.driver.GameDriver;
import com.game.engine.driver.mock.MockGameDriver;
import com.game.engine.rendering.cpu.CPURenderer;
import com.game.engine.rendering.opengl.JOGLRenderer;

/**
 * Test a {@link GameDisplay}.
 * 
 * @author Spencer Imbleau
 * @version October 2020
 */
public class TestGameDisplay {

	/**
	 * Arbitrary test driver settings.
	 */
	private static final GameDriver TEST_DRIVER = new MockGameDriver();

	/**
	 * Arbitrary test display settings.
	 */
	private static final DisplaySettings TEST_DISPLAY_SETTINGS = new MockDisplaySettings();

	/**
	 * A buffer {@link GameDisplay} object for tests.
	 */
	private GameDisplay d;

	/**
	 * Initialize game display buffer before each test.
	 */
	@Before
	public void before() {
		// Initialize values for tests
		d = new GameDisplay(TEST_DRIVER, TEST_DISPLAY_SETTINGS);
	}

	/**
	 * Dispose game display buffer after each test.
	 */
	@After
	public void after() {
		d.close();
	}

	/**
	 * Test {@link GameDisplay#GameDisplay(GameDriver, DisplaySettings)}.
	 */
	@Test
	public void testConstructor1() {
		// Test constructor
		Assert.assertEquals(d.driver, TEST_DRIVER);
		Assert.assertEquals(d.settings, TEST_DISPLAY_SETTINGS);
		Assert.assertNotNull(d.getRenderer());
		Assert.assertFalse(d.isInitialized());
		Assert.assertFalse(d.isVisible());
		Assert.assertNull(d.getFrameTitle());
	}

	/**
	 * Test {@link GameDisplay#close()}.
	 */
	@Test
	public void testClose() {
		// Attempt to close when it has not been initialized
		d.close();
		Assert.assertFalse(d.isInitialized());

		// Attempt to close when it has been initialized
		d.init();
		Assert.assertTrue(d.isInitialized());
		try {
			// Edit frame so closing doesn't terminate the test thread
			Field frameField = GameDisplay.class.getDeclaredField("frame");
			frameField.setAccessible(true);
			((JFrame) frameField.get(d)).setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		} catch (Exception e) {
			Assert.fail("Cannot test frame because test thread may terminate.");
		}
		d.close();
		Assert.assertFalse(d.isInitialized());
	}

	/**
	 * Test {@link GameDisplay#getFrameTitle()}.
	 */
	@Test
	public void testGetFrameTitle() {
		// Ensure frame title is null, since d is not initialized
		Assert.assertNull(d.getFrameTitle());

		// Initialize, and it should not be null now
		d.init();
		Assert.assertNotNull(d.getFrameTitle());
	}

	/**
	 * Test {@link GameDisplay#getRenderer()}.
	 */
	@Test
	public void testGetRenderer() {
		// Test render was correctly set
		switch (TEST_DISPLAY_SETTINGS.getPreferredMode()) {
		case OPENGL:
			Assert.assertTrue(d.getRenderer() instanceof JOGLRenderer);
			break;
		case SAFE:
			Assert.assertTrue(d.getRenderer() instanceof CPURenderer);
			break;
		default:
			Assert.fail("Unknown graphic mode for testing: " + TEST_DISPLAY_SETTINGS.getPreferredMode());
			break;
		}
	}

	/**
	 * Test {@link GameDisplay#init()}.
	 */
	@Test
	public void testInit() {
		// Test init
		d.init();
		// Test basic function once initialized
		d.show();
	}

	/**
	 * Test {@link GameDisplay#isInitialized()}.
	 */
	@Test
	public void testIsInitialized() {
		// Test init
		Assert.assertFalse(d.isInitialized());
		d.init();
		Assert.assertTrue(d.isInitialized());
	}

	/**
	 * Test {@link GameDisplay#isVisible()}.
	 */
	@Test
	public void testIsVisible() {
		// Test visibility
		Assert.assertFalse(d.isVisible());
		d.show();
		Assert.assertTrue(d.isVisible());
	}

	/**
	 * Test {@link GameDisplay#show()}.
	 */
	@Test
	public void testShow() {
		// Test show
		d.show();
		Assert.assertTrue(d.isInitialized());
		Assert.assertTrue(d.isVisible());
	}

	/**
	 * Test {@link GameDisplay#setFrameTitle(String)}.
	 */
	@Test
	public void testSetFrameTitle() {
		// Frame is not initialized, so setting the frame title should do nothing
		d.setFrameTitle("test");
		Assert.assertNull(d.getFrameTitle());

		// Initialize, and it should not be null now
		d.init();
		Assert.assertNotNull(d.getFrameTitle());
		d.setFrameTitle("test");
		Assert.assertEquals("test", d.getFrameTitle());
	}

}
