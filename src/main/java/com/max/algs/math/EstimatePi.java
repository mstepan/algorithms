package com.max.algs.math;


import java.math.BigDecimal;
import java.util.Random;

public final class EstimatePi {

    private static final Random RAND = new Random();

    /**
     * Estimate PI using Monte-Carlo method.
     */
    public static double estimatePi(int itCount) {

        int withinCircle = 0;

        for (int i = 0; i < itCount; ++i) {

            double x = RAND.nextDouble();
            double y = RAND.nextDouble();

            if (Double.compare(x * x + y * y, 1.0) < 0) {
                ++withinCircle;
            }
        }

        double lambda = ((double) (withinCircle)) / itCount;

        return 4.0 * lambda;
    }

    private static BigDecimal calculateAsBigDecimal(double withinCircle, int itCount) {
        BigDecimal lambda =
                new BigDecimal(withinCircle).divide(new BigDecimal(itCount), 11, BigDecimal.ROUND_HALF_UP);
        return new BigDecimal("4.0").multiply(lambda);
    }

    private EstimatePi() throws Exception {

        long startTime = System.currentTimeMillis();
        double pi = estimatePi(100_000_000);
        long endTime = System.currentTimeMillis();

        System.out.printf("estimated PI: %.10f %n", pi);
        System.out.printf("real      PI: %.10f %n", Math.PI);
        System.out.printf("Time spend: %d ms%n", (endTime - startTime));

        System.out.printf("EstimatePi done: java-%s %n", System.getProperty("java.version"));
    }

    public static void main(String[] args) {
        try {
            new EstimatePi();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

