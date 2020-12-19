package com.game.engine.graphics.request;

import com.game.engine.graphics.common.RenderRequest;
import com.game.engine.graphics.obj.Label;
import com.game.engine.rendering.common.RenderLevel;

/**
 * This is an {@link RenderRequest} which will encapsulate a {@link Label}
 * and where to draw it.
 *
 * @version December 2020
 * @author Spencer Imbleau
 */
public class LabelRequest extends ImageRequest {

	/**
	 * Initialize rectangle request
	 *
	 * @param label  - the label to render
	 * @param level - the render level
	 * @param x     - The x coordinate to render at
	 * @param y     - The y coordinate to render at
	 */
	public LabelRequest(Label label, RenderLevel level, int x, int y) {
		super(label, level, y + label.getHeight(), x, y);
	}

	/**
	 * Initialize rectangle request with special depth
	 *
	 * @param label  - the label to render
	 * @param level - the render level
	 * @param depth - a special depth
	 * @param x     - The x coordinate to render at
	 * @param y     - The y coordinate to render at
	 */
	public LabelRequest(Label label, RenderLevel level, int depth, int x, int y) {
		super(label, level, depth, x, y);
	}

}
