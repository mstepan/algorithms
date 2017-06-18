package com.max.algs.dstree;

import com.max.algs.ds.dstree.DigitizableInteger;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertSame;


public class DigitizableIntegerTest {


    private static void assertDigitsSame(DigitizableInteger value, int[] expectedDigits) {

        for (int i = 0; i < DigitizableInteger.NUM_OF_BINARY_DIGITS; i++) {

            int expectedDigit = i < expectedDigits.length ? expectedDigits[i] : 0;
            int actualDigit = value.binaryDigit(i);

            assertSame("Binary digit for index '" + i + "' is incorrect, actual value: " + Integer.toBinaryString(value.getValue()) +
                            ", expected digits: " + Arrays.toString(expectedDigits),
                    expectedDigit, actualDigit);
        }
    }

    @Test
    public void binaryDigit() {

        DigitizableInteger value = new DigitizableInteger(Integer.valueOf("1100", 2));

        assertSame(32, value.numOfDigits());
        assertDigitsSame(value, new int[]{0, 0, 1, 1});


        value = new DigitizableInteger(Integer.valueOf("0", 2));
        assertDigitsSame(value, new int[]{0});

        value = new DigitizableInteger(Integer.valueOf("1111", 2));
        assertDigitsSame(value, new int[]{1, 1, 1, 1});


        value = new DigitizableInteger(Integer.valueOf("10101100", 2));
        assertDigitsSame(value, new int[]{0, 0, 1, 1, 0, 1, 0, 1});

        // all [1,1,1.....1]
        value = new DigitizableInteger(-1);
        assertDigitsSame(value, new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
    }


}
