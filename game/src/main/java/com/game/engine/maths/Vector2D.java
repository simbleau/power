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
	 * Checks the equivalence of an object to this 2D vector. For a 2D vector to be
	 * equal, the backing 2D matrices must be equal.
	 *
	 * @param o - the vector to be tested against.
	 * @return true if they are equal, false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Vector2D) {
			Vector2D v = (Vector2D) o;
			return this.vMatrix.equals(v.vMatrix);
		} else {
			return false;
		}
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
	
	public Matrix2D getMatrix() {
		return this.vMatrix;
	}
	
	public Vector2D invert() {
		if (this.x() != 0) {
			this.vMatrix.setX(1d / this.x());
		}
		if (this.y() != 0) {
			this.vMatrix.setY(1d / this.y());
		}
		return this;
	}

	/**
	 * @return the displacement in the x direction of this vector
	 */
	public double x() {
		return this.vMatrix.x();
	}

	/**
	 * @return the displacement in the y direction of this vector
	 */
	public double y() {
		return this.vMatrix.y();
	}

	/**
	 * Returns this vector by which was added to by another vector.
	 *
	 * @param v - a vector used to modify this vector
	 * @return this vector added by the given vector
	 */
	public Vector2D plus(Vector2D v) {
		this.vMatrix.translate(v.x(), v.y());
		return this;
	}

	/**
	 * Returns this vector by which was subtracted from by another vector.
	 *
	 * @param v - a vector used to modify this vector
	 * @return this vector subtracted by the given vector
	 */
	public Vector2D minus(Vector2D v) {
		this.vMatrix.translate(-v.x(), -v.y());
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
		double length = this.length();
		if (length == 0) {
			// Cannot normalize it
			return this;
		}
		this.scale(1d / length);
		return this;
	}

	/**
	 * Rotates this vector by a given angle in radians around the point (0, 0).
	 * Positive values rotate counter-clockwise and negative values rotate
	 * clockwise.
	 * 
	 * @param theta - the angle of rotation (in radians)
	 * @return this vector rotated by a given theta
	 */
	public Vector2D rotate(double theta) {
		this.vMatrix.rotate(theta);
		return this;
	}

	/**
	 * Rotates this vector by a given angle in radians around an anchor point.
	 * Positive values rotate counter-clockwise and negative values rotate
	 * clockwise.
	 * 
	 * @param theta - the angle of rotation (in radians)
	 * @param x     - anchor point x to rotate around
	 * @param y     - anchor point y to rotate around
	 * @return this vector rotated by a given theta around an anchor point
	 */
	public Vector2D rotate(double theta, double x, double y) {
		this.vMatrix.rotate(theta, x, y);
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
		this.vMatrix.scale(v.x(), v.y());
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
		return Math.sqrt(this.lengthSquared());
	}

	/**
	 * @return the length (also known as the magnitude) of this vector to the power
	 *         of two
	 */
	public double lengthSquared() {
		return this.vMatrix.x() * this.vMatrix.x() + this.vMatrix.y() * this.vMatrix.y();
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
