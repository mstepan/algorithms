package com.max.algs.util;


import org.apache.log4j.Logger;

public final class DoubleUtils {

    private static final Logger LOG = Logger.getLogger(DoubleUtils.class);

    private DoubleUtils() {
    }

    /**
     * Compare two double values for equality using epsilon value.
     * <p>
     * if (Abs(x – y) <= EPSILON * Max(1.0f, Abs(x), Abs(y))
     */
    public static boolean isEquals(double a, double b) {
        double epsilon = Math.max(Math.ulp(a), Math.ulp(b));
        return Math.abs(a - b) <= epsilon * max(1.0, Math.abs(a), Math.abs(b));
    }

    private static double max(double x, double y, double z) {
        return Math.max(x, Math.max(y, z));
    }

    public static void main(String[] args) {
        LOG.info(isEquals(1.0000001234, 1.0000001234));
    }

}
