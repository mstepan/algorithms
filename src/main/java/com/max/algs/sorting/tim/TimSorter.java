package com.max.algs.sorting.tim;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;


/**
 * Stable tim sort in-place.
 */
public class TimSorter {

    private static final int MIN_RUN_THRESHOLD = 64;

    private static final int MIN_GALLOP = 7;

    public void sort(int[] arr) {

        checkArgument(arr != null, "null 'arr' parameter passed");

        if (arr.length < MIN_RUN_THRESHOLD) {
            BinaryInsertionSort.sort(arr, 0, arr.length - 1);
        }
        else {
            timSort(arr);
        }
    }

    private int calculateMinRun(int[] arr) {

        // If N < 64, minrun is N.
        if (arr.length < MIN_RUN_THRESHOLD) {
            return arr.length;
        }

        /*
        * We pick a minrun in range(32, 65) such that N/minrun is exactly a power of 2.
        * To do this take the first 6 bits of N, and add 1 if any of the remaining bits are set.
        */
        int value = arr.length;
        int leastBits = 0;

        while (value >= MIN_RUN_THRESHOLD) {
            leastBits |= (value & 1);
            value >>= 1;
        }

        return value + leastBits;
    }


    private void timSort(int[] arr) {

        int minRun = calculateMinRun(arr);

        List<Run> runs = createRuns(arr, minRun);

        assert runs.size() >= 2 : "runs.size() is incorrect, should be at least 2, current: " + runs.size();

        Iterator<Run> runsIt = runs.iterator();

        Deque<Run> mergeStack = new ArrayDeque<>();

        mergeStack.addLast(runsIt.next());
        mergeStack.addLast(runsIt.next());


        while (runsIt.hasNext()) {

            Run x = runsIt.next();
            Run y = mergeStack.pollLast();
            Run z = mergeStack.pollLast();

            if (x.length() <= y.length() + z.length()) {

                // merge 'y' with 'x'
                if (x.length() < z.length()) {
                    Run mergedRun = merge(arr, y, x);
                    mergeStack.addLast(z);
                    mergeStack.addLast(mergedRun);
                }

                // merge 'y' with 'z'
                else {
                    Run mergedRun = merge(arr, z, y);
                    mergeStack.addLast(mergedRun);
                    mergeStack.addLast(x);
                }
            }
        }

        while (mergeStack.size() != 1) {
            Run right = mergeStack.pollLast();
            Run left = mergeStack.pollLast();

            Run newRun = merge(arr, left, right);
            mergeStack.addLast(newRun);
        }
    }

    private Run merge(int[] arr, Run left, Run right) {
        // merge low
        if (left.length() <= right.length()) {
            MergeLo.merge(arr, left, right, MIN_GALLOP);
        }

        // merge high
        else {
            MergeHi.merge(arr, left, right, MIN_GALLOP);
        }

        return new Run(arr, left.from, right.to);
    }

    private List<Run> createRuns(int[] arr, int minRun) {
        List<Run> runs = new ArrayList<>();

        int from = 0;

        while (from < arr.length) {

            int to = from + 1;

            if (to >= arr.length) {
                runs.add(new Run(arr, from, from));
                break;
            }

            // look for increasing run
            if (arr[to - 1] <= arr[to]) {
                while (to + 1 < arr.length && arr[to] <= arr[to + 1]) {
                    ++to;
                }
            }

            // look for decreasing run
            else {
                while (to + 1 < arr.length && arr[to] > arr[to + 1]) {
                    ++to;
                }
                reverse(arr, from, to);
            }

            Run curRun = completeRunCreation(arr, from, to, minRun);
            runs.add(curRun);
            from = curRun.to + 1;
        }
        return runs;
    }

    private Run completeRunCreation(int[] arr, int from, int to, int minRun) {

        int runLength = calculateRunLength(from, to);

        // run is incomplete, add more elements
        if (runLength < minRun) {
            int elemsToAdd = minRun - runLength;
            to = Math.min(to + elemsToAdd, arr.length - 1);
            BinaryInsertionSort.sort(arr, from, to);
        }

        return new Run(arr, from, to);
    }

    private int calculateRunLength(int from, int to) {
        return to - from + 1;
    }

    private void reverse(int[] arr, int from, int to) {

        while (from < to) {
            swap(arr, from, to);
            ++from;
            --to;
        }
    }

    /**
     * Do not use xor trick here, like:
     * from = from ^ to
     * to = from ^ to
     * from = from ^ to
     * <p/>
     * cause spatial locality will be bad.
     */
    private void swap(int[] arr, int from, int to) {
        int temp = arr[from];
        arr[from] = arr[to];
        arr[to] = temp;
    }
}
