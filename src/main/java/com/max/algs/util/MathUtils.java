package com.max.algs.util;

import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ThreadLocalRandom;

import static com.google.common.base.Preconditions.checkArgument;

public final class MathUtils {

    private static final Logger LOG = Logger.getLogger(MathUtils.class);

    private static final double SQRT_PRECISION = 0.001;
    private static final double SQRT_MAX_ITERATIONS_COUNT = 50;
    private static final double ZERO_DOUBLE = 0.0;

    private static final long MAX_INT_VALUE = Integer.MAX_VALUE;

    private static final int[] CACHE = {1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800};

    private static final BigDecimal TWO = new BigDecimal("2");

    private static final int CHECK_FOR_PRIME_ITERATIONS = 10;

    private static final Random RAND = ThreadLocalRandom.current();

    // Suppresses default constructor, ensuring non-instantiability.
    private MathUtils() {
        throw new IllegalStateException("Non instantiable class '" + MathUtils.class + "' constructor called");
    }

    /**
     * Check if value is perfect square using addition only operation.
     * <p>
     * Addition of first n odd numbers is always perfect square.
     * <p>
     * 1 + 3 = 4,
     * 1 + 3 + 5 = 9,
     * 1 + 3 + 5 + 7 + 9 + 11 = 36 ...
     */
    public static boolean isPerfectSquare(int value) {

        if (value == 0) {
            return true;
        }

        int valueToCheck = value < 0 ? -value : value;

        int res = 0;

        final int half = valueToCheck / 2 + 1;

        for (int i = 1; i <= half; i += 2) {

            res += i;

            if (res == valueToCheck) {
                return true;
            }
        }

        return false;
    }

