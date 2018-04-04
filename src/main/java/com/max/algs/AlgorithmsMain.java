package com.max.algs;


import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public final class AlgorithmsMain {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());

    private static final Random RAND = ThreadLocalRandom.current();

    private AlgorithmsMain() throws Exception {

        for (int test = 0; test < 100; ++test) {
            int[] arr = randomInts(10_000, 100);

            if (stockmaxGreedy(arr) != stockmaxDynamic(arr)) {
                throw new IllegalStateException("Algorithms returned different results.");
            }

            System.out.println("test: " + test + " passed");
        }

        LOG.info("AlgorithmsMain done...");
    }

    private static int[] randomInts(int length, int maxValue) {
        int[] arr = new int[length];

        for (int i = 0; i < arr.length; ++i) {
            arr[i] = RAND.nextInt(maxValue);
        }

        return arr;
    }

    /**
     * time: O(N)
     * space: O(1)
     */
    static long stockmaxGreedy(int[] prices) {

        int maxSoFar = 0;

        long totalProfit = 0L;

        for (int i = prices.length - 1; i >= 0; --i) {

            if (prices[i] >= maxSoFar) {
                maxSoFar = prices[i];
            }
            else {
                totalProfit += (maxSoFar - prices[i]);
            }
        }

        return totalProfit;
    }


    /**
     * time: O(N^2)
     * space: O(N)
     */
    static long stockmaxDynamic(int[] prices) {

        final int n = prices.length;

        long[] opt = new long[n + 1];

        for (int lastDay = 2; lastDay < opt.length; ++lastDay) {

            long curOpt = 0L;

            final long lastDayPrice = prices[lastDay - 1];
            long windowSum = 0L;
            long windowCnt = 0L;

            long lastPartProfit = 0L;

            for (int k = lastDay; k > 0; --k) {

                if (prices[k - 1] < lastDayPrice) {
                    windowSum += prices[k - 1];
                    ++windowCnt;
                    lastPartProfit = (windowCnt * lastDayPrice - windowSum);
                }

                curOpt = Math.max(curOpt, opt[k - 1] + lastPartProfit);
            }

            opt[lastDay] = curOpt;
        }

        return opt[n];
    }


    public static void main(String[] args) {
        try {
            new AlgorithmsMain();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }


}
