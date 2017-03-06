package com.max.algs.sorting;

import com.max.algs.util.ArrayUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertTrue;


public class BinaryQuicksorterTest {

    @Test(expected = IllegalArgumentException.class)
    public void sortNullArrayThrowsException() {
        BinaryQuicksorter.sort(null);
    }

    @Test
    public void sortBoundaryValues() {

        int[] arr = {Integer.MAX_VALUE, 0, -1, Integer.MIN_VALUE, 1};

        BinaryQuicksorter.sort(arr);

        assertTrue(Arrays.equals(arr, new int[]{Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE}));
    }

    @Test
    public void sortRandomData() {
        final Random rand = new Random();

        for (int k = 0; k < 1000; k++) {
            int[] arr = ArrayUtils.generateRandomArray(100 + rand.nextInt(10_000));
            int[] copy = Arrays.copyOf(arr, arr.length);

            BinaryQuicksorter.sort(arr);
            Arrays.sort(copy);

            if (!Arrays.equals(arr, copy)) {
                throw new IllegalArgumentException("array isn't sorted");
            }
        }
    }
}
