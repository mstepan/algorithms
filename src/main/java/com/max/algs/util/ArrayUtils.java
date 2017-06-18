package com.max.algs.util;

import com.max.algs.ds.Pair;
import com.max.algs.ds.heap.BinaryHeap;
import org.apache.log4j.Logger;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class ArrayUtils {

    private static final String DEFAULT_ELEMS_DELIMITER = ", ";

    private static final Logger LOG = Logger.getLogger(ArrayUtils.class);

    private static final Random RAND = new Random();


    private ArrayUtils() {
        super();
    }

    /**
     * Rotate array i-th positions to the left.
     * <p>
     * time: O(N)
     * space: O(1)
     */
    public static void rotateLeft(char[] arr, int i) {
        checkNotNull(arr);
        checkArgument(i >= 0);

        if (arr.length < 2) {
            return;
        }

        int posToAdd = i % arr.length;

        if (posToAdd == 0) {
            return;
        }

        final int arrLength = arr.length;
        final int offset = arrLength - posToAdd;

        /*
         * The greatest common divisor of 'posToAdd' and 'arr.length' is the number of cycles
         * to be permuted (in terms of modern algebra, it is the number of cosets
         * in the permutation group induced by the rotation).
         */
        final int cyclesCount = MathUtils.gcd(posToAdd, arr.length);

        for (int cycle = 0; cycle < cyclesCount; ++cycle) {

            int from = cycle;
            char temp = arr[cycle];

            char next;
            int nextIndex;

            do {
                nextIndex = (from + offset) % arrLength;

                next = arr[nextIndex];
                arr[nextIndex] = temp;

                temp = next;
                from = nextIndex;
            }
            while (from != cycle);
        }
    }


    /**
     * Sum of bit differences among all pairs.
     * <p>
     * Given an integer array of n integers, find sum of bit differences in all pairs that can be
     * formed from array elements. Bit difference of a pair (x, y) is count of different bits at
     * same positions in binary representations of x and y.
     * <p>
     * For example, bit difference for 2 and 7 is 2. Binary representation of 2 is 010 and 7 is 111
     * (first and last bits differ in two numbers).
     * <p>
     * time: O(32*N) => O(N)
     * space: O(32) => O(1)
     */
    public static int calculateXoredOnes(int[] arr) {

        int[] onesBitFreq = new int[Integer.BYTES * Byte.SIZE];

        for (int value : arr) {

            int mask = 1;

            for (int i = 0, arrLength = onesBitFreq.length; i < arrLength && value != 0; i++) {

                if ((value & mask) != 0) {
                    ++onesBitFreq[i];
                    value ^= mask; // set to zero least significant '1' bit
                }

                mask <<= 1;
            }
        }

        int elemsCount = arr.length;

        int count = 0;
        int withZero;

        for (int withOne : onesBitFreq) {
            if (withOne != 0) {
                withZero = elemsCount - withOne;
                count += withOne * withZero;
            }
        }

        return count;
    }

    public static int calculateXoredOnesBruteforce(int[] arr) {

        int count = 0;

        for (int i = 0; i < arr.length - 1; i++) {

            int first = arr[i];

            for (int j = i + 1; j < arr.length; j++) {
                count += Integer.bitCount(first ^ arr[j]);
            }
        }

        return count;
    }

    public static String toString(int[] arr) {
        return toString(arr, DEFAULT_ELEMS_DELIMITER);
    }

    public static String toString(int[] arr, String delimiter) {
        StringBuilder buf = new StringBuilder("[");

        for (int value : arr) {
            buf.append(value).append(delimiter);
        }

        buf.replace(buf.length() - delimiter.length(), buf.length(), "]");

        return buf.toString();
    }

    public static void randomShuffle(String[] arr) {
        checkArgument(arr != null, "null 'arr' parameter passed");

        for (int i = 0, lastIndex = arr.length - 1; i < lastIndex; i++) {
            swap(arr, i, i + RAND.nextInt(arr.length - i));
        }
    }

    /**
     * time: O(N)
     * space: O(1)
     */
    public static void randomShuffle(char[] arr) {
        checkArgument(arr != null, "null 'arr' parameter passed");

        for (int i = 0, lastIndex = arr.length - 1; i < lastIndex; i++) {
            swap(arr, i, i + RAND.nextInt(arr.length - i));
        }
    }

    public static void randomShuffle(int[] arr) {
        checkArgument(arr != null, "null 'arr' parameter passed");

        for (int i = 0, lastIndex = arr.length - 1; i < lastIndex; i++) {
            swap(arr, i, i + RAND.nextInt(arr.length - i));
        }
    }

    public static int min(int... arr) {

        checkArgument(arr.length != 0, "'arr' length is 0");

        return Arrays.stream(arr).parallel().min().getAsInt();
    }

    public static int max(int... arr) {

        checkArgument(arr.length != 0, "'arr' length is 0");

        return Arrays.stream(arr).max().getAsInt();
    }

    /**
     * Choose random median from 3 different locations of array.
     * Used for quicksort 3 median random selection.
     */
    public static int randomMedianIndex(int[] arr, int from, int to) {

        int index1 = from + RAND.nextInt(to - from + 1);
        int index2 = from + RAND.nextInt(to - from + 1);
        int index3 = from + RAND.nextInt(to - from + 1);

        int value1 = arr[index1];
        int value2 = arr[index2];
        int value3 = arr[index3];

        int minValue = min(value1, value2, value3);
        int maxValue = max(value1, value2, value3);

        if (value1 != minValue && value1 != maxValue) {
            return index1;
        }

        if (value2 != minValue && value2 != maxValue) {
            return index2;
        }

        if (value3 != minValue && value3 != maxValue) {
            return index3;
        }

        return index1;
    }


    /**
     * Given an integer array. Perform circular right shift by n.
     * <p>
     * time: O(N)
     * space: O(1)
     */
    public static void shiftRight(int[] arr, int shift) {

        checkNotNull(arr, "'arr' is NULL");
        checkArgument(arr.length >= 2, "'arr' length < 2, length = %s", arr.length);

        if (shift % arr.length == 0) {
            return;
        }

        int index = 0;
        int prev = arr[0];

        do {
            int newIndex = (index + shift) % arr.length;
            int temp = arr[newIndex];

            arr[newIndex] = prev;
            prev = temp;
            index = newIndex;
        }
        while (prev != arr[index]);

    }


    /**
     * time: O(lgN)
     * space: O(1)
     */
    public static int findRotationPoint(int[] arr) {
        checkNotNull(arr, "'arr' is NULL");

        if (arr.length < 2) {
            return -1;
        }

        if (arr[0] <= arr[arr.length - 1]) {
            return -1;
        }

        int left = 0;
        int right = arr.length - 1;
        int mid;

        while (left < right) {

            mid = (left + right) >>> 1;

            if (arr[mid] < arr[mid - 1]) {
                return mid;
            }
            if (arr[mid] > arr[mid + 1]) {
                return mid + 1;
            }

            if (arr[left] <= arr[mid]) {
                // go right
                left = mid + 1;
            }
            else {
                // go left
                right = mid - 1;
            }

        }

        return -1;
    }


    /**
     * Rotate array.
     * time: O(N)
     * space: O(1) if one cycle only or O(N/16)
     *
     * @return count of cycles in array (if we view array rotation as permutation of elements)
     */
    public static int rotate(int[] arr, int baseOffset) {

        checkNotNull(arr, "null 'arr' passed");
        checkArgument(baseOffset != Integer.MIN_VALUE, "Incorrect 'baseOffset': %s (Integer.MIN_VALUE)",
                baseOffset);

        if (arr.length < 2 || baseOffset == 0) {
            return 0;
        }

        int offset = Math.abs(baseOffset);

        // negative offset, rotate array to the left
        if (baseOffset < 0) {
            offset = arr.length - offset;
        }

        offset = offset % arr.length;

        if (offset == 0) {
            return 0;
        }

        // only one cycle exists in array, we can do rotation without additional memory
        if (hasOneCycle(arr, baseOffset)) {

            int prev = arr[0];
            int next = offset;
            int temp;

            while (next != 0) {
                temp = arr[next];
                arr[next] = prev;
                prev = temp;
                next = (next + offset) % arr.length;
            }

            arr[0] = prev;

            return 1;
        }
        // array has more than 1 cycle,
        // we need to use additional memory to store already processed elements
        else {

            BitSet rotated = new BitSet(arr.length + 1);
            int cyclesCount = 0;

            int from = 0;
            while (from < arr.length) {

                ++cyclesCount;
                int prev = arr[from];
                int next = (from + offset) % arr.length;
                int temp;

                while (next != from) {
                    temp = arr[next];
                    arr[next] = prev;
                    rotated.set(next);
                    prev = temp;
                    next = (next + offset) % arr.length;
                }

                arr[next] = prev;
                rotated.set(from);

                from = rotated.nextClearBit(from + 1);
            }

            return cyclesCount;
        }
    }

    private static boolean hasOneCycle(int[] arr, int offset) {

        int from = 0;
        int next = (from + offset) % arr.length;
        int zeroCycleLength = 1;

        while (next != from) {
            ++zeroCycleLength;
            next = (next + offset) % arr.length;
        }

        return zeroCycleLength == arr.length;
    }


    private static void printCycle(int from, int offset, int length) {

        List<Integer> cycleIndexes = new ArrayList<>();
        cycleIndexes.add(from);

        int cur = (from + offset) % length;

        while (cur != from) {

            cycleIndexes.add(cur);

            cur = (cur + offset) % length;

        }

        System.out.printf("cycle[%d] %s %n", from, cycleIndexes);

    }


    @SuppressWarnings("unchecked")
    public static <T> T[] create(int length) {
        return (T[]) new Object[length];
    }

    public static <T> T[] create(Collection<T> col) {

        T[] arr = create(col.size());


        int index = 0;
        for (T value : col) {
            arr[index] = value;
            ++index;
        }

        return arr;
    }


    public static int[] getSpiral(int[][] m) {

        int circleIndex = 0;

        int totalCirclesCount = m.length / 2;

        int index = 0;
        int[] arr = new int[m.length * m.length];

        while (circleIndex < totalCirclesCount) {

            for (int col = circleIndex; col < m.length - circleIndex - 1; col++) {
                arr[index++] = m[circleIndex][col];
            }

            for (int row = circleIndex; row < m.length - circleIndex - 1; row++) {
                arr[index++] = m[row][m.length - circleIndex - 1];
            }

            for (int col = m.length - 1 - circleIndex; col > circleIndex; col--) {
                arr[index++] = m[m.length - 1 - circleIndex][col];
            }

            for (int row = m.length - 1 - circleIndex; row > circleIndex; row--) {
                arr[index++] = m[row][circleIndex];
            }

            ++circleIndex;
        }


        int middle = m.length / 2;

        // 'odd' matrix size
        if ((m.length & 1) == 1) {
            arr[index] = m[middle][middle];
        }

        return arr;

    }


    /**
     * time: O(N*lgN), cause we are using sorting
     * space: O(N)
     */
    public static int[] removeDuplicates(int[] arr) {

        checkForNull(arr);

        Arrays.sort(arr);

        int uniqueElements = 1;

        for (int i = 1; i < arr.length; i++) {
            if (arr[i] != arr[i - 1]) {
                ++uniqueElements;
            }
        }

        int[] uniqueArr = new int[uniqueElements];

        uniqueArr[0] = arr[0];
        int baseIndex = 1;

        for (int i = 1; i < arr.length; i++) {
            if (arr[i] != arr[i - 1]) {
                uniqueArr[baseIndex] = arr[i];
                ++baseIndex;
            }
        }


        return uniqueArr;
    }

    public static int maxValue(int[] arr) {

        checkForNull(arr);

        int max = Integer.MIN_VALUE;

        for (int value : arr) {
            if (value > max) {
                max = value;
            }
        }

        return max;
    }


    /**
     * Ugly, but highly optimized code.
     */
    public static int maxProductOfThreeElements(int[] arr) {

        if (arr == null) {
            throw new IllegalArgumentException();
        }

        if (arr.length < 4) {
            if (arr.length == 0) {
                return 0;
            }

            int res = 1;

            for (int val : arr) {
                res *= val;
            }
            return res;
        }

        int max1 = arr[0] > arr[1] ? arr[0] : arr[1];
        int max2 = arr[0] < arr[1] ? arr[0] : arr[1];
        int max3 = Integer.MIN_VALUE;

        int min1 = arr[0] < arr[1] ? arr[0] : arr[1];
        int min2 = arr[0] > arr[1] ? arr[0] : arr[1];
        int val;
        int temp;

        for (int i = 2; i < arr.length; i++) {

            val = arr[i];

            if (val > max3) {
                max3 = val;

                if (max3 > max2) {
                    temp = max3;
                    max3 = max2;
                    max2 = temp;
                }

                if (max2 > max1) {
                    temp = max2;
                    max2 = max1;
                    max1 = temp;
                }
            }
            else if (val < min2) {
                min2 = val;
                if (min2 < min1) {
                    temp = min2;
                    min2 = min1;
                    min1 = temp;
                }
            }

        }

        int negMinMul = min2 * min1;
        int posMinMul = max2 * max3;

        if (negMinMul > posMinMul) {
            return negMinMul * max1;
        }

        return posMinMul * max1;
    }

    /**
     * Given an array of integers, find all sub-arrays whose elements sum zero.
     * {1,-1,4,-4} has 3 such arrays {1, -1}, {1,-1,4,-4},  {4,-4}
     * <p>
     * time: O(N^2)
     * space: O(1)
     */
    public static List<int[]> zeroSubarrays(int[] arr) {

        checkForNull(arr);

        List<int[]> subarrays = new ArrayList<>();
        int sum;

        for (int i = 0; i < arr.length; i++) {

            if (arr[i] == 0) {
                subarrays.add(new int[]{arr[i]});
            }

            sum = arr[i];

            for (int j = i + 1; j < arr.length; j++) {

                sum += arr[j];

                if (sum == 0) {
                    subarrays.add(Arrays.copyOfRange(arr, i, j + 1));
                }

            }
        }

        return subarrays;
    }


    public static int[][] subsums(int[] arr) {
        int[][] sums = new int[arr.length][arr.length];

        for (int i = 0; i < arr.length; i++) {
            sums[i][i] = arr[i];
        }

        for (int row = 0; row < arr.length; row++) {
            for (int col = row + 1; col < arr.length; col++) {
                sums[row][col] = sums[row][col - 1] + arr[col];
            }
        }

        return sums;

    }

    /**
     * k = segmentsCount-1
     * <p>
     * time: O(N^2)
     * space: O(N^k)
     */
    public static int[][] printLinearPartition(int[] arr, int segmentsCount) {


        int[][] segments = new int[segmentsCount][];

        int[][] subSums = subsums(arr);

        int[] divs = new int[2];

        int minDiff = Integer.MAX_VALUE;

        for (int first = 0; first < arr.length - 2; first++) {

            int sum1 = subSums[0][first];

            for (int second = first + 1; second < arr.length - 1; second++) {
                int sum2 = subSums[first + 1][second];
                int sum3 = subSums[second + 1][arr.length - 1];

                int curDif = Math.abs(sum1 - sum2) + Math.abs(sum1 - sum3) + Math.abs(sum2 - sum3);

                if (curDif < minDiff) {
                    minDiff = curDif;
                    divs[0] = first;
                    divs[1] = second;
                }
            }
        }

        int start = 0;

        for (int i = 0; i < segments.length - 1; i++) {
            segments[i] = Arrays.copyOfRange(arr, start, divs[i] + 1);
            start = divs[i] + 1;
        }

        segments[segments.length - 1] = Arrays.copyOfRange(arr, start, arr.length);

        return segments;
    }


    /**
     * @param arr
     * @return
     */
    public static int moreThenHalfAppearedElement(int[] arr) {

        if (arr == null) {
            throw new IllegalArgumentException("NULL 'arr' parameter passed");
        }

        if (arr.length == 0) {
            return -1;
        }

        int counter = 1;
        int curValue = arr[0];

        for (int i = 1; i < arr.length; i++) {
            if (counter == 0) {
                curValue = arr[i];
                counter = 1;
            }
            else if (curValue == arr[i]) {
                ++counter;
            }
            else {
                --counter;
            }
        }

        int freq = 0;

        for (int value : arr) {
            if (value == curValue) {
                ++freq;
            }
        }

        if (freq > arr.length / 2) {
            return curValue;
        }

        return -1;
    }


    public static <T> void shuffle(T[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            swap(arr, i, i + RAND.nextInt(arr.length - i));
        }
    }

    public static void shuffle(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            swap(arr, i, i + RAND.nextInt(arr.length - i));
        }
    }

    /**
     * Find order statistic (k-th element) in array.
     */
    public static int findOrder(int[] baseArr, int index) {

        if (baseArr == null) {
            throw new IllegalArgumentException("'NULL' array passed");
        }

        if (index < 0) {
            throw new IllegalArgumentException("'index' is negative: " + index);
        }

        if (index >= baseArr.length) {
            throw new IllegalArgumentException("'index' too big should be in range [0; " + baseArr.length + "], " + index);
        }

        final int[] arr = Arrays.copyOf(baseArr, baseArr.length);

        int from = 0;
        int to = arr.length - 1;

        while (from < to) {

            int randIndex = from + RAND.nextInt(to - from);
            swap(arr, randIndex, to);

            int pivot = arr[to];
            int less = from - 1;

            for (int i = from; i < to; i++) {
                if (arr[i] < pivot) {
                    swap(arr, less + 1, i);
                    ++less;
                }
            }

            int finalPos = less + 1;

            swap(arr, finalPos, to);

            int offset = finalPos - from + 1;

            if (index == offset - 1) {
                return arr[finalPos];
            }
            else if (index > offset - 1) {
                index -= offset;
                from = finalPos + 1;
            }
            else {
                to = finalPos - 1;
            }
        }

        return arr[from];
    }


    /**
     * Returns index of start position for longest decreasing suffix, or -1 if not found.
     */
    public static int longestDecreasingSuffix(int[] arr) {

        for (int i = arr.length - 1; i > 0; i--) {
            if (arr[i - 1] < arr[i]) {
                return i;
            }
        }

        return -1;
    }


    public static void reverseArray(int[] arr, int from, int to) {

        while (from < to) {
            ArrayUtils.swap(arr, from, to);
            --to;
            ++from;
        }

    }

    /**
     * time: O(N*lgN)
     * space: O(1)
     */
    public static int findMode(int[] arr) {

        if (arr == null) {
            throw new IllegalArgumentException("Null array passed");
        }

        if (!ArrayUtils.isSorted(arr)) {
            LOG.info("Sorting array");
            Arrays.sort(arr);
        }

        int maxCount = Integer.MIN_VALUE;
        int mode = -1;

        int curCount = 1;

        for (int i = 1; i < arr.length; i++) {
            if (arr[i] == arr[i - 1]) {
                ++curCount;
            }
            else {
                if (curCount > maxCount) {
                    mode = arr[i - 1];
                    maxCount = curCount;
                }

                curCount = 1;
            }
        }


        return mode;

    }


    public static boolean isSorted(int[] arr) {

        checkForNull(arr);

        for (int i = 1; i < arr.length; i++) {
            if (arr[i - 1] > arr[i]) {
                return false;
            }
        }

        return true;
    }


    private static void checkForNull(int[] arr) {
        if (arr == null) {
            throw new IllegalArgumentException("NULL array passed");
        }
    }

    public static void fastSwap(int[] arr, int index1, int index2) {
        arr[index1] ^= arr[index2];
        arr[index2] = arr[index1] ^ arr[index2];
        arr[index1] ^= arr[index2];
    }

    public static void swap(char[] arr, int from, int to) {
        char temp = arr[from];
        arr[from] = arr[to];
        arr[to] = temp;
    }


    public static void swap(int[] arr, int index1, int index2) {

        checkForNull(arr);

        if (index1 < 0 || index2 < 0 || index1 >= arr.length || index2 >= arr.length) {
            throw new IllegalArgumentException("Illegal index passed, should be in range [0; " + (arr.length - 1) + "]: " +
                    "index1 = " + index1 + ", index2 = " + index2);
        }

        if (index1 == index2) {
            return;
        }

        int temp = arr[index1];

        arr[index1] = arr[index2];
        arr[index2] = temp;
    }


    public static <T> void swap(T[] arr, int index1, int index2) {

        checkForNull(arr);

        if (index1 < 0 || index2 < 0 || index1 >= arr.length || index2 >= arr.length) {
            throw new IllegalArgumentException("Illegal index passed, should be in range [0; " + (arr.length - 1) + "]: " +
                    "index1 = " + index1 + ", index2 = " + index2);
        }

        T temp = arr[index1];

        arr[index1] = arr[index2];
        arr[index2] = temp;
    }

    private static <T> void checkForNull(T[] arr) {
        if (arr == null) {
            throw new IllegalArgumentException("NULL array passed");
        }
    }


    /**
     * check for O(lgN) solution using binary search.
     * <p>
     * Use like median of medians algorithm.
     */
    public static int findElementByRankOptimized2(int[] arr1, int[] arr2, int rank) {

        int from1 = 0;
        int from2 = 0;

        int offset = rank / 2;

        while (offset > 0) {

            int value1 = arr1[from1 + offset];
            int value2 = arr2[from2 + offset];

            if (value1 < value2 || value1 == value2) {
                from1 += offset;
            }
            else {
                from2 += offset;
            }


            offset = offset / 2;
        }

        return Math.min(arr1[from1], arr2[from2]);
    }

    /**
     * time: O(N)
     * space: O(1)
     * <p>
     * Find the k-th largest element in the union of two sorted arrays (with duplicates).
     */
    public static int findElementByRankOptimized(int[] arr1, int[] arr2, int rank) {

        if (rank < 0) {
            throw new IllegalArgumentException("negative rank passed: " + rank);
        }

        final int maxRankValue = (arr1.length + arr2.length) - 1;

        if (rank > maxRankValue) {
            throw new IllegalArgumentException("Rank too big, rank = " + rank + ", should be in range [0, " + maxRankValue +
                    "]");
        }

        int index1 = 0;
        int index2 = 0;

        while (rank > 0) {
            if (index1 >= arr1.length) {
                ++index2;
            }
            else if (index2 >= arr2.length) {
                ++index1;
            }
            else {
                if (arr1[index1] < arr2[index2] || arr1[index1] == arr2[index2]) {
                    ++index1;
                }
                else {
                    ++index2;
                }
            }
            --rank;
        }

        if (index1 >= arr1.length) {
            return arr2[index2];
        }

        if (index2 >= arr2.length) {
            return arr1[index1];
        }

        if (arr1[index1] < arr2[index2]) {
            return arr1[index1];
        }

        return arr2[index2];
    }


    /**
     * time: O(N*lgN)
     * space: O(N)
     *
     * @param arr1
     * @param arr2
     * @param rank - rank should be in range [0, (arr.length + arr2.length) - 1]
     * @return
     */
    public static int findElementByRank(int[] arr1, int[] arr2, int rank) {

        if (rank < 0) {
            throw new IllegalArgumentException("negative rank passed: " + rank);
        }

        if (arr1.length == 0 && arr2.length == 0) {
            throw new IllegalArgumentException("Both arrays are empty");
        }

        final int maxRankValue = (arr1.length + arr2.length) - 1;

        if (rank > maxRankValue) {
            throw new IllegalArgumentException("Rank too big, rank = " + rank + ", should be in range [0, " + maxRankValue +
                    "]");
        }

        final int[] mergedSortedArr = mergeArraysToSorted(arr1, arr2);

        return mergedSortedArr[rank];
    }


    public static int[] mergeArraysToSorted(int[] arr1, int[] arr2) {
        if (arr1 == null || arr2 == null) {
            throw new IllegalArgumentException("Can't merge arrays, one of arrays or both arrays are NULL");
        }

        final int[] mergedSortedArr = combineArrays(arr1, arr2);

        Arrays.sort(mergedSortedArr);

        return mergedSortedArr;
    }

    public static int[] combineArrays(int[]... arrays) {

        int mergedLength = 0;

        for (int[] arr : arrays) {
            if (arr == null) {
                throw new IllegalArgumentException("Can't merge arrays, one array is NULL");
            }
            mergedLength += arr.length;
        }

        final int[] mergedSortedArr = new int[mergedLength];

        int copyPos = 0;
        for (int[] arr : arrays) {
            System.arraycopy(arr, 0, mergedSortedArr, copyPos, arr.length);
            copyPos += arr.length;
        }

        return mergedSortedArr;
    }


    public static int[] generateRandomSortedArray(int length, int maxValue) {
        int[] arr = generateRandomArray(length, maxValue);
        Arrays.sort(arr);
        return arr;
    }


    public static int[] generateRandomArray(int length, final int minValue, final int maxValue) {

        if (length < 0) {
            throw new IllegalArgumentException("negative 'length': " + length);
        }

        final int[] arr = new int[length];

        Arrays.parallelSetAll(arr, index -> {

            int value = RAND.nextInt();

            if (value > maxValue) {
                return value % maxValue;
            }

            return value % minValue;
        });

        return arr;
    }

    /**
     * Generate random array of some random length.
     */
    public static int[] generateRandomArrayOfRandomLength(int maxLength) {
        checkArgument(maxLength > 0);
        return generateRandomArray(1 + RAND.nextInt(maxLength));
    }


    public static int[] generateRandomArray(int length, int maxValue) {

        checkArgument(length >= 0, "'length' can't be negative, length = %s", length);
        checkArgument(maxValue > 0, "'maxValue' should be positive number, greater then '0', maxValue = %s", maxValue);

        int[] arr = new int[length];

        Arrays.parallelSetAll(arr, index -> RAND.nextInt(maxValue));

        return arr;
    }

    public static int[] generateRandomArray(int length) {
        return generateRandomArray(length, Integer.MAX_VALUE);
    }

    public static byte[] generateRandomByteArray(int length) {
        checkArgument(length >= 0, "'length' can't be negative, length = %s", length);
        byte[] arr = new byte[length];
        RAND.nextBytes(arr);
        return arr;
    }

    public static double[] generateDoubleArray(int length) {
        checkArgument(length >= 0, "'length' can't be negative, length = %s", length);
        double[] arr = new double[length];
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = RAND.nextDouble();
        }
        return arr;
    }

    public static int[] generateRandomArrayWithNegative(int length, int minValue, int maxValue) {

        int[] arr = new int[length];

        Arrays.parallelSetAll(arr, index -> {
            int randomValue = RAND.nextInt();

            if (randomValue < 0) {
                randomValue = randomValue % minValue;
            }
            else {
                randomValue = randomValue % maxValue;
            }
            return randomValue;
        });

        return arr;
    }


    public static int sum(int[] arr, Pair<Integer, Integer> indexes) {
        return sum(arr, indexes.getFirst(), indexes.getSecond());

    }

    public static int sum(int[] arr) {
        return sum(arr, 0, arr.length - 1);
    }

    public static int sum(int[] arr, int from, int to) {
        return Arrays.stream(arr).parallel().sum();
    }


    public static Pair<Integer, Integer> findMaxContinuousSumBruteforce(int[] arr) {

        int startIndexMax = 0;
        int endIndexMax = 0;

        int maxSum = 0;

        for (int i = 0; i < arr.length; i++) {

            int curSum = 0;

            for (int j = i; j < arr.length; j++) {
                curSum += arr[j];

                // from equals sum sequences choose smallest one
                if (curSum == maxSum) {
                    if (j - i < endIndexMax - startIndexMax) {
                        startIndexMax = i;
                        endIndexMax = j;
                    }
                }

                else if (curSum > maxSum) {
                    maxSum = curSum;
                    startIndexMax = i;
                    endIndexMax = j;
                }

            }
        }


        return new Pair<Integer, Integer>(startIndexMax, endIndexMax);
    }


    public static Pair<Integer, Integer> findMaxContinuousSum(int[] arr) {

        int maxEnd = 0;

        int maxSoFar = 0;
        int maxPrevSum = 0;

        for (int i = 0; i < arr.length; i++) {

            int curMaxSum = Math.max(maxPrevSum + arr[i], arr[i]);

            if (curMaxSum < 0) {
                curMaxSum = 0;
            }

            if (curMaxSum > maxSoFar) {
                maxSoFar = curMaxSum;
                maxEnd = i;
            }

            maxPrevSum = curMaxSum;
        }

        int index = maxEnd;

        while (index >= 0) {

            if (arr[index] == maxSoFar) {
                break;
            }

            maxSoFar -= arr[index];
            --index;
        }

        return new Pair<Integer, Integer>(index, maxEnd);
    }


    /**
     * @param arr - unsorted array.
     * @param k   - number of greatest elemenst to return
     * @return array of k-th greatest alaments.
     */
    public static int[] getMaxValues(int[] arr, int k) throws Exception {

        final BinaryHeap<Integer> heap = BinaryHeap.minHeap(k);

        for (int i = 0; i < arr.length; i++) {


            if (heap.size() < k) {
                heap.add(arr[i]);
            }
            else {
                if (heap.peek().compareTo(arr[i]) < 0) {
                    heap.poll(); // ignore returned value
                    heap.add(arr[i]);
                }
            }
        }

        Integer[] heapArr = heap.toArray();

        int[] res = new int[heapArr.length];

        for (int i = 0; i < heapArr.length; i++) {
            res[i] = heapArr[i];
        }

        return res;

    }

    /**
     * Rearrange an array in maximum minimum form.
     * See: http://www.geeksforgeeks.org/rearrange-array-maximum-minimum-form/
     * <p>
     * Example:
     * Input  : arr[] = {1, 2, 3, 4, 5, 6, 7}
     * Output : arr[] = {7, 1, 6, 2, 5, 3, 4}
     * <p>
     * time: O(N)
     * space: O(1)
     */
    public static void rearrangeMaxMin(int[] arr) {

        checkNotNull(arr, "Can't rearrange 'null' array");

        int length = arr.length;

        for (int i = 0; i < arr.length; i++) {

            if (arr[i] > 0) {

                if (arr[i] > 0) {
                    int prev = arr[i];
                    int pos = newPosition(i, length);

                    while (arr[pos] > 0) {
                        int temp = arr[pos];
                        arr[pos] = -prev;
                        prev = temp;
                        pos = newPosition(pos, length);
                    }
                }
            }
        }
        for (int i = 0; i < arr.length; i++) {
            arr[i] = -arr[i];
        }
    }

    private static int newPosition(int i, int length) {

        assert i >= 0 && i < length;
        assert length > 0;

        if (i < (length >> 1)) {
            return (i << 1) | 1;
        }
        return (length - 1 - i) << 1;
    }

    /**
     * Calculates Kendal tau distance between two permutations represented as 'arr' and 'arr2'.
     * <p>
     * time: O(N)
     * space: O(N)
     */
    public static int calculateDistance(int[] arr1, int[] arr2) {
        checkNotNull(arr1);
        checkNotNull(arr2);
        checkArgument(arr1.length == arr2.length);
        checkArgument(isPermutation(arr1) && isPermutation(arr2), "'arr' and/or 'arr2' is not a valid permutation");

        int distance = 0;

        Map<Integer, Integer> leftPos = new HashMap<>();

        for (int i = 0; i < arr1.length; ++i) {
            int first = arr1[i];
            int second = arr2[i];

            if (first != second) {
                distance += singleElemDistance(first, i, leftPos);
                distance += singleElemDistance(second, i, leftPos);
            }
        }

        return distance / 2;
    }

    private static boolean isPermutation(int[] arr) {
        assert arr != null;

        BitSet allValues = new BitSet(arr.length);

        for (int value : arr) {
            allValues.set(value);
        }

        return allValues.nextClearBit(0) == arr.length;
    }

    private static int singleElemDistance(int elem, int index, Map<Integer, Integer> leftPos) {
        assert index >= 0;
        assert leftPos != null;

        if (leftPos.containsKey(elem)) {
            int pos = leftPos.get(elem);
            leftPos.remove(elem);
            return index - pos;
        }

        leftPos.put(elem, index);
        return 0;
    }

    public int avg(int[] arr) {
        checkNotNull(arr);

        if (arr.length == 0) {
            return 0;
        }

        int sum = 0;

        for (int val : arr) {
            sum += val;
        }
        return sum / arr.length;
    }

}
