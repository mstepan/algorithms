package com.max.algs.ds.hamt;


public class PointerNode implements IDataNode {

    final BitNode ptr;

    public PointerNode(BitNode ptr) {
        this.ptr = ptr;
    }
}
