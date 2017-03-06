package com.max.algs.string;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mstepan on 1/14/17.
 */
public class MinCharsToRemoveToMakeAnagramm {

    /**
     * N = min(str1.length(), str2.length())
     * <p>
     * time: O(N)
     * space: O(N)
     */
    private static int minRemoveToAnagram(String str1, String str2) {

        checkNotNull(str1);
        checkNotNull(str2);

        String smaller = str1;
        String bigger = str2;

        if (str2.length() < str1.length()) {
            smaller = str2;
            bigger = str1;
        }

        final int smallerLength = smaller.length();

        // let's use LinkedHashMap for more predictable iterator
        Map<Character, Integer> chars = new LinkedHashMap<>(smallerLength);

        for (int i = 0; i < smallerLength; ++i) {
            chars.compute(smaller.charAt(i), (key, val) -> (val == null) ? 1 : val + 1);
        }

        int charsToRemove = 0;
        char ch;
        int count;

        for (int i = 0, biggerLength = bigger.length(); i < biggerLength; ++i) {
            ch = bigger.charAt(i);

            if (!chars.containsKey(ch)) {
                ++charsToRemove;
            }
            else {
                count = chars.get(ch);
                --count;

                if (count == 0) {
                    chars.remove(ch);
                }
                else {
                    chars.put(ch, count);
                }
            }
        }

        for (Map.Entry<Character, Integer> entry : chars.entrySet()) {
            charsToRemove += entry.getValue();
        }

        return charsToRemove;
    }


    /**
     * time: O(N*lgN + M*lgM)
     * space: O(N+M)
     *
     * @return
     */
    public static int minRemoveToAnagramWithSorting(String str1, String str2) {
        checkNotNull(str1);
        checkNotNull(str2);

        char[] arr1 = str1.toCharArray();
        char[] arr2 = str2.toCharArray();

        Arrays.sort(arr1);
        Arrays.sort(arr2);

        int charsToRemove = 0;

        int i = 0;
        int j = 0;

        while (i < arr1.length && j < arr2.length) {

            if (arr1[i] == arr2[j]) {
                ++i;
                ++j;
            }
            else if (arr1[i] < arr2[j]) {
                ++i;
                ++charsToRemove;
            }
            else {
                ++j;
                ++charsToRemove;
            }
        }

        if (i < arr1.length) {
            charsToRemove += (arr1.length - i);
        }
        else {
            charsToRemove += (arr2.length - j);
        }

        return charsToRemove;
    }

}
