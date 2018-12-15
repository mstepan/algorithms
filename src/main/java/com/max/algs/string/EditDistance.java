package com.max.algs.string;


import static com.google.common.base.Preconditions.checkArgument;

public final class EditDistance {

    private EditDistance() {
        throw new IllegalStateException("Can't initialize utility only class");
    }

    public static int editDistance(String str, String other) {

        checkArgument(str != null, "null 'str' passed");
        checkArgument(other != null, "null 'other' passed");

        if (str.length() >= other.length()) {
            return editDistanceInternal(str, other);
        }

        return editDistanceInternal(other, str);
    }

    /**
     * Levenshtein edit distance.
     * <p>
     * N - str.length()
     * M - other.length()
     * <p>
     * time: O(N*M)
     * space: O( min(N, M) )
     */
    private static int editDistanceInternal(String str, String other) {

        final int rows = other.length() + 1;
        final int cols = str.length() + 1;

        int[] prev = new int[cols];

        for (int colIndex = 1; colIndex < cols; colIndex++) {
            prev[colIndex] = colIndex;
        }

        for (int rowIndex = 1; rowIndex < rows; ++rowIndex) {

            int[] cur = new int[cols];
            cur[0] = rowIndex;

            for (int colIndex = 1; colIndex < cols; ++colIndex) {
                char strCh = str.charAt(colIndex - 1);
                char otherCh = other.charAt(rowIndex - 1);

                if (strCh == otherCh) {
                    cur[colIndex] = prev[colIndex - 1];
                }
                else {
                    cur[colIndex] = 1 + min(prev[colIndex], cur[colIndex - 1], prev[colIndex - 1]);
                }
            }

            prev = cur;
        }

        return prev[cols - 1];
    }

    private static int min(int first, int second, int third) {
        return Math.min(Math.min(first, second), third);
    }

}
