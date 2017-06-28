package com.max.algs.epi.array;

import org.apache.log4j.Logger;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;


public class ComputeNextPermutation {

    private static final Logger LOG = Logger.getLogger(ComputeNextPermutation.class);

    private ComputeNextPermutation() throws Exception {

        int[] arr = {6, 2, 1, 5, 4, 3, 0};
        System.out.println(Arrays.toString(arr));
        nextPermutation(arr);
        System.out.println(Arrays.toString(arr));

//        while (nextPermutation(arr)) {
//            ++permCount;
//        }

//        System.out.printf("permCount = %d %n", permCount);

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * 6.10. Compute the next permutation.
     * time: O(N)
     * space: O(1)
     */
    public static boolean nextPermutation(int[] arr) {
        checkNotNull(arr);
        checkArgument(arr.length > 0);

        int i = lastIncreasedFromRight(arr);

        if (i == 0) {
            return false;
        }

        int pivotIndex = i - 1;
        putPivotToPlace(arr, pivotIndex);

        revert(arr, i, arr.length - 1);

//        System.out.println(Arrays.toString(arr));

        return true;
    }

    private static void putPivotToPlace(int[] arr, int pivotIndex) {
        int pivot = arr[pivotIndex];

        for (int j = arr.length - 1; j >= pivotIndex + 1; --j) {
            if (arr[j] > pivot) {
                swap(arr, j, pivotIndex);
                break;
            }
        }
    }

    private static int lastIncreasedFromRight(int[] arr) {
        int i = arr.length - 1;

        while (i > 0) {
            if (arr[i - 1] < arr[i]) {
                return i;
            }
            --i;
        }

        return 0;
    }

    private static void revert(int[] arr, int from, int to) {
        int left = from;
        int right = to;

        while (left < right) {
            swap(arr, left, right);
            ++left;
            --right;
        }
    }

    private static void swap(int[] arr, int from, int to) {
        int temp = arr[from];
        arr[from] = arr[to];
        arr[to] = temp;
    }

    public static void main(String[] args) {
        try {
            new ComputeNextPermutation();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}
