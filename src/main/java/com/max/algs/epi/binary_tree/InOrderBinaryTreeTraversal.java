package com.max.algs.epi.binary_tree;

import org.apache.log4j.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

public class InOrderBinaryTreeTraversal {

    private static final Logger LOG = Logger.getLogger(InOrderBinaryTreeTraversal.class);

    private InOrderBinaryTreeTraversal() throws Exception {

        BinaryTreeNode ten = new BinaryTreeNode(10);
        BinaryTreeNode five = new BinaryTreeNode(5);
        BinaryTreeNode seven = new BinaryTreeNode(7);

        ten.left = five;
        five.parent = ten;
        five.right = seven;
        seven.parent = five;


        BinaryTreeNode twenty = new BinaryTreeNode(20);
        BinaryTreeNode seventeen = new BinaryTreeNode(17);
        BinaryTreeNode twentyFour = new BinaryTreeNode(24);
        BinaryTreeNode twentyOne = new BinaryTreeNode(21);

        ten.right = twenty;
        twenty.parent = ten;

        twenty.left = seventeen;
        seventeen.parent = twenty;

        twenty.right = twentyFour;
        twentyFour.parent = twenty;

        twentyFour.left = twentyOne;
        twentyOne.parent = twentyFour;

        inOrderTraversal(ten);

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * In-order tree traversal.  left-parent-right
     * <p>
     * time: O(N)
     * space: O(1)
     */
    private static void inOrderTraversal(BinaryTreeNode root) {
        checkNotNull(root);

        BinaryTreeNode prev = root;
        BinaryTreeNode cur = root;

        while (cur != null) {

            // just finished with 'right' subtree, move to parent
            if (cur.right == prev) {
                prev = cur;
                cur = cur.parent;
            }
            // no left node or just finished with 'left' subtree
            else if (cur.left == null || cur.left == prev) {
                System.out.printf("%d, ", cur.value);
                prev = cur;

                // no 'right', move to parent
                if (cur.right == null) {
                    cur = cur.parent;
                }
                // move to 'right'
                else {
                    cur = cur.right;
                }
            }
            // move to 'left'
            else {
                prev = cur;
                cur = cur.left;
            }
        }

        System.out.println();
    }

    public static void main(String[] args) {
        try {
            new InOrderBinaryTreeTraversal();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    private static final class BinaryTreeNode {
        final int value;
        BinaryTreeNode left;
        BinaryTreeNode right;
        BinaryTreeNode parent;

        BinaryTreeNode(int value) {
            this.value = value;
        }
    }

}
