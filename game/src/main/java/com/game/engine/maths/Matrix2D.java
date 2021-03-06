package com.game.engine.maths;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;

import com.game.engine.camera.AbstractCamera;

/**
 * A coordinate matrix for 2D cartesian points
 *
 * @author Spencer Imbleau
 * @version July 2020
 */
public class Matrix2D extends Matrix {

	/**
	 * Construct a 2D coordinate matrix.
	 * 
	 * @param matrix - the matrix data
	 */
	private Matrix2D(double[][] matrix) {
		super(matrix, 1, 2);
	}

	/**
	 * Create a 2D coordinate matrix.
	 *
	 * @param x - the x coordinate
	 * @param y - the y coordinate
	 * @return a coordinate matrix with the given coordinates
	 */
	public static Matrix2D create(double x, double y) {
		double[][] matrix = { { x, y } };
		return new Matrix2D(matrix);
	}

	/**
	 * Helper method which creates a rotation matrix which multiplies with a
	 * {@link Matrix2D}
	 *
	 * @param theta - the angle of rotation (in radians)
	 * @return a rotation matrix
	 */
	private static Matrix rotationMatrix(double theta) {
		double[][] rotMatrix = { { Math.cos(theta), Math.sin(theta) }, { -Math.sin(theta), Math.cos(theta) } };
		return new Matrix(rotMatrix, 2, 2);
	}

	/**
	 * Helper method which creates a scaling matrix which multiplies with a
	 * {@link Matrix2D}
	 *
	 * @param sx - the scale factor for the X axis
	 * @param sy - the scale factor for the Y axis
	 * @return a rotation matrix
	 */
	private static Matrix scaleMatrix(double sx, double sy) {
		double[][] scaleMatrix = { { sx, 0 }, { 0, sy } };
		return new Matrix(scaleMatrix, 2, 2);
	}

	@Override
	public String toString() {
		NumberFormat formatter = new DecimalFormat("000.0");
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		sb.append(formatter.format(this.matrix[0][0]));
		sb.append(", ");
		sb.append(formatter.format(this.matrix[0][1]));
		sb.append(']');
		return sb.toString();
	}

	@Override
	public Matrix2D clone() {
		double[][] copyMatrix = null;
		if (this.getArray() != null) {
			copyMatrix = Arrays.stream(this.matrix).map(double[]::clone).toArray(double[][]::new);
		}
		return new Matrix2D(copyMatrix);
	}

	/**
	 * Setter method for the X and Y element at once.
	 * 
	 * @param x - The value to set for X
	 * @param y - The value to set for Y
	 */
	public void set(double x, double y) {
		this.matrix[0][0] = x;
		this.matrix[0][1] = y;
	}

	/**
	 * Setter method for the X element element.
	 * 
	 * @param s - The value to set for X
	 */
	public void setX(double s) {
		this.matrix[0][0] = s;
	}

	/**
	 * Setter method for the Y element element.
	 * 
	 * @param s - The value to set for Y
	 */
	public void setY(double s) {
		this.matrix[0][1] = s;
	}

	/**
	 * Translates a matrix by given deltas.
	 * 
	 * @param tx - translational delta x
	 * @param ty - translational delta y
	 * @return A new matrix equaling this matrix translated by given constants.
	 */
	public Matrix2D translate(double tx, double ty) {
		double[][] translateData = { { this.matrix[0][0] + tx, this.matrix[0][1] + ty } };
		return new Matrix2D(translateData);
	}

	/**
	 * Translates this matrix by given deltas.
	 * 
	 * @param tx - translational delta x
	 * @param ty - translational delta y
	 * @return this matrix equaling translated by given constants.
	 */
	public Matrix2D translateEquals(double tx, double ty) {
		this.matrix[0][0] = this.matrix[0][0] + tx;
		this.matrix[0][1] = this.matrix[0][1] + ty;
		return this;
	}

	/**
	 * Translates the X co-ordinate of a matrix by a given delta.
	 * 
	 * @param tx - translational delta x
	 * @return A new matrix equaling this matrix translated on the X axis by a given
	 *         constant.
	 */
	public Matrix2D translateX(double tx) {
		double[][] translateData = { { this.matrix[0][0] + tx, this.matrix[0][1] } };
		return new Matrix2D(translateData);
	}

	/**
	 * Translates the X co-ordinate of this matrix by a given delta.
	 * 
	 * @param tx - translational delta x
	 * @return this matrix equaling translated on the X axis by given constant.
	 */
	public Matrix2D translateXEquals(double tx) {
		this.matrix[0][0] = this.matrix[0][0] + tx;
		return this;
	}

	/**
	 * Translates the Y co-ordinate of a matrix by a given delta.
	 * 
	 * @param ty - translational delta y
	 * @return A new matrix equaling this matrix translated on the Y axis by a given
	 *         constant.
	 */
	public Matrix2D translateY(double ty) {
		double[][] translateData = { { this.matrix[0][0], this.matrix[0][1] + ty } };
		return new Matrix2D(translateData);
	}

