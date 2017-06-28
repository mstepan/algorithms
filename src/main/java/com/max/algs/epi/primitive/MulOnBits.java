package com.max.algs.epi.primitive;

import org.apache.log4j.Logger;

import java.util.Random;

/**
 * Compute x * y without arithmetic operations.
 */
public class MulOnBits {

    private static final Logger LOG = Logger.getLogger(MulOnBits.class);

    private MulOnBits() throws Exception {

        Random rand = new Random();

        int x, y;
        long expectedSum, actualSum;

        for (int i = 0; i < 1_000_000; ++i) {
            x = rand.nextInt();
            y = rand.nextInt();

            expectedSum = (long) x * y;
            actualSum = mul(x, y);

            if (expectedSum != actualSum) {
                throw new IllegalStateException("Result is incorrect: expected = " + expectedSum + ", actual = " + actualSum +
                        ", for x = " + x + " and y = " + y);
            }
        }


        System.out.printf("'MulOnBits' completed. java-%s %n", System.getProperty("java.version"));
    }

    public static void main(String[] args) {
        try {
            new MulOnBits();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    /**
     * Grade school like multiplication.
     * <p>
     * N - word size (32 bits for int)
     * <p>
     * time: O(N^2)
     */
    public long mul(int x, int y) {
        if (x == 0 || y == 0) {
            return 0;
        }

        long res = 0L;
        long tempX = x;
        long tempY = y;

        while (tempY != 0L) {
            if ((tempY & 0x1L) == 1L) {
                res = add(res, tempX);
            }

            tempY >>>= 1;
            tempX <<= 1;
        }

        return res;
    }

    /**
     * Add x + y using only bit operations.
     * <p>
     * N - word size (32 bits for int)
     * <p>
     * time: O(N)
     */
    public long add(long x, long y) {
        long res = x ^ y;
        long carry = (x & y) << 1;

        long tempRes;

        while (carry != 0) {
            tempRes = res ^ carry;
            carry = (res & carry) << 1;
            res = tempRes;
        }

        return res;
    }

}
