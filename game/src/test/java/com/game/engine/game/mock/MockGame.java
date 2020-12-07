package com.game.engine.game.mock;

import com.game.engine.game.AbstractGame;
import com.game.engine.game.AbstractChunkedPlane;

/**
 * An arbitrary game for testing.
 * 
 * @author Spencer Imbleau
 * @version November 2020
 */
public class MockGame extends AbstractGame {

	/**
	 * An arbitrary plane for this mock game.
	 */
	private static final AbstractChunkedPlane PLANE = new MockPlane();

	/**
	 * An arbitrary minimum width for this mock game.
	 */
	private static final int GAME_WIDTH = 480;

	/**
	 * An arbitrary minimum height for this mock game.
	 */
	private static final int GAME_HEIGHT = 720;

	/**
	 * Construct an arbitrary game.
	 */
	public MockGame() {
		super(GAME_WIDTH, GAME_HEIGHT, PLANE);
	}

}
