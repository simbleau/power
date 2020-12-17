package com.game.engine.graphics.common;

import com.jogamp.opengl.GL2;

/**
 * A GL Object is an object which holds OpenGL resources such as VRAM.
 * 
 * @author Spencer Imbleau
 * @version December 2020
 */
public interface GLObject {

	/**
	 * Allocate components or OpenGL resources.
	 * 
	 * @param gl - a current GL context
	 */
	public void alloc(GL2 gl);

	/**
	 * Update any applicable OpenGL components and resources.
	 * 
	 * @param gl - a current GL context
	 */
	public void refresh(GL2 gl);

	/**
	 * Dispose of any used components or OpenGL resources.
	 * 
	 * @param gl - a current GL context
	 */
	public void dispose(GL2 gl);

}
