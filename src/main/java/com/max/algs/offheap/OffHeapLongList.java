package com.max.algs.offheap;

import com.max.system.UnsafeUtils;
import sun.misc.Unsafe;

/**
 * Off-heap implementation of resizable list (similar to java.util.ArrayList).
 * Not thread safe.
 */
public class OffHeapLongList {

    private static final int MIN_CAPACITY = 16;
    private static final long LONG_SIZE_IN_BYTES = 8L;

    private final Unsafe unsafe;

    private int capacity;
    private long base;
    private int size;

    public OffHeapLongList(int initialCapacity) {
        this.unsafe = UnsafeUtils.getUnsafe();
        this.capacity = Math.max(MIN_CAPACITY, initialCapacity);
        this.base = unsafe.allocateMemory(capacity * LONG_SIZE_IN_BYTES);
        OffHeapCleanerThread.register(this, this.base);
        System.out.println("OffHeapLongList with address: " + Long.toHexString(this.base) + " created");
    }

    public OffHeapLongList() {
        this(MIN_CAPACITY);
    }

    public void add(long value) {

        // Double memory and reallocate.
        if (size >= capacity) {
            capacity <<= 1;
            base = unsafe.reallocateMemory(base, capacity * LONG_SIZE_IN_BYTES);
        }

        unsafe.putLong(calculateAddress(size), value);
        ++size;
    }

    public long get(int index) {
        checkBoundary(index);
        return unsafe.getLong(calculateAddress(index));
    }

    private void checkBoundary(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index should be in range [0; " + (size - 1) + "]");
        }
    }

    private long calculateAddress(int index) {
        return base + index * LONG_SIZE_IN_BYTES;
    }

}
