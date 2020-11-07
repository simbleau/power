package com.game.engine.camera;

import java.awt.event.KeyEvent;

import com.game.engine.driver.GameDriver;
import com.game.engine.input.MouseKeyboard;

/**
 * A developer "no-clip" camera for debugging.
 * 
 * @author Spencer Imbleau
 * @version October 2020
 */
public class DevCamera extends AbstractCamera {

	/**
	 * The incremental amount for panning.
	 */
	private static final double PAN_SPEED = 20;

	/**
	 * The incremental amount for zooming.
	 */
	private static final double ZOOM_SPEED = 0.03;

	/**
	 * Initialize a dev camera at origin with standard zoom.
	 */
	public DevCamera() {
		super(0, 0, 1);
	}

	/**
	 * Initialize a dev camera at given coordinate with standard zoom.
	 * 
	 * @param x - starting x position
	 * @param y - starting y position
	 */
	public DevCamera(double x, double y) {
		super(x, y, 1);
	}

	/**
	 * Initialize a dev camera at given coordinate and zoom.
	 * 
	 * @param x    - starting x position
	 * @param y    - starting y position
	 * @param zoom - starting zoom
	 */
	public DevCamera(double x, double y, double zoom) {
		super(x, y, zoom);
	}

	@Override
	public void update(GameDriver driver) {
		if (driver.getInput().isKeyDown(MouseKeyboard.KEYS.MOVE_UP)) {
			this.y -= PAN_SPEED;
		}
		if (driver.getInput().isKeyDown(MouseKeyboard.KEYS.MOVE_DOWN)) {
			this.y += PAN_SPEED;
		}
		if (driver.getInput().isKeyDown(MouseKeyboard.KEYS.MOVE_LEFT)) {
			this.x -= PAN_SPEED;
		}
		if (driver.getInput().isKeyDown(MouseKeyboard.KEYS.MOVE_RIGHT)) {
			this.x += PAN_SPEED;
		}

		if (driver.getInput().isKeyDown(KeyEvent.VK_UP)) {
			this.zoom += ZOOM_SPEED;
		}
		if (driver.getInput().isKeyDown(KeyEvent.VK_DOWN)) {
			this.zoom -= ZOOM_SPEED;
		}
	}

	@Override
	public double x() {
		return this.x;
	}

	@Override
	public double y() {
		return this.y;
	}

}
