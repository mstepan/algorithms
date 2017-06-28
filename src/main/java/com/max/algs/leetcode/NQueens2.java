package com.max.algs.leetcode;


import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.BitSet;

/**
 * The n-queens puzzle is the problem of placing n queens on an n×n chessboard such that
 * no two queens attack each other.
 * <p>
 * see: https://leetcode.com/problems/n-queens/
 */
public class NQueens2 {

    private static final Logger LOG = Logger.getLogger(NQueens2.class);

    private static final char QUEEN = 'Q';
    private static final char EMPTY = '.';
    private static final String QUEEN_STR = "" + QUEEN;

    public NQueens2() throws Exception {
        int n = 10;
        System.out.println(totalNQueens(n));
    }

    public static void main(String[] args) {
        try {
            new NQueens2();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    public int totalNQueens(int n) {

        if (n == 1) {
            return 1;
        }

        if (n < 4) {
            return 0;
        }

        char[][] res = new char[n][n];

        for (char[] row : res) {
            Arrays.fill(row, EMPTY);
        }

        final int diagonalsCount = (n << 1) - 3;

        BitSet cols = new BitSet(n);
        BitSet diagonals = new BitSet(diagonalsCount);
        BitSet revDiagonals = new BitSet(diagonalsCount);

        MutableInt totalCount = new MutableInt();

        solveRec(0, n, res, cols, diagonals, revDiagonals, totalCount);

        return totalCount.value;
    }

    private void solveRec(int row, int n, char[][] res, BitSet cols, BitSet diagonals, BitSet revDiagonals,
                          MutableInt totalCount) {

        if (row == n) {
            totalCount.inc();
            return;
        }

        int offset = n - 2;

        for (int col = 0; col < n; col++) {

            int diagonalIdx = row + col - 1;

            int revDiagonalIdx = offset - (row - col);

            if (!(cols.get(col) || (diagonalIdx >= 0 && diagonals.get(diagonalIdx))
                    || (revDiagonalIdx >= 0 && revDiagonals.get(revDiagonalIdx)))) {

                cols.set(col);
                if (diagonalIdx >= 0) {
                    diagonals.set(diagonalIdx);
                }
                if (revDiagonalIdx >= 0) {
                    revDiagonals.set(revDiagonalIdx);
                }
                res[row][col] = QUEEN;

                solveRec(row + 1, n, res, cols, diagonals, revDiagonals, totalCount);

                res[row][col] = EMPTY;

                if (revDiagonalIdx >= 0) {
                    revDiagonals.clear(revDiagonalIdx);
                }

                if (diagonalIdx >= 0) {
                    diagonals.clear(diagonalIdx);
                }
                cols.clear(col);

            }
        }
    }

    private static final class MutableInt {
        private int value;

        void inc() {
            ++value;
        }
    }

}
