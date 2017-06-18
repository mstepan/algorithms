package com.max.algs.util;

import java.util.BitSet;

public class BitUtils {


    private static final int BYTE_BOUNDARY = 256;


    private static final BitSet PARITY_CACHE = new BitSet(BYTE_BOUNDARY);


    private static final int[] REVERSE_BYTES = new int[BYTE_BOUNDARY];
    private static final int BYTE_TO_INDEX[] = {0, 1, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4};

    static {
        for (int i = 0; i < BYTE_BOUNDARY; i++) {
            PARITY_CACHE.set(i, (numberOfOnes(i) & 1) == 1);
        }

        for (int i = 0; i < REVERSE_BYTES.length; i++) {
            REVERSE_BYTES[i] = reversePart(i, Byte.SIZE);
        }

    }

    private BitUtils() {
        throw new IllegalStateException("Can't instantiate '" + BitUtils.class.getName() + "'");
    }

    /**
     * Return most significant bit index(left most) that is set to '1', zero based indexing.
     * Works for negative, positive values and zero.
     */
    public static int msb(int value) {

        if (value < 0) {
            return Integer.SIZE - 1;
        }

        if (value == 0) {
            return 0;
        }

        int res = 0;
        if ((value & 0xFF_FF_00_00) != 0) {
            res += Short.SIZE;
            value >>= Short.SIZE;
        }
        if ((value & 0x00_00_FF_00) != 0) {
            res += (Short.SIZE >> 1);
            value >>= (Short.SIZE >> 1);
        }
        if ((value & 0x00_00_00_F0) != 0) {
            res += (Short.SIZE >> 2);
            value >>= (Short.SIZE >> 2);
        }
        return res + BYTE_TO_INDEX[value] - 1;
    }


    /**
     * Round UP to power of '2'
     */
    public static int ceil2(int value) {
        value = value - 1;
        value = value | (value >> 1);
        value = value | (value >> 2);
        value = value | (value >> 4);
        value = value | (value >> 8);
        value = value | (value >> 16);
        return value + 1;
    }

    /**
     * Round DOWN to power of '2'.
     */
    public static int floor2(int value) {
        value = value | (value >> 1);
        value = value | (value >> 2);
        value = value | (value >> 4);
        value = value | (value >> 8);
        value = value | (value >> 16);
        return value - (value >> 1);
    }

    /**
     * Compute next value with the same number of bits set.
     * Given a number 'value', find next number with same number
     * of 1 bits in it’s binary representation.
     */
    public static int nextWithSameNoOfBitsSet(int value) {

        if (value == 0) {
            return 0;
        }

        int mask = 1;

        while ((mask & value) == 0) {
            mask <<= 1;
        }

        int newValue = value + mask;

        int mask2 = mask << 1;

        while ((newValue & mask2) == 0) {
            mask2 <<= 1;
        }

        mask2 -= 1;

        int bitsToSet = Integer.bitCount(value & mask2) - 1;

        int mask3 = 0;

        while (bitsToSet != 0) {
            mask3 = mask3 << 1 | 1;
            --bitsToSet;
        }

        return newValue | mask3;
    }

    public static int nextClosestWeight(int value) {

        for (int i = 0; i < Integer.SIZE - 1; i++) {
            if (((value >> i) & 1) == 1 && (value >>> (i + 1) & 1) == 0) {
                return value ^ ((1 << i) | (1 << (i + 1)));
            }
        }

        throw new IllegalArgumentException("Can't find next value with same number of bit set to '1', for: " + Integer
                .toBinaryString(value));
    }

    public static int closestWeight(int value) {

        for (int i = 0; i < Integer.SIZE - 1; i++) {
            if ((((value >>> i) & 1) ^ (value >>> (i + 1) & 1)) == 1) {
                return value ^ ((1 << i) | (1 << (i + 1)));
            }
        }

        throw new IllegalArgumentException("Can't find next value with same number of bit set to '1', for: " + value);

    }


    public static int reverseShort(int value) {
        int res = REVERSE_BYTES[value & 0xFF];
        res = (res << 8) | REVERSE_BYTES[(value >> 8) & 0xFF];
        return res;
    }

    public static int reverseInt(int value) {
        int res = reverseShort(value & 0xFFFF);
        res = (res << 16) | reverseShort((value >> 16) & 0xFFFF);
        return res;
    }

    public static long reverseLong(long value) {
        long res = reverseShort((int) (value & 0xFFFF));
        res = (res << 16) | reverseShort((int) (value >>> 16) & 0xFFFF);
        res = (res << 16) | reverseShort((int) (value >>> 32) & 0xFFFF);
        res = (res << 16) | reverseShort((int) (value >>> 48) & 0xFFFF);
        return res;
    }

    private static int reversePart(int value, int numOfBitsToReverse) {

        int res = 0;

        for (int i = 0; i < numOfBitsToReverse; i++) {
            res <<= 1;
            res |= (value >>> i) & 1;
        }

        return res;
    }


    public static int swapBits(int value, int i, int j) {

        if (((value >>> i) & 1) != ((value >>> j) & 1)) {
            return value ^ ((1 << i) | (1 << j));
        }

        return value;
    }

    /**
     * time: O(lgN)
     * space: O(1)
     */
    public static int parity(long value) {

        value ^= (value >>> 32);
        value ^= (value >>> 16);
        value ^= (value >>> 8);
        value ^= (value >>> 4);
        value ^= (value >>> 2);
        value ^= (value >>> 1);

        return (int) (value & 0x1);

    }

    public static int computeParity(int value) {

        int parity = 0;

        for (int i = 0; i < 4; i++) {
            if (PARITY_CACHE.get((value >>> i * 8) & 0xFF)) {
                parity ^= 1;
            }
        }

        return parity;
    }

    public static int hammindDistance(int value1, int value2) {
        return Integer.bitCount(value1 ^ value2);
    }

    /**
     * The same as 'Integer.bitCount'
     */
    public static int numberOfOnes(int valueToCheck) {
        int value = valueToCheck;
        value = value - ((value >> 1) & 0x55555555);
        value = (value & 0x33333333) + ((value >> 2) & 0x33333333);
        return (((value + (value >> 4)) & 0x0F0F0F0F) * 0x01010101) >> 24;
    }


    public static int copyBitsRange(int src, int dest, int from1, int to1, int from2, int to2) {

        int n = dest;
        int m = src;

		/*
        if( from1 <= 0 || from1 > 31 ){}
		
		if( from2 <= 0 || from2 > 31 ){}
		
		if( from1 > to1 ){}
		
		if( from2 > to2 ){}
		*/

        int copyBitsCount = to2 - from2 + 1;
        int mask = 0;

        while (copyBitsCount > 0) {
            mask = mask << 1 | 1;
            --copyBitsCount;
        }

        int srcMask = mask << from2;

        int extractedPart = (m & srcMask) >>> from2;

        int destMask = ~(mask << from1);

        n = n & destMask | (extractedPart << from1);

        return n;
    }

}
