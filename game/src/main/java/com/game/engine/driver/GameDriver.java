package com.game.engine.driver;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Stream;

import com.game.engine.cache.Cache;
import com.game.engine.display.DisplaySettings;
import com.game.engine.display.GameDisplay;
import com.game.engine.game.AbstractGame;
import com.game.engine.input.Input;
import com.game.engine.input.MouseKeyboard;

/**
 * The driver for the game. This controls calls to updating and rendering for
 * the game.
 *
 * @author Spencer Imbleau
 * @version June 2020
 */
public class GameDriver implements Runnable {

	/**
	 * The path for the logger properties.
	 */
	private static final Path PROPERTIES = Paths.get("src", "main", "java", "com", "game", "engine", "logger",
			"logging.properties");

	/**
	 * The logger for the game
	 */
	public final Logger logger;

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
	 * The frames per second
	 */
	private int fps;

	/**
	 * Whether the game is running
	 */
	private boolean isRunning;

	static {
		try {
			LogManager.getLogManager().readConfiguration(new FileInputStream(PROPERTIES.toString()));
		} catch (SecurityException | IOException e) {
			System.err.println("Could not read properties: " + PROPERTIES.toAbsolutePath().toString());
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Construct the game driver
	 *
	 * @param settings - the settings for the game
	 * @param cache    - a cache for reusable assets
	 * @param game     - a game to manage
	 */
	public GameDriver(final DriverSettings settings, final Cache cache, final AbstractGame game) {
		this.logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		this.settings = settings;
		this.cache = cache;
		this.game = game;
		this.display = null;
		this.input = null;
		this.thread = null;
		this.isRunning = false;
		this.fps = 0;
	}

	/**
	 * Start the game loop
	 *
	 * @param displaySettings - display settings for the game
	 */
	public void start(DisplaySettings displaySettings) {
		// Add handlers for logger
		this.logger.addHandler(new ConsoleHandler());
		try {
			this.logger.addHandler(new FileHandler());
			this.logger.info("File logging initiated.");
		} catch (IOException e) {
			this.logger.log(Level.WARNING, "File logging could not be initialized", e);
		}
		
		// Initialize display
		this.display = new GameDisplay(this, displaySettings);
		this.display.init();

		// Initialize game
		this.game.init(this);

		// Start input listening
		this.input = new MouseKeyboard(this);

		// Start the game
		this.thread = new Thread(this);
		this.thread.start();
		this.isRunning = true;
	}

	/**
	 * Stop the game loop.
	 */
	public void stop() {
		if (this.thread != null) {
			this.thread.interrupt();
		}
		this.isRunning = false;
		Stream.of(this.logger.getHandlers()).forEach(handler -> handler.close());
	}

	/**
	 * Run the game loop. This should not be called by the developer, instead invoke
	 * start, which spawns a thread to call this runnable.
	 * 
	 * @see GameDriver#start(DisplaySettings)
	 */
	@Override
	public void run() {
		this.isRunning = true;

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

		// Variables for tracking FPS
		Duration frameTime = Duration.ZERO;
		int frames = 0;
		this.fps = 0;

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
				// Keep track of FPS
				frameTime = frameTime.plus(durationPassed);

				// Catch up on game ticks if behind
				while (durationSinceLastUpdate.toNanos() >= settings.getTickDuration().toNanos()) {
					durationSinceLastUpdate = durationSinceLastUpdate.minus(settings.getTickDuration());

					this.display.settings.getCamera().update(this);
					this.game.update(this);
					this.input.update();
				}

				// Check if we have surpassed a second of time for FPS measurement
				if (frameTime.getSeconds() >= 1) {
					this.fps = frames;
					frameTime = Duration.ZERO;
					frames = 0;
				}

				// Render if allowed
				if (render || !settings.isFpsRestricted()) {
					// Collect render requests
					this.game.stage(this, this.display.getRenderer());
					// Render the screen
					this.display.getRenderer().render();
					frames++;

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
		} catch (RuntimeException | InterruptedException e) {
			// Interrupted by developer or non-recoverable error - Stop running
			this.isRunning = false;
			e.printStackTrace();
		}

	}

	/**
	 * @return the game display, or null, if the driver has not started
	 */
	public GameDisplay getDisplay() {
		return this.display;
	}

	/**
	 * @return the input, or null, if the driver has not started
	 */
	public Input getInput() {
		return this.input;
	}

	/**
	 * @return the frames per second
	 */
	public int getFps() {
		return this.fps;
	}

	/**
	 * @return true if the game loop is running
	 */
	public boolean isRunning() {
		return this.isRunning;
	}
}
