package com.game.engine.graphics.common;

import com.game.engine.rendering.common.RenderLevel;

/**
 * A render request is a comparable object sent to the renderer for processing
 * (and sorting) before drawing graphics to the screen.
 *
 * @version October 2020
 * @author Spencer Imbleau
 */
public abstract class RenderRequest implements Comparable<RenderRequest> {

	/**
	 * The x coordinate, relative to the screen
	 */
	public final int x;

	/**
	 * The y coordinate, relative to the screen
	 */
	public final int y;

	/**
	 * The logical depth level of rendering for this request.
	 * @see #compareTo(RenderRequest)
	 */
	public final RenderLevel level;

	/**
	 * The depth used for sorting render requests on the same {@link #level}.
	 * @see #compareTo(RenderRequest)
	 */
	public final int depth;

	/**
	 * The requested {@link Drawable} object
	 */
	public final Drawable drawable;

	/**
	 * Initialize a render request
	 *
	 * @param drawable - a drawable object
	 * @param level    - the logical rendering level
	 * @param depth    - the comparable depth this request will take
	 * @param x        - the X coordinate to render at
	 * @param y        - the Y coordinate to render at
	 */
	public RenderRequest(Drawable drawable, RenderLevel level, int depth, int x, int y) {
		this.drawable = drawable;
		this.level = level;
		this.depth = depth;
		this.x = x;
		this.y = y;
	}

	/**
	 * Compare this render request's depth against another.
	 */
	@Override
	public int compareTo(RenderRequest o) {
		if (this.level.ordinal() > o.level.ordinal()) {
			return -1;
		} else if (this.level.ordinal() < o.level.ordinal()) {
			return 1;
		} else {
			// Same level
			if (this.depth < o.depth) {
				return -1;
			} else if (depth > o.depth) {
				return 1;
			} else {
				return 0;
			}
		}
	}

}