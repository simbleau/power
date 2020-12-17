package com.game.engine.graphics.common;

import com.game.engine.rendering.common.RenderLevel;
import com.game.engine.rendering.cpu.CPUProcessor;
import com.game.engine.rendering.opengl.JOGLProcessor;
import com.jogamp.opengl.GL2;

/**
 * An interface which denotes a graphic object which can be drawn by a renderer
 *
 * @author Spencer Imbleau
 * @version July 2020
 */
public interface Drawable extends GLObject {

	/**
	 * Flag this object as needing OpenGL to update its components or resources.
	 */
	public void flagGLRefresh();

	/**
	 * @return true if this object has updated local resources that need to be
	 *         pushed to OpenGL, false otherwise.
	 */
	public boolean needsGLRefresh();

	/**
	 * Draw for a CPU processor
	 *
	 * @param processor - the graphic processor
	 * @param x         - the x coordinate to draw at
	 * @param y         - the y coordinate to draw at
	 * @param sx        - the scale x
	 * @param sy        - the scale y
	 */
	public void draw(CPUProcessor processor, int x, int y, double sx, double sy);

	/**
	 * Draw for a JOGL processor
	 *
	 * @param processor - the graphic processor
	 * @param gl        - the gl context
	 * @param x         - the x coordinate to draw at
	 * @param y         - the y coordinate to draw at
	 * @param sx        - the scale x
	 * @param sy        - the scale y
	 */
	public void draw(JOGLProcessor processor, GL2 gl, double x, double y, double sx, double sy);

	/**
	 * Create a {@link RenderRequest} from this drawable
	 * 
	 * @param level - the render level
	 * @param x     - the x coordinate to render at
	 * @param y     - the y coordinate to render at
	 * @return a new {@link RenderRequest} from this drawable
	 */
	public RenderRequest asRequest(RenderLevel level, int x, int y);

	/**
	 * Create a {@link RenderRequest} from this drawable
	 * 
	 * @param level - the render level
	 * @param depth - the depth for this render request
	 * @param x     - the x coordinate to render at
	 * @param y     - the y coordinate to render at
	 * @return a new {@link RenderRequest} from this drawable
	 */
	public RenderRequest asRequest(RenderLevel level, int depth, int x, int y);

}
