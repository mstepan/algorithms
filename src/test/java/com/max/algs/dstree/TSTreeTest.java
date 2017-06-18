package com.max.algs.dstree;

import com.max.algs.ds.dstree.TStree;
import org.junit.Test;

import static org.junit.Assert.*;


public class TSTreeTest {


    private static void assertEquals(Integer value1, Integer value2) {
        org.junit.Assert.assertEquals(value1, value2);
    }

    @Test
    public void add() {

        TStree<Integer> tree = new TStree<Integer>();

        assertEquals(0, tree.size());
        assertTrue(tree.isEmpty());

        assertEquals(null, tree.put("one", 2));
        assertEquals(2, tree.put("one", 1));

        assertEquals(1, tree.size());
        assertFalse(tree.isEmpty());
        assertEquals(1, tree.getValue("one"));

        tree.put("two", 2);

        assertEquals(2, tree.size());
        assertEquals(1, tree.getValue("one"));
        assertEquals(2, tree.getValue("two"));

        tree.put("three", 3);

        assertEquals(3, tree.size());
        assertEquals(1, tree.getValue("one"));
        assertEquals(2, tree.getValue("two"));
        assertEquals(3, tree.getValue("three"));

        tree.put("four", 4);
        tree.put("five", 5);

        assertEquals(5, tree.size());
        assertEquals(1, tree.getValue("one"));
        assertEquals(2, tree.getValue("two"));
        assertEquals(3, tree.getValue("three"));
        assertEquals(4, tree.getValue("four"));
        assertEquals(5, tree.getValue("five"));

        assertSame(null, tree.getValue("fives"));
        assertSame(null, tree.getValue("fiv"));


        // "two" is PREFIX string for "twos"
        tree.put("twos", 22);
        assertEquals(22, tree.getValue("twos"));

        // "on" is PREFIX string for "one"
        tree.put("on", 33);
        assertEquals(33, tree.getValue("on"));


        tree.put("apple", 177);
        assertEquals(177, tree.getValue("apple"));

        tree.put("app", 567);
        assertEquals(567, tree.getValue("app"));
    }

}
