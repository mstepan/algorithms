package com.max.algs.dynamic;

import java.math.BigInteger;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Max subarray product problem
 */
public final class MaxSubarrayProduct {

    private MaxSubarrayProduct() {
        throw new IllegalStateException("Can't create non instantiable class '" + MaxSubarrayProduct.class.getName() + "'");
    }

    /**
     * Bruteforce solution.
     * <p>
     * time: O(N^2)
     * space: O(1)
     */
    public static BigInteger maxSubArrayProductBruteforce(int[] arr) {

        checkNotNull(arr);

        BigInteger maxSoFar = BigInteger.valueOf(Integer.MIN_VALUE);

        for (int i = 0; i < arr.length - 1; i++) {

            BigInteger curProd = BigInteger.valueOf(arr[i]);

            maxSoFar = maxBig(curProd, maxSoFar);

            for (int j = i + 1; j < arr.length; j++) {
                curProd = curProd.multiply(BigInteger.valueOf(arr[j]));
                maxSoFar = maxBig(curProd, maxSoFar);
            }
        }

        return maxSoFar;
    }

    /**
     * Dynamic programming solution.
     * <p>
     * time: O(N)
     * space: O(1)
     */
    public static BigInteger maxSubArrayProduct(int[] arr) {

        checkNotNull(arr);

        BigInteger minPrefix = BigInteger.ONE;
        BigInteger maxPrefix = BigInteger.ONE;
        BigInteger maxSoFar = BigInteger.valueOf(Integer.MIN_VALUE);

        for (int baseValue : arr) {

            BigInteger val = BigInteger.valueOf(baseValue);

            if (val.compareTo(BigInteger.ZERO) == 0) {
                minPrefix = BigInteger.ONE;
                maxPrefix = BigInteger.ONE;
            }
            else if (val.compareTo(BigInteger.ZERO) > 0) {
                BigInteger curPrefixMax = maxBig(maxPrefix.multiply(val), val);
                maxSoFar = maxBig(maxSoFar, curPrefixMax);
                maxPrefix = curPrefixMax;
                minPrefix = minBig(minPrefix.multiply(val), val);
            }
            else { // val < 0
                BigInteger curPrefixMax = maxBig(minPrefix.multiply(val), val);
                maxSoFar = maxBig(maxSoFar, curPrefixMax);

                BigInteger maxPrefixPrev = maxPrefix;
                maxPrefix = curPrefixMax;
                minPrefix = minBig(maxPrefixPrev.multiply(val), val);
            }
        }

        return maxSoFar;
    }

    private static void checkNotNull(int[] arr) {
        checkArgument(arr != null, "null 'arr' parameter passed");
    }

    private static BigInteger maxBig(BigInteger first, BigInteger second) {
        return first.compareTo(second) > 0 ? first : second;
    }

    private static BigInteger minBig(BigInteger first, BigInteger second) {
        return first.compareTo(second) < 0 ? first : second;
    }

}
