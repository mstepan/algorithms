package com.max.algs.dynamic;

import com.max.algs.AlgorithmsMain;
import org.apache.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Deque;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Find longest palindromic sub sequence in a string.
 */
public class LongestPalindromicSebSequence {


    private static final Logger LOG = Logger.getLogger(AlgorithmsMain.class);

    /**
     * time: O(N^2)
     * space: O(N^2)
     */
    public static void longestPalindromicDynamic(String str) {
        checkNotNull(str);

        if (str.length() < 2) {
            LOG.info("longestPalindromicDynamic: " + str.length() + ", " + str);
            return;
        }

        int rows = str.length();
        int cols = str.length();

        int[][] sol = new int[rows][cols];

        for (int row = rows - 1; row >= 0; --row) {
            sol[row][row] = 1;

            for (int col = row + 1; col < cols; ++col) {
                if (str.charAt(row) == str.charAt(col)) {
                    sol[row][col] = 2 + sol[row + 1][col - 1];
                }
                else {
                    sol[row][col] = Math.max(sol[row][col - 1], sol[row + 1][col]);
                }
            }
        }

        int longestSubSeqLength = sol[0][cols - 1];

        assert longestSubSeqLength > 0 : "longestSubSeqLength == 0";

        // reconstruct solution, time: O(N), space: O(N)
        int row = 0;
        int col = cols - 1;

        StringBuilder halfSeq = new StringBuilder();

        while (row < col) {

            assert row < rows && col > 0 : "incorrect row or col";
            assert row < col : "row >= col";

            // add 2 characters and go diagonal down
            if (str.charAt(row) == str.charAt(col)) {
                halfSeq.append(str.charAt(col));

                --col;
                ++row;
            }
            // go left
            else if (sol[row][col - 1] > sol[row + 1][col]) {
                --col;
            }
            // go down
            else {
                ++row;
            }
        }

        String seq = halfSeq.toString() + (row == col ? str.charAt(row) : "") + halfSeq.reverse().toString();

        LOG.info("longestPalindromicDynamic: " + longestSubSeqLength + ", " + seq);
    }

    /**
     * time: (2^N)
     * space: O(N)
     */
    public static void longestPalindromicBruteforce(String str) {
        checkNotNull(str);

        if (str.length() < 2) {
            LOG.info("longestPalindromicBruteforce: " + str.length() + ", " + str);
            return;
        }

        SubSeqSol longest = longestPalindromicSubSequenceRec(str, 0, str.length() - 1);

        assert !longest.seq.isEmpty() : "longest.seq.isEmpty()";

        LOG.info("longestPalindromicBruteforce: " + longest.length + ", " + longest);
    }

    private static SubSeqSol longestPalindromicSubSequenceRec(String str, int from, int to) {
        if (from > to) {
            return SubSeqSol.EMPTY;
        }

        if (from == to) {
            return new SubSeqSol(str.charAt(from));
        }

        if (str.charAt(from) == str.charAt(to)) {

            SubSeqSol solution = longestPalindromicSubSequenceRec(str, from + 1, to - 1);

            return new SubSeqSol(solution, str.charAt(from));
        }

        SubSeqSol lestSide = longestPalindromicSubSequenceRec(str, from + 1, to);
        SubSeqSol rightSide = longestPalindromicSubSequenceRec(str, from, to - 1);

        return lestSide.length >= rightSide.length ? lestSide : rightSide;
    }

    private static final class SubSeqSol {

        static final SubSeqSol EMPTY = new SubSeqSol(new ArrayDeque<>(), 0);

        final Deque<Character> seq;
        final int length;

        SubSeqSol(Deque<Character> seq, int length) {
            this.seq = seq;
            this.length = length;
        }

        SubSeqSol(char ch) {
            this.seq = new ArrayDeque<>();
            this.seq.add(ch);
            this.length = 1;
        }

        SubSeqSol(SubSeqSol sol, char ch) {
            this.seq = new ArrayDeque<>(sol.seq);
            this.seq.addFirst(ch);
            this.seq.addLast(ch);
            this.length = sol.length + 2;
        }

        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder(seq.size());

            for (char ch : seq) {
                buf.append(ch);
            }

            return buf.toString();
        }
    }

    private LongestPalindromicSebSequence() {

        String str = "BBABCBCAB";

        longestPalindromicBruteforce(str);
        longestPalindromicDynamic(str);

        LOG.info("LongestPalindromicSebSequence done: java-" + System.getProperty("java.version"));
    }

    public static void main(String[] args) {
        try {
            new LongestPalindromicSebSequence();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

}
