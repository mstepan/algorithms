package com.max.algs;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static com.google.common.base.Preconditions.checkState;

public final class Treap {

    private static final Random RAND = ThreadLocalRandom.current();

    // TODO: use sentinel here,new Node(0, Integer.MAX_VALUE);
    private Node root;

    private int size;

    public boolean add(int value) {
        if (root == null) {
            root = new Node(value, RAND.nextInt());
        }
        else {
            Node nodeOrParent = findNodeOrParent(value);

            if (nodeOrParent.value == value) {
                return false;
            }

            Node newNode = new Node(value, RAND.nextInt());
            newNode.parent = nodeOrParent;

            if (value < nodeOrParent.value) {
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

    public boolean contains(int value) {
        return findNodeOrParent(value).value == value;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private Node findNodeOrParent(int value) {

        Node parent = null;
        Node cur = root;

        while (cur != null) {

            if (cur.value == value) {
                return cur;
            }

            parent = cur;

            cur = (value < cur.value) ? cur.left : cur.right;
        }

        return parent;
    }

    /**
     * Rotate current node till max heap property satisfied.
     */
    private void rotateToSatisfyHeapConstraint(Node cur) {
        while (cur.parent != null && cur.parent.priority < cur.priority) {
            moveUp(cur);
        }

        if( cur.parent == null ){
            root = cur;
        }
    }

    private void moveUp(Node cur) {

        checkState(cur.parent != null);

        Node upNode = cur.parent;
        Node parent = upNode.parent;

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

    private static class Node {
        Node left;
        Node right;
        Node parent;

        final int value;
        final int priority;

        Node(int value, int priority) {
            this.value = value;
            this.priority = priority;
        }

        @Override
        public String toString() {
            return "value = " + value + ", priority = " + priority;
        }
    }

}
