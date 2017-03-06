package com.max.algs.puzzle_8;


import com.max.algs.util.ArrayUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public final class Puzzle8Main {

    private static List<MoveDirection> solution = null;

    private static enum MoveDirection {
        LEFT(0, -1) {
            @Override
            public MoveDirection inverse() {
                return RIGTH;
            }
        },
        RIGTH(0, 1) {
            @Override
            public MoveDirection inverse() {
                return LEFT;
            }
        },
        UP(-1, 0) {
            @Override
            public MoveDirection inverse() {
                return DOWN;
            }
        },
        DOWN(1, 0) {
            @Override
            public MoveDirection inverse() {
                return UP;
            }
        };

        private final int rowOffset;
        private final int colOffset;

        MoveDirection(int rowOffset, int colOffset) {
            this.rowOffset = rowOffset;
            this.colOffset = colOffset;
        }

        public abstract MoveDirection inverse();

        public int[] makeMove(int[][] board, int row, int col) {
            board[row][col] = board[row + rowOffset][col + colOffset];
            board[row + rowOffset][col + colOffset] = 0;

            return new int[]{row + rowOffset, col + colOffset};
        }

    }

    private static void solve(int[][] board) {
        solveRec(board, 1, 1, new ArrayDeque<>());
    }

    private static void solveRec(int[][] board, int row, int col, Deque<MoveDirection> moveHistory) {

        if (solution != null) {
            return;
        }

        if (isFinalState(board)) {

            printBoard(board);
            System.out.println("solved: " + moveHistory.size());

            solution = new ArrayList<>(moveHistory);

            return;
        }

        for (MoveDirection move : getPossibleMoves(board, row, col)) {

            // skip if last made move was in opposite direction
            if (!moveHistory.isEmpty() && moveHistory.peekLast() == move.inverse()) {
                continue;
            }

            moveHistory.addLast(move);
            int[] newPosition = move.makeMove(board, row, col);

            solveRec(board, newPosition[0], newPosition[1], moveHistory);

            move.inverse().makeMove(board, newPosition[0], newPosition[1]);
            moveHistory.pollLast();

        }
    }


    private static List<MoveDirection> getPossibleMoves(int[][] board, int row, int col) {

        List<MoveDirection> moves = new ArrayList<>();

        if (col != 0) {
            moves.add(MoveDirection.LEFT);
        }

        if (col != board[0].length - 1) {
            moves.add(MoveDirection.RIGTH);
        }

        if (row != 0) {
            moves.add(MoveDirection.UP);
        }

        if (row != board.length - 1) {
            moves.add(MoveDirection.DOWN);
        }

        Collections.shuffle(moves);

        return Collections.unmodifiableList(moves);
    }

    private static boolean isFinalState(int[][] board) {

        int index = 0;

        for (int[] singleRow : board) {
            for (int col = 0; col < singleRow.length; col++, index++) {
                if (singleRow[col] != index) {
                    return false;
                }
            }
        }

        return true;
    }

    private static int[][] createRandomBoard() {
        int[][] board = new int[3][3];

        int[] elems = new int[8];
        for (int i = 0; i < elems.length; i++) {
            elems[i] = i + 1;
        }

        ArrayUtils.shuffle(elems);

        int index = 0;

        for (int row = 0; row < board.length && index < elems.length; row++) {
            for (int col = 0; col < board[row].length && index < elems.length; col++) {

                if (row == 1 && col == 1) {
                    continue;
                }

                board[row][col] = elems[index];
                ++index;
            }
        }

        return board;
    }

    private static void printBoard(int[][] board) {
        for (int[] singleRow : board) {
            System.out.println(Arrays.toString(singleRow));
        }
    }


    private Puzzle8Main() throws Exception {

        int[][] board = createRandomBoard();

        int[][] boardCopy = makeCopy(board);

        solve(boardCopy);

//        checkSolution(board, solution);


        System.out.println("Puzzle 8 done...");
    }

//    private void checkSolution(int[][] initialBoardState, List<MoveDirection> curSolution) {
//
////        int row = 1;
////        int col = 1;
////
////        for( MoveDirection move : curSolution ){
////            int[] newPos = move.makeMove(initialBoardState, row, col);
////
////            row = newPos[0];
////            col = newPos[1];
////        }
////
////        if( ! isFinalState(initialBoardState) ){
////            throw new IllegalStateException("Board state is incorrect");
////        }
//
//        System.out.println("Solution verified");
//    }

    private int[][] makeCopy(int[][] board) {
        int[][] copy = new int[board.length][];

        for (int i = 0; i < board.length; i++) {
            copy[i] = Arrays.copyOf(board[i], board[i].length);
        }
        return copy;
    }


    public static void main(String[] args) {
        try {
            new Puzzle8Main();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}

