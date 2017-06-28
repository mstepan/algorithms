package com.max.algs.epi.searching;


import org.apache.log4j.Logger;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 12.2. Search in 2D sorted array.
 */
public class Search2DSortedArray {

    private static final Logger LOG = Logger.getLogger(Search2DSortedArray.class);

    private Search2DSortedArray() throws Exception {

        int[][] arr = {
                {-1, 2, 4, 4, 6},
                {1, 5, 5, 9, 21},
                {3, 6, 6, 9, 22},
                {3, 6, 8, 10, 24},
                {6, 8, 9, 12, 25},
                {8, 10, 12, 13, 40}
        };

        for (int[] rowArr : arr) {
            for (int value : rowArr) {

                boolean found = contains(arr, value);

                if (!found) {
                    throw new IllegalStateException("Not found");
                }
            }
        }


        System.out.printf("contains: %b %n", contains(arr, 7));
        System.out.printf("contains: %b %n", contains(arr, 50));
        System.out.printf("contains: %b %n", contains(arr, -10));
        System.out.printf("contains: %b %n", contains(arr, 11));

        System.out.printf("'Search2DSortedArray' completed. java-%s %n", System.getProperty("java.version"));
    }

    /**
     * N - rows
     * M - cols
     * <p>
     * time: O(N+M)
     * space: O(1)
     */
    public static boolean contains(int[][] arr, int value) {
        checkNotNull(arr);
        checkArrayRectangle(arr);

        int cols = arr[0].length;

        int row = arr.length - 1;
        int col = 0;

        while (row >= 0 && col < cols) {

            if (arr[row][col] == value) {
                return true;
            }

            // eliminate row
            if (arr[row][col] > value) {
                --row;
            }
            // eliminate col
            else {
                ++col;
            }
        }

        return false;
    }

    /**
     * time: O(N)
     */
    private static void checkArrayRectangle(int[][] arr) {
        if (arr.length == 0) {
            return;
        }
        checkArgument(arr[0] != null, "2D array isn't rectangle");

        int cols = arr[0].length;

        for (int row = 1; row < arr.length; ++row) {
            checkArgument(arr[row] != null && arr[row].length == cols, "2D array isn't rectangle");
        }
    }

    public static void main(String[] args) {
        try {
            new Search2DSortedArray();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

}
