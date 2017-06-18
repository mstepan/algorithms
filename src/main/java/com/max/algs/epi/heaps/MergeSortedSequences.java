package com.max.algs.epi.heaps;

import com.google.common.base.Objects;
import com.max.algs.util.ArrayUtils;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Merge sorted sequences.
 */
public class MergeSortedSequences {

    private static final Logger LOG = Logger.getLogger(MergeSortedSequences.class);

    private MergeSortedSequences() throws Exception {

        int[][] sequences = new int[3][];

        for (int i = 0; i < sequences.length; ++i) {
            int[] arr = ArrayUtils.generateRandomArray(10, 100);
            Arrays.sort(arr);
            sequences[i] = arr;
        }

        Iterator<Integer> it = mergeSortedSequences(sequences);

        // even if we null all the references the iterator is still valid
        sequences[0] = null;
        sequences[1] = null;
        sequences[2] = null;

        while (it.hasNext()) {
            System.out.print(it.next() + ", ");
        }

        System.out.println();


        System.out.printf("'ClosestIntegerWithSameWeight' completed. java-%s %n", System.getProperty("java.version"));
    }

    /**
     * time: O(N*lgK)
     * space: O(K)
     * <p>
     * N - total elements count in all sequences
     * K - sequences count
     */
    private static Iterator<Integer> mergeSortedSequences(int[][] sequences) {

        checkNotNull(sequences, "null 'sequences' reference passed");

        for (int[] arr : sequences) {
            checkNotNull(arr, "One of the sequences is null");
        }

        return new SortedSeqIterator(sequences);
    }

    public static void main(String[] args) {
        try {
            new MergeSortedSequences();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    private static final class ArrayEntry implements Comparable<ArrayEntry> {
        final int[] arr;
        int index;

        ArrayEntry(int[] arr) {
            this.arr = arr;
        }

        @Override
        public int compareTo(@NotNull ArrayEntry other) {
            return Integer.compare(getValue(), other.getValue());
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ArrayEntry that = (ArrayEntry) obj;

            return Objects.equal(getValue(), that.getValue());
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(getValue());
        }

        @Override
        public String toString() {
            return String.valueOf(getValue());
        }

        int getValue() {
            assert index < arr.length : "index > arr.length";
            return arr[index];
        }

        boolean hasMoreElements() {
            return index + 1 < arr.length;
        }

        void moveNext() {
            ++index;
            assert index < arr.length : "index > arr.length";
        }
    }

    private static final class SortedSeqIterator implements Iterator<Integer> {

        private final PriorityQueue<ArrayEntry> minHeap;

        SortedSeqIterator(int[][] sequences) {
            minHeap = new PriorityQueue<>(sequences.length);

            for (int[] arr : sequences) {
                minHeap.add(new ArrayEntry(arr));
            }
        }

        @Override
        public boolean hasNext() {
            return !minHeap.isEmpty();
        }

        @Override
        public Integer next() {

            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            ArrayEntry entry = minHeap.poll();

            Integer val = entry.getValue();

            if (entry.hasMoreElements()) {
                entry.moveNext();
                minHeap.add(entry);
            }

            return val;
        }
    }

}
