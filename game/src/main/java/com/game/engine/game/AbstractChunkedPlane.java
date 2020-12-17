package com.game.engine.game;

import java.util.stream.Stream;

import com.game.engine.driver.GameDriver;
import com.game.engine.logger.PowerLogger;
import com.game.engine.rendering.common.AbstractRenderer;
import com.game.engine.rendering.opengl.JOGLCanvas;
import com.game.engine.rendering.opengl.JOGLChunkedPlaneListener;
import com.jogamp.opengl.GLEventListener;

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

		// Add chunk listener if we are using OpenGL to allocate and dispose of chunks
		if (driver.getDisplay().isGL()) {
			GLEventListener listener = new JOGLChunkedPlaneListener(this);

			JOGLCanvas canvas = (JOGLCanvas) driver.getDisplay().getRenderer().getCanvas();
			this.glListeners.add(listener);
			canvas.addGLEventListener(0, listener);
			PowerLogger.LOGGER.fine("GL listeners added to " + this.getClass().getSimpleName());
		}

		// Log success
		PowerLogger.LOGGER.info(this.getClass().getSimpleName() + " was initialized");
	}

	@Override
	public void dispose(GameDriver driver) {
		super.dispose(driver);
	}

	@Override
	public void update(GameDriver driver) {
		// Chunk objects
		this.chunker.chunk(driver);

		// Update viewable chunks
		this.chunker.scan(driver, driver.getDisplay().settings.getCamera());
		this.chunker.viewableIterator().forEachRemaining(chunk -> chunk.update(driver));
	}

	@Override
	public void stage(GameDriver driver, AbstractRenderer renderer) {
		// Stage viewable chunks
		this.chunker.viewableIterator().forEachRemaining(chunk -> chunk.stage(driver, renderer));
	}

	@Override
	public void addGameObject(AbstractGameObject obj) {
		this.levelObjects.add(obj);
		this.chunker.chunks[(int) obj.x() / Chunk.SIZE][(int) obj.y() / Chunk.SIZE].addGameObject(obj);
	}

	@Override
	public void removeGameObject(AbstractGameObject obj) {
		this.levelObjects.remove(obj);
		this.chunker.chunks[(int) obj.x() / Chunk.SIZE][(int) obj.y() / Chunk.SIZE].removeGameObject(obj);
	}

	@Override
	public boolean isChunked() {
		return true;
	}

}
