package com.max.algs.ds.heap;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HeapUtilsTest {

    @Test(expected = IllegalArgumentException.class)
    public void checkGreaterNullArray() {
        HeapUtils.checkGreater(null, 6, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkGreaterNegativeCount() {
        HeapUtils.checkGreater(new int[]{7, 5, 3}, 6, -1);
    }


    @Test
    public void checkGreater() {
        int[] arr = new int[]{7, 3, 5, 2, 1, 4, 3, 0, 1, 0, -1};

        int SIX = 6;
        assertTrue(HeapUtils.checkGreater(arr, SIX, 1));
        assertFalse(HeapUtils.checkGreater(arr, SIX, 2));

        int FOUR = 4;
        assertTrue(HeapUtils.checkGreater(arr, FOUR, 1));
        assertTrue(HeapUtils.checkGreater(arr, FOUR, 2));
        assertFalse(HeapUtils.checkGreater(arr, FOUR, 3));

        int TWO = 2;
        assertTrue(HeapUtils.checkGreater(arr, TWO, 1));
        assertTrue(HeapUtils.checkGreater(arr, TWO, 2));
        assertTrue(HeapUtils.checkGreater(arr, TWO, 5));
        assertFalse(HeapUtils.checkGreater(arr, TWO, 6));
        assertFalse(HeapUtils.checkGreater(arr, TWO, 10));

        int ZERO = 0;
        assertTrue(HeapUtils.checkGreater(arr, ZERO, 1));
        assertTrue(HeapUtils.checkGreater(arr, ZERO, 2));
        assertTrue(HeapUtils.checkGreater(arr, ZERO, 8));
        assertFalse(HeapUtils.checkGreater(arr, ZERO, 9));
        assertFalse(HeapUtils.checkGreater(arr, ZERO, 15));

        assertFalse(HeapUtils.checkGreater(arr, 8, 1));
        assertFalse(HeapUtils.checkGreater(arr, 8, 10));
        assertFalse(HeapUtils.checkGreater(arr, 100, 1));
        assertFalse(HeapUtils.checkGreater(arr, 133, 10));

        // zero count
        assertTrue(HeapUtils.checkGreater(arr, 8, 0));
        assertTrue(HeapUtils.checkGreater(arr, 133, 0));

        // count >= arr.length
        int[] SMALL_ARR = new int[]{7, 5, 3};
        assertFalse(HeapUtils.checkGreater(SMALL_ARR, TWO, 3));
        assertFalse(HeapUtils.checkGreater(SMALL_ARR, TWO, 100));

    }

}
