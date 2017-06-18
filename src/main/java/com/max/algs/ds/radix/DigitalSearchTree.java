package com.max.algs.ds.radix;

import java.util.Optional;

/**
 * Simple implementation of digital (radix) search tree.
 * Key bits processed from most significant (left most) one.
 * "head" field always not null, dummy node used.
 * <p>
 * Not thread safe. Do not allow to store duplicates.
 */
public class DigitalSearchTree {

    private static final int MOST_SIGNIFICANT_BIT = 31;
    private final DigitalNode head = new DigitalNode(0);
    private int size;

    private static DigitalNode extractLeftmostLeaf(DigitalNode cur) {

        DigitalNode parent = cur;

        while (true) {

            // leaf node found
            if (cur.isLeaf()) {

                if (parent.left == cur) {
                    parent.left = null;
                }
                else {
                    parent.right = null;
                }

                return cur;
            }

            parent = cur;

            if (cur.left != null) {
                cur = cur.left;
            }
            else {
                cur = cur.right;
            }
        }
    }

    private static int getBitByIndex(int value, int index) {
        return (value >> index) & 0x1;
    }

    public boolean add(int value) {

        DigitalNode parent = null;
        DigitalNode cur = head;
        int lastBitValue = -1;

        for (int i = MOST_SIGNIFICANT_BIT; i >= 0; i--) {

            lastBitValue = getBitByIndex(value, i);
            parent = cur;

            // go right
            if (lastBitValue == 1) {
                cur = cur.right;
            }
            //go left
            else {
                cur = cur.left;
            }

            if (cur == null) {
                break;
            }

            // duplicate value found
            if (cur.value == value) {
                return false;
            }
        }

        if (lastBitValue == 1) {
            parent.right = new DigitalNode(value);
        }
        else {
            parent.left = new DigitalNode(value);
        }

        ++size;
        return true;
    }

    public boolean contains(int value) {
        return findNode(value).isPresent();
    }

    public boolean remove(int value) {

        Optional<SearchResult> entry = findNode(value);

        if (entry.isPresent()) {
            removeNode(entry.get().parent, entry.get().cur, entry.get().lastBit);
            return true;
        }

        return false;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private Optional<SearchResult> findNode(int value) {

        if (size == 0) {
            return Optional.empty();
        }

        DigitalNode parent;
        DigitalNode cur = head;

        for (int i = MOST_SIGNIFICANT_BIT; i >= 0; i--) {

            int bitValue = getBitByIndex(value, i);
            parent = cur;

            // go right
            if (getBitByIndex(value, i) == 1) {
                cur = cur.right;
            }
            //go left
            else {
                cur = cur.left;
            }

            if (cur == null) {
                break;
            }

            if (cur.value == value) {
                return Optional.of(new SearchResult(parent, cur, bitValue));
            }
        }

        return Optional.empty();
    }

    private void removeNode(DigitalNode parent, DigitalNode cur, int bitValue) {

        // delete leaf node
        if (cur.isLeaf()) {
            if (bitValue == 1) {
                parent.right = null;
            }
            else {
                parent.left = null;
            }
        }

        // delete non leaf node
        else {
            DigitalNode leafNode = extractLeftmostLeaf(cur);

            if (cur.left != leafNode) {
                leafNode.left = cur.left;
            }

            if (cur.right != leafNode) {
                leafNode.right = cur.right;
            }

            if (bitValue == 1) {
                parent.right = leafNode;
            }
            else {
                parent.left = leafNode;
            }

            cur.left = null;
            cur.right = null;
        }

        --size;
    }

    private static final class SearchResult {
        final DigitalNode parent;
        final DigitalNode cur;
        final int lastBit;

        public SearchResult(DigitalNode parent, DigitalNode cur, int lastBit) {
            this.parent = parent;
            this.cur = cur;
            this.lastBit = lastBit;
        }

        @Override
        public String toString() {
            return "parent: " + parent + ", cur: " + cur + ", lastBit: " + lastBit;
        }
    }

    private static final class DigitalNode {
        final int value;

        DigitalNode left;
        DigitalNode right;

        DigitalNode(int value) {
            this.value = value;
        }

        boolean isLeaf() {
            return left == null && right == null;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

}
