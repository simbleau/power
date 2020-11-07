package com.game.engine.graphics.common;

import com.game.engine.driver.GameDriver;
import com.game.engine.rendering.common.AbstractRenderer;

/**
 * An object that is able to be rendered by a {@link AbstractRenderer}
 *
 * @author Spencer Imbleau
 * @version June 2020
 */
public interface Renderable {

	/**
	 * Stage any requests for rendering
	 *
	 * @param driver   - the game driver
	 * @param renderer - the renderer for staging
	 */
	public void stage(GameDriver driver, AbstractRenderer renderer);

}