package com.max.algs.epi.recursion;

import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import static com.google.common.base.Preconditions.checkArgument;


public final class GrayCodesAll {


    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Generate all Gray codes of length 'K'.
     * <p>
     * K - number of bits.
     * <p>
     * time: O(2^k)
     * space: O(2^k)
     */
    private static List<Integer> grayCodes(int numBits) {
        checkArgument(numBits >= 0, "'numBits' is negative");
        checkArgument(numBits < Integer.SIZE, "'numBits' > %s", Integer.SIZE);
        checkMemoryConstraints(numBits);

        if (numBits == 0) {
            return Collections.emptyList();
        }

        List<Integer> allCodes = new ArrayList<>(1 << numBits);
        allCodes.add(0);
        allCodes.add(1);

        for (int bitIndex = 1; bitIndex < numBits; ++bitIndex) {
            List<Integer> secondHalf = new ArrayList<>(allCodes.size());

            int mask = 1 << bitIndex;

            ListIterator<Integer> allCodesRevIt = allCodes.listIterator(allCodes.size());

            while (allCodesRevIt.hasPrevious()) {
                int code = allCodesRevIt.previous();

                assert mask > code;

                secondHalf.add(code | mask);
            }

            assert allCodes.size() == secondHalf.size();

            allCodes.addAll(secondHalf);
        }

        return allCodes;
    }

    private static void checkMemoryConstraints(int numBits) {

        long freeMemory = Runtime.getRuntime().freeMemory();

        checkArgument((1L << numBits) * Integer.BYTES <= freeMemory,
                "Not enough memory to generate Gray codes (OutOfMemoryError will be thrown)");
    }

    private GrayCodesAll() {

        int bitsLength = 5;

        grayCodes(bitsLength).forEach(val -> LOG.info(Integer.toBinaryString(val)));

        LOG.info("GrayCodesAll done...");
    }


    public static void main(String[] args) {
        try {
            new GrayCodesAll();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}
