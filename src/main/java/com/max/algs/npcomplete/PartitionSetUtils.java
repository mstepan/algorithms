package com.max.algs.npcomplete;

import com.max.algs.ds.Pair;
import com.max.algs.util.IntegerUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public final class PartitionSetUtils {


    private PartitionSetUtils() {
        throw new IllegalStateException("Can't instantiate utility class");
    }


    public static Pair<List<Integer>, List<Integer>> partitionSet(int[] originalSet) {

        int endMask = 1 << originalSet.length;

        int minDiff = Integer.MAX_VALUE;
        List<Integer> first = new ArrayList<Integer>();
        List<Integer> second = new ArrayList<Integer>();


        for (int i = 1; i < endMask - 1; i++) {

            Pair<List<Integer>, List<Integer>> partitions = partitionByMask(originalSet, i);

            int curDiff = Math.abs(sum(partitions.getFirst()) - sum(partitions.getSecond()));

            if (curDiff < minDiff) {

                first = partitions.getFirst();
                second = partitions.getSecond();
                minDiff = curDiff;
            }
        }

        return new Pair<>(first, second);

    }


    public static int sum(List<Integer> list) {

        int sum = 0;

        for (Integer val : list) {
            sum += val;
        }

        return sum;
    }

    private static Pair<List<Integer>, List<Integer>> partitionByMask(int[] set, int bitset) {

        int mask = 1;
        int index = 0;


        List<Integer> first = new ArrayList<Integer>();
        List<Integer> second = new ArrayList<Integer>();

        while (index < set.length) {

            if ((bitset & mask) != 0) {
                first.add(set[index]);
            }
            else {
                second.add(set[index]);
            }

            ++index;
            mask <<= 1;
        }

        return new Pair<List<Integer>, List<Integer>>(first, second);
    }

    /**
     * Differencing algorithm (Karmarkar-Karp)
     * Another heuristic, due to Narendra Karmarkar and Richard Karp, is the differencing algorithm,
     * which at each step removes two numbers from the set and replaces them by their difference.
     * This represents the decision to put the two numbers in different sets, without immediately
     * deciding which one is in which set. The differencing heuristic performs better than the greedy one,
     * but is still bad for instances where the numbers are exponential in the size of the set.
     * <p>
     * See: http://en.wikipedia.org/wiki/Partition_problem
     */
    public static int partitionMinDiff(int[] orginalSet) {
        Queue<Integer> queue = new PriorityQueue<>(orginalSet.length, IntegerUtils.INT_DESC_CMP);

        for (int val : orginalSet) {
            queue.add(val);
        }

        while (queue.size() > 1) {
            final int first = queue.poll();
            final int second = queue.poll();
            queue.add(first - second);
        }

        return queue.poll();
    }


}
