package com.max.algs.hashing.robin_hood;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static junit.framework.TestCase.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class RobinHoodHashMapTest {

    @Test
    public void putAndGetRandomValuesWithLotsOfDuplicates() {
        putAndGetValuesInRange(100);
    }

    @Test
    public void putAndGetRandomValues() {
        putAndGetValuesInRange(null);
    }

    private static void putAndGetValuesInRange(Integer maxValue) {
        RobinHoodHashMap<Integer, Integer> actualMap = new RobinHoodHashMap<>();
        Map<Integer, Integer> expectedMap = new HashMap<>();

        Random rand = new Random();

        for (int i = 0; i < 1000; ++i) {
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

            expectedMap.put(key, value);
            actualMap.put(key, value);

            assertEquals(expectedMap.size(), actualMap.size());
            assertEquals(expectedMap.isEmpty(), actualMap.isEmpty());

            for (Integer expKey : expectedMap.keySet()) {
                assertEquals(expectedMap.get(expKey), actualMap.get(expKey));
            }
        }
    }


    @Test
    public void putAndGet() {
        RobinHoodHashMap<Integer, String> map = new RobinHoodHashMap<>();

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

}
