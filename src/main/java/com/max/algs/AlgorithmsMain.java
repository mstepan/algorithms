package com.max.algs;


import com.max.algs.util.DoubleUtils;
import com.max.algs.util.MathUtils;

import java.util.*;

public final class AlgorithmsMain {

    private static final double LN_2 = MathUtils.ln(2.0);
    private static final double LN_2_SQUARED = Math.pow(LN_2, 2.0);


    private AlgorithmsMain() throws Exception {

        float x = 0.1F;

        float res1 = 0.0F;

        for (int i = 0; i < 10; ++i) {
            res1 += x;
        }

        double res2 = x * 10;

        System.out.println(DoubleUtils.isEquals(res1, res2));

        System.out.printf("res1 = %.15f, res2 = %.15f, res3 = %.15f %n", res1, res2, x * 10);

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * See: https://stackoverflow.com/questions/658439/how-many-hash-functions-does-my-bloom-filter-need
     * <p>
     * Given:
     * <p>
     * n: how many items you expect to have in your filter (e.g. 216,553)
     * p: your acceptable false positive rate {0..1} (e.g. 0.01 → 1%) we want to calculate:
     * <p>
     * m: the number of bits needed in the bloom filter
     * k: the number of hash functions we should apply
     * The formulas:
     * <p>
     * m = -n*ln(p) / (ln(2)^2) the number of bits
     * k = m/n * ln(2) the number of hash functions
     * <p>
     * In our case:
     * <p>
     * m = -216553*ln(0.01) / (ln(2)^2) = 997263 / 0.48045 = 2,075,686 bits (253 kB)
     * k = m/n * ln(2) = 2075686/216553 * 0.693147 = 6.46 hash functions (7 hash functions)
     */
    private static void bloomFilterParameters() {

        // expected number of elements
        int n = 1000;

        // accepted false positive error rate, 3%
        double p = 0.01;

        // bits count for bloom filter
        int m = (int) Math.round(-n * MathUtils.ln(p) / LN_2_SQUARED);

        // hash functions count for bloom filter
        int k = (int) Math.round(((double) m / n) * LN_2);

        System.out.printf("m = %d, k = %d %n", m, k);

        // p = e^(-(m / n) * (ln(2)^2))
        double pActual = Math.pow(Math.E, (-(m / n) * LN_2_SQUARED));

        List<Integer> data = new ArrayList<>();

        data.addAll(Arrays.asList(1, 2, 3, 4, 5));

        Collections.sort(data);

        System.out.printf("pActual = %3f %n", pActual);
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

