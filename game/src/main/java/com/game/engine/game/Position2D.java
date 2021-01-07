package com.game.engine.game;

import com.game.engine.maths.Matrix2D;

/**
 * A position class which gives insights into positional data in different
 * formats and protected access methods.
 * 
 * @author Spencer Imbleau
 * @version December 2020
 */
public class Position2D {

	/**
	 * The backing coordinate matrix which holds position data.
	 */
	protected final Matrix2D pMatrix;

	/**
	 * The x co-ordinate accessor methods.
	 */
	public final X x;

	/**
	 * The y co-ordinate accessor methods.
	 */
	public final Y y;

	/**
	 * Create a position at an x,y co-ordinate.
	 * 
	 * @param x - the x co-ordinate
	 * @param y - the y co-ordinate
	 */
	public Position2D(double x, double y) {
		this.pMatrix = new Matrix2D(x, y);
		this.x = new X();
		this.y = new Y();
	}

	/**
	 * @return the x co-ordinate of this position
	 */
	public double x() {
		return this.pMatrix.x();
	}

	/**
	 * @return the y co-ordinate of this position
	 */
	public double y() {
		return this.pMatrix.y();
	}

	/**
	 * @return the chunk row index of this object
	 */
	public int chunkRow() {
		return this.y.asChunkIndex();
	}

	/**
	 * @return the chunk column index of this object
	 */
	public int chunkColumn() {
		return this.x.asChunkIndex();
	}

	/**
	 * Overwrite the positional co-ordinates with new co-ordinates.
	 * 
	 * @param x - the x co-ordinate
	 * @param y - the y co-ordinate
	 */
	public void set(double x, double y) {
		this.pMatrix.set(x, y);
	}

	/**
	 * Overwrite the positional co-ordinates with data from a position.
	 * 
	 * @param p - a position
	 */
	public void set(Position2D p) {
		this.set(p.x(), p.y());
	}

	/**
	 * Translate the positional co-ordinates with scalar values.
	 * 
	 * @param tx - a scalar to translate this position's x co-ordinate
	 * @param ty - a scalar to translate this position's y co-ordinate
	 */
	public void translate(double tx, double ty) {
		pMatrix.translate(tx, ty);
	}

	/**
	 * Accessing methods for a position's X co-ordinate.
	 * 
	 * @author Spencer Imbleau
	 * @version December 2020
	 */
	public class X {

		/**
		 * Translate the positional x co-ordinates with a scalar value.
		 * 
		 * @param tx - a scalar to translate this position's x co-ordinate
		 */
		public void translate(double tx) {
			pMatrix.translateX(tx);
		}

		/**
		 * Overwrite the positional x co-ordinate with a new scalar value.
		 * 
		 * @param x - an x co-ordinate
		 */
		public void set(double x) {
			pMatrix.setX(x);
		}

		/**
		 * @return the position's x co-ordinate as a double
		 */
		public double asDouble() {
			return pMatrix.x();
		}

		/**
		 * @return the position's x co-ordinate as an integer
		 */
		public int asInt() {
			return (int) pMatrix.x();
		}

		/**
		 * @return the position's x co-ordinate as a chunk index
		 * @see Chunk
		 */
		public int asChunkIndex() {
			return asInt() / Chunk.SIZE;
		}

	}

	/**
	 * Accessing methods for a position's Y co-ordinate.
	 * 
	 * @author Spencer Imbleau
	 * @version December 2020
	 */
	public class Y {

		/**
		 * Translate the positional y co-ordinates with a scalar value.
		 * 
		 * @param ty - a scalar to translate this position's y co-ordinate
		 */
		public void translate(double ty) {
			pMatrix.translateY(ty);
		}

		/**
		 * Overwrite the positional y co-ordinate with a new scalar value.
		 * 
		 * @param y - an y co-ordinate
		 */
		public void set(double y) {
			pMatrix.setY(y);
		}

		/**
		 * @return the position's y co-ordinate as a double
		 */
		public double asDouble() {
			return pMatrix.y();
		}

		/**
		 * @return the position's y co-ordinate as an integer
		 */
		public int asInt() {
			return (int) pMatrix.y();
		}

		/**
		 * @return the position's y co-ordinate as a chunk index
		 * @see Chunk
		 */
		public int asChunkIndex() {
			return asInt() / Chunk.SIZE;
		}

	}

}
