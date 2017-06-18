package com.max.algs.random;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Thread safe implementation of weighted pseudo random number generator.
 */
final class DenseWeightedRandom extends java.util.Random {

    private static final Random RAND = ThreadLocalRandom.current();
    private final int[] data;


    /**
     * K - sum of all weights
     * <p>
     * time: O(K)
     * space: O(K)
     */
    DenseWeightedRandom(int[] weights) {

        assert weights != null : "null 'weights' passed";

        int sum = sum(weights);

        data = new int[sum];

        int index = 0;
        for (int i = 0; i < weights.length; i++) {

            int count = weights[i];

            while (count != 0) {
                data[index] = i;
                ++index;
                --count;
            }
        }
    }

    private static int sum(int[] weights) {
        int sum = 0;
        for (int value : weights) {
            assert value > 0 : "incorrect weight detected '" + value + "', should be positive value";
            sum += value;
        }
        return sum;
    }

    /**
     * time: O(1)
     * space: O(1)
     */
    @Override
    public int nextInt() {
        return data[RAND.nextInt(data.length)];
    }


}
