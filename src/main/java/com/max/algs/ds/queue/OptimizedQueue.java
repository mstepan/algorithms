package com.max.algs.ds.queue;


/**
 * Store up to 14 integer values in range [0;9]
 * Use 2 integers to represent queue state.
 */
public final class OptimizedQueue {


    private static final int RANGE_MIN = 0;
    private static final int RANGE_MAX = 9;
    private static final int BITS_PER_ELEMENT = 4;
    private static final int BITS_MASK = 0x0F;
    private static final int MAX_CAPACITY = 14;
    private static final int BUCKET_SIZE = 7;
    private int[] data = {0, 0};
    private int size;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void add(int value) {

        if (value < RANGE_MIN || value > RANGE_MAX) {
            throw new IllegalArgumentException("Can't store value " + value + ", should be in range [" + RANGE_MIN + ";" + RANGE_MAX + "]");
        }

        if (size == MAX_CAPACITY) {
            throw new IllegalStateException("Queue is full");
        }

        int bucketIndex = size / BUCKET_SIZE;

        int bucketData = data[bucketIndex];
        bucketData = bucketData | (value << (size * 4));
        data[bucketIndex] = bucketData;

        ++size;
    }

    public int poll() {
        if (size == 0) {
            throw new IllegalStateException("Queue is empty");
        }


        int bucketIndex = (size - 1) / BUCKET_SIZE;

        int res = data[bucketIndex] & BITS_MASK;

        data[bucketIndex] = data[bucketIndex] >> BITS_PER_ELEMENT;

        --size;
        return res;
    }

    private int getElement(int index) {

        int bucketIndex = index / BUCKET_SIZE;

        int offset = (index % BUCKET_SIZE) * BITS_PER_ELEMENT;

        return (data[bucketIndex] >> offset) & BITS_MASK;
    }


    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("[");

        for (int index = 0; index < size; index++) {

            if (index > 0) {
                buf.append(", ");
            }

            buf.append(getElement(index));
        }

        buf.append("]");

        return buf.toString();
    }

}
