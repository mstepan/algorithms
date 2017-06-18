package com.max.algs.hashing.open;

import java.lang.reflect.Array;

/**
 * Hash map with open addressing.
 * Use linear probing for collision resolution.
 */
public final class OpenHashMap<K, V> {

    private static final int DEFAULT_CAPACITY = 8;
    private static final double DEFAULT_LOAD_FACTOR = 0.5;
    private final ProbingStrategy probingStrategy = new AdvancedQuadraticProbing();
    private final double loadFactor;
    private Entry<K, V>[] arr;
    private int size;

    public OpenHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public OpenHashMap(double loadFactor) {
        this(DEFAULT_CAPACITY, loadFactor);
    }

    public OpenHashMap(int capacity, double loadFactor) {
        this.arr = allocateArray(capacity);
        this.loadFactor = loadFactor;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public V put(K key, V value) {

        assertNotNull(key, "Can't store NULL key");
        assertNotNull(value, "Can't store NULL value");

        double currentLoad = (double) size / arr.length;

        if (Double.compare(currentLoad, loadFactor) >= 0) {
            resize();
        }

        int i = 0;
        final int hash = Math.abs(key.hashCode());
        int index = probingStrategy.slot(hash, i, arr.length);

        while (arr[index] != null) {

            if (key.equals(arr[index].key)) {
                V prevValue = arr[index].value;
                arr[index].value = value;
                return prevValue;
            }

            ++i;
            index = probingStrategy.slot(hash, i, arr.length);
        }

        arr[index] = new Entry<>(key, value);

        ++size;
        return null;
    }

    public V get(K key) {

        assertNotNull(key, "Can't obtain value for NULL key");

        int i = 0;
        final int hash = Math.abs(key.hashCode());
        int index = probingStrategy.slot(hash, i, arr.length);

        while (arr[index] != null) {

            if (key.equals(arr[index].key)) {
                return arr[index].value;
            }

            ++i;
            index = probingStrategy.slot(hash, i, arr.length);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public V remove(K key) {

        assertNotNull(key, "Can't remove NULL key value");

        int i = 0;

        final int hash = Math.abs(key.hashCode());

        int index = probingStrategy.slot(hash, i, arr.length);

        while (arr[index] != null) {

            if (arr[index].key.equals(key)) {
                V prevValue = arr[index].value;
                arr[index] = Entry.DELETED;
                --size;
                return prevValue;
            }

            ++i;
            index = probingStrategy.slot(hash, i, arr.length);
        }

        return null;
    }

    public int size() {
        return size;
    }


    public boolean isEmpty() {
        return size == 0;
    }

    private void resize() {

        Entry<K, V>[] prevArr = arr;
        arr = allocateArray(arr.length << 1);
        size = 0;

        for (Entry<K, V> prevEntry : prevArr) {
            if (prevEntry != null && prevEntry != Entry.DELETED) {
                put(prevEntry.key, prevEntry.value);
            }
        }

    }


    private <T> void assertNotNull(T value, String errorMsg) {
        if (value == null) {
            throw new IllegalArgumentException(errorMsg);
        }
    }

    @SuppressWarnings("unchecked")
    private Entry<K, V>[] allocateArray(int length) {
        return (Entry<K, V>[]) Array.newInstance(Entry.class, length);
    }

    private interface ProbingStrategy {
        int slot(int hash, int i, int m);
    }

    private static class Entry<K, V> {

        @SuppressWarnings("rawtypes")
        private static final Entry DELETED = new Entry();

        K key;
        V value;

        Entry() {
            super();
        }

        Entry(K key, V value) {
            super();
            this.key = key;
            this.value = value;
        }
    }

    private static class SimpleLinearProbing implements ProbingStrategy {

        @Override
        public int slot(int hash, int i, int m) {
            return (hash + i) % m;
        }
    }

    private static class SimpleQuadraticProbing implements ProbingStrategy {
        @Override
        public int slot(int hash, int i, int m) {
            return (hash + i * i) % m;
        }
    }


    /**
     * This probing will examine each slot form table is table 'm' is power of 2.
     */
    private static class AdvancedQuadraticProbing implements ProbingStrategy {
        @Override
        public int slot(int hash, int i, int m) {
            return (hash + ((i * (i + 1)) >> 1)) % m;
        }
    }

    /**
     * Quadratic probing with positive and negative offsets interchanged.
     */
    private static class QuadraticProbingWithNegativeOffset implements ProbingStrategy {
        @Override
        public int slot(int hash, int i, int m) {

            // odd case
            if ((i & 1) == 0) {
                return (hash + i * i) % m;
            }

            //even case
            int index = hash - i * i;

            if (index < 0) {
                return m - Math.abs(index) % m;
            }

            return index % m;
        }
    }


}
