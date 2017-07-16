package com.max.algs.util;


import java.math.BigDecimal;

public final class NumberUtils {

    private static final double NORM_DOUBLE = Math.pow(2.0, 30.0);
    private static final int NORM_INT = (int) NORM_DOUBLE;

    private static final int FLOAT_BIAS = 126;
    private static final BigDecimal ONE_SCALED = new BigDecimal("1").setScale(32, BigDecimal.ROUND_HALF_UP);
    private static final BigDecimal TWO = new BigDecimal("2");
    private static final BigDecimal MINUS_ONE = new BigDecimal("-1");

    private NumberUtils() {
        super();
    }


    /**
     * Add two values, throwing ArithmeticException if overflow/underflow occurred.
     */
    public static int safeAdd(int x, int y) {
        int res = x + y;

        /*
         * Works correctly even for Integer.MAX_VALUE + Integer.MAX_VALUE = -2
         */
        if (x > 0 && y > 0 && res < 0) {
            throw new ArithmeticException("Overflow: " + x + " + " + y + " = " + res);
        }

        /**
         * Works fine even for Integer.MIN_VALUE + Integer.MIN_VALUE = 0, so res >= 0
         */
        if (x < 0 && y < 0 && res >= 0) {
            throw new ArithmeticException("Underflow: " + x + " + " + y + " = " + res);
        }

        return res;
    }

    /**
     * Reconstruct 'float' 32 bit value from raw bit integer representation.
     */
    public static float reconstructFloatFromRawBits(int rawValue) {

        // 32 bits representation: S - sign bit, E - exponent bits, M - mantissa.
        // SEEEEEEE EMMMMMMM MMMMMMMM MMMMMMMM

        // 1 bit
        int signBit = rawValue >>> 31;

        // 8 bits
        int powerBits = ((rawValue >>> 23) & 0x00_00_00_FF);

        // 23 bits
        int mantissaBits = (rawValue & 0x00_7F_FF_FF) | (1 << 23);

        BigDecimal value = signBit == 0 ? BigDecimal.ONE : MINUS_ONE;

        int powerOf2 = powerBits - FLOAT_BIAS;

        BigDecimal power;

        if (powerOf2 >= 0) {
            power = TWO.pow(powerOf2);
        }
        else {
            power = ONE_SCALED.divide(TWO.pow(-powerOf2), BigDecimal.ROUND_HALF_UP);
        }

        value = value.multiply(power).multiply(calculateFloatMantissaValue(mantissaBits));

        return value.floatValue();
    }

    private static BigDecimal calculateFloatMantissaValue(int mantissaBits) {

        int mantissa = NumberUtils.reverseBits(mantissaBits);

        BigDecimal res = BigDecimal.ZERO;

        int twoOffset = 0;

        for (int i = 0; mantissa != 0; ++i, mantissa >>>= 1) {
            if ((mantissa & 1) != 0) {

                if (twoOffset == 0) {
                    twoOffset = 2;
                }

                // Process from most significant bit (from right-to-left).
                // 1/2 + 1/4 + 1/32 + .. + <-- for each '1' value in bit representation
                res = res.add(ONE_SCALED.divide(new BigDecimal(twoOffset), BigDecimal.ROUND_HALF_UP));
            }

            if (twoOffset != 0) {
                twoOffset <<= 1;
            }
        }

        return res;
    }

    /**
     * Compare two double values up to 'decimalPlaces' after comma.
     */
    public static int compareRounded(double first, double second, int decimalPlaces) {
        double scale = Math.pow(10.0, decimalPlaces);
        return Double.compare(Math.round(first * scale),
                Math.round(second * scale));
    }

    /**
     * Check if number is sparse. A number is said to be a sparse number if in binary representation of
     * the number no two or more consecutive bits are set.
     * <p>
     * time: O(1)
     * space: O(1)
     */
    public static boolean isSparse(int baseValue) {
        return (baseValue & (baseValue << 1)) == 0;
    }

    /**
     * Calculate abs for value.
     * Do not use branching.
     * <p>
     * Similar to Math.abs(), but throw IllegalArgumentException if Integer.MIN_VALUE passed as an argument.
     */
    public static int abs(int value) {

        if (value == Integer.MIN_VALUE) {
            throw new IllegalArgumentException("Can't abs(Integer.MIN_VALUE), overflow will occur");
        }

        int mask = value >> Integer.SIZE - 1;
        return (value + mask) ^ mask;
    }

    /**
     * Returns sign of a value.
     * -1 <- if value is negative
     * +1 <- if value is positive
     */
    public static int sign(int value) {
        return 1 | (value >> Integer.SIZE - 1);
    }

    /**
     * Returns integer with only one least significant bit set to '1'.
     * <p>
     * 0|010111000
     * and
     * 1|101001000
     * ------------
     * 0|000001000
     */
    public static int valueLeastSignificantBitSet(int value) {

        if (value == Integer.MIN_VALUE) {
            return 1;
        }

        return value & (-value);
    }

    /**
     * Return minimum of 2 values.
     * Optimized version: can use instruction set parallelism.
     */
    public static int min(int x, int y) {
        return y ^ ((x ^ y) & (x < y ? -1 : 0));
    }

    /**
     * Compute next power of 2 for 32 bit value.
     */
    public static int roundToPowerOf2(int value) {

        --value;

        value |= value >> 1;
        value |= value >> 2;
        value |= value >> 4;
        value |= value >> 8;
        value |= value >> 16;

        ++value;

        return value;
    }

