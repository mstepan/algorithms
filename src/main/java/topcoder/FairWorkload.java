package topcoder;


import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class FairWorkload {


    private FairWorkload() throws Exception {

        int[] arr = {10, 20, 30, 40, 50, 60, 70, 80, 90};
        int workers = 5;

        int optimalLoad = findOptimalLoad(arr, workers);

        System.out.printf("optimalLoad: %d %n", optimalLoad);

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * See https://www.topcoder.com/community/data-science/data-science-tutorials/binary-search/
     * and https://community.topcoder.com/stat?c=problem_statement&pm=1901&rd=4650
     */
    public static int findOptimalLoad(int[] arr, int workers) {
        checkNotNull(arr);
        checkArgument(workers > 0 && workers < 1_000_000);

        int lo = findMax(arr);
        int hi = sum(arr);
        int optimal = -1;
        List<Integer> optimalBoundaries = new ArrayList<>();

        List<Integer> boundaries = new ArrayList<>();

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;

            int required = calculateReqCount(arr, mid, boundaries);

            if (required <= workers) {
                optimal = mid;

                optimalBoundaries.clear();
                optimalBoundaries.addAll(boundaries);
                hi = mid - 1;
            }
            else {
                lo = mid + 1;
            }
        }

        System.out.println(optimalBoundaries);
        return optimal;
    }

    private static int calculateReqCount(int[] arr, int maxValue, List<Integer> boundaries) {

        boundaries.clear();

        int actualCount = 1;
        int curLoad = 0;

        for (int i = 0; i < arr.length; ++i) {

            if (curLoad + arr[i] > maxValue) {
                ++actualCount;
                boundaries.add(i);
                curLoad = arr[i];
            }
            else {
                curLoad += arr[i];
            }
        }

        return actualCount;
    }

    private static int sum(int[] arr) {
        int res = 0;

        for (int val : arr) {
            res += val;
        }
        return res;
    }

    private static int findMax(int[] arr) {
        int maxSoFar = Integer.MIN_VALUE;

        for (int val : arr) {
            maxSoFar = Math.max(maxSoFar, val);
        }
        return maxSoFar;
    }

    public static void main(String[] args) {
        try {
            new FairWorkload();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

