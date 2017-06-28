package com.max.algs.concurrency.dphilosophers;


import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @link https://en.wikipedia.org/wiki/Dining_philosophers_problem
 * <p>
 * Try lock solution to dining philosophers problem.
 */
public final class DiningPhilosophersTryLock {

    private static final Logger LOG = Logger.getLogger(DiningPhilosophersTryLock.class);

    private DiningPhilosophersTryLock() throws Exception {

        final int philosophersCnt = 4;

        ExecutorService pool = Executors.newFixedThreadPool(philosophersCnt);

        Lock[] forks = new ReentrantLock[philosophersCnt];
        Arrays.fill(forks, new ReentrantLock());

        List<Future<Integer>> futures = new ArrayList<>();

        for (int i = 0; i < philosophersCnt; ++i) {

            final int threadId = i;

            futures.add(pool.submit(() -> {

                System.out.printf("phil-%d started %n", threadId);

                final Random rand = new Random();

                Lock left = forks[threadId];
                Lock right = (threadId == 0) ? forks[forks.length - 1] : forks[threadId - 1];

                int eatingCnt = 0;

                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        if (left.tryLock(200, TimeUnit.MILLISECONDS)) {
                            if (right.tryLock(200, TimeUnit.MILLISECONDS)) {

                                System.out.printf("phil-%d eating %n", threadId);
                                ++eatingCnt;
                                TimeUnit.MILLISECONDS.sleep(rand.nextInt(200));

                                right.unlock();
                            }

                            left.unlock();

                            TimeUnit.MILLISECONDS.sleep(rand.nextInt(200));
                        }


                    }
                    catch (InterruptedException interEx) {
                        Thread.currentThread().interrupt();
                    }
                }

                return eatingCnt;
            }));
        }

        TimeUnit.SECONDS.sleep(15);

        pool.shutdownNow();
        pool.awaitTermination(2, TimeUnit.SECONDS);

        // wait till all threads completed
        for (int i = 0; i < futures.size(); ++i) {
            System.out.printf("phil-%d, eatingCnt = %d %n", i, futures.get(i).get());
        }

        System.out.printf("DiningPhilosophers done: java-%s %n", System.getProperty("java.version"));

    }

    public static void main(String[] args) {
        try {
            new DiningPhilosophersTryLock();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

}
