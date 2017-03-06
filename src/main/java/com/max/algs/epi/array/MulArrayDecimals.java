package com.max.algs.epi.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;


public class MulArrayDecimals {


    /**
     * Multiply two decimal numbers with most significant digit first.
     * <p>
     * N = max(first.length, second.length)
     * <p>
     * time: O(N^2)
     * space: O(2*N)
     */
    public static char[] multiply(char[] first, char[] second) {

        checkWellFormedDecimalValue(first);
        checkWellFormedDecimalValue(second);

        List<Character> res = new ArrayList<>();
        res.add('0');

        for (int i = second.length - 1, offset = 0; i >= 0 && second[i] != '-'; --i, ++offset) {

            List<Character> partialRes = multiplyForDigitWithOffset(first, second[i] - '0', offset);

            res = addNumbers(partialRes, res);
        }

        boolean negative = (first[0] == '-') ^ (second[0] == '-');
        if (negative) {
            res.add(0, '-');
        }

        return toArray(res);
    }


    private static void checkWellFormedDecimalValue(char[] value) {
        checkArgument(value != null);
        checkArgument(value.length > 0);

        int startIndex = value[0] == '-' ? 1 : 0;

        for (int i = startIndex; i < value.length; ++i) {
            if (value[i] < '0' || value[i] > '9') {
                throw new IllegalArgumentException("Not a valid decimal value: " + Arrays.toString(value));
            }
        }
    }

    /**
     * Most significant digit first.
     */
    private static List<Character> multiplyForDigitWithOffset(char[] value, int digit, int offset) {

        List<Character> res = new ArrayList<>();
        for (int i = 0; i < offset; ++i) {
            res.add('0');
        }

        int carry = 0;

        for (int i = value.length - 1; i >= 0 && value[i] != '-'; --i) {

            int singleDigitRes = (value[i] - '0') * digit + carry;

            char curDigit = (char) ('0' + (singleDigitRes % 10));

            res.add(curDigit);

            carry = singleDigitRes / 10;
        }

        if (carry != 0) {
            res.add((char) ('0' + carry));
        }

        Collections.reverse(res);

        return res;
    }

    /**
     * Add two decimal numbers with most significant digit first.
     */
    private static List<Character> addNumbers(List<Character> first, List<Character> second) {

        int carry = 0;

        int i = first.size() - 1;
        int j = second.size() - 1;

        List<Character> res = new ArrayList<>(Math.max(first.size(), second.size()) + 1);

        while (i >= 0 || j >= 0) {

            int d1 = i >= 0 ? (first.get(i--) - '0') : 0;
            int d2 = j >= 0 ? (second.get(j--) - '0') : 0;

            int curSum = d1 + d2 + carry;

            res.add((char) ('0' + (curSum % 10)));

            carry = curSum / 10;
        }

        if (carry != 0) {
            res.add((char) ('0' + carry));
        }

        Collections.reverse(res);

        return res;
    }

    private static char[] toArray(List<Character> res) {
        char[] arr = new char[res.size()];

        int index = 0;

        for (char value : res) {
            arr[index] = value;
            ++index;
        }

        return arr;
    }

}
