package com.game.engine.coordinates;

import com.game.engine.camera.AbstractCamera;

/**
 * A coordinate matrix for 2D cartesian points
 *
 * @author Spencer Imbleau
 * @version July 2020
 */
public class CoordinateMatrix extends Matrix {

	/**
	 * Construct a 2D coordinate matrix.
	 * 
	 * @param matrix - the matrix data
	 */
	private CoordinateMatrix(double[][] matrix) {
		super(matrix, 1, 2);
	}

	/**
	 * Create a 2D coordinate matrix.
	 *
	 * @param x - the x coordinate
	 * @param y - the y coordinate
	 * @return a coordinate matrix with the given coordinates
	 */
	public static CoordinateMatrix create(double x, double y) {
		double[][] matrix = { { x, y } };
		return new CoordinateMatrix(matrix);
	}

	/**
	 * Helper method which creates a rotation matrix which multiplies with a
	 * {@link CoordinateMatrix}
	 *
	 * @param theta - the angle of rotation (in radians)
	 * @return a rotation matrix
	 */
	private static Matrix rotationMatrix(double theta) {
		double[][] rotMatrix = { { Math.cos(theta), Math.sin(theta) },
				{ -Math.sin(theta), Math.cos(theta) } };
		return new Matrix(rotMatrix, 2, 2);
	}

	/**
	 * Helper method which creates a scaling matrix which multiplies with a
	 * {@link CoordinateMatrix}
	 *
	 * @param sx - the scale factor for the X axis
	 * @param sy - the scale factor for the Y axis
	 * @return a rotation matrix
	 */
	private static Matrix scaleMatrix(double sx, double sy) {
		double[][] scaleMatrix = { { sx, 0 }, { 0, sy } };
		return new Matrix(scaleMatrix, 2, 2);
	}

	/**
	 * Translates a matrix by given deltas.
	 * 
	 * @param tx - translational delta x
	 * @param ty - translational delta y
	 * @return A new matrix equaling this matrix translated by given constants.
	 */
	public CoordinateMatrix translate(double tx, double ty) {
		double[][] translateData = { { this.matrix[0][0] + tx, this.matrix[0][1] + ty } };
		return new CoordinateMatrix(translateData);
	}

	/**
	 * Translates this matrix by given deltas.
	 * 
	 * @param tx - translational delta x
	 * @param ty - translational delta y
	 * @return this matrix equaling translated by given constants.
	 */
	public CoordinateMatrix translateEquals(double tx, double ty) {
		this.matrix[0][0] = this.matrix[0][0] + tx;
		this.matrix[0][1] = this.matrix[0][1] + ty;
		return this;
	}

	/**
	 * Rotates a matrix by a given angle.
	 * 
	 * @param theta - the angle of rotation (in radians)
	 * @return A new matrix equaling this matrix rotated by a given theta.
	 */
	public CoordinateMatrix rotate(double theta) {
		Matrix buf = this.times(rotationMatrix(theta));
		return CoordinateMatrix.create(buf.matrix[0][0], buf.matrix[0][1]);
	}
	
	/**
	 * Rotates this matrix by a given angle.
	 * 
	 * @param theta - the angle of rotation (in radians)
	 * @return This matrix equaling rotated by a given theta.
	 */
	public CoordinateMatrix rotateEquals(double theta) {
		return (CoordinateMatrix) this.timesEquals(rotationMatrix(theta));
	}

	/**
	 * Rotates a matrix by a given angle around an anchor point.
	 * 
	 * @param theta - the angle of rotation (in radians)
	 * @param x     - anchor point x to rotate around
	 * @param y     - anchor point y to rotate around
	 * @return A new matrix equaling this matrix rotated by a given theta around an anchor point.
	 */
	public CoordinateMatrix rotate(double theta, double x, double y) {
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
	public CoordinateMatrix rotateEquals(double theta, double x, double y) {
		return this.translateEquals(-x, -y).rotateEquals(theta).translateEquals(x, y);
	}

	/**
	 * Scales a matrix by given scalars
	 * 
	 * @param sx - the scale factor for the X axis
	 * @param sy - the scale factor for the Y axis
	 * @return A new matrix equaling this matrix scaled by the given scalars.
	 */
	public CoordinateMatrix scale(double sx, double sy) {
		Matrix buf = this.times(scaleMatrix(sx, sy));
		return CoordinateMatrix.create(buf.matrix[0][0], buf.matrix[0][1]);
	}
	
	/**
	 * Scales this matrix by given scalars
	 * 
	 * @param sx - the scale factor for the X axis
	 * @param sy - the scale factor for the Y axis
	 * @return This matrix scaled by the given scalars.
	 */
	public CoordinateMatrix scaleEquals(double sx, double sy) {
		return (CoordinateMatrix) this.timesEquals(scaleMatrix(sx, sy));
	}
	
	/**
	 * Scales a matrix by a constant scalar
	 * 
	 * @param s - the scale factor
	 * @return A new matrix equaling this matrix scaled by the constant scalar.
	 */
	public CoordinateMatrix scale(double s) {
		Matrix buf = this.times(s);
		return CoordinateMatrix.create(buf.matrix[0][0], buf.matrix[0][1]);
	}
	
	/**
	 * Scales this matrix by a constant scalar
	 * 
	 * @param s - the scale factor
	 * @return This matrix scaled by the constant scalar.
	 */
	public CoordinateMatrix scaleEquals(double s) {
		return (CoordinateMatrix) this.timesEquals(s);
	}

	/**
	 * Transform a matrix by a given camera.
	 *
	 * @param camera - a camera
	 * @return A new matrix equaling this matrix transformed by a given camera.
	 */
	public CoordinateMatrix transform(AbstractCamera camera) {
		return this.translate(-camera.x(), -camera.y()).scaleEquals(camera.zoom());
	}
	
	/**
	 * Transform this matrix by a given camera.
	 *
	 * @param camera - a camera
	 * @return This matrix transformed by a given camera.
	 */
	public CoordinateMatrix transformEquals(AbstractCamera camera) {
		return this.translateEquals(-camera.x(), -camera.y()).scaleEquals(camera.zoom());
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
