package com.max.algs.leetcode;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;

/**
 * The n-queens puzzle is the problem of placing n queens on an n×n chessboard such that
 * no two queens attack each other.
 *
 * see: https://leetcode.com/problems/n-queens/
 */
public class NQueens {

    private static final char QUEEN = 'Q';
    private static final char EMPTY = '.';
    private static final String QUEEN_STR = "" + QUEEN;


    public List<String[]> solveNQueens(int n) {

        if (n == 1) {
            List<String[]> singlePlacement = new ArrayList<>();
            singlePlacement.add(new String[]{QUEEN_STR});
            return singlePlacement;
        }

        if (n < 4) {
            return Collections.emptyList();
        }

        List<String[]> allSolutions = new ArrayList<>();

        char[][] res = new char[n][n];

        for (char[] row : res) {
            Arrays.fill(row, EMPTY);
        }

        final int diagonalsCount = (n << 1) - 3;

        BitSet cols = new BitSet(n);
        BitSet diagonals = new BitSet(diagonalsCount);
        BitSet revDiagonals = new BitSet(diagonalsCount);

        solveRec(0, n, res, cols, diagonals, revDiagonals, allSolutions);

        return allSolutions;
    }

    private void solveRec(int row, int n, char[][] res, BitSet cols, BitSet diagonals, BitSet revDiagonals,
                          List<String[]> allSolutions) {

        if (row == n) {
            allSolutions.add(constructSolution(res));
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

                solveRec(row + 1, n, res, cols, diagonals, revDiagonals, allSolutions);

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

    private String[] constructSolution(char[][] data) {

        String[] solution = new String[data.length];

        for (int row = 0; row < data.length; row++) {

            StringBuilder buf = new StringBuilder();

            for (int col = 0; col < data.length; col++) {
                buf.append(data[row][col]);
            }

            solution[row] = buf.toString();
        }

        return solution;
    }


    public NQueens() throws Exception {
        int n = 10;
        List<String[]> solutions = solveNQueens(n);
        System.out.println(solutions.size());
    }


    public static void main(String[] args) {
        try {
            new NQueens();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
