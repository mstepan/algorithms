package com.max.algs.epi.recursion;


import com.max.algs.string.StringUtils;
import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.ArrayDeque;
import java.util.Deque;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Generate all possible palindromic decompositions of a string.
 */
public final class GeneratePalindromicDecompositionsOfString {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());


    /**
     * time: O(2^N * N)
     * space: O(N)
     */
    private static void generateAllPalindromicDecompositions(String str) {
        checkNotNull(str);

        // empty string or single character, just print it
        if (str.length() < 2) {
            LOG.info(str);
            return;
        }

        assert str.length() > 1;

        generateDecompositionsRec(str.toCharArray(), 0, new ArrayDeque<>());
    }

    private static void generateDecompositionsRec(char[] arr, int index, Deque<String> partialSol) {

        assert index >= 0 && index <= arr.length;

        if (index == arr.length) {

            assert !partialSol.isEmpty();

            LOG.info(partialSol);
            return;
        }

        assert index < arr.length;

        StringBuilder buf = new StringBuilder(arr.length);
        for (int i = index; i < arr.length; ++i) {
            buf.append(arr[i]);

            assert buf.length() > 0;

            if (StringUtils.isPalindrome(buf)) {
                partialSol.add(buf.toString());
                generateDecompositionsRec(arr, i + 1, partialSol);
                partialSol.pollLast();
            }
        }
    }


    private GeneratePalindromicDecompositionsOfString() {

        String str = "0204451881";

        generateAllPalindromicDecompositions(str);

        LOG.info("GeneratePalindromicDecompositionsOfString done...");
    }

    public static void main(String[] args) {
        try {
            new GeneratePalindromicDecompositionsOfString();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}
