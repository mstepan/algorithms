package com.max.algs.compression.lz;

import java.util.Iterator;

import static org.parboiled.common.Preconditions.checkArgument;

/**
 * Bounded circular queue used.
 */
public class SearchBuffer {

    private final char[] arr;

    private int size;
    private int head;
    private int tail;

    public SearchBuffer(int capacity) {
        checkArgument(capacity > 0, "Negative/zero 'capacity' passed: %s", capacity);
        this.arr = new char[capacity];

    }

    public Iterator<Character> getIteratorAtIndex(int index) {
        checkBoundaries(index);
        return new SimpleIterator(index);
    }

    public char get(int index) {

        checkBoundaries(index);

        int realIndex = (head + index) % arr.length;
        return arr[realIndex];
    }

    private void checkBoundaries(int index) {
        checkArgument(index >= 0 && index < size,
                "'index' out of buffer boundaries, index: %s, boundaries: [%s, %s]", index, 0, size - 1);
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int length() {
        return size;
    }

    public void add(char ch) {

        arr[tail] = ch;

        if (tail == head && size > 0) {
            head = (head + 1) % arr.length;
        }
        else {
            ++size;
        }

        tail = (tail + 1) % arr.length;
    }

    public void add(char[] data, int offset, int length) {
        for (int i = 0; i < length; i++) {
            add(data[offset + i]);
        }
    }

    @Override
    public String toString() {

        StringBuilder buf = new StringBuilder();

        buf.append("[");

        for (int i = 0; i < size; i++) {

            if (i != 0) {
                buf.append(", ");
            }

            buf.append(get(i));
        }

        buf.append("]");

        return buf.toString();
    }

    private final class SimpleIterator implements Iterator<Character> {

        private int index;

        SimpleIterator(int i) {
            index = (head + i) % arr.length;
        }

        @Override
        public boolean hasNext() {
            return index != tail;
        }

        @Override
        public Character next() {
            char res = arr[index];
            index = (index + 1) % arr.length;
            return res;
        }
    }
}
