package com.max.algs.ds.set;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DisjointSetTest {

    DisjointSet<Integer> set;

    @Before
    public void setUp() {
        set = new DisjointSet<>();
        set.add(1);
        set.add(2);
        set.add(3);
        set.add(4);
        set.add(5);
    }


    @After
    public void tearDown() {
        set = null;
    }


    @Test(expected = IllegalArgumentException.class)
    public void unionNotExistedSet() {
        set.union(1, 133);
    }

    @Test
    public void unionFind() {

        assertEquals(5, set.size());
        assertEquals(Integer.valueOf(1), set.find(1));
        assertEquals(Integer.valueOf(2), set.find(2));

        set.union(1, 2);
        assertEquals(4, set.size());
        assertEquals(Integer.valueOf(1), set.find(1));
        assertEquals(Integer.valueOf(1), set.find(2));

        set.union(1, 3);
        assertEquals(3, set.size());
        assertEquals(Integer.valueOf(1), set.find(1));
        assertEquals(Integer.valueOf(1), set.find(2));
        assertEquals(Integer.valueOf(1), set.find(3));

        assertEquals(Integer.valueOf(4), set.find(4));
        assertEquals(Integer.valueOf(5), set.find(5));

        set.union(4, 5);
        assertEquals(2, set.size());
        assertEquals(Integer.valueOf(4), set.find(4));
        assertEquals(Integer.valueOf(4), set.find(5));

        set.union(1, 4);
        assertEquals(1, set.size());
        assertEquals(Integer.valueOf(1), set.find(1));
        assertEquals(Integer.valueOf(1), set.find(2));
        assertEquals(Integer.valueOf(1), set.find(3));
        assertEquals(Integer.valueOf(1), set.find(4));
        assertEquals(Integer.valueOf(1), set.find(5));

        // size should be same after this union
        set.union(1, 4);
        assertEquals(1, set.size());
    }

}
