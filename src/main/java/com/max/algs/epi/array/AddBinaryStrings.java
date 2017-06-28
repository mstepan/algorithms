package com.max.algs.epi.array;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import static com.google.common.base.Preconditions.checkArgument;
import static org.junit.Assert.assertEquals;


public class AddBinaryStrings {

    private static final Logger LOG = Logger.getLogger(AddBinaryStrings.class);

    private static char ZERO_CHAR = '0';
    private static char ONE_CHAR = '1';

    /**
     * time: O(N)
     * space: O(N)
     */
    public static String addBinaryStrings(String s, String t) {

        checkBinaryString(s);
        checkBinaryString(t);

        StringBuilder res = new StringBuilder(Math.max(s.length(), t.length()));

        int carry = 0;
        int i = s.length() - 1;
        int j = t.length() - 1;
        int digitsSum, d1, d2;

        while (i >= 0 || j >= 0) {

            d1 = i < 0 ? 0 : s.charAt(i) - ZERO_CHAR;
            d2 = j < 0 ? 0 : t.charAt(j) - ZERO_CHAR;

            digitsSum = d1 + d2 + carry;
            --i;
            --j;

            res.append(digitsSum & 1);

            carry = digitsSum >> 1;
        }

        if (carry != 0) {
            res.append(ONE_CHAR);
        }

        res.reverse();

        return res.toString();
    }

    private static void checkBinaryString(String str) {
        checkArgument(str != null, "null 'str' passed");
        checkArgument(str.length() > 0, "0 length string passed");

        char ch;
        for (int i = 0, strLength = str.length(); i < strLength; ++i) {

            ch = str.charAt(i);

            if (ch != ZERO_CHAR && ch != ONE_CHAR) {
                throw new IllegalArgumentException("Not a binary string: " + str);
            }
        }
    }

    public static void main(String[] args) {
        try {
            JUnitCore junit = new JUnitCore();
            Result result = junit.run(AddBinaryStrings.class);

            for (Failure failure : result.getFailures()) {
                System.out.println(failure.getTrace());
            }
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    @Test
    public void addBinaryStrings() {
        assertEquals("10011", addBinaryStrings("0110", "1101"));
    }

}
