package com.max.algs.ds.hamt;


import static com.google.common.base.Preconditions.checkArgument;

/**
 * Hash array mapped trie.
 * <p/>
 * See: https://idea.popcount.org/2012-07-25-introduction-to-hamt
 */
public class ArrayMappedTrie {

    private static final int BITS_CHUNK_SIZE = 5;

    private BitNode root;

    private int size;

    public boolean add(String key) {
        checkNotNull(key);

        String hashedKey = EncodeUtils.hashKey(key);

        if (root == null) {
            root = new BitNode();
            int keyPart = Integer.valueOf(hashedKey.substring(0, BITS_CHUNK_SIZE), 2);
            root.mask.set(keyPart);
            root.nodes.add(new ValueNode(key));
            ++size;
            return true;
        }

        return false;

    }

    public boolean contains(String key) {
        checkNotNull(key);


        if (root == null) {
            return false;
        }

        String hashedKey = EncodeUtils.hashKey(key);


        return false;
    }

    public int size() {
        return size;
    }

    private void checkNotNull(String value) {
        checkArgument(value != null, "NULL 'value' parameter passed");
    }

}
