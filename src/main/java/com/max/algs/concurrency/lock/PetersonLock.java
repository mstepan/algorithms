package com.max.algs.concurrency.lock;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PetersonLock {

    private final int[] flags = new int[2];
    private final AtomicInteger lastId = new AtomicInteger(-1);
    private final ConcurrentMap<Thread, Integer> threadIds = new ConcurrentHashMap<>();
    private volatile int victim = -1;

    public void lock() {

        Integer id = threadIds.computeIfAbsent(Thread.currentThread(), key -> lastId.incrementAndGet());

        flags[id] = 1;
        victim = id;

        int j = lastId.intValue() - id;

        // spin here
        while (flags[j] == 1 && victim == id) {
        }
    }


    public void unlock() {
        int id = threadIds.get(Thread.currentThread());
        flags[id] = 0;
    }


}
