package com.max.algs.epi.recursion;

import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.BitSet;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.math3.util.MathUtils.checkNotNull;


public class SudokuRecursiveSolver {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());

    private static final class BoardTracker {

        private static final int BLOCK_SIZE = 3;

        final int length;

        final BitSet[] rowsMap;
        final BitSet[] colsMap;
        final BitSet[] blocksMap;

        boolean solved;

        static BoardTracker createFromBoard(int[][] board) {
            BoardTracker tracker = new BoardTracker(board.length);

            for (int row = 0; row < board.length; ++row) {
                for (int col = 0; col < board[row].length; ++col) {
                    if (board[row][col] != 0) {
                        tracker.addValue(row, col, board[row][col]);
                    }
                }
            }

            return tracker;
        }

        BoardTracker(int rowsCount) {
            this.length = rowsCount;
            this.rowsMap = new BitSet[rowsCount];
            this.colsMap = new BitSet[rowsCount];
            this.blocksMap = new BitSet[rowsCount];

            // we need to store elements in range [1;9]
            for (int i = 0; i < length; ++i) {
                rowsMap[i] = new BitSet(length + 1);
                colsMap[i] = new BitSet(length + 1);
                blocksMap[i] = new BitSet(length + 1);
            }
        }

        void addValue(int row, int col, int value) {

            assert value >= 1 && value <= 9;
            assert !rowsMap[row].get(value);
            assert !colsMap[col].get(value);
            assert !blocksMap[blockIndex(row, col)].get(value);

            rowsMap[row].set(value);
            colsMap[col].set(value);
            blocksMap[blockIndex(row, col)].set(value);
        }

        void removeValue(int row, int col, int value) {

            assert value >= 1 && value <= 9;
            assert rowsMap[row].get(value);
            assert colsMap[col].get(value);
            assert blocksMap[blockIndex(row, col)].get(value);

            rowsMap[row].clear(value);
            colsMap[col].clear(value);
            blocksMap[blockIndex(row, col)].clear(value);
        }

        void markSolved() {
            this.solved = true;
        }

        boolean notSolved() {
            return !solved;
        }

        boolean isSolved() {
            return solved;
        }

        private int blockIndex(int row, int col) {
            int index = BLOCK_SIZE * (row / BLOCK_SIZE) + (col / BLOCK_SIZE);

            assert index >= 0 && index < blocksMap.length;

            return index;
        }

        /**
         * check row, column and 3x3 square block
         */
        boolean isValid(int row, int col, int possibleValue) {
            return !(rowsMap[row].get(possibleValue) || colsMap[col].get(possibleValue) ||
                    blocksMap[blockIndex(row, col)].get(possibleValue));
        }
    }

    /**
     * N x N - board size (N rows and N columns)
     * K - empty cells (cells with 0 values)
     * <p>
     * time: O(10^K)
     * space: O(N)
     */
    private static boolean solveBoard(int[][] board) {
        checkValidBoard(board);

        BoardTracker tracker = BoardTracker.createFromBoard(board);

        solveRec(board, 0, 0, tracker);
        return tracker.isSolved();
    }

    private static void checkValidBoard(int[][] board) {
        checkNotNull(board);

        for (int[] row : board) {
            checkArgument(row.length == board.length);
        }
    }


    private static void solveRec(int[][] board, int row, int col, BoardTracker tracker) {
        // we reached the end of a board, so sudoku solvable
        if (row == board.length) {
            tracker.markSolved();
            return;
        }

        assert row < board.length && col < board.length;

        int cols = board.length;
        int nextRow = row + (col == cols - 1 ? 1 : 0);
        int nextCol = (col + 1) % cols;

        assert nextRow <= board.length;
        assert nextCol < board.length;

        // free slot found
        if (board[row][col] == 0) {
            for (int possibleValue = 1; possibleValue <= 9 && tracker.notSolved(); ++possibleValue) {

                if (tracker.isValid(row, col, possibleValue)) {

                    board[row][col] = possibleValue;
                    tracker.addValue(row, col, possibleValue);

                    solveRec(board, nextRow, nextCol, tracker);

                    if (tracker.notSolved()) {
                        board[row][col] = 0;
                        tracker.removeValue(row, col, possibleValue);
                    }
                }
            }
        }
        else {
            solveRec(board, nextRow, nextCol, tracker);
        }
    }

    private SudokuRecursiveSolver() {

        int[][] board = {
                {5, 3, 0, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 8, 0, 0, 7, 9}
        };

        boolean solvable = solveBoard(board);

        if (solvable) {
            for (int[] row : board) {
                LOG.info(Arrays.toString(row));
            }
        }
        else {
            LOG.info("Board not solvable");
        }

        LOG.info("SudokuRecursiveSolver done...");
    }

    public static void main(String[] args) {
        try {
            new SudokuRecursiveSolver();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}
