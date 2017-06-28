package com.max.algs.epi.searching;


import org.apache.log4j.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 12.1. Variant.
 * Finds the index of the first occurrence of an element greater than that key.
 */
public class FindFirstLargerThanKey {

    private static final Logger LOG = Logger.getLogger(FindFirstLargerThanKey.class);

    private FindFirstLargerThanKey() throws Exception {

        int[] arr = {5, 7, 12, 23, 45, 86, 86, 86, 86, 113, 115};

        int index = findFirstGreaterIndex(arr, 2);

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
    private static int findFirstGreaterIndex(int[] arr, int value) {
        checkNotNull(arr);

        int lo = 0;
        int hi = arr.length - 1;
        int index = -1;

        while (lo <= hi) {

            int mid = lo + (hi - lo) / 2;

            assert mid >= lo && mid <= hi : "incorrect mid value";

            if (arr[mid] > value) {
                index = mid;
                hi = mid - 1;
            }
            else {
                lo = mid + 1;
            }
        }

        return index;
    }

    public static void main(String[] args) {
        try {
            new FindFirstLargerThanKey();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

}
