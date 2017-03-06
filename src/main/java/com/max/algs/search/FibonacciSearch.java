package com.max.algs.search;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Fibonacci search. See: https://en.wikipedia.org/wiki/Fibonacci_search_technique
 * time: O(lgN)
 * space: O(1)
 */
public final class FibonacciSearch {

    private FibonacciSearch() {
        throw new IllegalStateException("Can't instantiate utility class ");
    }

    public static int fibSearch(int[] arr, int value) {

        checkArgument(arr != null, "null 'arr' passed");

        int f1 = 0;
        int f2 = 1;

        while (f2 <= arr.length) {
            int temp = f2;
            f2 += f1;
            f1 = temp;
        }

        int offset = 0;
        int prev;

        while (f2 > 1) {

            int mid = Math.min(offset + f1 - 1, arr.length - 1);

            if (arr[mid] == value) {
                return mid;
            }

            if (value < arr[mid]) {
                prev = f2 - f1;
                f2 = f1;
                f1 = prev;
            }
            else {
                offset = mid + 1;
                prev = f2 - f1;
                f1 = prev == 0 ? 0 : f1 - prev;
                f2 = prev;
            }
        }

        return -1;
    }

}
