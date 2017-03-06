package com.max.algs.ds.tree;

import org.junit.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class TreapTest {

    private static final Random RAND = new Random();

    @Test
    public void addAndContainsRandomData() {

        Set<Integer> actualSet = new HashSet<>();
        Treap treap = new Treap();

        final int count = 1000;

        for (int i = 0; i < count; i++) {
            int value = RAND.nextInt(count);
            assertEquals(actualSet.add(value), treap.add(value));
        }

        System.out.println("treap.size: " + treap.size());
        assertEquals(actualSet.size(), treap.size());

        for (int actualValue : actualSet) {
            assertTrue(treap.contains(actualValue));
        }

        for (int i = 0; i < 100; i++) {
            assertFalse(treap.contains(count + i));
        }
    }

    @Test
    public void addAndContains() {
        Treap data = new Treap();
        data.add(10);
        assertTrue(data.contains(10));
        assertFalse(data.contains(5));
        assertFalse(data.contains(3));
        assertFalse(data.contains(15));
        assertFalse(data.contains(22));
        assertEquals(1, data.size());

        data.add(5);
        assertTrue(data.contains(10));
        assertTrue(data.contains(5));
        assertFalse(data.contains(3));
        assertFalse(data.contains(15));
        assertFalse(data.contains(22));
        assertEquals(2, data.size());

        data.add(13);
        assertTrue(data.contains(10));
        assertTrue(data.contains(5));
        assertTrue(data.contains(13));
        assertFalse(data.contains(15));
        assertFalse(data.contains(22));
        assertEquals(3, data.size());

        data.add(15);
        assertTrue(data.contains(10));
        assertTrue(data.contains(5));
        assertTrue(data.contains(13));
        assertTrue(data.contains(15));
        assertFalse(data.contains(22));
        assertEquals(4, data.size());

        data.add(22);
        assertTrue(data.contains(10));
        assertTrue(data.contains(5));
        assertTrue(data.contains(13));
        assertTrue(data.contains(15));
        assertTrue(data.contains(22));
        assertFalse(data.contains(-30));
        assertFalse(data.contains(0));
        assertFalse(data.contains(133));
        assertEquals(5, data.size());
    }
}
