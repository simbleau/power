package com.game.engine.game;

import java.util.ArrayList;
import java.util.List;

import com.game.engine.driver.GameDriver;
import com.game.engine.graphics.common.GLObject;
import com.game.engine.graphics.common.Renderable;
import com.game.engine.physics2D.PhysicsComponent;
import com.game.engine.rendering.common.AbstractRenderer;
import com.jogamp.opengl.GL2;

/**
 * A game object
 *
 * @author Spencer Imbleau
 * @version December 2020
 */
public abstract class AbstractGameObject implements Updateable, Renderable, GLObject, Cloneable {

	/**
	 * The position of the object, which is attached to the plane. Do not directly
	 * modify the position in subclass implementations besides at initialization.
	 * Instead, use {@link #move(GameDriver, double, double)} to ensure proper
	 * garbage collection.
	 *
	 * @see #move(GameDriver, double, double)
	 */
	public final Position2D position = new Position2D(0, 0);

	/**
	 * Contains additional components to extend the functionality of this plane.
	 *
	 * @see AbstractComponent
	 */
	protected List<AbstractComponent> components = new ArrayList<>();

	/**
	 * The width of the object
	 */
	protected int width = 0;

	/**
	 * The height of the object
	 */
	protected int height = 0;

	/**
	 * The physics component for the game object. To give an object physics, set it
	 * via {@link #setPhysics(PhysicsComponent)}. For reactions to occur with a
	 * physical object, physics responders must be added.
	 *
	 * @see #setPhysics(PhysicsComponent)
	 * @see PhysicsComponent#responders
	 */
	private PhysicsComponent physics = null;

	/**
	 * @return the pixel width of the object
	 */
	public int width() {
		return this.width;
	}

	/**
	 * @return the pixel height of the object
	 */
	public int height() {
		return this.height;
	}

	/**
	 * Initialize this object.
	 *
	 * @param driver - the driver for the game
	 */
	public abstract void init(GameDriver driver);

	@Override
	public abstract void alloc(GL2 gl);

	@Override
	public abstract void refresh(GL2 gl);

	@Override
	public abstract void dispose(GL2 gl);

	@Override
	public abstract void update(GameDriver driver);

	@Override
	public abstract void stage(GameDriver driver, AbstractRenderer renderer);

	/**
	 * Create and returns a copy of this game object if it is a supported operation.
	 * Game objects wishing to be truly {@link Cloneable} must override this
	 * interface.
	 */
	@Override
	public AbstractGameObject clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	};

	/**
	 * Move this object's position to another coordinate.
	 *
	 * @param driver - the driver for the game
	 * @param x      - the x co-ordinate to move to
	 * @param y      - the y co-ordinate to move to
	 */
	public void move(GameDriver driver, double x, double y) {
		Position2D targetPosition = new Position2D(x, y);

		// Check if it needs to be marked for deletion/loading before moving
		if (driver.getDisplay().isGL() && driver.game.plane.isChunked()) {
			Chunker chunker = ((AbstractChunkedPlane) driver.game.plane).chunker;
			Chunk from = chunker.chunks[this.position.chunkRow()][this.position.chunkColumn()];
			Chunk to = chunker.chunks[targetPosition.chunkRow()][targetPosition.chunkColumn()];
			if (chunker.viewableChunks.contains(from) && !chunker.viewableChunks.contains(to)) {
				// Trash this object, it's going somewhere not viewable, but was previously
				chunker.flagGLTrash(this);
			}
			if (!chunker.viewableChunks.contains(from) && chunker.viewableChunks.contains(to)) {
				// Load this object, it's going somewhere viewable, but wasn't previously
				chunker.flagGLLoad(this);
			}
		}

		this.position.set(targetPosition);
	}

	/**
	 * @return true if the object has physics, false otherwise
	 */
	public boolean hasPhysics() {
		return this.physics != null;
	}

	/**
	 * @return the physics component for this object
	 * @see PhysicsComponent
	 */
	public PhysicsComponent getPhysics() {
		return this.physics;
	}

	/**
	 * Set the physics component for this object.
	 *
	 * @param c - a physics component
	 * @see PhysicsComponent
	 */
	public void setPhysics(PhysicsComponent c) {
		this.physics = c;
	}

}
