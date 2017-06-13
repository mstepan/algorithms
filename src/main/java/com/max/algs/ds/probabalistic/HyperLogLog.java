package com.max.algs.ds.probabalistic;

import com.max.algs.hashing.universal.UniversalHashFunction;
import com.max.algs.util.MathUtils;

/**
 * LogLog: error rate = 1.3 / sqrt(buckets) = 1.3/sqrt(1024) = 0.04 (aka 4 %)
 * <p>
 * HyperLogLog: error rate = 1.05 / sqrt(buckets) = 1.05 / sqrt(1024) = 0.032 (aka 3.2%)
 */
public class HyperLogLog {

    // 0.79402
    // 0.39701
    private static final double ESTIMATOR_FACTOR = 0.79402; // suitable for size > 64


    private static final int BUCKETS_COUNT = 1024;
    private static final int BITS_FOR_BUCKET = (int) MathUtils.log2(BUCKETS_COUNT);

    private static final int WORD_SIZE = 32;
    private static final int BITS_FOR_VALUE = WORD_SIZE - BITS_FOR_BUCKET;

    private final int[] buckets = new int[BUCKETS_COUNT];

    private final UniversalHashFunction<String> hashFunc = UniversalHashFunction.generate();

    /**
     * Add element.
     */
    public void add(String value) {

        int hashValue = hashFunc.hash(value);

        // use 10 bit for bucket [0;1023]
        int bucketIndex = hashValue & (BUCKETS_COUNT - 1);

        int binaryValue = hashValue >>> BITS_FOR_BUCKET;

        buckets[bucketIndex] = Math.max(buckets[bucketIndex], countTrailingZeros(binaryValue));
    }

    private static int countTrailingZeros(int baseValue) {

        int value = baseValue;
        int zeros = 0;

        for (int i = 0; i < BITS_FOR_VALUE; ++i) {
            if ((value & 1) != 0) {
                break;
            }

            value >>= 1;
            ++zeros;
        }

        return zeros;
    }

    /**
     * Estimate cardinality.
     */
    public int cardinality() {

        int sumValues = sum(buckets);

        double cardinality = Math.pow(2.0, ((double) sumValues) / BUCKETS_COUNT) * BUCKETS_COUNT * ESTIMATOR_FACTOR;

        return (int) Math.round(cardinality);
    }


    private static int sum(int[] arr) {

        int sum = 0;

        for (int value : arr) {
            sum += value;
        }
        return sum;
    }

}
