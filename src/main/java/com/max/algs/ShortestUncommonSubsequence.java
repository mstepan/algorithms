package com.max.algs;


import org.apache.log4j.Logger;

public final class ShortestUncommonSubsequence {

    private static final Logger LOG = Logger.getLogger(ShortestUncommonSubsequence.class);

    private ShortestUncommonSubsequence() throws Exception {

        String s = "babab";
        String t = "babba";

        int uncommontShortestSubseq = findShortestUncommonSubsequence(s, t);

        System.out.println("Shortest uncommon sub sequence: " + uncommontShortestSubseq);

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * N - s.length()
     * M - t.length()
     * <p>
     * time: O(N*M*M) = O(N*M^2)
     * space: O(N*M)
     */
    public static int findShortestUncommonSubsequence(String s, String t) {

        final int rows = s.length() + 1;
        final int cols = t.length() + 1;

        int[][] opt = new int[rows][cols];

        // 's' is empty
        for (int col = 0; col < cols; ++col) {
            opt[0][col] = Integer.MAX_VALUE >> 1;
        }

        // 't' is empty
        for (int row = 1; row < rows; ++row) {
            opt[row][0] = 1;
        }

        for (int i = 1; i < rows; ++i) {
            for (int j = 1; j < cols; ++j) {

                char ch = s.charAt(i - 1);

                int k = -1;

                for (int it = j; it > 0; --it) {
                    if (t.charAt(it - 1) == ch) {
                        k = it;
                        break;
                    }
                }

                // 'ch' not found in t
                if (k == -1) {
                    opt[i][j] = 1;
                }
                else {
                    opt[i][j] = Math.min(
                            opt[i - 1][k - 1] + 1, // include 'ch'
                            opt[i - 1][j] // do not include 'ch' is solution
                    );
                }
            }
        }

        int res = opt[rows - 1][cols - 1];

        return res < t.length() ? res : -1;
    }

    public static void main(String[] args) {
        try {
            new ShortestUncommonSubsequence();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}

