package com.max.algs.number;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * For definition see: https://en.wikipedia.org/wiki/Taxicab_number
 */
public final class TaxicabNumbersFinder {

    private TaxicabNumbersFinder() {
        throw new IllegalStateException("Can't instantiate utility class");
    }

    /**
     * time: O(N^2*lgN)
     * space: O(N) - inside max-heap we only store one column, so N numbers
     */
    public static void findTaxicab(int n) {

        final int lastRow = n - 1;
        final int lastCol = n;

        Queue<Solution> maxHeap = new PriorityQueue<>(Solution.DESC_CMP);

        for (int row = lastRow; row != 0; --row) {
            maxHeap.add(new Solution(row, lastCol));
        }

        Solution prevSolution = null;

        while (!maxHeap.isEmpty()) {

            Solution cur = maxHeap.poll();

            if (prevSolution != null && prevSolution.value == cur.value) {
                System.out.println("Found taxicab numbers: " + prevSolution + " = " + cur);
            }

            prevSolution = cur;

            // we can move one column left
            if (cur.row + 1 < cur.col) {
                maxHeap.add(new Solution(cur.row, cur.col - 1));
            }
        }
    }

    private static long cube(int value) {
        return value * value * value;
    }

    public static void main(String[] args) {

        int n = 500;

        TaxicabNumbersFinder.findTaxicab(n);

        // 140^3 + 183^3 = 14^3 + 207^3
        int a = 140;
        int b = 183;
        int c = 14;
        int d = 207;
        System.out.println(a * a * a + b * b * b);
        System.out.println(c * c * c + d * d * d);

        System.out.printf("Main done...");
    }

    private static final class Solution {

        private static final Comparator<Solution> DESC_CMP = new Comparator<Solution>() {
            @Override
            public int compare(Solution o1, Solution o2) {
                return -Long.compare(o1.value, o2.value);
            }
        };

        final int row;
        final int col;

        final long value;

        public Solution(int row, int col) {
            this.row = row;
            this.col = col;
            this.value = cube(row) + cube(col);
        }

        @Override
        public String toString() {
            return "" + row + "^3 + " + col + "^3";
        }
    }
}
