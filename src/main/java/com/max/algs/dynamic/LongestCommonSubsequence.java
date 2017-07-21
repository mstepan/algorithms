package com.max.algs.dynamic;

import org.apache.log4j.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Return longest common sub sequence for 2 strings.
 */
public class LongestCommonSubsequence {

    private static final Logger LOG = Logger.getLogger(LongestCommonSubsequence.class);

    /**
     * N = s1.length()
     * M = s2.length()
     * <p>
     * time: O(N*M)
     * space: O(N*M), can be reduced to O(min(N, M))
     */
    public static int lcsDynamic(String s1, String s2) {
        checkNotNull(s1);
        checkNotNull(s2);

        int cols = s1.length() + 1;
        int rows = s2.length() + 1;

        int[][] sol = new int[rows][cols];

        for (int row = 1; row < rows; ++row) {
            for (int col = 1; col < cols; ++col) {

                char ch1 = s1.charAt(col - 1);
                char ch2 = s2.charAt(row - 1);

                if (ch1 == ch2) {
                    sol[row][col] = 1 + sol[row - 1][col - 1];
                }
                else {
                    sol[row][col] = Math.max(sol[row - 1][col], sol[row][col - 1]);
                }
            }
        }

        String longestSubseq = reconstructLcsFromDynamicSolution(sol, s1, s2);

        PartialRes res = new PartialRes(sol[rows - 1][cols - 1], longestSubseq);

        LOG.info("lcsDynamic: " + res.seq);

        return res.length;
    }

    private static String reconstructLcsFromDynamicSolution(int[][] sol, String s1, String s2) {

        int rows = sol.length;
        int cols = sol[rows - 1].length;

        int curRow = rows - 1;
        int curCol = cols - 1;

        StringBuilder longestSeq = new StringBuilder(sol[rows - 1][cols - 1]);

        while (curRow > 0 && curCol > 0) {

            // characters matched, proceed to diagonal
            if (s1.charAt(curCol - 1) == s2.charAt(curRow - 1)) {
                longestSeq.append(s1.charAt(curCol - 1));
                --curRow;
                --curCol;
            }
            else {

                // solution from top cell
                if (sol[curRow - 1][curCol] > sol[curRow][curCol - 1]) {
                    --curRow;
                }
                // solution from left cell
                else {
                    --curCol;
                }
            }
        }

        assert longestSeq.length() > 0 : "longestSeq has 0 length";

        return longestSeq.reverse().toString();
    }

    /**
     * N = min(s1.length(), s2.length())
     * <p>
     * time: O(2^N)
     * space: O(N)
     */
    public static int lcs(String s1, String s2) {
        checkNotNull(s1);
        checkNotNull(s2);

        PartialRes res = lcsRec(s1, 0, s2, 0);

        assert isSubsequence(s1, res.seq) : res.seq + " is no a sub sequence for 's1': " + s1;
        assert isSubsequence(s2, res.seq) : res.seq + " is no a sub sequence for 's2': " + s2;

        LOG.info("lcs: " + res.seq);

        return res.length;
    }

    private static boolean isSubsequence(String base, String seq) {

        if (seq.length() > base.length()) {
            return false;
        }

        int offset = 0;

        for (int i = 0; i < base.length() && offset < seq.length(); ++i) {
            if (base.charAt(i) == seq.charAt(offset)) {
                ++offset;
            }
        }

        return offset == seq.length();
    }

    private static final class PartialRes {
        final int length;
        final String seq;

        PartialRes(int length, String seq) {
            this.length = length;
            this.seq = seq;
        }
    }

    private static PartialRes lcsRec(String s1, int i, String s2, int j) {

        if (i == s1.length() || j == s2.length()) {
            return new PartialRes(0, "");
        }

        if (s1.charAt(i) == s2.charAt(j)) {

            PartialRes cur = lcsRec(s1, i + 1, s2, j + 1);

            return new PartialRes(1 + cur.length, s1.charAt(i) + cur.seq);
        }

        PartialRes res1 = lcsRec(s1, i + 1, s2, j);
        PartialRes res2 = lcsRec(s1, i, s2, j + 1);

        return res1.length >= res2.length ? res1 : res2;
    }

    private LongestCommonSubsequence() {

        String s1 = "AAACCGTGAGTTATTCGTTCTAGAA";
        String s2 = "CACCCCTAAGGTACCTTTGGTTC";

//        String s1 = "ABCD";
//        String s2 = "AEBD";

        LOG.info("lcs: " + lcs(s1, s2));
        LOG.info("lcsDynamic: " + lcsDynamic(s1, s2));

        LOG.info("LongestCommonSubsequence done: java-" + System.getProperty("java.version"));
    }

    public static void main(String[] args) {
        try {
            new LongestCommonSubsequence();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}
