package com.game.engine.maths;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Random;

/**
 * A matrix containing float data
 *
 * @author Spencer Imbleau
 * @version June 2020
 */
public class Matrix implements Cloneable {

	/**
	 * The rows in the matrix
	 */
	protected int rows;

	/**
	 * The columns in the matrix
	 */
	protected int columns;

	/**
	 * The matrix data
	 */
	protected double[][] matrix;

	/**
	 * Construct an m-by-n matrix of zeros.
	 *
	 * @param rows    - Number of rows.
	 * @param columns - Number of colums.
	 */
	public Matrix(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		this.matrix = new double[rows][columns];
	}

	/**
	 * Construct an m-by-n constant matrix.
	 *
	 * @param rows    - Number of rows.
	 * @param columns - Number of colums.
	 * @param s       - Fill the matrix with this scalar value.
	 */
	public Matrix(int rows, int columns, double s) {
		this(rows, columns);
		Arrays.stream(this.matrix).forEach(a -> Arrays.fill(a, s));
	}

	/**
	 * Construct a matrix from a 2-D array.
	 *
	 * @param matrix - Two-dimensional array of doubles.
	 * @throws IllegalArgumentException All rows must have the same length
	 */
	public Matrix(double[][] matrix) throws IllegalArgumentException {
		if (matrix == null) {
			throw new IllegalArgumentException("Matrix data cannot be null.");
		}

		// Check all submatrices have the same length
		int cols = (matrix.length > 0) ? matrix[0].length : 0;
		for (double[] subMat : matrix) {
			if (subMat.length != cols) {
				throw new IllegalArgumentException("Matrix data must have consistent length for all " + "submatrices.");
			}
		}

		this.rows = matrix.length;
		this.columns = cols;
		this.matrix = matrix;
	}

	/**
	 * Construct a matrix quickly without checking arguments.
	 *
	 * @param matrix  - Two-dimensional array of doubles.
	 * @param rows    - Number of rows.
	 * @param columns - Number of colums.
	 */
	public Matrix(double[][] matrix, int rows, int columns) {
		this.matrix = matrix;
		this.rows = rows;
		this.columns = columns;
	}

