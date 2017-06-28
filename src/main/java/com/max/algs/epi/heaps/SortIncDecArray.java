package com.max.algs.epi.heaps;


import com.max.algs.util.ArrayUtils;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Sort increased-decreased array.
 */
public class SortIncDecArray {

    private static final Logger LOG = Logger.getLogger(SortIncDecArray.class);

    private SortIncDecArray() {

        int[] arr = ArrayUtils.generateRandomArrayOfRandomLength(1_000_000);

        int[] arr2 = Arrays.copyOf(arr, arr.length);
        Arrays.sort(arr2);

        sort(arr);

        LOG.info(arr.length);

        if (!Arrays.equals(arr, arr2)) {
            throw new IllegalStateException("'arr' content is different from 'arr2' content");
        }

        LOG.info("'SortIncDecArray' completed. java-" + System.getProperty("java.version"));
    }

    private static List<ArrayChunk> createChunks(int[] arr) {

        assert arr != null;

        if (arr.length == 0) {
            return Collections.emptyList();
        }

        List<ArrayChunk> chunks = new ArrayList<>();

        ChunkType type = arr[1] > arr[0] ? ChunkType.INC : ChunkType.DEC;

        int from = 0;

        for (int i = 2; i < arr.length; ++i) {

            if ((type == ChunkType.INC && arr[i - 1] > arr[i]) ||
                    (type == ChunkType.DEC && arr[i - 1] < arr[i])) {
                chunks.add(ArrayChunk.of(arr, from, i - 1, type));
                from = i;
                type = detectType(arr, i);
            }
        }

        // add last chunk here
        chunks.add(ArrayChunk.of(arr, from, arr.length - 1, type));

        return chunks;
    }

    private static ChunkType detectType(int[] arr, int index) {
        if (index + 1 == arr.length) {
            return ChunkType.INC;
        }

        if (arr[index] < arr[index + 1]) {
            return ChunkType.INC;
        }

        return ChunkType.DEC;
    }


    /**
     * N - number of elements in 'arr'
     * K - number of increased/decreased sequences in 'arr'
     * <p>
     * time: O(N * lgK)
     * space: O(N + K)
     */
    public static void sort(int[] arr) {

        checkNotNull(arr);

        if (arr.length < 2) {
            return;
        }

        int index = 0;
        int[] sortedArr = new int[arr.length];

        Queue<ArrayChunk> minHeap = new PriorityQueue<>(createChunks(arr));

        while (!minHeap.isEmpty()) {
            ArrayChunk cur = minHeap.poll();
            sortedArr[index++] = cur.getValue();

            if (cur.hasMoreElements()) {
                cur.moveNext();
                minHeap.add(cur);
            }
        }

        System.arraycopy(sortedArr, 0, arr, 0, sortedArr.length);
    }

    public static void main(String[] args) {
        try {
            new SortIncDecArray();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }


    private enum ChunkType {
        INC,
        DEC
    }

    private static final class ArrayChunk implements Comparable<ArrayChunk> {
        final int[] arr;
        final int to;
        int from;

        ArrayChunk(int[] arr, int from, int to) {
            assert arr != null : "null array passed";
            assert from >= 0 && from < arr.length : "incorrect 'from'";
            assert to >= 0 && to < arr.length : "incorrect 'to'";
            assert from <= to : "from > to";

            this.arr = arr;
            this.from = from;
            this.to = to;
        }

        private static ArrayChunk of(int[] arr, int from, int to, ChunkType type) {

            if (type == ChunkType.DEC) {
                reverse(arr, from, to);
            }

            return new ArrayChunk(arr, from, to);
        }

        private static void reverse(int[] arr, int originalFrom, int originalTo) {
            assert originalFrom <= originalTo;

            int from = originalFrom;
            int to = originalTo;

            while (from <= to) {
                int temp = arr[from];

                arr[from] = arr[to];
                arr[to] = temp;
                ++from;
                --to;
            }

        }

        @Override
        public int compareTo(@NotNull ArrayChunk other) {
            return Integer.compare(getValue(), other.getValue());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ArrayChunk that = (ArrayChunk) o;

            return getValue() == that.getValue();
        }

        @Override
        public int hashCode() {
            return getValue();
        }

        int getValue() {
            return arr[from];
        }

        boolean hasMoreElements() {
            return from < to;
        }

        void moveNext() {
            ++from;
        }

        @Override
        public String toString() {
            return String.valueOf(getValue());
        }
    }

}
