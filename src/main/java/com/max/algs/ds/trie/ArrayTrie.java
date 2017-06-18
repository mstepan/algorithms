package com.max.algs.ds.trie;


import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Trie implementation based on array representation of a node.
 */
public class ArrayTrie implements Set<String> {

    private static final char A_LOWER = 'a';
    private static final char Z_LOWER = 'z';

    private static final char ZERO = '0';
    private static final char NINE = '9';
    private final int alphabetSize;
    private final int offset;
    private final ArrayTrieNode root;
    private int size;

    public ArrayTrie(int alphabetSize, int offset) {
        this.alphabetSize = alphabetSize;
        this.offset = offset;
        this.root = new ArrayTrieNode(alphabetSize, offset);
    }

    public static ArrayTrie asciiTrie() {
        return new ArrayTrie(256, 0);
    }

    public static ArrayTrie alphabetLowerCaseTrie() {
        return new ArrayTrie(Z_LOWER - A_LOWER + 1, A_LOWER);
    }

    public static ArrayTrie digitsTrie() {
        return new ArrayTrie(NINE - ZERO + 1, ZERO);
    }

    private static void checkStringNotNull(String value) {
        checkNotNull(value, "null 'value' can't be added to trie");
    }

    @Override
    public boolean add(String value) {

        checkStringNotNull(value);

        ArrayTrieNode cur = root;

        for (int i = 0; i < value.length(); i++) {

            ArrayTrieNode nextNode = cur.next[value.charAt(i) - offset];

            if (nextNode == null) {

                // null link found
                for (int j = i; j < value.length(); j++) {
                    ArrayTrieNode newNode = new ArrayTrieNode(alphabetSize, offset);
                    cur.next[value.charAt(j) - offset] = newNode;
                    cur = newNode;
                }

                cur.leaf = true;
                ++size;

                return true;
            }

            cur = nextNode;
        }

        // string is a prefix of another string, like "she" prefix of "shell"
        cur.leaf = true;

        return false;
    }

    @Override
    public boolean addAll(Collection<? extends String> col) {
        boolean res = true;
        for (String value : col) {
            res &= add(value);
        }

        return res;
    }

    @Override
    public boolean contains(Object obj) {

        if (!(obj instanceof String)) {
            return false;
        }

        String value = (String) obj;

        if (isEmpty()) {
            return false;
        }

        ArrayTrieNode cur = root;

        for (int i = 0; i < value.length() && cur != null; i++) {
            char ch = value.charAt(i);
            cur = cur.next[ch - offset];
        }

        return cur != null && cur.leaf;
    }

    @Override
    public boolean containsAll(Collection<?> col) {
        boolean res = true;
        for (Object value : col) {
            res &= contains(value);
        }

        return res;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("'remove' is not supported by this implementation of trie");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("'removeAll' is not supported by this implementation of trie");
    }

    @Override
    public void clear() {
        ArrayTrieNode[] nextNodes = root.next;
        for (int i = 0, nextNodesLength = nextNodes.length; i < nextNodesLength; i++) {
            nextNodes[i] = null;
        }
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<String> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    private static class ArrayTrieNode {

        private final ArrayTrieNode[] next;
        private final int offset;
        private boolean leaf;

        public ArrayTrieNode(int alphabetSize, int offset) {
            this.next = new ArrayTrieNode[alphabetSize];
            this.offset = offset;
        }

        @Override
        public String toString() {

            StringBuilder buf = new StringBuilder();

            for (int i = 0; i < next.length; i++) {
                if (next[i] != null) {
                    buf.append((char) (i + offset));
                }
            }

            if (leaf) {
                buf.append(", leaf");
            }

            return buf.toString();
        }
    }
}
