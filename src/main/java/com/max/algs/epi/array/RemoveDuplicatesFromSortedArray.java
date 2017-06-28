package com.max.algs.epi.array;

import org.apache.log4j.Logger;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkNotNull;


public final class RemoveDuplicatesFromSortedArray {

    private static final Logger LOG = Logger.getLogger(RemoveDuplicatesFromSortedArray.class);

    private RemoveDuplicatesFromSortedArray() throws Exception {
        int[] arr = {-5, -5, 2, 3, 5, 5, 7, 7, 8, 11, 11, 11, 11, 13, 13};

        System.out.printf("before: %s %n", Arrays.toString(arr));

        int zeroIndex = removeDuplicatesFromSortedArray(arr);

        System.out.printf("after: %s %n", Arrays.toString(arr));
        System.out.printf("zeroIndex = %d %n", zeroIndex);

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * Remove duplicates from sorted array.
     * <p>
     * time: O(N)
     * space: O(1)
     */
    public static int removeDuplicatesFromSortedArray(int[] arr) {

        checkNotNull(arr);

        if (arr.length < 2) {
            return arr.length;
        }

        // 2, 3, 5, 5, 7, 7, 8, 11, 11, 11, 11, 13

        int prev = arr[0];
        int index = 1;

        for (int i = 1; i < arr.length; ++i) {

            if (arr[i] < arr[i - 1]) {
                throw new IllegalArgumentException("Array is not sorted");
            }

            if (arr[i] != prev) {
                arr[index] = arr[i];
                prev = arr[i];
                ++index;
            }
        }

        for (int i = index; i < arr.length; ++i) {
            arr[i] = 0;
        }

        return index;
    }

    public static void main(String[] args) {
        try {
            new RemoveDuplicatesFromSortedArray();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}
