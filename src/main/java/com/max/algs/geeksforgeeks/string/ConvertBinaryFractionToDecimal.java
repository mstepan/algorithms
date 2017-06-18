package com.max.algs.geeksforgeeks.string;

import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

final class ConvertBinaryFractionToDecimal {

    private static final Pattern DECIMAL_FRACTION_REGEXP = Pattern.compile("^-?[10]+(\\.[10]+)?$");

    private ConvertBinaryFractionToDecimal() throws Exception {

        String str = "-101.10011";

        double res = convert(str);

        System.out.printf("res = %.5f %n", res);

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    private static void checkCorrectFormat(String str) {
        if (!DECIMAL_FRACTION_REGEXP.matcher(str).matches()) {
            throw new IllegalArgumentException("Incorrect string format detected: '" + str + "'");
        }
    }

    /**
     * Convert Binary fraction to Decimal.
     * <p>
     * Given an string of binary number n. Convert binary fractional n into it’s decimal equivalent.
     * <p>
     * Input: n = 110.101
     * Output: 6.625
     * <p>
     * Input: n = 101.1101
     * Output: 5.8125
     * <p>
     * time: O(N)
     * space: O(N)
     *
     * @see <a href="http://www.geeksforgeeks.org/convert-binary-fraction-decimal/">Convert Binary fraction to Decimal.</a>
     */
    public static double convert(String str) {
        checkNotNull(str);
        checkArgument(str.length() > 0);
        checkCorrectFormat(str);

        int sign = 1;
        int from = 0;

        if (str.charAt(0) == '-') {
            sign = -1;
            ++from;
        }

        int comma = str.indexOf('.');

        double res = realPart(str, from, (comma == -1 ? str.length() - 1 : comma - 1));

        if (comma != -1) {
            res += decimalPart(str, comma + 1, str.length() - 1);
        }

        return sign * res;
    }

    private static double realPart(String str, int from, int to) {
        double res = 0.0;

        for (int i = from; i <= to; ++i) {
            res = res * 2.0 + (str.charAt(i) == '0' ? 0.0 : 1.0);
        }

        return res;
    }

    private static double decimalPart(String str, int from, int to) {

        double res = 0.0;
        double val = 1.0 / 2.0;

        for (int i = from; i <= to; ++i) {
            if (str.charAt(i) == '1') {
                res += val;
            }

            val /= 2.0;
        }

        return res;
    }

    public static void main(String[] args) {
        try {
            new ConvertBinaryFractionToDecimal();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
