package com.max.algs.graph;

import com.max.algs.ds.Pair;

import java.util.*;


/**
 * Given a matrix that contains 0s and 1s, find the shortest exit and print the path.
 * You can navigate in top,bottom,left or right directions.
 * <p>
 * <p>
 * Do BFS search in matrix.
 * <p>
 * N - matrix size (matrix.length * matrix.length)
 * <p>
 * time: O(N)
 * space: O(N)
 * <p>
 * Thread safe.
 */
public final class ShortestPathInMatrix {


    private final int[][] matrix;
    private final Pair<Integer, Integer> startPoint;

    private final int rowLength;

    private final Queue<Pair<Integer, Integer>> queue;
    private final BitSet visited;
    private final int[] parent;


    public ShortestPathInMatrix(int[][] searchMatrix, Pair<Integer, Integer> pointToStartSearch) {
        super();

        if (searchMatrix == null) {
            throw new IllegalArgumentException("NULL 'matrix' passed");
        }

        if (pointToStartSearch == null) {
            throw new IllegalArgumentException("NULL 'start point' passed");
        }

        matrix = searchMatrix;
        startPoint = pointToStartSearch;
        rowLength = matrix.length;

        if (!isMatrix(matrix)) {
            throw new IllegalArgumentException("Not a matrix passed");
        }


        parent = new int[rowLength * rowLength];

        clearParents();

        visited = new BitSet(rowLength * rowLength);
        queue = new ArrayDeque<>();
    }


    public List<Pair<Integer, Integer>> find() {

        if (startPoint.getFirst() < 0 || startPoint.getSecond() < 0 ||
                startPoint.getFirst() >= matrix.length || startPoint.getSecond() >= matrix.length) {
            throw new IllegalArgumentException("Incorrect point specified '" + startPoint + "'");
        }

        queue.add(startPoint);


        while (!queue.isEmpty()) {

            Pair<Integer, Integer> curPoint = queue.poll();

            if (isFinalPoint(curPoint)) {
                return reconstructPath(curPoint);
            }

            int row = curPoint.getFirst();
            int col = curPoint.getSecond();

            checkPosition(row + 1, col, curPoint);
            checkPosition(row - 1, col, curPoint);
            checkPosition(row, col + 1, curPoint);
            checkPosition(row, col - 1, curPoint);

            visited.set(calculateIndex(curPoint));
        }


        return new ArrayList<>();
    }


    private void clearParents() {
        for (int i = 0; i < parent.length; i++) {
            parent[i] = -1;
        }
    }

    private boolean isMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            if (row == null || row.length != matrix.length) {
                return false;
            }
        }

        return true;
    }

    private List<Pair<Integer, Integer>> reconstructPath(Pair<Integer, Integer> point) {

        List<Pair<Integer, Integer>> path = new ArrayList<>();

        Pair<Integer, Integer> cur = point;

        while (cur != null) {

            path.add(cur);

            int parentIndex = parent[calculateIndex(cur)];

            if (parentIndex == -1) {
                cur = null;
            }
            else {
                cur = new Pair<Integer, Integer>(parentIndex / matrix.length, parentIndex % matrix.length);
            }
        }

        Collections.reverse(path);
        return path;
    }

    private int calculateIndex(Pair<Integer, Integer> point) {
        return point.getFirst() * rowLength + point.getSecond();
    }

    private void checkPosition(int row, int col, Pair<Integer, Integer> point) {

        if (row < 0 || col < 0 || row >= matrix.length || col >= matrix.length) {
            return;
        }

        if (matrix[row][col] == 1) {

            Pair<Integer, Integer> nextPoint = new Pair<>(row, col);

            int nextPointIndex = calculateIndex(nextPoint);

            if (visited.get(nextPointIndex) || parent[nextPointIndex] != -1) {
                return;
            }

            parent[nextPointIndex] = calculateIndex(point);

            queue.add(nextPoint);
        }
    }

    private boolean isFinalPoint(Pair<Integer, Integer> pair) {
        return pair.getFirst() == 0 || pair.getSecond() == 0 ||
                pair.getFirst() == matrix.length - 1 || pair.getSecond() == matrix.length - 1;
    }

}
