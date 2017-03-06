package com.max.algs.concurrency.lock;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Two threads non blocking mutual exclusion algorithm.
 */
public class DeckersLock {

    private final AtomicBoolean[] wants_to_enter = {new AtomicBoolean(false), new AtomicBoolean(false)};
    private volatile int turn = 0;

    public void lock() {

        int id = (int) (Thread.currentThread().getId() & 1);
        int other = 1 - id;

        wants_to_enter[id].set(true);

        while (wants_to_enter[other].get()) {
            if (turn != id) {
                wants_to_enter[id].set(false);

                while (turn != id) {
                    // busy wait
                }
                wants_to_enter[id].set(true);
            }
        }
        // critical section enter
    }

    public void unlock() {

        int id = (int) (Thread.currentThread().getId() & 1);
        int other = 1 - id;

        turn = other;
        wants_to_enter[id].set(false);
    }
}
