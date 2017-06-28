package com.max.algs.epi.string;

import org.apache.log4j.Logger;

import static com.google.common.base.Preconditions.checkArgument;


public class RLEEncoding {

    private static final Logger LOG = Logger.getLogger(RLEEncoding.class);

    private RLEEncoding() throws Exception {

        String str = "aaaabcccaae";
        System.out.println(str);

        String encoded = encode(str);
        System.out.println(encoded);

        String original = decode(encoded);
        System.out.println(original);

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * 7.12. RLE encoding.
     * <p>
     * time: O(N)
     * space: O(2*N) ~ O(N)
     */
    public static String encode(String str) {
        checkArgument(str != null, "null 'str' parameter passed");

        if (str.length() == 0) {
            return str;
        }

        StringBuilder encodedBuf = new StringBuilder(str.length());

        final int strLength = str.length();

        int cnt = 1;
        for (int i = 1; i < strLength; ++i) {
            if (str.charAt(i) != str.charAt(i - 1)) {
                encodedBuf.append(cnt).append(str.charAt(i - 1));
                cnt = 1;
            }
            else {
                ++cnt;
            }
        }

        encodedBuf.append(cnt).append(str.charAt(strLength - 1));

        return encodedBuf.toString();
    }

    /**
     * RLE decoding.
     * <p>
     * time: O(N)
     * space: O(N)
     */
    public static String decode(String encodedStr) {

        checkArgument(encodedStr != null, "null 'encodedStr' parameter passed");

        if (encodedStr.length() == 0) {
            return encodedStr;
        }

        StringBuilder decodedBuf = new StringBuilder();

        int cnt = 0;
        char ch;

        for (int i = 0, encodedStrLength = encodedStr.length(); i < encodedStrLength; ++i) {
            ch = encodedStr.charAt(i);

            if (Character.isLetter(ch)) {
                for (int k = 0; k < cnt; ++k) {
                    decodedBuf.append(ch);
                }
                cnt = 0;
            }
            else {
                cnt = 10 * cnt + (ch - '0');
            }
        }

        return decodedBuf.toString();
    }

    public static void main(String[] args) {
        try {
            new RLEEncoding();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

}
