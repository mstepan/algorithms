package com.max.algs.ds.skiplist;

import org.junit.Test;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.*;


public class SkipListSetTest {

    @Test
    public void addByOne() {

        for (int i = 0; i < 100; i++) {
            Set<Character> set = new SkipListSet<>();

            assertTrue(set.isEmpty());
            assertEquals(0, set.size());

            assertTrue(set.add('a'));
            assertFalse(set.isEmpty());
            assertEquals(1, set.size());
            assertTrue(set.contains('a'));
            assertFalse(set.contains('b'));
            assertFalse(set.contains('s'));
            assertFalse(set.contains('t'));

            assertTrue(set.add('b'));
            assertFalse(set.isEmpty());
            assertEquals(2, set.size());
            assertTrue(set.contains('b'));
            assertFalse(set.contains('s'));
            assertFalse(set.contains('t'));

            assertTrue(set.add('s'));
            assertFalse(set.isEmpty());
            assertEquals(3, set.size());
            assertTrue(set.contains('s'));
            assertFalse(set.contains('t'));

            assertTrue(set.add('t'));
            assertFalse(set.isEmpty());
            assertEquals(4, set.size());
            assertTrue(set.contains('t'));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void addNotComparable() {

        class Custom {
        }
        ;

        Set<Custom> set = new SkipListSet<>();

        set.add(new Custom());
        set.add(new Custom());
    }

    @Test
    public void addWithComparator() {

        class Custom {
        }
        ;

        Set<Custom> set = new SkipListSet<>(new Comparator<Custom>() {
            @Override
            public int compare(Custom o1, Custom o2) {
                return 1;
            }
        });

        set.add(new Custom());
        set.add(new Custom());
    }


    @Test
    public void addRnaomdInts() {
        Set<Integer> set = new SkipListSet<>();

        Random RAND = ThreadLocalRandom.current();
        Set<Integer> actualSet = new LinkedHashSet<>();

        for (int i = 0; i < 100_000; i++) {
            int randValue = RAND.nextInt();
            actualSet.add(randValue);
            set.add(randValue);
        }

        assertEquals(actualSet.size(), set.size());

        for (Integer value : actualSet) {
            assertTrue(set.contains(value));
        }

        for (int i = 0; i < 1000; i++) {
            int randValue = RAND.nextInt();

            while (actualSet.contains(randValue)) {
                randValue = RAND.nextInt();
            }

            assertFalse(set.contains(randValue));
        }

    }


}
