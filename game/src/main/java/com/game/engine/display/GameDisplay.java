package com.game.engine.display;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.game.engine.driver.GameDriver;
import com.game.engine.rendering.cpu.CPURenderer;
import com.game.engine.rendering.opengl.JOGLRenderer;
import com.game.engine.rendering.common.AbstractRenderer;
import com.game.engine.rendering.common.RenderMode;

/**
 * The manager for the game frame
 *
 * @author Spencer Imbleau
 * @version June 2020
 */
public class GameDisplay {

	/**
	 * The driver for the game.
	 */
	public final GameDriver driver;

	/**
	 * The display settings.
	 */
	public final DisplaySettings settings;

	/**
	 * The frame for the game.
	 */
	private JFrame frame;

	/**
	 * The renderer used for graphics.
	 */
	private AbstractRenderer renderer;

	/**
	 * Construct the game display
	 *
	 * @param driver          - the game driver
	 * @param displaySettings - the display settings to use
	 */
	public GameDisplay(GameDriver driver, DisplaySettings displaySettings) {
		this.driver = driver;
		this.settings = displaySettings;
		this.frame = null;

		// Select renderer
		switch (this.settings.getRenderingMode()) {
		case OPENGL:
			this.renderer = new JOGLRenderer(this);
			break;
		case SAFE:
		default:
			this.renderer = new CPURenderer(this);
			break;
		}
	}

	/**
	 * Initialize the display.
	 */
	public void init() {
		// Initialize renderer
		try {
			this.renderer.init();
		} catch (Exception e) {
			// TODO : send to logger as warning
			e.printStackTrace();
			// Default to safe mode
			this.settings.setRenderingMode(RenderMode.SAFE);
			this.renderer = new CPURenderer(this);
			try {
				// There's no recovering from this if it fails
				this.renderer.init();
			} catch (Exception e1) {
				// This should literally never happen.
				// TODO : send to logger as error
				e1.printStackTrace();
				System.exit(1);
			}
		}

		// Create frame
		this.frame = new JFrame();
		this.frame.setResizable(true);
		this.frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				driver.stop();
			}
		});

		this.frame.add(this.renderer.getCanvas(), BorderLayout.CENTER);

		// Set minimum dimensions for the canvas
		Dimension minRes = new Dimension(this.driver.game.MIN_RESOLUTION_WIDTH, this.driver.game.MIN_RESOLUTION_HEIGHT);
		this.renderer.getCanvas().setMinimumSize(minRes);
		this.renderer.getCanvas().setSize(minRes);
		this.frame.pack();
		this.frame.setMinimumSize(this.frame.getSize());

		// Set preferred dimensions for the canvas
		this.renderer.getCanvas().setPreferredSize(this.settings.getPreferredResolution());
		this.renderer.getCanvas().setSize(this.settings.getPreferredResolution());
		this.frame.pack();
		this.frame.setPreferredSize(this.frame.getSize());

		// If there's a display setting in the future to start maximized:
		// this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

		// Create buffering strategy for canvas (now appropriately sized)
		this.renderer.getCanvas().createBufferStrategy(2); // Double buffering
		// Show frame
		this.frame.setLocationRelativeTo(null); // Start center-screen

		// Resize camera viewport to canvas
		this.settings.getCamera().viewport.resize(this.renderer.getCanvas().getWidth(),
				this.renderer.getCanvas().getHeight());

		this.frame.setVisible(true);
	}

	/**
	 * @return true if the display has been initialized, false otherwise
	 */
	public boolean isInitialized() {
		return this.frame != null;
	}

	/**
	 * @return true if the display exists and is visible, false otherwise
	 */
	public boolean isVisible() {
		if (this.frame != null) {
			return this.frame.isVisible();
		} else {
			return false;
		}
	}

	/**
	 * @return the frame's title, or null if the frame is not initialized
	 */
	public String getFrameTitle() {
		if (this.frame != null) {
			return this.frame.getTitle();
		} else {
			return null;
		}
	}

	/**
	 * Sets the title of the frame, if it exists. If the frame does not exist,
	 * nothing happens.
	 *
	 * @param frameTitle - the new title
	 */
	public void setFrameTitle(String frameTitle) {
		if (this.frame != null) {
			this.frame.setTitle(frameTitle);
		}
	}

	/**
	 * Close the display window, if it exists.
	 */
	public void close() {
		if (this.frame != null) {
			this.frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			this.frame = null;
		}
	}

	/**
	 * Show the display window. This will initialize the display if it is not
	 * initialized already.
	 */
	public void show() {
		if (this.frame == null) {
			init();
		}
		// Show frame
		this.frame.setLocationRelativeTo(null); // Start center-screen
		this.frame.setVisible(true);
	}

	/**
	 * @return the renderer
	 */
	public AbstractRenderer getRenderer() {
		return this.renderer;
	}

	/**
	 * @return true if the renderer is an OpenGL renderer, false otherwise
	 */
	public boolean isGL() {
		return this.settings.getRenderingMode() == RenderMode.OPENGL;
	}

	/**
	 * @return true if the renderer is a safe renderer, false otherwise
	 */
	public boolean isSafe() {
		return this.settings.getRenderingMode() == RenderMode.OPENGL;
	}
}
