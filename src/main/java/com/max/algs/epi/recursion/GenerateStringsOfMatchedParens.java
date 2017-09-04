package com.max.algs.epi.recursion;

import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * "Elements of programming interview in java".
 * 16.6. Generate strings of matched parens.
 */
public final class GenerateStringsOfMatchedParens {


    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());


    private static List<String> generateMatchedParens(int n) {
        checkArgument(n >= 0);

        if (n == 0) {
            return Collections.emptyList();
        }

        if (n == 1) {
            return Collections.singletonList("{}");
        }

        List<String> allStrings = new ArrayList<>();

        gatherMatchedParensRec(0, 0, new char[2 * n], 0, n, allStrings);

        return allStrings;
    }

    private static void gatherMatchedParensRec(int leftCnt, int rightCnt, char[] res, int index, int n, List<String> allStrings) {

        assert leftCnt >= rightCnt : "leftCnt < rightCnt";

        assert index <= res.length;

        if (leftCnt == n && rightCnt == n) {
            allStrings.add(String.valueOf(res));
            return;
        }

        assert index < res.length;

        // append left '{'
        if (leftCnt < n) {
            res[index] = '{';
            gatherMatchedParensRec(leftCnt + 1, rightCnt, res, index + 1, n, allStrings);
        }

        // append right '}'
        if (rightCnt < leftCnt) {
            res[index] = '}';
            gatherMatchedParensRec(leftCnt, rightCnt + 1, res, index + 1, n, allStrings);
        }
    }

    private GenerateStringsOfMatchedParens() {

        int n = 3;
        List<String> allStrings = generateMatchedParens(n);

        allStrings.forEach(LOG::info);

        LOG.info("GenerateStringsOfMatchedParens done...");
    }

    public static void main(String[] args) {
        try {
            new GenerateStringsOfMatchedParens();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}
