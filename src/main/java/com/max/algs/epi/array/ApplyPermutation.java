package com.max.algs.epi.array;

import com.max.algs.util.ArrayUtils;
import org.apache.log4j.Logger;

import java.util.Arrays;


public class ApplyPermutation {

    private static final Logger LOG = Logger.getLogger(ApplyPermutation.class);

    private ApplyPermutation() throws Exception {

        int[] arr = {1, 2, 3, 4};
        int[] p = {2, 1, 3, 0};

        applyPermutation(arr, p);

        // {4, 2, 1, 3} after applying permutation
        System.out.println(Arrays.toString(arr));

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * Apply permutation represented by 'p' to array 'arr'.
     * <p>
     * time: O(N)
     * space: O(1)
     */
    public static void applyPermutation(int[] arr, int[] p) {
        int n = p.length;

        for (int i = 0; i < arr.length; ++i) {

            if (p[i] >= 0) {
                int next = i;

                while (p[next] >= 0) {
                    ArrayUtils.swap(arr, p[next], i);
                    int tempNext = p[next];
                    p[next] -= n;
                    next = tempNext;
                }
            }
        }

        for (int i = 0; i < p.length; ++i) {
            p[i] += n;
        }
    }

    public static void main(String[] args) {
        try {
            new ApplyPermutation();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

}
