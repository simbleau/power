package com.game.engine.logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * A monostate logger which can be accessed in any context.
 * 
 * @author Spencer Imbleau
 * @version December 2020
 */
public class PowerLogger {

	/**
	 * The path for the logger properties.
	 */
	private static final Path PROPERTIES = Paths.get("src", "main", "java", "com", "game", "engine", "logger",
			"logging.properties");

	/**
	 * The logger for the game
	 */
	public static final Logger LOGGER;

	static {
		// Initialize the log configuration
		try {
			LogManager.getLogManager().readConfiguration(new FileInputStream(PROPERTIES.toString()));
		} catch (SecurityException | IOException e) {
			System.err.println("Could not read properties: " + PROPERTIES.toAbsolutePath().toString());
			e.printStackTrace();
			System.exit(1);
		}
		LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	}

	/**
	 * Start the logger.
	 */
	public static void start() {
		// Add handlers for logger
		LOGGER.addHandler(new ConsoleHandler());
		try {
			LOGGER.addHandler(new FileHandler());
			LOGGER.info("File logging initiated.");
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "File logging could not be initialized", e);
		}
	}

	/**
	 * Stop the logger. This clears all handlers tied to the logger and closes any
	 * open file streams.
	 */
	public static void stop() {
		// Close all handlers
		Stream.of(LOGGER.getHandlers()).forEach(handler -> handler.close());
	}
}
