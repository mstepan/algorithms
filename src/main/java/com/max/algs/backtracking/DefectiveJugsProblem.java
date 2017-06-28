package com.max.algs.backtracking;

import org.apache.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Queue;


public class DefectiveJugsProblem {

    private static final Logger LOG = Logger.getLogger(DefectiveJugsProblem.class);

    private DefectiveJugsProblem() {

        Range[] arr = {
                new Range(230, 240),
                new Range(290, 310),
                new Range(500, 515)
        };

        Range res = new Range(2100, 2300);
        findSol(res, arr);

        LOG.info("Main done: java-" + System.getProperty("java.version"));
    }

    private static void findSol(Range res, Range[] arr) {
        Queue<Range> queue = new ArrayDeque<>();
        findSolRec(res, arr, queue, 0, 0);
    }

    private static void findSolRec(Range res, Range[] arr, Queue<Range> partial, int from, int to) {

        Range combined = new Range(from, to);
        // found solution
        if (res.contains(combined)) {
            LOG.info("solution: " + partial);
            LOG.info("combined: " + combined);
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

    public static void main(String[] args) {
        try {
            new DefectiveJugsProblem();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    private static final class Range implements Comparable<Range> {
        final int from;
        final int to;

        Range(int from, int to) {
            this.from = from;
            this.to = to;
        }

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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Range range = (Range) o;

            if (from != range.from) return false;
            return to == range.to;
        }

        @Override
        public int hashCode() {
            return 31 * from + to;
        }

        public boolean contains(Range other) {
            return from <= other.from && to >= other.to;
        }

        @Override
        public String toString() {
            return "[" + from + ", " + to + "]";
        }
    }
}
