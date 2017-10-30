package com.max.algs.hashing.perfect;

import com.max.algs.hashing.universal.UniversalHashFunction;
import com.max.algs.hashing.universal.UniversalHashes;

import java.util.ArrayList;
import java.util.List;

class HashBucket<U> {


    private final U[] table;
    private UniversalHashFunction<U> function;

    @SuppressWarnings("unchecked")
    HashBucket(int capacity, UniversalHashFunction<U> function) {
        super();
        table = (U[]) new Object[capacity];
        this.function = function;
    }


    @SuppressWarnings("unchecked")
    HashBucket(int capacity) {
        super();
        if (capacity <= 1) {
            function = UniversalHashes.generate();
        }
        else {
            function = UniversalHashes.generate();
        }
        table = (U[]) new Object[capacity];
    }

    boolean contains(U value) {
        U valueFromTable = table[bucketIndex(value)];
        return valueFromTable != null && valueFromTable.equals(value);
    }

    boolean add(U newValue) {

        int bucket = bucketIndex(newValue);

        // collision detected, choose another hash function
        if (table[bucket] != null) {

            // duplicate element detected
            if (table[bucket].equals(newValue)) {
                return false;
            }

            function = UniversalHashes.generate();

            List<U> prevValues = new ArrayList<>(table.length);

            for (int i = 0; i < table.length; i++) {
                if (table[i] != null) {
                    prevValues.add(table[i]);
                    table[i] = null;
                }
            }

            for (U prevValue : prevValues) {
                add(prevValue);
            }

            add(newValue);

        }
        else {
            table[bucketIndex(newValue)] = newValue;
        }

        return true;
    }


    int bucketIndex(U value) {
        return (function.hash(value) & 0x7FFFFFFF) % table.length;
    }

    @Override
    public String toString() {
        return "new HashBucket<Integer>(" + table.length + ", new UniversalHashFunction<Integer>(" + function.getA() + ", " + function.getB() + ") )";
    }

}
