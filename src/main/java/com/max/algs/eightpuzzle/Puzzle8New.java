package com.max.algs.eightpuzzle;

import com.max.algs.ds.Pair;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Puzzle 8 solver using A* algorithm and Manhattan distance as heuristic.
 */
public class Puzzle8New {

    private static final Logger LOG = Logger.getLogger(Puzzle8New.class);


    private Puzzle8New() {

        int[][] data = {
                {1, 4, 8},
                {2, 5, 7},
                {3, 6, 0}
        };

        solve(data);

        LOG.info("Puzzle 8 done: java-" + System.getProperty("java.version"));
    }

    private static Pair<Integer, Integer> findRowAndCol(int[][] data) {
        for (int row = 0; row < data.length; ++row) {
            for (int col = 0; col < data[row].length; ++col) {
                if (data[row][col] == 0) {
                    return new Pair<>(row, col);
                }
            }
        }

        return null;
    }

    private static List<MoveDirection> possibleMoves(int[][] data) {

        Pair<Integer, Integer> targetRowColPair = findRowAndCol(data);

        int targetRow = targetRowColPair == null ? 0 : targetRowColPair.getFirst();
        int targetCol = targetRowColPair == null ? 0 : targetRowColPair.getSecond();

        List<MoveDirection> moves = new ArrayList<>();

        if (targetRow != 0) {
            moves.add(MoveDirection.UP);
        }

        if (targetRow != data.length - 1) {
            moves.add(MoveDirection.DOWN);
        }

        if (targetCol != 0) {
            moves.add(MoveDirection.LEFT);
        }

        if (targetCol != data[0].length - 1) {
            moves.add(MoveDirection.RIGHT);
        }

        return moves;

    }

    private static void solve(int[][] data) {

        int inversionCount = countInversions(data);

        // odd inversions count, unsolvable board for 'odd' board size
        if ((inversionCount & 1) != 0) {
            throw new IllegalArgumentException("Unsolvable board found, inversions count = " + inversionCount +
                    ", should be even.");
        }

        Queue<PartialSol> minQueue = new PriorityQueue<>();
        minQueue.add(new PartialSol(data));

        while (!minQueue.isEmpty()) {
            PartialSol partialSolution = minQueue.poll();

            if (partialSolution.isFinal()) {
                LOG.info("Solution found in " + partialSolution.solutions.size() + " moves");
                for (PartialSol singleMoveSolution : partialSolution.solutions) {
                    LOG.info(singleMoveSolution);
                }
                break;
            }

            List<MoveDirection> moves = possibleMoves(partialSolution.dataCopy);
            if (partialSolution.curMove != null) {
                moves.remove(partialSolution.curMove.inverse());
            }

            for (MoveDirection singleMove : moves) {

                PartialSol newSolution = new PartialSol(partialSolution, singleMove);

                minQueue.add(newSolution);
            }
        }
    }

    private static int countInversions(int[][] data) {

        final int rows = data.length;
        final int lastIndex = data.length * data.length;

        int inversions = 0;

        for (int index = 0; index < lastIndex - 2; ++index) {

            int value = data[index / rows][index % rows];

            for (int i = index + 1; i < lastIndex; ++i) {

                int curValue = data[i / rows][i % rows];

                if (curValue != 0 && curValue > value) {
                    ++inversions;
                }
            }

        }

        return inversions;
    }

