package com.max.algs.ds.rope;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;


final class RopeLeafNode implements RopeNode {

    private final String data;

    RopeLeafNode(String data) {
        checkNotNull(data);
        this.data = data;
    }

    @Override
    public int length() {
        return data.length();
    }

    @Override
    public char getChar(int index) {
        checkArgument(index >= 0 && index < data.length());
        return data.charAt(index);
    }

    @Override
    public RopeNode getRight() {
        return null;
    }

    @Override
    public String toString() {
        return data + ", length = " + data.length();
    }
}
