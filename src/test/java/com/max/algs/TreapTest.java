package com.max.algs;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class TreapTest {

    @Test
    @DisplayName("Add random values to a treap")
    void addRandomData() {

        Random rand = ThreadLocalRandom.current();

        for (int it = 0; it < 10; ++it) {

            Set<Integer> expected = new HashSet<>();
            Treap<Integer> actual = new Treap<>();

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
