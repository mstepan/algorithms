package com.max.algs.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for {@link com.max.algs.util.MathUtils}
 */
public class MathUtilsTest {


    @Test
    public void isPrime() {
        for (int i = 0; i <= 39; ++i) {
            assertTrue("value " + i + " marked as not prime, but it's actually prime", MathUtils.isPrime(primePolynom(i)));
        }

        assertFalse("value " + 40 + " marked as not prime, but it's actually prime", MathUtils.isPrime(primePolynom(40)));
        assertFalse("value " + 41 + " marked as not prime, but it's actually prime", MathUtils.isPrime(primePolynom(41)));
    }


    /**
     * This polynom generates prime numbers from 0 till 39.
     */
    private static int primePolynom(int n) {
        return n * n + n + 41;
    }


    @Test
    public void addBinary() {

        // check positive + positive
        assertEquals(12, MathUtils.addBinary(5, 7));

        // check positive + negative
        assertEquals(2, MathUtils.addBinary(-5, 7));
        assertEquals(-2, MathUtils.addBinary(5, -7));
        assertEquals(2, MathUtils.addBinary(7, -5));

        //check negative + negative
        assertEquals(-2, MathUtils.addBinary(-7, 5));

        assertEquals(-12, MathUtils.addBinary(-7, -5));
        assertEquals(-57, MathUtils.addBinary(-7, -50));

        // check with 0
        assertEquals(0, MathUtils.addBinary(0, 0));
        assertEquals(9, MathUtils.addBinary(0, 9));
        assertEquals(9, MathUtils.addBinary(9, 0));

        assertEquals(-13, MathUtils.addBinary(-13, 0));
        assertEquals(-13, MathUtils.addBinary(0, -13));

        // check power of 2
        assertEquals(0, MathUtils.addBinary(-8, 8));
        assertEquals(272, MathUtils.addBinary(16, 256));
        assertEquals(6, MathUtils.addBinary(2, 4));
        assertEquals(4, MathUtils.addBinary(2, 2));

        assertEquals(Integer.MAX_VALUE, MathUtils.addBinary(0, Integer.MAX_VALUE));
        assertEquals(Integer.MAX_VALUE, MathUtils.addBinary(Integer.MAX_VALUE, 0));

        assertEquals(Integer.MIN_VALUE, MathUtils.addBinary(0, Integer.MIN_VALUE));
        assertEquals(Integer.MIN_VALUE, MathUtils.addBinary(Integer.MIN_VALUE, 0));

        assertEquals(Integer.MAX_VALUE - 1, MathUtils.addBinary(-1, Integer.MAX_VALUE));
        assertEquals(Integer.MIN_VALUE + 1, MathUtils.addBinary(1, Integer.MIN_VALUE));

        // check overflow
        assertEquals(Integer.MIN_VALUE, MathUtils.addBinary(1, Integer.MAX_VALUE));
        assertEquals(Integer.MIN_VALUE + 9, MathUtils.addBinary(10, Integer.MAX_VALUE));

        // check underflow
        assertEquals(Integer.MAX_VALUE, MathUtils.addBinary(-1, Integer.MIN_VALUE));
        assertEquals(Integer.MAX_VALUE - 9, MathUtils.addBinary(-10, Integer.MIN_VALUE));
    }
}
