package com.max.algs.creative;


import com.max.algs.util.ArrayUtils;
import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 6.11.2. Find the two largest elements in a Set.
 */
public final class TwoLargestElementsInSet {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());

    static final class Tuple {
        final int max;
        final int secondMax;

        Tuple(int max, int secondMax) {
            this.max = max;
            this.secondMax = secondMax;
        }

        static Tuple of(int max) {
            return new Tuple(max, max);
        }

        static Tuple of(int max, int secondMax) {
            return new Tuple(max, secondMax);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tuple tuple = (Tuple) o;
            return max == tuple.max &&
                    secondMax == tuple.secondMax;
        }

        @Override
        public int hashCode() {
            return Objects.hash(max, secondMax);
        }

        @Override
        public String toString() {
            return "max = " + max + ", secondMax = " + secondMax;
        }

    }


    static final class MaxAndCandidates {
        final int max;
        final List<Integer> candidates;

        MaxAndCandidates(int max, List<Integer> candidates) {
            assert candidates != null;
            this.max = max;
            this.candidates = new ArrayList<>(candidates);
        }

        static MaxAndCandidates of(int singleValue) {
            return new MaxAndCandidates(singleValue, Collections.emptyList());
        }

        static MaxAndCandidates of(int maxValue, int candidate) {
            return new MaxAndCandidates(maxValue, Collections.singletonList(candidate));
        }

        static MaxAndCandidates of(int maxValue, List<Integer> candidates, int otherCandidate) {
            List<Integer> combinedCandidates = new ArrayList<>(candidates.size() + 1);
            combinedCandidates.addAll(candidates);
            combinedCandidates.add(otherCandidate);

            return new MaxAndCandidates(maxValue, combinedCandidates);
        }

        int findMaxCandidate() {
            int maxCandidate = Integer.MIN_VALUE;
            for (int val : candidates) {
                maxCandidate = Math.max(maxCandidate, val);
            }

            return maxCandidate;
        }

        @Override
        public String toString() {
            return "max = " + max + ", candidates = " + candidates;
        }

    }

    private static Tuple getTwoMaxesDivideAndConquer(int[] arr) {
        MaxAndCandidates partialRes = getTwoMaxesRec(arr, 0, arr.length - 1);
        return new Tuple(partialRes.max, partialRes.findMaxCandidate());
    }

    private static MaxAndCandidates getTwoMaxesRec(int[] arr, int from, int to) {

        int elems = to - from + 1;

        if (elems == 1) {
            return MaxAndCandidates.of(arr[from]);
        }

        if (elems == 2) {
            return arr[from] >= arr[to] ?
                    MaxAndCandidates.of(arr[from], arr[to]) : MaxAndCandidates.of(arr[to], arr[from]);
        }

        int mid = from + (to - from) / 2;

        MaxAndCandidates leftSide = getTwoMaxesRec(arr, from, mid);
        MaxAndCandidates rightSide = getTwoMaxesRec(arr, mid + 1, to);

        if (leftSide.max >= rightSide.max) {
            return MaxAndCandidates.of(leftSide.max, leftSide.candidates, rightSide.max);
        }

        return MaxAndCandidates.of(rightSide.max, rightSide.candidates, leftSide.max);
    }

    /**
     * Get two maximum values from unsorted array.
     * <p>
     * time: O(N)
     * space: O(1)
     * <p>
     * comparisons count = 2*n - 3
     */
    private static Tuple getTwoMaxes(int[] arr) {
        checkNotNull(arr);
        checkArgument(arr.length > 0);

        if (arr.length == 1) {
            return Tuple.of(arr[0]);
        }

        int max = arr[0];
        int secondMax = arr[1];

        if (arr[0] < arr[1]) {
            max = arr[1];
            secondMax = arr[0];
        }

        for (int i = 2; i < arr.length; ++i) {
            int cur = arr[i];

            if (cur > max) {
                secondMax = max;
                max = cur;
            }
            else if (cur > secondMax) {
                secondMax = cur;
            }
        }

        return Tuple.of(max, secondMax);
    }

    private TwoLargestElementsInSet() {

        for (int it = 0; it < 1000; ++it) {
            int[] arr = ArrayUtils.generateRandomArray(10_000);

            Tuple maxValues = getTwoMaxes(arr);
            Tuple maxValuesDivide = getTwoMaxesDivideAndConquer(arr);

            if (!maxValues.equals(maxValuesDivide)) {
                throw new AssertionError("Max values aren't equals");
            }
        }

        LOG.info("TwoLargestElementsInSet done...");
    }


    public static void main(String[] args) {
        try {
            new TwoLargestElementsInSet();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }


}
