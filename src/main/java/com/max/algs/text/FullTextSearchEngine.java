package com.max.algs.text;

import java.util.*;


/**
 * Given a string, you need to find super string by word match. i.e. all words in the input string has to occure in any order in output string.
 * e.g. given data set:
 * "string search"
 * "java string search"
 * "manual c++ string search equals"
 * "java search code"
 * "c++ java code search"
 * ...
 * <p>
 * input: "java search"
 * output:
 * 1) "java string search"
 * 2) "java search code"
 * 3) "c++ java code search"
 * <p>
 * input: "c++ search"
 * output:
 * 1) "manual c++ string search equals"
 * 2) "c++ java code search"
 * <p>
 * There are millions of records in given data set and you need to process few million as input.
 *
 * @author mstepanenko
 */

public class FullTextSearchEngine {


    private final List<String> allSentences = new ArrayList<>();

    private final Map<String, Set<Integer>> wordsIndex = new HashMap<>();

    private int sentenceIndex = 0;


    public void add(String str) {

        String[] words = splitWords(str);

        for (String word : words) {
            Set<Integer> index = wordsIndex.get(word);

            if (index == null) {
                index = new HashSet<>();
                wordsIndex.put(word, index);
            }

            index.add(sentenceIndex);
        }

        allSentences.add(str);
        ++sentenceIndex;
    }


    public List<String> search(String str) {

        String[] words = splitWords(str);

        Set<Integer> fullIndex = new HashSet<>();

        for (String word : words) {

            Set<Integer> curIndex = wordsIndex.get(word);

            // word index is EMPTY
            if (curIndex.isEmpty()) {
                return new ArrayList<String>();
            }

            if (fullIndex.isEmpty()) {
                fullIndex.addAll(curIndex);
            }
            else {
                fullIndex = intersect(fullIndex, curIndex);
            }
        }

        List<String> res = new ArrayList<>(fullIndex.size());

        for (Integer docIndex : fullIndex) {
            res.add(allSentences.get(docIndex));
        }

        return res;
    }


    private Set<Integer> intersect(Set<Integer> set1, Set<Integer> set2) {

        Set<Integer> res = new HashSet<>();

        for (Integer entry : set1) {
            if (set2.contains(entry)) {
                res.add(entry);
            }
        }

        return res;

    }

    private String[] splitWords(String str) {
        return str.toLowerCase().split("\\s");
    }


}
