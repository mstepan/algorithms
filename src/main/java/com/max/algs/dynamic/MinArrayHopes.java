package com.max.algs.dynamic;

import java.util.Arrays;

/**
 * Given an array, you should start at index 0, and you can jump
 * from the current index to a max of " current index + arr[current index]
 * and make it out of the array at the other end in minimum number of hops.
 *
 * @author Maksym Stepanenko
 */
public final class MinArrayHopes {


    private MinArrayHopes() {
        throw new IllegalStateException("Can't instantiate utility class '" + MinArrayHopes.class.getName() + "'");
    }

    /**
     * time: O(N^2)
     * space: O(N)
     */
    public static int findMinHopes(int[] arr) {

        if (arr == null) {
            throw new IllegalArgumentException();
        }

        if (arr.length == 0) {
            return 0;
        }

        int[] res = new int[arr.length];

        Arrays.fill(res, 1, arr.length, Integer.MAX_VALUE);

        for (int i = 0; i < arr.length; i++) {

            for (int j = 1; j <= arr[i]; j++) {
                int index = i + j;

                if (index >= arr.length) {
                    return res[i] + 1;
                }

                res[index] = Math.min(res[index], res[i] + 1);
            }
        }

        return -1;
    }

}
