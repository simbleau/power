package com.game.engine.graphics.request;

import com.game.engine.graphics.common.RenderRequest;
import com.game.engine.graphics.obj.Ellipse;
import com.game.engine.rendering.common.RenderLevel;

/**
 * This is an {@link RenderRequest} which will encapsulate am {@link Ellipse}
 * and where to draw it.
 *
 * @version July 2020
 * @author Spencer Imbleau
 */
public class EllipseRequest extends RenderRequest {

	/**
	 * Initialize ellipse request
	 *
	 * @param ellipse - the ellipse to render
	 * @param level   - the render level
	 * @param cx      - The center x coordinate to render at
	 * @param cy      - The centery coordinate to render at
	 */
	public EllipseRequest(Ellipse ellipse, RenderLevel level, int cx, int cy) {
		super(ellipse, level, cy + ellipse.getRy() * 2, cx, cy);
	}

	/**
	 * Initialize ellipse request
	 *
	 * @param ellipse - the ellipse to render
	 * @param level   - the render level
	 * @param depth   - a special depth
	 * @param cx      - The center x coordinate to render at
	 * @param cy      - The center y coordinate to render at
	 */
	public EllipseRequest(Ellipse ellipse, RenderLevel level, int depth, int cx, int cy) {
		super(ellipse, level, depth, cx, cy);
	}

}
