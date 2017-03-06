package com.max.algs.ds.external;

/**
 * External memory B-tree.
 */
public class BTree {

    private static final int DEFAULT_BRANCH_FACTOR = 6;

    private BTreeNode root;

    public BTree(){
        root = new BTreeNode(DEFAULT_BRANCH_FACTOR);
    }


    public void add(String key){

    }

    public boolean contains(String key){
        return false;
    }

}
