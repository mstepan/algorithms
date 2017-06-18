package com.max.algs.ds.bitset;

import java.util.HashMap;
import java.util.Map;


public final class DynamicBitSet {


    private final Map<Integer, Long> buckets = new HashMap<>();


    public boolean revertAndGet(int value) {

        int bucketIndex = calculateBucket(value);

        Long bucketValue = buckets.get(bucketIndex);
        long index = calculateIndex(value);

        if (bucketValue == null) {
            buckets.put(bucketIndex, index);
            return true;
        }
        else {

            if ((bucketValue & index) > 0) {
                // clear bit
                buckets.put(bucketIndex, bucketValue & (~index));
                return false;
            }
            else {
                // set bit
                buckets.put(bucketIndex, bucketValue | index);
                return true;
            }

        }

    }


    private int calculateBucket(int value) {
        return value / 64;
    }

    private long calculateIndex(int value) {
        return (1L << (value % 64));
    }


    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder("{");

        for (Map.Entry<Integer, Long> entry : buckets.entrySet()) {
            buf.append(entry.getKey()).append("=").append(Long.toBinaryString(entry.getValue())).append(", ");
        }

        buf.append("}");

        return buf.toString();
    }

}
