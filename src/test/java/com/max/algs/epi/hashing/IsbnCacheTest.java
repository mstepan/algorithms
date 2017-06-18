package com.max.algs.epi.hashing;

import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import static junit.framework.TestCase.*;


public class IsbnCacheTest {

    private static void assertCacheSame(String[] arr, IsbnCache cache) {

        Iterator<IsbnCache.IsbnPricePair> it = cache.iterator();

        for (int i = 0; i < arr.length; ++i) {
            assertTrue("Cache iterator is empty", it.hasNext());
            assertEquals(arr[i], it.next().isbn);
        }

        assertFalse("Cache it not fully exhausted", it.hasNext());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNegativeCapacity() {
        new IsbnCache(-10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createZeroCapacity() {
        new IsbnCache(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createBigCapacity() {
        new IsbnCache(1_000_001);
    }

    @Test(expected = ConcurrentModificationException.class)
    public void iteratingAndModifyingAtTheSameTime() {
        IsbnCache cache = new IsbnCache(5);
        cache.put("1", 1.0);
        cache.put("2", 2.0);
        cache.put("3", 3.0);

        Iterator<IsbnCache.IsbnPricePair> it = cache.iterator();

        assertTrue(it.hasNext());
        assertNotNull(it.next());

        cache.put("4", 4.0);

        assertTrue(it.hasNext());
        it.next();
    }

    @Test
    public void addAndGetNoEviction() {
        IsbnCache cache = new IsbnCache(5);

        assertSame(null, cache.put("1", 1.0));
        assertSame(null, cache.put("2", 2.0));
        assertSame(null, cache.put("3", 3.0));

        assertCacheSame(new String[]{"3", "2", "1"}, cache);

        assertEquals(2.0, cache.get("2"), 0.0);
        assertCacheSame(new String[]{"2", "3", "1"}, cache);

        assertSame(null, cache.put("4", 4.0));
        assertSame(null, cache.put("5", 5.0));

        assertCacheSame(new String[]{"5", "4", "2", "3", "1"}, cache);

        assertEquals(4.0, cache.put("4", 40.0), 0.0);
        assertCacheSame(new String[]{"4", "5", "2", "3", "1"}, cache);

    }

    @Test
    public void addAndGetWithEviction() {
        IsbnCache cache = new IsbnCache(5);

        for (int i = 0; i < 10; ++i) {
            cache.put(String.valueOf(i), (double) i + 100.0);
        }

        assertCacheSame(new String[]{"9", "8", "7", "6", "5"}, cache);

        cache.get("7");
        cache.get("8");

        assertCacheSame(new String[]{"8", "7", "9", "6", "5"}, cache);

        cache.put("13", 13.0);
        cache.put("14", 14.0);
        cache.put("15", 15.0);

        assertCacheSame(new String[]{"15", "14", "13", "8", "7"}, cache);
    }


}
