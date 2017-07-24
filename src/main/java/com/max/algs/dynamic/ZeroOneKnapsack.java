package com.max.algs.dynamic;

import com.max.algs.AlgorithmsMain;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;

/**
 * 0-1 Knapsack problem.
 */
public class ZeroOneKnapsack {


    private static final Logger LOG = Logger.getLogger(AlgorithmsMain.class);

    /**
     * Dynamic programming solution.
     * <p>
     * K = knapsackSize
     * N = weights.length
     * <p>
     * time: O(N*K)
     * space: O(N*K)
     */
    private static void maxValueDynamic(int[] weights, int[] values, int knapsackSize) {

        int rows = knapsackSize + 1;
        int cols = weights.length + 1;

        // 1st row is all zeros, left column is all zeros too
        int[][] optProfit = new int[rows][cols];

        for (int row = 1; row < rows; ++row) {
            for (int col = 1; col < cols; ++col) {

                optProfit[row][col] = optProfit[row][col - 1];

                int curWeight = weights[col - 1];
                int curValue = values[col - 1];

                if (curWeight <= row) {
                    optProfit[row][col] = Math.max(optProfit[row][col],
                            curValue + optProfit[row - curWeight][col - 1]);
                }
            }
        }

        // reconstruct solution, time: O(N), space: O(N)
        List<Integer> solWeights = new ArrayList<>();
        int row = rows - 1;
        int col = cols - 1;

        while (row > 0 && col > 0) {
            // skip current element
            if (optProfit[row][col] == optProfit[row][col - 1]) {
                --col;
            }
            // add current element to knapsack solution
            else {
                int curWeight = weights[col - 1];
                solWeights.add(curWeight);
                row = row - curWeight;
                --col;
            }
        }

        LOG.info("maxValueDynamic: " + solWeights + " , value = " + optProfit[rows - 1][cols - 1]);
    }

    /**
     * Recursive solution without memoization.
     * <p>
     * K = knapsackSize
     * N = weights.length
     * <p>
     * time: O(2^N)
     * space: O(N)
     */

    private static void maxValueBruteforce(int[] weights, int[] values, int knapsackSize) {

        KnapsackPartial maxProfit = maxValueRec(weights, values, knapsackSize, new BitSet(weights.length), 0);

        LOG.info("maxValueBruteforce: weight = " + maxProfit.weights + ", value = " + maxProfit.value);
    }

    private static KnapsackPartial maxValueRec(int[] weights, int[] values, int knapsackSize, BitSet leftItems,
                                               int usedItemsCount) {

        // all items in use or knapsack os full
        if (usedItemsCount == weights.length || knapsackSize == 0) {
            return new KnapsackPartial(Collections.emptyList(), 0);
        }

        KnapsackPartial maxProfit = new KnapsackPartial(Collections.emptyList(), 0);

        for (int i = 0; i < weights.length; ++i) {

            // item not used yet
            if (!leftItems.get(i)) {
                int curWeight = weights[i];
                int curValue = values[i];

                // fit for current knapsack size
                if (curWeight <= knapsackSize) {
                    leftItems.set(i);

                    KnapsackPartial prev = maxValueRec(weights, values, knapsackSize - curWeight, leftItems,
                            usedItemsCount + 1);

                    List<Integer> recWeightsSol = new ArrayList<>(prev.weights);
                    recWeightsSol.add(curWeight);

                    KnapsackPartial curProfit = new KnapsackPartial(recWeightsSol, curValue + prev.value);

                    if (curProfit.value > maxProfit.value) {
                        maxProfit = curProfit;
                    }

                    leftItems.clear(i);
                }
            }
        }

        return maxProfit;
    }

    private static final class KnapsackPartial {
        final List<Integer> weights;
        final int value;

        KnapsackPartial(List<Integer> weights, int value) {
            this.weights = weights;
            this.value = value;
        }
    }

    private ZeroOneKnapsack() {

        int[] weights = {2, 3, 4, 5};
        int[] values = {3, 4, 5, 6};

        int knapsackSize = 5;

        maxValueBruteforce(weights, values, knapsackSize);
        maxValueDynamic(weights, values, knapsackSize);

        LOG.info("ZeroOneKnapsack done: java-" + System.getProperty("java.version"));
    }

    public static void main(String[] args) {
        try {
            new ZeroOneKnapsack();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}
