package com.max.algs.util;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Range sum for array functionality.
 */
public final class RangeSumUtils {

    private static final int SMALL_ARR_LENGTH = 1;

    private RangeSumUtils() {
    }

    public static RangeSum create(int[] arr) {
        checkNotNull(arr);
        if (arr.length == 0) {
            return new RangeSumEmpty();
        }

        if (arr.length <= SMALL_ARR_LENGTH) {
            return new RangeSumSmallArray(arr);
        }

        return new RangeSumNormal(arr);
    }

    private static int readPartialData(int[] arr, int from, int to) {

        int chunkSum = 0;

        for (int i = from; i <= to; ++i) {
            chunkSum += arr[i];
        }

        return chunkSum;
    }

    private static void checkNoIntOverflow(long value) {
        if (value > Integer.MAX_VALUE || value < Integer.MIN_VALUE) {
            throw new IllegalStateException("Overflow/underflow occurred");
        }
    }

    private static int calculateRange(int[] arr, int from, int to) {
        int sum = 0;

        for (int i = from; i <= to; ++i) {
            sum += arr[i];
        }

        return sum;
    }

    interface RangeSum {
        int sum(int from, int to);

        void set(int index, int value);
    }

    /**
     * space: O(sqrt(N))
     * <p>
     * creation time: O(N)
     * <p>
     * query time: O(sqrt(N))
     * update time: O(1)
     */
    private static final class RangeSumNormal implements RangeSum {

        private final int[] arr;

        private final int blockSize;

        private final int[] chunks;

        public RangeSumNormal(int[] arr) {
            checkNotNull(arr);
            this.arr = arr;
            this.blockSize = (int) Math.ceil(Math.sqrt(arr.length));
            this.chunks = new int[arr.length / blockSize + (arr.length % blockSize == 0 ? 0 : 1)];
            fillAggregates();
        }

        private void fillAggregates() {
            int from = 0;
            int to = from + blockSize - 1;
            int chunkIndex = 0;

            while (from != arr.length) {

                long chunkSum = 0L;

                for (int i = from; i <= to; ++i) {
                    chunkSum += arr[i];
                }

                checkNoIntOverflow(chunkSum);

                chunks[chunkIndex] = (int) chunkSum;

                from = to + 1;
                to = Math.min(arr.length - 1, from + blockSize - 1);
                ++chunkIndex;
            }
        }

        /**
         * time: O(sqrt(N))
         */
        @Override
        public int sum(int from, int to) {
            checkArgument(from >= 0 && to < arr.length);
            checkArgument(from <= to);

            if (from == to) {
                return arr[from];
            }

            int fromChunkIndex = from / blockSize;
            int toChunkIndex = to / blockSize;

            // same 1 chunk
            if (fromChunkIndex == toChunkIndex) {
                return readPartialData(arr, from, to);
            }

            long sum = readFirstChunk(from, fromChunkIndex);

            sum += readMiddle(fromChunkIndex, toChunkIndex);

            sum += readLastChunk(to, toChunkIndex);

            checkNoIntOverflow(sum);

            return (int) sum;
        }

        /**
         * time: O(1)
         */
        @Override
        public void set(int index, int value) {
            int oldValue = arr[index];
            arr[index] = value;

            int chunkIndex = index / blockSize;
            chunks[chunkIndex] -= oldValue;
            chunks[chunkIndex] += value;
        }

        private int readFirstChunk(int from, int fromChunkIndex) {
            // 1-st chunk is complete
            if (from % blockSize == 0) {
                return chunks[fromChunkIndex];
            }
            // 1-st chunk is partial

            return readPartialData(arr, from, (fromChunkIndex + 1) * blockSize - 1);
        }

        private int readMiddle(int fromChunkIndex, int toChunkIndex) {
            int res = 0;
            for (int index = fromChunkIndex + 1; index < toChunkIndex; ++index) {
                res += chunks[index];
            }
            return res;
        }

        private int readLastChunk(int to, int toChunkIndex) {
            // last chunk complete
            if ((to + 1) % blockSize == 0) {
                return chunks[toChunkIndex];
            }
            // last chunk is partial
            return readPartialData(arr, toChunkIndex * blockSize, to);
        }

    }

    private static final class RangeSumEmpty implements RangeSum {
        @Override
        public int sum(int from, int to) {
            return 0;
        }

        @Override
        public void set(int index, int value) {
            throw new IndexOutOfBoundsException("Can't set new value for index = " + index + ", array is empty.");
        }
    }

    private static final class RangeSumSmallArray implements RangeSum {

        private final int[] arr;

        public RangeSumSmallArray(int[] arr) {
            this.arr = arr;
        }

        @Override
        public int sum(int from, int to) {
            return calculateRange(arr, from, to);
        }

        @Override
        public void set(int index, int value) {
            arr[index] = value;
        }
    }
}
