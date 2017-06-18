package com.max.algs.compression;

public final class RleEncoder {


    private RleEncoder() {
        super();
        throw new IllegalStateException("Can't instantiate utility class");
    }


    /**
     * time: O(N)
     * space: O(N)
     */
    public static String encode(String str) {

        checkNullOrEmpty(str);
        checkAlphabetic(str, 0);

        StringBuilder buf = new StringBuilder();

        char lastCh = str.charAt(0);
        int count = 1;

        for (int i = 1; i < str.length(); i++) {

            checkAlphabetic(str, i);

            if (lastCh == str.charAt(i)) {
                ++count;
            }
            else {
                buf.append(count).append(lastCh);
                lastCh = str.charAt(i);
                count = 1;
            }
        }

        buf.append(count).append(lastCh);

        return buf.toString();
    }

    public static String decode(String encodedStr) {

        checkNullOrEmpty(encodedStr);

        if ((encodedStr.length() & 1) == 1) {
            throwIncorrectRleEncoding(encodedStr);
        }

        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < encodedStr.length(); i += 2) {

            char countCh = encodedStr.charAt(i);

            if (!Character.isDigit(countCh)) {
                throwIncorrectRleEncoding(encodedStr);
            }

            int count = countCh - '0';

            char ch = encodedStr.charAt(i + 1);

            if (!Character.isAlphabetic(ch)) {
                throwIncorrectRleEncoding(encodedStr);
            }

            while (count > 0) {
                buf.append(ch);
                --count;
            }
        }

        return buf.toString();
    }


    private static void throwIncorrectRleEncoding(String encodedStr) {
        throw new IllegalArgumentException("Incorrect RLE encoded string: '" + encodedStr + "'");
    }

    private static void checkNullOrEmpty(String str) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("NULL or empty string passed: '" + str + "'");
        }
    }

    private static void checkAlphabetic(String str, int index) {

        assert str != null;
        assert index > 0 && index < str.length();

        if (!Character.isAlphabetic(str.charAt(index))) {
            throw new IllegalArgumentException("Alphanumeric string passed: '" + str + "'");
        }
    }

}
