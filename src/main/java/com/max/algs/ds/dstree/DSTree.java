package com.max.algs.ds.dstree;


public class DSTree<K extends Digitazable, V> {


    private int size;
    private Entry<K, V> root;


    public V put(K key, V value) {

        if (root == null) {
            root = new Entry<K, V>(key, value, null);
        }
        else {

            int numOfDigits = key.numOfDigits();

            Entry<K, V> cur = root;
            int index = 0;

            Entry<K, V> parent = null;
            int prevDigit = 0;

            while (cur != null && index < numOfDigits) {

                if (cur.key.equals(key)) {
                    V prevValue = cur.value;
                    cur.value = value;
                    return prevValue;
                }

                int binaryDigit = key.binaryDigit(index);
                ++index;

                parent = cur;
                prevDigit = binaryDigit;

                // go left
                if (binaryDigit == 0) {
                    cur = cur.left;
                }
                // go right
                else {
                    cur = cur.right;
                }
            }

            if (prevDigit == 0) {
                parent.left = new Entry<K, V>(key, value, parent);
            }
            else {
                parent.right = new Entry<K, V>(key, value, parent);
            }
        }

        ++size;
        return null;
    }

    public V get(K key) {

        int index = 0;
        int numOfDigits = key.numOfDigits();

        Entry<K, V> cur = root;

        while (cur != null && index < numOfDigits) {

            if (cur.key.equals(key)) {
                return cur.value;
            }

            int binaryDigit = key.binaryDigit(index);
            ++index;

            // go left
            if (binaryDigit == 0) {
                cur = cur.left;
            }
            // go right
            else {
                cur = cur.right;
            }
        }

        return null;
    }


    /**
     * @return the previous value associated with <tt>key</tt>, or
     * <tt>null</tt> if there was no mapping for <tt>key</tt>
     */
    public V remove(K key) {

        if (root == null) {
            return null;
        }

        Entry<K, V> entry = find(key);

        if (entry == null) {
            return null;
        }

        V value = entry.value;

        Entry<K, V> parent = entry.parent;


        // leaf node
        if (entry.left == null && entry.right == null) {
            if (parent.left == entry) {
                parent.left = null;
            }
            else {
                parent.right = null;
            }

            entry.parent = null;
        }
        else {
            Entry<K, V> cur = (entry.left != null ? firstLeaf(entry.left) : firstLeaf(entry.right));

            if (cur.parent.left == cur) {
                cur.parent.left = null;
            }
            else {
                cur.parent.right = null;
            }


            cur.parent = null;
            entry.key = cur.key;
            entry.value = cur.value;

        }


        --size;
        return value;
    }

    public int size() {
        return size;
    }

    private Entry<K, V> firstLeaf(Entry<K, V> base) {

        Entry<K, V> cur = base;

        while (!cur.isLeaf()) {
            if (cur.left != null) {
                cur = cur.left;
            }
            else {
                cur = cur.right;
            }
        }

        return cur;
    }

    public boolean isEmpty() {
        return size == 0;
    }


    private Entry<K, V> find(K key) {

        if (root == null) {
            return null;
        }

        int numOfDigits = key.numOfDigits();

        Entry<K, V> cur = root;

        for (int index = 0; index < numOfDigits && cur != null; index++) {

            if (cur.key.equals(key)) {
                return cur;
            }

            int binaryDigit = key.binaryDigit(index);
            // go left
            if (binaryDigit == 0) {
                cur = cur.left;
            }
            // go right
            else {
                cur = cur.right;
            }
        }

        return null;
    }

    private static final class Entry<K extends Digitazable, V> {


        K key;
        V value;
        Entry<K, V> left;
        Entry<K, V> right;
        Entry<K, V> parent;
        Entry(K key, V value, Entry<K, V> parent) {
            super();
            this.key = key;
            this.value = value;
            this.parent = parent;
        }

        boolean isLeaf() {
            return left == null && right == null;
        }

        @Override
        public String toString() {
            return String.valueOf(key) + " = " + String.valueOf(value);
        }

    }

}
