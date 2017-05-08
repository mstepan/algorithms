package com.max.algs;


import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

import static com.google.common.base.Preconditions.checkArgument;

public final class AlgorithmsMain {

    private static final class Point {
        final int x;
        final int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        double distance(double otherX, double otherY) {
            double dx = x - otherX;
            double dy = y - otherY;
            return Math.sqrt(dx * dx + dy * dy);
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    private static final class MaxRecusriveTask extends RecursiveTask<Integer> {

        final int[] arr;
        final int from;
        final int to;

        MaxRecusriveTask(int[] arr, int from, int to) {
            this.arr = arr;
            this.from = from;
            this.to = to;
        }

        @Override
        protected Integer compute() {

            int elems = to - from + 1;

            // sequential execution
            if (elems < arr.length / 4) {
                int maxValue = Integer.MIN_VALUE;

                for (int i = from; i <= to; ++i) {
                    maxValue = Math.max(maxValue, arr[i]);
                }

                return maxValue;
            }

            int mid = from + (to - from) / 2;

            ForkJoinTask<Integer> left = new MaxRecusriveTask(arr, from, mid).fork();

            MaxRecusriveTask right = new MaxRecusriveTask(arr, mid + 1, to);

            return Math.max(right.compute(), left.join());
        }
    }

    /**
     * time: O(lgN)
     * space: O(lgN)
     */
    public static double power(int value, int pow) {

        checkArgument(pow >= 0, "Negative power detected");

        if (pow == 0) {
            return 1.0;
        }

        if (pow == 1 || value == 1) {
            return value;
        }

        if (value == 0) {
            return 0.0;
        }

        return powerRec(value, pow);
    }

    private static double powerRec(int value, int pow) {

        if (pow == 0) {
            return 1.0;
        }

        // even case
        if ((pow & 1) == 0) {
            double res = powerRec(value, pow >> 1);
            return res * res;
        }

        return value * powerRec(value, pow - 1);
    }

    private AlgorithmsMain() throws Exception {

        int value = Integer.MAX_VALUE;
        int pow = 10;

        System.out.printf("expected: %.0f, actual: %.0f %n", Math.pow(value, pow), power(value, pow));

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    public static void main(String[] args) {
        try {
            new AlgorithmsMain();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

