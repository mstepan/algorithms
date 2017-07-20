package com.max.algs.dynamic;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Given an array of non-negative integers and a positive number 'X',
 * determine if there exists a subset of the elements of array with sum equal to 'X'.
 * <p>
 */
public class SubsetSum {

    private static final Logger LOG = Logger.getLogger(SubsetSum.class);

    /**
     * N - arr.length
     * K - 'sum' value
     * <p>
     * time: O(N*K)
     * space: O(N*K)
     */
    public static boolean subsetSumDynamic(int[] arr, int sum) {
        checkPreconditions(arr, sum);

        int rows = sum + 1;
        int cols = arr.length + 1;

        assert rows > 0 && cols > 0;

        boolean[][] sol = new boolean[rows][cols];
        sol[0][0] = true;

        // fill 1-st row
        for (int col = 1; col < cols; ++col) {
            sol[0][col] = true;
        }

        for (int row = 1; row < rows; ++row) {
            for (int col = 1; col < cols; ++col) {

                int val = arr[col - 1];

                // curSum == row
                sol[row][col] = sol[row][col - 1] || (val <= row && sol[row - val][col - 1]);
            }
        }

        boolean canFormSum = sol[rows - 1][cols - 1];

        if (canFormSum) {
            List<Integer> subset = reconstructSubset(sol, arr);
            LOG.info("subset: " + subset);
        }

        return canFormSum;
    }

    /**
     * N - arr.length
     * <p>
     * time: O(N)
     * space: O(N)
     */
    private static List<Integer> reconstructSubset(boolean[][] sol, int[] arr) {

        int row = sol.length - 1;
        int col = sol[row].length - 1;

        List<Integer> subset = new ArrayList<>();

        while (row != 0) {

            assert sol[row][col] : "Somehow we reached a cell with 'false' value.";

            // skip cur element
            if (sol[row][col - 1]) {
                --col;
            }

            // add element to subset
            else {

                int val = arr[col - 1];

                subset.add(val);
                --col;
                row -= val;
            }

            assert row >= 0 && col >= 0 : "'row' or 'col' is negative";
        }

        assert !subset.isEmpty() : "empty subset returned";

        return subset;
    }

    /**
     * Recursive bruteforce solution.
     * <p>
     * time: O(2^N)
     * space: O(N)
     */
    public static boolean subsetSumBruteforce(int[] arr, int sum) {
        checkPreconditions(arr, sum);
        return subsetSumRec(sum, arr, 0, 0);
    }

    private static boolean subsetSumRec(int expSum, int[] arr, int curSum, int index) {
        if (expSum == curSum) {
            return true;
        }

        if (index == arr.length) {
            return false;
        }

        // add arr[index] for subset sum
        boolean res1 = subsetSumRec(expSum, arr, curSum + arr[index], index + 1);

        // skip arr[index] for subset sum
        boolean res2 = subsetSumRec(expSum, arr, curSum, index + 1);

        return res1 || res2;
    }

    private static void checkPreconditions(int[] arr, int sum) {
        checkNotNull(arr, "Array is null");
        checkArgument(sum >= 0, "sum = " + sum + " is negative.");

        for (int i = 0; i < arr.length; ++i) {
            checkArgument(arr[i] >= 0, "arr[" + i + "] = " + arr[i] + " is negative value.");
        }
    }

    private SubsetSum() {

        int[] arr = {8, 7, 3, 5, 10};
        int[] sums = {11, 15, 18, 9, 6, 14};

        for (int sum : sums) {
            LOG.info("subsetSumBruteforce(" + sum + "): " + subsetSumBruteforce(arr, sum) +
                    ", subsetSumDynamic(" + sum + "): " + subsetSumDynamic(arr, sum));
        }


        LOG.info("SubsetSum done: java-" + System.getProperty("java.version"));
    }

    public static void main(String[] args) {
        try {
            new SubsetSum();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

}
