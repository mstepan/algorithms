package com.max.algs.ds.trie;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;


public class ArrayTrieTest {


    @Test
    public void addTextFromWiki() {

        String text = "Alternatively, a tree can be defined abstractly as a whole (globally) as an ordered tree, with a value" +
                " assigned to each node. Both these perspectives are useful: while a tree can be analyzed mathematically as a" +
                " whole, when actually represented as a data structure it is usually represented and worked with separately " +
                "by node (rather than as a list of nodes and an adjacency list of edges between nodes, as one may represent a" +
                " digraph, for instance). For example, looking at a tree as a whole, one can talk about \"the parent node\" " +
                "of a given node, but in general as a data structure a given node only contains the list of its children, but" +
                " does not contain a reference to its parent (if any)";

        Set<String> allWords = new HashSet<>();

        ArrayTrie trie = ArrayTrie.asciiTrie();

        for (String word : text.split("\\s+")) {
            word = word.replace(",", "");

            allWords.add(word);
            trie.add(word);

//            boolean wasAdded = allWords.add(word);
//            assertEquals("'" + word + "' wasn't added properly", wasAdded, trie.add(word));

        }

//        assertEquals(allWords.size(), trie.size());
//
//        assertTrue(trie.contains("Alternatively"));
//        assertTrue(trie.contains("parent"));
//        assertTrue(trie.contains("its"));
//
//        assertFalse(trie.contains("machine"));
    }

    @Test
    public void addWithPrefix3() {

        ArrayTrie trie = ArrayTrie.alphabetLowerCaseTrie();

        trie.add("nodes");
        trie.add("node");

        assertTrue(trie.contains("nodes"));
        assertTrue(trie.contains("node"));
    }

    @Test
    public void addWithPrefix2() {

        ArrayTrie trie = ArrayTrie.alphabetLowerCaseTrie();

        trie.add("she");
        trie.add("shell");

        assertTrue(trie.contains("shell"));
        assertTrue(trie.contains("she"));
    }

    @Test
    public void addWithPrefix() {

        ArrayTrie trie = ArrayTrie.alphabetLowerCaseTrie();

        trie.add("shell");
        trie.add("she");

        assertTrue(trie.contains("shell"));
        assertTrue(trie.contains("she"));
    }

    @Test
    public void addContains() {

        ArrayTrie trie = ArrayTrie.alphabetLowerCaseTrie();

        assertTrue(trie.isEmpty());
        assertEquals(0, trie.size());

        assertTrue(trie.add("she"));
        assertFalse(trie.isEmpty());
        assertEquals(1, trie.size());
        assertTrue(trie.contains("she"));

        assertTrue(trie.add("sells"));
        assertFalse(trie.isEmpty());
        assertEquals(2, trie.size());
        assertTrue(trie.contains("she"));
        assertTrue(trie.contains("sells"));

        trie.add("sea");
        trie.add("shells");
        trie.add("by");
        trie.add("the");
        trie.add("shore");

        assertTrue(trie.contains("she"));
        assertTrue(trie.contains("sells"));
        assertTrue(trie.contains("sea"));
        assertTrue(trie.contains("shells"));
        assertTrue(trie.contains("by"));
        assertTrue(trie.contains("the"));
        assertTrue(trie.contains("shore"));
    }
}
