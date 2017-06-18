package com.max.algs.codility;

import org.apache.log4j.Logger;

import java.util.Arrays;

/*

 Given an array A of N integers, we draw N discs in a 2D plane such that the I-th disc is centered on (0,I) and has a radius of A[I]. We say that the J-th disc and K-th disc intersect if J ≠ K and J-th and K-th discs have at least one common point.
 Write a function:
 class Solution { public int solution(int[] A); }
 that, given an array A describing N discs as explained above, returns the number of pairs of intersecting discs. For example, given N=6 and:

 A[0] = 1  
 A[1] = 5  
 A[2] = 2 
 A[3] = 1  
 A[4] = 4  
 A[5] = 0

 intersecting discs appear in eleven pairs of elements:
 0 and 1,
 0 and 2,
 0 and 4,
 1 and 2,
 1 and 3,
 1 and 4,
 1 and 5,
 2 and 3,
 2 and 4,
 3 and 4,
 4 and 5.
 so the function should return 11.
 The function should return −1 if the number of intersecting pairs exceeds 10,000,000.
 Assume that:
 N is an integer within the range [0..10,000,000];
 each element of array A is an integer within the range [0..2147483647].
 Complexity:
 expected worst-case time complexity is O(N*log(N));
 expected worst-case space complexity is O(N), beyond input storage (not counting the storage required for input arguments).
 Elements of input arrays can be modified.

 */
public class Beta {

    private static final Logger LOG = Logger.getLogger(Beta.class);

    public Beta() throws Exception {
        int[] a = {1, 5, 2, 1, 4, 0};
        LOG.info(solution(a));
    }

    public static void main(String[] args) {
        try {
            new Beta();
        }
        catch (Exception ex) {
            LOG.error(ex);
        }

    }

    public int solution(int[] a) {

        Range[] ranges = new Range[a.length];

        for (int center = 0; center < a.length; center++) {
            int from = Math.max(0, center - a[center]);
            int to = center + a[center];
            ranges[center] = new Range(from, to);
        }

        Arrays.sort(ranges);

        int intersections = 0;

        for (int i = ranges.length - 1; i > 0; i--) {

            int checkTo = ranges[i].from;

            for (int j = i - 1; j >= 0; j--) {
                if (checkTo > ranges[j].to) {
                    break;
                }

                ++intersections;
            }

        }

        return intersections;
    }

    private static final class Range implements Comparable<Range> {

        final int from;
        final int to;

        Range(int x, int y) {
            super();
            this.from = x;
            this.to = y;
        }

        @Override
        public int compareTo(Range other) {

            if (to > other.to) {
                return 1;
            }
            else if (to < other.to) {
                return -1;
            }

            if (from > other.from) {
                return 1;
            }
            else if (from < other.from) {
                return -1;
            }
            return 0;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + from;
            result = prime * result + to;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Range other = (Range) obj;
            if (from != other.from)
                return false;
            if (to != other.to)
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "[" + from + ";" + to + "]";
        }

    }

}
