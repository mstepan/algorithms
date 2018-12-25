package com.max.algs.string;

import org.junit.Test;

import static org.jgroups.util.Util.assertTrue;
import static org.junit.Assert.assertFalse;

public class SuffixTrieTest {

    @Test
    public void addAndContainsWholeWord() {

        SuffixTrie trie = new SuffixTrie();

        assertFalse(trie.contains("cat"));
        assertFalse(trie.contains("rat"));
        assertFalse(trie.contains("bat"));
        assertFalse(trie.contains("mat"));

        trie.add("cat");
        assertTrue(trie.contains("cat"));
        assertFalse(trie.contains("rat"));
        assertFalse(trie.contains("bat"));
        assertFalse(trie.contains("mat"));

        trie.add("rat");
        trie.add("bat");
        assertTrue(trie.contains("cat"));
        assertTrue(trie.contains("rat"));
        assertTrue(trie.contains("bat"));
        assertFalse(trie.contains("mat"));
        assertFalse(trie.contains("catapult"));

        assertFalse(trie.contains("m"));
        assertFalse(trie.contains("ma"));
        assertFalse(trie.contains("ba"));
        assertFalse(trie.contains("ca"));
    }

    @Test
    public void addAndContainsSuffix() {
        SuffixTrie trie = new SuffixTrie();

        trie.add("cat");
        trie.add("rat");
        trie.add("bat");

        assertTrue(trie.hasSuffix("t"));
        assertTrue(trie.hasSuffix("at"));

        assertFalse(trie.hasSuffix("cat"));
//        assertFalse(trie.hasSuffix(""));
        assertFalse(trie.hasSuffix("p"));
        assertFalse(trie.hasSuffix("patterson"));

        assertFalse(trie.contains("c"));
        assertFalse(trie.contains("ca"));
        assertFalse(trie.contains("ra"));
        assertFalse(trie.contains("ba"));
    }
}
