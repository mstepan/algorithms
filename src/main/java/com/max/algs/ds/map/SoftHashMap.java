package com.max.algs.ds.map;

import org.apache.log4j.Logger;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class SoftHashMap<K, V> {

    private static final Logger LOG = Logger.getLogger(SoftHashMap.class);

    private static final int DEFAULT_CAPACITY = 8;

    private static final Random RAND = ThreadLocalRandom.current();
    // universal hash function parameters
    // H(K) = ((a*K + b) % BIG_PRIME) % capacity
    private static final int BIG_PRIME = 492876847;
    private final ReferenceQueue<K> refQueue = new ReferenceQueue<>();
    private final int a = 1 + RAND.nextInt(BIG_PRIME - 1); // 'a' in range [1;
    // p-1]
    private final int b = RAND.nextInt(BIG_PRIME); // 'b' in range [0; p-1]
    @SuppressWarnings("unchecked")
    private Entry<K, V>[] data = new Entry[DEFAULT_CAPACITY];
    private AtomicInteger size = new AtomicInteger(0);

    public SoftHashMap() {
        super();
        Thread cleanerTask = new ReferenceQueueCleaner();
        cleanerTask.setDaemon(true);
        cleanerTask.start();
    }

    public int size() {
        return size.get();
    }

    private void deleteReference(Reference<K> ref) {

        for (int i = 0; i < data.length; i++) {

            Entry<K, V> entry = data[i];

            if (entry != null) {

                Entry<K, V> prev = null;
                Entry<K, V> cur = entry;

                while (cur != null) {

                    // find and delete WeakReference
                    if (cur.keyRef == ref) {

                        LOG.info("Will delete " + cur.keyRef.get());

                        if (prev == null) {
                            data[i] = cur.next;
                        }

                        else {
                            prev.next = cur.next;
                        }

                        cur.next = null;
                        size.decrementAndGet();

                        break;
                    }

                    prev = cur;
                    cur = cur.next;

                }

            }

        }

        LOG.info("Cleaning key: " + ref);
    }

    public V put(K key, V value) {
        notNull(key);

        int slot = slotIndex(key);

        Entry<K, V> entry = data[slot];

        if (entry == null) {
            data[slot] = new Entry<>(key, value, true, refQueue);
        }
        else {

            Entry<K, V> cur = new Entry<>(key, value, true, refQueue);
            Entry<K, V> prev = data[slot];

            data[slot] = cur;
            cur.next = prev;
        }

        checkLoadFactor();

        size.incrementAndGet();

        return null;
    }

    public String getStatistics() {

        StringBuilder statData = new StringBuilder();

        statData.append("size = ").append(size).append(", capacity = ")
                .append(data.length);

        int bucketIndex = 0;
        for (Entry<K, V> entry : data) {
            statData.append(System.getProperty("line.separator"))
                    .append("bucket ").append(bucketIndex).append(": ")
                    .append(entry == null ? "0" : entry.getSize());
            ++bucketIndex;
        }

        return statData.toString();

    }

    private void notNull(K key) {
        if (key == null) {
            throw new IllegalArgumentException("NULL 'key' passed");
        }
    }

    public V get(K key) {

        notNull(key);

        int slot = slotIndex(key);

        Entry<K, V> entry = data[slot];

        if (entry == null) {
            return null;
        }

        while (entry != null) {

            if (key.equals(entry.keyRef.get())) {
                return entry.value;
            }

            entry = entry.next;
        }

        return null;
    }

    private int slotIndex(K key) {
        return ((a * key.hashCode() + b) % BIG_PRIME) & (data.length - 1);
    }

    private void checkLoadFactor() {

        int loadFactor = ((size.get() * 100) / data.length) + 1;

        if (loadFactor > 75) {
            LOG.info("Resize should occur...");
        }

    }

    private static final class Entry<K1, V1> {

        final Reference<K1> keyRef;
        final V1 value;

        Entry<K1, V1> next;

        Entry(K1 key, V1 value, boolean weak, ReferenceQueue<K1> queue) {
            super();

            if (weak) {
                this.keyRef = new WeakReference<>(key, queue);
            }
            else {
                this.keyRef = new SoftReference<>(key, queue);
            }
            this.value = value;
        }

        @Override
        public String toString() {
            return keyRef.get() + " => " + value;
        }

        int getSize() {

            Entry<K1, V1> cur = this;

            int bucketSize = 0;

            while (cur != null) {
                cur = cur.next;
                ++bucketSize;
            }

            return bucketSize;

        }

    }

    private final class ReferenceQueueCleaner extends Thread {

        @SuppressWarnings("unchecked")
        @Override
        public void run() {

            Thread.currentThread()
                    .setName("SoftHashMap cleaner: " + hashCode());

            while (!Thread.currentThread().isInterrupted()) {

                try {
                    Reference<K> ref = (Reference<K>) refQueue.remove();
                    deleteReference(ref);
                }
                catch (InterruptedException interEx) {
                    Thread.currentThread().interrupt();
                }
            }

        }

    }

}
