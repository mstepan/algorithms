package com.max.algs.string;

import java.math.BigInteger;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;


public class PalindromeSubstringQuery {

    private static final BigInteger HASH_BASE = BigInteger.valueOf(37);

    private final BigInteger[] prefix;
    private final BigInteger[] suffix;

    public PalindromeSubstringQuery(String str) {
        checkNotNull(str);
        final char[] arr = str.toCharArray();
        this.prefix = calculatePrefix(arr);
        this.suffix = calculateSuffix(arr);
    }

    private static BigInteger[] calculatePrefix(char[] arr) {
        BigInteger[] prefix = new BigInteger[arr.length];
        prefix[0] = BigInteger.valueOf(arr[0]);

        for (int i = 1; i < prefix.length; ++i) {
            prefix[i] = prefix[i - 1].multiply(HASH_BASE).add(BigInteger.valueOf(arr[i]));
        }
        return prefix;
    }

    private static BigInteger[] calculateSuffix(char[] arr) {
        BigInteger[] suffix = new BigInteger[arr.length];
        suffix[suffix.length - 1] = BigInteger.valueOf(arr[arr.length - 1]);

        for (int i = suffix.length - 2; i >= 0; --i) {
            suffix[i] = suffix[i + 1].multiply(HASH_BASE).add(BigInteger.valueOf(arr[i]));
        }
        return suffix;
    }

    private static BigInteger toPower(BigInteger base, int power) {
        BigInteger basePowerValue = BigInteger.ONE;

        for (int i = 0; i < power; ++i) {
            basePowerValue = basePowerValue.multiply(base);
        }
        return basePowerValue;
    }

    private static BigInteger hashForRangeFromPrefixArray(BigInteger[] prefix, int from, int to) {

        if (from == 0) {
            return prefix[to];
        }

        return prefix[to].subtract(prefix[from - 1].multiply(toPower(HASH_BASE, to - from + 1)));
    }

    private static BigInteger hashForRangeFromSuffixArray(BigInteger[] suffix, int from, int to) {

        if (to == suffix.length - 1) {
            return suffix[from];
        }

        return suffix[from].subtract(suffix[to + 1].multiply(toPower(HASH_BASE, to - from + 1)));
    }

    public boolean isPalindrome(int from, int to) {

        checkRange(from, to);

        BigInteger prefixHash = hashForRangeFromPrefixArray(prefix, from, to);
        BigInteger suffixHash = hashForRangeFromSuffixArray(suffix, from, to);

        return prefixHash.equals(suffixHash);
    }

    private void checkRange(int from, int to) {
        checkArgument(from >= 0 && to < prefix.length && from <= to);
    }

}
