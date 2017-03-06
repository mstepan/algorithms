package com.max.algs.compression;

import java.math.BigDecimal;


public final class CompressionUtils {

    private static final int ENTROPY_SCALE = 3;

    private CompressionUtils(){
        throw new IllegalStateException("Can't instantiate utility class '" + CompressionUtils.class.getName() + "'");
    }

    /**
     * H = -[0.5 * lg(0.5) + 0.4*lg(0.4) + 0.1*lg(0.1)]
     */
    public static BigDecimal calculateEntropy(double... probabilities) {
        BigDecimal res = BigDecimal.ZERO;

        for (double singleProbability : probabilities) {

            BigDecimal singleEntropy =
                    BigDecimal.valueOf(singleProbability).multiply(BigDecimal.valueOf(lg2(singleProbability)));

            res = res.add(singleEntropy);
        }

        return res.negate().setScale(ENTROPY_SCALE, BigDecimal.ROUND_HALF_UP);
    }

    private static double lg2(double value) {
        return Math.log10(value) / Math.log10(2.0);
    }
}
