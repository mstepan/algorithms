package com.max.algs.hashing.robin_hood;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Robin Hood hash table.
 * <p>
 * See: http://codecapsule.com/2013/11/11/robin-hood-hashing/
 */
public class RobinHoodHashMap<K, V> {

    // big prime number
    private static final int SALT = 7_368_787;

    private static final int INITIAL_CAPACITY = 16;

    // Resize after hash table 50% filled in.
    private static final double RESIZE_LOAD_FACTOR = 0.5;

    private int size;

    private Entry[] data;

    public RobinHoodHashMap() {
        this.data = new Entry[INITIAL_CAPACITY];
    }

    public V get(K key) {
        checkNotNull(key);

        Entry<K, V> entry = findNode(key);
        return entry == null ? null : entry.value;
    }

    private Entry<K, V> findNode(K key) {

        int bucket = bucket(key);
        int keyHashCode = key.hashCode();

        for (int i = bucket; ; i = (i + 1) & (data.length - 1)) {

            // empty bucket
            if (data[i] == null) {
                return null;
            }

            @SuppressWarnings("unchecked")
            Entry<K, V> entry = data[i];

            if (entry.key.hashCode() == keyHashCode && entry.key.equals(key)) {
                return entry;
            }

            // cur node distance < distance for a search key
            if (entry.distance(i) < (i - bucket)) {
                return null;
            }
        }
    }


    public void put(K key, V value) {

        Entry<K, V> entry = findNode(key);

        // update existing entry
        if (entry != null) {
            entry.value = value;
            return;
        }

        Entry<K, V> entryToInsert = new Entry<>(key, value, bucket(key));

        for (int i = entryToInsert.initialBucket; ; i = (i + 1) & (data.length - 1)) {

            // found empty slot, insert entry
            if (data[i] == null) {
                data[i] = entryToInsert;
                break;
            }

            // found entry with smaller distance, swap entries and proceed
            else if (data[i].distance(i) < entryToInsert.distance(i)) {
                @SuppressWarnings("unchecked")
                Entry<K, V> temp = data[i];
                data[i] = entryToInsert;
                entryToInsert = temp;
            }
        }

        ++size;
        if (Double.compare(loadFactor(), RESIZE_LOAD_FACTOR) >= 0) {
            resize();
        }
    }

    public void delete(K key) {
        //TODO: implement
    }

    public boolean contains(K key) {
        return get(key) != null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @SuppressWarnings("unchecked")
    private void resize() {

        Entry[] tempData = data;

        data = new Entry[data.length << 1];
        size = 0;

        for (Entry<K, V> prevEntry : tempData) {
            if (prevEntry != null) {
                put(prevEntry.key, prevEntry.value);
            }
        }
    }

    private int bucket(K key) {
        return (key.hashCode() * SALT) & (data.length - 1);
    }

    private double loadFactor() {
        return ((double) size) / data.length;
    }

    private static final class Entry<K, V> {

        final K key;
        V value;
        final int initialBucket;

        Entry(K key, V value, int initialBucket) {
            this.key = key;
            this.value = value;
            this.initialBucket = initialBucket;
        }

        /**
         * DIB - distance to initial bucket
         */
        int distance(int curBucketIndex) {
            return curBucketIndex - initialBucket;
        }

        @Override
        public String toString() {
            return String.valueOf(key) + "=" + String.valueOf(value) + "[" + initialBucket + "]";
        }
    }

}
