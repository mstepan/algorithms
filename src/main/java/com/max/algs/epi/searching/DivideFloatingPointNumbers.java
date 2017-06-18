package com.max.algs.epi.searching;

import org.apache.log4j.Logger;

/**
 * 12.5. Variant. Divide two floating point numbers, x/y within specific tolerance.
 * Only addition, subtraction and multiplication allowed.
 */
public class DivideFloatingPointNumbers {

    private static final double EPSILON = 0.0001;
    private static final double NEG_EPSILON = -EPSILON;

    private static final Logger LOG = Logger.getLogger(DivideFloatingPointNumbers.class);


    private DivideFloatingPointNumbers() throws Exception {

        double x = 0.6;
        double y = 0.8;

        double res = divide(x, y);

        System.out.printf("%.1f / %.1f = %.4f %n", x, y, res);

        System.out.println(x / y);

        System.out.printf("'DivideFloatingPointNumbers' completed. java-%s %n", System.getProperty("java.version"));
    }

    public static double divide(double x, double y) {

        if (compare(y, 0.0) == 0) {
            return compare(x, 0.0) == 0 ? Double.NaN : Double.POSITIVE_INFINITY;
        }

        int sign = 1;

        // 'x' negative
        if (compare(x, 0.0) == -1) {
            sign *= -1;
            x = -x;
        }

        // 'y' negative
        if (compare(y, 0.0) == -1) {
            sign *= -1;
            y = -y;
        }

        // x == y
        if (compare(x, y) == 0) {
            return sign * 1.0;
        }

        // x > y
        double lo = 1.0;
        double hi = Double.MAX_VALUE;

        // x < y
        if (compare(x, y) == -1) {
            lo = 0.0;
            hi = 1.0;
        }

        double mid = lo;

        // lo < hi
        while (compare(lo, hi) == -1) {

            mid = lo + 0.5 * (hi - lo);

            int cmpRes = compare(mid * y, x);

            if (cmpRes == 0) {
                break;
            }

            if (cmpRes == -1) {
                lo = mid;
            }
            else {
                hi = mid;
            }
        }

        return sign * mid;
    }

    /**
     * Compare two doubles using normalization and epsilon.
     */
    private static int compare(double a, double b) {

        double res = (a - b) / b;

        if (res < NEG_EPSILON) {
            return -1;
        }

        if (res > EPSILON) {
            return 1;
        }

        return 0;
    }

    public static void main(String[] args) {
        try {
            new DivideFloatingPointNumbers();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

}
