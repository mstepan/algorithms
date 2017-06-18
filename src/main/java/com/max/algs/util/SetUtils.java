package com.max.algs.util;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility class for working with Sets.
 */
public final class SetUtils {


    private SetUtils() {
        super();
    }

    /**
     * time: O(N)
     * space: O(N)
     */
    public static List<Integer> difference(List<Integer> set1, List<Integer> set2) {

        if (set1 == null || set2 == null) {
            throw new IllegalArgumentException("'NULL' paramster(s) passed");
        }

        if (set1.size() == 0 || set2.size() == 0) {
            return set1;
        }

        Set<Integer> secondSet = new HashSet<>(set2);

        return set1.stream().parallel().filter(val -> !secondSet.contains(val)).collect(Collectors.toList());
    }

    /**
     * space: O(N)
     * time: O(N)
     */
    public static List<Integer> union(List<Integer> set1, List<Integer> set2) {
        return Stream.of(set1, set2).flatMap(arr -> arr.stream()).distinct().collect(Collectors.toList());
    }

    /*
     * Generate Cartesian products.
     *
     * time/space: O(N^k)
     * N - average number of elements in a set
     * k - number of sets, ie 'sets.size()'
     */
    public static List<List<Integer>> cartesianProducts(List<List<Integer>> sets) {
        Collections.reverse(sets);
        return cartesianProductsRec(sets, 0);
    }

    private static List<Integer> createListWithOneValue(int value) {
        List<Integer> data = new ArrayList<>();
        data.add(value);
        return data;
    }

    private static List<Integer> concatenate(List<Integer> baseList, int newValue) {
        List<Integer> concatenatedList = new ArrayList<>(baseList);
        concatenatedList.add(newValue);
        return concatenatedList;
    }

    private static List<List<Integer>> cartesianProductsRec(List<List<Integer>> sets, int index) {

        List<List<Integer>> res = new ArrayList<>();

        if (index == sets.size() - 1) {
            for (final int value : sets.get(index)) {
                res.add(createListWithOneValue(value));
            }

            return res;
        }

        List<List<Integer>> partialRes = cartesianProductsRec(sets, index + 1);

        for (int baseValue : sets.get(index)) {
            for (List<Integer> carProduct : partialRes) {
                res.add(concatenate(carProduct, baseValue));
            }
        }

        return res;
    }

}
