package com.game.engine.coordinates;

import java.util.Arrays;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import com.game.engine.maths.Matrix;

/**
 * Tests a {@link Matrix}
 * 
 * @author Spencer Imbleau
 * @version October 2020
 */
public class TestMatrix {

	/**
	 * An arbitrary amount of rows for testing certain features.
	 */
	private static int TEST_ROWS = 5;

	/**
	 * An arbitrary amount of columns for testing certain features.
	 */
	private static int TEST_COLUMNS = 5;

	/**
	 * Required accuracy for testing equivalence in float values.
	 */
	private static double TEST_DELTA = 0.05d;

	/**
	 * The default 2D test data.
	 */
	public static double[][] TEST_DATA_2D;

	/**
	 * Initialize {@link #TEST_DATA_2D} with range 1...n in lexicographic order,
	 * where n = {@link #TEST_ROWS} * {@link #TEST_COLUMNS}
	 */
	static {
		// Populate test data
		TEST_DATA_2D = new double[TEST_ROWS][TEST_COLUMNS];
		for (int row = 0; row < TEST_ROWS; row++) {
			for (int col = 0; col < TEST_COLUMNS; col++) {
				TEST_DATA_2D[row][col] = row * TEST_COLUMNS + col;
			}
		}
	}

	/**
	 * Test {@link Matrix#Matrix(int rows, int columns)}.
	 */
	@Test
	public void testConstructor1() {
		// Test for empty matrix
		Matrix m = new Matrix(0, 0);
		Assert.assertArrayEquals(new double[0][0], m.getArray());
		Assert.assertEquals(0, m.getNumRows());
		Assert.assertEquals(0, m.getNumCols());

		// Test for test matrix
		m = new Matrix(TEST_ROWS, TEST_COLUMNS);
		Assert.assertArrayEquals(new double[TEST_ROWS][TEST_COLUMNS], m.getArray());
		Assert.assertEquals(TEST_ROWS, m.getNumRows());
		Assert.assertEquals(TEST_COLUMNS, m.getNumCols());
	}

	/**
	 * Test {@link Matrix#Matrix(int rows, int columns, double s)}.
	 */
	@Test
	public void testConstructor2() {
		// Test for empty matrix
		Matrix m = new Matrix(0, 0, 0d);
		Assert.assertArrayEquals(new double[0][0], m.getArray());
		Assert.assertEquals(0, m.getNumRows());
		Assert.assertEquals(0, m.getNumCols());

		// Test for test matrix
		m = new Matrix(TEST_ROWS, TEST_COLUMNS, -1d);
		double[][] expected = new double[TEST_ROWS][TEST_COLUMNS];
		Arrays.stream(expected).forEach(a -> Arrays.fill(a, -1d));
		Assert.assertArrayEquals(expected, m.getArray());
		Assert.assertEquals(TEST_ROWS, m.getNumRows());
		Assert.assertEquals(TEST_COLUMNS, m.getNumCols());
	}

	/**
	 * Test {@link Matrix#Matrix(double[][] matrix)}.
	 */
	@Test
	public void testConstructor3() {
		// Test for error when null data is passed
		Assert.assertThrows(IllegalArgumentException.class, () -> new Matrix(null));

		// Test for error when bad data is passed
		double[][] badData = { { -1, -1 }, { -1 } };
		Assert.assertThrows(IllegalArgumentException.class, () -> new Matrix(badData));

		// Test for empty matrix
		Matrix m = new Matrix(new double[0][0]);
		Assert.assertArrayEquals(new double[0][0], m.getArray());
		Assert.assertEquals(0, m.getNumRows());
		Assert.assertEquals(0, m.getNumCols());

		// Test for full matrix
		m = new Matrix(TEST_DATA_2D);
		for (int ri = 0; ri < m.getArray().length; ri++) {
			double[] row = m.getArray()[ri];
			for (int ci = 0; ci < row.length; ci++) {
				double expected = TEST_DATA_2D[ri][ci];
				double actual = row[ci];
				Assert.assertEquals(expected, actual, TEST_DELTA);
			}
		}
		Assert.assertEquals(TEST_ROWS, m.getNumRows());
		Assert.assertEquals(TEST_COLUMNS, m.getNumCols());
	}

