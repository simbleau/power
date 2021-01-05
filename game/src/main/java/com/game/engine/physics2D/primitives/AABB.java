package com.game.engine.physics2D.primitives;

import com.game.engine.maths.Matrix2D;

/**
 * An axis-aligned bounding box body for 
 *
 * @author Spencer Imbleau
 * @version January 2021
 */
public class AABB {
	
	private Matrix2D center = Matrix2D.create(0, 0);
	
	private Matrix2D size = Matrix2D.create(0, 0);
	
	public AABB(Matrix2D min, Matrix2D max) {
		
	}

}
