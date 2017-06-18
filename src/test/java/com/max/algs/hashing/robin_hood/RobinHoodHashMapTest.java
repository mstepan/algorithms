package com.max.algs.hashing.robin_hood;

import org.junit.Test;

import java.util.*;

import static junit.framework.TestCase.assertSame;
import static org.junit.Assert.*;


public class RobinHoodHashMapTest {

    private static void changeMap(Integer maxValue, double deletionRate) {
        Map<Integer, Integer> actualMap = new RobinHoodHashMap<>();
        Map<Integer, Integer> expectedMap = new HashMap<>();

        List<Integer> keyElements = new ArrayList<>();

        Random rand = new Random();

        for (int i = 0; i < 10_000; ++i) {
            int key;
            int value;

            if (maxValue == null) {
                key = rand.nextInt();
                value = rand.nextInt();
            }
            else {
                key = rand.nextInt(maxValue);
                value = rand.nextInt(maxValue);
            }

            // delete element
            if (!keyElements.isEmpty() && Double.compare(rand.nextDouble(), deletionRate) <= 0) {

                int indexToDelete = rand.nextInt(keyElements.size());

                Integer elemToRemove = keyElements.get(indexToDelete);

                actualMap.remove(elemToRemove);
                expectedMap.remove(elemToRemove);

                keyElements.remove(indexToDelete);

            }
            // add element
            else {
                expectedMap.put(key, value);
                actualMap.put(key, value);
                keyElements.add(key);
            }

            assertMapEquals(expectedMap, actualMap);
        }
    }

    private static <K, V> void assertMapEquals(Map<K, V> expectedMap, Map<K, V> actualMap) {
        assertEquals(expectedMap.size(), actualMap.size());
        assertEquals(expectedMap.isEmpty(), actualMap.isEmpty());

        for (K key : expectedMap.keySet()) {
            assertEquals(expectedMap.get(key), actualMap.get(key));
        }
    }

    @Test
    public void putAndGetRandomValuesWithLotsOfDuplicates() {
        changeMap(100, 0.0);
    }

    @Test
    public void putAndGetRandomValues() {
        changeMap(null, 0.0);
    }

    @Test
    public void putGetAndRemoveRandomValues() {
        changeMap(null, 0.1);
        changeMap(null, 0.2);
        changeMap(null, 0.3);
    }

    @Test
    public void putAndGet() {
        Map<Integer, String> map = new RobinHoodHashMap<>();

        assertEquals(map.size(), 0);
        assertTrue(map.isEmpty());
        assertSame(null, map.get(133));
        assertSame(null, map.get(155));

        map.put(133, "133");
        assertEquals(map.size(), 1);
        assertFalse(map.isEmpty());
        assertEquals("133", map.get(133));
        assertSame(null, map.get(155));

        map.put(133, "133-2");
        map.put(155, "155");
        map.put(177, "177");
        assertEquals(map.size(), 3);
        assertFalse(map.isEmpty());
        assertEquals("133-2", map.get(133));
        assertEquals("155", map.get(155));
        assertEquals("177", map.get(177));
    }

    @Test
    public void checkRemove() {
        Map<Integer, Integer> map = new RobinHoodHashMap<>();

        for (int i = 0; i < 100; ++i) {
            map.put(i, i);
        }

        assertEquals(100, map.size());
        assertFalse(map.isEmpty());

        for (int i = 0; i < 100; ++i) {
            if ((i & 1) == 0) {
                assertEquals(Integer.valueOf(i), (Integer) map.remove(i));
            }
        }

        assertEquals(50, map.size());
        assertFalse(map.isEmpty());

        for (int i = 0; i < 100; ++i) {
            if ((i & 1) != 0) {
                assertEquals(Integer.valueOf(i), map.get(i));
            }
        }
    }

}
