package com.max.algs.dynamic;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link Max2DSubarray}
 */
public class Max2DSubarrayTest {

    @Test
    public void findMaxSubarray() {
        int[][] matrix = {
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 1, 0, 0, 0},
                {0, 0, 1, 1, 1, 0, 0, 0},
                {0, 1, 1, 1, 1, 0, 0, 0},
                {1, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 1, 1, 1, 0, 0},
                {0, 0, 0, 1, 1, 1, 0, 0},
        };

        assertEquals("max subarray is incorrect", 9, Max2DSubarray.findMaxSubarray(matrix));
    }
}
