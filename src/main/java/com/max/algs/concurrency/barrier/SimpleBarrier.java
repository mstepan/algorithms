package com.max.algs.concurrency.barrier;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple barrier implementation, with some bug.
 */
public final class SimpleBarrier implements Barrier {

    private final int capacity;
    private final AtomicInteger count;

    public SimpleBarrier(int capacity) {
        this.capacity = capacity;
        this.count = new AtomicInteger(capacity);
    }

    public int await() {

        int value = count.decrementAndGet();

        if (value == 0) {
            //Race condition here can break the barrier.
            count.set(capacity);
        }
        else {
            while (count.get() != 0) {
                // busy spin here
            }
        }

        return value;
    }
}
