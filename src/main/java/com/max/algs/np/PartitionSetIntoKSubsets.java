package com.max.algs.np;

import com.max.algs.AlgorithmsMain;
import com.max.algs.it.BaseAllValuesIterator;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Partition set into 'K' equal subsets(if possible).
 */
public class PartitionSetIntoKSubsets {

    private static final Logger LOG = Logger.getLogger(AlgorithmsMain.class);

    private PartitionSetIntoKSubsets() {

        int[] arr = {20, 23, 25, 45, 27, 40};

        int partitionsCount = 3;
        int length = arr.length;

        Iterator<String> it = new BaseAllValuesIterator(partitionsCount, length);

        int minDiff = Integer.MAX_VALUE;
        List<List<Integer>> bestPartition = null;

        while (it.hasNext()) {
            String partitionConfig = it.next();

            List<List<Integer>> partitions = partitionArray(partitionsCount, arr, partitionConfig);

            int diff = findMaxDifference(partitions);

            if (diff < minDiff) {
                bestPartition = partitions;
                minDiff = diff;
            }
        }

        LOG.info("bestPartition: " + bestPartition + ", diff: " + minDiff);

        LOG.info("PartitionSetIntoKSubsets done: java-" + System.getProperty("java.version"));
    }

    private static int findMaxDifference(List<List<Integer>> data) {

        int minSum = Integer.MAX_VALUE;
        int maxSum = Integer.MIN_VALUE;

        for (List<Integer> singlePartition : data) {
            int curSum = singlePartition.stream().mapToInt(val -> val).sum();
            minSum = Math.min(minSum, curSum);
            maxSum = Math.max(maxSum, curSum);
        }

        return maxSum - minSum;
    }

    private static List<List<Integer>> partitionArray(int count, int[] arr, String config) {

        List<List<Integer>> res = new ArrayList<>();

        for (int i = 0; i < count; ++i) {
            res.add(new ArrayList<>());
        }

        for (int i = 0; i < arr.length; ++i) {
            int index = config.charAt(i) - '0';

            res.get(index).add(arr[i]);
        }

        return res;
    }

    public static void main(String[] args) {
        try {
            new PartitionSetIntoKSubsets();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}


