package com.game.engine.driver;

import java.io.IOException;
import java.net.InetAddress;
import java.time.Clock;
import java.time.Duration;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests {@link DriverSettings}.
 * 
 * @author Spencer Imbleau
 * @version November 2020
 */
public class TestDriverSettings {

	/**
	 * An arbitrary ticks per second to limit to.
	 */
	private static final int TEST_TPS = 30;

	/**
	 * An arbitrary frames per second to limit to.
	 */
	private static final int TEST_FPS = 60;

	/**
	 * The delta milliseconds which a clock can be incorrect by to be considered
	 * correct.
	 */
	private static final long TEST_TIME_DELTA_MS = 5;

	/**
	 * The NTP server to test with.
	 */
	private static final String TEST_NTP_SERVER = "pool.ntp.org";

	/**
	 * The timeout for reaching the NTP server.
	 */
	private static final int TEST_TIMEOUT_MS = 5000;

	/**
	 * Test {@link DriverSettings#create(int)}.
	 */
	@Test
	public void testCreate1() {
		// Initialize values
		DriverSettings d = DriverSettings.create(TEST_TPS);

		// Test
		Assert.assertEquals(TEST_TPS, d.getTicksPerSecond());
		Assert.assertEquals(0, d.getFPSGoal());
		Assert.assertEquals(Duration.ZERO, d.getFrameDuration());
	}

	/**
	 * Test {@link DriverSettings#create(int, int)}.
	 */
	@Test
	public void testCreate2() {
		// Initialize values
		DriverSettings d = DriverSettings.create(TEST_TPS, TEST_FPS);

		// Test
		Assert.assertEquals(TEST_TPS, d.getTicksPerSecond());
		Assert.assertEquals(TEST_FPS, d.getFPSGoal());
		Assert.assertEquals(Duration.ofSeconds(1).dividedBy(TEST_FPS), d.getFrameDuration());
	}

	/**
	 * Test {@link DriverSettings#getFPSGoal()}.
	 */
	@Test
	public void testGetFPSGoal() {
		// Initialize values
		DriverSettings d = DriverSettings.create(TEST_TPS);

		// Test
		Assert.assertEquals(0, d.getFPSGoal());
		d.restrictFPS(TEST_FPS);
		Assert.assertEquals(TEST_FPS, d.getFPSGoal());
	}

	/**
	 * Test {@link DriverSettings#getFrameDuration()}.
	 */
	@Test
	public void testGetFrameDuration() {
		// Initialize values
		DriverSettings d = DriverSettings.create(TEST_TPS);

		// Test
		Assert.assertEquals(Duration.ZERO, d.getFrameDuration());
		d.restrictFPS(TEST_FPS);
		Assert.assertEquals(Duration.ofSeconds(1).dividedBy(TEST_FPS), d.getFrameDuration());
	}

	/**
	 * Test {@link DriverSettings#getGameClock()}.
	 */
	@Test
	public void testGetGameClock() {
		// Initialize values
		DriverSettings d = DriverSettings.create(TEST_TPS);

		// Test time
		Assert.assertEquals(System.currentTimeMillis(), d.getGameClock().millis());
	}

	/**
	 * Test {@link DriverSettings#getServerClock()}.
	 */
	@Test
	public void testGetServerClock() {
		// Initialize values
		DriverSettings d = DriverSettings.create(TEST_TPS);
		try {
			d.initServerClock(TEST_NTP_SERVER, TEST_TIMEOUT_MS);
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail("Server clock could not be initialized.");
		}

		// Test server time to an NTP server
		try {
			// Make an NTP clock to compare against server clock
			NTPUDPClient client = new NTPUDPClient();
			InetAddress inetAddress = InetAddress.getByName(TEST_NTP_SERVER);
			TimeInfo timeInfo = client.getTime(inetAddress);
			timeInfo.computeDetails();

			Clock ntpClock = Clock.offset(Clock.systemUTC(), Duration.ofMillis(timeInfo.getOffset()));
			long serverMillis = d.getServerClock().withZone(ntpClock.getZone()).millis();
			long ntpMillis = ntpClock.millis();
			long offset = Math.abs(serverMillis - ntpMillis);

			Assert.assertTrue("Time difference between NTP and Server clock was " + offset + "ms",
					offset <= TEST_TIME_DELTA_MS);
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail("NTP server clock could not be initialized.");
		}
	}

