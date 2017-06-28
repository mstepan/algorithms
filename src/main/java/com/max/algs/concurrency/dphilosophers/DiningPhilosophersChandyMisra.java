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
 * Chandy/Misra solution
 * <p>
 * In 1984, K. Mani Chandy and J. Misra proposed a different solution to the dining philosophers problem
 * to allow for arbitrary agents (numbered P1, ..., Pn) to contend for an arbitrary number of resources,
 * unlike Dijkstra's solution. It is also completely distributed and requires no central authority after
 * initialization. However, it violates the requirement that "the philosophers do not speak to each other"
 * (due to the request messages).
 * <p>
 * 1. For every pair of philosophers contending for a resource, create a fork and give it to the philosopher
 * with the lower ID (n for agent Pn). Each fork can either be dirty or clean. Initially, all forks are dirty.
 * <p>
 * 2. When a philosopher wants to use a set of resources (i.e. eat), said philosopher must obtain the forks from
 * their contending neighbors. For all such forks the philosopher does not have, they send a request message.
 * <p>
 * 3. When a philosopher with a fork receives a request message, they keep the fork if it is clean, but give it
 * up when it is dirty. If the philosopher sends the fork over, they clean the fork before doing so.
 * <p>
 * 4. After a philosopher is done eating, all their forks become dirty. If another philosopher had previously
 * requested one of the forks, the philosopher that has just finished eating cleans the fork and sends it
 */
public final class DiningPhilosophersChandyMisra {

    private static final Logger LOG = Logger.getLogger(DiningPhilosophersChandyMisra.class);

    private DiningPhilosophersChandyMisra() throws Exception {

        final int philosophersCnt = 4;

        ExecutorService pool = Executors.newFixedThreadPool(philosophersCnt);

        Lock[] forks = new ReentrantLock[philosophersCnt];
        Arrays.fill(forks, new Stick());

        List<Future<Integer>> futures = new ArrayList<>();

        for (int i = 0; i < philosophersCnt; ++i) {

            final int threadId = i;

            futures.add(pool.submit(() -> {

                System.out.printf("phil-%d started %n", threadId);

                Random rand = new Random();

                int eatingCnt = 0;

                while (!Thread.currentThread().isInterrupted()) {
                    try {

                        System.out.printf("phil-%d eating %n", threadId);
                        ++eatingCnt;
                        TimeUnit.MILLISECONDS.sleep(rand.nextInt(200));


                        TimeUnit.MILLISECONDS.sleep(rand.nextInt(200));

                    }
                    catch (InterruptedException interEx) {
                        Thread.currentThread().interrupt();
                    }
                }

                return eatingCnt;
            }));
        }

        TimeUnit.SECONDS.sleep(10);

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
            new DiningPhilosophersChandyMisra();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    private static final class Stick {
        final Lock lock = new ReentrantLock();
        int holderId;
        boolean dirty;
    }

}
