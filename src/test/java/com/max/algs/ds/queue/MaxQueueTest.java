package com.max.algs.ds.queue;

import org.junit.Test;

import static org.junit.Assert.*;

public class MaxQueueTest {


    @Test(expected = IllegalStateException.class)
    public void maxFromEmptyQueue() {
        MaxQueue queue = new MaxQueue();
        queue.max();
    }

    @Test(expected = IllegalStateException.class)
    public void pollFromEmptyQueue() {
        MaxQueue queue = new MaxQueue();
        queue.poll();
    }

    @Test
    public void addPollAndMax() {
        MaxQueue queue = new MaxQueue();

        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());

        // add 10
        queue.add(10);

        assertFalse(queue.isEmpty());
        assertEquals(1, queue.size());
        assertEquals(10, queue.max());

        // add 5,3,9,4
        queue.add(5);
        queue.add(3);
        queue.add(9);
        queue.add(4);

        assertFalse(queue.isEmpty());
        assertEquals(5, queue.size());
        assertEquals(10, queue.max());

        // poll 10
        assertEquals(10, queue.poll());
        assertFalse(queue.isEmpty());
        assertEquals(4, queue.size());
        assertEquals(9, queue.max());

        // poll 5, 3
        assertEquals(5, queue.poll());
        assertEquals(3, queue.poll());
        assertFalse(queue.isEmpty());
        assertEquals(2, queue.size());
        assertEquals(9, queue.max());

        // poll 9
        assertEquals(9, queue.poll());
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.size());
        assertEquals(4, queue.max());

        // add 13
        queue.add(13);
        assertFalse(queue.isEmpty());
        assertEquals(2, queue.size());
        assertEquals(13, queue.max());

        // poll 4, 13
        assertEquals(4, queue.poll());
        assertEquals(13, queue.poll());
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
    }

}
