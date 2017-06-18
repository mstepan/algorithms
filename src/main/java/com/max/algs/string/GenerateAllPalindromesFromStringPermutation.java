package com.max.algs.string;

import java.util.*;

/**
 * Generate all palindromes from string permutation.
 */
public class GenerateAllPalindromesFromStringPermutation {

    private GenerateAllPalindromesFromStringPermutation() throws Exception {

        String str = "aabbccc";

        Set<String> allPermutations = generateAllPalindromicPermutations(str);

        for (String perm : allPermutations) {
            System.out.println(perm);
        }

        System.out.println(allPermutations.size());

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    private static void generateHalfsOfPermutation(Map<Character, Integer> freq, Set<String> permutations,
                                                   Deque<Character> partialSol,
                                                   int totalCount, int elemsUsed) {

        if (elemsUsed == totalCount) {
            permutations.add(constructString(partialSol));
            return;
        }

        for (Map.Entry<Character, Integer> entry : freq.entrySet()) {

            char ch = entry.getKey();
            int count = entry.getValue();

            if (count != 0) {
                entry.setValue(count - 1);
                partialSol.add(ch);
                generateHalfsOfPermutation(freq, permutations, partialSol, totalCount, elemsUsed + 1);
                partialSol.pollLast();
                entry.setValue(count);
            }
        }
    }

    private static String constructString(Deque<Character> res) {
        StringBuilder buf = new StringBuilder(res.size());

        for (char ch : res) {
            buf.append(ch);
        }

        return buf.toString();
    }

    private static int countChars(Map<Character, Integer> freq) {
        int count = 0;

        for (int chFreq : freq.values()) {
            count += chFreq;
        }

        return count;
    }

    private static Map<Character, Integer> countCharsFreq(String str) {
        Map<Character, Integer> freq = new HashMap<>();

        for (int i = 0, length = str.length(); i < length; ++i) {
            char ch = str.charAt(i);
            freq.compute(ch, (chKey, value) -> value == null ? 1 : value + 1);
        }

        return freq;
    }

    private static int countOddValues(Map<Character, Integer> map) {
        int oddValuesCount = 0;
        for (Integer value : map.values()) {

            // odd value
            if ((value & 1) == 1) {
                ++oddValuesCount;
            }
        }
        return oddValuesCount;
    }

    private static <T> Map<T, Integer> divideEachValueRemovingZeros(Map<T, Integer> baseMap, int divider) {

        Map<T, Integer> dividedMap = new HashMap<>();

        for (Map.Entry<T, Integer> entry : baseMap.entrySet()) {
            int value = entry.getValue() / divider;

            if (value != 0) {
                dividedMap.put(entry.getKey(), value);
            }
        }

        return dividedMap;
    }

    private static <T> T getFirstKeyWithOddValue(Map<T, Integer> map) {

        for (Map.Entry<T, Integer> entry : map.entrySet()) {
            if ((entry.getValue() & 1) == 1) {
                return entry.getKey();
            }
        }

        throw new IllegalStateException("Can't find and odd value in a Map");
    }

    private static Set<String> generateAllPalindromicPermutations(String baseStr) {

        Map<Character, Integer> freq = countCharsFreq(baseStr);

        int oddsValuesCount = countOddValues(freq);

        if (oddsValuesCount > 1) {
            return Collections.emptySet();
        }

        Map<Character, Integer> halfFreq = divideEachValueRemovingZeros(freq, 2);

        int totalCharsCount = countChars(halfFreq);

        Set<String> halfPermutations = new HashSet<>();
        generateHalfsOfPermutation(halfFreq, halfPermutations, new ArrayDeque<>(), totalCharsCount, 0);

        String delimiter = "";

        if (oddsValuesCount == 1) {
            delimiter = String.valueOf(getFirstKeyWithOddValue(freq));
        }

        Set<String> palindromes = new HashSet<>();

        for (String halfStr : halfPermutations) {
            palindromes.add(halfStr + delimiter + revert(halfStr));
        }

        return palindromes;
    }

    private static String revert(String str) {
        StringBuilder buf = new StringBuilder(str.length());

        for (int i = str.length() - 1; i >= 0; --i) {
            buf.append(str.charAt(i));
        }
        return buf.toString();
    }

    public static void main(String[] args) {
        try {
            new GenerateAllPalindromesFromStringPermutation();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
