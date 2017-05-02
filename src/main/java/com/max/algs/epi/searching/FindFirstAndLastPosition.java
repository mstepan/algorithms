package com.max.algs.epi.searching;


import com.max.algs.ds.Pair;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 12.1. Variant.
 * Finds first and last index of a key.
 */
public class FindFirstAndLastPosition {

    private static final Pair<Integer, Integer> NOT_FOUND = new Pair<>(-1, -1);

    private enum Order {
        FIRST, LAST
    }

    /**
     * time: O(lgN)
     * space: O(1)
     */
    private static Pair<Integer, Integer> findFirstAndLastIndex(int[] arr, int value) {
        checkNotNull(arr);

        int left = findOccurrence(arr, value, Order.FIRST);

        if (left == -1) {
            return NOT_FOUND;
        }

        int right = findOccurrence(arr, value, Order.LAST);

        assert left <= right;

        return right == -1 ? NOT_FOUND : new Pair<>(left, right);
    }

    private static int findOccurrence(int[] arr, int value, Order order) {

        assert arr != null : "null 'arr' detected";

        int lo = 0;
        int hi = arr.length - 1;
        int index = -1;

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;

            if (arr[mid] == value) {
                index = mid;

                if (order == Order.FIRST) {
                    hi = mid - 1;
                }
                else {
                    lo = mid + 1;
                }
            }
            else if (arr[mid] < value) {
                lo = mid + 1;
            }
            else {
                hi = mid - 1;
            }
        }

        return index;
    }

    private FindFirstAndLastPosition() throws Exception {

        int[] arr = {5, 5, 7, 12, 23, 45, 86, 86, 86, 86, 113, 115};

        Pair<Integer, Integer> indexes = findFirstAndLastIndex(arr, 86);

        System.out.printf("indexes: %s %n", indexes);


        System.out.printf("'FindFirstLargerThanKey' completed. java-%s %n", System.getProperty("java.version"));
    }

    public static void main(String[] args) {
        try {
            new FindFirstAndLastPosition();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
