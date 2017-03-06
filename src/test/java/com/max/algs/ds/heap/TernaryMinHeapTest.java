package com.max.algs.ds.heap;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TernaryMinHeapTest {

    @Test(expected =  IllegalStateException.class)
    public void extractFromEmptyHeap() {
        TernaryMinHeap<Integer> heap = new TernaryMinHeap<>();

        for( int i = 0; i < 17; i++){
            heap.add(i);
        }

        while( ! heap.isEmpty() ){
            heap.extract();
        }
        heap.extract();
    }

    @Test
    public void extract() {
        TernaryMinHeap<Integer> heap = new TernaryMinHeap<>();

        heap.add(5);
        heap.add(10);
        heap.add(2);
        heap.add(3);
        heap.add(7);
        heap.add(9);
        heap.add(6);
        heap.add(1);
        heap.add(4);
        heap.add(6);

        assertEquals(10, heap.size());
        assertDataEquals(new Integer[]{1, 6, 2, 3, 10, 9, 7, 5, 4, 6}, heap.data);

        assertEquals(new Integer(1), heap.min());
        assertEquals(new Integer(1), heap.min());
        assertEquals(new Integer(1), heap.extract());
        assertEquals(9, heap.size());
        assertDataEquals(new Integer[]{2, 6, 4, 3, 10, 9, 7, 5, 6}, heap.data);

        assertEquals(new Integer(2), heap.extract());
        assertEquals(8, heap.size());
        assertDataEquals(new Integer[]{3, 6, 4, 6, 10, 9, 7, 5}, heap.data);

        assertEquals(new Integer(3), heap.extract());
        assertEquals(new Integer(4), heap.extract());
        assertEquals(new Integer(5), heap.extract());
        assertEquals(5, heap.size());
        assertDataEquals(new Integer[]{6,9,7,6,10}, heap.data);

        assertEquals(new Integer(6), heap.extract());
        assertEquals(new Integer(6), heap.extract());
        assertEquals(3, heap.size());
        assertDataEquals(new Integer[]{7,9,10}, heap.data);

        assertEquals(new Integer(7), heap.min());
        assertEquals(new Integer(7), heap.extract());
        assertEquals(new Integer(9), heap.min());
        assertEquals(new Integer(9), heap.extract());
        assertEquals(1, heap.size());
        assertDataEquals(new Integer[]{10}, heap.data);

        assertEquals(new Integer(10), heap.extract());
        assertEquals(0, heap.size());
        assertTrue(heap.isEmpty());
    }


    @Test
    public void add() {
        TernaryMinHeap<Integer> heap = new TernaryMinHeap<>();

        assertTrue(heap.isEmpty());
        assertEquals(0, heap.size());

        assertDataEquals(new Integer[]{}, heap.data);

        heap.add(5);
        assertFalse(heap.isEmpty());
        assertEquals(1, heap.size());
        assertDataEquals(new Integer[]{5}, heap.data);

        heap.add(10);
        assertEquals(2, heap.size());
        assertDataEquals(new Integer[]{5, 10}, heap.data);

        heap.add(2);
        assertEquals(3, heap.size());
        assertDataEquals(new Integer[]{2, 10, 5}, heap.data);

        heap.add(3);
        heap.add(7);
        heap.add(9);
        heap.add(6);
        assertDataEquals(new Integer[]{2, 6, 5, 3, 10, 9, 7}, heap.data);

        heap.add(1);
        heap.add(4);
        heap.add(6);
        assertEquals(10, heap.size());
        assertDataEquals(new Integer[]{1, 6, 2, 3, 10, 9, 7, 5, 4, 6}, heap.data);
    }

    private <T> void assertDataEquals(T[] expected, T[] actual) {

        for (int i = 0; i < expected.length; i++) {
            if (actual[i] != expected[i]) {
                throw new AssertionError("Arrays aren't equals");
            }
        }

    }

}
