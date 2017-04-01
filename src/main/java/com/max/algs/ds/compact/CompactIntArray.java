package com.max.algs.ds.compact;


import com.max.algs.util.MathUtils;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class CompactIntArray {

    // 'W'
    private static final int WORD_SIZE = 32;

    // Mask for 5 bits = 0...000_11111
    private final int oneMask;

    // 'L'
    private final int bitsPerElem;

    // 'N'
    private final int logicalLength;

    private final int[] arr;

    public static CompactIntArray create(int[] arr) {

        checkNotNull(arr);

        int maxValue = Integer.MIN_VALUE;

        for (int val : arr) {
            if (val > maxValue) {
                maxValue = val;
            }
        }

        CompactIntArray compactArr = new CompactIntArray(arr.length, maxValue);

        for (int i = 0; i < arr.length; ++i) {
            compactArr.set(i, arr[i]);
        }

        return compactArr;
    }

    public CompactIntArray(int length, int maxValue) {
        logicalLength = length;
        bitsPerElem = (int) Math.ceil(MathUtils.log2(maxValue));
        oneMask = (1 << bitsPerElem) - 1;
        arr = new int[calculateArrayLength(length, bitsPerElem)];
    }

    private static int calculateArrayLength(int expectedLength, int bitsPerElem) {
        return (int) Math.ceil(((double) (expectedLength * bitsPerElem)) / WORD_SIZE);
    }

    public int get(int i) {

        checkBoundary(i);

        if( isSpawnBoundary(i) ){
            int x = 100;
        }

        int compactedValue = arr[bucket(i)];

        int value = (compactedValue >> shift(i)) & oneMask;

        //TODO: get elements on boundaries

        return value;
    }

    private boolean isSpawnBoundary(int i){

        int startBucket = (i * bitsPerElem) / WORD_SIZE;
        int endBucket = (((i+1) * bitsPerElem) - 1) / WORD_SIZE;

        return startBucket != endBucket;
    }



    public void set(int i, int value) {

        checkBoundary(i);

        clear(i);

        int shift = shift(i);

        int maskedValue = value << shift;

        String str = Integer.toBinaryString(maskedValue);

        arr[bucket(i)] |= maskedValue;
    }

    public int length() {
        return logicalLength;
    }

    private void clear(int i) {
        int shiftedValue = get(i) << shift(i);

        arr[bucket(i)] ^= shiftedValue;
    }

    private int bucket(int i) {
        return (i * bitsPerElem) / WORD_SIZE;
    }

    private int shift(int i) {
        return WORD_SIZE - (i + 1) * bitsPerElem;
    }

    private void checkBoundary(int i) {
        if (i < 0 || i > logicalLength) {
            throw new IndexOutOfBoundsException("Can't set value at index = " + i);
        }
    }

}

/*




 */