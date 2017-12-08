package com.max.algs.creative;

import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.ThreadLocalRandom;

import static com.google.common.base.Preconditions.checkNotNull;

final class MaxConsSubarrayProduct {


    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * time: O(N^2)
     * space: O(1)
     */
    private static double maxConsSubarrayProductBruteforce(double[] arr) {
        checkNotNull(arr);

        double maxSoFar = 1.0;

        for (int i = 0; i < arr.length; ++i) {

            double cur = 1.0;

            for (int j = i; j < arr.length; ++j) {
                cur *= arr[j];
                maxSoFar = Math.max(cur, maxSoFar);
            }
        }

        return maxSoFar;
    }

    /**
     * time: O(N)
     * space: O(1)
     */
    private static double maxConsSubarrayProduct(double[] arr) {
        checkNotNull(arr);

        double maxSoFar = 1.0;
        double lastPos = 1.0;
        double lastNeg = 1.0;

        for (double val : arr) {

            // val == 0.0
            if (isZero(val)) {
                lastPos = 1.0;
                lastNeg = 1.0;
            }

            // val > 0
            else if (isPositive(val)) {
                lastPos = Math.max(1.0, lastPos * val);

                // has negative values before
                if (isNegative(lastNeg)) {
                    lastNeg = lastNeg * val;
                }
            }

            // val < 0
            else {

                double lastPosNew;
                double lastNegNew;

                // no negative before
                if (isPositive(lastNeg)) {
                    lastPosNew = 1.0;
                    lastNegNew = Math.min(val, lastPos * val);
                }
                else {
                    lastPosNew = Math.max(1.0, lastNeg * val);
                    lastNegNew = Math.min(val, lastPos * val);
                }

                lastPos = lastPosNew;
                lastNeg = lastNegNew;
            }

            maxSoFar = Math.max(maxSoFar, lastPos);
        }

        return maxSoFar;
    }

    private static boolean isZero(double value) {
        return Double.compare(value, 0.0) == 0;
    }

    private static boolean isPositive(double value) {
        return Double.compare(value, 0.0) > 0;
    }

    private static boolean isNegative(double value) {
        return Double.compare(value, 0.0) < 0;
    }

    private MaxConsSubarrayProduct() {

        ThreadLocalRandom rand = ThreadLocalRandom.current();

        for (int it = 0; it < 1; ++it) {

            double[] arr = new double[1000];

            for (int i = 0; i < arr.length; ++i) {

                if (rand.nextInt(100) == 0) {
                    arr[i] = 0.0;
                }
                else {
                    double sign = Math.pow(-1, rand.nextInt(2));
                    arr[i] = sign * rand.nextDouble(-10.0, 11.0);
                }
            }

            double res1 = maxConsSubarrayProductBruteforce(arr);
            double res2 = maxConsSubarrayProduct(arr);

            if (Double.compare(res1, res2) != 0) {
                LOG.info("Different results returned");
                LOG.info("res1 = " + res1);
                LOG.info("res2 = " + res2);
            }
        }


        LOG.info("MaxConsSubarrayProduct done...");
    }


    public static void main(String[] args) {
        try {
            new MaxConsSubarrayProduct();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }


}