    public static boolean isPrimeLong(long value) {

        if (value < 2L) {
            return false;
        }

        for (int i = 2; i <= Math.sqrt(value); ++i) {
            if ((value % i) == 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check if number is prime using Ferma's little theorem.
     * This algorithms use randomisation technique to reduce probability of error.
     * Probability of error <= 1/(2^CHECK_FOR_PRIME_ITERATIONS) ~ 0.1%
     * <p>
     * Carmichael numbers are not handled properly, see https://en.wikipedia.org/wiki/Carmichael_number
     * <p>
     * time: O(K*lgN), where K - iterations count
     * space: O(1)
     */
    public static boolean isPrime(int baseValue) {

        // '0', '1' are not primes be definition.
        if (baseValue == 0 || baseValue == 1) {
            return false;
        }

        // '2' and '3' are first two primes.
        if (baseValue == 2 || baseValue == 3) {
            return true;
        }

        if (baseValue == Integer.MIN_VALUE) {
            return false;
        }

        int value = baseValue & 0x7F_FF_FF_FF;
        int a;

        for (int iterations = 0; iterations < CHECK_FOR_PRIME_ITERATIONS; iterations++) {
            // generate 'a' in range [2 .. value-1]
            a = 2 + RAND.nextInt(value - 4);

            if (MathUtils.modularExponentiation(a, value - 1, value) != 1) {
                // 100% composite
                return false;
            }
        }

        // probably prime
        return true;
    }


    /**
     * Binary exponentiation.
     *
     * @return value ^ exp
     */
    public static int power(int value, int exp) {

        checkArgument(exp >= 0, "Negative 'exp' passed");

        if (exp == 0) {
            return 1;
        }

        int bitsCount = NumberUtils.numOfBits(exp);
        int mask = 1 << (bitsCount - 1);

        int res = 1;

        while (mask != 0) {

            res *= res;

            if ((exp & mask) != 0) {
                res *= value;
            }

            mask >>= 1;
        }

        return res;

    }


    /**
     * Modular exponentiation.
     * <p>
     * res = ( base ^ exponent ) % modulus
     */
    public static int modularExponentiation(int base, int exponent, int modulus) {

        if (modulus == 1) {
            return 0;
        }

        if (exponent == 0) {
            return 1;
        }

        int res = 1;

        int value = base % modulus;

        while (exponent > 0) {
            // 'odd' case
            if ((exponent & 1) == 1) {
                res = (res * value) % modulus;
            }

            exponent >>= 1;
            value = (value * value) % modulus;
        }

        return res;
    }

    /**
     * Check if BigDecimal is a whole number(integer)
     */
    public static boolean isIntegerValue(BigDecimal value) {
        return value.signum() == 0 || value.scale() <= 0 || value.stripTrailingZeros().scale() <= 0;
    }


    public static double squareRoot(double value) {

        checkArgument(Double.compare(value, ZERO_DOUBLE) >= 0, "Can't compute square root from negative value");

        double guess = value / 2.0;

        int itCount = 0;

        while (itCount < SQRT_MAX_ITERATIONS_COUNT) {

            double diff = guess * guess - value;
            if (Double.compare(diff, ZERO_DOUBLE) < 0) {
                diff = -diff;
            }

            if (Double.compare(diff, SQRT_PRECISION) <= 0) {
                System.out.println("itCount: " + itCount);
                return guess;
            }

            guess = 0.5 * (guess + value / guess);
            ++itCount;
        }

        throw new IllegalStateException("Can't compute sqrt for '" + value + "' with precision '" +
                SQRT_PRECISION + "' using '" +
                SQRT_MAX_ITERATIONS_COUNT + "' iterations");
    }

    /**
     * Get square root from BigDecimal.
     */
    public static BigDecimal sqrt(BigDecimal value, int scale) {
        BigDecimal x0 = new BigDecimal("0");
        BigDecimal x1 = new BigDecimal(Math.sqrt(value.doubleValue()));
        while (!x0.equals(x1)) {
            x0 = x1;
            x1 = value.divide(x0, scale, BigDecimal.ROUND_HALF_UP);
            x1 = x1.add(x0);
            x1 = x1.divide(TWO, scale, BigDecimal.ROUND_HALF_UP);

        }
        return x1;
    }

    /**
     * Divide value/div without using '/' operation
     */
    public static int divide(int value, int div) {

        // 0 - if both positive or negative
        // 1 - if one positive other negative
        int signBit = (value ^ div) >>> 31;

        value = Math.abs(value);
        div = Math.abs(div);

        int power = 1;
        int scaledDiv = div;

        while ((scaledDiv << 1) <= value) {
            scaledDiv <<= 1;
            power <<= 1;
        }

        value = value - scaledDiv;

        int res = power;

        while (value >= div) {
            value -= div;
            ++res;
        }

        if (signBit == 1) {
            res = ~res + 1;
        }

        return res;
    }


    /**
     * Add two number using only binary operations.
     * Works for any value (positive + positive, negative + positive, negative + negative)
     */
    public static int addBinary(int first, int second) {
        int value = first;
        int carry = second;

        while (carry != 0) {
            int newValue = value ^ carry;
            carry = (value & carry) << 1;
            value = newValue;
        }

        return value;
    }

    /**
     * Binary gcd implementation.
     * See: https://en.wikipedia.org/wiki/Binary_GCD_algorithm
     */
    public static int gcdBinary(int first, int second) {
        if (first == 0) {
            return second;
        }
        if (second == 0) {
            return first;
        }

        int a = Math.abs(first);
        int b = Math.abs(second);

        int power = 0;

        while (a != b) {

            // both even
            if ((a & 1) == 0 && (b & 1) == 0) {
                a >>= 1;
                b >>= 1;
                ++power;
            }
            // 'a' even
            else if ((a & 1) == 0) {
                a >>= 1;
            }
            // 'b' even
            else if ((b & 1) == 0) {
                b >>= 1;
            }
            // both odd, 'a > b'
            else if (a > b) {
                a = (a - b) >> 1;
            }
            // both odd, 'b > a'
            else {
                b = (b - a) >> 1;
            }
        }

        return a * (1 << power);
    }


    /**
     * Calculate great common divisor using Euclidean method (see: http://en.wikipedia.org/wiki/Euclidean_algorithm).
     */
    public static int gcd(int x, int y) {
        int a = Math.abs(x);
        int b = Math.abs(y);

        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }

        return a;
    }


    /**
     * Calculate true 'mod' function. Similar to python '%' implementation.
     * <p>
     * Example:
     * mod(9, 7) = 2 <- positive value
     * mod(17, 8) = 1 <-- positive value
     * <p>
     * mod(-9, 7) = -5 <- negative value
     * mod(-10, 8) = -6 <- negative value
     */
    public static int mod(int value, int base) {
        if (value >= 0) {
            return value % base;
        }

        return -(base + (value % base));
    }


    public static double log2(long value) {
        return Math.log10(value) / Math.log10(2.0);
    }

    public static double logAnyBase(long value, int base) {
        checkArgument(base >= 2, "Incorrect log base found '%s', expected >= 2", base);
        return Math.log10(value) / Math.log10(base);
    }

    public static double log2(double value) {
        return Math.log10(value) / Math.log10(2.0);
    }

    /**
     * Natural logarithm.
     */
    public static double ln(double value) {
        return Math.log10(value) / Math.log10(Math.E);
    }

    /**
     * Returns n-th Catalan number.
     * see: http://en.wikipedia.org/wiki/Catalan_number
     */
    public static BigInteger catalanNumber(int n) {

        if (n == 0) {
            return BigInteger.ONE;
        }

        BigInteger res = MathUtils.factorialBig(BigInteger.valueOf(n << 1)); // 2*n
        BigInteger nFac = MathUtils.factorialBig(BigInteger.valueOf(n)); // n
        res = res.divide(nFac.multiply(nFac).multiply(BigInteger.valueOf(n + 1)));
        return res;
    }

    /**
     * Russian peasant multiplication.
     */
    public static long mul(long n, long m) {
        long addition = 0;
        int iterationsCount = 0;

        while (m > 1) {

            // 'm' is odd
            if ((m & 1) > 0) {
                addition += n;
                --m;
            }
            n = n << 1;
            m = m >>> 1;

            ++iterationsCount;
        }

        LOG.info("iterationsCount: " + iterationsCount);
        return n + addition;
    }

    /**
     * Calculate factorial using divide-and-conquer technique and
     * fork-join pool to load all CPU cores.
     */
    public static BigInteger factorialParallel(int value) {
        checkArgument(value >= 0, "Can't find factorial for negative value: %s", value);

        ForkJoinPool pool = new ForkJoinPool();
        BigInteger res;
        try {
            res = pool.invoke(new FacRecTask(2, value));
        }
        finally {
            pool.shutdownNow();
        }

        return res;
    }

    public static BigInteger factorialBig(BigInteger value) {

        checkArgument(value.compareTo(BigInteger.ZERO) >= 0, "Can't find factorial for negative value");

        BigInteger res = BigInteger.ONE;

        while (!value.equals(BigInteger.ONE)) {
            res = res.multiply(value);
            value = value.subtract(BigInteger.ONE);
        }

        return res;

    }

    public static int factorial(int value) {
        checkArgument(value >= 0, "Can't find factorial for negative value: %s", value);

        if (value < CACHE.length) {
            return CACHE[value];
        }

        long res = 1L;

        while (value > 1) {
            res *= value;
            if (res > MAX_INT_VALUE) {
                throw new IllegalArgumentException("Factorial too big");
            }
            --value;
        }

        return (int) res;
    }

    public static int combinations(int k, int n) {
        return factorial(n) / (factorial(k) * factorial(n - k));
    }

    private static final class FacRecTask extends RecursiveTask<BigInteger> {

        private static final int SEQ_THRESHOLD = 50;

        private final int from;
        private final int to;

        public FacRecTask(int from, int to) {
            this.from = from;
            this.to = to;
        }

        @Override
        protected BigInteger compute() {

            int elemsCount = to - from + 1;

            if (elemsCount <= SEQ_THRESHOLD) {

                BigInteger res = BigInteger.valueOf(to);

                for (int i = from; i < to; ++i) {
                    res = res.multiply(BigInteger.valueOf(i));
                }

                return res;
            }
            int mid = from + ((to - from) >> 1);

            FacRecTask leftTask = new FacRecTask(from, mid);
            leftTask.fork();

            FacRecTask rightTask = new FacRecTask(mid + 1, to);

            return rightTask.compute().multiply(leftTask.join());
        }
    }

}
