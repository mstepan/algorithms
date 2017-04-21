package com.max.algs.epi.heaps;


import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Queue;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * 11.7. Implement a stack API using a heap.
 */
public class StackApiUsingHeap {

    /**
     * Not thread safe.
     */
    private static final class StackAdapter {

        // allow to store 2^32 values
        private int sequenceNo = Integer.MIN_VALUE;

        private final Queue<StackEntry> maxHeap = new PriorityQueue<>(Collections.reverseOrder());

        /**
         * time: O(lgN)
         */
        public void push(int value) {

            if (sequenceNo == Integer.MAX_VALUE) {
                throw new IllegalStateException("Possible overflow");
            }

            maxHeap.add(new StackEntry(value, sequenceNo));
            ++sequenceNo;
        }

        /**
         * time: O(lgN)
         */
        public int pop() {

            if (isEmpty()) {
                throw new IllegalStateException("Can't 'pop' from empty array");
            }

            StackEntry entry = maxHeap.poll();

            --sequenceNo;

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
            return size() == 0;
        }

        private static final class StackEntry implements Comparable<StackEntry> {
            final int value;
            final int sequence;

            StackEntry(int value, int sequence) {
                this.value = value;
                this.sequence = sequence;
            }

            @Override
            public int compareTo(@NotNull StackEntry other) {
                return Integer.compare(sequence, other.sequence);
            }
        }
    }

    @Test
    public void pushAndPop() {
        StackAdapter stack = new StackAdapter();

        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());

        stack.push(10);
        assertEquals(10, stack.top());
        assertFalse(stack.isEmpty());
        assertEquals(1, stack.size());

        stack.push(1);
        stack.push(5);
        assertEquals(5, stack.pop());
        assertEquals(1, stack.pop());

        stack.push(7);
        stack.push(12);
        stack.push(4);

        assertEquals(4, stack.pop());
        assertEquals(12, stack.pop());
        assertEquals(7, stack.pop());
        assertEquals(10, stack.pop());
        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());
    }

    @Test
    public void pushAndPopNoOverflow() {
        StackAdapter stack = new StackAdapter();

        for (long i = 0; i < (2L * Integer.MAX_VALUE) + 2L; ++i) {
            stack.push((int) i);
            stack.pop();
        }

    }


    public static void main(String[] args) {
        try {
            JUnitCore junit = new JUnitCore();
            Result result = junit.run(StackApiUsingHeap.class);

            for (Failure failure : result.getFailures()) {
                System.out.println(failure.getTrace());
            }

            System.out.println("'StackApiUsingHeap' completed");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
