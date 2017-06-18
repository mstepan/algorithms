package com.max.algs.ds.stack;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Stack that use max heap as implementation.
 * Not thread safe.
 *
 * @author Maksym Stepanenko
 */
public class StackFromHeap {

    private final Queue<Entry> maxHeap = new PriorityQueue<>(Entry.PRIORITY_DESC_CMP);

    private int priority = Integer.MIN_VALUE;

    /**
     * time: O(lgN)
     */
    public void push(int value) {

        if (priority == Integer.MAX_VALUE) {
            throw new IllegalStateException("Max stack size reached");
        }

        maxHeap.add(new Entry(priority, value));
        ++priority;
    }

    /**
     * time: O(lgN)
     */
    public int pop() {
        Entry entry = maxHeap.poll();

        /** reset priority index */
        if (maxHeap.isEmpty()) {
            priority = Integer.MIN_VALUE;
        }

        return entry.value;
    }

    /**
     * time: O(1)
     */
    public int top() {
        return maxHeap.peek().value;
    }

    /**
     * time: O(1)
     */
    public int size() {
        return maxHeap.size();
    }

    /**
     * time: O(1)
     */
    public boolean isEmpty() {
        return maxHeap.isEmpty();
    }


    private static final class Entry {

        private static final Comparator<Entry> PRIORITY_DESC_CMP = new Comparator<Entry>() {

            @Override
            public int compare(Entry e1, Entry e2) {
                return Integer.compare(e2.priority, e1.priority);
            }

        };

        final int priority;
        final int value;


        Entry(int priority, int value) {
            super();
            this.priority = priority;
            this.value = value;
        }

        @Override
        public String toString() {
            return value + " (" + priority + ")";
        }


    }
}
