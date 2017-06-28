package com.max.algs.epi.binary_tree;

import org.apache.log4j.Logger;

import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

public class PreOrderBinaryTreeTraversal {

    private static final Logger LOG = Logger.getLogger(PreOrderBinaryTreeTraversal.class);

    private PreOrderBinaryTreeTraversal() throws Exception {

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

        preOrderTraversal(ten, System.out::println);

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * Pre-order tree traversal.  parent-left-right
     * <p>
     * time: O(N)
     * space: O(1)
     */
    private static void preOrderTraversal(BinaryTreeNode root, Consumer<Integer> consumer) {
        checkNotNull(root);

        BinaryTreeNode prev = root;
        BinaryTreeNode cur = root;

        while (cur != null) {

            // fully traversed LEFT subtree
            if (cur.left == prev) {
                prev = cur;
                cur = (cur.right == null ? cur.parent : cur.right);
            }

            // fully traversed RIGHT subtree
            else if (cur.right == prev) {
                prev = cur;
                cur = cur.parent;
            }
            else {

                // handle cur value
                consumer.accept(cur.value);
                prev = cur;

                // go left
                if (cur.left != null) {
                    cur = cur.left;
                }

                // go right
                else if (cur.right != null) {
                    cur = cur.right;
                }

                // go up to the parent
                else {
                    cur = cur.parent;
                }
            }
        }

        System.out.println();
    }

    public static void main(String[] args) {
        try {
            new PreOrderBinaryTreeTraversal();
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
