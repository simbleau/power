package com.game.engine.camera;

import com.game.engine.driver.GameDriver;

/**
 * A camera which looks at a point.
 * 
 * @author Spencer Imbleau
 * @version December 2020
 */
public class StationaryCamera extends AbstractCamera {

	/**
	 * Construct a stationary camera which looks at a point.
	 * 
	 * @param x      - starting x co-ordinate origin for the camera
	 * @param y      - starting y co-ordinate origin for the camera
	 * @param width  - viewport width of the camera
	 * @param height - viewport height of the camera
	 * @param zoom   - starting zoom of the camera
	 */
	public StationaryCamera(double x, double y, int width, int height, double zoom) {
		super(x, y, width, height, zoom);
	}

	@Override
	public void update(GameDriver driver) {
		super.update(driver);
	}

}
