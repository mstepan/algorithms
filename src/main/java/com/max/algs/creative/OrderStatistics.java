package com.max.algs.creative;

import com.max.algs.util.ArrayUtils;
import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.Random;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

final class OrderStatistics {


    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * time: O(N)
     * space: O(1)
     */
    private static int orderStatistics(int[] arr, int order) {
        checkNotNull(arr);
        checkArgument(order >= 0 && order < arr.length);

        int lo = 0;
        int hi = arr.length - 1;

        int searchOrder = order;

        while (lo < hi) {

            int index = partition(arr, lo, hi);

            int pivotOrder = index - lo;

            if (pivotOrder == searchOrder) {
                return arr[index];
            }

            if (pivotOrder > searchOrder) {
                hi = index - 1;
            }
            else {
                lo = index + 1;
                searchOrder -= (pivotOrder + 1);
            }
        }

        return arr[lo];
    }

    private static final Random RAND = new Random();

    /**
     * time: O(N)
     * space: O(1)
     */
    private static int partition(int[] arr, int lo, int hi) {

        final int randIndex = lo + RAND.nextInt(hi - lo + 1);
        ArrayUtils.swap(arr, randIndex, hi);

        final int pivot = arr[hi];
        int less = lo - 1;

        for (int i = lo; i < hi; ++i) {
            if (arr[i] <= pivot) {
                ArrayUtils.swap(arr, less + 1, i);
                ++less;
            }
        }

        ArrayUtils.swap(arr, less + 1, hi);
        return less + 1;
    }

    private OrderStatistics() {

        for (int it = 0; it < 10; ++it) {
            final int[] arr1 = ArrayUtils.generateRandomArray(10_000);
            final int[] arr2 = Arrays.copyOf(arr1, arr1.length);
            Arrays.sort(arr2);

            for (int k = 0; k < arr1.length; ++k) {
                int value = orderStatistics(arr1, k);

                if (value != arr2[k]) {
                    throw new AssertionError("Incorrect order statistics returned: expected = " +
                            arr2[k] + ", actual = " + value);
                }
            }
        }

        LOG.info("OrderStatistics done...");
    }


    public static void main(String[] args) {
        try {
            new OrderStatistics();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }


}
