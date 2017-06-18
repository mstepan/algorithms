package com.max.algs.ds.tree;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Unbalanced binary search tree. Not thread safe. Don't allow duplicates. Don't
 * allow NULL values.
 */
public class BSTree<K extends Comparable<K>> {

    private static final String SEPARATOR = ", ";
    private static final Random RAND = ThreadLocalRandom.current();
    private Node<K> root;
    private int size;

    public static BSTree<Integer> createRandomTree(int maxValue, int size) {

        if (maxValue < size) {
            throw new IllegalArgumentException(
                    "Can't generate tree with desired size = " + size
                            + ", maxValue = " + maxValue);
        }

        BSTree<Integer> randomTree = new BSTree<>();
        int index = 0;

        while (index < size) {
            if (randomTree.add(RAND.nextInt(maxValue))) {
                ++index;
            }
        }

        return randomTree;
    }

    public static BSTree<Integer> createFromSortedArray(int[] arr) {
        BSTree<Integer> tree = new BSTree<>();
        tree.root = constructRec(null, arr, 0, arr.length - 1);
        tree.size = arr.length;
        return tree;
    }

    /**
     * time: O(N)
     * space: O(lgN)
     */
    private static Node<Integer> constructRec(Node<Integer> parent, int[] arr, int lo, int hi) {

        int elems = hi - lo + 1;

        if (elems == 1) {
            return new Node<>(arr[lo], parent);
        }

        if (elems == 2) {
            Node<Integer> node = new Node<>(arr[hi], parent);
            node.left = new Node<>(arr[lo], parent);
            return node;
        }

        int mid = lo + ((hi - lo) >> 1);

        Node<Integer> node = new Node<>(arr[mid], parent);
        node.left = constructRec(node, arr, lo, mid - 1);
        node.right = constructRec(node, arr, mid + 1, hi);

        return node;
    }

    /**
     * Find lowest common ancestor in binary search tree.
     * time: O(lgN)
     * space: O(1)
     */
    public Optional<K> lca(K first, K second) {
        checkNotNull(first, "Can't find LCA for null value");
        checkNotNull(second, "Can't find LCA for null value");

        // both elements should present in a tree
        if (!(contains(first) && contains(second))) {
            return Optional.empty();
        }

        // elements are equal, LCA is the element value itself
        if (first.equals(second)) {
            return Optional.of(first);
        }

        K minValue = first;
        K maxValue = second;

        if (first.compareTo(second) > 0) {
            minValue = second;
            maxValue = first;
        }

        Node<K> cur = root;

        while (true) {

            if (cur.value.equals(minValue) || cur.value.equals(maxValue)) {
                return Optional.of(cur.value);
            }

            int cmpResForMin = minValue.compareTo(cur.value);

            if (cmpResForMin < 0 && maxValue.compareTo(cur.value) > 0) {
                return Optional.of(cur.value);
            }

            // both values located to the 'left'
            if (cmpResForMin < 0) {

                assert maxValue.compareTo(cur.value) < 0 : "Incorrect invariant detected for left side move";

                cur = cur.left;
            }
            // both values located to the 'right'
            else {

                assert maxValue.compareTo(cur.value) > 0 : "Incorrect invariant detected for right side move";

                cur = cur.right;
            }
        }
    }

    /**
     * Return 'k' largets elements from BS tree. Use reverse in-order traversal.
     * time: O(h+k) space: O(h+k)
     */
    public List<K> getKLargest(int k) {

        checkArgument(k >= 0, "Negative parameter passed: %s", k);

        if (isEmpty() || k == 0) {
            return Collections.emptyList();
        }

        int elemsCount = Math.min(k, size);

        List<K> largest = new ArrayList<>(elemsCount);

        Node<K> cur = root;
        Deque<Node<K>> stack = new ArrayDeque<>();

        while (cur != null && largest.size() < k) {

            if (cur.right != null) {
                stack.push(cur);
                cur = cur.right;
            }
            else {
                largest.add(cur.value);
                cur = cur.left;

                while (cur == null && !stack.isEmpty() && largest.size() < k) {
                    cur = stack.pop();
                    largest.add(cur.value);
                    cur = cur.left;
                }
            }
        }

        return largest;
    }


    /**
     * time: O(N)
     * space: O(h), where 'h' - height of binary search tree. For balanced BStree this value equals lgN.
     */
    private void reverseInOrderRec(Node<Integer> cur, Consumer<Node<Integer>> consumer) {

        if (cur.right != null) {
            reverseInOrderRec(cur.right, consumer);
        }

        consumer.accept(cur);

        if (cur.left != null) {
            reverseInOrderRec(cur.left, consumer);
        }
    }

