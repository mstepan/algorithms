package com.max.algs.ds.heap;

import org.junit.Test;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

import static org.junit.Assert.assertEquals;


public class MinHeapTest {

    private static final int MIN_ARITY = 2;
    private static final int MAX_ARITY = 9;

    @Test(expected = IllegalArgumentException.class)
    public void createWithNegativeArity() {
        new MinHeap<>(-2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createWithZeroArity() {
        new MinHeap<>(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createWithOneArity() {
        new MinHeap<>(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createWithBigArity() {
        new MinHeap<>(16);
    }

    @Test(expected = IllegalArgumentException.class)
    public void pollFromEmptyHeap() {
        MinHeap<Integer> heap = new MinHeap<>();
        heap.add(133);

        heap.poll();
        heap.poll();
    }

    @Test
    public void addAndPollRandomValues() {

        Random rand = new Random();

        for (int arity = MIN_ARITY; arity <= MAX_ARITY; ++arity) {
            Queue<Integer> actualQueue = new PriorityQueue<>();
            MinHeap<Integer> heap = new MinHeap<>(arity);

            for (int i = 0; i < 1000; i++) {
                int value = rand.nextInt(1000);
                actualQueue.add(value);
                heap.add(value);
            }

            assertEquals(actualQueue.size(), heap.size());

            while (!actualQueue.isEmpty()) {
                Integer expectedValue = actualQueue.poll();
                Integer actualValue = heap.poll();

                assertEquals(expectedValue, actualValue);
                assertEquals(actualQueue.size(), heap.size());
                assertEquals(actualQueue.isEmpty(), heap.isEmpty());
            }
        }

    }
}
