package com.max.algs.creative;


import com.max.algs.util.ArrayUtils;
import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;


public final class GetElementFromSecondHalf {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());

    private static final int PROBABILITY_ALGORITHM_THRESHOLD = 1000;

    private static final Random RAND = new Random();

    private static final int SAMPLING_SIZE = 20;

    /**
     * Monte Carlo algorithm that returns element from second half of an array
     * with probability p = 1 - 1/(2^k).
     * <p>
     * Example: if k = 20, p = 0.999999 (k = SAMPLING_SIZE).
     * <p>
     * time: O(1)
     * space: O(SAMPLING_SIZE)
     */
    private static int getFromSecondHalf(int[] arr) {
        checkNotNull(arr);

        // return max value from 1/2 of an array if below threshold
        if (arr.length < PROBABILITY_ALGORITHM_THRESHOLD) {

            int maxValue = Integer.MIN_VALUE;

            for (int i = 0; i < arr.length / 2 + 1; ++i) {
                maxValue = Math.max(maxValue, arr[i]);
            }

            return maxValue;
        }

        int maxSoFar = Integer.MIN_VALUE;

        Set<Integer> indexes = new HashSet<>();

        for (int i = 0; i < SAMPLING_SIZE; ++i) {

            int randIndex = RAND.nextInt(arr.length);

            while( indexes.contains(randIndex) ){
                randIndex = RAND.nextInt(arr.length);
            }

            indexes.add(randIndex);
            maxSoFar = Math.max(maxSoFar, randIndex);
        }

        return maxSoFar;
    }

    private GetElementFromSecondHalf() {

        int[] arr = ArrayUtils.generateRandomArray(10_000, 10_000);

        int elem = getFromSecondHalf(arr);

        LOG.info("from second half: " + elem);

        LOG.info("GetElementFromSecondHalf done...");
    }


    public static void main(String[] args) {
        try {
            new GetElementFromSecondHalf();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }


}
