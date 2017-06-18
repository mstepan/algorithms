package com.max.algs.ds.radix;

import org.junit.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import static org.jgroups.util.Util.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for {@link DigitalSearchTree}
 */
public class DigitalSearchTreeTest {

    private static final Random RAND = new Random();

    @Test
    public void addAndContains() {

        DigitalSearchTree tree = new DigitalSearchTree();

        assertTrue(tree.isEmpty());
        assertEquals(0, tree.size());
        assertFalse(tree.contains(10));

        tree.add(10);
        assertFalse(tree.isEmpty());
        assertEquals(1, tree.size());
        assertTrue(tree.contains(10));

        tree.add(5);
        tree.add(13);
        tree.add(133);
        tree.add(87);

        assertFalse(tree.add(10));
        assertFalse(tree.add(133));

        assertFalse(tree.isEmpty());
        assertEquals(5, tree.size());
        assertTrue(tree.contains(10));
        assertTrue(tree.contains(5));
        assertTrue(tree.contains(133));
        assertTrue(tree.contains(87));
        assertFalse(tree.contains(777));

        tree.add(-15);
        tree.add(-44);
        tree.add(-76);
        tree.add(-33);

        assertFalse(tree.add(13));
        assertFalse(tree.add(87));
        assertFalse(tree.add(-15));
        assertFalse(tree.add(-33));

        assertFalse(tree.isEmpty());
        assertEquals(9, tree.size());
        assertTrue(tree.contains(10));
        assertTrue(tree.contains(5));
        assertTrue(tree.contains(133));
        assertTrue(tree.contains(87));
        assertFalse(tree.contains(777));
        assertFalse(tree.contains(0));
        assertFalse(tree.contains(8));

        assertTrue(tree.contains(-15));
        assertTrue(tree.contains(-44));
        assertTrue(tree.contains(-76));
        assertTrue(tree.contains(-33));
        assertFalse(tree.contains(-678));
    }

    @Test
    public void addAndContainsRandomValues() {
        Set<Integer> expectedSet = new HashSet<>();
        DigitalSearchTree tree = new DigitalSearchTree();

        for (int i = 0; i < 10_000; i++) {
            int value = -1000 + RAND.nextInt(2000);

            boolean expected = expectedSet.add(value);
            boolean actual = tree.add(value);

            assertEquals("value = " + value + " was inserted incorrectly, expected = " +
                    expected + ", actual = " + actual, expected, actual);
        }

        assertEquals(expectedSet.isEmpty(), tree.isEmpty());
        assertEquals(expectedSet.size(), tree.size());

        for (int expectedValue : expectedSet) {
            assertTrue(tree.contains(expectedValue));
        }

        for (int i = 0; i < 10_000; i++) {
            int value = RAND.nextInt();
            assertEquals(expectedSet.contains(value), tree.contains(value));
        }
    }

    @Test
    public void remove() {

        DigitalSearchTree tree = new DigitalSearchTree();

        assertTrue(tree.isEmpty());
        assertEquals(0, tree.size());

        tree.add(10);
        tree.add(5);
        tree.add(13);
        tree.add(133);
        tree.add(87);
        tree.add(-15);
        tree.add(-44);
        tree.add(-76);
        tree.add(-33);

        assertFalse(tree.isEmpty());
        assertEquals(9, tree.size());

        assertTrue(tree.contains(10));
        assertTrue(tree.contains(5));
        assertTrue(tree.contains(133));
        assertTrue(tree.contains(87));
        assertTrue(tree.contains(-15));
        assertTrue(tree.contains(-44));
        assertTrue(tree.contains(-76));
        assertTrue(tree.contains(-33));

        assertTrue(tree.remove(-76));
        assertTrue(tree.remove(87));

        assertFalse(tree.isEmpty());
        assertEquals(7, tree.size());
        assertTrue(tree.contains(10));
        assertTrue(tree.contains(5));
        assertTrue(tree.contains(133));
        assertFalse(tree.contains(87));
        assertTrue(tree.contains(-15));
        assertTrue(tree.contains(-44));
        assertFalse(tree.contains(-76));
        assertTrue(tree.contains(-33));

        assertFalse(tree.remove(-76));
        assertFalse(tree.remove(87));
        assertTrue(tree.remove(13));
        assertTrue(tree.remove(10));
        assertTrue(tree.remove(5));
        assertTrue(tree.remove(133));
        assertTrue(tree.remove(-15));
        assertTrue(tree.remove(-44));
        assertTrue(tree.remove(-33));

        assertTrue(tree.isEmpty());
        assertEquals(0, tree.size());
        assertFalse(tree.contains(10));
        assertFalse(tree.contains(5));
        assertFalse(tree.contains(133));
        assertFalse(tree.contains(87));
        assertFalse(tree.contains(-15));
        assertFalse(tree.contains(-44));
        assertFalse(tree.contains(-76));
        assertFalse(tree.contains(-33));
    }

    @Test
    public void removeRandomValues() {

        Set<Integer> expectedSet = new HashSet<>();
        DigitalSearchTree tree = new DigitalSearchTree();

        for (int i = 0; i < 100_000; i++) {
            int value = RAND.nextInt();

            expectedSet.add(value);
            tree.add(value);
        }

        assertEquals(expectedSet.size(), tree.size());

        Set<Integer> removedValuesSet = new HashSet<>();
        Iterator<Integer> expectedSetIt = expectedSet.iterator();

        while (expectedSetIt.hasNext()) {

            int value = expectedSetIt.next();

            // remove every 3-rd value from 'expectedSet'
            if (RAND.nextInt(3) == 0) {
                expectedSetIt.remove();
                assertTrue(tree.remove(value));
                removedValuesSet.add(value);
            }
        }

        assertEquals(expectedSet.size(), tree.size());

        for (int value : expectedSet) {
            assertTrue(tree.contains(value));
        }

        for (int removedValue : removedValuesSet) {
            assertFalse(tree.contains(removedValue));
        }
    }

}
