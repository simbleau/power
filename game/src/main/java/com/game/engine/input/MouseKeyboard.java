package com.game.engine.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.game.engine.driver.GameDriver;

/**
 * A mouse and keyboard input
 *
 * @version June 2020
 * @author Spencer Imbleau
 */
public class MouseKeyboard implements Input, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

	/**
	 * Important keys
	 *
	 * @author Spencer Imbleau
	 * @version June 2020
	 */
	public static class KEYS {
		/**
		 * Moves the player up
		 */
		public static final int MOVE_UP = KeyEvent.VK_W;
		/**
		 * Moves the player left
		 */
		public static final int MOVE_LEFT = KeyEvent.VK_A;
		/**
		 * Moves the player down
		 */
		public static final int MOVE_DOWN = KeyEvent.VK_S;
		/**
		 * Moves the player right
		 */
		public static final int MOVE_RIGHT = KeyEvent.VK_D;
	}

	/**
	 * Important buttons
	 *
	 * @author Spencer Imbleau
	 * @version June 2020
	 */
	public static class BUTTONS {
		/** The button code for a left click */
		public static final int LEFT_CLICK = 1;
		/** The button code for a wheel click */
		public static final int WHEEL_CLICK = 2;
		/** The button code for a right click */
		public static final int RIGHT_CLICK = 3;
	}

	/**
	 * Important scroll directions
	 *
	 * @author Spencer Imbleau
	 * @version June 2020
	 */
	public static class SCROLL {
		/**
		 * The value analogous to scrolling up
		 */
		public static final int UP = -1;
		/**
		 * The value analogous to not scrolling
		 */
		public static final int IDLE = 0;
		/**
		 * The value analogous to scrolling down
		 */
		public static final int DOWN = 1;
	}

	/**
	 * The amount of keys to listen to
	 */
	public static final int NUM_KEYS = 256;
	/**
	 * The amount of mouse buttons to listen to
	 */
	public static final int NUM_BUTTONS = 3;

	/**
	 * The game driver
	 */
	private GameDriver driver;

	/**
	 * The position X coordinate of our mouse
	 */
	private int x;

	/**
	 * The position Y coordinate of our mouse
	 */
	private int y;

	/**
	 * The position X coordinate of our mouse relative to the plane.
	 */
	private int planeX;

	/**
	 * The position Y coordinate of our mouse relative to the plane.
	 */
	private int planeY;

	/**
	 * The scroll direction
	 */
	private int scroll;

	/**
	 * The states of keyboard keys. True if down, false otherwise
	 */
	private boolean[] keys = new boolean[NUM_KEYS];

	/**
	 * The previous states of keyboard keys from the last update
	 */
	private boolean[] keysLast = new boolean[NUM_KEYS];

	/**
	 * The states of mouse buttons. True if down, false otherwise
	 */
	private boolean[] buttons = new boolean[NUM_BUTTONS];

	/**
	 * The previous states of mouse buttons from the last update
	 */
	private boolean[] buttonsLast = new boolean[NUM_BUTTONS];

	/**
	 * Initializes the mouse and keyboard input
	 *
	 * @param driver - The game driver to bound input to
	 */
	public MouseKeyboard(GameDriver driver) {
		this.driver = driver;

		this.x = 0;
		this.y = 0;
		this.planeX = 0;
		this.planeY = 0;
		this.scroll = 0;

		// Key listeners
		this.driver.getDisplay().getRenderer().getCanvas().addKeyListener(this);
		// Enables keys such as Tab to be captured
		this.driver.getDisplay().getRenderer().getCanvas().setFocusTraversalKeysEnabled(false);

		// Mouse listeners
		this.driver.getDisplay().getRenderer().getCanvas().addMouseListener(this);
		this.driver.getDisplay().getRenderer().getCanvas().addMouseMotionListener(this);
		this.driver.getDisplay().getRenderer().getCanvas().addMouseWheelListener(this);
	}

	@Override
	public void update() {
		// Reset scroll
		this.scroll = 0;

		// Archive key states
		for (int i = 0; i < NUM_KEYS; i++) {
			this.keysLast[i] = this.keys[i];
		}

		// Archive button states
		for (int i = 0; i < NUM_BUTTONS; i++) {
			this.buttonsLast[i] = this.buttons[i];
		}
	}

	@Override
	public boolean isKeyActive(int code) {
		return (code < NUM_KEYS) ? this.keys[code] : false;
	}

	@Override
	public boolean isKeyUp(int code) {
		return (code < NUM_KEYS) ? !this.keys[code] && this.keysLast[code] : false;
	}

	@Override
	public boolean isKeyDown(int code) {
		return (code < NUM_KEYS) ? this.keys[code] && !this.keysLast[code] : false;
	}

	@Override
	public boolean isButtonActive(int button) {
		return (button < NUM_BUTTONS) ? this.buttons[button] : false;
	}

	@Override
	public boolean isButtonUp(int button) {
		return (button < NUM_BUTTONS) ? !this.buttons[button] && this.buttonsLast[button] : false;
	}

	@Override
	public boolean isButtonDown(int button) {
		return (button < NUM_BUTTONS) ? this.buttons[button] && !this.buttonsLast[button] : false;
	}

	@Override
	public int x() {
		return this.x;
	}

	@Override
	public int y() {
		return this.y;
	}

	@Override
	public int planeX() {
		return this.planeX;
	}

	@Override
	public int planeY() {
		return this.planeY;
	}

	@Override
	public int getScroll() {
		return this.scroll;
	}

	// ==========================================
	// ========MOUSE AND KEY EVENTS BELOW========
	// ==========================================

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		this.scroll = e.getWheelRotation();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		this.x = e.getX();
		this.y = e.getY();
		this.planeX = Math.max(0,
				Math.min(this.driver.game.getPlane().width,
						(int) (this.x / this.driver.getDisplay().getRenderer().getCamera().zoom()
								+ this.driver.getDisplay().getRenderer().getCamera().viewport.x())));
		this.planeY = Math.max(0,
				Math.min(this.driver.game.getPlane().height,
						(int) (this.y / this.driver.getDisplay().getRenderer().getCamera().zoom()
								+ this.driver.getDisplay().getRenderer().getCamera().viewport.y())));
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() < NUM_BUTTONS) {
			this.buttons[e.getButton()] = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() < NUM_BUTTONS) {
			this.buttons[e.getButton()] = false;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// Do nothing
		e.consume();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// Do nothing
		e.consume();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// Do nothing
		e.consume();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() < NUM_KEYS) {
			this.keys[e.getKeyCode()] = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() < NUM_KEYS) {
			this.keys[e.getKeyCode()] = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// Do nothing
		e.consume();
	}

}