    /**
     * Compute next power of 2 for 64 bit value.
     */
    public static long roundToPowerOf2(long value) {

        --value;

        value |= value >> 1;
        value |= value >> 2;
        value |= value >> 4;
        value |= value >> 8;
        value |= value >> 16;
        value |= value >> 32;

        ++value;

        return value;
    }

    /**
     * returns (x+y) % n
     */
    public static int modSum(int x, int y, int mod) {
        int temp = x + y;
        return temp - (mod & (temp < mod ? 0 : -1));
    }

    /**
     * Overflow: x > 0, y > 0, res < 0: (~x & ~y & res) < 0
     * Underflow: x < 0, y < 0, res > 0:  (x & y & ~res) < 0
     */
    public static boolean canOverflow(int x, int y) {

        final int res = x + y;

        if (((x & y & ~res) | (~x & ~y & res)) < 0) {
            return true;
        }

        return false;
    }

    public static int floor(double value) {
        return (int) (value + NORM_DOUBLE) - NORM_INT;

    }

    public static int ceil(double value) {
        return NORM_INT - (int) (NORM_DOUBLE - value);
    }

    public static boolean isPerfectSquare(double value) {
        int squareValue = (int) Math.sqrt(value);
        int intValue = (int) value;
        return intValue == squareValue * squareValue;
    }

    public static boolean isFibonacci(int x) {
        return isPerfectSquare(5 * x * x + 4) ||
                isPerfectSquare(5 * x * x - 4);
    }

    /*
     * Reverse bits representation for 'int' value
     */
    public static int reverseBits(int value) {
        value = swapBytes(value, 0, 3);
        value = swapBytes(value, 1, 2);
        return value;
    }

    /**
     * Swap bytes in 'int'
     */
    private static int swapBytes(int value, int from, int to) {

        if (from < 0 || to < 0) {
            throw new IllegalArgumentException("from < 0 or to < 0, from = " + from + ", to = " + to);
        }

        if (from > 4 || to > 4) {
            throw new IllegalArgumentException("from > 4 or to > 4, from = " + from + ", to = " + to);
        }

        if (from == to) {
            return value;
        }

        // extract 'from' and 'to' bytes
        int fromByte = swapBitsInByte((value >>> Byte.SIZE * from) & 0xFF);
        int toByte = swapBitsInByte((value >>> Byte.SIZE * to) & 0xFF);

        // zero 'from' and 'to' bytes
        value = value & (~(0xFF << Byte.SIZE * from));
        value = value & (~(0xFF << Byte.SIZE * to));

        // set 'from' and 'to', swapping each other
        value |= (fromByte << Byte.SIZE * to);
        value |= (toByte << Byte.SIZE * from);

        return value;
    }

    private static int swapBitsInByte(int b) {
        b = (b & 0xF0) >>> 4 | (b & 0x0F) << 4;
        b = (b & 0xCC) >>> 2 | (b & 0x33) << 2;
        b = (b & 0xAA) >>> 1 | (b & 0x55) << 1;
        return b;

    }

    /**
     * Calculate parity of binary representation for integer.
     * <p>
     * Works for positive and negative values.
     */
    public static boolean calculateParity(int baseValue) {

        int value = baseValue;

        // mask sign bit if negative value
        if (value < 0) {
            value = maskSignBit(baseValue);
        }

        value ^= value >> 1;
        value ^= value >> 2;
        value = (value & 0x11_11_11_11) * 0x11_11_11_11;

        boolean parityFlag = ((value >> 28) & 1) == 0;

        return baseValue < 0 ? !parityFlag : parityFlag;
    }

    /**
     * Mask sign bit for integer.
     */
    public static int maskSignBit(int baseValue) {

        int value = baseValue;
        value = (value >>> 1);
        value &= 0x3F_FF_FF_FF;
        value <<= 1;
        value |= baseValue & 1;

        return value;
    }

    public static int swapAdjBits(int value) {
        return ((value & 0x55555555) << 1) | ((value & 0xAAAAAAAA) >>> 1);
    }

    /**
     * Calculate polynom in O(n) time.
     */
    public static long evaluatePolynom(int[] coeff, int x) {

        if (coeff == null) {
            throw new IllegalArgumentException("NULL coefficients array passed");
        }

        if (coeff.length == 0) {
            throw new IllegalArgumentException("Empty coefficients array passed");
        }

        long res = coeff[coeff.length - 1];
        int index = coeff.length - 2;

        while (index >= 0) {
            res = res * x + coeff[index];
            --index;
        }

        return res;
    }

    /**
     * Number of bits needed to represent 'value'.
     */
    public static int numOfBits(int value) {

        value = abs(value);

        if (value < 2) {
            return 1;
        }

        if (value < 4) {
            return 2;
        }

        double numBitsRaw = NumberUtils.log2(value);
        int unmOfBitsNeeded = (int) Math.ceil(numBitsRaw);

        // check if power of 2
        if ((value & (value - 1)) == 0) {
            ++unmOfBitsNeeded;
        }

        return unmOfBitsNeeded;
    }

    /**
     * Logarithm base 2.
     */
    public static double log2(int i) {

        if (i == 0 || i == 1) {
            return 1;
        }

        return Math.log10(i) / Math.log10(2.0);
    }


}
