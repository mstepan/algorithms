package com.max.algs.util;

/**
 * Created by mstepan on 4/4/16.
 */
public final class MulUtils {

    private MulUtils() {
        throw new IllegalStateException("Can't instantiate utility class");
    }

    public static long multiply(int x, int y) {

        int s1 = (x >>> 31) ^ (y >>> 31);

        long res = multiplyRec(Math.abs(x), Math.abs(y), Integer.SIZE);

        if (s1 == 0) {
            return res;
        }

        return -res;
    }

    /**
     * Karatsuba algorithm for multiplication.
     * <p>
     * See: https://en.wikipedia.org/wiki/Karatsuba_algorithm
     */
    private static long multiplyRec(int x, int y, int bits) {

        if (bits <= 8) {
            return x * y;
        }

        int half = bits >> 1;

        int x0 = x & ((1 << half) - 1);
        int x1 = x >>> half;

        int y0 = y & ((1 << half) - 1);
        int y1 = y >>> half;

        long z0 = multiplyRec(x0, y0, half);
        long z2 = multiplyRec(x1, y1, half);

        long z1 = multiplyRec((x1 + x0), (y1 + y0), half) - z2 - z0;

        return (z2 << bits) + (z1 << half) + z0;
    }

}
