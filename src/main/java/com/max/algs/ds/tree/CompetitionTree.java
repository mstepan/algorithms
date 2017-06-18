package com.max.algs.ds.tree;


import java.util.ArrayDeque;
import java.util.Queue;


/**
 * Min integer at 'root' node.
 * Doesn't allow to store Integer.MAX_VALUE.
 */
public class CompetitionTree {


    private ArrayNode root;


    public CompetitionTree(int[][] arrays) {
        if (arrays == null) {
            throw new IllegalArgumentException("NULL 'arrays' passed");
        }

        buildTree(arrays);
    }


    public int extract() {

        int retValue = root.value;

        ArrayNode cur = root;

        while (cur.isNode()) {

            if (cur.left.value == retValue) {
                cur = cur.left;
            }
            else {
                cur = cur.right;
            }
        }

        cur.moveNext();

        ArrayNode parentNode = cur.parent;

        while (parentNode != null) {
            parentNode.value = Math.min(parentNode.left.value, parentNode.right.value);
            parentNode = parentNode.parent;
        }


        return retValue;
    }


    public boolean hasNext() {
        return root.value != Integer.MAX_VALUE;
    }


    //==== PRIVATE ====

    private void buildTree(int[][] arrays) {

        Queue<ArrayNode> queue = new ArrayDeque<>();

        for (int[] arr : arrays) {
            queue.add(new ArrayNode(arr[0], arr));
        }

        ArrayNode node1 = null;
        ArrayNode node2 = null;
        ArrayNode newNode = null;

        while (queue.size() > 1) {
            node1 = queue.poll();
            node2 = queue.poll();

            newNode = new ArrayNode(Math.min(node1.value, node2.value), node1, node2);

            node1.parent = newNode;
            node2.parent = newNode;

            queue.add(newNode);
        }

        root = queue.poll();
    }


    //==== NESTED ====

    private static final class ArrayNode {

        int value;
        ArrayNode parent;
        ArrayNode left;
        ArrayNode right;
        private int[] arr;
        private int index;

        ArrayNode(int value, int[] arr) {
            this.value = value;
            this.arr = arr;
        }

        ArrayNode(int value, ArrayNode left, ArrayNode right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }


        boolean isLeaf() {
            return arr != null;
        }

        boolean isNode() {
            return !isLeaf();
        }

        void moveNext() {

            if (index < arr.length - 1) {
                ++index;
                value = arr[index];
            }
            else {
                value = Integer.MAX_VALUE;
            }

        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

}
