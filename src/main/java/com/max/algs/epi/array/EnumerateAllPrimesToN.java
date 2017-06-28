package com.max.algs.epi.array;

import org.apache.log4j.Logger;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;


public class EnumerateAllPrimesToN {

    private static final Logger LOG = Logger.getLogger(EnumerateAllPrimesToN.class);

    private static final int ZERO = 0;
    private static final int TWO = 2;

    private EnumerateAllPrimesToN() throws Exception {

        List<Integer> expectedPrimes =
                Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73,
                        79, 83, 89, 97, 101);
        System.out.println("expected: " + expectedPrimes);

        int n = 101;
        List<Integer> actualPrimes = findAllPrimes(n);
        System.out.println("actual:   " + actualPrimes);

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * Get all primes using sieve of Eratosthenes.
     * <p>
     * N - boundary.
     * M - number of primes below N.
     * <p>
     * time: sum( n/3 + n/5 + n/7 + n/11 ) = O( N * (lg lg N) )
     * space: O(M + N/(8*2)). Space reduced by '8', because we use BitSet and by '2', because we ignore all even numbers.
     */
    public static List<Integer> findAllPrimes(int boundary) {

        checkArgument(boundary >= 0);

        if (boundary < 2) {
            return Collections.emptyList();
        }

        BitSet composite = new BitSet((boundary >> 1) + 1);
        composite.set(ZERO);

        int lastPrime = (int) (Math.ceil(0.5 * boundary)) + 1;

        List<Integer> primes = new ArrayList<>();
        primes.add(TWO);

        int primeIndex;

        for (primeIndex = composite.nextClearBit(0); indexToValue(primeIndex) <= lastPrime;
             primeIndex = composite.nextClearBit(primeIndex + 1)) {

            int prime = indexToValue(primeIndex);

            primes.add(prime);

            /*
            Start 'sieving' from 'prime ^ 2'. This value will be always 'odd',
            because multiplying two odd numbers will always return an odd number.
            Use long to reduce int multiplication overflow error.
            */
            long compositeMul = ((long) prime) * prime;

            while (compositeMul <= boundary) {

                composite.set(valueToIndex((int) compositeMul));

                /*
                Handle only 'odd' number, skip 'even' numbers.
                To skip even number we need to add 'prime * 2' to 'compositeMul'
                 */
                compositeMul += (prime << 1);
            }
        }

        for (primeIndex = composite.nextClearBit(primeIndex); indexToValue(primeIndex) <= boundary;
             primeIndex = composite.nextClearBit(primeIndex + 1)) {
            primes.add(indexToValue(primeIndex));
        }

        return Collections.unmodifiableList(primes);
    }

    /**
     * value = 2 * index + 1
     */
    private static int indexToValue(int index) {
        return (index << 1) | 1;
    }

    /**
     * index = value / 2
     */
    private static int valueToIndex(int value) {
        return value >> 1;
    }

    public static void main(String[] args) {
        try {
            new EnumerateAllPrimesToN();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}
