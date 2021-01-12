package com.game.engine.maths;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Random;

/**
 * A matrix for handling double precision float data. This matrix class also
 * uses the decorator pattern to increase readability and make it easy to
 * transform matrices quickly.
 *
 * @author Spencer Imbleau
 * @version January 2021
 */
public class Matrix implements Cloneable {

	/**
	 * The rows in the matrix.
	 */
	protected int rows;

	/**
	 * The columns in the matrix.
	 */
	protected int columns;

	/**
	 * The matrix data.
	 */
	protected double[][] matrix;

	/**
	 * Construct a rows-by-columns matrix of zeros.
	 *
	 * @param rows    - number of rows
	 * @param columns - number of colums
	 */
	public Matrix(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		this.matrix = new double[rows][columns];
	}

	/**
	 * Construct a rows-by-columns constant matrix.
	 *
	 * @param rows    - number of rows
	 * @param columns - number of colums
	 * @param s       - a scalar value to initialize all elements to
	 */
	public Matrix(int rows, int columns, double s) {
		this(rows, columns);
		Arrays.stream(this.matrix).forEach(a -> Arrays.fill(a, s));
	}

	/**
	 * Construct a matrix from an array.
	 *
	 * @param matrix - an array of matrix data
	 * @throws IllegalArgumentException all rows must have the same length
	 */
	public Matrix(double[][] matrix) throws IllegalArgumentException {
		if (matrix == null) {
			throw new IllegalArgumentException("Matrix data cannot be null.");
		}

		// Check all submatrices have the same length
		int cols = (matrix.length > 0) ? matrix[0].length : 0;
		for (double[] subMat : matrix) {
			if (subMat.length != cols) {
				throw new IllegalArgumentException("Matrix data must have consistent length for all submatrices.");
			}
		}

		this.rows = matrix.length;
		this.columns = cols;
		this.matrix = matrix;
	}

	/**
	 * Construct a matrix quickly without checking arguments.
	 *
	 * @param matrix  - two-dimensional array of doubles
	 * @param rows    - number of rows
	 * @param columns - number of colums
	 */
	protected Matrix(double[][] matrix, int rows, int columns) {
		this.matrix = matrix;
		this.rows = rows;
		this.columns = columns;
	}

