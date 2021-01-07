package com.game.engine.camera;

import java.awt.event.KeyEvent;

import com.game.engine.driver.GameDriver;
import com.game.engine.game.Position2D;
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
	 * Initialize a dev camera.
	 * 
	 * @param x      - starting x position
	 * @param y      - starting y position
	 * @param width  - width of the viewport
	 * @param height - height of the viewport
	 * @param zoom   - starting zoom
	 */
	public DevCamera(double x, double y, int width, int height, double zoom) {
		super(x, y, width, height, zoom);
	}

	@Override
	public void update(GameDriver driver) {
		super.update(driver);

		if (driver.getInput().isKeyActive(MouseKeyboard.KEYS.MOVE_UP)) {
			this.translateY(-PAN_SPEED);
		}
		if (driver.getInput().isKeyActive(MouseKeyboard.KEYS.MOVE_DOWN)) {
			this.translateY(PAN_SPEED);
		}
		if (driver.getInput().isKeyActive(MouseKeyboard.KEYS.MOVE_LEFT)) {
			this.translateX(-PAN_SPEED);
		}
		if (driver.getInput().isKeyActive(MouseKeyboard.KEYS.MOVE_RIGHT)) {
			this.translateX(PAN_SPEED);
		}

		if (driver.getInput().isKeyActive(KeyEvent.VK_UP)) {
			Position2D focus = this.focus();
			this.magnify(ZOOM_SPEED);
			this.lookAt(focus.x(), focus.y());
		}
		if (driver.getInput().isKeyActive(KeyEvent.VK_DOWN)) {
			Position2D focus = this.focus();
			this.magnify(-ZOOM_SPEED);
			this.lookAt(focus.x(), focus.y());
		}
	}

}
