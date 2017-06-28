package com.max.algs.epi.searching;


import org.apache.log4j.Logger;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 12.1. Variant.
 * Find local minimum in the unsorted array.
 */
public class FindLocalMinimum {

    private static final Logger LOG = Logger.getLogger(FindLocalMinimum.class);

    private FindLocalMinimum() throws Exception {

        int[] arr = {15, 7, 12, 23, 45, 86, 86, 86, 86, 113, 115};

        int index = findLocalMinimumIndex(arr);

        if (index < 0) {
            System.out.println("Not found");
        }
        else {
            System.out.printf("arr[%d] = %d %n", index, arr[index]);
        }

        System.out.printf("'FindFirstLargerThanKey' completed. java-%s %n", System.getProperty("java.version"));
    }

    /**
     * time: O(lgN)
     * space: O(1)
     */
    private static int findLocalMinimumIndex(int[] arr) {
        checkNotNull(arr);

        if (arr.length < 3) {
            return -1;
        }

        int last = arr.length - 1;

        // arr[0] >= arr[1] AND arr[n-1] >= arr[n-2]
        checkArgument(arr[0] >= arr[1] && arr[last] >= arr[last - 1], "Incorrect array detected");

        int lo = 0;
        int hi = arr.length - 1;

        while (true) {

            int elementsCount = hi - lo + 1;

            if (elementsCount == 2) {
                return arr[lo] <= arr[hi] ? lo : hi;
            }

            int mid = lo + (hi - lo) / 2;

            // found local minimum
            if (arr[mid] <= arr[mid - 1] && arr[mid] <= arr[mid + 1]) {
                return mid;
            }

            int leftVal = arr[mid - 1];
            int midVal = arr[mid];
            int rightVal = arr[mid + 1];

            // go left
            if (leftVal < midVal || (leftVal == midVal && rightVal > midVal)) {
                hi = mid - 1;
            }
            // go right
            else {
                lo = mid + 1;
            }

            assert elementsCount > (hi - lo) + 1 : "Number of elements wasn't decreased";
        }
    }

    public static void main(String[] args) {
        try {
            new FindLocalMinimum();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

}
