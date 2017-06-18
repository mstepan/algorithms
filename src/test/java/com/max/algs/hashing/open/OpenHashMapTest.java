package com.max.algs.hashing.open;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.*;

public class OpenHashMapTest {


    @Test
    public void remove() {

        OpenHashMap<Integer, String> map = new OpenHashMap<>();

        for (int i = 0; i < 100; i++) {
            map.put(i, String.valueOf(i));
        }

        for (int i = 50; i < 100; i++) {
            assertEquals(String.valueOf(i), map.remove(i));
        }

        assertEquals(50, map.size());
        assertFalse(map.isEmpty());

        for (int i = 0; i < 50; i++) {
            assertEquals(String.valueOf(i), map.get(i));
        }

        for (int i = 50; i < 100; i++) {
            assertSame(null, map.get(i));
        }


        for (int i = 100; i < 1000; i++) {
            map.put(i, String.valueOf(i));
        }

        assertEquals(950, map.size());
        assertFalse(map.isEmpty());
    }

    @Test
    public void replaceValue() {
        OpenHashMap<Integer, String> map = new OpenHashMap<>();

        for (int i = 0; i < 100; i++) {
            map.put(i, String.valueOf(i));
        }

        assertEquals("23", map.get(23));
        assertEquals("23", map.put(23, "55"));

        assertEquals("55", map.get(23));
        assertEquals(100, map.size());
    }


    @Test
    public void putLotsOfRandomValues() {
        OpenHashMap<Integer, String> map = new OpenHashMap<>();
        Map<Integer, String> expectedMap = new HashMap<>();

        Random rand = new Random();

        for (int i = 0; i < 1_000_000; ++i) {
            int value = rand.nextInt();
            String strValue = String.valueOf(value);

            boolean wasAdded = map.put(value, strValue) == null;
            boolean expectedWasAdded = expectedMap.put(value, strValue) == null;

            assertSame(wasAdded, expectedWasAdded);
        }

        assertEquals(map.size(), expectedMap.size());

        for (Map.Entry<Integer, String> entry : expectedMap.entrySet()) {
            assertEquals(map.get(entry.getKey()), entry.getValue());
        }
    }


    @Test
    public void put() {

        OpenHashMap<Integer, String> map = new OpenHashMap<>();

        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
        assertSame(null, map.get(13));
        assertSame(null, map.get(2));

        assertSame(null, map.put(0, "0"));
        assertSame(null, map.put(1, "1"));

        assertEquals(2, map.size());
        assertFalse(map.isEmpty());

        assertEquals("0", map.get(0));
        assertEquals("1", map.get(1));

        for (int i = 2; i < 20; i++) {
            assertSame(null, map.put(i, String.valueOf(i)));
        }

        assertEquals(20, map.size());
        assertFalse(map.isEmpty());

        for (int i = 0; i < 20; i++) {
            assertEquals(String.valueOf(i), map.get(i));
        }

        for (int i = 20; i < 100; i++) {
            assertSame(null, map.get(i));
        }
    }


}
