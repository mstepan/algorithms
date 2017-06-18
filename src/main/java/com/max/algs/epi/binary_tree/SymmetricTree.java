package com.max.algs.epi.binary_tree;

import com.max.algs.ds.Pair;

import java.util.ArrayDeque;
import java.util.Deque;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.max.algs.epi.binary_tree.BinaryTree.BinaryTreeNode;

/**
 * 10.2. Test if binary tree is symmetric.
 */
public final class SymmetricTree {


    private SymmetricTree() throws Exception {

        BinaryTree tree = new BinaryTree();

        tree.addEdges(
                "A-B-LEFT",
                "A-b-RIGHT",

                "B-C-RIGHT",
//                "B-T-LEFT",

                "b-c-LEFT",

                "C-D-LEFT",
                "C-E-RIGHT",

                "c-e-LEFT",
                "c-d-RIGHT"
        );


        System.out.printf("isSymmetric: %b %n", isSymmetric(tree.root));

        System.out.printf("SymmetricTree done: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * Iterative approach.
     * <p>
     * time: O(n), space: O(h)
     */
    public static boolean isSymmetric(BinaryTreeNode root) {
        checkNotNull(root);

        Deque<Pair<BinaryTreeNode, BinaryTreeNode>> stack = new ArrayDeque<>();

        stack.push(new Pair<>(root.left, root.right));

        while (!stack.isEmpty()) {

            Pair<BinaryTreeNode, BinaryTreeNode> pair = stack.pop();

            BinaryTreeNode left = pair.getFirst();
            BinaryTreeNode right = pair.getSecond();

            if (left == null && right == null) {
                continue;
            }

            if ((left == null) ^ (right == null)) {
                return false;
            }

            if (!left.value.equalsIgnoreCase(right.value)) {
                return false;
            }

            stack.push(new Pair<>(left.left, right.right));
            stack.push(new Pair<>(left.right, right.left));

        }

        return true;
    }

    public static void main(String[] args) {
        try {
            new SymmetricTree();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
