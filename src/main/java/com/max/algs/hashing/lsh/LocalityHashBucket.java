package com.max.algs.hashing.lsh;


import com.max.algs.util.ArrayUtils;

import java.util.*;

final class LocalityHashBucket {

    private static final int BITS_TO_USE = Integer.SIZE;

    private final int[] bitsIndexes;

    private final Map<Integer, List<int[]>> buckets = new HashMap<>();

    LocalityHashBucket(int hashLength) {

        int[] allIndexes = new int[hashLength];

        for (int i = 0; i < allIndexes.length; i++) {
            allIndexes[i] = i;
        }

        ArrayUtils.shuffle(allIndexes);

        bitsIndexes = Arrays.copyOf(allIndexes, BITS_TO_USE);
        Arrays.sort(bitsIndexes);
    }

    void add(String hash, int[] data) {
        int bucketIndex = calculateBucket(hash);

        List<int[]> singleBucket = buckets.get(bucketIndex);

        if (singleBucket == null) {
            singleBucket = new ArrayList<>();
            buckets.put(bucketIndex, singleBucket);
        }

        singleBucket.add(data);

    }

    List<int[]> get(String hash) {

        int bucketIndex = calculateBucket(hash);

        return buckets.get(bucketIndex);
    }

    private int calculateBucket(String hashStr) {

        int hash = 0;

        for (int bitToCheck : bitsIndexes) {

            int bitValue = hashStr.charAt(bitToCheck) - '0';

            hash |= bitValue;
            hash <<= 1;
        }

        return hash;
    }


}
