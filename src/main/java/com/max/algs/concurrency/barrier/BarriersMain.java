package com.max.algs.concurrency.barrier;


import org.apache.log4j.Logger;

import java.util.Arrays;


public final class BarriersMain {


    private static final Logger LOG = Logger.getLogger(BarriersMain.class);


    private BarriersMain() {

        final int threadsCount = 300;

        int[][] matrix = new int[threadsCount][10];

        Barrier barrier = new SenseBarrier(threadsCount);
        Thread[] threads = new Thread[threadsCount];

        for (int i = 0; i < threads.length; ++i) {

            final int index = i;

            threads[i] = new Thread(() -> {

                for (int it = 0; it < 10; ++it) {

                    if (index == 0) {
                        LOG.info("iteration: " + it);
                    }

                    for (int col = 0; col < matrix[index].length; ++col) {
                        matrix[index][col] += 1;
                    }

                    barrier.await();
                }

            });
            threads[i].start();
        }

        for (Thread th : threads) {
            try {
                th.join();
            }
            catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                LOG.error(ex.getMessage(), ex);
            }
        }

        for (int[] row : matrix) {
            LOG.info(Arrays.toString(row));
        }


        LOG.info("BarriersMain done: java-" + System.getProperty("java.version"));
    }

    public static void main(String[] args) {
        try {
            new BarriersMain();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}