	/**
	 * Test {@link Matrix#Matrix(double[][] matrix)}.
	 */
	@Test
	public void testConstructor4() {
		// Test good data
		Matrix m = new Matrix(TEST_DATA_2D);
		Assert.assertArrayEquals(TEST_DATA_2D, m.getArray());
		Assert.assertEquals(TEST_ROWS, m.getNumRows());
		Assert.assertEquals(TEST_COLUMNS, m.getNumCols());
	}

	/**
	 * Test {@link Matrix#identity(int, int)}.
	 */
	@Test
	public void testIdentityMatrix() {
		// Test for error for illegal arguments
		Assert.assertThrows(IllegalArgumentException.class, () -> Matrix.identity(0, -1));
		Assert.assertThrows(IllegalArgumentException.class, () -> Matrix.identity(-1, 0));

		Matrix m = new Matrix(TEST_DATA_2D);
		Matrix identity = Matrix.identity(TEST_COLUMNS, TEST_COLUMNS);
		try {
			Matrix result = m.clone().times(identity);
			// Any matrix times an identity matrix should result in the original matrix
			Assert.assertTrue(m.equals(result));
		} catch (UnsupportedOperationException e) {
			Assert.fail("The identity matrix of any matrix should be multipliable.");
		}
	}

	/**
	 * Test {@link Matrix#random(int, int)}.
	 */
	@Test
	public void testRandom() {
		Matrix m = Matrix.random(TEST_ROWS, TEST_COLUMNS);

		// Test construction
		Assert.assertEquals(TEST_ROWS, m.getNumRows());
		Assert.assertEquals(TEST_COLUMNS, m.getNumCols());

		// Test randomness. This may fail due to luck.
		for (int ri = 0; ri < m.getNumRows(); ri++) {
			for (int ci = 1; ci < m.getNumCols(); ci++) {
				Assert.assertFalse(m.get(ri, ci) == m.get(ri, ci - 1));
			}
		}
	}

	/**
	 * Test {@link Matrix#equals(Object)}.
	 */
	@Test
	public void testEquals() {
		// Initialize values
		Matrix m1 = new Matrix(TEST_DATA_2D);
		Matrix m2 = m1.clone();
		Matrix m3 = new Matrix(m1.clone().times(-1d).getArray());

		// Test equals
		Assert.assertTrue(m1.equals(m2));
		Assert.assertFalse(m1.equals(m3));
	}

	/**
	 * Test {@link Matrix#equals(Object)}.
	 */
	@Test
	public void testClone() {
		Matrix m = new Matrix(TEST_DATA_2D);
		Assert.assertEquals(m, m.clone());
	}

