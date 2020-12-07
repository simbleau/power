package com.game.engine.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.game.engine.driver.GameDriver;
import com.game.engine.graphics.common.Renderable;
import com.game.engine.graphics.obj.Rectangle;
import com.game.engine.graphics.request.RectangleRequest;
import com.game.engine.rendering.common.AbstractRenderer;
import com.game.engine.rendering.common.RenderLevel;

/**
 * A chunk of a {@link AbstractChunkedPlane}
 *
 * @author Spencer Imbleau
 * @version June 2020
 */
public class Chunk implements Updateable, Renderable {

	/**
	 * The size of a chunk (width and height), measured in pixels
	 */
	public final static int SIZE = 500;

	/**
	 * The parent plane
	 */
	protected AbstractChunkedPlane plane;

	/**
	 * The chunk's row in the parent plane
	 */
	protected int row;

	/**
	 * The chunk's column in the parent plane
	 */
	protected int column;

	/**
	 * This chunk's neighboring chunks
	 */
	protected List<Chunk> neighbors;

	/**
	 * The level objects in this chunk
	 */
	protected List<AbstractGameObject> chunkObjects;

	/**
	 * Initialize a chunk
	 *
	 * @param plane - the parent plane
	 * @param row   - the chunk's row in the parent plane
	 * @param col   - the chunk's column in the parent plane
	 */
	public Chunk(AbstractChunkedPlane plane, int row, int col) {
		this.plane = plane;
		this.row = row;
		this.column = col;

		this.neighbors = new ArrayList<Chunk>();
		this.chunkObjects = new ArrayList<AbstractGameObject>();
	}

	/**
	 * Add a neighbor for this chunk
	 *
	 * @param chunk - a chunk
	 */
	public void addNeighbor(Chunk chunk) {
		this.neighbors.add(chunk);
	}

	/**
	 * @return an iterator for the neighboring chunks
	 */
	public Iterator<Chunk> neighborIterator() {
		return this.neighbors.iterator();
	}

	/**
	 * Add an object to this chunk
	 *
	 * @param object - an {@link AbstractGameObject}
	 */
	public void addObject(AbstractGameObject object) {
		this.chunkObjects.add(object);
	}

	/**
	 * Clear the level objects off this chunk
	 */
	public void clear() {
		this.chunkObjects.clear();
	}

	/**
	 * Initialize this chunk.
	 * 
	 * @param driver - the driver for this game
	 */
	public void init(GameDriver driver) {
		// Initialize all chunk objects
		this.chunkObjects.forEach(obj -> obj.init(driver));
	}

	@Override
	public void update(GameDriver driver) {
		// Update all chunk objects
		this.chunkObjects.forEach(obj -> obj.update(driver));
	}

	@Override
	public void stage(GameDriver driver, AbstractRenderer renderer) {
		// Stage a boundary with color differentiation
		int red = 0xff - (int) ((float) this.row / this.plane.chunker.getRows() * 0xff);

		int green = 0xff - (int) ((float) this.column / this.plane.chunker.getColumns() * 0xff);
		int blue = 0xff - (int) ((float) (this.row + this.column)
				/ (this.plane.chunker.getRows() + this.plane.chunker.getColumns()) * 0xff);

		int argb = (0xff << 24 | red << 16 | green << 8 | blue);

		int width = (this.row == this.plane.chunker.getRows() - 1) ? this.plane.chunker.plane.width % SIZE : SIZE;
		int height = (this.column == this.plane.chunker.getColumns() - 1) ? this.plane.chunker.plane.height % SIZE
				: SIZE;
		Rectangle rect = new Rectangle(width, height, argb);
		RectangleRequest request = new RectangleRequest(rect, RenderLevel.VOID, 0, (int) this.row * SIZE,
				(int) this.column * SIZE);
		renderer.stage(request);

		// Stage all chunk objects
		this.chunkObjects.forEach(obj -> obj.stage(driver, renderer));
	}
}
