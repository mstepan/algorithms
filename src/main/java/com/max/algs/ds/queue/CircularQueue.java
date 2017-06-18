package com.max.algs.ds.queue;

import org.apache.log4j.Logger;


public class CircularQueue<T> {

    private static final Logger LOG = Logger.getLogger(CircularQueue.class);

    private static final int DEFAULT_CAPACITY = 8;

    private int size;
    private int head, tail;

    @SuppressWarnings("unchecked")
    private T[] arr = (T[]) new Object[DEFAULT_CAPACITY];

    public void add(T value) {

        if (size == arr.length) {
            resize();
        }

        arr[tail] = value;
        tail = (tail + 1) % arr.length;

        ++size;

    }

    @SuppressWarnings("unchecked")
    private void resize() {

        LOG.info("Resizing...");

        T[] temp = arr;

        arr = (T[]) new Object[arr.length << 1];

        int index = 0;
        int oldIndex = head;

        while (index < size) {
            arr[index] = temp[oldIndex];
            ++index;
            oldIndex = (oldIndex + 1) % temp.length;
        }

        head = 0;
        tail = index;
    }

    public T dequeue() {

        if (size == 0) {
            throw new IllegalStateException("Can't dequeue from empty queue");
        }

        T res = arr[head];
        arr[head] = null;
        head = (head + 1) % arr.length;

        --size;

        return res;
    }


    public int size() {
        return size;
    }


    public boolean isEmpty() {
        return size == 0;
    }

}
