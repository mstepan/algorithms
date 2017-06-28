package com.max.algs.ds.probabalistic;

import com.max.algs.hashing.universal.UniversalHashFunction;
import com.max.algs.util.MathUtils;
import org.apache.log4j.Logger;

/**
 * This code is not as described in HyperLogLog paper, so can be very INACCURATE.
 * <p>
 * LogLog: expected error rate = 1.3 / sqrt(buckets) = 1.3/sqrt(1024) = 0.04 (aka 4%)
 * <p>
 * HyperLogLog: expected error rate = 1.05 / sqrt(buckets) = 1.05 / sqrt(1024) = 0.032 (aka 3.2%)
 */
public class HyperLogLog {

    private static final Logger LOG = Logger.getLogger(HyperLogLog.class);

    private static final int BUCKETS_COUNT = 1024;
    private static final int BITS_FOR_BUCKET = (int) MathUtils.log2(BUCKETS_COUNT);

    private static final int WORD_SIZE = 32;
    private static final int BITS_FOR_VALUE = WORD_SIZE - BITS_FOR_BUCKET;

    private final byte[] buckets = new byte[BUCKETS_COUNT];

    private final UniversalHashFunction<String> hashFunc = UniversalHashFunction.generate();

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

        assert zeros >= 0 && zeros <= WORD_SIZE : "countTrailingZeros found incorrect number of '0's";

        return zeros;
    }

    private static double estimatorFactor(int m) {
        switch (m) {
            case 16:
                return 0.673;
            case 32:
                return 0.697;
            case 64:
                return 0.709;
            default:
                return 0.7213 / (1.0 + 1.079 / m);
        }
    }

    private static int sum(byte[] arr) {
        int sum = 0;

        for (int val : arr) {
            sum += val;
        }
        return sum;
    }

    public static void main(String[] args) {
        try {
            final int n = 1_000_000;

            HyperLogLog data = new HyperLogLog();

            for (int it = 0; it < 10; ++it) {
                for (int i = 0; i < n; ++i) {
                    data.add("val-" + i);
                }
            }

            System.out.println(data.cardinality());

            System.out.printf("error: %.1f%% %n", Math.abs(100.0 - (data.cardinality() * 100.0 / n)));

            System.out.printf("HyperLogLog done: java-%s %n", System.getProperty("java.version"));
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    /**
     * Add element.
     */
    public void add(String value) {

        int hashValue = hashFunc.hash(value);

        // use 10 bit for bucket [0;1023]
        int bucketIndex = hashValue & (BUCKETS_COUNT - 1);

        int trailingZeros = countTrailingZeros(hashValue >>> BITS_FOR_BUCKET);

        assert trailingZeros <= WORD_SIZE : "incorrect value for 'trailingZeros'";

        buckets[bucketIndex] = (byte) Math.max(buckets[bucketIndex], trailingZeros);
    }

    /**
     * Estimate cardinality.
     */
    public int cardinality() {
        int sumValues = sum(buckets);

        double cardinality = Math.pow(2.0, ((double) sumValues) / BUCKETS_COUNT) * BUCKETS_COUNT *
                estimatorFactor(buckets.length);

        return (int) Math.round(cardinality);
    }

}
