package com.max.algs.dynamic;

import java.util.Arrays;


/**
 * See:
 *  https://en.wikipedia.org/wiki/Lattice_path
 *  https://en.wikipedia.org/wiki/Catalan_number
 *
 */
public final class DyckPathsCount {


    private DyckPathsCount() {
        throw new IllegalStateException("Can't instantiate utility only class");
    }

    /**
     * time: O(N^2)
     * space: O(N)
     */
    public static int dyckPathsCount(int n) {

        int[] sol = new int[n + 1];

        Arrays.fill(sol, 1);
        sol[n] = 0;

        int colsLeft = n - 1;

        while (colsLeft >= 0) {

            for (int col = colsLeft - 1; col >= 0; --col) {
                sol[col] = sol[col] + sol[col + 1];
            }

            --colsLeft;
        }

        return sol[0];
    }

}
