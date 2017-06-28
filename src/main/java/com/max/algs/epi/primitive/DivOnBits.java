package com.max.algs.epi.primitive;

import org.apache.log4j.Logger;

import java.util.Random;

/**
 * Compute x * y without arithmetic operations.
 */
public class DivOnBits {

    private static final Logger LOG = Logger.getLogger(DivOnBits.class);

    private static final int SIGN_BIT = Integer.SIZE - 1;

    private DivOnBits() throws Exception {

        Random rand = new Random();

        int x, y;
        int expectedSum, actualSum;

        for (int i = 0; i < 1_000_000; ++i) {
            x = rand.nextInt();
            y = rand.nextInt();

            expectedSum = x / y;
            actualSum = div(x, y);

            if (expectedSum != actualSum) {
                throw new IllegalStateException("Result is incorrect: expected = " + expectedSum + ", actual = " + actualSum +
                        ", for x = " + x + " and y = " + y);
            }
        }

        System.out.printf("'DivOnBits' completed. java-%s %n", System.getProperty("java.version"));
    }

    private static int negate(int value) {
        return (~value) + 1;
    }

    private static boolean hasSameSign(int x, int y) {
        return ((x >>> SIGN_BIT) ^ (y >>> SIGN_BIT)) == 0;
    }

    public static void main(String[] args) {
        try {
            new DivOnBits();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    /**
     * Divide 'value' on 'divider' using only addition, subtraction and shift operations.
     */
    public int div(int value, int divider) {
        if (divider == 0) {
            throw new ArithmeticException("Division by zero");
        }

        if (value == 0) {
            return 0;
        }

        int x = value < 0 ? negate(value) : value;
        int y = divider < 0 ? negate(divider) : divider;

        int res = 0;
        int power = Integer.SIZE;
        long yPower = ((long) y) << power;

        while (x >= y) {
            while (yPower > x) {
                yPower >>= 1;
                --power;
            }

            x = x - (int) yPower;
            res += 1 << power;
        }

        return hasSameSign(value, divider) ? res : negate(res);
    }

}
