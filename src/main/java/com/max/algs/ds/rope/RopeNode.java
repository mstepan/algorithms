package com.max.algs.ds.rope;


interface RopeNode {

    int length();

    char getChar(int index);

    RopeNode getRight();
}
