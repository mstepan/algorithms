package com.max.algs.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class NumberUtilsTest {

    private static String reverse(String str) {

        char[] arr = str.toCharArray();

        int left = 0;
        int right = arr.length - 1;

        while (left < right) {

            char ch = arr[left];
            arr[left] = arr[right];
            arr[right] = ch;

            ++left;
            --right;
        }

        String res = new String(arr).replace("_", "");

        return res;
    }

    @Test
    public void isSparse() {

        assertTrue(NumberUtils.isSparse(0b1010101010));
        assertTrue(NumberUtils.isSparse(0b1));
        assertTrue(NumberUtils.isSparse(0b0));
        assertTrue(NumberUtils.isSparse(0b1000000));
        assertTrue(NumberUtils.isSparse(Integer.MIN_VALUE));
        assertTrue(NumberUtils.isSparse(Integer.MIN_VALUE + 1));

        assertFalse(NumberUtils.isSparse(0b1100));
        assertFalse(NumberUtils.isSparse(-12));
        assertFalse(NumberUtils.isSparse(-1));
        assertFalse(NumberUtils.isSparse(0b001010101100));
    }

    @Test
    public void valueLeastSignificantBitSet() {

        assertEquals(0b1, NumberUtils.valueLeastSignificantBitSet(0b011101));
        assertEquals(0b100, NumberUtils.valueLeastSignificantBitSet(0b011100));
        assertEquals(0, NumberUtils.valueLeastSignificantBitSet(0));
        assertEquals(0b1000, NumberUtils.valueLeastSignificantBitSet(0b1000));
        assertEquals(1, NumberUtils.valueLeastSignificantBitSet(Integer.MIN_VALUE));
        assertEquals(1, NumberUtils.valueLeastSignificantBitSet(Integer.MAX_VALUE));
        assertEquals(0b10, NumberUtils.valueLeastSignificantBitSet(Integer.MAX_VALUE - 1));
    }

    @Test
    public void sign() {
        assertEquals(1, NumberUtils.sign(10));
        assertEquals(-1, NumberUtils.sign(-133));
        assertEquals(1, NumberUtils.sign(0));
        assertEquals(1, NumberUtils.sign(Integer.MAX_VALUE));
        assertEquals(-1, NumberUtils.sign(Integer.MIN_VALUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void absForMinIntegerThrowsException() {
        NumberUtils.abs(Integer.MIN_VALUE);
    }

    @Test
    public void abs() {
        assertEquals(10, NumberUtils.abs(10));
        assertEquals(17, NumberUtils.abs(-17));
        assertEquals(0, NumberUtils.abs(0));
    }

    @Test
    public void canOverflow() {

        // check overflow
        assertTrue(NumberUtils.canOverflow(Integer.MAX_VALUE, 1));
        assertTrue(NumberUtils.canOverflow(Integer.MAX_VALUE / 2, Integer.MAX_VALUE / 2 + 2));
        assertFalse(NumberUtils.canOverflow(Integer.MAX_VALUE, 0));
        assertFalse(NumberUtils.canOverflow(Integer.MAX_VALUE / 2 - 5, Integer.MAX_VALUE / 2 - 3));
        assertFalse(NumberUtils.canOverflow(10, 20));
        assertFalse(NumberUtils.canOverflow(Integer.MAX_VALUE, -10));

        // check underflow
        assertTrue(NumberUtils.canOverflow(Integer.MIN_VALUE, -1));
        assertFalse(NumberUtils.canOverflow(Integer.MIN_VALUE, 0));
        assertFalse(NumberUtils.canOverflow(Integer.MIN_VALUE, 10));
        assertFalse(NumberUtils.canOverflow(Integer.MIN_VALUE, 1));
    }

    @Test
    public void reverseBits() {
        assertEquals(0b11111111000000000000000000000000, NumberUtils.reverseBits(0b11111111));
        assertEquals(0b01000010_00000000_00000000_10000001, NumberUtils.reverseBits(0b10000001_00000000_00000000_01000010));


        assertEquals(0, NumberUtils.reverseBits(0));

        assertEquals(0b00000000_00000000_00001111_00000000, NumberUtils.reverseBits(0b00000000_11110000_00000000_00000000));

    }

}
