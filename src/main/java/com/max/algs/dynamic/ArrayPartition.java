package com.max.algs.dynamic;

import com.max.algs.util.MatrixUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ArrayPartition {

    private static final Logger LOG = Logger.getLogger(ArrayPartition.class);


    private ArrayPartition() {
        throw new IllegalArgumentException("Can't instantiate utility class '" + ArrayPartition.class.getName() + "'");
    }


    public static List<List<Integer>> partition(int[] arr, int partitionsCount) {

        int[][] m = new int[partitionsCount + 1][arr.length + 1];

        for (int col = 1; col < m[0].length; col++) {
            m[0][col] = Integer.MAX_VALUE;
        }

        for (int col = 1; col < m[1].length; col++) {
            m[1][col] = m[1][col - 1] + arr[col - 1];
        }

        for (int row = 2; row < m.length; row++) {
            m[row][row] = Math.max(m[row - 1][row - 1], arr[row - 1]);

            for (int col = row + 1; col < m[row].length; col++) {

                int minValue = Integer.MAX_VALUE;
                int partitionSum = arr[col - 1];

                for (int i = col - 1; i > 0; i--) {
                    int maxSeparation = Math.max(m[row - 1][i], partitionSum);
                    minValue = Math.min(minValue, maxSeparation);
                    partitionSum += arr[i - 1];
                }

                m[row][col] = minValue;
            }
        }

        LOG.info(MatrixUtils.toString(m));


        int row = m.length - 1;
        int col = m[row].length - 1;

        List<List<Integer>> partitions = new ArrayList<>();

        while (row > 1) {

            int prev = Integer.MAX_VALUE;

            int sum = arr[col - 1];

            List<Integer> singlePartition = new ArrayList<>();
            partitions.add(singlePartition);

            while (true) {

                int curDiff = Math.abs(m[row - 1][col - 1] - sum);

                if (curDiff > prev) {
                    break;
                }

                singlePartition.add(arr[col - 1]);
                prev = curDiff;
                --col;
                sum += arr[col - 1];
            }

            Collections.reverse(singlePartition);

            --row;

        }

        List<Integer> singlePartition = new ArrayList<>();
        partitions.add(singlePartition);

        for (int i = col; i > 0; i--) {
            singlePartition.add(arr[i - 1]);
        }

        Collections.reverse(singlePartition);
        Collections.reverse(partitions);

        return partitions;
    }

}
