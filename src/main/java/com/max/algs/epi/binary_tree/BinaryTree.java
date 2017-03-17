package com.max.algs.epi.binary_tree;


import com.google.common.base.Objects;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

final class BinaryTree {

    /**
     * Do not use "[-]" here, because this will call 'Pattern.compile(regex).split(...)' for each call.
     */
    private static final String EDGES_DELIMITER = "-";

    private final Map<String, BinaryTreeNode> nodes = new HashMap<>();

    BinaryTreeNode root;

    void addEdges(String... edges) {

        checkNotNull(edges);

        for (String edgeStr : edges) {
            String[] elements = edgeStr.split(EDGES_DELIMITER);
            checkArgument(elements.length == 3, "Invalid edge format: '%s', expected: '%s'", edgeStr, "A-B-LEFT");
            addEdge(elements[0], elements[1], ChildDirection.valueOf(elements[2]));
        }
    }

    public int size() {
        return nodes.size();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    private void addEdge(String parentValue, String childValue, ChildDirection direction) {

        assert direction != null : "null 'direction' detected";

        BinaryTreeNode parent = nodes.computeIfAbsent(parentValue, BinaryTreeNode::new);
        BinaryTreeNode child = nodes.computeIfAbsent(childValue, BinaryTreeNode::new);

        if (root == null) {
            root = parent;
        }

        if (direction == ChildDirection.LEFT) {
            assert parent.left == null : "duplicate 'left' node set";
            parent.left = child;
        }
        else {
            assert parent.right == null : "duplicate 'right' node set";
            parent.right = child;
        }
    }

    private enum ChildDirection {
        LEFT, RIGHT
    }

    static class BinaryTreeNode {

        String value;
        BinaryTreeNode left;
        BinaryTreeNode right;

        BinaryTreeNode(String value, BinaryTreeNode left, BinaryTreeNode right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }

        BinaryTreeNode(String value) {
            this(value, null, null);
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            BinaryTreeNode that = (BinaryTreeNode) o;
            return Objects.equal(left, that.left) &&
                    Objects.equal(right, that.right);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(left, right);
        }
    }

}
