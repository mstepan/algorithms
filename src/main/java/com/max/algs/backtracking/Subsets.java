package com.max.algs.backtracking;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public final class Subsets {

    private Subsets() {
        super();
    }


    public static List<List<Integer>> subsets(int[] elems) {

        List<List<Integer>> res = new ArrayList<>();

        subsets(elems, 0, new BitSet(elems.length + 1), res);

        return res;
    }


    private static void subsets(int[] elems, int index, BitSet sol, List<List<Integer>> res) {

        if (index >= elems.length) {
            res.add(indexesToElems(elems, sol));
            return;
        }

        sol.set(index);
        subsets(elems, index + 1, sol, res);

        sol.clear(index);
        subsets(elems, index + 1, sol, res);

    }

    private static List<Integer> indexesToElems(int[] elems, BitSet indexes) {

        List<Integer> res = new ArrayList<>();

        for (int i = 0; i < elems.length; i++) {
            if (indexes.get(i)) {
                res.add(elems[i]);
            }
        }

        return res;
    }


}
