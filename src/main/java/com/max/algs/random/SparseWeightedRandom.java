package com.max.algs.random;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Thread safe implementation of weighted SPARSE pseudo random number generator.
 */
final class SparseWeightedRandom extends java.util.Random {


    private final int[] data;
    private final int sum;

    private static final Random RAND = ThreadLocalRandom.current();


    /**
     * N - weights length
     * <p>
     * time: O(N)
     * space: O(N)
     */
    SparseWeightedRandom(int[] weights) {

        assert weights != null : "null 'weights' passed";

        data = new int[weights.length];

        data[0] = weights[0] - 1;

        for (int i = 1; i < data.length; i++) {
            data[i] = data[i - 1] + weights[i];
        }

        sum = data[data.length - 1] + 1;
    }

    /**
     * time: O(lgN)
     * space: O(1)
     */
    @Override
    public int nextInt() {

        int randValue = RAND.nextInt(sum);

        int index = Arrays.binarySearch(data, randValue);

        // value found in array, return just index
        if (index >= 0) {
            return index;
        }

        // value not found in array, next greater value index returned
        return Math.abs(index + 1);
    }


}
