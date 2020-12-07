package com.game.engine.game;

import java.util.Arrays;
import java.util.stream.Stream;

import com.game.engine.driver.GameDriver;
import com.game.engine.rendering.common.AbstractRenderer;

/**
 * An abstract plane. This is a playable surface that gets chunked for
 * optimization.
 *
 * @author Spencer Imbleau
 * @version December 2020
 * @see Chunk
 */
public abstract class AbstractChunkedPlane extends AbstractPlane {

	/**
	 * The chunker for this plane
	 */
	public final Chunker chunker;

	/**
	 * Construct an abstract plane
	 * 
	 * @param width  - pixel width of the plane
	 * @param height - pixel height of the plane
	 */
	public AbstractChunkedPlane(final int width, final int height) {
		super(width, height);
		this.chunker = new Chunker(this);
	}

	@Override
	public void init(GameDriver driver) {
		// Initialize the chunker
		this.chunker.init(driver);

		// Chunk objects
		this.chunker.chunk(driver);

		// Initialize all chunks
		Stream.of(this.chunker.chunks).flatMap(Stream::of).forEach(chunk -> chunk.init(driver));
	}

	@Override
	public void update(GameDriver driver) {
		// Chunk objects
		this.chunker.chunk(driver);

		// Update viewable chunks
		this.chunker.update(driver.getDisplay().settings.getCamera());
		this.chunker.viewableIterator().forEachRemaining(chunk -> chunk.update(driver));
	}

	@Override
	public void stage(GameDriver driver, AbstractRenderer renderer) {
		// Stage viewable chunks
		this.chunker.viewableIterator().forEachRemaining(chunk -> chunk.stage(driver, renderer));
	}

}
