package com.max.algs.epi.hashing;


import org.apache.log4j.Logger;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 13.7. Find the smallest subarray covering all values.
 */
public final class SmallestSubarrayCoveringAllValues1 {

    private static final Logger LOG = Logger.getLogger(SmallestSubarrayCoveringAllValues1.class);

    private SmallestSubarrayCoveringAllValues1() throws Exception {

        String textToSearch = "hello the wonderful and beautiful world this world is hello like";

        String[] searchKeywords = {"hello", "world"};

        Subarray res = findShortestDigest(textToSearch, searchKeywords);

        System.out.println(res);

        System.out.printf("SmallestSubarrayCoveringAllValues: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * N - arr.length
     * M - search.size()
     * <p>
     * time: O(N)
     * space: O(M)
     */
    public static Subarray findShortestDigest(String text, String[] searchArr) {
        checkNotNull(text);
        checkNotNull(searchArr);

        String[] arr = text.split(" ");

        if (searchArr.length == 0) {
            return Subarray.EMPTY;
        }

        Set<String> search = new HashSet<>(Arrays.asList(searchArr));

        Subarray minSoFar = Subarray.MAX_VALUE;
        int from = 0;
        int to = 0;

        Map<String, Integer> window = new HashMap<>(2 * search.size());

        while (true) {

            assert from <= to : "from > to";

            if (window.size() == search.size()) {

                int curLength = to - from - 1;

                if (curLength < minSoFar.length()) {
                    minSoFar = new Subarray(from, to - 1);
                }

                assert from < arr.length : "from > arr.length";
                removeFromWindow(normalizeWord(arr[from]), window, search);
                ++from;
            }
            else {
                if (to == arr.length) {
                    break;
                }

                assert to < arr.length : "to > arr.length";
                addToWindow(normalizeWord(arr[to]), window, search);
                ++to;
            }

            assert from <= to : "from > to";
        }

        assert window.size() != search.size() : "window.size() == search.size()";
        assert search.size() == searchArr.length;

        return minSoFar == Subarray.MAX_VALUE ? Subarray.EMPTY : minSoFar;
    }

    private static String normalizeWord(String word) {
        checkNotNull(word);
        return word.toLowerCase();
    }

    private static void addToWindow(String word, Map<String, Integer> window, Set<String> search) {
        if (search.contains(word)) {
            window.compute(word, (key, value) -> value == null ? 1 : value + 1);
        }
    }

    private static void removeFromWindow(String word, Map<String, Integer> window, Set<String> search) {
        if (search.contains(word)) {
            int freq = window.get(word);

            if (freq == 1) {
                window.remove(word);
            }
            else {
                window.put(word, freq - 1);
            }
        }
    }

    public static void main(String[] args) {
        try {
            new SmallestSubarrayCoveringAllValues1();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    public static final class Subarray {

        static final Subarray EMPTY = new Subarray(-1, -1);
        static final Subarray MAX_VALUE = new Subarray(0, Integer.MAX_VALUE);

        final int from;
        final int to;

        Subarray(int from, int to) {
            checkArgument(from <= to);
            this.from = from;
            this.to = to;
        }

        int length() {
            return to - from;
        }

        @Override
        public String toString() {
            return String.format("[%d, %d]", from, to);
        }
    }
}

