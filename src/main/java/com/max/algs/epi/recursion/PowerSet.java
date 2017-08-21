package com.max.algs.epi.recursion;

import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Generate power set from array.
 */
public final class PowerSet {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * time: O(2^n), can be reduced to O(N)
     * space: O(2^n), can be reduced to O(N)
     */
    private static List<List<Integer>> generatePowerSet(int[] arr) {
        checkNotNull(arr, "null 'arr' passed");

        List<List<Integer>> res = new ArrayList<>();

        int boundary = 1 << arr.length;

        for (int i = 0; i < boundary; ++i) {
            res.add(generateFromBitMask(arr, i));
        }

        return res;
    }

    private static List<Integer> generateFromBitMask(int[] arr, int bitMask) {

        List<Integer> subset = new ArrayList<>();

        for (int i = 0, mask = bitMask; mask != 0 && i < arr.length; ++i, mask >>= 1) {
            if ((mask & 1) != 0) {
                subset.add(arr[i]);
            }
        }

        return subset;
    }

    private PowerSet() {

        List<List<Integer>> res = generatePowerSet(new int[]{1, 2, 3, 4, 5});

        for (List<Integer> subset : res) {
            LOG.info(subset);
        }

        LOG.info("PowerSet done...");
    }

    public static void main(String[] args) {
        try {
            new PowerSet();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

}
