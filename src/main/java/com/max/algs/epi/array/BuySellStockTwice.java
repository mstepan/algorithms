package com.max.algs.epi.array;

import com.max.algs.util.ArrayUtils;

import java.util.Arrays;
import java.util.Random;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Max profit that can be obtained buying and selling stocks twice.
 */
public class BuySellStockTwice {

    private BuySellStockTwice() throws Exception {

        Random rand = new Random();

        for (int i = 0; i < 1000; ++i) {
            int[] arr1 = ArrayUtils.generateRandomArray(1 + rand.nextInt(10_000), 1000);
            int[] arr2 = Arrays.copyOf(arr1, arr1.length);

            int maxProfit = maxProfitWithTwoTransactions(arr1);
            checkAllElementsPositive(arr1);

            int maxProfitBruteforce = maxProfitWithTwoTransactionsBruteforce(arr2);

            if (maxProfit != maxProfitBruteforce) {
                throw new IllegalStateException("Results arent equals for array: " +
                        Arrays.toString(arr2) +
                        ", maxProfit = " + maxProfit +
                        ", maxProfitB = " + maxProfitBruteforce);
            }
        }

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * time: O(N^2)
     * space: O(1)
     */
    public static int maxProfitWithTwoTransactionsBruteforce(int[] arr) {
        checkNotNull(arr);

        if (arr.length < 4) {
            return 0;
        }

        int maxProfit = 0;

        for (int i = 1; i < arr.length - 2; ++i) {
            int leftProfit = singleProfit(arr, 0, i);
            int rightProfit = singleProfit(arr, i + 1, arr.length - 1);
            if (leftProfit > 0 && rightProfit > 0) {
                maxProfit = Math.max(maxProfit, leftProfit + rightProfit);
            }
        }

        return maxProfit;
    }

    private static int singleProfit(int[] arr, int from, int to) {

        int minValue = Integer.MAX_VALUE;
        int maxProfit = 0;

        for (int i = from; i <= to; ++i) {
            if (arr[i] <= minValue) {
                minValue = arr[i];
            }
            else {
                maxProfit = Math.max(maxProfit, arr[i] - minValue);
            }
        }
        return maxProfit;
    }

    /**
     * Highly optimized solution.
     * <p>
     * time: O(N)
     * space: O(1)
     */
    public static int maxProfitWithTwoTransactions(int[] arr) {
        checkNotNull(arr);

        if (arr.length < 4) {
            return 0;
        }

        int profitRight = findProfitRight(arr);

        // stocks strictly decreasing
        if (profitRight == 0) {
            restoreAllElements(arr);
            return 0;
        }

        int boundary = 2;
        int maxProfit = 0;

        int minLeft = Integer.MAX_VALUE;
        int profitLeft = 0;

        int maxRight;

        for (int j = 0; j < arr.length - 2; ++j) {

            if (j == boundary) {
                if (arr[j] < 0) {
                    arr[j] = -arr[j];
                }

                ++boundary;

                while (boundary < arr.length && arr[boundary] < 0) {
                    arr[boundary] = -arr[boundary];
                    ++boundary;
                }

                if (boundary == arr.length) {
                    return maxProfit;
                }

                maxRight = -findFirstNegative(arr, boundary);

                profitRight = maxRight - arr[boundary];
            }
            if (arr[j] < minLeft) {
                minLeft = arr[j];
            }
            else {
                profitLeft = Math.max(profitLeft, arr[j] - minLeft);
            }

            if (profitLeft > 0) {
                maxProfit = Math.max(maxProfit, profitLeft + profitRight);
            }
        }

        restoreAllElements(arr);

        return maxProfit;
    }

    private static int findFirstNegative(int[] arr, int index) {
        for (int i = index; i < arr.length; ++i) {
            if (arr[i] < 0) {
                return arr[i];
            }
        }

        return -1;
    }

    private static void restoreAllElements(int[] arr) {
        for (int k = 0; k < arr.length; ++k) {
            if (arr[k] < 0) {
                arr[k] = -arr[k];
            }
        }
    }

    private static int findProfitRight(int[] arr) {
        int profitRight = 0;
        int maxRight = Integer.MIN_VALUE;

        for (int i = arr.length - 1; i >= 2; --i) {
            if (arr[i] > maxRight) {
                maxRight = arr[i];
                arr[i] = -arr[i];
            }
            else {
                profitRight = Math.max(profitRight, maxRight - arr[i]);
            }
        }

        return profitRight;
    }

    private static void checkAllElementsPositive(int[] arr) {
        for (int i = 0; i < arr.length; ++i) {
            if (arr[i] < 0) {
                throw new IllegalArgumentException("Negative value found: " + arr[i]);
            }
        }
    }

}
