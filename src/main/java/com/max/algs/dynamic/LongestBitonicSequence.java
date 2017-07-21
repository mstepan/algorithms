package com.max.algs.dynamic;

import com.max.algs.util.ArrayUtils;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Find longest bitonic sub sequence in array.
 * <p>
 * A sequence is bitonic if it is first monotonically increased and then monotonically decreased.
 * <p>
 */
public class LongestBitonicSequence {

    private static final Logger LOG = Logger.getLogger(LongestBitonicSequence.class);

    /**
     * Longest bitonic sub sequence dynamic solution.
     * <p>
     * A sequence is bitonic if it is first monotonically increased
     * and then monotonically decreased.
     * <p>
     * <p>
     * time: O(N)
     * space: O(N)
     */
    public static int longestBitonicSequence(int[] arr) {
        checkNotNull(arr);

        int[] leftIncreased = constructLeftIncreased(arr);
        int[] rightDecreased = constructRightDecreased(arr);

        assert leftIncreased.length == rightDecreased.length : "leftIncreased.length != rightDecreased.length";

        int maxBitonicLength = 0;
        int bitonicIndex = -1;

        for (int i = 0; i < leftIncreased.length; ++i) {
            int curBitonicLength = 1 + (leftIncreased[i] - 1) + (rightDecreased[i] - 1);

            if (curBitonicLength > maxBitonicLength) {
                maxBitonicLength = curBitonicLength;
                bitonicIndex = i;
            }
        }

        assert bitonicIndex >= 0 && bitonicIndex < arr.length : "'bitonicIndex' is incorrect";

        List<Integer> longestBitonicSeq = reconstructSolution(leftIncreased, rightDecreased, bitonicIndex, arr);

        assert !longestBitonicSeq.isEmpty() : "'longestBitonicSeq' is empty";

        LOG.info("longestBitonicSeq: " + longestBitonicSeq);

        return maxBitonicLength;
    }

    private static List<Integer> reconstructSolution(int[] left, int[] right, int bitonicIndex, int[] arr) {

        List<Integer> longestBitonicSeq =
                LongestIncreasedSubsequence.reconstructSolutionFromIndex(left, arr, bitonicIndex);

        longestBitonicSeq.remove(longestBitonicSeq.size() - 1);

        int decIndex = bitonicIndex;

        while (decIndex < arr.length) {

            longestBitonicSeq.add(arr[decIndex]);

            int k = decIndex + 1;

            while (k < arr.length) {
                if (arr[k] < arr[decIndex] && right[k] == right[decIndex] - 1) {
                    break;
                }
                ++k;
            }

            decIndex = k;

        }

        return longestBitonicSeq;
    }

    /**
     * time: O(N)
     * space: O(N)
     */
    private static int[] constructRightDecreased(int[] arr) {

        int[] rightDecreased = new int[arr.length];

        for (int i = arr.length - 1; i >= 0; --i) {

            int maxCur = 0;

            for (int j = i + 1; j < arr.length; ++j) {
                if (arr[j] < arr[i]) {
                    maxCur = Math.max(maxCur, rightDecreased[j]);
                }
            }

            rightDecreased[i] = 1 + maxCur;
        }

        return rightDecreased;
    }

    /**
     * time: O(N)
     * space: O(N)
     */
    private static int[] constructLeftIncreased(int[] arr) {
        int[] leftIncreased = new int[arr.length];

        for (int i = 0; i < arr.length; ++i) {
            int maxCur = 0;

            for (int j = i - 1; j >= 0; --j) {
                if (arr[j] < arr[i]) {
                    maxCur = Math.max(maxCur, leftIncreased[j]);
                }
            }

            leftIncreased[i] = 1 + maxCur;
        }

        return leftIncreased;
    }

    private LongestBitonicSequence() {

//        int[] arr = {1, 3, 12, 4, 2, 10};
        int[] arr = ArrayUtils.generateRandomArray(20, -20, 20);

        LOG.info(Arrays.toString(arr));

        int length = longestBitonicSequence(arr);

        LOG.info("longestBitonicSequence: " + length);

        LOG.info("LongestBitonicSequence done: java-" + System.getProperty("java.version"));
    }

    public static void main(String[] args) {
        try {
            new LongestBitonicSequence();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

}
