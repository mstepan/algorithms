package com.max.algs.epi.primitive;

import com.max.algs.AlgorithmsMain;
import org.apache.log4j.Logger;

import static org.parboiled.common.Preconditions.checkArgument;

/**
 * Find a closest integer with the same weight.
 */
public class ClosestIntegerWithSameWeight {

    private static final Logger LOG = Logger.getLogger(AlgorithmsMain.class);

    private ClosestIntegerWithSameWeight() throws Exception {
        int value = 7;
        int closestValue = closestWithSameWeight(value);

        System.out.println("value1 = " + value + ", " + Integer.toBinaryString(value));
        System.out.println("value2 = " + closestValue + ", " + Integer.toBinaryString(closestValue));

        System.out.printf("'ClosestIntegerWithSameWeight' completed. java-%s %n", System.getProperty("java.version"));
    }

    /**
     * time: O(1)
     * space: O(1)
     */
    public static int closestWithSameWeight(int value) {
        checkArgument(value != 0 && value != -1, "Can't find closest value with same weight for '%s',  " +
                        "binary representation '%s'",
                value, Integer.toBinaryString(value));

        int firstOneBit = value & ~(value - 1);

        // if LSB is `1`
        if (firstOneBit == 1) {

            // collapse all consecutive 1 bits from right side.
            // 1000111 => 1001000
            int collapsedOnes = value + firstOneBit;

            // extract right most 1 bit
            firstOneBit = collapsedOnes & ~(collapsedOnes - 1);

//            while ((value & firstOneBit) != 0) {
//                firstOneBit <<= 1;
//            }
        }

        // swap values for two consecutive different bits.
        return value ^ (firstOneBit | (firstOneBit >> 1));
    }

    public static void main(String[] args) {
        try {
            new ClosestIntegerWithSameWeight();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

}
