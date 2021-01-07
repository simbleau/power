package com.game.engine.maths;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * A two-dimensional vector which stores a direction and magnitude, stored as
 * double precision float data. This vector class also uses the decorator
 * pattern to increase readability and make it easy to transform vectors
 * quickly.
 *
 * @author Spencer Imbleau
 * @version January 2021
 */
public class Vector2D implements Cloneable {

	/**
	 * The backing matrix of this vector.
	 */
	private Matrix2D vMatrix;

	/**
	 * Construct a 2D vector.
	 *
	 * @param dx - the displacement in the X direction
	 * @param dy - the displacement in the Y direction
	 */
	public Vector2D(double dx, double dy) {
		this.vMatrix = new Matrix2D(dx, dy);
	}

	/**
	 * Construct a 2D vector with zeroed values.
	 */
	public Vector2D() {
		this.vMatrix = new Matrix2D();
	}

	/**
	 * Construct a vector quickly without checking arguments.
	 *
	 * @param m - a backing vector matrix
	 */
	protected Vector2D(Matrix2D m) {
		this.vMatrix = m;
	}

	/**
	 * Create and return the unit vector of a given vector.
	 *
	 * @param v - a given vector
	 * @return a new unit vector
	 */
	public static Vector2D getUnitVector(Vector2D v) {
		Vector2D clone = v.clone();
		clone.normalize();
		return clone;
	}

	/**
	 * Represent this vector in standard vector representation. For readability, all
	 * floating-point values have been rounded to the tenths-place and decimal
	 * values will display a leading zero.<br>
	 * <br>
	 * e.g., a vector with x of 1.56 and y of 0 will output:
	 *
	 * <pre>
	 * <1.5, 0.0>
	 * </pre>
	 *
	 */
	@Override
	public String toString() {
		NumberFormat formatter = new DecimalFormat("0.0");
		StringBuilder sb = new StringBuilder();
		sb.append('<');
		sb.append(formatter.format(this.vMatrix.x()));
		sb.append(", ");
		sb.append(formatter.format(this.vMatrix.y()));
		sb.append('>');
		return sb.toString();
	}

	/**
	 * Creates and returns a deep-copy of this 2D Vector object.
	 */
	@Override
	public Vector2D clone() {
		Matrix2D m = this.vMatrix.clone();
		return new Vector2D(m);
	}

	/**
	 * @return the displacement in the x direction of this vector
	 */
	public double dx() {
		return this.vMatrix.x();
	}

	/**
	 * @return the displacement in the y direction of this vector
	 */
	public double dy() {
		return this.vMatrix.y();
	}

	/**
	 * Returns this vector by which was added to by another vector.
	 *
	 * @param v - a vector used to modify this vector
	 * @return this vector added by the given vector
	 */
	public Vector2D plus(Vector2D v) {
		this.vMatrix.translate(v.dx(), v.dy());
		return this;
	}

	/**
	 * Returns this vector by which was subtracted from by another vector.
	 *
	 * @param v - a vector used to modify this vector
	 * @return this vector subtracted by the given vector
	 */
	public Vector2D minus(Vector2D v) {
		this.vMatrix.translate(-v.dx(), -v.dy());
		return this;
	}

	/**
	 * Returns this vector by which was scaled by a given scalar value.
	 *
	 * @param s - a scalar used to modify this vector
	 * @return this vector scaled by the given scalar
	 */
	public Vector2D scale(double s) {
		this.vMatrix.scale(s);
		return this;
	}

	/**
	 * Normalize this vector such that is will turn into a unit vector, e.g., will
	 * have a magnitude of 1 and unchanged direction.
	 *
	 * @return this vector normalized
	 */
	public Vector2D normalize() {
		this.scale(1d / this.length());
		return this;
	}

	/**
	 * Returns this vector by which is now the resulting component product of a
	 * given vector.
	 *
	 * @param v - a vector used to modify this vector
	 * @return this vector, now the component product of the given vector
	 */
	public Vector2D componentProduct(Vector2D v) {
		this.vMatrix.scale(v.dx(), v.dy());
		return this;
	}

	/**
	 * @param v - a vector used to produce the dot product with this vector
	 * @return the scalar dot product value of this vector and another vector
	 */
	public double dotProduct(Vector2D v) {
		return this.vMatrix.x() * v.vMatrix.x() + this.vMatrix.y() * v.vMatrix.y();
	}

	/**
	 * @param v - a vector used to produce the dot product with this vector
	 * @return the scalar cross product value of this vector and another vector
	 */
	public double crossProduct(Vector2D v) {
		return this.vMatrix.x() * v.vMatrix.y() - this.vMatrix.y() * v.vMatrix.x();
	}

	/**
	 * @return the length (also known as the magnitude) of this vector
	 */
	public double length() {
		return Math.sqrt(this.dx() * this.dx() + this.dy() * this.dy());
	}

	/**
	 * @param v - a vector to test parallelism
	 * @return true if this vector is parallel with the given vector, false
	 *         otherwise
	 */
	public boolean isParallel(Vector2D v) {
		return this.dotProduct(v) == 1;
	}

	/**
	 * @param v - a vector to test perpendicularity
	 * @return true if this vector is perpendicular with the given vector, false
	 *         otherwise
	 */
	public boolean isPerpendicular(Vector2D v) {
		return this.dotProduct(v) == 0;
	}

}
