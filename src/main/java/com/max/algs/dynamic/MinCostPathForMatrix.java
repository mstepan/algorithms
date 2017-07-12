package com.max.algs.dynamic;

import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Write a function that return the minimum cost of moving from the top-left cell
 * to bottom-right cell of a 2-D array.
 */
public class MinCostPathForMatrix {

    private static final Logger LOG = Logger.getLogger(MinCostPathForMatrix.class);

    private static int callsCount = 0;

    private static final Map<String, Integer> CACHE = new HashMap<>();

    private static int minCostRecWithMemoization(int[][] cost, int row, int col) {

        ++callsCount;

        String key = row + ";" + col;

        if (CACHE.containsKey(key)) {
            return CACHE.get(key);
        }

        int minCost;

        if (row == 0 && col == 0) {
            minCost = cost[row][col];
        }

        // first row
        else if (row == 0) {
            minCost = minCostRecWithMemoization(cost, row, col - 1) + cost[row][col];
        }

        // first column
        else if (col == 0) {
            minCost = minCostRecWithMemoization(cost, row - 1, col) + cost[row][col];
        }

        else {
            minCost = Math.min(minCostRecWithMemoization(cost, row - 1, col), minCostRecWithMemoization(cost, row, col - 1)) + cost[row][col];
        }

        CACHE.putIfAbsent(key, minCost);

        return CACHE.get(key);
    }

    /**
     * N - rows count
     * M - columns count
     * <p>
     * time: O(N*M)
     * space: O(N)
     */
    private static int minCostDynamic(int[][] cost) {

        final int cols = cost[0].length;

        int[] prevRow = new int[cols];
        prevRow[0] = cost[0][0];

        // fill 0-row
        for (int col = 1; col < prevRow.length; ++col) {
            prevRow[col] = prevRow[col - 1] + cost[0][col];
        }

        int[] curRow = new int[cols];

        for (int row = 1; row < cost.length; ++row) {

            Arrays.fill(curRow, 0);
            curRow[0] = prevRow[0] + cost[row][0];

            for (int col = 1; col < cols; ++col) {
                curRow[col] = Math.min(prevRow[col], curRow[col - 1]) + cost[row][col];
            }

            System.arraycopy(curRow, 0, prevRow, 0, prevRow.length);
        }

        return curRow[curRow.length - 1];
    }

    private MinCostPathForMatrix() {

        int[][] cost = {
                {1, 3, 5, 8},
                {4, 2, 1, 7},
                {4, 3, 2, 3}
        };

        int minCost = minCostRecWithMemoization(cost, cost.length - 1, cost[0].length - 1);

        LOG.info("minCost: " + minCost + ", callsCount: " + callsCount);
        LOG.info("minCostDynamic: " + minCostDynamic(cost));

        LOG.info("MinCostPathForMatrix done: java-" + System.getProperty("java.version"));
    }


    public static void main(String[] args) {
        try {
            new MinCostPathForMatrix();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}
