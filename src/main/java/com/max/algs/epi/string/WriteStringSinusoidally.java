package com.max.algs.epi.string;

import org.apache.log4j.Logger;

import static com.google.common.base.Preconditions.checkArgument;


public class WriteStringSinusoidally {

    private static final Logger LOG = Logger.getLogger(WriteStringSinusoidally.class);

    private WriteStringSinusoidally() throws Exception {

        System.out.println(getSinusoidalForm("Hello world!"));

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * 7.11. Write a string sinusoidally.
     * time: O(N)
     * space: O(N)
     */
    public static String getSinusoidalForm(String str) {
        checkArgument(str != null);

        if (str.length() < 2) {
            return str;
        }

        StringBuilder buf = new StringBuilder(str.length());

        appendToBuffer(str, 1, 4, buf);
        appendToBuffer(str, 0, 2, buf);
        appendToBuffer(str, 3, 4, buf);

        return buf.toString();
    }

    private static void appendToBuffer(String str, int from, int offset, StringBuilder buf) {
        for (int i = from, strLength = str.length(); i < strLength; i += offset) {
            buf.append(str.charAt(i));
        }
    }

    public static void main(String[] args) {
        try {
            new WriteStringSinusoidally();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }


}
