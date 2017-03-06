package com.max.algs.leetcode;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;

/**
 * See: https://leetcode.com/problems/merge-intervals/
 * <p/>
 * Given a collection of intervals, merge all overlapping intervals.
 * <p/>
 * For example,
 * <p/>
 * Given [1,3],[2,6],[8,10],[15,18],
 * return [1,6],[8,10],[15,18].
 */
public class MergeIntervals {

    private static final Comparator<Interval> START_CMP = new Comparator<Interval>() {
        @Override
        public int compare(Interval interval1, Interval interval2) {
            return Integer.compare(interval1.start, interval2.start);
        }
    };

    private static boolean isIntersect(Interval cur, Interval other) {

        Interval left = cur;
        Interval right = other;

        if (left.start > right.start) {
            left = other;
            right = cur;
        }

        return left.end >= right.start;
    }

    private static Interval intersect(Interval cur, Interval other) {

        Interval left = cur;
        Interval right = other;

        if (left.start > right.start) {
            left = other;
            right = cur;
        }

        return new Interval(left.start, Math.max(left.end, right.end));
    }

    /**
     * time: O(N*lgN)
     * space: O(N)
     */
    public List<Interval> merge(List<Interval> intervals) {

        if (intervals.isEmpty()) {
            return intervals;
        }

        Collections.sort(intervals, START_CMP);

        Deque<Interval> stack = new ArrayDeque<>();

        stack.push(intervals.get(0));

        for (int i = 1; i < intervals.size(); i++) {

            Interval cur = intervals.get(i);

            Interval prev = stack.pop();

            if (isIntersect(prev, cur)) {
                Interval merged = intersect(prev, cur);
                stack.push(merged);
            }
            else {
                stack.push(prev);
                stack.push(cur);
            }
        }

        List<Interval> res = new ArrayList<>();

        while (!stack.isEmpty()) {
            res.add(stack.pollLast());
        }

        return res;
    }

    public static class Interval {

        int start;
        int end;

        Interval(int s, int e) {
            start = s;
            end = e;
        }

        @Override
        public String toString() {
            return "[" + start + "," + end + "]";
        }
    }


    public MergeIntervals() throws Exception {
        List<Interval> intervals = new ArrayList<>();

        intervals.add(new Interval(1, 3));
        intervals.add(new Interval(2, 6));
        intervals.add(new Interval(8, 10));
        intervals.add(new Interval(15, 18));

        List<Interval> mergedList = merge(intervals);

        System.out.println(mergedList);
    }


    public static void main(String[] args) {
        try {
            new MergeIntervals();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
