package com.max.algs.epi.heaps;


import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.util.PriorityQueue;
import java.util.Queue;

import static junit.framework.TestCase.*;

/**
 * 11.7. Variant. Implement a queue API using a heap.
 */
public class QueueApiUsingHeap {

    private static final Logger LOG = Logger.getLogger(QueueApiUsingHeap.class);

    public static void main(String[] args) {
        try {
            JUnitCore junit = new JUnitCore();
            Result result = junit.run(QueueApiUsingHeap.class);

            for (Failure failure : result.getFailures()) {
                System.out.println(failure.getTrace());
            }

            System.out.println("'QueueApiUsingHeap' completed");
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    @Test
    public void enqAndDeqWithOverflow() {
        QueueAdapter queue = new QueueAdapter();

        for (long i = 0; i < (2L * Integer.MAX_VALUE) + 2L; ++i) {
            queue.add((int) i);
            queue.poll();
        }
    }

    @Test
    public void enqAndDeq() {

        QueueAdapter queue = new QueueAdapter();

        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
        assertSame(null, queue.peek());

        queue.add(5);
        queue.add(7);
        queue.add(12);

        assertEquals(Integer.valueOf(5), queue.poll());
        assertEquals(Integer.valueOf(7), queue.poll());

        queue.add(3);
        queue.add(4);
        queue.add(10);
        queue.add(6);
        queue.add(8);

        assertEquals(Integer.valueOf(12), queue.poll());
        assertEquals(Integer.valueOf(3), queue.poll());
    }

    @Test(expected = IllegalStateException.class)
    public void pollFromEmptyQueue() {
        QueueAdapter queue = new QueueAdapter();
        queue.poll();
    }

    @Test
    public void peekFromEmptyQueue() {
        QueueAdapter queue = new QueueAdapter();
        assertSame(null, queue.peek());
    }

    private static final class QueueAdapter {

        private int sequenceNo = Integer.MIN_VALUE;

        private Queue<QueueEntry> minHeap = new PriorityQueue<>();

        /**
         * time: O(lgN), but more probably will be O(1) in a typical min heap implementation.
         */
        public void add(Integer value) {

            // If 'sequenceNo' overflowed, reinsert elements again.
            if (sequenceNo == Integer.MAX_VALUE) {

                Queue<QueueEntry> oldMinHeap = new PriorityQueue<>();
                minHeap = new PriorityQueue<>();
                sequenceNo = Integer.MIN_VALUE;

                while (!oldMinHeap.isEmpty()) {
                    minHeap.add(new QueueEntry(oldMinHeap.poll().value, sequenceNo));
                }

                System.out.println("Reinsert completed");
            }

            minHeap.add(new QueueEntry(value, sequenceNo));

            ++sequenceNo;
        }

        /**
         * time: O(lgN)
         */
        public Integer poll() {

            if (isEmpty()) {
                throw new IllegalStateException("Empty queue");
            }

            return minHeap.poll().value;

        }

        /**
         * time: O(1)
         */
        public Integer peek() {

            if (isEmpty()) {
                return null;
            }

            return minHeap.peek().value;
        }

        /**
         * time: O(1)
         */
        public int size() {
            return minHeap.size();
        }

        /**
         * time: O(1)
         */
        public boolean isEmpty() {
            return size() == 0;
        }

        private static final class QueueEntry implements Comparable<QueueEntry> {
            final Integer value;
            final int sequence;

            QueueEntry(int value, int sequence) {
                this.value = value;
                this.sequence = sequence;
            }

            @Override
            public int compareTo(@NotNull QueueEntry other) {
                return Integer.compare(sequence, other.sequence);
            }
        }
    }

}
