package com.max.algs.epi.array;

import com.max.algs.util.MatrixUtils;
import org.apache.log4j.Logger;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * 6.17. Compute the spiral ordering of a 2-D array.
 */
public class SpiralOrderingOf2DArray {

    private static final Logger LOG = Logger.getLogger(SpiralOrderingOf2DArray.class);

    private SpiralOrderingOf2DArray() throws Exception {

        int n = 7;

        int[][] m = new int[n][n];

        for (int row = 0, elem = 1; row < n; ++row) {
            for (int col = 0; col < n; ++col, ++elem) {
                m[row][col] = elem;
            }
        }

        int[][] m2 = MatrixUtils.deepCopy(m);

//        System.out.println(MatrixUtils.toString(m));

        printInSpiralOrder(m);
        printInSpiralOrderAsTraversation(m2);

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * Print 2-D matrix in spiral order.
     * <p>
     * time: O(N^2)
     * space: O(1)
     */
    public static void printInSpiralOrder(int[][] matrix) {

        checkArgument(matrix != null, "null 'matrix' argument passed");
        checkArgument(isSquareMatrix(matrix), "Argument 'matrix' is not a square 2-D array");

        final int matrixLength = matrix.length;

        for (int first = 0; first < matrixLength >> 1; ++first) {

            int last = matrixLength - 1 - first;

            // first row left-to-right
            for (int col = first; col < last; ++col) {
                System.out.print(matrix[first][col] + ", ");
            }

            // last column top-to-down
            for (int row = first; row < last; ++row) {
                System.out.print(matrix[row][last] + ", ");
            }

            // last row right-to-left
            for (int col = last; col > first; --col) {
                System.out.print(matrix[last][col] + ", ");
            }

            // first column bottom-to-up
            for (int row = last; row > first; --row) {
                System.out.print(matrix[row][first] + ", ");
            }
        }

        // print last element if matrix size is ODD
        if ((matrix.length & 1) != 0) {
            int mid = matrixLength >> 1;
            System.out.print(matrix[mid][mid]);
        }

        // call `println` to flush the stream.
        System.out.println();
    }

    /**
     * time: O(N^2)
     * space: O(1)
     */
    public static void printInSpiralOrderAsTraversation(int[][] matrix) {

        int row = 0;
        int col = 0;

        Direction curDir = Direction.RIGTH;

        while (matrix[row][col] != 0) {

            System.out.print(matrix[row][col] + ", ");
            matrix[row][col] = 0;

            // check if we need to change the direction
            if (!curDir.canMove(matrix, row, col)) {
                curDir = curDir.nextDirection();

                // stop algorithm, if after changing
                // the direction there is no move left
                if (!curDir.canMove(matrix, row, col)) {
                    break;
                }
            }

            int[] pair = curDir.next(row, col);

            row = pair[0];
            col = pair[1];
        }

        System.out.println();

    }

    private static boolean isSquareMatrix(int[][] m) {
        assert m != null;

        int rowsCnt = m.length;

        for (int[] row : m) {
            if (row == null || row.length != rowsCnt) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        try {
            new SpiralOrderingOf2DArray();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    private static enum Direction {
        RIGTH {
            @Override
            boolean canMove(int[][] m, int row, int col) {
                return col < m.length - 1 && m[row][col + 1] != 0;
            }

            @Override
            Direction nextDirection() {
                return BOTTOM;
            }

            @Override
            int[] next(int row, int col) {
                return new int[]{row, col + 1};
            }
        },
        BOTTOM {
            @Override
            boolean canMove(int[][] m, int row, int col) {
                return row < m.length - 1 && m[row + 1][col] != 0;
            }

            @Override
            Direction nextDirection() {
                return LEFT;
            }

            @Override
            int[] next(int row, int col) {
                return new int[]{row + 1, col};
            }
        },
        LEFT {
            @Override
            boolean canMove(int[][] m, int row, int col) {
                return col > 0 && m[row][col - 1] != 0;
            }

            @Override
            Direction nextDirection() {
                return TOP;
            }

            @Override
            int[] next(int row, int col) {
                return new int[]{row, col - 1};
            }
        },
        TOP {
            @Override
            boolean canMove(int[][] m, int row, int col) {
                return row > 0 && m[row - 1][col] != 0;
            }

            @Override
            Direction nextDirection() {
                return RIGTH;
            }

            @Override
            int[] next(int row, int col) {
                return new int[]{row - 1, col};
            }
        };

        abstract boolean canMove(int[][] m, int row, int col);

        abstract Direction nextDirection();

        abstract int[] next(int row, int col);
    }

}
