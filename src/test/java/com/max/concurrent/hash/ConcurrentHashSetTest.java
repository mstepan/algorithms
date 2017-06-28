package com.max.concurrent.hash;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class ConcurrentHashSetTest {


    @Test
    public void addWithResizing() throws InterruptedException {
        ResizableConcurrentHashSet set = new ResizableConcurrentHashSet(8);

        int itCount = 117;

        assertTrue(set.isEmpty());

        for (int i = 0; i < itCount; i++) {
            assertTrue(set.add(i));
        }

        assertEquals(itCount, set.size());
        assertFalse(set.isEmpty());

        for (int i = 0; i < itCount; i++) {
            assertTrue(set.contains(i));
        }
    }

    @Test
    public void addContains() throws InterruptedException {
        ResizableConcurrentHashSet set = new ResizableConcurrentHashSet();

        assertEquals(0, set.size());
        assertTrue(set.isEmpty());

        assertTrue(set.add(10));
        assertTrue(set.contains(10));
        assertFalse(set.contains(5));
        assertFalse(set.contains(17));
        assertEquals(1, set.size());
        assertFalse(set.isEmpty());

        assertFalse(set.add(10));
        assertEquals(1, set.size());
        assertFalse(set.isEmpty());

        assertTrue(set.add(5));
        assertTrue(set.contains(10));
        assertTrue(set.contains(5));
        assertEquals(2, set.size());
        assertFalse(set.isEmpty());
    }
}
