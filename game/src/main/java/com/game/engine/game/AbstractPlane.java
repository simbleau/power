package com.game.engine.game;

import java.util.ArrayList;
import java.util.List;

import com.game.engine.driver.GameDriver;
import com.game.engine.graphics.common.Renderable;
import com.game.engine.graphics.obj.Rectangle;
import com.game.engine.graphics.request.RectangleRequest;
import com.game.engine.rendering.common.AbstractRenderer;
import com.game.engine.rendering.common.RenderLevel;

/**
 * An abstract plane. This is a playable plane.
 *
 * @author Spencer Imbleau
 * @version December 2020
 * @see Chunk
 */
public abstract class AbstractPlane implements Updateable, Renderable {

	/**
	 * Contains the game objects in this level
	 *
	 * @see AbstractGameObject
	 */
	protected List<AbstractGameObject> levelObjects;

	/**
	 * The pixel width of the plane.
	 */
	public final int width;

	/**
	 * The pixel height of the plane.
	 */
	public final int height;

	/**
	 * Construct an abstract plane
	 * 
	 * @param width  - pixel width of the plane
	 * @param height - pixel height of the plane
	 */
	public AbstractPlane(final int width, final int height) {
		this.width = width;
		this.height = height;
		this.levelObjects = new ArrayList<AbstractGameObject>();
	}

	/**
	 * Initialize this plane.
	 *
	 * @param driver - the driver for the game
	 */
	public void init(GameDriver driver) {
		// Initialize all level objects
		this.levelObjects.forEach(obj -> obj.init(driver));
	}

	@Override
	public void update(GameDriver driver) {
		// Update all level objects
		this.levelObjects.forEach(obj -> obj.update(driver));
	}

	@Override
	public void stage(GameDriver driver, AbstractRenderer renderer) {
		// Stage a red rectangle
		Rectangle rect = new Rectangle(this.width, this.height, 0xffff0000);
		RectangleRequest request = new RectangleRequest(rect, RenderLevel.VOID, 0, 0, 0);
		renderer.stage(request);

		// Stage all level objects
		this.levelObjects.forEach(obj -> obj.stage(driver, renderer));
	}

	/**
	 * Add a game object which exists on the plane.
	 * 
	 * @param obj - The object to add to the level.
	 */
	public void addGameObject(AbstractGameObject obj) {
		this.levelObjects.add(obj);
	}

	/**
	 * Remove a game object if it exists on the plane.
	 * 
	 * @param obj - The object to remove from the level.
	 */
	public void removeGameObject(AbstractGameObject obj) {
		this.levelObjects.remove(obj);
	}
}
