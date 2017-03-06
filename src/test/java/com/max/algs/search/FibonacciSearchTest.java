package com.max.algs.search;

import com.max.algs.util.ArrayUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class FibonacciSearchTest {

    private static final Random RAND = new Random();

    @Test
    public void fibSearch() {
        int[] arr = {2, 3, 4, 10, 40};

        assertEquals(0, FibonacciSearch.fibSearch(arr, 2));
        assertEquals(1, FibonacciSearch.fibSearch(arr, 3));
        assertEquals(2, FibonacciSearch.fibSearch(arr, 4));
        assertEquals(3, FibonacciSearch.fibSearch(arr, 10));
        assertEquals(4, FibonacciSearch.fibSearch(arr, 40));

        assertEquals(-1, FibonacciSearch.fibSearch(arr, -25));
        assertEquals(-1, FibonacciSearch.fibSearch(arr, 0));
        assertEquals(-1, FibonacciSearch.fibSearch(arr, 5));
        assertEquals(-1, FibonacciSearch.fibSearch(arr, 39));
        assertEquals(-1, FibonacciSearch.fibSearch(arr, 55));
    }

    @Test
    public void fibSearchInRandomArray() {

        final int minValue = -100_000;
        final int maxValue = 100_000;

        for (int i = 0; i < 100; i++) {
            int[] arr = ArrayUtils.generateRandomArrayWithNegative(RAND.nextInt(100_000), minValue, maxValue);
            Arrays.sort(arr);

            for (int value : arr) {
                int index = FibonacciSearch.fibSearch(arr, value);
                assertEquals(arr[index], value);
            }

            assertEquals(-1, FibonacciSearch.fibSearch(arr, minValue - 1));
            assertEquals(-1, FibonacciSearch.fibSearch(arr, minValue - 67));

            assertEquals(-1, FibonacciSearch.fibSearch(arr, maxValue + 1));
            assertEquals(-1, FibonacciSearch.fibSearch(arr, maxValue + 77));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void fibSearchNullArray() {
        FibonacciSearch.fibSearch(null, 40);
    }

    @Test
    public void fibSearchEmptyArray() {
        int[] arr = {};
        assertEquals(-1, FibonacciSearch.fibSearch(arr, 40));
    }

    @Test
    public void fibSearchSingleElementArray() {
        int[] arr = {40};
        assertEquals(0, FibonacciSearch.fibSearch(arr, 40));

        assertEquals(-1, FibonacciSearch.fibSearch(arr, 39));
        assertEquals(-1, FibonacciSearch.fibSearch(arr, 107));
    }

}
