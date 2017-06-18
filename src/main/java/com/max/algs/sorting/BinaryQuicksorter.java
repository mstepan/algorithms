package com.max.algs.sorting;

import com.max.algs.util.ArrayUtils;

import java.util.ArrayDeque;
import java.util.Deque;

import static com.google.common.base.Preconditions.checkArgument;


/**
 * In-place binary quicksort (MSD sort).
 * Not stable.
 * Optimised to take into account long same prefixes, like lot's of 0s at the beginning.
 */
public final class BinaryQuicksorter {

    private BinaryQuicksorter() {
        throw new IllegalArgumentException(BinaryQuicksorter.class.getCanonicalName() + " can't be instantiated");
    }

    public static void sort(int[] arr) {

        checkArgument(arr != null, "can't sort null 'arr'");

        // divide array in two halves: negative and positive including 0
        int boundary = -1;

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < 0) {
                ArrayUtils.swap(arr, i, boundary + 1);
                ++boundary;
            }
        }

        // sort negative values (left side)
        if (boundary > 0) {
            binaryQuicksortPartial(arr, 0, boundary);
        }

        // sort positive values and zero (right side)
        if ((arr.length - 1 - boundary) > 1) {
            binaryQuicksortPartial(arr, boundary + 1, arr.length - 1);
        }
    }


    /**
     * Do binary quicksort only for negative or positive values.
     */
    private static void binaryQuicksortPartial(int[] arr, int from, int to) {

        Deque<SortChunk> stack = new ArrayDeque<>();
        stack.add(new SortChunk(from, to, Integer.SIZE - 2));

        while (!stack.isEmpty()) {

            SortChunk range = stack.pollLast();

            int offset = range.bitIndex;
            int lo = range.from - 1;

            for (int i = range.from; i <= range.to; i++) {
                int digit = (arr[i] >> offset) & 1;

                if (digit == 0) {
                    ArrayUtils.swap(arr, i, lo + 1);
                    ++lo;
                }
            }

            if (offset != 0 && lo > range.from) {
                int nextBit = nextDifferentBit(arr, range.from, lo, offset);
                stack.add(new SortChunk(range.from, lo, nextBit));
            }

            if (offset != 0 && lo + 1 < range.to) {
                int nextBit = nextDifferentBit(arr, lo + 1, range.to, offset);
                stack.add(new SortChunk(lo + 1, range.to, nextBit));
            }
        }
    }

    /**
     * Determine next bit to check.
     */
    private static int nextDifferentBit(int[] arr, int from, int to, int lastBitChecked) {

        int andValue = 1;
        int orValue = 0;

        for (int i = from; i <= to; i++) {
            andValue &= arr[i];
            orValue |= arr[i];
        }

        int xoredValue = andValue ^ orValue;
        int nextBit = 0;

        for (int i = 0; i < lastBitChecked && xoredValue != 0; i++) {

            if ((xoredValue & 1) != 0) {
                nextBit = i;
            }

            xoredValue >>= 1;
        }

        return nextBit;
    }

    private static final class SortChunk {

        final int from;
        final int to;
        final int bitIndex;

        SortChunk(int from, int to, int bitIndex) {
            assert bitIndex >= 0 && bitIndex < Integer.SIZE;
            this.from = from;
            this.to = to;
            this.bitIndex = bitIndex;
        }
    }

}
