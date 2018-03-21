package com.max.algs.ds.rope;


import static com.google.common.base.Preconditions.checkNotNull;

final class RopeMidNode implements RopeNode {

    private RopeNode left;
    private RopeNode right;
    private int length;

    RopeMidNode(RopeNode left, RopeNode right) {
        this.left = left;
        this.right = right;
        this.length = calculateLength(left);
    }

    @Override
    public RopeNode getRight() {
        return right;
    }

    private int calculateLength(RopeNode leftNode) {
        int combinedLength = 0;

        while (leftNode != null) {
            combinedLength += leftNode.length();
            leftNode = leftNode.getRight();
        }

        return combinedLength;
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public char getChar(int index) {
        if (index < length) {
            checkNotNull(left);
            return left.getChar(index);
        }

        checkNotNull(right);
        return right.getChar(index - length);
    }

    @Override
    public String toString() {
        return "left = " + left.length() + ", right = " + right.length();
    }
}
