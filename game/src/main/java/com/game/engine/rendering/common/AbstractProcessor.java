package com.game.engine.rendering.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.game.engine.graphics.common.RenderRequest;

/**
 * A graphics processor to handle all drawing functions as well as processing
 * the order of rendering
 *
 * @author Spencer Imbleau
 * @version June 2020
 */
public abstract class AbstractProcessor {

	/**
	 * Reference to the renderer
	 */
	protected AbstractRenderer renderer;

	/**
	 * Buffer for stored renderable requests
	 */
	protected List<RenderRequest> requests;

	/**
	 * Create a graphics processor used for sorting rendering and staging requests
	 * for the next render call
	 *
	 * @param renderer - the renderer to process for
	 */
	public AbstractProcessor(AbstractRenderer renderer) {
		this.renderer = renderer;
		this.requests = new ArrayList<RenderRequest>();
	}

	/**
	 * Sort the render and overlay requests
	 */
	public void sort() {
		Collections.sort(this.requests); // O(n*logn) worst case, O(n) best case
	}

	/**
	 * Clear the render and overlay requests
	 */
	public void reset() {
		this.requests.clear();
	}

	/**
	 * @return an iterator with the current render requests
	 */
	public Iterator<RenderRequest> iterator() {
		return this.requests.iterator();
	}

	/**
	 * Store a render request
	 *
	 * @param request - the request to store
	 */
	public void stage(RenderRequest request) {
		this.requests.add(request);
	}

	/**
	 * @return the renderer
	 */
	public AbstractRenderer getRenderer() {
		return this.renderer;
	}
}
