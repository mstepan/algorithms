package com.max.algs.epi.array;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Buy and sell stock once.
 */
public class BuySellStockOnce {

    /**
     * time: O(N)
     * space: O(1)
     */
    private static void findMaxProfit(int[] stocks) {
        checkNotNull(stocks);

        if (stocks.length < 2) {
            return;
        }

        checkValidStocks(stocks);

        int minValue = stocks[0];
        int minIndex = 0;

        int maxProfit = 0;
        int[] maxProfitPair = {0, 0};

        int curProfit, singleStock;

        for (int i = 1, stocksLength = stocks.length; i < stocksLength; ++i) {

            singleStock = stocks[i];

            if (singleStock < minValue) {
                minIndex = i;
                minValue = singleStock;
            }
            else {

                curProfit = singleStock - minValue;

                if (curProfit > maxProfit) {
                    maxProfit = curProfit;
                    maxProfitPair[0] = stocks[minIndex];
                    maxProfitPair[1] = singleStock;
                }
            }
        }

        System.out.printf("maxProfit = %d , buy = %d, sell = %d%n",
                maxProfit,
                maxProfitPair[0],
                maxProfitPair[1]);
    }

    private static void checkValidStocks(int[] stocks) {
        for (int value : stocks) {
            if (value < 0) {
                throw new IllegalArgumentException("Incorrect stock value detected: " + value + ", should be positive value.");
            }
        }
    }

    /**
     * Variant: fina max subarray with all elements equal.
     * <p>
     * time: O(N)
     * space: O(1)
     */
    public static int[] maxSubarrWithSameValues(int[] arr) {
        checkNotNull(arr);

        if (arr.length < 2) {
            return Arrays.copyOf(arr, arr.length);
        }

        int maxLength = 1;
        int curLength = 1;

        int maxFrom = 0;
        int maxTo = 0;

        for (int i = 1; i < arr.length; ++i) {
            if (arr[i] == arr[i - 1]) {
                ++curLength;

                if (curLength > maxLength) {
                    maxLength = curLength;

                    maxFrom = i - curLength + 1;
                    maxTo = i;
                }
            }
            else {
                curLength = 1;
            }
        }

        return Arrays.copyOfRange(arr, maxFrom, maxTo + 1);

    }
}
