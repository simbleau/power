package com.game.engine.graphics.request;

import com.game.engine.graphics.common.RenderRequest;
import com.game.engine.graphics.obj.Rectangle;
import com.game.engine.rendering.common.RenderLevel;

/**
 * This is an {@link RenderRequest} which will encapsulate a {@link Rectangle}
 * and where to draw it.
 *
 * @version June 2020
 * @author Spencer Imbleau
 */
public class RectangleRequest extends RenderRequest {

	/**
	 * Initialize rectangle request
	 *
	 * @param rect  - the rectangle to render
	 * @param level - the render level
	 * @param x     - The x coordinate to render at
	 * @param y     - The y coordinate to render at
	 */
	public RectangleRequest(Rectangle rect, RenderLevel level, int x, int y) {
		super(rect, level, y + rect.getHeight(), x, y);
	}

	/**
	 * Initialize rectangle request with special depth
	 *
	 * @param rect  - the rectangle to render
	 * @param level - the render level
	 * @param depth - a special depth
	 * @param x     - The x coordinate to render at
	 * @param y     - The y coordinate to render at
	 */
	public RectangleRequest(Rectangle rect, RenderLevel level, int depth, int x, int y) {
		super(rect, level, depth, x, y);
	}

}
