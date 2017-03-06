package com.max.algs.leetcode;

/**
 * Implement wildcard pattern matching with support for '?' and '*'.
 * <p/>
 * '?' Matches any single character.
 * '*' Matches any sequence of characters (including the empty sequence).
 * <p/>
 * The matching should cover the entire input string (not partial).
 * <p/>
 * The function prototype should be:
 * bool isMatch(const char *s, const char *p)
 * <p/>
 * Some examples:
 * isMatch("aa","a") → false
 * isMatch("aa","aa") → true
 * isMatch("aaa","aa") → false
 * isMatch("aa", "*") → true
 * isMatch("aa", "a*") → true
 * isMatch("ab", "?*") → true
 * isMatch("aab", "c*a*b") → false
 * <p/>
 * <p/>
 * See: https://leetcode.com/problems/wildcard-matching/
 */
public class WildcardMatching {

    public boolean isMatch(String base, String patern) {

        int starsCount = 0;

        for (int i = -1, pLength = patern.length(); ++i < pLength; ) {
            if (patern.charAt(i) == '*') {
                ++starsCount;
            }
        }

        // optimisation, number of characters (excluding '*') in 'pattern'
        // is greater then in 'base' string
        if (patern.length() - starsCount > base.length()) {
            return false;
        }

        final int cols = patern.length() + 1;

        boolean[] lastRow = new boolean[cols];
        lastRow[0] = true;

        for (int i = 0; ++i < cols; ) {
            if (patern.charAt(i - 1) == '*') {
                lastRow[i] = lastRow[i - 1];
            }
        }

        boolean lastCell;
        boolean[] nextRow;
        char ch;
        char pCh;

        for (int row = -1, sLength = base.length(); ++row < sLength; ) {

            nextRow = new boolean[lastRow.length];
            lastCell = false;

            ch = base.charAt(row);

            for (int col = 0; ++col < cols; ) {

                pCh = patern.charAt(col - 1);

                if (pCh == '*') {
                    nextRow[col] = lastCell || lastRow[col - 1] || lastRow[col];
                }
                else if (ch == pCh || pCh == '?') {
                    nextRow[col] = lastRow[col - 1];
                }

                lastCell = nextRow[col];
            }

            lastRow = nextRow;
        }

        return lastRow[cols - 1];
    }


    private WildcardMatching() throws Exception {

        long startTime = System.nanoTime();

        String[][] pairs = new String[][]{
                {"aa", "a"},
                {"aa", "aa"},
                {"aaa", "aa"},
                {"aa", "*"},
                {"aa", "a*"},
                {"ab", "?*"},
                {"aab", "c*a*b"},
                {"babaaababaabababbbbbbaabaabbabababbaababbaaabbbaaab", "***bba**a*bbba**aab**b"}
        };

        for (String[] pair : pairs) {
            System.out.println(String.format("isMatching(%s, %s) => %s", pair[0], pair[1], isMatch(pair[0], pair[1])));
        }

        long endTime = System.nanoTime();

        System.out.println("time: " + ((endTime - startTime) / 1_000_000) + " ms");

        System.out.println("WildcardMatching done...");
    }

    public static void main(String[] args) {
        try {
            new WildcardMatching();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
