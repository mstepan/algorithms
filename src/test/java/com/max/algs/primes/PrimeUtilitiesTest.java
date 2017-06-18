package com.max.algs.primes;

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import static junit.framework.TestCase.assertEquals;


public class PrimeUtilitiesTest {

    // limit -> number of primes
    private static final Map<Integer, Integer> PRIMES_EXPECTED_COUNTERS = new LinkedHashMap<>();

    static {
        PRIMES_EXPECTED_COUNTERS.put(10, 4);
        PRIMES_EXPECTED_COUNTERS.put(100, 25);
        PRIMES_EXPECTED_COUNTERS.put(1_000, 168);
        PRIMES_EXPECTED_COUNTERS.put(10_000, 1_229);
        PRIMES_EXPECTED_COUNTERS.put(100_000, 9_592);
        PRIMES_EXPECTED_COUNTERS.put(1_000_000, 78_498);
        PRIMES_EXPECTED_COUNTERS.put(10_000_000, 664_579);
        PRIMES_EXPECTED_COUNTERS.put(100_000_000, 5_761_455);
        PRIMES_EXPECTED_COUNTERS.put(1_000_000_000, 50_847_534);
    }

    private static void checkCountPrimesFunction(Function<Integer, Integer> primesCalcFunc) {
        for (Map.Entry<Integer, Integer> entry : PRIMES_EXPECTED_COUNTERS.entrySet()) {
            assertEquals(entry.getValue(), primesCalcFunc.apply(entry.getKey()));
        }
    }

    @Test
    public void countPrimesSegmentedWithBoolean() {
        checkCountPrimesFunction(PrimeUtilities::countPrimesSegmentedWithBoolean);
    }

    @Test
    public void countPrimesSegmented() {
        checkCountPrimesFunction(PrimeUtilities::countPrimesSegmented);
    }

    @Test
    public void countPrimes() {
        checkCountPrimesFunction(PrimeUtilities::countPrimes);
    }


}
