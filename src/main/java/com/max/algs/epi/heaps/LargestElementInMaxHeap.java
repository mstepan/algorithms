package com.max.algs.epi.heaps;


import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Queue;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 11.6. Compute the K largest elements in a max-heap.
 */
public class LargestElementInMaxHeap {

    private static final class HeapEntry implements Comparable<HeapEntry> {

        final Object[] arr;
        final int index;

        HeapEntry(Object[] arr, int index) {
            this.arr = arr;
            this.index = index;
        }

        Integer getValue() {
            return (Integer) arr[index];
        }

        int getIndex() {
            return index;
        }

        @Override
        public int compareTo(@NotNull HeapEntry other) {
            return getValue().compareTo(other.getValue());
        }

        @Override
        public String toString() {
            return String.valueOf(getValue());
        }
    }

    private static final int[] EMPTY_ARR = new int[0];

    /**
     * time: O(K*lgK)
     * space: O(K)
     */
    public static int[] getLargestElements(Object[] maxHeapArr, int totalElementsCount, int k) {
        checkNotNull(maxHeapArr);
        checkArgument(totalElementsCount <= maxHeapArr.length);
        checkArgument(k >= 0);

        if (k == 0 || totalElementsCount == 0) {
            return EMPTY_ARR;
        }

        int[] largest = new int[Math.min(k, totalElementsCount)];

        Queue<HeapEntry> candidates = new PriorityQueue<>(k, Collections.reverseOrder());

        assert maxHeapArr.length > 0 && totalElementsCount > 0 : "Empty array handled incorrectly";

        candidates.add(new HeapEntry(maxHeapArr, 0));

        for (int i = 0; i < largest.length; ++i) {

            assert !candidates.isEmpty() : "empty 'candidates' max-heap detected";

            HeapEntry curEntry = candidates.poll();

            largest[i] = curEntry.getValue();

            int child1 = 2 * curEntry.getIndex() + 1;
            int child2 = 2 * curEntry.getIndex() + 2;

            if (child1 < totalElementsCount) {
                candidates.add(new HeapEntry(maxHeapArr, child1));
            }

            if (child2 < totalElementsCount) {
                candidates.add(new HeapEntry(maxHeapArr, child2));
            }
        }

        assert largest.length == Math.min(k, maxHeapArr.length) : "Incorrect number of elements selected";

        return largest;
    }

    private LargestElementInMaxHeap() throws Exception {

        Queue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());

        maxHeap.add(10);

        maxHeap.add(6);
        maxHeap.add(8);

        maxHeap.add(3);
        maxHeap.add(4);
        maxHeap.add(7);
        maxHeap.add(5);

        maxHeap.add(1);
        maxHeap.add(2);
        maxHeap.add(3);
        maxHeap.add(2);
        maxHeap.add(6);
        maxHeap.add(5);

        /*
         * Access array field for PriorityQueue using reflection, otherwise
         * we need to use 'maxHeap.toArray(new Integer[0])', but this will add space complexity O(N).
        */
        Field arrField = PriorityQueue.class.getDeclaredField("queue");
        arrField.setAccessible(true);

        int k = 5;
        int[] arr = getLargestElements((Object[]) arrField.get(maxHeap), maxHeap.size(), k);

        System.out.println(Arrays.toString(arr));

        System.out.printf("'OnlineMedian' completed. java-%s %n", System.getProperty("java.version"));
    }

    public static void main(String[] args) {
        try {
            new LargestElementInMaxHeap();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
