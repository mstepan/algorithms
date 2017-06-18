package com.max.algs.combination;

import java.util.ArrayList;
import java.util.List;

public final class CombinationUtils {

    private CombinationUtils() {
        super();
    }


    public static List<List<Integer>> generateNaturalNumbersCombinations(int length) {

        if (length <= 0) {
            throw new IllegalArgumentException("Negative/zero length passed '" + length + "'");
        }

        int[] naturalNumebrs = new int[]{0, 1, 2, 3, 4, 5};

        if (length > naturalNumebrs.length) {
            throw new IllegalArgumentException("length too big '" + length + "', max possible value '" + naturalNumebrs.length + "'");
        }

        return generateCombinationsRec(naturalNumebrs, 0, length);
    }


    private static List<List<Integer>> generateCombinationsRec(int[] arr, int index, int length) {

        if (length == 1) {

            List<List<Integer>> comb = new ArrayList<List<Integer>>();

            for (int i = index; i < arr.length; i++) {

                List<Integer> singleComb = new ArrayList<Integer>();
                singleComb.add(arr[i]);

                comb.add(singleComb);
            }

            return comb;
        }

        List<List<Integer>> allComb = new ArrayList<>();

        for (int i = index; i <= arr.length - length; i++) {

            int base = arr[i];

            List<List<Integer>> partialComb = generateCombinationsRec(arr, i + 1, length - 1);

            for (List<Integer> singlePartial : partialComb) {

                singlePartial.add(0, base);

                allComb.add(singlePartial);
            }
        }

        return allComb;
    }


}
