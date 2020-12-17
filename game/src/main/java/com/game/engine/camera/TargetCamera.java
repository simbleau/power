package com.game.engine.camera;

import java.awt.event.KeyEvent;

import com.game.engine.coordinates.CoordinateMatrix;
import com.game.engine.driver.GameDriver;
import com.game.engine.game.AbstractGameObject;
import com.game.engine.input.MouseKeyboard;

/**
 * A camera which follows an object at an exponential rate.
 * 
 * @author Spencer Imbleau
 * @version December 2020
 */
public class TargetCamera extends AbstractCamera {

	/**
	 * The following speed of the camera.
	 */
	private static final double FOLLOW_SPEED = 2;

	/**
	 * The incremental amount for panning.
	 */
	private static final double PAN_SPEED = 100;

	/**
	 * The incremental amount for zooming.
	 */
	private static final double ZOOM_SPEED = 0.03;

	/**
	 * The target for the camera to follow.
	 */
	private AbstractGameObject target;

	/**
	 * Construct a target camera with a target.
	 * 
	 * @param x      - starting x co-ordinate origin for the camera
	 * @param y      - starting y co-ordinate origin for the camera
	 * @param width  - viewport width of the camera
	 * @param height - viewport height of the camera
	 * @param zoom   - starting zoom of the camera
	 * @param target - a target for the camera to follow
	 */
	public TargetCamera(double x, double y, int width, int height, double zoom, AbstractGameObject target) {
		super(x, y, width, height, zoom);
		this.target = target;
	}

	/**
	 * Construct a target camera with no target.
	 * 
	 * @param x      - starting x co-ordinate origin for the camera
	 * @param y      - starting y co-ordinate origin for the camera
	 * @param width  - viewport width of the camera
	 * @param height - viewport height of the camera
	 * @param zoom   - starting zoom of the camera
	 */
	public TargetCamera(double x, double y, int width, int height, double zoom) {
		this(x, y, width, height, zoom, null);
	}

	/**
	 * Set a new target for the camera to follow.
	 * 
	 * @param obj - the object to follow
	 */
	public void setTarget(AbstractGameObject obj) {
		this.target = obj;
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
			CoordinateMatrix focus = this.focus();
			this.magnify(ZOOM_SPEED);
			this.lookAt(focus.x(), focus.y());
		}
		if (driver.getInput().isKeyActive(KeyEvent.VK_DOWN)) {
			CoordinateMatrix focus = this.focus();
			this.magnify(-ZOOM_SPEED);
			this.lookAt(focus.x(), focus.y());
		}

		if (this.target == null) {
			return;
		}

		// Declare target focus
		CoordinateMatrix focus = CoordinateMatrix.create(this.target.x(), this.target.y());
		// Center the focus on the screen
		focus.translateEquals(-(this.viewport.width / this.zoom / 2), -(this.viewport.height / this.zoom / 2));
		// Springarm bounds
		focus.setX(Math.min(focus.x(), driver.game.getPlane().width - (this.viewport.width / this.zoom)));
		focus.setX(Math.max(focus.x(), 0));
		focus.setY(Math.min(focus.y(), driver.game.getPlane().height - (this.viewport.height / this.zoom)));
		focus.setY(Math.max(focus.y(), 0));

		// Get movement delta to reach the target
		double updateDt = 1d / driver.settings.getTicksPerSecond();
		double dx = updateDt * (this.viewport.origin.x() - focus.x()) * FOLLOW_SPEED;
		double dy = updateDt * (this.viewport.origin.y() - focus.y()) * FOLLOW_SPEED;
		
		// Update camera
		this.viewport.origin.translateEquals(-dx, -dy);
	}

}
