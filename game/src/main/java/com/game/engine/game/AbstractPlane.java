package com.game.engine.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.game.engine.driver.GameDriver;
import com.game.engine.graphics.common.Renderable;
import com.game.engine.rendering.common.AbstractRenderer;

/**
 * An abstract plane. This is a playable surface that gets chunked for
 * optimization.
 *
 * @author Spencer Imbleau
 * @version June 2020
 * @see Chunk
 */
public abstract class AbstractPlane implements Updateable, Renderable {

	/**
	 * The chunker
	 */
	protected Chunker chunker;

	/**
	 * Contains the game objects in this level
	 *
	 * @see AbstractGameObject
	 */
	protected List<AbstractGameObject> levelObjects;

	/**
	 * Construct an abstract plane
	 */
	public AbstractPlane() {
		this.chunker = new Chunker(this);
		this.levelObjects = new ArrayList<AbstractGameObject>();
	}

	/**
	 * Initialization of the plane
	 *
	 * @param driver - the driver for the game
	 */
	public void init(GameDriver driver) {
		this.chunker.init(driver);
	}

	@Override
	public void update(GameDriver driver) {
		this.chunker.chunk(driver);
		Iterator<Chunk> currentChunks = this.chunker.iterator();
		while (currentChunks.hasNext()) {
			currentChunks.next().update(driver);
		}
	}

	@Override
	public void stage(GameDriver driver, AbstractRenderer renderer) {
		Iterator<Chunk> currentChunks = this.chunker.iterator();
		while (currentChunks.hasNext()) {
			currentChunks.next().stage(driver, renderer);
		}
	}

	/**
	 * @return the pixel width of the plane
	 */
	public abstract int getWidth();

	/**
	 * @return the pixel height of the plane
	 */
	public abstract int getHeight();
}
