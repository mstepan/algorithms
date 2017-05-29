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

    private BucketEntry[] buckets;

    public IsbnCache(int capacity) {
        checkArgument(capacity > 0 && capacity < 1_000_000,
                "Incorrect capacity value, should be in range [%s, %s], found: %s",
                1, 1_000_000, capacity);

        combineHeadTail();

        this.capacity = capacity;
        int bucketsCount = (int) Math.ceil((100.0 * capacity) / 75.0);
        this.buckets = (BucketEntry[]) Array.newInstance(BucketEntry.class, bucketsCount);

        for (int i = 0; i < buckets.length; ++i) {
            buckets[i] = BucketEntry.createSentinel();
        }
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

            // insert to head
            buckets[bucketIndex].next = new BucketEntry(entry, buckets[bucketIndex].next);

            insertAfterHead(entry);
            ++size;

            ensureCapacity();

            return null;
        }

        // entry already exists
        BucketEntry bucketEntry = findEntry(isbn);

        Double prevPrice = bucketEntry.entry.price;
        bucketEntry.entry.price = price;

        deleteEntryFromList(bucketEntry.entry);
        insertAfterHead(bucketEntry.entry);

        return prevPrice;
    }

    public Double get(String isbn) {

        BucketEntry bucketEntry = findEntry(isbn);

        if (bucketEntry == null) {
            return null;
        }

        ++baseModCount;

        deleteEntryFromList(bucketEntry.entry);
        insertAfterHead(bucketEntry.entry);

        return bucketEntry.entry.price;
    }

    private void ensureCapacity() {
        if (size > capacity) {
            // evict last element from double-linked-list
            IsbnEntry deleteEntry = deleteLast();
//            System.out.println("LRU evicted: " + deleteEntry.isbn);
        }
    }

    private IsbnEntry deleteLast() {
        // delete last element from double linked list
        IsbnEntry entryToDelete = tail.prev;

        tail.prev = entryToDelete.prev;
        entryToDelete.prev.next = tail;

        entryToDelete.next = null;
        entryToDelete.prev = null;

        // delete element from bucket single linked list
        BucketEntry cur = buckets[calculateBucket(entryToDelete.isbn)];

        while (cur.next.entry != entryToDelete) {
            cur = cur.next;
        }

        BucketEntry bucketEntryToDelete = cur.next;

        cur.next = bucketEntryToDelete.next;
        bucketEntryToDelete.next = null;

        return entryToDelete;
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

    private BucketEntry findEntry(String isbn) {

        int bucketIndex = calculateBucket(isbn);

        BucketEntry cur = buckets[bucketIndex].next;

        while (cur != null) {
            if (cur.entry.isbn.equals(isbn)) {
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

    private static final class BucketEntry {

        static BucketEntry createSentinel() {
            return new BucketEntry(new IsbnEntry("sentinal", 0.0), null);
        }

        BucketEntry(IsbnEntry entry, BucketEntry next) {
            this.entry = entry;
            this.next = next;
        }

        IsbnEntry entry;
        BucketEntry next;
    }

}
