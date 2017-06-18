package com.max.algs.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class GrayCodesUtils {


    private static final List<Integer> ZERO_ONE_LIST = new ArrayList<>();


    static {
        ZERO_ONE_LIST.add(0);
        ZERO_ONE_LIST.add(1);
    }


    private GrayCodesUtils() {
        super();
    }

    /**
     * Generate reflected binary Gray codes.
     */
    public static List<Integer> generateGrayCodes(int numOfBits) {

        if (numOfBits == 1) {
            return ZERO_ONE_LIST;
        }

        List<Integer> partCodes = generateGrayCodes(numOfBits - 1);

        List<Integer> allCodes = new ArrayList<>(partCodes);

        Collections.reverse(partCodes);

        final int mask = 1 << (numOfBits - 1);

        for (int value : partCodes) {
            allCodes.add(value | mask);
        }

        return allCodes;
    }

}
