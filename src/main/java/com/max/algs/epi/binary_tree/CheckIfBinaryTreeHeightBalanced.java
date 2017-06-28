package com.max.algs.epi.binary_tree;

import org.apache.log4j.Logger;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static com.max.algs.epi.binary_tree.BinaryTree.BinaryTreeNode;

public class CheckIfBinaryTreeHeightBalanced {

    private static final Logger LOG = Logger.getLogger(CheckIfBinaryTreeHeightBalanced.class);

    private static final Random RAND = ThreadLocalRandom.current();

    private CheckIfBinaryTreeHeightBalanced() throws Exception {

        BinaryTree tree = new BinaryTree();

        tree.addEdges(
                "A-B-LEFT",
                "A-C-RIGHT",
                "C-D-LEFT",
                "C-E-RIGHT"

//                "D-F-RIGHT"
        );


        System.out.printf("balanced: %b %n", isBalanced(tree.root));

        System.out.printf("CheckIfBinaryTreeHeightBalanced done: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * time: O(N), space: O(h) ~ O(N)
     * <p>
     * Thread safe.
     */
    private static boolean isBalanced(BinaryTreeNode root) {
        final boolean[] balanced = {true};
        isBalancedRec(root, balanced);
        return balanced[0];
    }

    private static int isBalancedRec(BinaryTreeNode cur, boolean[] balanced) {

        if (cur == null) {
            return -1;
        }

        // Randomly choose which child node to handle first (left or right), to reduce
        // possibility of worst case time/space complexity.
        BinaryTreeNode firstChild = cur.left;
        BinaryTreeNode secondChild = cur.right;

        if (RAND.nextBoolean()) {
            BinaryTreeNode temp = firstChild;
            firstChild = secondChild;
            secondChild = temp;
        }

        int firstHeight = isBalancedRec(firstChild, balanced);

        if (!balanced[0]) {
            return firstHeight;
        }

        int secondHeight = isBalancedRec(secondChild, balanced);

        if (!balanced[0]) {
            return secondHeight;
        }

        if (Math.abs(firstHeight - secondHeight) > 1) {
            balanced[0] = false;
        }

        return 1 + Math.max(firstHeight, secondHeight);
    }

    public static void main(String[] args) {
        try {
            new CheckIfBinaryTreeHeightBalanced();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

}
