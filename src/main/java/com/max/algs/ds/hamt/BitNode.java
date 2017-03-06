package com.max.algs.ds.hamt;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;


final class BitNode {

    private static final int BIT_MASK_SIZE = 32;

    final BitSet mask = new BitSet(BIT_MASK_SIZE);
    final List<IDataNode> nodes = new ArrayList<>();


}
