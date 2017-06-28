package com.max.algs.epi.binary_tree;

import org.apache.log4j.Logger;

import java.math.BigInteger;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.max.algs.epi.binary_tree.BinaryTree.BinaryTreeNode;

public class PathsWithSpecifiedSum {

    private static final Logger LOG = Logger.getLogger(PathsWithSpecifiedSum.class);

    private PathsWithSpecifiedSum() throws Exception {

        BinaryTree tree = new BinaryTree();

        tree.addEdges(
                "A314-B6-LEFT",
                "A314-I6-RIGHT",

                "B6-C271-LEFT",
                "B6-F561-RIGHT",

                "C271-D28-LEFT",
                "C271-E0-RIGHT",

                "F561-G3-RIGHT",

                "G3-H17-LEFT",


                "6I-J2-LEFT",
                "I6-O271-RIGHT",

                "O271-P28-RIGHT",

                "J2-K1-RIGHT",

                "K1-L401-LEFT",
                "K1-N257-RIGHT",

                "L401-M641-RIGHT"
        );

        int expSum = 619;

        List<String> paths = findAllPathsWithSum(tree.root, expSum);

        for (String singlePath : paths) {
            System.out.println(singlePath);
        }

        System.out.printf("CheckIfBinaryTreeHeightBalanced done: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * 10.6. Variant.
     * <p>
     * Find all root to leaf paths with specified sum.
     * <p>
     * N - number of nodes in binary tree.
     * L - number of paths with expected sum, can be up to 2^h in perfect binary tree.
     * <p>
     * time: O(N)
     * space: O(L*h) ~ O(2^h * h)
     * <p>
     * Stateless, so thread safe.
     */
    public static List<String> findAllPathsWithSum(BinaryTreeNode root, int expSum) {
        checkNotNull(root);

        List<String> res = new ArrayList<>();
        collectPathesRec(root, BigInteger.valueOf(expSum), BigInteger.ZERO, new ArrayDeque<>(), res);
        return Collections.unmodifiableList(res);
    }

    private static void collectPathesRec(BinaryTreeNode cur, BigInteger expSum, BigInteger actualSum,
                                         Deque<String> partialPath, List<String> res) {

        String strValue = cur.value;

        assert strValue != null && strValue.length() > 1 : "Incorrect node format detected, should be at least 2 symbols";

        String nodeName = String.valueOf(strValue.charAt(0));

        // can throw NumberFormatException, but it's ok
        int nodeValue = Integer.parseInt(strValue.substring(1));

        // use BigInteger to remove overflow/underflow here
        BigInteger curSum = actualSum.add(BigInteger.valueOf(nodeValue));
        partialPath.add(nodeName);

        if (cur.left == null && cur.right == null) {
            if (curSum.equals(expSum)) {
                res.add(partialPath.toString());
            }
        }
        else {
            if (cur.left != null) {
                collectPathesRec(cur.left, expSum, curSum, partialPath, res);
            }

            if (cur.right != null) {
                collectPathesRec(cur.right, expSum, curSum, partialPath, res);
            }
        }

        partialPath.pollLast();
    }

    public static void main(String[] args) {
        try {
            new PathsWithSpecifiedSum();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

}
