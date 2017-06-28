package com.max.algs.epi.array;

import org.apache.log4j.Logger;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;


public class PascalTriangle {

    private static final Logger LOG = Logger.getLogger(PascalTriangle.class);

    private PascalTriangle() throws Exception {

        int[][] res = pascalTriangle(7);

        for (int[] line : res) {
            System.out.println(Arrays.toString(line));
        }

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * Calculate N lines of Pascal's triangle.
     * <p>
     * time: O(N^2)
     * space: O(N^2)
     */
    public static int[][] pascalTriangle(int n) {
        checkArgument(n >= 0);

        if (n == 0) {
            return new int[][]{};
        }

        int[][] res = new int[n][];
        res[0] = new int[]{1};

        for (int index = 1; index < n; ++index) {
            int[] prev = res[index - 1];

            int[] cur = new int[prev.length + 1];
            cur[0] = 1;
            for (int i = 1; i < prev.length; ++i) {
                cur[i] = prev[i - 1] + prev[i];
            }

            cur[cur.length - 1] = 1;

            res[index] = cur;
        }

        return res;
    }

    public static void main(String[] args) {
        try {
            new PascalTriangle();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

}
