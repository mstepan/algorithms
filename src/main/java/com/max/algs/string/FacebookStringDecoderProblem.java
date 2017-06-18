package com.max.algs.string;

import java.util.ArrayDeque;
import java.util.Deque;

import static com.google.common.base.Preconditions.checkArgument;

public class FacebookStringDecoderProblem {

    private FacebookStringDecoderProblem() throws Exception {

        String str = "3[a2[bd]g4[ef]h]"; //"5[k]fc3[de]abc";

        System.out.println(str);

        String decodedStr = decode(str);

        String expected = "abdbdgefefefefhabdbdgefefefefhabdbdgefefefefh";

        System.out.println(decodedStr);

        if (!expected.equals(decodedStr)) {
            throw new AssertionError("Strings aren't equals");
        }

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * Write code to decode a string.
     * <p>
     * Example:
     * <p>
     * str = "3[a2[bd]g4[ef]h]"
     * decodeStr = "abdbdgefefefefhabdbdgefefefefhabdbdgefefefefh"
     * <p>
     * time: O(N * maxFrequency), where 'maxFrequency' max frequency number from initial encoded string.
     * space: O(N * maxFrequency)
     */
    private static String decode(String str) {
        checkValidEncoding(str);

        Deque<StringBuilder> stack = new ArrayDeque<>();

        int i = str.length() - 1;

        while (i >= 0) {

            char ch = str.charAt(i);

            if (ch == ']') {
                stack.push(new StringBuilder());
                --i;
            }
            else if (ch == '[') {
                --i;

                int count = 0;
                int coeff = 1;

                while (i >= 0 && Character.isDigit(str.charAt(i))) {
                    count += (coeff * (str.charAt(i) - '0'));
                    coeff *= 10;
                    --i;
                }

                duplicateTopValueOnStack(stack, (int) count);
                mergeIntoOveValueOnStack(stack);

            }
            else if (Character.isAlphabetic(ch)) {
                if (stack.isEmpty()) {
                    stack.push(new StringBuilder());
                }
                stack.peekFirst().append(ch);
                --i;
            }
        }

        return stack.pop().reverse().toString();
    }

    private static void checkValidEncoding(String str) {

        checkArgument(str != null, "null 'str' parameter passed");

        int i = str.length() - 1;
        int openBrackets = 0;

        while (i >= 0) {
            char ch = str.charAt(i);

            if (ch == ']') {
                ++openBrackets;
                --i;
            }
            else if (ch == '[') {
                --openBrackets;
                --i;

                if (openBrackets < 0) {
                    throw new IllegalArgumentException("Number of open/closed brackets is invalid in string: " + str);
                }

                long count = 0L;
                long coeff = 1L;

                while (i >= 0 && Character.isDigit(str.charAt(i))) {

                    count += (coeff * (str.charAt(i) - '0'));

                    if (count > Integer.MAX_VALUE) {
                        throw new IllegalArgumentException("Value too big: " + count);
                    }

                    coeff *= 10L;
                    --i;
                }
            }
            else if (Character.isAlphabetic(ch)) {
                --i;
            }
            else {
                throw new IllegalArgumentException("Invalid character detected '" + ch + "' in string " + str);
            }
        }
    }

    private static void duplicateTopValueOnStack(Deque<StringBuilder> stack, int count) {

        StringBuilder stackBuf = stack.peekFirst();
        String value = stackBuf.toString();

        for (int i = 0; i < count - 1; ++i) {
            stackBuf.append(value);
        }
    }

    private static void mergeIntoOveValueOnStack(Deque<StringBuilder> stack) {
        if (stack.size() != 1) {
            StringBuilder first = stack.pop();
            stack.peekFirst().append(first);
        }
    }

    public static void main(String[] args) {
        try {
            new FacebookStringDecoderProblem();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
