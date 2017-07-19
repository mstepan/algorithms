package com.max.algs.dynamic;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public final class StringInterleaving {

    private static final Logger LOG = Logger.getLogger(StringInterleaving.class);


    /**
     * String Interleaving.
     * <p>
     * String 'res' is said to be interleaving of string 's1' and 's2', if
     * charcaters from both strings appear in teh same relative order in 'res'.
     * <p>
     * N - res.length(), aka s1.length() + s2.length()
     * <p>
     * time: O(N), polynomial time.
     * space: O(N), can be reduced to O( 2 * min(s1.length(), s2.length()) )
     */
    public static boolean isInterleavingDynamic(String s1, String s2, String res) {
        checkNotNull(s1);
        checkNotNull(s2);
        checkNotNull(res);

        if (s1.length() + s2.length() != res.length()) {
            return false;
        }

        if (noOverlappingInChars(s1, s2)) {
            return isInterleavingNoCharsOverlapping(s1, s2, res);
        }

        int rows = s1.length() + 1;
        int cols = s2.length() + 1;

        boolean[][] sol = new boolean[rows][cols];
        sol[0][0] = true;

        // fill 1-st row
        for (int col = 1; col < cols; ++col) {
            if (s2.charAt(col - 1) != res.charAt(col - 1)) {
                break;
            }

            sol[0][col] = true;
        }

        // fill left column
        for (int row = 1; row < rows; ++row) {
            if (s1.charAt(row - 1) != res.charAt(row - 1)) {
                break;
            }

            sol[row][0] = true;
        }

        for (int row = 1; row < rows; ++row) {
            for (int col = 1; col < cols; ++col) {

                // s1 pos
                char s1Ch = s1.charAt(row - 1);

                // s2 pos
                char s2Ch = s2.charAt(col - 1);

                // res pos
                char resCh = res.charAt(row + col - 1);

                if (s1Ch == resCh) {
                    sol[row][col] = sol[row - 1][col];
                }

                if (s2Ch == resCh) {
                    sol[row][col] = sol[row][col - 1];
                }
            }
        }

        return sol[rows - 1][cols - 1];
    }

    /**
     * 's1' and 's2' do not have same characters, so interleaving can be further optimized.
     * <p>
     * N - s1.length() + s2.length()
     * <p>
     * time: O(N)
     * space: O(1)
     */
    private static boolean isInterleavingNoCharsOverlapping(String s1, String s2, String res) {
        return hasSameRelativeCharsOrder(s1, res) && hasSameRelativeCharsOrder(s2, res);
    }

    private static boolean hasSameRelativeCharsOrder(String s, String res) {

        int offset = 0;

        for (int i = 0; i < res.length() && offset < s.length(); ++i) {
            if (res.charAt(i) == s.charAt(offset)) {
                ++offset;
            }
        }

        return offset == s.length();
    }

    private static boolean noOverlappingInChars(String s1, String s2) {

        Set<Character> s1Chars = new HashSet<>();

        for (int i = 0; i < s1.length(); ++i) {
            s1Chars.add(s1.charAt(i));
        }

        for (int j = 0; j < s2.length(); ++j) {
            if (s1Chars.contains(s2.charAt(j))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Bruteforce recursive solution.
     * <p>
     * N - res.length()
     * <p>
     * time: O(2^N), exponential time.
     * space: O(N)
     */
    public static boolean isInterleavingBruteforce(String s1, String s2, String res) {
        checkNotNull(s1);
        checkNotNull(s2);
        checkNotNull(res);

        return (s1.length() + s2.length() == res.length()) && isInterleavingRec(s1, 0, s2, 0, res, 0);
    }

    private static boolean isInterleavingRec(String s1, int i, String s2, int j, String res, int k) {
        if (k == res.length()) {
            return true;
        }

        assert k < res.length() : "k > res.length()";
        assert i < s1.length() || j < s2.length() : "Incorrect i/j values";
        assert (s1.length() - i) + (s2.length() - j) == res.length() - k : "Incorrect indexes detected";

        char baseCh = res.charAt(k);
        char ch1 = i < s1.length() ? s1.charAt(i) : Character.MIN_VALUE;
        char ch2 = j < s2.length() ? s2.charAt(j) : Character.MIN_VALUE;

        boolean matched = false;

        if (ch1 == baseCh) {
            matched = isInterleavingRec(s1, i + 1, s2, j, res, k + 1);
        }

        if (!matched && ch2 == baseCh) {
            matched = isInterleavingRec(s1, i, s2, j + 1, res, k + 1);
        }

        return matched;
    }

    public static List<String> getAllPossibleInterleavings(String s1, String s2) {
        checkNotNull(s1);
        checkNotNull(s2);

        List<String> res = new ArrayList<>();

        StringBuilder buf = new StringBuilder(s1.length() + s2.length());

        gatherInterleavingsRec(s1, 0, s2, 0, buf, res);

        return res;
    }

    /**
     * time: O(2^N)
     * space: O(N)
     */
    private static void gatherInterleavingsRec(String s1, int i, String s2, int j, StringBuilder buf, List<String> res) {

        if (i == s1.length()) {
            res.add(buf.toString() + s2.substring(j));
            return;
        }

        if (j == s2.length()) {
            res.add(buf.toString() + s1.substring(i));
            return;
        }

        // use character from s1
        gatherInterleavingsRec(s1, i + 1, s2, j, buf.append(s1.charAt(i)), res);
        buf.deleteCharAt(buf.length() - 1);

        // use character form s2
        gatherInterleavingsRec(s1, i, s2, j + 1, buf.append(s2.charAt(j)), res);
        buf.deleteCharAt(buf.length() - 1);
    }

    private StringInterleaving() {

        String s1 = "bcc";
        String s2 = "bbca";

        String incorrectRes = "bbcccab";

        LOG.info("isInterleavingBruteforce: " + isInterleavingBruteforce(s1, s2, incorrectRes));
        LOG.info("isInterleavingDynamic: " + isInterleavingDynamic(s1, s2, incorrectRes));

        List<String> res = getAllPossibleInterleavings(s1, s2);

        res.forEach(curStr ->
                LOG.info("isInterleavingBruteforce: " + isInterleavingBruteforce(s1, s2, curStr) +
                        ", isInterleavingDynamic: " + isInterleavingDynamic(s1, s2, curStr))
        );

        LOG.info(isInterleavingDynamic("abc", "xyz", "axbyzc"));

        LOG.info("Main done: java-" + System.getProperty("java.version"));
    }

    public static void main(String[] args) {
        try {
            new StringInterleaving();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}
