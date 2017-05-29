package com.max.algs.epi.hashing;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * LRU cache for ISBN -> book price.
 */
public class IsbnCache implements Iterable<IsbnCache.IsbnPricePair> {

    private final int capacity;

    private int size;

    private int baseModCount;

    private final IsbnEntry head = new IsbnEntry("HEAD", 0.0);
    private final IsbnEntry tail = new IsbnEntry("TAIL", 0.0);

    private IsbnEntryBucket[] buckets;

    public IsbnCache(int capacity) {
        checkArgument(capacity > 0 && capacity < 1_000_000,
                "Incorrect capacity value, should be in range [%s, %s], found: %s",
                1, 1_000_000, capacity);

        combineHeadTail();

        this.capacity = capacity;
        int bucketsCount = (int) Math.ceil((100.0 * capacity) / 75.0);
        this.buckets = (IsbnEntryBucket[]) Array.newInstance(IsbnEntryBucket.class, bucketsCount);
    }

    private void combineHeadTail() {
        head.next = tail;
        tail.prev = head;
    }

    public Double put(String isbn, Double price) {

        ++baseModCount;

        // not exists in hash table
        if (findEntry(isbn) == null) {

            IsbnEntry entry = new IsbnEntry(isbn, price);

            int bucketIndex = calculateBucket(isbn);

            if (buckets[bucketIndex] == null) {
                buckets[bucketIndex] = new IsbnEntryBucket();
            }

            IsbnEntryBucket bucketList = buckets[bucketIndex];

            entry.next = bucketList.entry;
            bucketList.entry = entry;

            insertAfterHead(entry);
            ++size;

            ensureCapacity();

            return null;
        }

        // entry already exists
        IsbnEntry entry = findEntry(isbn);

        Double prevPrice = entry.price;
        entry.price = price;

        deleteEntryFromList(entry);
        insertAfterHead(entry);

        return prevPrice;
    }

    public Double get(String isbn) {

        IsbnEntry entry = findEntry(isbn);

        if (entry == null) {
            return null;
        }

        ++baseModCount;

        deleteEntryFromList(entry);
        insertAfterHead(entry);

        return entry.price;
    }

    private void ensureCapacity() {
        if (size > capacity) {
            // evict last element from double-linked-list

            System.out.println("ensureCapacity");
        }
    }

    private void insertAfterHead(IsbnEntry entry) {
        entry.next = head.next;
        entry.prev = head;

        head.next.prev = entry;
        head.next = entry;
    }

    private void deleteEntryFromList(IsbnEntry entry) {
        IsbnEntry entryPrev = entry.prev;
        IsbnEntry entryNext = entry.next;

        entryPrev.next = entryNext;
        entryNext.prev = entryPrev;

        entry.next = null;
        entry.prev = null;
    }

    private IsbnEntry findEntry(String isbn) {

        int bucketIndex = calculateBucket(isbn);

        IsbnEntryBucket bucket = buckets[bucketIndex];

        if (bucket == null) {
            return null;
        }

        IsbnEntry cur = bucket.entry;

        while (cur != null) {
            if (cur.isbn.equals(isbn)) {
                return cur;
            }

            cur = cur.next;
        }

        return null;
    }

    private int calculateBucket(String isbn) {
        return (isbn.hashCode() & 0x7F_FF_FF_FF) % buckets.length;
    }

    @NotNull
    @Override
    public Iterator<IsbnPricePair> iterator() {
        return new LinearOrderIterator();
    }

    // === ISBN to book price iterator ===
    public final class LinearOrderIterator implements Iterator<IsbnPricePair> {

        private IsbnEntry cur;
        private final int modCount;

        LinearOrderIterator() {
            this.cur = IsbnCache.this.head.next;
            this.modCount = IsbnCache.this.baseModCount;
        }

        @Override
        public boolean hasNext() {
            return cur != IsbnCache.this.tail;
        }

        @Override
        public IsbnPricePair next() {
            if (this.modCount != IsbnCache.this.baseModCount) {
                throw new ConcurrentModificationException("Collection was modified during traversation");
            }

            IsbnPricePair res = new IsbnPricePair(cur.isbn, cur.price);
            cur = cur.next;
            return res;
        }
    }

    // === Iterator entry ===
    public static final class IsbnPricePair {
        public final String isbn;
        public final Double price;

        public IsbnPricePair(String isbn, Double price) {
            this.isbn = isbn;
            this.price = price;
        }

        @Override
        public String toString() {
            return isbn + ": " + price;
        }
    }

    // === ENTRY nested class ===
    private static final class IsbnEntry {
        final String isbn;
        Double price;

        IsbnEntry next;
        IsbnEntry prev;

        IsbnEntry(String isbn, Double price) {
            this.isbn = isbn;
            this.price = price;
        }

        @Override
        public String toString() {
            return isbn + ": " + price;
        }

        @Override
        public int hashCode() {
            return Objects.hash(isbn);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            if (other == null || other.getClass() != this.getClass()) {
                return false;
            }

            IsbnEntry otherEntry = (IsbnEntry) other;

            return Objects.equals(this.isbn, otherEntry.isbn);
        }
    }

    // === BUCKETS single linked list ===

    private static final class IsbnEntryBucket {
        IsbnEntry entry;
        IsbnEntry next;
    }

}
