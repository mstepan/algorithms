package com.max.algs.ds.external;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Interna/external node representation.
 */
final class BTreeNode {

    private static final int MIN_CAPACITY = 2;
    private static final int MAX_CAPACITY = 1000;

    private final String[] nodes;
    private final BTreeNode[] links;

    BTreeNode(int capacity) {
        checkArgument(capacity >= MIN_CAPACITY && capacity <= MAX_CAPACITY, "capacity '%s': should be in range [%s, %s]",
                capacity, MIN_CAPACITY, MAX_CAPACITY);
        checkArgument(isEven(capacity), "capacity '%s': not an even value", capacity);

        this.nodes = new String[capacity];
        this.links = new BTreeNode[capacity];
    }


    private static boolean isEven(int value) {
        return (value & 1) == 0;
    }

}
