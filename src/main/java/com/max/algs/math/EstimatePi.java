package com.max.algs.math;


import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

public final class EstimatePi {

    private static final Logger LOG = Logger.getLogger(EstimatePi.class);

    private EstimatePi() throws Exception {

        /*
        //------------
        sequential:
        //------------
        estimated PI: 3.1415803040
        real      PI: 3.1415926536
        Time spend: 16572 ms

        //------------
        parallel:
        //------------
        estimated PI: 3.1415694240
        real      PI: 3.1415926536
        Time spend: 3347 ms
         */

        final int itCount = 2_000_000_000;

        long startTime = System.currentTimeMillis();

//        double pi = estimatePi(itCount);
        double pi = estimatePiParallel(itCount);

        long endTime = System.currentTimeMillis();

        System.out.printf("estimated PI: %.10f %n", pi);
        System.out.printf("real      PI: %.10f %n", Math.PI);
        System.out.printf("Time spend: %d ms%n", (endTime - startTime));

        System.out.printf("EstimatePi done: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * Estimate PI using Monte-Carlo method.
     */
    public static double estimatePi(int itCount) {
        double lambda = ((double) (hitCount(itCount))) / itCount;
        return 4.0 * lambda;
    }

    /**
     * Estimate PI using Monte-Carlo method.
     */
    public static double estimatePiParallel(int itCount) {
        double lambda = ((double) (hitCountParallel(itCount))) / itCount;
        return 4.0 * lambda;
    }

    private static int hitCount(int itCount) {

        Random rand = ThreadLocalRandom.current();

        int withinCircle = 0;

        for (int i = 0; i < itCount; ++i) {

            double x = rand.nextDouble();
            double y = rand.nextDouble();

            if (Double.compare(x * x + y * y, 1.0) < 0) {
                ++withinCircle;
            }
        }

        return withinCircle;
    }

    public static int hitCountParallel(int itCount) {

        final int threadsCount = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = Executors.newFixedThreadPool(threadsCount);

        int chunkSize = (int) Math.ceil(((double) itCount) / threadsCount);

        @SuppressWarnings("unchecked")
        Future<Integer>[] res = new Future[threadsCount];

        int totalSize = 0;
        int lastChunkSize = (itCount % chunkSize == 0) ? chunkSize : itCount % chunkSize;

        for (int i = 0; i < threadsCount; ++i) {
            int curItCount = (i == threadsCount - 1) ? lastChunkSize : chunkSize;

            totalSize += curItCount;

            res[i] = pool.submit(() -> hitCount(curItCount));
        }

        pool.shutdownNow();
        System.out.println("totalSize: " + totalSize);

        int totalHitCount = 0;

        for (Future<Integer> future : res) {
            try {
                totalHitCount += future.get();
            }
            catch (Exception ex) {
                LOG.error(ex.getMessage(), ex);
            }
        }

        return totalHitCount;
    }

    private static BigDecimal calculateAsBigDecimal(double withinCircle, int itCount) {
        BigDecimal lambda =
                new BigDecimal(withinCircle).divide(new BigDecimal(itCount), 11, BigDecimal.ROUND_HALF_UP);
        return new BigDecimal("4.0").multiply(lambda);
    }

    public static void main(String[] args) {
        try {
            new EstimatePi();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}

