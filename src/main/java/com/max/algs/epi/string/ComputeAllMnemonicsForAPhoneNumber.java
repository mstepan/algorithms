package com.max.algs.epi.string;

import org.apache.log4j.Logger;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * 7.7. Compute all menmonics for a phone number.
 */
public class ComputeAllMnemonicsForAPhoneNumber {

    private static final Logger LOG = Logger.getLogger(ComputeAllMnemonicsForAPhoneNumber.class);

    private static final char MIN_NO = '2';
    private static final char MAX_NO = '9';

    private static final char[][] SYMBOLS = new char[][]{
            null, // 0
            null, // 1
            {'A', 'B', 'C'}, // 2
            {'D', 'E', 'F'}, // 3
            {'G', 'H', 'I'}, // 4
            {'J', 'K', 'L'}, // 5
            {'M', 'N', 'O'}, // 6
            {'P', 'Q', 'R', 'S'}, // 7
            {'T', 'U', 'V'}, // 8
            {'W', 'X', 'Y', 'Z'}, // 9
    };

    private ComputeAllMnemonicsForAPhoneNumber() throws Exception {

        String phoneNumber = "2276696";

        printAllMnemonicsRecursive(phoneNumber);
        printAllMnemonicsIterative(phoneNumber);

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * N - number.length()
     * <p>
     * time: O(4^N)
     * space: O(N)
     */
    public static void printAllMnemonicsRecursive(String number) {
        checkArgument(number != null, "'number' parameter is null");

        if (number.length() == 0) {
            return;
        }

        validateNumber(number);

        char[] arr = number.toCharArray();

        printMnemonicsRecInternal(arr, 0, new char[arr.length]);
    }

    private static void validateNumber(String number) {
        char ch;
        for (int i = 0, numberLength = number.length(); i < numberLength; ++i) {
            ch = number.charAt(i);
            if (ch < MIN_NO || ch > MAX_NO) {
                throw new IllegalArgumentException("Not a valid phone number passed for mnemonics: " + number +
                        ", character '" + ch + "' not within range.");
            }
        }
    }

    private static void printMnemonicsRecInternal(char[] number, int index, char[] res) {

        if (index == number.length) {
            System.out.println(String.valueOf(res));
            return;
        }

        int digit = number[index] - '0';

        for (char ch : SYMBOLS[digit]) {
            res[index] = ch;
            printMnemonicsRecInternal(number, index + 1, res);
        }
    }

    /**
     * N - number.length()
     * <p>
     * time: O(4^N)
     * space: O(N) Space can be reduces to O(1) if we use Iterator here.
     */
    public static void printAllMnemonicsIterative(String number) {

        int[] indexes = new int[number.length()];

        char[] arr = number.toCharArray();
        reverse(arr);

        int itCount = countMnemonics(arr);

        for (int i = 0; i < itCount; ++i) {
            String mnemonic = calculate(arr, indexes);
            System.out.println(mnemonic);
            incIndexes(arr, indexes);
        }
    }

    private static void reverse(char[] arr) {
        int left = 0;
        int right = arr.length - 1;
        char temp;

        while (left < right) {
            temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
            ++left;
            --right;
        }
    }

    private static String calculate(char[] number, int[] indexes) {
        StringBuilder res = new StringBuilder(indexes.length);

        for (int i = 0; i < indexes.length; ++i) {

            int digit = number[i] - '0';
            int offset = indexes[i];

            res.append(SYMBOLS[digit][offset]);
        }

        res.reverse();

        return res.toString();
    }

    private static void incIndexes(char[] number, int[] indexes) {

        for (int i = 0; i < indexes.length; ++i) {

            ++indexes[i];

            int digit = number[i] - '0';

            if (indexes[i] == SYMBOLS[digit].length) {
                indexes[i] = 0;
            }
            else {
                break;
            }
        }

    }

    private static int countMnemonics(char[] number) {

        int mnemonics = 1;

        for (int i = 0; i < number.length; ++i) {
            int digit = number[i] - '0';
            mnemonics *= SYMBOLS[digit].length;
        }

        return mnemonics;
    }

    public static void main(String[] args) {
        try {
            new ComputeAllMnemonicsForAPhoneNumber();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

}
