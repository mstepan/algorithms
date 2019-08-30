package com.max.algs.arrays;

import com.max.algs.util.MathUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Array with quick min range query implementation.
 * <p>
 * Preprocess: time O(N*lgN*N), space O(N*lgN)
 * Query: time O(1), space O(1)
 */
public final class MinRangeQueryArray {

    private final int[] arr;
    private final Map<PositionAndLength, Integer> minValues;

    public MinRangeQueryArray(int[] arr) {
        checkNotNull(arr, "null 'arr' parameter passed");
        this.arr = Arrays.copyOf(arr, arr.length);
        this.minValues = buildMap(arr);
    }

    /**
     * time: O(1)
     * space: O(1)
     */
    public int minValue(int from, int to) {
        checkArgument(from <= to && from >= 0 && to < arr.length, "incorrect 'from' or 'to' value");

        if (from == to) {
            return arr[from];
        }

        final int elems = to - from + 1;
        final int powerOfTwo = (int) MathUtils.log2(elems);
        final int lengthToCheck = 1 << powerOfTwo;

        // first range min
        int firstMin = minValues.get(new PositionAndLength(from, lengthToCheck));

        // second range min
        int secondMin = minValues.get(new PositionAndLength(to - lengthToCheck + 1, lengthToCheck));

        return Math.min(firstMin, secondMin);
    }

    private Map<PositionAndLength, Integer> buildMap(int[] arr) {

        assert arr != null : "null 'arr' passed";

        Map<PositionAndLength, Integer> map = new HashMap<>();

        for (int i = 0, arrLength = arr.length; i < arrLength; ++i) {

            for (int length = 1; i + length <= arr.length; length <<= 1) {
                PositionAndLength posElem = new PositionAndLength(i, length);
                map.put(posElem, minValueInRange(arr, posElem));
            }
        }

        return map;
    }

    private int minValueInRange(int[] arr, PositionAndLength posElem) {
        int minValue = Integer.MAX_VALUE;
        for (int i = posElem.pos, to = posElem.pos + posElem.length; i < to; ++i) {
            minValue = Math.min(minValue, arr[i]);
        }

        return minValue;

    }

    private static final class PositionAndLength {
        final int pos;
        final int length;

        public PositionAndLength(int pos, int length) {
            this.pos = pos;
            this.length = length;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            PositionAndLength that = (PositionAndLength) o;

            if (length != that.length) {
                return false;
            }
            if (pos != that.pos) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result = pos;
            result = 31 * result + length;
            return result;
        }

        @Override
        public String toString() {
            return String.valueOf(pos) + ";" + length;
        }
    }


}
