package com.max.algs.util;

import com.max.algs.ds.Pair;
import com.max.algs.ds.heap.BinaryHeap;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class MatrixUtils {


    private static final Random RAND = ThreadLocalRandom.current();

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");


    private MatrixUtils() {
        super();
    }

    /**
     * Search value in row and columns sorted matrix.
     * <p>
     * time: O(N)
     * space: O(1)
     */
    public static Optional<Pair<Integer, Integer>> findElement(int[][] matrix, int value) {

        checkNotNull(matrix);

        int row = 0;
        int col = matrix[0].length - 1;

        while (row < matrix.length && col >= 0) {
            if (matrix[row][col] == value) {
                return Optional.of(new Pair<>(row, col));
            }

            // skip row
            if (matrix[row][col] < value) {
                ++row;
            }
            // skip column
            else {
                --col;
            }
        }

        return Optional.empty();
    }

    /**
     * Convert matrix to string using spiral order traversation.
     */
    public static String toSpiralOrder(int[][] matrix) {
        checkNotNull(matrix, "null 'matrix' passed");
        checkArgument(isMatrix(matrix), "Not a square matrix passed");

        if (matrix.length == 0) {
            return "";
        }

        if (matrix.length == 1) {
            return String.valueOf(matrix[0][0]);
        }

        StringBuilder buf = new StringBuilder();

        final int middle = matrix.length / 2;

        for (int cycle = 0; cycle < middle; ++cycle) {

            int last = matrix.length - 1 - cycle;

            // first row, left to right
            for (int col = cycle; col < last; ++col) {
                buf.append(matrix[cycle][col]).append(", ");
            }

            // last column, top  to down
            for (int row = cycle; row < last; ++row) {
                buf.append(matrix[row][last]).append(", ");
            }

            // last row, right to left
            for (int col = last; col > cycle; --col) {
                buf.append(matrix[last][col]).append(", ");
            }

            // first column, bottom to up
            for (int row = last; row > cycle; --row) {
                buf.append(matrix[row][cycle]).append(", ");
            }
        }

        // odd matrix size, add last middle element
        if ((matrix.length & 1) != 0) {
            buf.append(matrix[middle][middle]);
        }
        else {
            // delete last ',' value
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
        }

        return buf.toString();
    }

    /**
     * Check if a 2D array is matrix, so no of rows == no of columns.
     */
    private static boolean isMatrix(int[][] matrix) {
        int rowsCount = matrix.length;

        if (rowsCount == 0) {
            return true;
        }

        for (int[] singleRow : matrix) {
            if (singleRow == null || singleRow.length != rowsCount) {
                return false;
            }
        }

        return true;
    }

    /**
     * N - matrix size
     * M - max elements count to find.
     * <p>
     * time: O(N*lgM)
     * space: O(M)
     */
    public static List<Integer> findMaxElements(int[][] matrix, int maxCount) {

        checkNotNull(matrix);
        checkArgument(maxCount >= 0, "'maxCount' parameter is incorrect %s, expected positive value", maxCount);

        if (maxCount == 0) {
            return Collections.emptyList();
        }

        BinaryHeap<Integer> minHeap = BinaryHeap.minHeap(maxCount);

        int rowsCount = matrix.length;
        int value;

        for (int row = 0; row < rowsCount; row++) {
            for (int col = 0; col < matrix[row].length; col++) {

                value = matrix[row][col];

                if (minHeap.size() < maxCount) {
                    minHeap.add(value);
                }
                else if (minHeap.peek().compareTo(value) < 0) {
                    minHeap.poll();
                    minHeap.add(value);
                }
            }
        }

        return new ArrayList<>(minHeap);
    }

    public static boolean isMatrix(char[][] data) {

        if (data == null) {
            throw new IllegalArgumentException("'NULL' parameter passed for matrix check");
        }

        final int rowsCount = data.length;

        for (int row = 0; row < rowsCount; row++) {
            if (data[row] == null || rowsCount != data[row].length) {
                return false;
            }
        }
        return true;
    }

    /**
     * Rotate matrix by 90 degree.
     * <p>
     * time: O(N^2)
     * space: O(1)
     */
    public static void rotate(int[][] matrix) {

        if (matrix == null) {
            throw new IllegalArgumentException("NULL 'matrix' passed");
        }

        for (int cycle = 0; cycle < matrix.length / 2; cycle++) {
            int first = cycle;
            int last = matrix.length - cycle - 1;

            int elemsCount = last - first;

            for (int i = 0; i < elemsCount; i++) {
                int temp = matrix[first][first + i];
                matrix[first][first + i] = matrix[last - i][first];
                matrix[last - i][first] = matrix[last][last - i];
                matrix[last][last - i] = matrix[first + i][last];
                matrix[first + i][last] = temp;
            }
        }
    }

    /**
     * Zero row and column if cell is '0'.
     * <p>
     * time: O(NM)
     * space: O(N/8 + M/8) bytes = O(N/32 + M/32)
     */
    public static void zeroRowAndColumn(int[][] matrix) {

        if (matrix == null) {
            throw new IllegalArgumentException("NULL 'matrix' passed");
        }

        BitSet zeroRow = new BitSet(matrix.length);
        BitSet zeroColumn = new BitSet(matrix[0].length);

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 0) {
                    zeroRow.set(i);
                    zeroColumn.set(j);
                }
            }
        }

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (zeroRow.get(i) || zeroColumn.get(j)) {
                    matrix[i][j] = 0;
                }
            }
        }

    }

    public static void sort(int[][] matrix) {

        // sort each column
        for (int i = 0; i < matrix.length; i++) {
            Arrays.sort(matrix[i]);
        }

        // sort each row
        for (int column = 0; column < matrix[0].length; column++) {
            for (int row = 1; row < matrix.length; row++) {
                int temp = matrix[row][column];

                int j = row - 1;

                while (j >= 0 && matrix[j][column] > temp) {
                    matrix[j + 1][column] = matrix[j][column];
                    --j;
                }

                matrix[j + 1][column] = temp;
            }
        }
    }

    public static boolean bSearch(int[][] matrix, int key) {
        return binarySearchRec(matrix, key, 0, matrix.length - 1, 0, matrix[0].length - 1);
    }

    /**
     * Sequential search.
     * <p>
     * time: O(N*M)
     * space: O(1)
     */
    public static final boolean search(int[][] matrix, int key) {

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (key == matrix[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Binary search through matrix.
     * Matrix should be sorted by ows and columns.
     * <p>
     * time:  O( (N*M)^0.8 )
     * space: O(1)
     */
    private static boolean binarySearchRec(int[][] matrix, int key, int rowLo, int rowHi, int colLo, int colHi) {

        if (rowLo > rowHi || colLo > colHi) {
            return false;
        }

        // one element left
        if (rowLo == rowHi && colLo == colHi) {
            return (matrix[rowLo][colLo] == key) ? true : false;
        }

        int rowMid = (rowLo + rowHi) >>> 1;
        int colMid = (colLo + colHi) >>> 1;

        int curMatrixValue = matrix[rowMid][colMid];

        if (curMatrixValue == key) {
            return true;
        }

        if (key > curMatrixValue) {
            return binarySearchRec(matrix, key, rowLo, rowMid, colMid + 1, colHi) ||
                    binarySearchRec(matrix, key, rowMid + 1, rowHi, colLo, colMid) ||
                    binarySearchRec(matrix, key, rowMid + 1, rowHi, colMid + 1, colHi);
        }

        // key less
        return binarySearchRec(matrix, key, rowLo, rowMid - 1, colLo, colMid - 1) ||
                binarySearchRec(matrix, key, rowLo, rowMid - 1, colMid, colHi) ||
                binarySearchRec(matrix, key, rowMid, rowHi, colLo, colMid - 1);
    }

    /**
     * Generate square matrix with random values.
     *
     * @param size     - row x cols
     * @param maxValue - max random value excluded.
     * @return
     */
    public static int[][] generateRandomMatrix(int size, int maxValue) {

        int[][] matrix = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = RAND.nextInt(maxValue);
            }
        }

        return matrix;
    }

    public static int[][] generateRandomMatrix(int size) {

        int[][] matrix = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = RAND.nextInt();
            }
        }

        return matrix;
    }

    /**
     * Deep copy of 2 dimensional array.
     */
    public static int[][] deepCopy(int[][] matrix) {

        final int rows = matrix.length;

        int[][] res = new int[rows][];

        for (int i = 0; i < rows; ++i) {
            res[i] = Arrays.copyOf(matrix[i], matrix[i].length);
        }

        return res;
    }

    public static String toString(char[][] matrix) {

        StringBuilder buf = new StringBuilder();

        for (char[] row : matrix) {
            for (int j = 0; j < row.length; j++) {
                if (j > 0) {
                    buf.append(", ");
                }
                buf.append(row[j]);
            }

            buf.append(LINE_SEPARATOR);
        }


        return buf.toString();
    }

    public static String toString(int[][] matrix) {

        StringBuilder buf = new StringBuilder();

        for (int[] row : matrix) {
            for (int j = 0; j < row.length; j++) {
                if (j > 0) {
                    buf.append(", ");
                }
                buf.append(row[j]);
            }

            buf.append(LINE_SEPARATOR);
        }


        return buf.toString();
    }

    public static String toString(double[][] matrix) {

        StringBuilder buf = new StringBuilder();

        for (double[] row : matrix) {
            for (int j = 0; j < row.length; j++) {
                if (j > 0) {
                    buf.append(", ");
                }
                buf.append(row[j]);
            }

            buf.append(LINE_SEPARATOR);
        }


        return buf.toString();
    }

    public static <T> String toString(T[][] matrix) {

        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (j > 0) {
                    buf.append(", ");
                }
                buf.append(matrix[i][j]);
            }

            buf.append(LINE_SEPARATOR);
        }


        return buf.toString();
    }


}
