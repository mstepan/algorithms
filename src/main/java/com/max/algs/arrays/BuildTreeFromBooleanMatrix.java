package com.max.algs.arrays;

import org.apache.log4j.Logger;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * You are given an NxN boolean matrix where matrix[i][j] == true,
 * if 'i'-th node is a parent for 'j'-th node.
 */
public class BuildTreeFromBooleanMatrix {

    private static final Logger LOG = Logger.getLogger(BuildTreeFromBooleanMatrix.class);

    private BuildTreeFromBooleanMatrix() throws Exception {

        boolean[][] matrix = {
                {false, false, false, false, false, false},
                {true, false, false, false, true, false},
                {false, false, false, false, false, false},
                {false, false, false, false, false, false},
                {false, false, true, true, false, true},
                {false, false, false, false, false, false},
        };


        TreeNode root = buildTree(matrix);

        System.out.println(root);

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * time: O(N^2)
     * space: O(N)
     */
    public static TreeNode buildTree(boolean[][] matrix) {
        checkNotNull(matrix);
        checkArgument(matrix.length > 0, "'matrix' is empty");
        checkIsMatrix(matrix);

        TreeNode[] nodes = new TreeNode[matrix.length];
        BitSet possibleRoot = new BitSet(nodes.length);
        possibleRoot.set(0, nodes.length, true);

        // time: O(N)
        for (int nodeValue = 0; nodeValue < matrix.length; ++nodeValue) {
            nodes[nodeValue] = new TreeNode(nodeValue);
        }

        // time: O(N^2)
        for (int row = 0; row < matrix.length; ++row) {
            for (int col = 0; col < matrix[row].length; ++col) {
                if (matrix[row][col]) {
                    TreeNode parent = nodes[row];
                    TreeNode child = nodes[col];

                    possibleRoot.clear(col);
                    parent.addChild(child);
                }
            }
        }

        // time: O(N)
        checkIsTree(nodes, possibleRoot);

        int rootIndex = possibleRoot.nextSetBit(0);

        return nodes[rootIndex];
    }

    private static void checkIsMatrix(boolean[][] matrix) {

        int rowsCount = matrix.length;

        for (boolean[] row : matrix) {
            if (row == null || row.length != rowsCount) {
                throw new IllegalArgumentException("Not a square matrix");
            }
        }
    }

    private static void checkIsTree(TreeNode[] nodes, BitSet possibleRoot) {
        // check we have only one root, so the tree is fully connected
        int candidateForRootCnt = possibleRoot.cardinality();

        if (candidateForRootCnt != 1) {
            throw new IllegalStateException("Not a tree");
        }

        // do DFS and check for a cycle (back edge)
        Set<TreeNode> markedNodes = new HashSet<>();

        TreeNode root = nodes[possibleRoot.nextSetBit(0)];

        Deque<TreeNode> stack = new ArrayDeque<>();
        stack.push(root);
        markedNodes.add(root);

        while (!stack.isEmpty()) {
            TreeNode curNode = stack.pollFirst();

            for (TreeNode child : curNode.children) {
                if (markedNodes.contains(child)) {
                    throw new IllegalStateException("Not a tree, there is a cycle between '" +
                            child.value + "' and '" + curNode.value + "'");
                }

                markedNodes.add(child);
                stack.push(child);
            }
        }
    }

    public static void main(String[] args) {
        try {
            new BuildTreeFromBooleanMatrix();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    private static final class TreeNode {
        final int value;
        final Set<TreeNode> children;

        TreeNode(int value) {
            this.value = value;
            this.children = new LinkedHashSet<>();
        }

        void addChild(TreeNode child) {
            children.add(child);
        }

        @Override
        public String toString() {
            return String.valueOf(value) + ", " + (children.size() == 0 ? "leaf" : "node");
        }
    }

}

