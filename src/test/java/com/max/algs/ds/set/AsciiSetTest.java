package com.max.algs.ds.set;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @see com.max.algs.ds.set.AsciiSet
 */
public class AsciiSetTest {

    @Test
    public void add() {
        AsciiSet set = new AsciiSet();
        assertFalse(set.add('a'));
        assertFalse(set.add('z'));
        assertFalse(set.add('k'));
        assertTrue(set.add('z'));
        assertTrue(set.add('a'));
        assertTrue(set.add('a'));
        assertTrue(set.add('k'));

        assertFalse(set.add('w'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addNotAsciiChar() {
        AsciiSet set = new AsciiSet();
        set.add('\uFFFF');
    }

    @Test
    public void contains() {
        AsciiSet set = new AsciiSet();

        assertFalse(set.contains('m'));
        assertFalse(set.contains('j'));
        assertFalse(set.contains('p'));

        set.add('m');
        assertTrue(set.contains('m'));
        assertFalse(set.contains('j'));
        assertFalse(set.contains('p'));

        set.add('j');
        set.add('p');
        assertTrue(set.contains('m'));
        assertTrue(set.contains('j'));
        assertTrue(set.contains('p'));
    }

}
