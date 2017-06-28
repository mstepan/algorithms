package com.max.algs.epi.sorting;


import org.apache.log4j.Logger;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public final class IntersectSortedArrays {

    private static final Logger LOG = Logger.getLogger(IntersectSortedArrays.class);

    private IntersectSortedArrays() throws Exception {

        int[] arr1 = {3, 5, 5, 7, 10, 12};
        int[] arr2 = {2, 5, 5, 5, 8, 10, 14, 19, 22, 33};

        Set<Integer> res = intersection(arr1, arr2);

        LOG.info(res);
    }

    /**
     * N - arr1.length
     * M - arr2.length
     * <p>
     * K - intersection size (min(N, M))
     * <p>
     * <p>
     * time: O(N+M)
     * space: O(K)
     */
    public static Set<Integer> intersection(int[] arr1, int[] arr2) {
        checkNotNull(arr1);
        checkNotNull(arr2);

        Set<Integer> res = new LinkedHashSet<>();

        int i = 0;
        int j = 0;

        while (i < arr1.length && j < arr2.length) {

            int cmp = Integer.compare(arr1[i], arr2[j]);

            if (cmp == 0) {
                res.add(arr1[i]);
                ++i;
                ++j;
            }
            else if (cmp < 0) {
                ++i;
            }
            else {
                ++j;
            }
        }

        return res;
    }

    public static void main(String[] args) {
        try {
            new IntersectSortedArrays();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}

