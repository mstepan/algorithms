package com.max.algs.creative;


import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Random;

import static com.google.common.base.Preconditions.checkNotNull;


final class FindValueWithIndexDoubleArray {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 6.19. Given an array of integers arr[0..n-1], such that, for all 0 <= i < n, we have
     * |A[i] - A[i+1]| <= 1. Let A [0] = x and A [n-1] = y, such that x < y. Design an efficient search
     * algorithm to find j such that A[j] = z for a given value z, x <= z <= y.
     * time: O(lgN)
     * space: O(1)
     */
    private static int findIndex(double[] arr) {
        checkNotNull(arr);

        int lo = 0;
        int hi = arr.length - 1;

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;

            int cmp = Double.compare(mid, arr[mid]);

            if (cmp == 0) {
                return mid;
            }

            if (cmp > 0) {
                // go left only
                hi = mid - 1;
            }
            else {
                // go right only
                lo = mid + 1;
            }
        }

        return -1;
    }

    private static int findIndexBruteforce(double[] arr) {
        for (int i = 0; i < arr.length; ++i) {
            if (Double.compare(arr[i], i) == 0) {
                return i;
            }
        }

        return -1;
    }

    private static final Random RAND = new Random();

    private static final double[] offsets = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0};

    private static double[] generate(double baseValue, int length) {
        double[] arr = new double[length];
        arr[0] = baseValue;

        double val = baseValue;

        for (int i = 0; i < arr.length; ++i) {
            val += offsets[RAND.nextInt(offsets.length)];
            arr[i] = BigDecimal.valueOf(val).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }

        return arr;
    }

    private FindValueWithIndexDoubleArray() {

        for (int i = 0; i < 100_000; ++i) {
            final int length = 100;

            double[] arr = generate(offsets[RAND.nextInt(offsets.length)], length);

            int index = findIndex(arr);
            int indexBruteforce = findIndexBruteforce(arr);

            if ( (index != -1 && Double.compare(index, arr[index]) != 0) ||
                    (index == -1 && indexBruteforce != -1)) {
                LOG.info(Arrays.toString(arr) + ", index = " + index + ", indexBruteforce = " + indexBruteforce);
                throw new AssertionError("Error occurred");
            }
        }

        LOG.info("FindValueWithIndexDoubleArray done...");
    }


    public static void main(String[] args) {
        try {
            new FindValueWithIndexDoubleArray();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }


}
