package com.max.algs.concurrency;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class BoundedBlockingQueue<T> {

    private final T[] arr;
    private final Lock mutex = new ReentrantLock();
    private final Condition notEmpty = mutex.newCondition();
    private final Condition notFull = mutex.newCondition();
    private int size = 0;
    private int head = 0;
    private int tail = 0;


    @SuppressWarnings("unchecked")
    public BoundedBlockingQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity can't be negative or '0': " + capacity);
        }
        arr = (T[]) new Object[capacity];
    }


    public void offer(T value) throws InterruptedException {

        mutex.lock();
        try {
            try {
                while (size == arr.length) {
                    notFull.await();
                }

                arr[tail] = value;
                tail = (tail + 1) % arr.length;
                ++size;
                notEmpty.signalAll();
            }
            catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw ex;
            }

        }
        finally {
            mutex.unlock();
        }

    }


    public T take() throws InterruptedException {

        T res = null;

        mutex.lock();
        try {
            try {
                while (size == 0) {
                    notEmpty.await();
                }

                res = arr[head];
                arr[head] = null;
                head = (head + 1) % arr.length;
                --size;

                notFull.signalAll();
            }
            catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw ex;
            }
        }
        finally {
            mutex.unlock();
        }

        return res;
    }

}
