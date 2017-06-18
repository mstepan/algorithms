package com.max.algs.presorted;


import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;


public final class PresortedAlgorithms {


    private PresortedAlgorithms() {
        super();
    }

    // (1, 4), (0, 3), (−1.5, 2), (3.6, 5)
    public static int maxOverlappDepth(OpenInterval[] points) {

        Arrays.sort(points, new Comparator<OpenInterval>() {
            @Override
            public int compare(OpenInterval p1, OpenInterval p2) {
                return Double.compare(p1.getA(), p2.getA());
            }
        });


        Deque<OpenInterval> workingDeque = new ArrayDeque<OpenInterval>(points.length);

        int maxDepth = 0;

        for (OpenInterval point : points) {
            workingDeque.add(point);
        }

        while (workingDeque.size() > 1) {

            while (workingDeque.isEmpty()) {

            }
        }

        return maxDepth;
    }

    /**
     * time: O(N^2)
     * space: O(1)
     */
    public static boolean twoNumbersSumBruteforce(int[] arr, int searchSum) {

        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] + arr[j] == searchSum) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * time: O(N*lgN)
     * space: O(N), we need to copy array before sorting
     */
    public static boolean twoNumbersSum(int[] originalArr, int searchSum) {

        int[] arr = Arrays.copyOf(originalArr, originalArr.length);

        Arrays.sort(arr);

        int i = 0;
        int j = arr.length - 1;

        int curSum = 0;

        while (i < j) {

            curSum = arr[i] + arr[j];

            if (curSum == searchSum) {
                return true;
            }

            if (curSum < searchSum) {
                ++i;
            }
            else {
                --j;
            }
        }

        return false;
    }

    /**
     * time: O(N*lgN)
     * space: O(N)
     */
    public static int distanceBetweenTwoClosestNumbers(int[] originalArr) {

        if (originalArr == null) {
            throw new IllegalArgumentException("NULL array passed");
        }

        if (originalArr.length == 0) {
            return 0;
        }

        final int[] arrCopy = Arrays.copyOf(originalArr, originalArr.length);

        Arrays.sort(arrCopy);

        int minDistance = Integer.MAX_VALUE;


        for (int i = 1; i < arrCopy.length; i++) {
            int curDistance = Math.abs(arrCopy[i - 1] - arrCopy[i]);

            if (curDistance < minDistance) {
                minDistance = curDistance;
            }
        }

        return minDistance;
    }

    /**
     * time: O(N^2)
     * space: O(1)
     */
    public static int distanceBetweenTwoClosestNumbersBruteforce(int[] originalArr) {

        if (originalArr == null) {
            throw new IllegalArgumentException("NULL array passed");
        }

        if (originalArr.length == 0) {
            return 0;
        }

        int minDistance = Integer.MAX_VALUE;

        for (int i = 0; i < originalArr.length - 1; i++) {

            for (int j = i + 1; j < originalArr.length; j++) {

                int curDistance = Math.abs(originalArr[i] - originalArr[j]);

                if (curDistance < minDistance) {
                    minDistance = curDistance;
                }
            }

        }

        return minDistance;
    }
}
