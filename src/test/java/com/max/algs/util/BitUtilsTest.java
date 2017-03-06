package com.max.algs.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class BitUtilsTest {


    @Test
    public void msb() {
        final Random rand = new Random();

        for (int i = 0; i < 1_000_000; i++) {

            int value = rand.nextInt();
            String binaryValue = Integer.toBinaryString(value);

            int bitIndex = BitUtils.msb(value);

            assertEquals(binaryValue.length() - 1, bitIndex);
        }
    }

    @Test
    public void nextWithSameNoOfBitsSet() {
        assertEquals(0, BitUtils.nextWithSameNoOfBitsSet(0));
        assertNextWithSameNoOfBitsSet(1);
        assertNextWithSameNoOfBitsSet(2);
        assertNextWithSameNoOfBitsSet(3);
        assertNextWithSameNoOfBitsSet(4);
        assertNextWithSameNoOfBitsSet(5);
    }


    private void assertNextWithSameNoOfBitsSet(int bitsSetCount) {

        final int maxValue = 257;

        List<Integer> expected = new ArrayList<>();

        for (int i = 0; i < maxValue; i++) {
            if (Integer.bitCount(i) == bitsSetCount) {
                expected.add(i);
            }
        }

        List<Integer> actual = new ArrayList<>();

        int bitsToShift = bitsSetCount;
        int value = 0;

        while (bitsToShift != 0) {
            value = value << 1 | 1;
            --bitsToShift;
        }

        while (value < maxValue) {
            actual.add(value);
            value = BitUtils.nextWithSameNoOfBitsSet(value);
        }

        assertEquals(expected, actual);
    }

}
