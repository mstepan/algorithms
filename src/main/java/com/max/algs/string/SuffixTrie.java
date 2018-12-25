package com.max.algs.string;

import static com.google.common.base.Preconditions.checkNotNull;

public final class SuffixTrie {


    private final TrieLevel root = new TrieLevel();


    public void add(String value) {
        checkNotNull(value, "Can't store 'null' value");

        TrieLevel level = root;

        for (int i = 0; i < value.length() - 1; ++i) {
            char ch = value.charAt(i);

            TrieLevel nextLevel = level.next(ch);

            if (nextLevel != null) {
                level = nextLevel;
            }
            else {
                TrieLevel newLevel = new TrieLevel();
                level.setNextLevel(ch, newLevel);
                level = newLevel;
            }
        }

        char lastCh = value.charAt(value.length() - 1);

        level.markWordEnd(lastCh);

    }

    public boolean contains(String searchValue) {
        checkNotNull(searchValue, "Can't query 'null' value");

        TrieLevel level = root;

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
        return level.hasEndOfWord(lastCh);
    }

    private static final class TrieLevel {

        private final TrieNode[] nodes = new TrieNode['z' - 'a' + 1];

        TrieLevel next(char ch) {
            if (nodes[index(ch)] == null) {
                return null;
            }

            return nodes[index(ch)].nextLevel;
        }

        void setNextLevel(char ch, TrieLevel nextLevel) {
            nodes[index(ch)] = new TrieNode(nextLevel, false, false);
        }

        void markWordEnd(char lastCh) {
            nodes[index(lastCh)] = new TrieNode(null, true, false);
        }

        int index(char ch) {
            return ch - 'a';
        }

        boolean hasEndOfWord(char ch) {
            TrieNode singleNode = nodes[index(ch)];
            return singleNode != null && singleNode.word;
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

    private static final class TrieNode {

        final TrieLevel nextLevel;
        final boolean word;
        final boolean suffix;

        TrieNode(TrieLevel nextLevel, boolean word, boolean suffix) {
            this.nextLevel = nextLevel;
            this.word = word;
            this.suffix = suffix;
        }

        @Override
        public String toString() {
            return "next: " + nextLevel + ", word: " + word + ", suffix: " + suffix;
        }
    }
}
