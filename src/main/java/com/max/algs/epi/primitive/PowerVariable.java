package com.max.algs.epi.primitive;

import com.max.algs.util.NumberUtils;

import java.util.Random;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Compute power(x, y).
 */
public class PowerVariable {

    private PowerVariable() throws Exception {

        final int decimalPlacesAfterCommaToCompare = 10;
        Random rand = new Random();

        double x;
        int y;

        double expectedSum, actualSum;

        for (int i = 0; i < 1_000_000; ++i) {
            x = rand.nextDouble();
            y = rand.nextInt(100);

            expectedSum = Math.pow(x, y);
            actualSum = powIter(x, y);

            if (NumberUtils.compareRounded(expectedSum, actualSum, decimalPlacesAfterCommaToCompare) != 0) {
                throw new IllegalStateException("Result is incorrect: expected = " + expectedSum + ", actual = " + actualSum +
                        ", for x = " + x + " and y = " + y);
            }
        }

        System.out.printf("'PowerVariable' completed. java-%s %n", System.getProperty("java.version"));
    }

    /**
     * time: O(lg*N), where N is 'power'.
     */
    public static double powRec(double value, int power) {
        checkArgument(power >= 0, "Negative power passed %s", power);

        if (power == 0) {
            return 1.0;
        }

        if (power == 1) {
            return value;
        }

        double valueDoubled = powRec(value, power >> 1);

        double res = valueDoubled * valueDoubled;

        if ((power & 0x1) == 1) {
            res *= value;
        }

        return res;
    }

    /**
     * time: O(lg*N), where N is 'power'.
     */
    public static double powIter(double value, int power) {
        checkArgument(power >= 0, "Negative power passed %s", power);

        if (power == 0) {
            return 1.0;
        }

        if (power == 1) {
            return value;
        }

        double x = value;
        int y = power;
        double res = 1.0;

        while (y != 0) {
            // 'power' is odd
            if ((y & 1) == 1) {
                res *= x;
                --y;
            }
            // 'power' is even
            else {
                double tempRes = x;
                int tempY = 2;

                while (tempY < y) {
                    tempRes *= tempRes;
                    tempY <<= 1;
                }

                res *= tempRes;
                y -= (tempY >> 1);
            }
        }

        return res;
    }

    public static void main(String[] args) {
        try {
            new PowerVariable();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
