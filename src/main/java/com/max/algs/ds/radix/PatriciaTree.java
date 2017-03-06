package com.max.algs.ds.radix;

import javax.annotation.Nonnull;

/**
 * Patricia (Practical Algorithm To Retrieve Information Coded In Alphanumeric) tree implementation.
 * Handle String values from left-to-right. Do not allow to store duplicates.
 * <p>
 * Not tread safe.
 */
public class PatriciaTree {

    private static final int BITS_PER_CHAR = 16;

    private final Node head = new Node("head", -1);

    public boolean add(@Nonnull String value) {

        int bitsCount = value.length() * BITS_PER_CHAR;

        for (int i = 0; i <= bitsCount; i++) {

        }

        return false;
    }

    public boolean contains(int value) {
        return false;
    }


    public boolean remove(String value) {
        return false;
    }

    private static final class Node {
        final String value;
        final int bitIndex;

        Node left;
        Node right;

        Node(String value, int bitIndex) {
            this.value = value;
            this.bitIndex = bitIndex;
        }

        @Override
        public String toString() {
            return value + ", bitIndex: " + bitIndex;
        }
    }


}
