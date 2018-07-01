package com.max.algs;


import org.junit.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class TreapTest {

    @Test
    public void addRandomData() {

        final Random rand = ThreadLocalRandom.current();

        for (int it = 0; it < 10; ++it) {

            final Set<Integer> expected = new HashSet<>();
            final Treap<Integer> actual = new Treap<>();

            for (int i = 0; i < 1000; ++i) {
                int randValue = rand.nextInt();

                assertEquals(expected.add(randValue), actual.add(randValue));
                assertEquals(expected.size(), actual.size());
                assertEquals(expected.isEmpty(), actual.isEmpty());

                for (int value : expected) {
                    assertTrue(actual.contains(value));
                }

                for (int j = 0; j < 100; ++j) {
                    int otherRandValue = rand.nextInt();
                    assertEquals(expected.contains(otherRandValue), actual.contains(otherRandValue));
                }
            }
        }
    }


}
