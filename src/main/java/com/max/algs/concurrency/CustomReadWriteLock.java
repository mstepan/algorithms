package com.max.algs.concurrency;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;


public class CustomReadWriteLock implements ReadWriteLock {


    private final Object mutex = new Object();
    private final Lock readLock = new ReadLock();
    private final Lock writeLock = new WriteLock();
    private int waitingWriters;
    private int readers;
    private int writers;

    @Override
    public Lock readLock() {
        return readLock;
    }


    @Override
    public Lock writeLock() {
        return writeLock;
    }


    private class ReadLock extends AbstractLock {

        @Override
        public void lock() {
            synchronized (mutex) {
                try {

                    while (waitingWriters > 0 || writers > 0) {
                        mutex.wait();
                    }

                    ++readers;

                    if (readers > 0 && writers > 0) {
                        throw new IllegalStateException("readers: " + readers + ", writers: " + writers);
                    }

                }
                catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }


        }

        @Override
        public void unlock() {
            synchronized (mutex) {
                --readers;
                mutex.notifyAll();
            }
        }

    }

    private class WriteLock extends AbstractLock {

        @Override
        public void lock() {

            synchronized (mutex) {
                try {

                    ++waitingWriters;

                    while (readers > 0 || writers > 0) {
                        mutex.wait();
                    }

                    --waitingWriters;
                    ++writers;

                    if (readers > 0 && writers > 0) {
                        throw new IllegalStateException("readers: " + readers + ", writers: " + writers);
                    }
                }
                catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }


        }

        @Override
        public void unlock() {
            synchronized (mutex) {
                --writers;
                mutex.notifyAll();
            }

        }
    }

}
