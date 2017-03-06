package com.max.algs.primes;

import java.util.Arrays;
import java.util.BitSet;


public final class PrimeUtilities {

    // hw.l1dcachesize: 32768
    private static final int L1D_CACHE_SIZE_IN_BYTES = 32_768;

    /**
     * Calculate all primes using segmented Sieve of Eratosthenes.
     *
     * @param limit
     */
    public static int countPrimesSegmentedWithBoolean(int limit) {

        int totalPrimesCount = 0;

        int limitSqrt = (int) Math.sqrt(limit) + 1;

        BitSet basePrimes = calculateBasePrimes(limitSqrt);
        totalPrimesCount += basePrimes.cardinality();

        final int segmentSize = L1D_CACHE_SIZE_IN_BYTES - 1;

        int from = limitSqrt;
        int to = Math.min(limit, from + segmentSize);

        boolean[] segmented = new boolean[segmentSize + 1];

        while (true) {

            Arrays.fill(segmented, false);
            Arrays.fill(segmented, 0, Math.min(to - from + 1, segmentSize + 1), true);

            int toSqrt = (int) Math.sqrt(to);

            for (int prime = basePrimes.nextSetBit(0); prime != -1 &&
                    prime <= toSqrt; prime = basePrimes.nextSetBit(prime + 1)) {

                int compositeValue = (from / prime) * prime;

                while (compositeValue <= to) {

                    if (compositeValue >= from) {
                        segmented[compositeValue - from] = false;
                    }

                    compositeValue += prime;
                }
            }

            totalPrimesCount += cardinality(segmented);

            if (to == limit) {
                break;
            }

            from = to + 1;
            to = Math.min(limit, from + segmentSize);
        }

        return totalPrimesCount;
    }

    private static int cardinality(boolean[] arr) {

        int count = 0;

        for (boolean val : arr) {
            if (val) {
                ++count;
            }
        }

        return count;
    }

    /**
     * Calculate all primes using segmented Sieve of Eratosthenes.
     *
     * @param limit
     */
    public static int countPrimesSegmented(int limit) {

        int totalPrimesCount = 0;

        int limitSqrt = (int) Math.sqrt(limit) + 1;

        BitSet basePrimes = calculateBasePrimes(limitSqrt);
        totalPrimesCount += basePrimes.cardinality();

        final int segmentSize = L1D_CACHE_SIZE_IN_BYTES * 8;

        BitSet segment = new BitSet(segmentSize);

        int from = limitSqrt;
        int to = Math.min(limit, from + segmentSize);

        while (true) {

            segment.clear();
            segment.set(0, Math.min(to - from + 1, segmentSize + 1));

            int toSqrt = (int) Math.sqrt(to);

            for (int prime = basePrimes.nextSetBit(0); prime != -1 &&
                    prime <= toSqrt; prime = basePrimes.nextSetBit(prime + 1)) {

                int compositeValue = (from / prime) * prime;

                while (compositeValue <= to) {

                    if (compositeValue >= from) {
                        segment.clear(compositeValue - from);
                    }

                    compositeValue += prime;
                }
            }

            totalPrimesCount += segment.cardinality();

            if (to == limit) {
                break;
            }

            from = to + 1;
            to = Math.min(limit, from + segmentSize);

        }

        return totalPrimesCount;
    }

    private static BitSet calculateBasePrimes(int limit) {

        BitSet primes = new BitSet(limit + 1);
        primes.set(0, limit);
        primes.clear(0);
        primes.clear(1);

        int limitSqrt = (int) Math.sqrt(limit);

        for (int i = primes.nextSetBit(2); i <= limitSqrt && i != -1; i = primes.nextSetBit(i + 1)) {

            int compositeVal = i + i;

            while (compositeVal <= limit) {
                primes.clear(compositeVal);
                compositeVal += i;
            }
        }

        return primes;
    }

    /**
     * Calculate all primes using Sieve of Eratosthenes.
     *
     * @param limit
     */
    public static int countPrimes(int limit) {
        BitSet primes = calculateBasePrimes(limit);
        return primes.cardinality();
    }


    private PrimeUtilities() {
    }
}
