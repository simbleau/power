package com.game.engine.maths;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.game.engine.camera.AbstractCamera;

/**
 * A two-dimensional matrix for handling double precision float data. This
 * matrix class also uses the decorator pattern to increase readability and make
 * it easy to transform matrices quickly.
 *
 * @author Spencer Imbleau
 * @version January 2021
 */
public class Matrix2D extends Matrix {

	/**
	 * Construct a 2D matrix.
	 *
	 * @param x - the x coordinate
	 * @param y - the y coordinate
	 */
	public Matrix2D(double x, double y) {
		super(new double[][] { { x, y } }, 1, 2);
	}

	/**
	 * Construct a 2D matrix with zeroed values.
	 */
	public Matrix2D() {
		this(0, 0);
	}

	/**
	 * Creates and returns a rotation matrix with a given angle in radians, theta.
	 * The resulting matrix is compatible for matrix multiplication on a
	 * {@link Matrix2D} object.
	 *
	 * @param theta - the angle, in radians, to rotate the X and Y elements
	 * @return a rotation matrix
	 */
	private static Matrix rotationMatrix(double theta) {
		double[][] rotMatrix = { { Math.cos(theta), Math.sin(theta) }, { -Math.sin(theta), Math.cos(theta) } };
		return new Matrix(rotMatrix, 2, 2);
	}

	/**
	 * Creates and returns a scaling matrix with given axial scalars. The resulting
	 * matrix is compatible for matrix multiplication on a {@link Matrix2D} object.
	 *
	 * @param sx - the axial scaling factor for the X element
	 * @param sy - the axial scaling factor for the Y element
	 * @return a scaling matrix
	 */
	public static Matrix scaleMatrix(double sx, double sy) {
		double[][] scaleMatrix = { { sx, 0 }, { 0, sy } };
		return new Matrix(scaleMatrix, 2, 2);
	}

	/**
	 * Represent this 2D matrix in standard matrix representation. For readability,
	 * all floating-point values have been rounded to the tenths-place and decimal
	 * values will display a leading zero.<br>
	 * <br>
	 * e.g., a 2D matrix with x of 1 and y of 0 will output:
	 *
	 * <pre>
	 * {1.0, 0.0}
	 * </pre>
	 *
	 */
	@Override
	public String toString() {
		NumberFormat formatter = new DecimalFormat("0.0");
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		sb.append(formatter.format(this.matrix[0][0]));
		sb.append(", ");
		sb.append(formatter.format(this.matrix[0][1]));
		sb.append('}');
		return sb.toString();
	}

	/**
	 * Creates and returns a deep-copy of this 2D Matrix object.
	 */
	@Override
	public Matrix2D clone() {
		return new Matrix2D(this.x(), this.y());
	}

	/**
	 * Set both the X and Y elements at once.
	 *
	 * @param x - The value to set for the X element
	 * @param y - The value to set for the Y element
	 */
	public void set(double x, double y) {
		this.matrix[0][0] = x;
		this.matrix[0][1] = y;
	}

	/**
	 * Set the X element.
	 *
	 * @param s - The value to set for the X element
	 */
	public void setX(double s) {
		this.matrix[0][0] = s;
	}

	/**
	 * Set the Y element.
	 *
	 * @param s - The value to set for the Y element
	 */
	public void setY(double s) {
		this.matrix[0][1] = s;
	}

	/**
	 * Translates this matrix by given deltas.
	 *
	 * @param tx - translational delta x
	 * @param ty - translational delta y
	 * @return this matrix with a translated elements
	 */
	public Matrix2D translate(double tx, double ty) {
		this.matrix[0][0] = this.matrix[0][0] + tx;
		this.matrix[0][1] = this.matrix[0][1] + ty;
		return this;
	}

	/**
	 * Translates the X element of this matrix by a given delta.
	 *
	 * @param tx - translational delta x
	 * @return this matrix with a translated X element
	 */
	public Matrix2D translateX(double tx) {
		this.matrix[0][0] = this.matrix[0][0] + tx;
		return this;
	}

	/**
	 * Translates the Y element of this matrix by a given delta.
	 *
	 * @param ty - translational delta y
	 * @return this matrix with a translated Y element
	 */
	public Matrix2D translateY(double ty) {
		this.matrix[0][1] = this.matrix[0][1] + ty;
		return this;
	}

	/**
	 * Rotates this matrix by a given angle. Positive values rotate
	 * counter-clockwise and negative values rotate clockwise.
	 *
	 * @param theta - the angle of rotation (in radians)
	 * @return this matrix rotated by a given theta
	 */
	public Matrix2D rotate(double theta) {
		return (Matrix2D) this.times(rotationMatrix(theta));
	}

	/**
	 * Rotates this matrix by a given angle in radians around an anchor point.
	 * Positive values rotate counter-clockwise and negative values rotate
	 * clockwise.
	 *
	 * @param theta - the angle of rotation (in radians)
	 * @param x     - anchor point x to rotate around
	 * @param y     - anchor point y to rotate around
	 * @return this matrix rotated by a given theta around an anchor point
	 */
	public Matrix2D rotate(double theta, double x, double y) {
		return this.translate(-x, -y).rotate(theta).translate(x, y);
	}

	/**
	 * Scales this matrix by given scalars values.
	 *
	 * @param sx - the scale factor for the X element
	 * @param sy - the scale factor for the Y element
	 * @return this matrix scaled by given scalars
	 */
	public Matrix2D scale(double sx, double sy) {
		return (Matrix2D) this.times(scaleMatrix(sx, sy));
	}

	/**
	 * Scales all elements in this matrix by a scalar value.
	 *
	 * @param s - the scale factor for the X and Y element
	 * @return this matrix scaled by a given scalar
	 */
	public Matrix2D scale(double s) {
		return (Matrix2D) this.times(s);
	}

	/**
	 * Transform this matrix by a given camera.
	 *
	 * @param camera - a camera
	 * @return this matrix transformed by a given camera.
	 */
	public Matrix2D transform(AbstractCamera camera) {
		return this.translate(-camera.viewport.x(), -camera.viewport.y()).scale(camera.zoom());
	}

	/**
	 * @return the x element in this 2D matrix
	 */
	public double x() {
		return this.matrix[0][0];
	}

	/**
	 * @return the y element in this 2D matrix
	 */
	public double y() {
		return this.matrix[0][1];
	}

}