	/**
	 * Test {@link Matrix#get(int, int)}.
	 */
	@Test
	public void testGet() {
		// Initialize values
		Matrix m = new Matrix(TEST_DATA_2D);

		// Test for error when out of bounds
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(-1, 0));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(0, -1));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(TEST_ROWS, 0));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.get(0, TEST_COLUMNS));

		// Test get
		for (int ri = 0; ri < m.getNumRows(); ri++) {
			for (int ci = 0; ci < m.getNumCols(); ci++) {
				Assert.assertTrue(TEST_DATA_2D[ri][ci] == m.get(ri, ci));
			}
		}
	}

	/**
	 * Test {@link Matrix#getNumRows()}.
	 */
	@Test
	public void testGetNumRows() {
		Matrix m = new Matrix(TEST_DATA_2D);
		Assert.assertEquals(TEST_ROWS, m.getNumRows());
	}

	/**
	 * Test {@link Matrix#getNumCols()}.
	 */
	@Test
	public void testGetNumCols() {
		Matrix m = new Matrix(TEST_DATA_2D);
		Assert.assertEquals(TEST_COLUMNS, m.getNumCols());
	}

	/**
	 * Test {@link Matrix#getArray()}.
	 */
	@Test
	public void testGetArray() {
		Matrix m = new Matrix(TEST_DATA_2D);
		Assert.assertArrayEquals(TEST_DATA_2D, m.getArray());
	}

	/**
	 * Test {@link Matrix#getMatrix(int, int, int, int)}
	 */
	@Test
	public void testGetMatrix1() {
		// Initialize values
		double[][] a0 = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
		Matrix m0 = new Matrix(a0);

		// Test for error with too small indices
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> m0.getMatrix(-1, 0, 0, 0));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> m0.getMatrix(0, 0, -1, 0));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> m0.getMatrix(0, -1, 0, 0));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> m0.getMatrix(0, 0, 0, -1));

		// Test for error with too large indices
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> m0.getMatrix(3, 0, 0, 0));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> m0.getMatrix(0, 0, 3, 0));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> m0.getMatrix(0, 3, 0, 0));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> m0.getMatrix(0, 0, 0, 3));

		// Test for error with starting larger than ending
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> m0.getMatrix(2, 0, 0, 1));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> m0.getMatrix(0, 2, 1, 0));

		// Test slice 1
		double[][] a1 = { { 1, 2 }, { 4, 5 } };
		Matrix m1 = new Matrix(a1);
		Assert.assertEquals(m1, m0.getMatrix(0, 0, 1, 1));

		// Test slice 2
		double[][] a2 = { { 5, 6 }, { 8, 9 } };
		Matrix m2 = new Matrix(a2);
		Assert.assertEquals(m2, m0.getMatrix(1, 1, 2, 2));

		// Test slice 3
		double[][] a3 = { { 1 } };
		Matrix m3 = new Matrix(a3);
		Assert.assertEquals(m3, m0.getMatrix(0, 0, 0, 0));

	}

	/**
	 * Test {@link Matrix#getMatrix(int[], int, int)}
	 */
	@Test
	public void testGetMatrix2() {
		// Initialize values
		double[][] a0 = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
		Matrix m0 = new Matrix(a0);
		int[] acceptableR = { 0, 1, 2 };
		int[] lowR = { -1 };
		int[] highR = { 3 };

		// Test for error with too small indices
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> m0.getMatrix(lowR, 0, 0));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> m0.getMatrix(acceptableR, -1, 0));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> m0.getMatrix(acceptableR, 0, -1));

		// Test for error with too large indices
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> m0.getMatrix(highR, 0, 0));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> m0.getMatrix(acceptableR, 3, 0));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> m0.getMatrix(acceptableR, 0, 3));

		// Test for error with starting larger than ending
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> m0.getMatrix(acceptableR, 2, 0));

		int[] r1 = { 0, 1 };
		double[][] a1 = { { 1, 2 }, { 4, 5 } };
		Matrix m1 = new Matrix(a1);
		Assert.assertEquals(m1, m0.getMatrix(r1, 0, 1));

		int[] r2 = { 2, 0 };
		double[][] a2 = { { 8, 9 }, { 2, 3 } };
		Matrix m2 = new Matrix(a2);
		Assert.assertEquals(m2, m0.getMatrix(r2, 1, 2));

		int[] r3 = { 0 };
		double[][] a3 = { { 1 } };
		Matrix m3 = new Matrix(a3);
		Assert.assertEquals(m3, m0.getMatrix(r3, 0, 0));

		// Weird
		int[] r4 = { 0, 0, 0, 0 };
		double[][] a4 = { { 1 }, { 1 }, { 1 }, { 1 } };
		Matrix m4 = new Matrix(a4);
		Assert.assertEquals(m4, m0.getMatrix(r4, 0, 0));
	}

	/**
	 * Test {@link Matrix#getMatrix(int, int, int[])}
	 */
	@Test
	public void testGetMatrix3() {
		// Initialize values
		double[][] a0 = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
		Matrix m0 = new Matrix(a0);
		int[] acceptableC = { 0, 1, 2 };
		int[] lowC = { 1, 0, -1 };
		int[] highC = { 1, 3, 0 };

		// Test for error with too small indices
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> m0.getMatrix(0, 0, lowC));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> m0.getMatrix(-1, 0, acceptableC));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> m0.getMatrix(0, -1, acceptableC));

		// Test for error with too large indices
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> m0.getMatrix(0, 0, highC));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> m0.getMatrix(3, 0, acceptableC));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> m0.getMatrix(0, 3, acceptableC));

		// Test for error with starting larger than ending
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> m0.getMatrix(2, 0, acceptableC));

		int[] c1 = { 0, 1 };
		double[][] a1 = { { 1, 2 }, { 4, 5 } };
		Matrix m1 = new Matrix(a1);
		Assert.assertEquals(m1, m0.getMatrix(0, 1, c1));

		int[] c2 = { 2, 0 };
		double[][] a2 = { { 6, 4 }, { 9, 7 } };
		Matrix m2 = new Matrix(a2);
		Assert.assertEquals(m2, m0.getMatrix(1, 2, c2));

		int[] c3 = { 0 };
		double[][] a3 = { { 1 } };
		Matrix m3 = new Matrix(a3);
		Assert.assertEquals(m3, m0.getMatrix(0, 0, c3));

		// Weird
		int[] c4 = { 0, 0, 0, 0 };
		double[][] a4 = { { 1, 1, 1, 1 } };
		Matrix m4 = new Matrix(a4);
		Assert.assertEquals(m4, m0.getMatrix(0, 0, c4));
	}

	/**
	 * Test {@link Matrix#getMatrix(int[], int[])}
	 */
	@Test
	public void testGetMatrix4() {
		// Initialize values
		double[][] a0 = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
		Matrix m0 = new Matrix(a0);
		int[] acceptableR = { 0, 1, 2 };
		int[] acceptableC = { 0, 1, 2 };
		int[] low = { -1 };
		int[] highR = { 3 };
		int[] highC = { 3 };

		// Test for error with too small indices
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> m0.getMatrix(acceptableR, low));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> m0.getMatrix(low, acceptableC));

		// Test for error with too large indices
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> m0.getMatrix(acceptableR, highC));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> m0.getMatrix(highR, acceptableC));

		int[] r1 = { 0, 1 };
		int[] c1 = { 0, 1 };
		double[][] a1 = { { 1, 2 }, { 4, 5 } };
		Matrix m1 = new Matrix(a1);
		Assert.assertEquals(m1, m0.getMatrix(r1, c1));

		int[] r2 = { 1, 2 };
		int[] c2 = { 2, 0 };
		double[][] a2 = { { 6, 4 }, { 9, 7 } };
		Matrix m2 = new Matrix(a2);
		Assert.assertEquals(m2, m0.getMatrix(r2, c2));

		int[] r3 = { 0 };
		int[] c3 = { 0 };
		double[][] a3 = { { 1 } };
		Matrix m3 = new Matrix(a3);
		Assert.assertEquals(m3, m0.getMatrix(r3, c3));

		// Weird
		int[] r4 = { 2, 2 };
		int[] c4 = { 0, 0, 2, 1 };
		double[][] a4 = { { 7, 7, 9, 8 }, { 7, 7, 9, 8 } };
		Matrix m4 = new Matrix(a4);
		Assert.assertEquals(m4, m0.getMatrix(r4, c4));
	}

	/**
	 * Test {@link Matrix#set(int, int, double)}.
	 */
	@Test
	public void testSet() {
		// Initialize Values
		Random rand = new Random(System.nanoTime());
		Matrix m = new Matrix(TEST_DATA_2D);
		int row = rand.nextInt(m.getNumRows());
		int col = rand.nextInt(m.getNumCols());
		double num = rand.nextDouble();

		// Test for error on illegal bounds
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(-1, 0, 1));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(0, -1, 1));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(TEST_ROWS, 0, 1));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> m.set(0, TEST_COLUMNS, 1));

		// Test set
		m.set(row, col, num);
		Assert.assertTrue(m.get(row, col) == num);
	}

	/**
	 * Test {@link Matrix#setMatrix(Matrix, int, int, int, int)}.
	 */
	@Test
	public void testSetMatrix1() {
		// Initialize Values
		double[][] a0 = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
		Matrix m0 = new Matrix(a0);
		Matrix buf = new Matrix(0, 0);

		// Test for error on too small bounds
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> buf.setMatrix(m0, -1, 0, 0, 0));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> buf.setMatrix(m0, 0, 0, -1, 0));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> buf.setMatrix(m0, 0, -1, 0, 0));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> buf.setMatrix(m0, 0, 0, 0, -1));

		// Test for error on too large bounds
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> buf.setMatrix(m0, 3, 0, 0, 0));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> buf.setMatrix(m0, 0, 0, 3, 0));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> buf.setMatrix(m0, 0, 3, 0, 0));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> buf.setMatrix(m0, 0, 0, 0, 3));

		// Test for error on bounds starting larger than ending
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> buf.setMatrix(m0, 1, 0, 0, 0));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> buf.setMatrix(m0, 0, 1, 0, 0));

		// Test set
		double[][] a1 = { { 1, 2 }, { 4, 5 } };
		Matrix m1 = new Matrix(a1);
		buf.setMatrix(m0, 0, 0, 1, 1);
		Assert.assertTrue(m1.equals(buf));

		double[][] a2 = { { 5, 6 }, { 8, 9 } };
		Matrix m2 = new Matrix(a2);
		buf.setMatrix(m0, 1, 1, 2, 2);
		Assert.assertTrue(m2.equals(buf));

		double[][] a3 = { { 1 } };
		Matrix m3 = new Matrix(a3);
		buf.setMatrix(m0, 0, 0, 0, 0);
		Assert.assertTrue(m3.equals(buf));
	}

	/**
	 * Test {@link Matrix#setMatrix(Matrix, int[], int, int)}.
	 */
	@Test
	public void testSetMatrix2() {
		// Initialize Values
		double[][] a0 = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
		Matrix m0 = new Matrix(a0);
		Matrix buf = new Matrix(0, 0);
		int[] acceptableR = { 0, 1, 2 };
		int[] lowR = { -1 };
		int[] highR = { 3 };

		// Test for error on too small bounds
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> buf.setMatrix(m0, lowR, 0, 0));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> buf.setMatrix(m0, acceptableR, -1, 0));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> buf.setMatrix(m0, acceptableR, 0, -1));

		// Test for error on too large bounds
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> buf.setMatrix(m0, highR, 0, 0));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> buf.setMatrix(m0, acceptableR, 3, 0));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> buf.setMatrix(m0, acceptableR, 0, 3));

		// Test for error on bounds starting larger than ending
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> buf.setMatrix(m0, acceptableR, 1, 0));

		// Test set
		int[] r1 = { 0, 1 };
		double[][] a1 = { { 1, 2 }, { 4, 5 } };
		Matrix m1 = new Matrix(a1);
		buf.setMatrix(m0, r1, 0, 1);
		Assert.assertTrue(m1.equals(buf));

		int[] r2 = { 1, 2 };
		double[][] a2 = { { 5, 6 }, { 8, 9 } };
		Matrix m2 = new Matrix(a2);
		buf.setMatrix(m0, r2, 1, 2);
		Assert.assertTrue(m2.equals(buf));

		int[] r3 = { 0 };
		double[][] a3 = { { 1 } };
		Matrix m3 = new Matrix(a3);
		buf.setMatrix(m0, r3, 0, 0);
		Assert.assertTrue(m3.equals(buf));

		// Weird
		int[] r4 = { 0, 0, 0, 0 };
		double[][] a4 = { { 1 }, { 1 }, { 1 }, { 1 } };
		Matrix m4 = new Matrix(a4);
		buf.setMatrix(m0, r4, 0, 0);
		Assert.assertTrue(m4.equals(buf));
	}

	/**
	 * Test {@link Matrix#setMatrix(Matrix, int, int, int[])}.
	 */
	@Test
	public void testSetMatrix3() {
		// initialization of result doesn't matter since it is changed
		double[][] a0 = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
		Matrix m0 = new Matrix(a0);
		Matrix buf = new Matrix(0, 0);
		int[] acceptableC = { 0, 1, 2 };
		int[] lowC = { -1 };
		int[] highC = { 3 };

		// Test for error on too small bounds
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> buf.setMatrix(m0, 0, 0, lowC));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> buf.setMatrix(m0, -1, 0, acceptableC));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> buf.setMatrix(m0, 0, -1, acceptableC));

		// Test for error on too large bounds
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> buf.setMatrix(m0, 0, 0, highC));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> buf.setMatrix(m0, 3, 0, acceptableC));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> buf.setMatrix(m0, 0, 3, acceptableC));

		// Test for error on bounds starting larger than ending
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> buf.setMatrix(m0, 1, 0, acceptableC));

		// Test set
		int[] c1 = { 0, 1 };
		double[][] a1 = { { 1, 2 }, { 4, 5 } };
		Matrix m1 = new Matrix(a1);
		buf.setMatrix(m0, 0, 1, c1);
		Assert.assertTrue(m1.equals(buf));

		int[] c2 = { 1, 2 };
		double[][] a2 = { { 5, 6 }, { 8, 9 } };
		Matrix m2 = new Matrix(a2);
		buf.setMatrix(m0, 1, 2, c2);
		Assert.assertTrue(m2.equals(buf));

		int[] c3 = { 0 };
		double[][] a3 = { { 1 } };
		Matrix m3 = new Matrix(a3);
		buf.setMatrix(m0, 0, 0, c3);
		Assert.assertTrue(m3.equals(buf));

		int[] c4 = { 0, 0, 0, 0 };
		double[][] a4 = { { 1, 1, 1, 1 } };
		Matrix m4 = new Matrix(a4);
		buf.setMatrix(m0, 0, 0, c4);
		Assert.assertTrue(m4.equals(buf));
	}

	/**
	 * Test {@link Matrix#setMatrix(Matrix, int[], int[])}.
	 */
	@Test
	public void testSetMatrix4() {
		// initialization of result doesn't matter since it is changed
		double[][] a0 = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
		Matrix m0 = new Matrix(a0);
		Matrix buf = new Matrix(0, 0);
		int[] acceptableR = { 0, 1, 2 };
		int[] acceptableC = { 0, 1, 2 };
		int[] low = { -1 };
		int[] high = { 3 };

		// Test for error on too small bounds
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> buf.setMatrix(m0, low, acceptableC));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> buf.setMatrix(m0, acceptableR, low));

		// Test for error on too large bounds
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> buf.setMatrix(m0, high, acceptableC));
		Assert.assertThrows(ArrayIndexOutOfBoundsException.class, () -> buf.setMatrix(m0, acceptableR, high));

		// Test set
		int[] r1 = { 0, 1 };
		int[] c1 = { 0, 1 };
		double[][] a1 = { { 1, 2 }, { 4, 5 } };
		Matrix m1 = new Matrix(a1);
		buf.setMatrix(m0, r1, c1);
		Assert.assertTrue(m1.equals(buf));

		int[] r2 = { 1, 2 };
		int[] c2 = { 1, 0 };
		double[][] a2 = { { 5, 4 }, { 8, 7 } };
		Matrix m2 = new Matrix(a2);
		buf.setMatrix(m0, r2, c2);
		Assert.assertTrue(m2.equals(buf));

		int[] r3 = { 0 };
		int[] c3 = { 0 };
		double[][] a3 = { { 1 } };
		Matrix m3 = new Matrix(a3);
		buf.setMatrix(m0, r3, c3);
		Assert.assertTrue(m3.equals(buf));

		int[] r4 = { 0 };
		int[] c4 = { 0, 0, 0, 0 };
		double[][] a4 = { { 1, 1, 1, 1 } };
		Matrix m4 = new Matrix(a4);
		buf.setMatrix(m0, r4, c4);
		Assert.assertTrue(m4.equals(buf));
	}

	/**
	 * Test {@link Matrix#times(Matrix)} and {@link Matrix#times(Matrix)}.
	 */
	@Test
	public void testTimesMatrix() {
		// Initialize values
		double[][] a1 = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } };
		double[][] a2 = { { 10.0 }, { 20.0 }, { 30.0 } };
		double[][] a1a2 = { { (a1[0][0] * a2[0][0]) + (a1[0][1] * a2[1][0]) + (a1[0][2] * a2[2][0]) },
				{ (a1[1][0] * a2[0][0]) + (a1[1][1] * a2[1][0]) + (a1[1][2] * a2[2][0]) },
				{ (a1[2][0] * a2[0][0]) + (a1[2][1] * a2[1][0]) + (a1[2][2] * a2[2][0]) } };
		double[][] a3 = { { 1.0 }, { 2.0 } };
		Matrix m1 = new Matrix(a1);
		Matrix m2 = new Matrix(a2);
		Matrix m1m2 = new Matrix(a1a2);
		Matrix m3 = new Matrix(a3);

		// Test for error when dimensions are invalid
		Assert.assertThrows(UnsupportedOperationException.class, () -> m3.times(m1));

		// Test for correct multiplication
		Assert.assertEquals(m1m2, m1.times(m2));
	}

	/**
	 * Test {@link Matrix#times(double)} and {@link Matrix#times(double)}.
	 */
	@Test
	public void testTimesScalar() {
		// Initialize values
		double[][] a1 = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } };
		double s = 2;
		double[][] a1s = { { a1[0][0] * s, a1[0][1] * s, a1[0][2] * s }, { a1[1][0] * s, a1[1][1] * s, a1[1][2] * s },
				{ a1[2][0] * s, a1[2][1] * s, a1[2][2] * s } };
		Matrix m1 = new Matrix(a1);
		Matrix m1s = new Matrix(a1s);

		// Test for correct scalar multiplication
		Assert.assertEquals(m1s, m1.times(s));
	}

	/**
	 * Test {@link Matrix#divide(double)} and {@link Matrix#divide(double)}.
	 */
	@Test
	public void testDivide() {
		// Initialize values
		double[][] a1 = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } };
		double s = 2;
		double[][] a1s = { { a1[0][0] / s, a1[0][1] / s, a1[0][2] / s }, { a1[1][0] / s, a1[1][1] / s, a1[1][2] / s },
				{ a1[2][0] / s, a1[2][1] / s, a1[2][2] / s } };
		Matrix m1 = new Matrix(a1);
		Matrix m1s = new Matrix(a1s);

		// Test for correct scalar division
		Assert.assertEquals(m1s, m1.divide(s));
	}

	/**
	 * Test {@link Matrix#plus(double)} and {@link Matrix#plus(double)}.
	 */
	@Test
	public void testPlus() {
		// Initialize values
		double[][] a1 = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } };
		double dx = 2;
		double[][] a1dx = { { a1[0][0] + dx, a1[0][1] + dx, a1[0][2] + dx },
				{ a1[1][0] + dx, a1[1][1] + dx, a1[1][2] + dx }, { a1[2][0] + dx, a1[2][1] + dx, a1[2][2] + dx } };
		Matrix m1 = new Matrix(a1);
		Matrix m1dx = new Matrix(a1dx);

		// Test for correct addition
		Assert.assertEquals(m1dx, m1.plus(dx));
	}

	/**
	 * Test {@link Matrix#minus(double)} and {@link Matrix#minus(double)}.
	 */
	@Test
	public void testMinus() {
		// Initialize values
		double[][] a1 = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } };
		double dx = 2;
		double[][] a1dx = { { a1[0][0] - dx, a1[0][1] - dx, a1[0][2] - dx },
				{ a1[1][0] - dx, a1[1][1] - dx, a1[1][2] - dx }, { a1[2][0] - dx, a1[2][1] - dx, a1[2][2] - dx } };
		Matrix m1 = new Matrix(a1);
		Matrix m1dx = new Matrix(a1dx);

		// Test for correct addition
		Assert.assertEquals(m1dx, m1.minus(dx));
	}
}
