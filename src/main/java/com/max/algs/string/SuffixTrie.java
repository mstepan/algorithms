package com.max.algs.string;

import static com.google.common.base.Preconditions.checkNotNull;

final class SuffixTrie {

    private final Node root = new Node();

    /**
     * Add the whole word with all suffixes to a trie.
     */
    void add(String value) {
        checkNotNull(value, "Can't store 'null' value");

        for (int i = 1; i < value.length(); ++i) {
            addWordOrSuffix(value, i);
        }

        addWordOrSuffix(value, 0);
    }

    private void addWordOrSuffix(String value, int from) {

        assert from >= 0 && from < value.length();

        Node cur = root;

        for (int i = from; i < value.length() - 1; ++i) {
            char ch = value.charAt(i);

            Node nextNode = cur.next(ch);

            if (nextNode != null) {
                cur = nextNode;
            }
            else {
                Node newNode = new Node();
                cur.addNext(ch, newNode);
                cur = newNode;
            }
        }

        char lastCh = value.charAt(value.length() - 1);
        cur.markEnd(lastCh, from == 0 ? EndMarker.WORD : EndMarker.SUFFIX);
    }

    /**
     * Check if current trie contains the whole word.
     */
    boolean contains(String searchValue) {
        checkNotNull(searchValue, "Can't query 'null' value");
        return search(searchValue, EndMarker.WORD);
    }

    /**
     * Check if current trie contains just a suffix.
     */
    boolean hasSuffix(String suffixToSearch) {
        checkNotNull(suffixToSearch, "Can't query 'null' value");
        return search(suffixToSearch, EndMarker.SUFFIX);
    }

    private boolean search(String searchValue, EndMarker marker) {
        Node level = root;

        for (int i = 0; i < searchValue.length() - 1; ++i) {
            char ch = searchValue.charAt(i);

            if (level == null) {
                return false;
            }

            level = level.next(ch);
        }

        if (level == null) {
            return false;
        }

        final char lastCh = searchValue.charAt(searchValue.length() - 1);
        return level.hasMarker(lastCh, marker);
    }


    private static final class Node {

        private final Edge[] nodes = new Edge['z' - 'a' + 1];

        Node next(char ch) {
            if (nodes[index(ch)] == null) {
                return null;
            }

            return nodes[index(ch)].dest;
        }

        void addNext(char ch, Node nextNode) {
            nodes[index(ch)] = new Edge(nextNode, EndMarker.TRANSITION);
        }

        void markEnd(char lastCh, EndMarker marker) {
            nodes[index(lastCh)] = new Edge(null, marker);
        }

        int index(char ch) {
            return ch - 'a';
        }

        boolean hasMarker(char ch, EndMarker marker) {
            Edge singleNode = nodes[index(ch)];
            return singleNode != null && singleNode.marker == marker;
        }

        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder();
            for (int i = 0; i < nodes.length; ++i) {
                if (nodes[i] != null) {
                    if (buf.length() != 0) {
                        buf.append(", ");
                    }
                    buf.append(i);
                }
            }

            return buf.toString();
        }
    }

    private static final class Edge {
        final Node dest;
        final EndMarker marker;

        Edge(Node dest, EndMarker marker) {
            this.dest = dest;
            this.marker = marker;
        }

        @Override
        public String toString() {
            return "next: " + dest + ", marker: " + marker;
        }
    }

    private enum EndMarker {
        WORD, SUFFIX, TRANSITION
    }
}
