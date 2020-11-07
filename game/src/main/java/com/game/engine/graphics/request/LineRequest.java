package com.game.engine.graphics.request;

import com.game.engine.graphics.common.RenderRequest;
import com.game.engine.graphics.obj.Line;
import com.game.engine.rendering.common.RenderLevel;

/**
 * This is an {@link RenderRequest} which will encapsulate a {@link Line} and
 * where to draw it.
 *
 * @version July 2020
 * @author Spencer Imbleau
 */
public class LineRequest extends RenderRequest {

	/**
	 * Initialize line request
	 *
	 * @param line - the line to render
	 * @param level - the render level
	 * @param x    - The x coordinate to render at
	 * @param y    - The y coordinate to render at
	 */
	public LineRequest(Line line, RenderLevel level, int x, int y) {
		super(line, level, y + line.getDy(), x, y);
	}

	/**
	 * Initialize line request with special depth
	 *
	 * @param line  - the line to render
	 * @param level - the render level
	 * @param depth - a special depth
	 * @param x     - The x coordinate to render at
	 * @param y     - The y coordinate to render at
	 */
	public LineRequest(Line line, RenderLevel level, int depth, int x, int y) {
		super(line, level, depth, x, y);
	}

}
