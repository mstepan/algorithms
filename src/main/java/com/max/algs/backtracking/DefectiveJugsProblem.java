package com.max.algs.backtracking;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by mstepan on 6/21/16.
 */
public class DefectiveJugsProblem {

    private static final class Range implements Comparable<Range> {
        final int from;
        final int to;

        public static Range combine(Iterable<Range> ranges) {

            int fromSum = 0;
            int toSum = 0;

            for (Range range : ranges) {
                fromSum += range.from;
                toSum += range.to;
            }

            return new Range(fromSum, toSum);
        }

        @Override
        public int compareTo(Range other) {

            if (from < other.from) {
                return -1;
            }

            if (to > other.to) {
                return 1;
            }

            return 0;
        }

        public boolean contains(Range other) {
            return from <= other.from && to >= other.to;
        }

        public Range(int from, int to) {
            this.from = from;
            this.to = to;
        }


        @Override
        public String toString() {
            return "[" + from + ", " + to + "]";
        }
    }

    private static void findSol(Range res, Range[] arr) {
        Queue<Range> queue = new ArrayDeque<>();
        findSolRec(res, arr, queue, 0, 0);
    }

    private static void findSolRec(Range res, Range[] arr, Queue<Range> partial, int from, int to) {

        Range combined = new Range(from, to);
        // found solution
        if (res.contains(combined)) {
            System.out.println("solution: " + partial);
            System.out.println("combined: " + combined);
            return;
        }
        int cmp = combined.compareTo(res);

        // overflow
        if (cmp > 0) {
            return;
        }

        for (Range singleRange : arr) {
            partial.add(singleRange);
            findSolRec(res, arr, partial, from + singleRange.from, to + singleRange.to);
            partial.poll();
        }
    }

    private DefectiveJugsProblem() throws Exception {

        Range[] arr = {
                new Range(230, 240),
                new Range(290, 310),
                new Range(500, 515)
        };

        Range res = new Range(2100, 2300);
        findSol(res, arr);

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    public static void main(String[] args) {
        try {
            new DefectiveJugsProblem();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