    public static void main(String[] args) {
        try {
            new Puzzle8New();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    private enum MoveDirection {
        LEFT {
            @Override
            public void move(int[][] data, int row, int col) {
                int temp = data[row][col - 1];
                data[row][col - 1] = 0;
                data[row][col] = temp;
            }

            @Override
            public MoveDirection inverse() {
                return RIGHT;
            }
        },
        RIGHT {
            @Override
            public void move(int[][] data, int row, int col) {

                try {
                    int temp = data[row][col + 1];
                    data[row][col + 1] = 0;
                    data[row][col] = temp;
                }
                catch (ArrayIndexOutOfBoundsException ex) {
                    LOG.error(ex.getMessage(), ex);
                }
            }

            @Override
            public MoveDirection inverse() {
                return LEFT;
            }
        },
        UP {
            @Override
            public void move(int[][] data, int row, int col) {
                int temp = data[row - 1][col];
                data[row - 1][col] = 0;
                data[row][col] = temp;
            }

            @Override
            public MoveDirection inverse() {
                return DOWN;
            }
        },
        DOWN {
            @Override
            public void move(int[][] data, int row, int col) {
                int temp = data[row + 1][col];
                data[row + 1][col] = 0;
                data[row][col] = temp;
            }

            @Override
            public MoveDirection inverse() {
                return UP;
            }
        };

        public abstract void move(int[][] data, int row, int col);

        public abstract MoveDirection inverse();

        public void makeMove(int[][] data) {

            for (int row = 0; row < data.length; ++row) {
                for (int col = 0; col < data[row].length; ++col) {
                    if (data[row][col] == 0) {
                        this.move(data, row, col);
                        return;
                    }
                }
            }
        }
    }

    private static final class PartialSol implements Comparable<PartialSol> {

        private final int originalCost;
        private final int estimatedCost;

        private final int[][] dataCopy;

        private final MoveDirection curMove;

        private final List<PartialSol> solutions = new ArrayList<>();

        public PartialSol(PartialSol base, MoveDirection move) {
            this.solutions.addAll(base.solutions);
            this.solutions.add(this);
            this.originalCost = base.originalCost + 1;
            this.dataCopy = new int[base.dataCopy.length][];

            for (int row = 0; row < dataCopy.length; ++row) {
                dataCopy[row] = Arrays.copyOf(base.dataCopy[row], base.dataCopy[row].length);
            }

            move.makeMove(this.dataCopy);

            this.estimatedCost = manhattanDistance(dataCopy);
            this.curMove = move;
        }

        public PartialSol(int[][] data) {
            this.solutions.add(this);
            this.originalCost = 0;
            this.dataCopy = new int[data.length][];

            for (int row = 0; row < dataCopy.length; ++row) {
                dataCopy[row] = Arrays.copyOf(data[row], data[row].length);
            }

            this.estimatedCost = manhattanDistance(dataCopy);
            this.curMove = null;
        }

        private int calculateCost() {
            return originalCost + estimatedCost;
        }

        private static int manhattanDistance(int[][] data) {
            return ManhattanDistanceCalculator.calculateDistance(data);
        }

        @Override
        public int compareTo(PartialSol other) {
            return Integer.compare(calculateCost(), other.calculateCost());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PartialSol that = (PartialSol) o;

            return calculateCost() == that.calculateCost();
        }

        @Override
        public int hashCode() {
            return calculateCost();
        }

        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder();

            for (int[] row : dataCopy) {
                buf.append(Arrays.toString(row)).append(System.getProperty("line.separator"));
            }

            return buf.toString();
        }

        /**
         * Determine if position final or not.
         */
        boolean isFinal() {

            int lastIndex = dataCopy.length * dataCopy[0].length;

            int index = 0;

            for (int row = 0; row < dataCopy.length; ++row) {
                for (int col = 0; col < dataCopy[row].length; ++col) {

                    index = (index + 1) % lastIndex;

                    if (dataCopy[row][col] != index) {
                        return false;
                    }


                }
            }

            return true;
        }


    }

    private static class ManhattanDistanceCalculator {

        private ManhattanDistanceCalculator() {

        }

        private static final Pair<Integer, Integer>[] EXPECTED_POS = (Pair[]) new Pair[9]; // [0;7]

        static {

            final int rows = 3;

            for (int i = 1; i < 9; ++i) {

                int index = i - 1;
                EXPECTED_POS[i] = new Pair<>(index / rows, index % rows);
            }
        }

        public static int calculateDistance(int[][] data) {

            int distance = 0;

            for (int row = 0; row < data.length; ++row) {
                for (int col = 0; col < data[row].length; ++col) {

                    int value = data[row][col];

                    if (value != 0) {
                        Pair<Integer, Integer> expectedPos = EXPECTED_POS[value];
                        distance += Math.abs(expectedPos.getFirst() - row) + Math.abs(expectedPos.getSecond() - col);
                    }
                }
            }

            return distance;
        }

    }

}
