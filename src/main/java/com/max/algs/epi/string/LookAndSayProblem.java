package com.max.algs.epi.string;

import org.apache.log4j.Logger;

import static com.google.common.base.Preconditions.checkArgument;


public class LookAndSayProblem {

    private static final Logger LOG = Logger.getLogger(LookAndSayProblem.class);

    private static final String INITIAL_NUMBER = "1";

    private LookAndSayProblem() throws Exception {

        for (int i = 0; i < 11; ++i) {
            System.out.printf("i = %s, number = %s %n", i, getNthNumber(i));
        }

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * time: O(n * 2^n)
     * space: O(2^n)
     */
    public static String getNthNumber(int lines) {
        checkArgument(lines >= 0, "parameter 'n' is negative %s, should be positive or zero", lines);

        String number = INITIAL_NUMBER;

        for (int i = 1; i < lines; ++i) {
            number = nextNumber(number);
        }

        return number;
    }

    private static String nextNumber(String number) {

        assert number != null;

        int numberLength = number.length();

        int cnt = 1;
        StringBuilder res = new StringBuilder(number.length() << 1);

        char prev = number.charAt(0);
        char cur;

        for (int i = 1; i < numberLength; ++i) {

            cur = number.charAt(i);

            if (cur != prev) {
                res.append(cnt).append(prev);
                cnt = 1;
            }
            else {
                ++cnt;
            }

            prev = cur;
        }

        res.append(cnt).append(number.charAt(numberLength - 1));

        return res.toString();
    }

    public static void main(String[] args) {
        try {
            new LookAndSayProblem();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

}
