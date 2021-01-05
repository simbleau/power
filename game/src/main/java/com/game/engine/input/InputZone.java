package com.game.engine.input;

/**
 * This interface describes an abstract zone which input can interact with. The
 * object which implements this interface should have a discrete area or zone
 * which can interact with an input type.<br>
 * <br>
 * For example, if there is an input of a {@link MouseKeyboard}, and the
 * implementation of this input zone a user interface element, this input zone
 * should determine if the mouse is overlapping the user interface element.
 *
 * @author Spencer Imbleau
 * @version January 2021
 */
public interface InputZone {

	/**
	 * Determines whether an input type overlaps this zone.
	 *
	 * @param input - an input to test for overlap
	 * @return true if user input this overlaps this zone, false otherwise
	 */
	public abstract boolean overlaps(Input input);

}
