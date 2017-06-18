package com.max.algs.compression.lz;


import java.io.BufferedReader;
import java.io.IOException;

import static org.parboiled.common.Preconditions.checkArgument;

public class SelfFetchBuffer {

    final char[] arr;
    private final BufferedReader in;
    int length;

    public SelfFetchBuffer(BufferedReader in, int capacity) {
        checkArgument(capacity > 0, "Negative/zero 'capacity' passed: %s", capacity);
        this.in = in;
        this.arr = new char[capacity];
        fetch();
    }

    public boolean isEmpty() {
        return length <= 0;
    }

    public char get(int index) {
        checkBoundaries(index);
        return arr[index];
    }

    private void checkBoundaries(int index) {
        checkArgument(index >= 0 && index < length,
                "'index' out of buffer boundaries, index: %s, boundaries: [%s, %s]", index, 0, length - 1);
    }

    public int effectiveLength() {
        return length;
    }

    public void fetch() {
        try {
            int readedBytes = in.read(arr);
            if (readedBytes > 0) {
                length = readedBytes;
            }
        }
        catch (IOException ioEx) {
            throw new IllegalStateException("Can't properly fetch data from file", ioEx);
        }
    }

    public void fetch(int offset) {
        try {

            // no elements left to fetch
            if (offset >= length) {
                length = 0;
                return;
            }

            int i = 0;
            for (int index = offset; index < length; i++, index++) {
                arr[i] = arr[index];
            }

            length -= offset;

            int actualReadedCount = in.read(arr, i, arr.length - length);

            length += (actualReadedCount > 0 ? actualReadedCount : 0);
        }
        catch (IOException ioEx) {
            throw new IllegalStateException("Can't properly fetch data from file", ioEx);
        }
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            if (i != 0) {
                buf.append(", ");
            }
            buf.append(arr[i]);
        }
        return buf.toString();
    }

}
