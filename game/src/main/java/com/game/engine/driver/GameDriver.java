package com.game.engine.driver;

import java.time.Duration;
import java.time.Instant;
import java.util.logging.Level;

import com.game.engine.cache.Cache;
import com.game.engine.display.DisplaySettings;
import com.game.engine.display.GameDisplay;
import com.game.engine.game.AbstractGame;
import com.game.engine.input.Input;
import com.game.engine.input.MouseKeyboard;
import com.game.engine.logger.PowerLogger;

/**
 * The driver for the game. This controls calls to updating and rendering for
 * the game.
 *
 * @author Spencer Imbleau
 * @version June 2020
 */
public class GameDriver implements Runnable {

	/**
	 * The settings for the driver
	 */
	public final DriverSettings settings;

	/**
	 * The cache
	 */
	public final Cache cache;

	/**
	 * The game being played
	 */
	public final AbstractGame game;

	/**
	 * The display for the game
	 */
	private GameDisplay display;

	/**
	 * The input for the game
	 */
	private Input input;

	/**
	 * The game thread
	 */
	private Thread thread;

	/**
	 * Whether the game is running
	 */
	private boolean isRunning;

	/**
	 * The last instant the game updated.
	 */
	private Instant lastUpdate;

	/**
	 * The last instant a frame started to render.
	 */
	private Instant lastFrameStarted;

	/**
	 * The last duration a frame was on screen.
	 */
	private Duration lastFrameTime;

	/**
	 * Construct the game driver
	 *
	 * @param settings - the settings for the game
	 * @param cache    - a cache for reusable assets
	 * @param game     - a game to manage
	 */
	public GameDriver(final DriverSettings settings, final Cache cache, final AbstractGame game) {
		this.settings = settings;
		this.cache = cache;
		this.game = game;
		this.display = null;
		this.input = null;
		this.thread = null;
		this.isRunning = false;
		this.lastUpdate = Instant.MIN;
		this.lastFrameStarted = Instant.MIN;
		this.lastFrameTime = Duration.ZERO;
	}

	/**
	 * Initializes the game driver.
	 *
	 * @param displaySettings - display settings for the driver
	 */
	public void init(DisplaySettings displaySettings) {
		// Start the logger
		PowerLogger.start();

		// Initialize display
		this.display = new GameDisplay(this, displaySettings);
		this.display.init();

		// Initialize game
		this.game.init(this);

		// Start input listening
		this.input = new MouseKeyboard(this);
	}

	/**
	 * Start the game loop on a new thread.
	 *
	 * @param displaySettings - display settings for the game
	 */
	public void start() {
		this.isRunning = true;

		// Thread the game loop
		this.thread = new Thread(this);
		this.thread.start();
	}

	/**
	 * Stop the game loop.
	 */
	public void stop() {
		this.isRunning = false;

		// Stop the game thread
		if (this.thread != null) {
			this.thread.interrupt();
		}

		// Dispose resources - This does not close the display.
		if (this.display != null) {
			this.game.getPlane().dispose(this);
		}

		// Stop the logger
		PowerLogger.stop();
	}

	/**
	 * Run the game loop on the called thread.
	 *
	 * @see GameDriver#start(DisplaySettings)
	 */
	@Override
	public void run() {
		// The buffer for our current time
		Instant instant;
		// The last time since the game loop ran
		Instant lastInstant = Instant.now(settings.getGameClock());
		// The buffer for passed time
		Duration durationPassed;
		// The amount of time passed since the last update
		Duration durationSinceLastUpdate = Duration.ZERO;
		// The amount of time passed since the last frame rendered
		Duration durationSinceLastRender = Duration.ZERO;

		// Reset variables for tracking frames
		this.lastUpdate = Instant.now();
		this.lastFrameStarted = Instant.now();
		this.lastFrameTime = Duration.ZERO;

		// Buffer for whether to render
		boolean render = true;

		// Game loop
		try {
			while (this.isRunning) {
				// Update time
				instant = Instant.now(settings.getGameClock());
				durationPassed = Duration.between(lastInstant, instant);
				lastInstant = instant;

				// Calculate time since last update
				durationSinceLastUpdate = durationSinceLastUpdate.plus(durationPassed);
				// Calculate time since last render if throttling FPS
				if (settings.isFpsRestricted()) {
					durationSinceLastRender = durationSinceLastRender.plus(durationPassed);
					render = durationSinceLastRender.toNanos() >= settings.getFrameDuration().toNanos();
				}

				// Catch up on game ticks if behind
				while (durationSinceLastUpdate.toNanos() >= settings.getTickDuration().toNanos()) {
					durationSinceLastUpdate = durationSinceLastUpdate.minus(settings.getTickDuration());

					this.display.settings.getCamera().update(this);
					this.game.update(this);
					this.input.update();
					this.lastUpdate = Instant.now();
				}

				// Render if allowed
				if (render || !settings.isFpsRestricted()) {
					// Collect render requests
					this.game.stage(this, this.display.getRenderer());
					// Measure frame rendering
					Instant now = Instant.now();
					this.lastFrameTime = Duration.between(this.lastFrameStarted, now);
					this.lastFrameStarted = now;
					// Render the screen
					this.display.getRenderer().render();

					// If FPS is restricted, keep track
					if (settings.isFpsRestricted()) {
						durationSinceLastRender = durationSinceLastRender.minus(settings.getFrameDuration());
					}
				} else {
					// Alleviate the CPU by sleeping until needed next
					// Calculate time until next render or update
					Duration timeTillTick = settings.getTickDuration().minus(durationSinceLastUpdate);
					Duration timeTillRender = settings.getFrameDuration().minus(durationSinceLastRender);
					long millisTillEvent = Math.min(timeTillTick.toMillis(), timeTillRender.toMillis());

					if (millisTillEvent > 0) {
						Thread.sleep(millisTillEvent);
					}
				}
			}
		} catch (InterruptedException | RuntimeException e) {
			this.isRunning = false;
			// Log the exception if it was not caused by the developer
			if (!(e.getCause() instanceof InterruptedException)) {
				PowerLogger.LOGGER.log(Level.SEVERE, "Non-recoverable runtime exception ocurred", e);
			}
		}

	}

	/**
	 * @return the game display, or null, if the driver has not started
	 */
	public GameDisplay getDisplay() {
		return this.display;
	}

	/**
	 * Sets the input for the driver
	 *
	 * @param input - an input source
	 */
	public void setInput(Input input) {
		this.input = input;
	}

	/**
	 * @return the input, or null, if the driver has not started
	 */
	public Input getInput() {
		return this.input;
	}

	/**
	 * @return the duration the last frame was on screen
	 */
	public Duration getFrameTime() {
		return this.lastFrameTime;
	}

	/**
	 * @return the current delta time between rendering and the last update
	 */
	public Duration getFrameDt() {
		return Duration.between(this.lastUpdate, this.lastFrameStarted);
	}

	/**
	 * @return true if the game loop is running
	 */
	public boolean isRunning() {
		return this.isRunning;
	}
}
