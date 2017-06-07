package com.max.algs;


import static com.google.common.base.Preconditions.checkNotNull;

public final class AlgorithmsMain {


    /**
     * Use backtracking.
     *
     * N - str.length()
     *
     * space: O(N)
     */
    public static void printAllValues(String str) {
        checkNotNull(str);

        System.out.println(str);

        char[] arr = str.toCharArray();

        for (int i = 0; i < arr.length; ++i) {

            char prev = arr[i];
            arr[i] = '1';

            printRec(new String(arr), i);

            arr[i] = prev;
        }
    }

    private static void printRec(String str, int index) {
        System.out.println(str);

        if (index == str.length() - 1) {
            return;
        }

        int count = str.charAt(index) - '0';

        if (count == 9) {
            return;
        }

        ++count;

        String newStr = str.substring(0, index) + String.valueOf(count) + str.substring(index + 2, str.length());

        printRec(newStr, index);
    }

    private AlgorithmsMain() throws Exception {

        printAllValues("ABCD");

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    public static void main(String[] args) {
        try {
            new AlgorithmsMain();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

