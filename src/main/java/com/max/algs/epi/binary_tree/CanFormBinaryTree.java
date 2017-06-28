package com.max.algs.epi.binary_tree;

import com.max.algs.util.ArrayUtils;
import org.apache.log4j.Logger;

import java.util.IdentityHashMap;
import java.util.Map;

import static com.max.algs.epi.binary_tree.BinaryTree.BinaryTreeNode;

public class CanFormBinaryTree {

    private static final Logger LOG = Logger.getLogger(CanFormBinaryTree.class);

    private CanFormBinaryTree() throws Exception {

        BinaryTreeNode A = new BinaryTreeNode("A", null, null);

        BinaryTreeNode B = new BinaryTreeNode("B", null, null);
        BinaryTreeNode C = new BinaryTreeNode("C", null, null);

        BinaryTreeNode D = new BinaryTreeNode("D", null, null);
        BinaryTreeNode E = new BinaryTreeNode("E", null, null);

        A.left = B;
        A.right = C;

        B.left = D;
        B.right = E;

        E.right = C;

        BinaryTreeNode[] nodes = {
                A,
                B,
                C,
                D,
                E
        };

        ArrayUtils.shuffle(nodes);

        System.out.printf("balanced: %b %n", canFormTree(nodes));

        System.out.printf("CheckIfBinaryTreeHeightBalanced done: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * Given an array of binary tree nodes, check if all of this nodes can form a tree.
     * <p>
     * time: O(N), space: O(N)
     */
    private static boolean canFormTree(BinaryTreeNode[] nodes) {

        final Object DUMMY = new Object();

        Map<BinaryTreeNode, Object> nodesMap = new IdentityHashMap<>();

        Map<BinaryTreeNode, Object> possibleRootNodes = new IdentityHashMap<>();

        for (BinaryTreeNode singleNode : nodes) {

            possibleRootNodes.put(singleNode, DUMMY);

            BinaryTreeNode left = singleNode.left;
            BinaryTreeNode right = singleNode.right;

            if (left != null && nodesMap.containsKey(left)) {
                return false;
            }

            if ((right != null && nodesMap.containsKey(right))) {
                return false;
            }

            // left and right points to the same node
            if (left == right) {
                return false;
            }

            possibleRootNodes.remove(left);
            possibleRootNodes.remove(right);

            nodesMap.put(left, DUMMY);
            nodesMap.put(right, DUMMY);
        }

        return possibleRootNodes.size() == 1;
    }

    public static void main(String[] args) {
        try {
            new CanFormBinaryTree();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

}
