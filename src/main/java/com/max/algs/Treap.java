package com.max.algs;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static com.google.common.base.Preconditions.checkState;

public final class Treap<T extends Comparable<T>> {

    private static final Random RAND = ThreadLocalRandom.current();

    // TODO: use sentinel here,new Node(0, Integer.MAX_VALUE);
    private Node<T> root;

    private int size;

    public boolean add(T value) {
        if (root == null) {
            root = new Node<>(value, RAND.nextInt());
        }
        else {
            Node<T> nodeOrParent = findNodeOrParent(value);

            if (nodeOrParent.value == value) {
                return false;
            }

            Node<T> newNode = new Node<>(value, RAND.nextInt());
            newNode.parent = nodeOrParent;

            if (value.compareTo(nodeOrParent.value) < 0) {
                nodeOrParent.left = newNode;
            }
            else {
                nodeOrParent.right = newNode;
            }

            rotateToSatisfyHeapConstraint(newNode);
        }

        ++size;
        return true;
    }

    public boolean contains(T value) {
        return findNodeOrParent(value).value.compareTo(value) == 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private Node<T> findNodeOrParent(T value) {

        Node<T> parent = null;
        Node<T> cur = root;

        while (cur != null) {

            if (cur.value.compareTo(value) == 0) {
                return cur;
            }

            parent = cur;

            cur = (value.compareTo(cur.value) < 0) ? cur.left : cur.right;
        }

        checkState(parent != null);
        return parent;
    }

    /**
     * Rotate current node till max heap property satisfied.
     */
    private void rotateToSatisfyHeapConstraint(Node<T> cur) {
        while (cur.parent != null && cur.parent.priority < cur.priority) {
            moveUp(cur);
        }

        if (cur.parent == null) {
            root = cur;
        }
    }

    private void moveUp(Node<T> cur) {

        checkState(cur.parent != null);

        Node<T> upNode = cur.parent;
        Node<T> parent = upNode.parent;

        if (parent == null) {
            root = cur;
        }
        else {
            if (parent.left == upNode) {
                parent.left = cur;
            }
            else {
                parent.right = cur;
            }
        }

        cur.parent = parent;

        if (upNode.right == cur) {

            upNode.right = cur.left;
            if (cur.left != null) {
                cur.left.parent = upNode;
            }

            cur.left = upNode;
            upNode.parent = cur;
        }
        else {
            upNode.left = cur.right;
            if (cur.right != null) {
                cur.right.parent = upNode;
            }

            cur.right = upNode;
            upNode.parent = cur;
        }
    }

    private static class Node<U extends Comparable<U>> {
        Node<U> left;
        Node<U> right;
        Node<U> parent;

        final U value;
        final int priority;

        Node(U value, int priority) {
            this.value = value;
            this.priority = priority;
        }

        @Override
        public String toString() {
            return "value = " + value + ", priority = " + priority;
        }
    }

}
