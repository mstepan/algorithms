package com.max.algs.search;

public final class UnboundedBinarySearch {


    private UnboundedBinarySearch() {
        throw new IllegalStateException("Can't instantiate utility class '" + UnboundedBinarySearch.class + "'");
    }

    /**
     * Binary search for an array with an unknown length.
     * <p>
     * time: O(lgN)
     * space: O(1)
     */
    public static int find(int[] arr, int value) {

        if (arr == null) {
            throw new IllegalArgumentException("NULL array passed");
        }
        int lo = 0;
        int hi = 0;

        for (int i = 0; ; i++) {

            lo = hi;
            hi = 1 << i;

            try {
                if (arr[hi] == value) {
                    return hi;
                }

                if (arr[hi] > value) {
                    --hi;
                    break;
                }
            }
            catch (IndexOutOfBoundsException ex) {
                break;
            }
        }

        int mid = 0;

        while (lo <= hi) {
            try {
                mid = lo + ((hi - lo) >>> 1);

                if (arr[mid] == value) {
                    return mid;
                }

                if (arr[mid] < value) {
                    lo = mid + 1;
                }
                else {
                    hi = mid - 1;
                }
            }
            catch (IndexOutOfBoundsException ex) {
                hi = mid - 1;
            }
        }

        return -1;
    }

}
