package com.max.algs.chess;

import com.max.algs.ds.Pair;

import java.util.*;

/**
 * Find shortest Knight path from 'start' position till 'end'.
 */
public final class KnightShortestPathResolver {

    /**
     * 8 possible locations
     */
    private static List<Pair<Integer, Integer>> getPossibleMoves(int row, int col) {

        List<Pair<Integer, Integer>> moves = new ArrayList<>();

        int[][] offsets = {
                {-2, 2},
                {-1, 1}
        };

        fillMoves(moves, row, col, offsets[0], offsets[1]);
        fillMoves(moves, row, col, offsets[1], offsets[0]);

        return moves;
    }

    private static void fillMoves(List<Pair<Integer, Integer>> moves, int initialRow, int initialCol,
                                  int[] rowOffsetValues, int[] collOffsetValues) {
        for (int rowOffset : rowOffsetValues) {
            for (int colOffset : collOffsetValues) {

                int curRow = initialRow + rowOffset;
                int curCol = initialCol + colOffset;

                if (inRange(curRow, curCol)) {
                    moves.add(new Pair<>(curRow, curCol));
                }
            }
        }
    }

    private static boolean inRange(int row, int col) {
        return row >= 0 && col >= 0 && row < 8 && col < 8;
    }

    public static void printPath(Pair<Integer, Integer> start, Pair<Integer, Integer> end) {

        Set<Pair<Integer, Integer>> marked = new HashSet<>();

        Queue<SingleStepSolution> queue = new ArrayDeque<>();

        marked.add(start);
        queue.add(new SingleStepSolution(start.getFirst(), start.getSecond()));

        while (!queue.isEmpty()) {
            SingleStepSolution cur = queue.poll();

            if (cur.row == end.getFirst() && cur.col == end.getSecond()) {
                System.out.println("solution found: " + cur);
                break;
            }

            List<Pair<Integer, Integer>> possibleMoves = getPossibleMoves(cur.row, cur.col);

            for (Pair<Integer, Integer> move : possibleMoves) {
                if (!marked.contains(move)) {
                    queue.add(new SingleStepSolution(move.getFirst(), move.getSecond(), cur));
                }
            }

        }

    }

    private static class SingleStepSolution {
        final int row;
        final int col;
        final SingleStepSolution prev;

        SingleStepSolution(int row, int col, SingleStepSolution prev) {
            this.row = row;
            this.col = col;
            this.prev = prev;
        }

        SingleStepSolution(int row, int col) {
            this(row, col, null);
        }

        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder();

            Deque<SingleStepSolution> stack = new ArrayDeque<>();
            SingleStepSolution cur = this;

            while (cur != null) {
                stack.push(cur);
                cur = cur.prev;
            }

            while (true) {
                SingleStepSolution curSol = stack.pop();
                buf.append("[").append(curSol.row).append("; ").append(curSol.col).append("]");

                if (stack.isEmpty()) {
                    break;
                }
                buf.append(" -> ");
            }

            return buf.toString();
        }
    }


}
