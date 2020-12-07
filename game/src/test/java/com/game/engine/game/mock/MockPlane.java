package com.game.engine.game.mock;

import com.game.engine.game.AbstractChunkedPlane;

/**
 * An arbitrary test plane.
 * 
 * @author Spencer Imbleau
 * @version November 2020
 */
public class MockPlane extends AbstractChunkedPlane {

	/**
	 * An arbitrary plane pixel width.
	 */
	private static final int WIDTH = 1500;

	/**
	 * An arbitrary plane pixel height.
	 */
	private static final int HEIGHT = 1000;

	/**
	 * Construct a mock plane.
	 */
	public MockPlane() {
		super(WIDTH, HEIGHT);
	}

}
