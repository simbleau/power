package com.game.engine.camera;

import com.game.engine.game.AbstractGameObject;
import com.game.engine.game.Chunk;
import com.game.engine.game.Position2D;

/**
 * The user's visible area.
 * 
 * @author Spencer Imbleau
 * @version December 2020
 */
public class Viewport {

	/**
	 * The parent camera for this viewport
	 */
	private final AbstractCamera camera;

	/**
	 * The origin of the camera, also the top-left point of the camera.
	 */
	protected Position2D origin;

	/**
	 * The width of the viewport.
	 */
	protected int width;

	/**
	 * The height of the viewport.
	 */
	protected int height;

	/**
	 * Initialize a viewport with dimensions.
	 * 
	 * @param camera - the parent camera
	 * @param x      - starting displacement on the X axis
	 * @param y      - starting displacement on the Y axis
	 * @param width  - width of the viewport
	 * @param height - height of the viewport
	 */
	public Viewport(AbstractCamera camera, double x, double y, int width, int height) {
		this.camera = camera;
		this.origin = new Position2D(x, y);
		this.width = width;
		this.height = height;
	}

	/**
	 * @return the viewport's displacement on the X-axis
	 */
	public double x() {
		return this.origin.x();
	}

	/**
	 * @return the viewport's displacement on the Y-axis
	 */
	public double y() {
		return this.origin.y();
	}

	/**
	 * @return the viewport's width
	 */
	public int width() {
		return this.width;
	}

	/**
	 * @return the viewport's height
	 */
	public int height() {
		return this.height;
	}

	/**
	 * Set the origin of the viewport.
	 *
	 * @param x - an x co-ordinate
	 * @param y - a y co-ordinate
	 */
	public void setOrigin(double x, double y) {
		this.origin.set(x, y);
	}

	/**
	 * Resize the viewport dimensions.
	 *
	 * @param width  - a new width for the viewport
	 * @param height - a new height for the viewport
	 */
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * @param x - an x co-ordinate
	 * @param y - a y co-ordinate
	 * @return true if the viewport can see the given point, false otherwise
	 */
	public boolean canSee(double x, double y) {
		// Too far west
		if (this.origin.x() > x) {
			return false;
		}
		// Too far east
		if (x > this.origin.x() + this.width) {
			return false;
		}
		// Too far north
		if (this.origin.y() > y) {
			return false;
		}
		// To far south
		if (y > this.origin.y() + this.height) {
			return false;
		}
		return true;
	}

	/**
	 * @param obj - an object
	 * @return true if the viewport can see the given object, false otherwise
	 */
	public boolean canSee(AbstractGameObject obj) {
		return this.canSee(obj.position.x(), obj.position.y());
	}

	/**
	 * @return the closest chunk row index visible by this viewport
	 */
	public int closestChunkRow() {
		return this.origin.y.asChunkIndex();
	}

	/**
	 * @return the closest chunk column index visible by this viewport
	 */
	public int closestChunkColumn() {
		return this.origin.x.asChunkIndex();
	}

	/**
	 * @return the furthest chunk row index visible by this viewport
	 */
	public int furthestChunkRow() {
		int toY = (int) (this.origin.y() + (this.height / this.camera.zoom));
		int toChunkY = (toY < 0) ? toY / Chunk.SIZE - 1 : toY / Chunk.SIZE;
		if (toY % Chunk.SIZE == 0) {
			// Viewport clips on the boundary of a new chunk, we will exclude that new
			// chunk.
			if (toY < 0) {
				toChunkY++;
			} else {
				toChunkY--;
			}
		}
		return toChunkY;
	}

	/**
	 * @return the furthest chunk column index visible by this viewport
	 */
	public int furthestChunkColumn() {
		int toX = (int) (this.origin.x() + (this.width / this.camera.zoom));
		int toChunkX = (toX < 0) ? toX / Chunk.SIZE - 1 : toX / Chunk.SIZE;
		if (toX % Chunk.SIZE == 0) {
			// Viewport clips on the boundary of a new chunk, we will exclude that new
			// chunk.
			if (toX < 0) {
				toChunkX++;
			} else {
				toChunkX--;
			}
		}
		return toChunkX;
	}

}
