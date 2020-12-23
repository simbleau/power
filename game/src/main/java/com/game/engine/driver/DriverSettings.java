package com.game.engine.driver;

import java.io.IOException;
import java.net.InetAddress;
import java.time.Clock;
import java.time.Duration;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

/**
 * The settings for a game client
 *
 * @author Spencer Imbleau
 * @version November 2020
 */
public class DriverSettings {

	/**
	 * The clock for time synchronization
	 */
	private Clock gameClock = Clock.systemDefaultZone();

	/**
	 * The server's game clock.
	 */
	private Clock serverClock = null;

	/**
	 * The game updates (ticks) per second.
	 */
	private int ticksPerSecond;

	/**
	 * The duration between ticks.
	 */
	private Duration tickDuration;

	/**
	 * Whether the frames per second is restricted to a maximum.
	 */
	private boolean isFpsRestricted;

	/**
	 * The frames per second goal, if frames per second is restricted.
	 */
	private int framesPerSecondGoal;

	/**
	 * The duration between frames rendering if FPS is restricted.
	 */
	private Duration frameDuration;

	/**
	 * Construct driver settings
	 *
	 * @param tps - the ticks per second
	 */
	public DriverSettings(int tps) {
		setTicksPerSecond(tps);
		unrestrictFPS();
	}

	/**
	 * Construct driver settings with restricted FPS
	 *
	 * @param tps           - the ticks per second
	 * @param restrictedFps - the restricted FPS
	 */
	public DriverSettings(int tps, int restrictedFps) {
		setTicksPerSecond(tps);
		restrictFPS(restrictedFps);
	}

	/**
	 * @return the game clock
	 */
	public Clock getGameClock() {
		return this.gameClock;
	}

	/**
	 * @return the server clock, or null if the clock has not been initialized.
	 */
	public Clock getServerClock() {
		return this.serverClock;
	}

	/**
	 * @return true if the server clock is not initialized, false otherwise.
	 */
	public boolean isServerClockInitialized() {
		return this.serverClock != null;
	}

	/**
	 * Initialize a server clock based on an NTP server address.
	 * 
	 * @param server  - an NTP server address
	 * @param timeout - the timeout for reaching the NTP server
	 * @throws IOException on server contact failure
	 */
	public void initServerClock(String server, int timeout) throws IOException {
		NTPUDPClient client = new NTPUDPClient();
		client.setDefaultTimeout(timeout);

		InetAddress inetAddress = InetAddress.getByName(server);
		TimeInfo timeInfo = client.getTime(inetAddress);
		timeInfo.computeDetails();

		this.serverClock = Clock.offset(Clock.systemUTC(), Duration.ofMillis(timeInfo.getOffset()));
	}

	/**
	 * Set the ticks per second
	 *
	 * @param tps - the ticks per second
	 * @throws IllegalArgumentException if you provide a non-positive integer
	 */
	public void setTicksPerSecond(int tps) {
		if (tps <= 0) {
			throw new IllegalArgumentException("Ticks per second must be a positive integer");
		}
		this.ticksPerSecond = tps;
		this.tickDuration = Duration.ofSeconds(1).dividedBy(tps);
	}

	/**
	 * @return the ticks per second
	 */
	public int getTicksPerSecond() {
		return this.ticksPerSecond;
	}

	/**
	 * @return the duration between game ticks
	 */
	public Duration getTickDuration() {
		return this.tickDuration;
	}

	/**
	 * @return whether the frames per second is capped
	 */
	public boolean isFpsRestricted() {
		return this.isFpsRestricted;
	}

	/**
	 * Cap the frames per second to {@link #frameDuration}
	 * 
	 * @param fps - the frames per second to restrict
	 */
	public void restrictFPS(int fps) {
		this.framesPerSecondGoal = fps;
		this.frameDuration = Duration.ofSeconds(1).dividedBy(fps);
		this.isFpsRestricted = true;
	}

	/**
	 * Uncap the frames per second
	 */
	public void unrestrictFPS() {
		this.framesPerSecondGoal = 0;
		this.frameDuration = Duration.ZERO;
		this.isFpsRestricted = false;
	}

	/**
	 * @return the frames per second goal if FPS is restricted, or zero.
	 */
	public int getFPSGoal() {
		return this.framesPerSecondGoal;
	}

	/**
	 * @return the duration between rendering frames if FPS is restricted, or zero.
	 */
	public Duration getFrameDuration() {
		return this.frameDuration;
	}

}