	/**
	 * Overrides the equals method for matrices. For two matrices to be equal they
	 * must have the same number of rows and columns with the same entries in every
	 * cell.
	 * 
	 * @param m The matrix to be tested against.
	 * @return true if they are equal, false otherwise.
	 */
	@Override
	public boolean equals(Object m) {
		if (m instanceof Matrix) {
			Matrix m1 = (Matrix) m;
			if (m1.rows != this.rows) {
				return false;
			}
			if (m1.columns != this.columns) {
				return false;
			}
			return Arrays.deepEquals(m1.matrix, this.matrix);
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		NumberFormat formatter = new DecimalFormat("000.0");
		StringBuilder sb = new StringBuilder();
		sb.append('[');
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
		sb.append(']');
		return sb.toString();
	}

	@Override
	public Matrix clone() {
		double[][] copyMatrix = null;
		if (this.getArray() != null) {
			copyMatrix = Arrays.stream(this.matrix).map(double[]::clone).toArray(double[][]::new);
		}
		return new Matrix(copyMatrix, this.rows, this.columns);
	}

	/**
	 * Access the internal two-dimensional array.
	 *
	 * @return Pointer to the two-dimensional array of matrix elements.
	 */
	public double[][] getArray() {
		return this.matrix;
	}

	/**
	 * Access the number of rows in the matrix
	 * 
	 * @return The number of rows in the matrix.
	 */
	public int getNumRows() {
		return this.rows;
	}

	/**
	 * Access the number of columns in the matrix
	 * 
	 * @return The number of columns in the matrix.
	 */
	public int getNumCols() {
		return this.columns;
	}

	/**
	 * Retrieves an element in the matrix.
	 * 
	 * @param i The row from which to retrieve the element.
	 * @param j The column from which to retrieve the element.
	 * @return the element at matrix[i]jl]
	 * @throws ArrayIndexOutOfBoundsException If the row or column specified is not
	 *                                        within the bounds of the matrix
	 */
	public double get(int i, int j) {
		if (i < 0 || i >= this.rows || j < 0 || j >= this.columns) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return this.matrix[i][j];
	}

	/**
	 * Gets the submatrix specified by the row and column indices given.
	 * 
	 * @param i0 Initial row index
	 * @param i1 Final row index
	 * @param j0 Initial column index
	 * @param j1 Final column index
	 * @return A(i0:i1, j0:j1)
	 * @throws ArrayIndexOutOfBoundsException If the submatrix bounds do not fit
	 *                                        inside this matrix.
	 */
	public Matrix getMatrix(int i0, int i1, int j0, int j1) {
		int numRows = i1 - i0 + 1;
		int numCols = j1 - j0 + 1;
		if (i0 < 0 || i1 >= this.rows || j0 < 0 || j1 >= this.columns || i0 > i1 || j0 > j1) {
			throw new ArrayIndexOutOfBoundsException();
		}
		double[][] result = new double[numRows][numCols];
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) {
				result[i][j] = this.matrix[i + i0][j + j0];
			}
		}
		return new Matrix(result);
	}

	/**
	 * Gets the submatrix specified by the row and column indices given.
	 * 
	 * @param r  Array of row indices
	 * @param j0 Initial column index
	 * @param j1 Final column index
	 * @return A(r(:), j0:j1)
	 * @throws ArrayIndexOutOfBoundsException If the submatrix bounds do not fit
	 *                                        inside this matrix.
	 */
	public Matrix getMatrix(int[] r, int j0, int j1) {
		int numRows = r.length;
		int numCols = j1 - j0 + 1;
		if (j0 < 0 || j1 >= this.columns || j0 > j1) {
			throw new ArrayIndexOutOfBoundsException();
		}
		double[][] result = new double[numRows][numCols];
		for (int i = 0; i < numRows; i++) {
			if (r[i] < 0 || r[i] > this.rows) {
				throw new ArrayIndexOutOfBoundsException();
			}
			for (int j = 0; j < numCols; j++) {
				result[i][j] = this.matrix[r[i]][j + j0];
			}
		}
		return new Matrix(result);
	}

	/**
	 * Gets the submatrix specified by the row and column indices given.
	 * 
	 * @param i0 Initial row index
	 * @param i1 Final row index
	 * @param c  Array of column indices
	 * @return A(i0:i1, c(:))
	 * @throws ArrayIndexOutOfBoundsException If the submatrix bounds do not fit
	 *                                        inside this matrix.
	 */
	public Matrix getMatrix(int i0, int i1, int[] c) {
		int numRows = i1 - i0 + 1;
		int numCols = c.length;
		if (i0 < 0 || i1 >= this.columns || i0 > i1) {
			throw new ArrayIndexOutOfBoundsException();
		}
		double[][] result = new double[numRows][numCols];
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) {
				if (c[j] < 0 || c[j] > this.columns) {
					throw new ArrayIndexOutOfBoundsException();
				}
				result[i][j] = this.matrix[i + i0][c[j]];
			}
		}
		return new Matrix(result);
	}

	/**
	 * Gets the submatrix specified by the row and column indices given.
	 * 
	 * @param r Array of row indices
	 * @param c Array of column indices
	 * @return A(i0:i1, c(:))
	 * @throws ArrayIndexOutOfBoundsException If the submatrix bounds do not fit
	 *                                        inside this matrix.
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
	 * Setter method for a single element.
	 * 
	 * @param i Row index
	 * @param j Column index
	 * @param s The value to set at A(i,j)
	 * @throws ArrayIndexOutOfBoundsException If the submatrix bounds do not fit
	 *                                        inside this matrix.
	 */
	public void set(int i, int j, double s) {
		if (i < 0 || i >= this.rows || j < 0 || j >= this.columns) {
			throw new ArrayIndexOutOfBoundsException();
		}
		this.matrix[i][j] = s;
	}

	/**
	 * Sets this matrix to the submatrix of X,
	 * 
	 * @param i0 Initial row index
	 * @param i1 Final row index
	 * @param j0 Initial column index
	 * @param j1 Final column index
	 * @param X  A(i0:i1, j0:j1)
	 * @throws ArrayIndexOutOfBoundsException If the submatrix bounds do not fit
	 *                                        inside the matrix.
	 */
	public void setMatrix(int i0, int i1, int j0, int j1, Matrix X) {
		int numRows = i1 - i0 + 1;
		int numCols = j1 - j0 + 1;
		if (i0 < 0 || i1 >= X.getNumRows() || j0 < 0 || j1 >= X.getNumCols() || i0 > i1 || j0 > j1) {
			throw new ArrayIndexOutOfBoundsException();
		}
		double[][] newMatrixData = new double[numRows][numCols];
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) {
				newMatrixData[i][j] = X.get(i + i0, j + j0);
			}
		}
		this.matrix = newMatrixData;
		this.rows = numRows;
		this.columns = numCols;
	}

	/**
	 * Sets this matrix to the submatrix of X,
	 * 
	 * @param r  Array of row indices
	 * @param j0 Initial column index
	 * @param j1 Final column index
	 * @param X  A(r(:), j0:j1)
	 * @throws ArrayIndexOutOfBoundsException If the submatrix bounds do not fit
	 *                                        inside the matrix.
	 */
	public void setMatrix(int r[], int j0, int j1, Matrix X) {
		int numRows = r.length;
		int numCols = j1 - j0 + 1;
		if (j0 < 0 || j1 >= X.getNumCols() || j0 > j1) {
			throw new ArrayIndexOutOfBoundsException();
		}
		double[][] newMatrixData = new double[numRows][numCols];
		for (int i = 0; i < numRows; i++) {
			if (r[i] < 0 || r[i] > X.getNumRows()) {
				throw new ArrayIndexOutOfBoundsException();
			}
			for (int j = 0; j < numCols; j++) {
				newMatrixData[i][j] = X.get(r[i], j + j0);
			}
		}
		this.matrix = newMatrixData;
		this.rows = numRows;
		this.columns = numCols;
	}

	/**
	 * Sets this matrix to the submatrix of X,
	 * 
	 * @param i0 Initial row index
	 * @param i1 Final row index
	 * @param c  Array of column indices
	 * @param X  A(i0:i1, c(:))
	 * @throws ArrayIndexOutOfBoundsException If the submatrix bounds do not fit
	 *                                        inside the matrix.
	 */
	public void setMatrix(int i0, int i1, int c[], Matrix X) {
		int numRows = i1 - i0 + 1;
		int numCols = c.length;
		if (i0 < 0 || i1 >= X.getNumRows() || i0 > i1) {
			throw new ArrayIndexOutOfBoundsException();
		}
		double[][] newMatrixData = new double[numRows][numCols];
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) {
				if (c[j] < 0 || c[j] > X.getNumCols()) {
					throw new ArrayIndexOutOfBoundsException();
				}
				newMatrixData[i][j] = X.get(i + i0, c[j]);
			}
		}
		this.matrix = newMatrixData;
		this.rows = numRows;
		this.columns = numCols;
	}

	/**
	 * Sets this matrix to the submatrix of X,
	 * 
	 * @param r Array of row indices
	 * @param c Array of column indices
	 * @param X A(r(:), c(:))
	 * @throws ArrayIndexOutOfBoundsException If the submatrix bounds do not fit
	 *                                        inside the matrix.
	 */
	public void setMatrix(int r[], int c[], Matrix X) {
		int numRows = r.length;
		int numCols = c.length;
		double[][] newMatrixData = new double[numRows][numCols];
		for (int i = 0; i < numRows; i++) {
			if (r[i] < 0 || r[i] > X.getNumRows()) {
				throw new ArrayIndexOutOfBoundsException();
			}
			for (int j = 0; j < numCols; j++) {
				if (c[j] < 0 || c[j] > X.getNumCols()) {
					throw new ArrayIndexOutOfBoundsException();
				}
				newMatrixData[i][j] = X.get(r[i], c[j]);
			}
		}
		this.matrix = newMatrixData;
		this.rows = numRows;
		this.columns = numCols;
	}

	/**
	 * Creates an m x n matrix containing random values between 0 and 1.
	 * 
	 * @param m The number of rows for the matrix
	 * @param n The number of columns for the matrix
	 * @return The m x n matrix containing random values
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
	 * Generate an identity matrix.
	 * 
	 * @param m The number of rows for the matrix
	 * @param n The number of columns for the matrix
	 * @throws IllegalArgumentException Array length must be a multiple of rows
	 * @return The m x n identity matrix
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
	 * Multiple two matrices together using the dot product.
	 * 
	 * @param x The matrix to multiply with this matrix.
	 * @return The resulting matrix.
	 * @throws UnsupportedOperationException If the matrices do not have dimensions
	 *                                       which allow them to be multiplied.
	 */
	public Matrix times(Matrix x) {
		double[][] a = x.getArray();
		if (x != null && this.columns == x.getNumRows()) {
			double[][] result = new double[this.rows][x.getNumCols()];
			for (int rowA = 0; rowA < this.rows; rowA++) {
				for (int colB = 0; colB < x.getNumCols(); colB++) {
					// Dot product
					for (int rowB = 0; rowB < x.getNumRows(); rowB++) {
						result[rowA][colB] += (this.matrix[rowA][rowB] * a[rowB][colB]);
					}
				}
			}
			return new Matrix(result);
		} else {
			throw new UnsupportedOperationException("Matrix multiplication not possible between given matricies.");
		}
	}

	/**
	 * Alter this matrix by multiplying it with another using the dot product.
	 * 
	 * @param x The matrix to multiply with this matrix.
	 * @return This matrix.
	 * @throws UnsupportedOperationException If the matrices do not have dimensions
	 *                                       which allow them to be multiplied.
	 */
	public Matrix timesEquals(Matrix x) {
		double[][] a = x.getArray();
		if (x != null && this.columns == x.getNumRows()) {
			double[][] result = new double[this.rows][x.getNumCols()];
			for (int rowA = 0; rowA < this.rows; rowA++) {
				for (int colB = 0; colB < x.getNumCols(); colB++) {
					// Dot product
					for (int rowB = 0; rowB < x.getNumRows(); rowB++) {
						result[rowA][colB] += (this.matrix[rowA][rowB] * a[rowB][colB]);
					}
				}
			}
			// This matrix is altered
			this.columns = x.getNumCols();
			this.matrix = result;
			return this;
		} else {
			throw new UnsupportedOperationException("Matrix multiplication not possible between given matricies.");
		}
	}

	/**
	 * Multiplies this matrix by a scalar.
	 * 
	 * @param s The number by which to multiply the matrix.
	 * @return This matrix multiplied by the given scalar
	 */
	public Matrix timesEquals(double s) {
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.columns; j++) {
				this.matrix[i][j] = this.matrix[i][j] * s;
			}
		}
		return this;
	}

	/**
	 * Multiplies a matrix by a constant scalar.
	 * 
	 * @param s The number by which to multiply the matrix.
	 * @return A new matrix equaling this matrix multiplied by the given scalar.
	 */
	public Matrix times(double s) {
		double[][] result = new double[this.rows][this.columns];
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.columns; j++) {
				result[i][j] = this.matrix[i][j] * s;
			}
		}
		return new Matrix(result);
	}

	/**
	 * Divide this matrix by a scalar.
	 * 
	 * @param s The number by which to divide the matrix.
	 * @return This matrix divided by the given scalar
	 */
	public Matrix divideEquals(double s) {
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.columns; j++) {
				this.matrix[i][j] = this.matrix[i][j] / s;
			}
		}
		return this;
	}

	/**
	 * Divide a matrix by a constant scalar.
	 * 
	 * @param s The number by which to divide the matrix.
	 * @return A new matrix equaling this matrix divided by the given scalar.
	 */
	public Matrix divide(double s) {
		double[][] result = new double[this.rows][this.columns];
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.columns; j++) {
				result[i][j] = this.matrix[i][j] / s;
			}
		}
		return new Matrix(result);
	}

	/**
	 * Adds to this matrix by a constant.
	 * 
	 * @param s - The number by which to add to this matrix.
	 * @return This matrix added by a given constant.
	 */
	public Matrix plusEquals(double s) {
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.columns; j++) {
				this.matrix[i][j] = this.matrix[i][j] + s;
			}
		}
		return this;
	}

	/**
	 * Adds to a matrix by a constant.
	 * 
	 * @param s - The number by which to add to the matrix.
	 * @return A new matrix equaling this matrix plus a given constant.
	 */
	public Matrix plus(double s) {
		double[][] result = new double[this.rows][this.columns];
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.columns; j++) {
				result[i][j] = this.matrix[i][j] + s;
			}
		}
		return new Matrix(result);
	}

	/**
	 * Subtracts this matrix by a constant.
	 * 
	 * @param s - The number by which to subtract this matrix.
	 * @return This matrix subtracted by a given constant.
	 */
	public Matrix minusEquals(double s) {
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.columns; j++) {
				this.matrix[i][j] = this.matrix[i][j] - s;
			}
		}
		return this;
	}

	/**
	 * Subtracts a matrix by a constant.
	 * 
	 * @param s - The number by which to subtract to the matrix.
	 * @return A new matrix equaling this matrix subtracted by a given constant.
	 */
	public Matrix minus(double s) {
		double[][] result = new double[this.rows][this.columns];
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.columns; j++) {
				result[i][j] = this.matrix[i][j] - s;
			}
		}
		return new Matrix(result);
	}

}
