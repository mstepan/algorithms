package com.max.algs.concurrency.dphilosophers;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @link https://en.wikipedia.org/wiki/Dining_philosophers_problem
 * <p>
 * Another approach is to guarantee that a philosopher can only pick up both
 * forks or none by introducing an arbitrator, e.g., a waiter. In order to
 * pick up the forks, a philosopher must ask permission of the waiter.
 */
public final class DiningPhilosophersArbiter {

    private DiningPhilosophersArbiter() throws Exception {

        final int philosophersCnt = 4;

        ExecutorService pool = Executors.newFixedThreadPool(philosophersCnt);

        Semaphore waiter = new Semaphore(1);

        Lock[] forks = new ReentrantLock[philosophersCnt];
        Arrays.fill(forks, new ReentrantLock());

        List<Future<Integer>> futures = new ArrayList<>();

        for (int i = 0; i < philosophersCnt; ++i) {

            final int threadId = i;

            futures.add(pool.submit(() -> {

                System.out.printf("phil-%d started %n", threadId);

                Lock left = forks[threadId];
                Lock right = (threadId == 0) ? forks[forks.length - 1] : forks[threadId - 1];

                Random rand = new Random();

                int eatingCnt = 0;

                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        waiter.acquire();

                        if (rand.nextBoolean()) {
                            left.lock();
                            right.lock();
                        }
                        else {
                            right.lock();
                            left.lock();
                        }

                        System.out.printf("phil-%d eating %n", threadId);
                        ++eatingCnt;
                        TimeUnit.MILLISECONDS.sleep(rand.nextInt(200));

                        left.unlock();
                        right.unlock();

                        waiter.release();

                        TimeUnit.MILLISECONDS.sleep(rand.nextInt(200));
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
            new DiningPhilosophersArbiter();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
