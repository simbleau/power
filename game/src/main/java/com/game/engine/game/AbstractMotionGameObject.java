package com.game.engine.game;

import java.time.Duration;

import com.game.engine.driver.GameDriver;

/**
 * An abstract game object with motion capability.
 *
 * @author Spencer Imbleau
 * @version December 2020
 */
public abstract class AbstractMotionGameObject extends AbstractGameObject {

	/**
	 * The speed of this object, measured in pixels per millisecond.
	 */
	protected double speed = 0d;

	/**
	 * The direction of this object, measured in radians.
	 */
	protected double direction = 0d;

	/**
	 * Set the speed of this object, measured in pixels per millisecond.
	 *
	 * @param speed - the new speed of the object
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	/**
	 * @return the speed of this object, measured in pixels per millisecond
	 */
	public double getSpeed() {
		return this.speed;
	}

	/**
	 * Set the direction of this object, in radians.
	 *
	 * @param direction - the new direction of the object
	 */
	public void setDirection(double direction) {
		this.direction = direction;
	}

	/**
	 * @return the direction of this object, measured in radians
	 */
	public double getDirection() {
		return this.direction;
	}

	/**
	 * An unimpeded displacement on the x-axis this motion object would move over a
	 * given duration.
	 *
	 * @param duration - the duration this object has been moving
	 * @return the displacement on the x-axis over a duration
	 */
	public double dx(Duration duration) {
		return Math.cos(this.direction) * (this.speed * duration.toMillis());
	}

	/**
	 * An unimpeded displacement on the y-axis this motion object would move over a
	 * given duration.
	 *
	 * @param duration - the duration this object has been moving
	 * @return the displacement on the y-axis over a duration
	 */
	public double dy(Duration duration) {
		return Math.sin(this.direction) * (this.speed * duration.toMillis());
	}

	/**
	 * Turn the direction of this object clockwise by given delta radians.
	 *
	 * @param radians - the delta scalar to turn this object clockwise
	 */
	public void turnCW(double radians) {
		this.direction += radians;
	}

	/**
	 * Turn the direction of this object counter-clockwise by given delta radians.
	 *
	 * @param radians - the delta scalar to turn this object counter-clockwise
	 */
	public void turnCCW(double radians) {
		this.direction -= radians;
	}

	@Override
	public void update(GameDriver driver) {
		AbstractPlane parent = driver.game.getPlane();

		Duration uDt = driver.settings.getTickDuration();
		double dx = this.dx(uDt);
		double dy = this.dy(uDt);

		if (dx + dy != 0) {
			// TODO Collision check here instead
			this.move(driver, Math.max(0, Math.min(this.x() + dx, parent.width - this.width)),
					Math.max(0, Math.min(this.y() + dy, parent.height - this.height)));
		}
	}

}
