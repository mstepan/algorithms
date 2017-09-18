package com.max.algs.epi.recursion;

import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;


public final class GenerateAllBinaryTrees {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());

    private static final class BinaryNode {
        BinaryNode left;
        BinaryNode right;

        BinaryNode(BinaryNode left, BinaryNode right) {
            this.left = left;
            this.right = right;
        }

        BinaryNode() {
            this(null, null);
        }

        @Override
        public String toString() {
            return (left == null && right == null) ? "leaf" : "node";
        }
    }

    /**
     * Generate all possible binary trees.
     * <p>
     * C - Catalans number for 'N'
     * <p>
     * time: O(C*N)
     * space: O(C*N)
     */
    private static List<BinaryNode> generateAllBinaryTrees(int nodesCount) {
        checkArgument(nodesCount >= 0);
        return generateTreesRec(nodesCount);
    }

    private static List<BinaryNode> generateTreesRec(int cnt) {

        assert cnt >= 0 : "Negative 'cnt' detected";

        if (cnt == 0) {
            return Collections.emptyList();
        }

        if (cnt == 1) {
            return Collections.singletonList(new BinaryNode());
        }

        int totalCnt = cnt - 1;

        assert totalCnt > 0;

        List<BinaryNode> trees = new ArrayList<>();

        for (int leftCnt = 0; leftCnt <= totalCnt; ++leftCnt) {

            int rightCnt = totalCnt - leftCnt;

            assert rightCnt >= 0;

            List<BinaryNode> leftSubtrees = generateTreesRec(leftCnt);
            List<BinaryNode> rightSubtrees = generateTreesRec(rightCnt);

            trees.addAll(combineSubtrees(leftSubtrees, rightSubtrees));
        }

        return trees;
    }

    private static List<BinaryNode> combineSubtrees(List<BinaryNode> left, List<BinaryNode> right) {

        assert left != null && right != null;

        List<BinaryNode> trees = new ArrayList<>();

        if (left.isEmpty()) {
            for (BinaryNode rightTree : right) {
                trees.add(new BinaryNode(null, rightTree));
            }
        }
        else if (right.isEmpty()) {
            for (BinaryNode leftTree : left) {
                trees.add(new BinaryNode(leftTree, null));
            }
        }
        else {
            for (BinaryNode leftTree : left) {
                for (BinaryNode rightTree : right) {
                    trees.add(new BinaryNode(leftTree, rightTree));
                }
            }
        }

        return trees;
    }

    private GenerateAllBinaryTrees() {

        int n = 5;

        List<BinaryNode> allTrees = generateAllBinaryTrees(n);

        LOG.info("allTrees count: " + allTrees.size());

        LOG.info("GenerateAllBinaryTrees done...");
    }


    public static void main(String[] args) {
        try {
            new GenerateAllBinaryTrees();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}
