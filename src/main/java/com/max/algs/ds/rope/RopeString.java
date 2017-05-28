package com.max.algs.ds.rope;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;


public final class RopeString implements CharSequence {

    private final RopeNode root;
    private final int totalLength;

    public RopeString(String str) {
        checkNotNull(str);
        this.root = new RopeLeafNode(str);
        this.totalLength = str.length();
    }

    private RopeString(RopeMidNode rootNode, int totalLength) {
        this.root = rootNode;
        this.totalLength = totalLength;
    }

    @Override
    public char charAt(int index) {
        checkArgument(index >= 0 && index < totalLength);
        return root.getChar(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        checkArgument(start >= 0 && end <= totalLength && start < end);

        RopeString ropeSubSeq = new RopeString("");


        //TODO:
        return ropeSubSeq;
    }

    @Override
    public int length() {
        return totalLength;
    }

    public RopeString append(String otherStr) {
        checkNotNull(otherStr);

        RopeMidNode combinedRoot = new RopeMidNode(this.root, new RopeLeafNode(otherStr));

        return new RopeString(combinedRoot, this.totalLength + otherStr.length());
    }
}
