package com.max.algs.epi.hashing;

import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * LRU cache for ISBN -> book price using LinkedHashMap implementation.
 */
public class IsbnCacheDecorator implements Iterable<IsbnCacheDecorator.IsbnPricePair> {

    private final Map<String, Double> data;

    public IsbnCacheDecorator() {
        this(16);
    }

    public IsbnCacheDecorator(int capacity) {
        checkArgument(capacity > 0 && capacity < 1_000_000);
        this.data = new LinkedHashMap<String, Double>((100 * capacity / 75) + 1, 0.75F, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Double> eldest) {
                return size() > capacity;
            }
        };
    }

    public Double put(String isbn, Double price) {
        return data.put(isbn, price);
    }

    public Double get(String isbn) {
        return data.get(isbn);
    }

    @NotNull
    @Override
    public Iterator<IsbnPricePair> iterator() {
        return new LinearIterator(data.entrySet());
    }

    private static final class LinearIterator implements Iterator<IsbnPricePair> {

        private final Iterator<Map.Entry<String, Double>> entriesIt;

        public LinearIterator(Set<Map.Entry<String, Double>> entries) {
            this.entriesIt = entries.iterator();
        }

        @Override
        public boolean hasNext() {
            return entriesIt.hasNext();
        }

        @Override
        public IsbnPricePair next() {

            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Map.Entry<String, Double> curEntry = entriesIt.next();

            return new IsbnPricePair(curEntry.getKey(), curEntry.getValue());
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


}
