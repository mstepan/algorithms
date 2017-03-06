package com.max.algs;


import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import static com.google.common.base.Preconditions.checkArgument;
import static org.junit.Assert.assertEquals;

public final class AlgorithmsMain2 {

    /**
     * time: O(lgN)
     * space: O(1)
     */
    public static int bSearch(int[] arr, int value) {
        checkArgument(arr != null, "null 'arr' parameter passed");

        int lo = 0;
        int hi = arr.length - 1;

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2; // (lo + hi) >>> 1;

            if (arr[mid] > value) {
                hi = mid - 1;
            }
            else if (arr[mid] < value) {
                lo = mid + 1;
            }
            else {
                return mid;
            }
        }

        return -1;
    }

    @Test
    public void bSearch() {

        int[] arr = {-77, -10, -5, 0, 1, 6, 14, 18, 57};

        assertEquals(0, bSearch(arr, -77));
        assertEquals(1, bSearch(arr, -10));
        assertEquals(3, bSearch(arr, 0));
        assertEquals(6, bSearch(arr, 14));
        assertEquals(8, bSearch(arr, 57));

        assertEquals(-1, bSearch(arr, -103));
        assertEquals(-1, bSearch(arr, 89));
    }

    @Test
    public void bSearchEmptyArray() {

        int[] arr = {};

        assertEquals(-1, bSearch(arr, 0));
        assertEquals(-1, bSearch(arr, -103));
        assertEquals(-1, bSearch(arr, 89));
    }

    @Test(expected = IllegalArgumentException.class)
    public void bSearchNullArray() {
        bSearch(null, 25);
    }

    public static void main(String[] args) {
        try {
            JUnitCore junit = new JUnitCore();
            Result result = junit.run(AlgorithmsMain2.class);

            for (Failure failure : result.getFailures()) {
                System.out.println(failure.getTrace());
            }

            System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

