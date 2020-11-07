package com.game.engine.input;

/**
 * A listener which describes user input
 *
 * @version June 2020
 * @author Spencer Imbleau
 */
public interface Input {

	/**
	 * Update the input
	 */
	public void update();

	/**
	 * @return X position of the cursor
	 */
	public int x();

	/**
	 * @return Y position of the cursor
	 */
	public int y();

	/**
	 * @return X position of the cursor relative to the screen
	 */
	public int planeX();

	/**
	 * @return Y position of the cursor relative to the screen
	 */
	public int planeY();

	/**
	 * @param code - the code for a key
	 * @return true if the given key is held, false otherwise
	 */
	public boolean isKeyActive(int code);

	/**
	 * @param code - the code for a key
	 * @return true if the given key was just released, false otherwise
	 */
	public boolean isKeyUp(int code);

	/**
	 * @param code - the code for a key
	 * @return true if the given key was just pressed, false otherwise
	 */
	public boolean isKeyDown(int code);

	/**
	 * @param button - the code for a button
	 * @return true if the button is held, false otherwise
	 */
	public boolean isButtonActive(int button);

	/**
	 * @param button - the code for a button
	 * @return true if the button is held, false otherwise
	 */
	public boolean isButtonUp(int button);

	/**
	 * @param button - the code for a button
	 * @return true if the button is held, false otherwise
	 */
	public boolean isButtonDown(int button);

	/**
	 * @return the scroll direction
	 */
	public int getScroll();

}