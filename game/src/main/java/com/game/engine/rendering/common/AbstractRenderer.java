package com.game.engine.rendering.common;

import java.awt.Canvas;

import com.game.engine.display.GameDisplay;
import com.game.engine.graphics.common.RenderRequest;

/**
 * A graphics renderer
 *
 * @author Spencer Imbleau
 * @version June 2020
 */
public abstract class AbstractRenderer {

	/**
	 * The display to render for
	 */
	public final GameDisplay display;

	/**
	 * Initialize the renderer
	 *
	 * @param display - the display to render for
	 */
	public AbstractRenderer(GameDisplay display) {
		this.display = display;
	}

	/**
	 * Initialize the renderer.
	 * 
	 * @throws Exception if the renderer could not be initialized due to graphic
	 *                   incompatibility.
	 */
	public abstract void init() throws Exception;

	/**
	 * Render the game screen
	 */
	public abstract void render();

	/**
	 * Stage a render request for the next render call
	 *
	 * @param request - the request to render
	 */
	public abstract void stage(RenderRequest request);

	/**
	 * @return the renderer's canvas
	 */
	public abstract Canvas getCanvas();
}
