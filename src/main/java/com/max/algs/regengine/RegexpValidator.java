package com.max.algs.regengine;

public final class RegexpValidator {

    private RegexpValidator() {
        super();
    }


    public static boolean isValid(String str) {

        char[] arr = str.toCharArray();

        for (int i = 1; i < arr.length; i++) {

            if (arr[i - 1] == '*' && arr[i] == '*') {
                return false;
            }

            if (arr[i - 1] == '+' && arr[i] == '+') {
                return false;
            }

            if (arr[i - 1] == '|' && !(Character.isAlphabetic(arr[i]) || Character.isDigit(arr[i]))) {
                return false;
            }
        }

        return true;
    }

}
