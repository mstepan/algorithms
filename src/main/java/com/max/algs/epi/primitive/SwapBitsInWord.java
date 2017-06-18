package com.max.algs.epi.primitive;

import static org.parboiled.common.Preconditions.checkArgument;

/**
 * Implement code that takes as input 32-bit integer
 * and swap the bits at index 'i' and 'j'.
 */
public class SwapBitsInWord {


    private static final int LSB_INDEX_INT = 0;
    private static final int MSB_INDEX_INT = Integer.SIZE - 1;

    private SwapBitsInWord() throws Exception {

        int value = 0b011001100;

        System.out.println("before: " + Integer.toBinaryString(value));

        value = swapBits(value, 1, 6);

        System.out.println("before: " + Integer.toBinaryString(value));

        System.out.printf("'SwapBitsInWord' completed. java-%s %n", System.getProperty("java.version"));
    }

    /**
     * Swap two bits values with indexes: 'i' and 'j', for an integer value.
     * <p>
     * time: O(1)
     * space: O(1)
     */
    public static int swapBits(int value, int i, int j) {
        checkArgument(i >= LSB_INDEX_INT && i <= MSB_INDEX_INT,
                "Parameter 'i' is out of correct boundary, expected [%s, %s), actual = %s",
                LSB_INDEX_INT, MSB_INDEX_INT, i);

        checkArgument(j >= LSB_INDEX_INT && j <= MSB_INDEX_INT,
                "Parameter 'j' is out of correct boundary, expected [%s, %s), actual = %s",
                LSB_INDEX_INT, MSB_INDEX_INT, j);

        // bit indexes to swap are the same
        if (i == j) {
            return value;
        }

        int iBit = (value >> i) & 0x1;
        int jBit = (value >> j) & 0x1;

        // bits values are the same
        if ((iBit ^ jBit) == 0) {
            return value;
        }

        return value ^ ((1 << i) | (1 << j));
    }

    public static void main(String[] args) {
        try {
            new SwapBitsInWord();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
