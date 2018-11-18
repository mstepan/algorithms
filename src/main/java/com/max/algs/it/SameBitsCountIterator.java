package com.max.algs.it;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.google.common.base.Preconditions.checkState;


/**
 * Iterate over all integer values with the same number of bits set.
 */
public final class SameBitsCountIterator implements Iterator<Integer> {

    private final int initialValue;
    private int value;

    public SameBitsCountIterator(int initialValue) {
        this.initialValue = initialValue;
        this.value = initialValue;
    }

    @Override
    public boolean hasNext() {
        return (value & (-value)) > 0;
    }

    @Override
    public Integer next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        final int retValue = value;

        value = nextValue(value);

        return retValue;
    }

    private static int nextValue(int x) {

        final int smallest = x & (-x);

        checkState(smallest > 0, "No bigger element with same number of bits set");

        final int ripple = x + smallest;

        return ripple | ((x ^ ripple) >> 2) / smallest;
    }

    @Override
    public String toString() {
        return "initialValue = " + initialValue + ", value = " + value;
    }
}
