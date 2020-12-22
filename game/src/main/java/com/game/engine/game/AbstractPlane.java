package com.game.engine.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.game.engine.driver.GameDriver;
import com.game.engine.graphics.common.Renderable;
import com.game.engine.graphics.obj.Rectangle;
import com.game.engine.graphics.request.RectangleRequest;
import com.game.engine.logger.PowerLogger;
import com.game.engine.rendering.common.AbstractRenderer;
import com.game.engine.rendering.common.RenderLevel;
import com.game.engine.rendering.opengl.JOGLCanvas;
import com.game.engine.rendering.opengl.JOGLPlaneMemoryListener;
import com.jogamp.opengl.GLEventListener;

/**
 * An abstract plane. This is a playable plane.
 *
 * @author Spencer Imbleau
 * @version December 2020
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
	 * A list of GL EventListeners attached to this plane in an OpenGL context.
	 */
	protected final Stack<GLEventListener> glListeners;

	/**
	 * Construct an abstract plane
	 * 
	 * @param width  - pixel width of the plane
	 * @param height - pixel height of the plane
	 */
	public AbstractPlane(final int width, final int height) {
		// Standard
		this.width = width;
		this.height = height;
		this.levelObjects = new ArrayList<AbstractGameObject>();

		// OpenGL
		this.glListeners = new Stack<GLEventListener>();
	}

	/**
	 * Initialize this plane.
	 *
	 * @param driver - the driver for the game
	 */
	public void init(GameDriver driver) {
		// Initialize all level objects
		this.levelObjects.forEach(obj -> obj.init(driver));

		// OpenGL plane listener
		if (driver.getDisplay().isGL()) {
			JOGLCanvas canvas = (JOGLCanvas) driver.getDisplay().getRenderer().getCanvas();

			// Add memory listener
			GLEventListener listener = new JOGLPlaneMemoryListener(this);
			this.glListeners.push(listener);
			canvas.addGLEventListener(0, listener);
			PowerLogger.LOGGER
					.finer(listener.getClass().getSimpleName() + " added to " + this.getClass().getSimpleName());
		}

		// Log success
		PowerLogger.LOGGER.info(this.getClass().getSimpleName() + " was initialized");
	}

	/**
	 * Dispose this plane.
	 *
	 * @param driver - the driver for the game
	 */
	public void dispose(GameDriver driver) {
		// Dispose of any OpenGL listeners
		if (driver.getDisplay().isGL()) {
			if (!this.glListeners.isEmpty()) {
				JOGLCanvas canvas = (JOGLCanvas) driver.getDisplay().getRenderer().getCanvas();
				while (!this.glListeners.isEmpty()) {
					GLEventListener listener = this.glListeners.peek();
					canvas.removeGLEventListener(listener);
					PowerLogger.LOGGER.finer(
							listener.getClass().getSimpleName() + " removed from " + this.getClass().getSimpleName());
					this.glListeners.pop();
				}
				PowerLogger.LOGGER.fine("All GL listeners removed from " + this.getClass().getSimpleName());
			}
		}

		// Log success
		PowerLogger.LOGGER.info(this.getClass().getSimpleName() + " was disposed");
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
	 * @return an iterator for the level objects
	 */
	public Iterable<AbstractGameObject> objects() {
		return this.levelObjects;
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

	/**
	 * @return true if this is a chunked plane, false otherwise.
	 */
	public boolean isChunked() {
		return false;
	}
}
