package com.max.algs.ds.heap;

import java.lang.reflect.Array;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Min heap.
 */
public class MinHeap<T extends Comparable<T>> {

    private static final int INITIAL_CAPACITY = 8;

    private static final int MIN_ARITY = 2;
    private static final int MAX_ARITY = 9;

    private static final int DEFAULT_ARITY = 4;

    private final int arity;

    private T[] arr;
    private int size;

    public MinHeap() {
        this(DEFAULT_ARITY);
    }

    public MinHeap(int arity) {
        checkArgument(arity >= MIN_ARITY && arity <= MAX_ARITY,
                "'arity' parameter is incorrect, should be in range [%s, %s], actual = %s", MIN_ARITY, MAX_ARITY, arity);
        this.arity = arity;
        this.arr = createArray(INITIAL_CAPACITY);
    }

    public void add(T value) {
        checkNotNull(value);

        if (size == arr.length) {
            resize();
        }

        arr[size] = value;
        fixUp(size);
        ++size;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public T poll() {

        checkArgument(size != 0, "Empty heap");

        T res = arr[0];
        arr[0] = arr[size - 1];
        arr[size - 1] = null;
        --size;

        fixDown(0);

        return res;
    }

    private void resize() {

        T[] temp = arr;

        // double array size
        arr = createArray(arr.length << 1);

        System.arraycopy(temp, 0, arr, 0, temp.length);

//        System.out.printf("resized from %d to %d %n", temp.length, arr.length);
    }

    private void fixUp(int index) {

        int child = index;
        int parent = parentIndex(index);

        while (child != 0 && arr[parent].compareTo(arr[child]) > 0) {
            swap(parent, child);
            child = parent;
            parent = parentIndex(child);
        }
    }

    private int parentIndex(int index) {
        return (index - 1) / arity;
    }

    private void fixDown(int index) {

        int parent = index;

        while (parent < size) {

            int minIndex = parent;

            for (int i = 1; i <= arity; ++i) {
                int child = parent * arity + i;

                if (child >= size) {
                    break;
                }

                if (arr[child].compareTo(arr[minIndex]) < 0) {
                    minIndex = child;
                }
            }

            if (parent == minIndex) {
                break;
            }

            swap(parent, minIndex);

            parent = minIndex;
        }
    }

    private void swap(int from, int to) {
        T temp = arr[from];
        arr[from] = arr[to];
        arr[to] = temp;
    }

    @SuppressWarnings("unchecked")
    private T[] createArray(int length) {
        return (T[]) Array.newInstance(Comparable.class, length);
    }
}
