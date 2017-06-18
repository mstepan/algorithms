package com.max.algs.epi.string.matching;

import com.max.algs.string.StringUtils;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;


public class BoyerMooreStringMatching {

    private BoyerMooreStringMatching() throws Exception {

        Random rand = new Random();

        for (int i = 0; i < 10_000; ++i) {
            String str = StringUtils.generateDNAString(20 + rand.nextInt(5000));
            String pattern = StringUtils.generateDNAString(3 + rand.nextInt(10));

            int expectedIndex = str.indexOf(pattern);

            int actualIndex = find(str, pattern);

            if (actualIndex != expectedIndex) {
                System.out.printf("actualIndex = %d, expectedIndex = %d %n", actualIndex, expectedIndex);
                throw new IllegalStateException("actualIndex != expectedIndex for string '" + str + "' and pattern '" +
                        pattern + "'");
            }

            int actualIndexExtended = findExtended(str, pattern);

            if (actualIndexExtended != expectedIndex) {
                System.out.printf("actualIndexExtended = %d, expectedIndex = %d %n", actualIndex, expectedIndex);
                throw new IllegalStateException("actualIndexExtended != expectedIndex for string '" + str + "' and pattern '" +
                        pattern + "'");
            }
        }

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * Boyer-Moore substring matching algorithm.
     */
    public static int find(String str, String pat) {
        checkNotNull(str);
        checkNotNull(pat);
        checkArgument(str.length() >= pat.length());

        Map<Character, Integer> badCh = calculateBadCharacter(pat);

        final int patLength = pat.length();
        final int strLength = str.length();

        int i = patLength - 1;
        int j = i;
        int rightI = i;

        char strCh;
        Integer index;

        while (i < strLength && j >= 0) {

            strCh = str.charAt(i);

            if (strCh == pat.charAt(j)) {
                --i;
                --j;
            }
            else {
                index = badCh.get(strCh);

                // no chars to match, fully shift
                if (index == null) {
                    i += patLength;
                    rightI = i;
                }
                // already matched character mismatch, shift by 1
                else if (index > j) {
                    rightI += 1;
                    i = rightI;
                }
                else {
                    i += (patLength - index - 1);
                    rightI = i;
                }

                j = patLength - 1;
            }
        }

        if (j < 0) {
            return i + 1;
        }

        return -1;
    }

    private static Map<Character, Integer> calculateBadCharacter(String pat) {
        Map<Character, Integer> badChOffsets = new HashMap<>();

        for (int i = 0, patLength = pat.length(); i < patLength; ++i) {
            badChOffsets.put(pat.charAt(i), i);
        }

        return badChOffsets;
    }

    /**
     * Boyer-Moore substring matching algorithm.
     */
    public static int findExtended(String str, String pat) {
        checkNotNull(str);
        checkNotNull(pat);
        checkArgument(str.length() >= pat.length());

        Map<Character, List<Integer>> badCh = calculateBadCharacterExtended(pat);

        final int patLength = pat.length();
        final int strLength = str.length();

        int i = patLength - 1;
        int j = i;

        char strCh;
        int index;

        while (i < strLength && j >= 0) {

            strCh = str.charAt(i);

            if (strCh == pat.charAt(j)) {
                --i;
                --j;
            }
            else {
                index = getCharIndexToLeft(badCh, strCh, j);

                // no chars to match, fully shift
                if (index == -1) {
                    i += patLength;
                }
                else {
                    i += (patLength - index - 1);
                }

                j = patLength - 1;
            }
        }

        if (j < 0) {
            return i + 1;
        }

        return -1;
    }

    private static int getCharIndexToLeft(Map<Character, List<Integer>> badCh, char ch, int baseIndex) {

        if (!badCh.containsKey(ch)) {
            return -1;
        }

        for (int offset : badCh.get(ch)) {
            if (offset < baseIndex) {
                return offset;
            }
        }

        return -1;
    }

    private static Map<Character, List<Integer>> calculateBadCharacterExtended(String pat) {

        Map<Character, List<Integer>> badChOffsets = new HashMap<>();

        for (int i = pat.length() - 1; i >= 0; --i) {
            badChOffsets.computeIfAbsent(pat.charAt(i), ArrayList::new).add(i);
        }

        return badChOffsets;
    }

    public static void main(String[] args) {
        try {
            new BoyerMooreStringMatching();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