    public boolean add(K newValue) {

        if (newValue == null) {
            throw new IllegalArgumentException("Can't save NULL value");
        }

        if (size == 0) {
            root = new Node<>(newValue, null);
        }
        else {

            Node<K> cur = root;
            Node<K> prev = null;

            while (cur != null) {

                prev = cur;

                if (newValue.compareTo(cur.value) == 0) {
                    return false;
                }
                else if (newValue.compareTo(cur.value) > 0) {
                    cur = cur.right;
                }
                else {
                    cur = cur.left;
                }
            }

            assert prev != null : "'prev' value is NULL";

            if (newValue.compareTo(prev.value) < 0) {
                prev.left = new Node<>(newValue, prev);
            }
            else {
                prev.right = new Node<>(newValue, prev);
            }
        }

        ++size;
        return true;
    }

    public boolean remove(K value) {
        return false;
    }

    public boolean contains(K searchValue) {

        Node<K> cur = root;

        while (cur != null) {

            if (cur.value.equals(searchValue)) {
                return true;
            }
            else if (searchValue.compareTo(cur.value) > 0) {
                cur = cur.right;
            }
            else {
                cur = cur.left;
            }
        }

        return false;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private int subtreeSize(Node<K> cur) {
        if (cur == null) {
            return 0;
        }

        return 1 + subtreeSize(cur.left) + subtreeSize(cur.right);
    }

    /**
     * In-order tree String representation. Use stack.
     */
    public String inOrderStr() {

        if (root == null) {
            return "[]";
        }

//        StringBuilder buf = new StringBuilder();
//        buf.append("[");
        Deque<Node<K>> stack = new ArrayDeque<>();
        Node<K> cur = root;

        while (cur != null) {

            if (cur.left != null) {
                stack.push(cur);
                cur = cur.left;
            }
            else {

//                buf.append(cur.value).append(SEPARATOR);
                cur = cur.right;

                while (cur == null && !stack.isEmpty()) {
                    cur = stack.pop();
//                    buf.append(cur.value).append(SEPARATOR);
                    cur = cur.right;
                }
            }
        }

//        buf.delete(buf.length() - SEPARATOR.length(), buf.length());
//        buf.append("]");

//        return buf.toString();
        return "";
    }

    public String inOrderMorrisStr() {

        if (root == null) {
            return "[]";
        }

//        StringBuilder buf = new StringBuilder();
//        buf.append("[");

        Node<K> cur = root;

        while (cur != null) {
            if (cur.left == null) {
//                buf.append(cur.value).append(SEPARATOR);
                cur = cur.right;
            }
            else {

                Node pred = cur.left;

                while (pred.right != null && pred.right != cur) {
                    pred = pred.right;
                }

                if (pred.right == null) {
                    pred.right = cur;
                    cur = cur.left;
                }
                else {
                    pred.right = null;
//                    buf.append(cur.value).append(SEPARATOR);
                    cur = cur.right;
                }
            }
        }

//        buf.delete(buf.length() - SEPARATOR.length(), buf.length());
//        buf.append("]");
//        return buf.toString();
        return "";
    }

    /**
     * Returns in-order tree as a String.
     * Use Morris in-order traversation.
     * time: O(N)
     * space: O(1)
     */
    @Override
    public String toString() {

        StringBuilder buf = new StringBuilder("[");

        Node<K> cur = root;
        Node<K> next;

        while (cur != null) {
            if (cur.left == null) {

                if (buf.length() > 1) {
                    buf.append(", ");
                }

                buf.append(cur.value);
                cur = cur.right;
                continue;
            }

            next = cur.left;

            while (next.right != null && next.right != cur) {
                next = next.right;
            }

            if (next.right == null) {
                next.right = cur;
                cur = cur.left;
            }
            else {
                next.right = null;
                if (buf.length() > 1) {
                    buf.append(", ");
                }
                buf.append(cur.value);
                cur = cur.right;
            }
        }

        buf.append("]");

        return buf.toString();
    }

    private String createRepetition(String base, int count) {

        StringBuilder buf = new StringBuilder(base.length() * count);

        for (int i = 0; i < count; i++) {
            buf.append(base);
        }

        return buf.toString();

    }

    static class NodeWithRange<U extends Comparable<U>> {

        final int from;
        final Node<U> node;

        NodeWithRange(int from, Node<U> node) {
            super();
            this.from = from;
            this.node = node;
        }
    }

    public static final class Node<U extends Comparable<U>> {

        @SuppressWarnings("unused")
        final Node<U> parent;
        public U value;
        Node<U> left;
        Node<U> right;

        Node(U value, Node<U> parent) {
            this.value = value;
            this.parent = parent;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

}
