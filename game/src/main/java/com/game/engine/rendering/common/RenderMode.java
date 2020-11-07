package com.game.engine.rendering.common;

/**
 * Different graphics modes supported
 *
 * @author Spencer Imbleau
 * @version June 2020
 */
public enum RenderMode {

	/**
	 * OpenGL graphics are rendered by the GPU and preferred due to performance.
	 */
	OPENGL,

	/**
	 * Safe mode graphics are rendered by the CPU. This is slower but requires no
	 * dependencies.
	 */
	SAFE

}
