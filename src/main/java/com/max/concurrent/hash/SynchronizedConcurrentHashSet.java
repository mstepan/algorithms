package com.max.concurrent.hash;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SynchronizedConcurrentHashSet implements ConcurrentSet {

    private static final int DEFAULT_CAPACITY = 8;
    private static final double LOAD_FACTOR = 0.75;


    private volatile List<Integer>[] buckets;
    private volatile Lock mutex;

    private volatile int size;

    public SynchronizedConcurrentHashSet() {
        this(DEFAULT_CAPACITY);
    }

    public SynchronizedConcurrentHashSet(int capacity) {
        initBuckets(capacity);
        mutex = new ReentrantLock();
    }

    @Override
    public boolean add(int value) throws InterruptedException {

        mutex.lockInterruptibly();

        try {

            if (shouldResize()) {
                resize();
            }

            List<Integer> bucket = buckets[hash(value) % buckets.length];
            if (bucket.contains(value)) {
                return false;
            }
            bucket.add(value);
            ++size;
            return true;
        }
        finally {
            mutex.unlock();
        }
    }

    @Override
    public boolean remove(int value) throws InterruptedException {

        mutex.lockInterruptibly();

        try {

            List<Integer> bucket = buckets[hash(value) % buckets.length];

            boolean wasPresent = bucket.remove(Integer.valueOf(value));

            if (wasPresent) {
                --size;
            }

            return wasPresent;
        }
        finally {
            mutex.unlock();
        }
    }

    @Override
    public boolean contains(int value) throws InterruptedException {

        mutex.lockInterruptibly();

        try {
            return buckets[hash(value) % buckets.length].contains(value);
        }
        finally {
            mutex.unlock();
        }
    }


    private int hash(int value) {
        return Math.abs(Integer.valueOf(value).hashCode());
    }

    private double loadFactor() {
        return (double) size / buckets.length;
    }

    private boolean shouldResize() {
        return Double.compare(loadFactor(), LOAD_FACTOR) >= 0;
    }

    private void resize() throws InterruptedException {

        List[] oldBuckets = buckets;

        initBuckets(oldBuckets.length << 1);

        copyBuckets(oldBuckets);
    }

    @SuppressWarnings("unchecked")
    private void initBuckets(int capacity) {

        buckets = new List[capacity];

        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new ArrayList<>();
        }
    }

    @SuppressWarnings("unchecked")
    private void copyBuckets(List[] oldBuckets) {
        for (List<Integer> prevBucket : oldBuckets) {
            for (Integer value : prevBucket) {
                buckets[hash(value) % buckets.length].add(value);
            }
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }


}