	/**
	 * Creates and returns an m-by-n matrix containing random values between 0 and
	 * 1.
	 *
	 * @param m - the number of rows for the matrix
	 * @param n - the number of columns for the matrix
	 * @return an m-by-n matrix containing random values between 0 and 1
	 */
	public static Matrix random(int m, int n) {
		Random rand = new Random(System.nanoTime());
		double[][] matrixData = new double[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				matrixData[i][j] = rand.nextDouble();
			}
		}
		return new Matrix(matrixData);
	}

	/**
	 * Creates and returns an m-by-n identity matrix.
	 *
	 * @param m - the number of rows for the matrix
	 * @param n - the number of columns for the matrix
	 * @throws IllegalArgumentException array length must be a multiple of rows
	 * @return an m-by-n identity matrix
	 */
	public static Matrix identity(int m, int n) throws IllegalArgumentException {
		if (m < 0 || n < 0) {
			throw new IllegalArgumentException("Illegal size for identity matrix: " + m + "x" + n);
		}

		double[][] data = new double[m][n];
		for (int ri = 0; ri < m; ri++) {
			for (int ci = 0; ci < n; ci++) {
				data[ri][ci] = (ri == ci) ? 1 : 0;
			}
		}

		return new Matrix(data, m, n);
	}

	/**
	 * Checks the equivalence of an object to this matrix. For a matrix to be equal,
	 * the following conditions must be true:
	 * 
	 * <ul>
	 * <li>Both matrices must have the same number of rows</li>
	 * <li>Both matrices must have the same number of columns</li>
	 * <li>Elements in one matrix must correspond in equivalence to an allowed
	 * degree of error to the other matrix</li>
	 * </ul>
	 *
	 * @param o - the matrix to be tested against.
	 * @return true if they are equal, false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Matrix) {
			Matrix m = (Matrix) o;
			// Test row equivalence
			if (m.rows != this.rows) {
				return false;
			}
			// Test column equivalence
			if (m.columns != this.columns) {
				return false;
			}
			// Test double equivalence
			for (int i = 0; i < this.rows; i++) {
				for (int j = 0; j < this.columns; j++) {
					double e1 = this.matrix[i][j];
					double e2 = m.matrix[i][j];
					if (Math.abs(e1 - e2) > Double.MIN_VALUE * Math.max(1, Math.max(e1, e2))) {
						return false;
					}
				}
			}
			// No checks failed, they are equal
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Represent this matrix in standard matrix representation. For readability, all
	 * floating-point values have been rounded to the tenths-place and decimal
	 * values will display a leading zero.<br>
	 * <br>
	 * e.g., a matrix with 3x2 matrix of zeroes will output:
	 *
	 * <pre>
	 * { { 0.0, 0.0 }, { 0.0, 0.0 }, { 0.0, 0.0 } }
	 * </pre>
	 *
	 */
	@Override
	public String toString() {
		NumberFormat formatter = new DecimalFormat("000.0");
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		for (int row = 0; row < this.rows; row++) {
			if (row != 0) {
				sb.append(", ");
			}
			sb.append('{');
			for (int col = 0; col < this.columns; col++) {
				if (col != 0) {
					sb.append(", ");
				}
				sb.append(formatter.format(this.matrix[row][col]));
			}
			sb.append('}');
		}
		sb.append('}');
		return sb.toString();
	}

	/**
	 * Creates and returns a deep-copy of this Matrix object.
	 */
	@Override
	public Matrix clone() {
		double[][] copyMatrix = null;
		if (this.getArray() != null) {
			copyMatrix = Arrays.stream(this.matrix).map(double[]::clone).toArray(double[][]::new);
		}
		return new Matrix(copyMatrix, this.rows, this.columns);
	}

	/**
	 * Return the internal matrix element array. Changes made to this array will
	 * affect the object.
	 *
	 * @return a pointer to the array of matrix elements
	 */
	public double[][] getArray() {
		return this.matrix;
	}

	/**
	 * @return the number of rows in the matrix
	 */
	public int getNumRows() {
		return this.rows;
	}

	/**
	 * @return the number of columns in the matrix
	 */
	public int getNumCols() {
		return this.columns;
	}

	/**
	 * Return the element in the matrix at given position.
	 *
	 * @param row    - the row from which to retrieve the element
	 * @param column - the column from which to retrieve the element
	 * @return the element at matrix[i][j]
	 * @throws ArrayIndexOutOfBoundsException if the row or column specified is not
	 *                                        within the bounds of the matrix
	 */
	public double get(int row, int column) {
		if (row < 0 || row >= this.rows || column < 0 || column >= this.columns) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return this.matrix[row][column];
	}

	/**
	 * Return the submatrix specified by the row and column indices given.
	 *
	 * @param r1 Initial row index
	 * @param c1 Initial column index
	 * @param r2 Final row index
	 * @param c2 Final column index
	 * @return a new matrix bounded from r1->r2 and c1->c2
	 * @throws ArrayIndexOutOfBoundsException if the submatrix bounds do not fit
	 *                                        inside this matrix
	 */
	public Matrix getMatrix(int r1, int c1, int r2, int c2) {
		int numRows = r2 - r1 + 1;
		int numCols = c2 - c1 + 1;
		if (r1 < 0 || r2 >= this.rows || c1 < 0 || c2 >= this.columns || r1 > r2 || c1 > c2) {
			throw new ArrayIndexOutOfBoundsException();
		}
		double[][] result = new double[numRows][numCols];
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) {
				result[i][j] = this.matrix[i + r1][j + c1];
			}
		}
		return new Matrix(result);
	}

	/**
	 * Return the submatrix specified by the rows and column indices given.
	 *
	 * @param rows - array of row indices
	 * @param c1   - initial column index
	 * @param c2   - final column index
	 * @return a new matrix bounded by the rows given and c1->c2
	 * @throws ArrayIndexOutOfBoundsException if the submatrix bounds do not fit
	 *                                        inside this matrix
	 */
	public Matrix getMatrix(int[] rows, int c1, int c2) {
		int numRows = rows.length;
		int numCols = c2 - c1 + 1;
		if (c1 < 0 || c2 >= this.columns || c1 > c2) {
			throw new ArrayIndexOutOfBoundsException();
		}
		double[][] result = new double[numRows][numCols];
		for (int i = 0; i < numRows; i++) {
			if (rows[i] < 0 || rows[i] > this.rows) {
				throw new ArrayIndexOutOfBoundsException();
			}
			for (int j = 0; j < numCols; j++) {
				result[i][j] = this.matrix[rows[i]][j + c1];
			}
		}
		return new Matrix(result);
	}

	/**
	 * Returns the submatrix specified by the row indices and columns given.
	 *
	 * @param r1 - initial row index
	 * @param r2 - final row index
	 * @param c  - array of column indices
	 * @return a new matrix bounded by the columns given and r1->r2
	 * @throws ArrayIndexOutOfBoundsException if the submatrix bounds do not fit
	 *                                        inside this matrix
	 */
	public Matrix getMatrix(int r1, int r2, int[] c) {
		int numRows = r2 - r1 + 1;
		int numCols = c.length;
		if (r1 < 0 || r2 >= this.columns || r1 > r2) {
			throw new ArrayIndexOutOfBoundsException();
		}
		double[][] result = new double[numRows][numCols];
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) {
				if (c[j] < 0 || c[j] > this.columns) {
					throw new ArrayIndexOutOfBoundsException();
				}
				result[i][j] = this.matrix[i + r1][c[j]];
			}
		}
		return new Matrix(result);
	}

	/**
	 * Returns the submatrix specified by the rows and columns given.
	 *
	 * @param r - array of row indices
	 * @param c - array of column indices
	 * @return a new matrix bounded by the rows and columns given
	 * @throws ArrayIndexOutOfBoundsException if the submatrix bounds do not fit
	 *                                        inside this matrix
	 */
	public Matrix getMatrix(int r[], int[] c) {
		int numRows = r.length;
		int numCols = c.length;
		double[][] result = new double[numRows][numCols];
		for (int i = 0; i < numRows; i++) {
			if (r[i] < 0 || r[i] > this.rows) {
				throw new ArrayIndexOutOfBoundsException();
			}
			for (int j = 0; j < numCols; j++) {
				if (c[j] < 0 || c[j] > this.columns) {
					throw new ArrayIndexOutOfBoundsException();
				}
				result[i][j] = this.matrix[r[i]][c[j]];
			}
		}
		return new Matrix(result);
	}

	/**
	 * Set a single matrix element.
	 *
	 * @param r - row index
	 * @param c - column index
	 * @param s - the scalar value to set at {r,c}
	 * @throws ArrayIndexOutOfBoundsException If the submatrix bounds do not fit
	 *                                        inside this matrix
	 */
	public void set(int r, int c, double s) {
		if (r < 0 || r >= this.rows || c < 0 || c >= this.columns) {
			throw new ArrayIndexOutOfBoundsException();
		}
		this.matrix[r][c] = s;
	}

	/**
	 * Overwrites this matrix to be a defined submatrix.
	 *
	 * @param m  - a matrix to retrieve a submatrix from
	 * @param r1 - initial row index
	 * @param c1 - initial column index
	 * @param r2 - final row index
	 * @param c2 - final column index
	 * @throws ArrayIndexOutOfBoundsException if the submatrix bounds do not fit
	 *                                        inside the given matrix
	 */
	public void setMatrix(Matrix m, int r1, int c1, int r2, int c2) {
		int numRows = r2 - r1 + 1;
		int numCols = c2 - c1 + 1;
		if (r1 < 0 || r2 >= m.getNumRows() || c1 < 0 || c2 >= m.getNumCols() || r1 > r2 || c1 > c2) {
			throw new ArrayIndexOutOfBoundsException();
		}
		double[][] newMatrixData = new double[numRows][numCols];
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) {
				newMatrixData[i][j] = m.get(i + r1, j + c1);
			}
		}
		this.matrix = newMatrixData;
		this.rows = numRows;
		this.columns = numCols;
	}

	/**
	 * Overwrites this matrix to be a defined submatrix.
	 *
	 * @param m  - a matrix to retrieve a submatrix from
	 * @param r  - array of row indices
	 * @param c1 - initial column index
	 * @param c2 - final column index
	 * @throws ArrayIndexOutOfBoundsException if the submatrix bounds do not fit
	 *                                        inside the given matrix
	 */
	public void setMatrix(Matrix m, int r[], int c1, int c2) {
		int numRows = r.length;
		int numCols = c2 - c1 + 1;
		if (c1 < 0 || c2 >= m.getNumCols() || c1 > c2) {
			throw new ArrayIndexOutOfBoundsException();
		}
		double[][] newMatrixData = new double[numRows][numCols];
		for (int i = 0; i < numRows; i++) {
			if (r[i] < 0 || r[i] > m.getNumRows()) {
				throw new ArrayIndexOutOfBoundsException();
			}
			for (int j = 0; j < numCols; j++) {
				newMatrixData[i][j] = m.get(r[i], j + c1);
			}
		}
		this.matrix = newMatrixData;
		this.rows = numRows;
		this.columns = numCols;
	}

	/**
	 * Overwrites this matrix to be a defined submatrix.
	 *
	 * @param m  - a matrix to retrieve a submatrix from
	 * @param r1 - initial row index
	 * @param r2 - final row index
	 * @param c  - array of column indices
	 * @throws ArrayIndexOutOfBoundsException if the submatrix bounds do not fit
	 *                                        inside the given matrix
	 */
	public void setMatrix(Matrix m, int r1, int r2, int c[]) {
		int numRows = r2 - r1 + 1;
		int numCols = c.length;
		if (r1 < 0 || r2 >= m.getNumRows() || r1 > r2) {
			throw new ArrayIndexOutOfBoundsException();
		}
		double[][] newMatrixData = new double[numRows][numCols];
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) {
				if (c[j] < 0 || c[j] > m.getNumCols()) {
					throw new ArrayIndexOutOfBoundsException();
				}
				newMatrixData[i][j] = m.get(i + r1, c[j]);
			}
		}
		this.matrix = newMatrixData;
		this.rows = numRows;
		this.columns = numCols;
	}

	/**
	 * Overwrites this matrix to be a defined submatrix.
	 *
	 * @param m - a matrix to retrieve a submatrix from
	 * @param r - array of row indices
	 * @param c - array of column indices
	 * @throws ArrayIndexOutOfBoundsException if the submatrix bounds do not fit
	 *                                        inside the given matrix
	 */
	public void setMatrix(Matrix m, int r[], int c[]) {
		int numRows = r.length;
		int numCols = c.length;
		double[][] newMatrixData = new double[numRows][numCols];
		for (int i = 0; i < numRows; i++) {
			if (r[i] < 0 || r[i] > m.getNumRows()) {
				throw new ArrayIndexOutOfBoundsException();
			}
			for (int j = 0; j < numCols; j++) {
				if (c[j] < 0 || c[j] > m.getNumCols()) {
					throw new ArrayIndexOutOfBoundsException();
				}
				newMatrixData[i][j] = m.get(r[i], c[j]);
			}
		}
		this.matrix = newMatrixData;
		this.rows = numRows;
		this.columns = numCols;
	}

	/**
	 * Return the product matrix of two matrices using the dot product. This
	 * operation alters this matrix to be the product matrix and leaves the given
	 * one unchanged.
	 *
	 * @param m - the matrix by which to multiply with this matrix
	 * @return this matrix, which is altered to be the product matrix of this and a
	 *         given matrix
	 * @throws IllegalArgumentException if the matrices do not have dimensions which
	 *                                  allow them to be multiplied
	 */
	public Matrix times(Matrix m) {
		if (m != null && this.columns == m.getNumRows()) {
			double[][] result = new double[this.rows][m.getNumCols()];
			for (int rowA = 0; rowA < this.rows; rowA++) {
				for (int colB = 0; colB < m.getNumCols(); colB++) {
					// Dot product
					for (int rowB = 0; rowB < m.getNumRows(); rowB++) {
						result[rowA][colB] += (this.matrix[rowA][rowB] * m.matrix[rowB][colB]);
					}
				}
			}
			// This matrix is altered
			this.columns = m.getNumCols();
			this.matrix = result;
			return this;
		} else {
			throw new IllegalArgumentException("Matrix multiplication not possible between given matricies.");
		}
	}

	/**
	 * Returns this matrix multiplied by a given scalar.
	 *
	 * @param s - a scalar value by which to multiply this matrix
	 * @return this matrix multiplied by the given scalar
	 */
	public Matrix times(double s) {
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.columns; j++) {
				this.matrix[i][j] = this.matrix[i][j] * s;
			}
		}
		return this;
	}

	/**
	 * Returns this matrix divided a given scalar.
	 *
	 * @param s - the scalar value by which to divide this matrix
	 * @return this matrix divided by the given scalar
	 */
	public Matrix divide(double s) {
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.columns; j++) {
				this.matrix[i][j] = this.matrix[i][j] / s;
			}
		}
		return this;
	}

	/**
	 * Returns this matrix plus a given scalar.
	 *
	 * @param s - the scalar value by which to add to this matrix
	 * @return this matrix added to by the given scalar
	 */
	public Matrix plus(double s) {
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.columns; j++) {
				this.matrix[i][j] = this.matrix[i][j] + s;
			}
		}
		return this;
	}

	/**
	 * Returns this matrix minus a given scalar.
	 *
	 * @param s - the scalar value by which to subtract this matrix
	 * @return this matrix subtracted from by the given scalar
	 */
	public Matrix minus(double s) {
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.columns; j++) {
				this.matrix[i][j] = this.matrix[i][j] - s;
			}
		}
		return this;
	}

}
