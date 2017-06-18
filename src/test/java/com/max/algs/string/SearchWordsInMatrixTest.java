package com.max.algs.string;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class SearchWordsInMatrixTest {


    @Test
    public void findValidWords() {

        final char[][] data = {
                {'a', 'f', 'h', 'u', 'n'},
                {'e', 't', 'a', 'i', 'r'},
                {'a', 'e', 'g', 'g', 'o'},
                {'t', 'r', 'm', 'l', 'p'},
                {'a', 'p', 't', 's', 'k'}
        };

        final String[] dictionary = {
                "after", "hate", "hair", "air", "eat", "tea", "goa", "teri", "arm",
                "airp", "mee", "poi", "egg"};

        SearchWordsInMatrix search = new SearchWordsInMatrix();

        Set<String> allValidWords = new HashSet<>(search.findValidWords(data, dictionary));

        // valid
        assertTrue(allValidWords.contains("after"));
        assertTrue(allValidWords.contains("hate"));
        assertTrue(allValidWords.contains("hair"));
        assertTrue(allValidWords.contains("air"));
        assertTrue(allValidWords.contains("eat"));
        assertTrue(allValidWords.contains("tea"));
        assertTrue(allValidWords.contains("goa"));
        assertTrue(allValidWords.contains("teri"));
        assertTrue(allValidWords.contains("arm"));
        assertTrue(allValidWords.contains("poi"));
        assertTrue(allValidWords.contains("mee"));
        assertEquals(12, allValidWords.size());

        // not valid
        assertFalse(allValidWords.contains("airp"));


    }

}
