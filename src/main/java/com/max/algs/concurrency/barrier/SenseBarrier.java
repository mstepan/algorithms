package com.max.algs.concurrency.barrier;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *  Sense-Reversing Barrier.
 */
public final class SenseBarrier implements Barrier {

    private final int initialCapacity;
    private final AtomicInteger count;
    private final ThreadLocal<Boolean> threadLocalSense;

    private volatile boolean sense = false;

    public SenseBarrier(int initialCapacity) {
        this.initialCapacity = initialCapacity;
        this.count = new AtomicInteger(initialCapacity);
        this.threadLocalSense = ThreadLocal.withInitial(() -> !sense);
    }

    @Override
    public int await() {
        int curCount = count.decrementAndGet();
        boolean mySense = threadLocalSense.get();

        if (curCount == 0) {
            count.set(initialCapacity);
            sense = mySense;
        }
        else {
            while (sense != mySense) {
                // busy spin
            }
        }

        threadLocalSense.set(!mySense);

        return curCount;
    }
}