	/**
	 * Test {@link DriverSettings#getTickDuration()}.
	 */
	@Test
	public void testGetTickDuration() {
		// Initialize values
		DriverSettings d = DriverSettings.create(TEST_TPS);

		// Test
		Assert.assertEquals(Duration.ofSeconds(1).dividedBy(TEST_TPS), d.getTickDuration());
	}

	/**
	 * Test {@link DriverSettings#getTicksPerSecond()}.
	 */
	@Test
	public void testGetTicksPerSecond() {
		// Initialize values
		DriverSettings d = DriverSettings.create(TEST_TPS);

		// Test
		Assert.assertEquals(TEST_TPS, d.getTicksPerSecond());
	}

	/**
	 * Test {@link DriverSettings#initServerClock(String, int)}.
	 */
	@Test
	public void testInitServerClock() {
		// Initialize values
		DriverSettings d = DriverSettings.create(TEST_TPS);

		try {
			d.initServerClock(TEST_NTP_SERVER, TEST_TIMEOUT_MS);
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail("Server clock could not be initialized.");
		}

		// Test it was initialized
		Assert.assertTrue(d.isServerClockInitialized());
	}

	/**
	 * Test {@link DriverSettings#isFpsRestricted()}.
	 */
	@Test
	public void testIsFPSRestricted() {
		// Initialize values
		DriverSettings d = DriverSettings.create(TEST_TPS);

		// Test
		Assert.assertFalse(d.isFpsRestricted());
		d.restrictFPS(TEST_FPS);
		Assert.assertTrue(d.isFpsRestricted());
	}

	/**
	 * Test {@link DriverSettings#isServerClockInitialized()}.
	 */
	@Test
	public void testIsServerClockInitialized() {
		// Initialize values
		DriverSettings d = DriverSettings.create(TEST_TPS);

		// Test
		Assert.assertFalse(d.isServerClockInitialized());
		try {
			d.initServerClock(TEST_NTP_SERVER, TEST_TIMEOUT_MS);
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail("Could not test method due to initialization exception.");
		}
		Assert.assertTrue(d.isServerClockInitialized());
	}

	/**
	 * Test {@link DriverSettings#restrictFPS(int)}.
	 */
	@Test
	public void testRestrictFps() {
		// Initialize values
		DriverSettings d = DriverSettings.create(TEST_TPS);
		Duration testFpsDuration = Duration.ofSeconds(1).dividedBy(TEST_FPS);

		// Test
		d.restrictFPS(TEST_FPS);
		Assert.assertEquals(TEST_FPS, d.getFPSGoal());
		Assert.assertEquals(testFpsDuration, d.getFrameDuration());
	}

	/**
	 * Test {@link DriverSettings#setTicksPerSecond(int)}.
	 */
	@Test
	public void testSetTicksPerSecond() {
		// Initialize values
		DriverSettings d = DriverSettings.create(1);
		Duration testTickDuration = Duration.ofSeconds(1).dividedBy(TEST_TPS);

		// Test
		d.setTicksPerSecond(TEST_TPS);
		Assert.assertEquals(testTickDuration, d.getTickDuration());
		Assert.assertEquals(TEST_TPS, d.getTicksPerSecond());
	}

	/**
	 * Test {@link DriverSettings#unrestrictFPS()}.
	 */
	@Test
	public void testUnrestrictFps() {
		// Initialize values
		DriverSettings d = DriverSettings.create(TEST_TPS, TEST_FPS);

		// Test
		d.unrestrictFPS();
		Assert.assertEquals(0, d.getFPSGoal());
		Assert.assertEquals(Duration.ZERO, d.getFrameDuration());
		Assert.assertFalse(d.isFpsRestricted());
	}
}