package com.max.algs.ds.tree;

import java.util.Random;

/**
 * Treap implementation based on binary search tree and max heap.
 * Not thread safe.
 */
public class Treap {

    private static final Random RAND = new Random();
    private TreapNode root;
    private int size;

    public boolean add(int value) {

        if (root == null) {
            root = new TreapNode(value);
        }
        else {
            TreapNode prev = null;
            TreapNode cur = root;

            while (cur != null) {

                // don't allow to store duplicates
                if (cur.value == value) {
                    return false;
                }

                prev = cur;
                if (cur.value < value) {
                    cur = cur.right;
                }
                else {
                    cur = cur.left;
                }
            }

            TreapNode newNode = new TreapNode(value, prev);

            if (prev.value < value) {
                prev.right = newNode;

            }
            else {
                prev.left = newNode;
            }

            fixUp(newNode);
        }

        ++size;
        return true;
    }

    private void fixUp(TreapNode nodeToMove) {

        TreapNode cur = nodeToMove;

        while (cur.parent != null && cur.parent.priority < cur.priority) {
            if (cur.parent.left == cur) {
                rotateRight(cur);
            }
            else {
                rotateLeft(cur);
            }
        }
    }

    private void rotateRight(TreapNode cur) {
        rotate(cur, false);
    }

    private void rotateLeft(TreapNode cur) {
        rotate(cur, true);
    }

    private void rotate(TreapNode cur, boolean left) {

        assert cur.parent != null : "cur.parent is null";

        TreapNode parent = cur.parent;
        TreapNode grandParent = parent.parent;

        if (left) {
            // do left rotation
            parent.right = cur.left;
            if (cur.left != null) {
                cur.left.parent = parent;
            }

            cur.left = parent;
        }
        else {
            // do right rotation
            parent.left = cur.right;
            if (cur.right != null) {
                cur.right.parent = parent;
            }

            cur.right = parent;
        }

        cur.parent = grandParent;
        parent.parent = cur;

        if (grandParent != null) {
            if (grandParent.left == parent) {
                grandParent.left = cur;
            }
            else {
                grandParent.right = cur;
            }
        }

        if (cur.parent == null) {
            root = cur;
        }
    }


    public boolean delete(int valueToDelete) {

        /*
        1. find TreapNode cur for 'valueToDelete'
        2. set cur.priority = Integer.MIN_VALUE;
        3. call fixDown(cur);
        4. delete leaf node 'cur'
         */

        //TODO:
        return false;
    }

    public boolean contains(int searchValue) {

        TreapNode cur = root;
        while (cur != null) {
            if (cur.value == searchValue) {
                return true;
            }

            if (cur.value < searchValue) {
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
        return root == null;
    }

    private static final class TreapNode {
        final int value;
        final int priority;

        TreapNode left;
        TreapNode right;
        TreapNode parent;

        public TreapNode(int value) {
            this.value = value;
            this.priority = RAND.nextInt();
        }

        public TreapNode(int value, TreapNode parent) {
            this(value);
            this.parent = parent;
        }

        @Override
        public String toString() {
            return "value: " + value + ", priority: " + priority;
        }
    }

}
