package com.max.algs.ds.heap;

import org.apache.log4j.Logger;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * 3-ary min/max heap.
 */
public class TernaryMinHeap<T extends Comparable<T>> {

    private static final Logger LOG = Logger.getLogger(TernaryMinHeap.class);

    private static final int DEFAULT_CAPACITY = 8;

    private int size;

    protected T[] data;

    public TernaryMinHeap() {
        data = createArray(DEFAULT_CAPACITY);
    }

    public void add(T value) {

        checkArgument(value != null, "Can't store 'null' value");

        if (size >= data.length) {
            resize();
        }

        data[size] = value;
        ++size;

        fixUp(size - 1);
    }


    public T min(){
        checkNotEmpty();
        return data[0];
    }

    public T extract() {

        checkNotEmpty();

        T res = data[0];

        data[0] = data[size - 1];
        data[size - 1] = null;
        --size;

        fixDown(0);

        shrink();

        return res;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void checkNotEmpty(){
        if (size == 0) {
            throw new IllegalStateException("Can't extract element from empty heap");
        }
    }

    private void resize() {

        T[] tempData = data;

        data = createArray(data.length << 1);
        System.arraycopy(tempData, 0, data, 0, tempData.length);

        LOG.debug("resized, old  = " + tempData.length + ", new = " + data.length);
    }

    private void shrink() {

        if ( data.length > DEFAULT_CAPACITY && size < (data.length >> 1)) {
            T[] tempData = data;
            data = createArray(data.length - data.length/4 );
            System.arraycopy(tempData, 0, data, 0, size);

            LOG.debug("shrinked, old  = " + tempData.length + ", new = " + data.length);
        }
    }

    @SuppressWarnings("unchecked")
    private T[] createArray(int capacity){
        return (T[]) new Comparable[capacity];
    }

    private void fixUp(int index) {

        int child = index;
        int parent = (child - 1) / 3;

        while (child > 0 && data[child].compareTo(data[parent]) < 0) {

            T temp = data[parent];
            data[parent] = data[child];
            data[child] = temp;

            child = parent;
            parent = (child - 1) / 3;
        }
    }

    private void fixDown(int index1) {

        int currentIndex = index1;

        while (true) {

            int child1 = 3 * currentIndex + 1;
            int child2 = 3 * currentIndex + 2;
            int child3 = 3 * currentIndex + 3;

            int minIndex = currentIndex;

            if (child1 < size && data[child1].compareTo(data[minIndex]) < 0) {
                minIndex = child1;
            }

            if (child2 < size && data[child2].compareTo(data[minIndex]) < 0) {
                minIndex = child2;
            }

            if (child3 < size && data[child3].compareTo(data[minIndex]) < 0) {
                minIndex = child3;
            }

            if (minIndex == currentIndex) {
                break;
            }

            T temp = data[currentIndex];
            data[currentIndex] = data[minIndex];
            data[minIndex] = temp;
            currentIndex = minIndex;
        }

    }
}
