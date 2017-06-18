package com.max.algs.permutation;

import java.util.ArrayList;
import java.util.List;

public final class SubsetGenerator {


    private SubsetGenerator() {
        super();
    }

    public static List<List<Integer>> generateSubsets(int[] arr, int subsetSize) {

        if (arr == null) {
            throw new IllegalArgumentException("NULL 'arr' parameter passed");
        }

        if (subsetSize > arr.length) {
            throw new IllegalArgumentException("subset size > array length: " + subsetSize + " > " + arr.length);
        }

        if (subsetSize == 0) {
            return emptyListOfLists();
        }

        return generateSubsetsOfSizeRec(arr, subsetSize, 0);
    }

    private static List<List<Integer>> emptyListOfLists() {
        List<List<Integer>> list = new ArrayList<List<Integer>>();
        list.add(new ArrayList<Integer>());
        return list;
    }

    private static List<List<Integer>> generateSubsetsOfSizeRec(final int[] arr, final int subsetSize, final int index) {

        if (subsetSize == 1) {

            List<List<Integer>> allSubsets = new ArrayList<>();

            for (int i = index; i < arr.length; i++) {
                allSubsets.add(createSingleElemList(arr[i]));
            }

            return allSubsets;
        }

        final List<List<Integer>> allSubsets = new ArrayList<>();

        for (int i = index; arr.length - i >= subsetSize; i++) {

            List<List<Integer>> subsets = generateSubsetsOfSizeRec(arr, subsetSize - 1, i + 1);

            for (List<Integer> singleSubset : subsets) {
                singleSubset.add(0, arr[i]);
                allSubsets.add(singleSubset);
            }
        }

        return allSubsets;
    }

    private static <T> List<T> createSingleElemList(T elem) {
        List<T> list = new ArrayList<T>();
        list.add(elem);
        return list;
    }

    /**
     * Generate all subsets for a set using binary representation of an integer.
     */
    public static List<List<Integer>> generateAllSubsets2(int[] arr) {

        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("NULL or EMPTY 'arr' passed");
        }

        final int elemsCount = arr.length;
        final int maxValue = 1 << elemsCount;

        final List<List<Integer>> subsets = new ArrayList<>();

        for (int i = 0; i < maxValue; i++) {

            int value = i;

            List<Integer> singleSubset = new ArrayList<>();

            int elemIndex = 0;

            while (value > 0) {

                if ((value & 1) == 1) {
                    singleSubset.add(arr[elemIndex]);
                }

                ++elemIndex;
                value >>>= 1;
            }

            subsets.add(singleSubset);

        }

        return subsets;
    }

    /**
     * Generate all 2^n subsets from current set ('arr' parameter represents set)
     *
     * @param arr
     * @return
     */
    public static List<List<Integer>> generateAllSubsets(int[] arr) {

        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("NULL or EMPTY 'arr' passed");
        }


        return generateSubsetsRec(arr, 0);
    }

    private static List<List<Integer>> generateSubsetsRec(final int[] arr, final int index) {

        if (index == arr.length - 1) {
            List<List<Integer>> subsets = new ArrayList<>();

            subsets.add(new ArrayList<Integer>()); // empty set

            List<Integer> oneElementList = new ArrayList<>();
            oneElementList.add(arr[index]);
            subsets.add(oneElementList);

            return subsets;
        }

        int baseElem = arr[index];

        List<List<Integer>> allSubsets = new ArrayList<>();

        List<List<Integer>> subsets = generateSubsetsRec(arr, index + 1);

        allSubsets.addAll(subsets);

        for (List<Integer> singleSet : subsets) {

            List<Integer> newSet = new ArrayList<>();
            newSet.add(baseElem);
            newSet.addAll(singleSet);

            allSubsets.add(newSet);
        }

        return allSubsets;
    }


}
