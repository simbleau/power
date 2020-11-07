package com.game.engine.game;

import com.game.engine.driver.GameDriver;

/**
 * An object which can be called to update
 *
 * @author Spencer Imbleau
 * @version June 2020
 */
public interface Updateable {

	/**
	 * Update this object
	 *
	 * @param driver - the driver for the game
	 */
	public void update(GameDriver driver);

}
