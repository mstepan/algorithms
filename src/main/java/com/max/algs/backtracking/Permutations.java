package com.max.algs.backtracking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;


public final class Permutations {

    private Permutations() {
        super();
    }


    /**
     * Generate permutations in lexicographical order.
     */
    public static List<int[]> permutations(int[] elems) {

        if (elems == null) {
            throw new IllegalArgumentException("NULL 'elems' array passed");
        }

        Arrays.sort(elems); // sort to receive lexicographical permutation order

        List<int[]> res = new ArrayList<>();

        permutations(elems, 0, new BitSet(elems.length + 1), new int[elems.length], res);

        return res;
    }

    private static void permutations(int[] elems, int index, BitSet used, int[] sol, List<int[]> res) {

        if (index >= elems.length) {
            res.add(sol.clone());
            return;
        }

        for (int i = 0; i < elems.length; i++) {

            if (!used.get(i)) {

                sol[index] = elems[i];

                used.set(i);

                permutations(elems, index + 1, used, sol, res);

                used.clear(i);
            }

        }

    }

}
