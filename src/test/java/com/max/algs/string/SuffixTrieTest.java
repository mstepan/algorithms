package com.max.algs.string;

import org.junit.Test;

import static org.jgroups.util.Util.assertTrue;
import static org.junit.Assert.assertFalse;

public class SuffixTrieTest {

    @Test
    public void addAndContains() {

        SuffixTrie trie = new SuffixTrie();

        assertFalse(trie.contains("cat"));
        assertFalse(trie.contains("rat"));
        assertFalse(trie.contains("bat"));
        assertFalse(trie.contains("mat"));

        trie.add("cat");
        assertTrue("Can't find 'cat' in trie", trie.contains("cat"));
        assertFalse("Trie contains 'rat'", trie.contains("rat"));
        assertFalse("Trie contains 'bat'", trie.contains("bat"));
        assertFalse("Trie contains 'mat'", trie.contains("mat"));

        trie.add("rat");
        trie.add("bat");
        assertTrue("Can't find 'cat' in trie", trie.contains("cat"));
        assertTrue("Can't find 'rat' in trie'", trie.contains("rat"));
        assertTrue("Can't find 'bat' in trie'", trie.contains("bat"));
        assertFalse("Trie contains 'mat'", trie.contains("mat"));
    }
}
