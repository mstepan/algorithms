package com.max.algs.dynamic;

import com.max.algs.util.MathUtils;

/**
 * Count number of subsets that have specified xor value using dynamic programming.
 */
public final class SubsetsCountWithXorValue {

    private SubsetsCountWithXorValue(){
        throw new IllegalStateException("Can't instantiate utility class");
    }

    public static int countSubsetsOfXor(int[] arr, int searchValue) {

        int maxPossibleValue = calculateMaxPossibleValue(arr);

        int[][] dp = new int[arr.length + 1][maxPossibleValue + 1];

        dp[0][0] = 1;

        for (int row = 1; row < dp.length; row++) {
            for (int col = 0; col < dp[row].length; col++) {
                dp[row][col] = dp[row - 1][col] + dp[row - 1][col ^ arr[row - 1]];
            }
        }

        return dp[dp.length - 1][searchValue];

    }

    private static int calculateMaxPossibleValue(int[] arr) {

        int maxValue = arr[0];

        for (int value : arr) {
            maxValue = Math.max(maxValue, value);
        }

        int value = (int) MathUtils.log2(maxValue) + 1;
        return (1 << value) - 1;
    }

}
