package com.max.algs.epi.string;

import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;


public class ConvertFromRomanToDecimal {

    private static final Logger LOG = Logger.getLogger(ConvertFromRomanToDecimal.class);

    private static final Map<Character, Integer> ROMAN_DIGITS = romanDigits();
    private static int[] NUMBERS = {1, 4, 5, 9, 10, 40, 50, 90, 100, 400, 500, 900, 1000};
    private static String[] SYMBOLS = {"I", "IV", "V", "IX", "X", "XL", "L", "XC", "C", "CD", "D", "CM", "M"};

    private ConvertFromRomanToDecimal() throws Exception {
        String[] romanNumbers = {
                "I",
                "VII",
                "IX",
                "IV",
                "CDM",
                "IC"
        };


        System.out.println("Roman to decimal.");

        for (String singleRomanNumber : romanNumbers) {
            System.out.printf("%s: %d %n", singleRomanNumber, toDecimal(singleRomanNumber));
        }

        System.out.println("Decimal to Roman.");
        for (int i = 1; i < 101; ++i) {
            System.out.printf("%d: %s %n", i, toRoman(i));
        }

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    private static Map<Character, Integer> romanDigits() {
        Map<Character, Integer> map = new HashMap<>();
        map.put('I', 1);
        map.put('V', 5);
        map.put('X', 10);
        map.put('L', 50);
        map.put('C', 100);
        map.put('D', 500);
        map.put('M', 1000);
        return Collections.unmodifiableMap(map);
    }

    /**
     * 7.9. Convert Roman number to decimal.
     * <p>
     * time: O(N)
     * space: O(1)
     */
    public static int toDecimal(String str) {
        checkArgument(str != null, "'str' parameter is null");
        checkArgument(str.length() > 0, "empty 'str' parameter passed");
        checkValidRomanDigits(str);

        int maxSoFar = Integer.MIN_VALUE;
        int res = 0;

        int number;

        for (int i = str.length() - 1; i >= 0; --i) {
            number = ROMAN_DIGITS.get(str.charAt(i));

            // overflow/underflow could be here, but we just ignore
            res += (number >= maxSoFar ? number : -number);
            maxSoFar = Math.max(number, maxSoFar);
        }

        return res;
    }

    private static void checkValidRomanDigits(String str) {
        for (int i = 0, strLength = str.length(); i < strLength; ++i) {
            if (!ROMAN_DIGITS.containsKey(str.charAt(i))) {
                throw new IllegalArgumentException("'" + str + "' is not a valid Roman number, unknown digit '" +
                        str.charAt(i) + "'");
            }
        }
    }

    /**
     * Convert decimal value to Roman number string.
     * <p>
     * time/space: O(Integer.MAX_VALUE / 1000) ~ 2_147_490
     * <p>
     * 2_147_490
     */
    public static String toRoman(int decimalValue) {

        checkArgument(decimalValue > 0, "Can't convert to Roman number negative value '%s'", decimalValue);

        StringBuilder res = new StringBuilder(decimalValue / 1000 + 18);
        int value = decimalValue;

        while (value != 0) {
            for (int i = NUMBERS.length - 1; i >= 0; --i) {
                if (NUMBERS[i] <= value) {
                    value -= NUMBERS[i];
                    res.append(SYMBOLS[i]);
                    break;
                }
            }
        }

        return res.toString();
    }


    public static void main(String[] args) {
        try {
            new ConvertFromRomanToDecimal();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }


}
