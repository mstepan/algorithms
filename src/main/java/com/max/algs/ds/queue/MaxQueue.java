package com.max.algs.ds.queue;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;

/**
 * Queue which supports add, poll and max in O(1) amortized cost.
 *
 * @author Maksym Stepanenko
 */
public class MaxQueue {


    private final Queue<Integer> queue = new ArrayDeque<>();
    private final Deque<Integer> deque = new ArrayDeque<>();

    /**
     * time: O(1)
     */
    public void add(int value) {
        queue.add(value);

        while (!deque.isEmpty() && deque.peekLast() < value) {
            deque.pollLast();
        }

        deque.add(value);
    }

    /**
     * time: O(1)
     */
    public int poll() {

        checkNotEmpty();

        int value = queue.poll();

        if (deque.peekFirst() == value) {
            deque.pollFirst();
        }

        return value;
    }

    /**
     * Returns max element from this queue, but doesn't remove this element.
     * <p>
     * time: O(1)
     */
    public int max() {
        checkNotEmpty();
        return deque.peekFirst();
    }

    public int size() {
        return queue.size();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    private void checkNotEmpty() {
        if (queue.isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
    }

}