	/**
	 * Translates the Y co-ordinate of this matrix by a given delta.
	 * 
	 * @param ty - translational delta y
	 * @return this matrix equaling translated on the Y axis by given constant.
	 */
	public Matrix2D translateYEquals(double ty) {
		this.matrix[0][1] = this.matrix[0][1] + ty;
		return this;
	}

	/**
	 * Rotates a matrix by a given angle.
	 * 
	 * @param theta - the angle of rotation (in radians)
	 * @return A new matrix equaling this matrix rotated by a given theta.
	 */
	public Matrix2D rotate(double theta) {
		Matrix buf = this.times(rotationMatrix(theta));
		return Matrix2D.create(buf.matrix[0][0], buf.matrix[0][1]);
	}

	/**
	 * Rotates this matrix by a given angle.
	 * 
	 * @param theta - the angle of rotation (in radians)
	 * @return This matrix equaling rotated by a given theta.
	 */
	public Matrix2D rotateEquals(double theta) {
		return (Matrix2D) this.timesEquals(rotationMatrix(theta));
	}

	/**
	 * Rotates a matrix by a given angle around an anchor point.
	 * 
	 * @param theta - the angle of rotation (in radians)
	 * @param x     - anchor point x to rotate around
	 * @param y     - anchor point y to rotate around
	 * @return A new matrix equaling this matrix rotated by a given theta around an
	 *         anchor point.
	 */
	public Matrix2D rotate(double theta, double x, double y) {
		return this.translate(-x, -y).rotateEquals(theta).translateEquals(x, y);
	}

	/**
	 * Rotates this matrix by a given angle around an anchor point.
	 * 
	 * @param theta - the angle of rotation (in radians)
	 * @param x     - anchor point x to rotate around
	 * @param y     - anchor point y to rotate around
	 * @return This matrix equaling rotated by a given theta around an anchor point.
	 */
	public Matrix2D rotateEquals(double theta, double x, double y) {
		return this.translateEquals(-x, -y).rotateEquals(theta).translateEquals(x, y);
	}

	/**
	 * Scales a matrix by given scalars
	 * 
	 * @param sx - the scale factor for the X axis
	 * @param sy - the scale factor for the Y axis
	 * @return A new matrix equaling this matrix scaled by the given scalars.
	 */
	public Matrix2D scale(double sx, double sy) {
		Matrix buf = this.times(scaleMatrix(sx, sy));
		return Matrix2D.create(buf.matrix[0][0], buf.matrix[0][1]);
	}

	/**
	 * Scales this matrix by given scalars
	 * 
	 * @param sx - the scale factor for the X axis
	 * @param sy - the scale factor for the Y axis
	 * @return This matrix scaled by the given scalars.
	 */
	public Matrix2D scaleEquals(double sx, double sy) {
		return (Matrix2D) this.timesEquals(scaleMatrix(sx, sy));
	}

	/**
	 * Scales a matrix by a constant scalar
	 * 
	 * @param s - the scale factor
	 * @return A new matrix equaling this matrix scaled by the constant scalar.
	 */
	public Matrix2D scale(double s) {
		Matrix buf = this.times(s);
		return Matrix2D.create(buf.matrix[0][0], buf.matrix[0][1]);
	}

	/**
	 * Scales this matrix by a constant scalar
	 * 
	 * @param s - the scale factor
	 * @return This matrix scaled by the constant scalar.
	 */
	public Matrix2D scaleEquals(double s) {
		return (Matrix2D) this.timesEquals(s);
	}

	/**
	 * Transform a matrix by a given camera.
	 *
	 * @param camera - a camera
	 * @return A new matrix equaling this matrix transformed by a given camera.
	 */
	public Matrix2D transform(AbstractCamera camera) {
		return this.translate(-camera.viewport.x(), -camera.viewport.y()).scaleEquals(camera.zoom());
	}

	/**
	 * Transform this matrix by a given camera.
	 *
	 * @param camera - a camera
	 * @return This matrix transformed by a given camera.
	 */
	public Matrix2D transformEquals(AbstractCamera camera) {
		return this.translateEquals(-camera.viewport.x(), -camera.viewport.y()).scaleEquals(camera.zoom());
	}
	
	public double dot(Matrix2D m) {
		return this.x() * m.x() + this.y() * m.y();
	}

	public double cross(Matrix2D m) {
		return this.x() * m.y() - this.y() * m.x();
	}

	/**
	 * @return the x value in this coordinate matrix
	 */
	public double x() {
		return this.matrix[0][0];
	}

	/**
	 * @return the y value in this coordinate matrix
	 */
	public double y() {
		return this.matrix[0][1];
	}
	
	
}
