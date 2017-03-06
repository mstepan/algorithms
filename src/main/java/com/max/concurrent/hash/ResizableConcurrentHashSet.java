package com.max.concurrent.hash;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ResizableConcurrentHashSet implements ConcurrentSet {

    private static final int DEFAULT_CAPACITY = 8;
    private static final double LOAD_FACTOR = 0.75;

    private final AtomicMarkableReference<Thread> owner;

    private volatile List<Integer>[] buckets;
    private volatile Lock[] locks;

    private volatile int size;

    public ResizableConcurrentHashSet() {
        this(DEFAULT_CAPACITY);
    }

    public ResizableConcurrentHashSet(int capacity) {
        initBuckets(capacity);
        owner = new AtomicMarkableReference<>(null, false);
    }

    @Override
    public boolean add(int value) throws InterruptedException {

        if (shouldResize()) {
            resize();
        }

        Lock mutex = lockBucket(value);

        try {
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

        Lock mutex = lockBucket(value);

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

        Lock mutex = lockBucket(value);

        try {
            return buckets[hash(value) % buckets.length].contains(value);
        }
        finally {
            mutex.unlock();
        }
    }

    private Lock lockBucket(int value) throws InterruptedException {

        boolean[] mark = {true};

        Thread me = Thread.currentThread();
        Thread who;

        while (true) {

            do {
                who = owner.get(mark);
            }
            while (mark[0] && who != me);

            Lock[] oldLocks = locks;

            Lock oldLock = oldLocks[hash(value) % oldLocks.length];
            oldLock.lockInterruptibly();

            who = owner.get(mark);

            if ((!mark[0] || who == me) && locks == oldLocks) {
                return oldLock;
            }
            else {
                oldLock.unlock();
            }

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

//        LOG.info("resizing...");

        final int oldCapacity = buckets.length;
        final Thread me = Thread.currentThread();

        if (owner.compareAndSet(null, me, false, true)) {
            try {
                // someone else has resized the table
                if (oldCapacity != buckets.length) {
                    return;
                }

                waitAllLocks();

                List[] oldBuckets = buckets;

                initBuckets(oldBuckets.length << 1);

                copyBuckets(oldBuckets);
            }
            finally {
                owner.set(null, false);
            }
        }

    }

    @SuppressWarnings("unchecked")
    private void initBuckets(int capacity) {
        buckets = new List[capacity];
        locks = new Lock[capacity];

        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new ArrayList<>();
            locks[i] = new ReentrantLock();
        }
    }

    private void waitAllLocks() {
        for (Lock singleLock : locks) {
            while (((ReentrantLock) singleLock).isLocked()) {
            }
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
