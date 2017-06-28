package com.max.algs.epi;


import org.apache.log4j.Logger;

import java.util.AbstractMap.SimpleEntry;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public final class AbsSortedMain {

    private static final Logger LOG = Logger.getLogger(AbsSortedMain.class);

    private AbsSortedMain() throws Exception {

        final int[] arr = {-49, 75, 103, -147, 164, -197, -238, 314, 348, -422};
//        final int sum = 167;
        final int sum = -385;
//        final int sum = 267;
//        final int sum = 367;

        System.out.println(findPairSum(arr, sum));

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    public static SimpleEntry<Integer, Integer> findPairSum(int[] arr, int sum) {
        checkNotNull(arr);

        return findPositive(arr, sum).orElseGet(
                () -> findNegative(arr, sum).orElseGet(
                        () -> findMixed(arr, sum).
                                orElse(new SimpleEntry<>(-1, -1)))
        );
    }

    private static Optional<SimpleEntry<Integer, Integer>> findPositive(int[] arr, int sum) {

        assert arr != null;

        int smaller = 0;
        int bigger = arr.length - 1;

        while (smaller < bigger) {
            if (arr[smaller] < 0) {
                ++smaller;
                continue;
            }

            if (arr[bigger] < 0) {
                --bigger;
                continue;
            }

            long curSum = (long) arr[smaller] + arr[bigger];

            if (curSum == sum) {
                return Optional.of(new SimpleEntry<>(Math.min(bigger, smaller), Math.max(bigger, smaller)));
            }

            if (curSum > sum) {
                --bigger;
            }
            else {
                ++smaller;
            }
        }

        return Optional.empty();
    }

    private static Optional<SimpleEntry<Integer, Integer>> findNegative(int[] arr, int sum) {

        assert arr != null;

        int bigger = 0;
        int smaller = arr.length - 1;

        while (bigger < smaller) {
            if (arr[bigger] >= 0) {
                ++bigger;
                continue;
            }

            if (arr[smaller] >= 0) {
                --smaller;
                continue;
            }

            long curSum = (long) arr[bigger] + arr[smaller];

            if (curSum == sum) {
                return Optional.of(new SimpleEntry<>(Math.min(bigger, smaller), Math.max(bigger, smaller)));
            }

            if (curSum > sum) {
                ++bigger;
            }
            else {
                --smaller;
            }
        }

        return Optional.empty();

    }

    private static Optional<SimpleEntry<Integer, Integer>> findMixed(int[] arr, int sum) {

        assert arr != null;

        int bigger = arr.length - 1;
        int smaller = arr.length - 1;

        while (bigger >= 0 && smaller >= 0) {
            if (arr[bigger] < 0) {
                --bigger;
                continue;
            }

            if (arr[smaller] >= 0) {
                --smaller;
                continue;
            }

            long curSum = (long) arr[bigger] + arr[smaller];

            if (curSum == sum) {
                return Optional.of(new SimpleEntry<>(Math.min(bigger, smaller), Math.max(bigger, smaller)));
            }

            if (curSum > sum) {
                --bigger;
            }
            else {
                --smaller;
            }
        }

        return Optional.empty();

    }

    public static void main(String[] args) {
        try {
            new AbsSortedMain();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}
