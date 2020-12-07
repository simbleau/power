package com.game.engine.camera.mock;

import com.game.engine.camera.AbstractCamera;
import com.game.engine.driver.GameDriver;

/**
 * A mock camera.
 * 
 * @author Spencer Imbleau
 * @version October 2020
 */
public class MockCamera extends AbstractCamera {

	/**
	 * Initialize a mock camera.
	 * 
	 * @param x      - starting x position
	 * @param y      - starting y position
	 * @param width  - width of the viewport
	 * @param height - height of the viewport
	 * @param zoom   - starting zoom
	 */
	public MockCamera(double x, double y, int width, int height, double zoom) {
		super(x, y, width, height, zoom);
	}

	@Override
	public void update(GameDriver driver) {
		super.update(driver);
	}

}
