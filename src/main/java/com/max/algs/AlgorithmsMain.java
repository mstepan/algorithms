package com.max.algs;


import com.max.algs.concurrency.lock.ReentrantLock;
import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.TimeUnit;


public final class AlgorithmsMain {


    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());

    private AlgorithmsMain() throws Exception {

        Thread lockDetector = new LockDetector();
        lockDetector.start();

        ReentrantLock lock1 = new ReentrantLock();
        ReentrantLock lock2 = new ReentrantLock();

        Thread th1 = new Thread(() -> {

            LOG.info("thread-1 started");

            try {
                lock1.lock();
                TimeUnit.SECONDS.sleep(1);
                lock2.lock();
            }
            catch (InterruptedException interEx) {
                Thread.currentThread().interrupt();
            }

        }, "thread-1");
        th1.start();

        Thread th2 = new Thread(() -> {

            LOG.info("thread-2 started");

            try {
                lock2.lock();

                TimeUnit.SECONDS.sleep(1);

                lock1.lock();
            }
            catch (InterruptedException interEx) {
                Thread.currentThread().interrupt();
            }
        }, "thread-2");
        th2.start();

        th1.join();
        th2.join();

        LOG.info("AlgorithmsMain done...");
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
