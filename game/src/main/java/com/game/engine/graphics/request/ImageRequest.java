package com.game.engine.graphics.request;

import com.game.engine.graphics.common.RenderRequest;
import com.game.engine.graphics.obj.Image;
import com.game.engine.rendering.common.RenderLevel;

/**
 * This is an {@link RenderRequest} which will encapsulate a {@link Image} and
 * where to draw it.
 *
 * @version July 2020
 * @author Spencer Imbleau
 */
public class ImageRequest extends RenderRequest {

	/**
	 * Initialize image request
	 *
	 * @param image - the image to render
	 * @param level - the render level
	 * @param x     - The x coordinate to render at
	 * @param y     - The y coordinate to render at
	 */
	public ImageRequest(Image image, RenderLevel level, int x, int y) {
		super(image, level, y + image.getHeight(), x, y);
	}

	/**
	 * Initialize image request with special depth
	 *
	 * @param image - the image to render
	 * @param level - the render level
	 * @param depth - a special depth
	 * @param x     - The x coordinate to render at
	 * @param y     - The y coordinate to render at
	 */
	public ImageRequest(Image image, RenderLevel level, int depth, int x, int y) {
		super(image, level, depth, x, y);
	}

}
