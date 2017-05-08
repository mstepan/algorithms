package com.max.algs.epi.searching;

import com.max.algs.ds.Pair;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 12.7. Find the min and max simultaneously.
 */
public class FindMinMaxSimultaneously {

    /**
     * time: O(N)
     * space: O(1)
     * <p>
     * comparisons count: 3 * (N/2)
     */
    public static Pair<Integer, Integer> finMinMax(int[] arr) {
        checkNotNull(arr);

        if (arr.length == 0) {
            return Pair.empty();
        }

        int minSoFar = Integer.MAX_VALUE;
        int maxSoFar = Integer.MIN_VALUE;

        for (int i = 1; i < arr.length; i += 2) {
            int minCur = arr[i - 1];
            int maxCur = arr[i];

            if (minCur > maxCur) {
                minCur = arr[i];
                maxCur = arr[i - 1];
            }

            minSoFar = Math.min(minSoFar, minCur);
            maxSoFar = Math.max(maxSoFar, maxCur);
        }

        // odd case
        if ((arr.length & 1) != 0) {
            minSoFar = Math.min(minSoFar, arr[arr.length - 1]);
            maxSoFar = Math.max(maxSoFar, arr[arr.length - 1]);
        }

        return Pair.of(minSoFar, maxSoFar);
    }

    private FindMinMaxSimultaneously() throws Exception {

        int[] arr = {5, 3, 3, 2, 12, 12, 8, 12, -6, -5, -33};

        System.out.println(finMinMax(arr));

        System.out.printf("'FindMinMaxSimultaneously' completed. java-%s %n", System.getProperty("java.version"));
    }

    public static void main(String[] args) {
        try {
            new FindMinMaxSimultaneously();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
