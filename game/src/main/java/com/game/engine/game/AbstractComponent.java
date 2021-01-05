package com.game.engine.game;

import com.game.engine.driver.GameDriver;
import com.game.engine.graphics.common.Renderable;
import com.game.engine.rendering.common.AbstractRenderer;

/**
 * A mechanism which can abstract away additional unique functionality via user
 * defined implementation of the {@link Updateable} and {@link Renderable}
 * interfaces. <br>
 * <br>
 * Contrary to {@link AbstractGameObject} and {@link AbstractPlane} objects, for
 * example, components are not designed to have initialization or disposal
 * calls. They are provided, as is, to implement separable functionality from a
 * parent class.
 *
 * @author Spencer Imbleau
 * @version January 2021
 */
public abstract class AbstractComponent implements Updateable, Renderable {

	/**
	 * Update this component.
	 *
	 * @param driver - the driver for the game
	 */
	@Override
	public abstract void update(GameDriver driver);

	/**
	 * Stage any component requests for rendering.
	 *
	 * @param driver   - the driver for the game
	 * @param renderer - the renderer for staging
	 */
	@Override
	public abstract void stage(GameDriver driver, AbstractRenderer renderer);

}
