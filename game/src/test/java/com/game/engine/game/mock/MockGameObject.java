package com.game.engine.game.mock;

import com.game.engine.driver.GameDriver;
import com.game.engine.game.AbstractGameObject;
import com.game.engine.graphics.obj.Rectangle;
import com.game.engine.rendering.common.AbstractRenderer;
import com.game.engine.rendering.common.RenderLevel;

/**
 * An arbitrary game object for testing.
 * 
 * @author Spencer Imbleau
 * @version December 2020
 */
public class MockGameObject extends AbstractGameObject {

	/**
	 * A drawable rectangle for this object.
	 */
	private Rectangle r;

	/**
	 * Construct an arbitrary game object with a given location.
	 * 
	 * @param x - x co-ordinate position
	 * @param y - y co-ordinate position
	 */
	public MockGameObject(double x, double y) {
		this.position.set(x, y);
		this.width = 1;
		this.height = 1;
		this.r = null;
	}

	/**
	 * Construct an arbitrary game object with a given location and size.
	 * 
	 * @param x      - x co-ordinate position
	 * @param y      - y co-ordinate position
	 * @param width  - width of the object
	 * @param height - height of the object
	 */
	public MockGameObject(double x, double y, int width, int height) {
		this.position.set(x, y);
		this.width = width;
		this.height = height;
		this.r = null;
	}

	/**
	 * Construct an arbitrary game object.
	 */
	public MockGameObject() {
		super();
	}

	@Override
	public void init(GameDriver driver) {
		this.r = new Rectangle(this.width, this.height, 0xffff0000);
	}

	@Override
	public void update(GameDriver driver) {
		// Do nothing
	}

	@Override
	public void stage(GameDriver driver, AbstractRenderer renderer) {
		// Stage a red bounding box at the x,y position
		renderer.stage(r.asRequest(RenderLevel.WORLD_OBJECTS, (int) this.x(), (int) this.y()));
	}

}
