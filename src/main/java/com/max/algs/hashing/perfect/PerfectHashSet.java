package com.max.algs.hashing.perfect;

import com.max.algs.hashing.universal.UniversalHashFunction;

import java.util.Collection;
import java.util.List;


public class PerfectHashSet<T> {


    private final HashBucket<T>[] table;
    private final UniversalHashFunction<T> mainHashFunction;

    PerfectHashSet(List<T> values, UniversalHashFunction<T> mainHashFunction, HashBucket<T>[] table) {
        this.table = table;
        this.mainHashFunction = mainHashFunction;
        putValues(values);

    }


    @SuppressWarnings("unchecked")
    public PerfectHashSet(Collection<T> values) {
        table = new HashBucket[values.size()];
        mainHashFunction = UniversalHashFunction.generate();
        initBuckets(values);
        putValues(values);
    }

    private void initBuckets(Collection<T> values) {

        int[] freq = new int[table.length];

        for (T value : values) {
            int bucket = mainBucketIndex(value);
            freq[bucket] += 1;
        }

        for (int i = 0; i < table.length; i++) {
            if (freq[i] > 0) {
                table[i] = new HashBucket<T>(freq[i] * freq[i]);
            }
        }
    }

    private void putValues(Collection<T> values) {

        for (T value : values) {
            if (value == null) {
                throw new IllegalArgumentException("Can't store NULL value");
            }

            int bucket = mainBucketIndex(value);
            table[bucket].add(value);

        }
    }

    private int mainBucketIndex(T value) {
        return (mainHashFunction.hash(value) & 0x7FFFFFFF) % table.length;
    }

    /**
     * Time: O(1)
     *
     * @param value
     * @return
     */
    public boolean contains(T value) {
        int bucket = mainBucketIndex(value);

        if (table[bucket] == null) {
            return false;
        }

        return table[bucket].contains(value);
    }

    @Override
    public String toString() {

        final String lineSeparator = System.getProperty("line.separator");

        StringBuilder buf = new StringBuilder();

        buf.append("----------------------------------------------------").append(lineSeparator);
        buf.append("main =>  new UniversalHashFunction<Integer>(" + mainHashFunction.getA() + ", " + mainHashFunction.getB() + ")").append(lineSeparator);
        buf.append("----------------------------------------------------");

        for (int i = 0; i < table.length; i++) {
            buf.append(lineSeparator).append(table[i]).append(",");
        }

        return buf.toString();
    }
}
