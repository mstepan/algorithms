package com.max.algs;


import com.max.algs.util.MathUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class AlgorithmsMain {

    private static final double LN_2 = MathUtils.ln(2.0);
    private static final double LN_2_SQUARED = Math.pow(LN_2, 2.0);


    /*

./AgentInstall.sh AGENT_TYPE=cloud_agent AGENT_BASE_DIR=/Users/mstepan/opower/omc_agent AGENT_REGISTRATION_KEY=RDXYB1gURiuw71BkVwszMo59uZ AGENT_PROPERTIES=agent.properties

     */

    private AlgorithmsMain() throws Exception {

        int success = 3;
        int neg = 1;

        double offset = 1.0 / (success + neg);

        double odds = (success * offset) / (neg * offset);

        System.out.printf("odds = %.2f, log odds = %.2f %n", odds, Math.log10(odds));

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

        data.addAll(Arrays.asList(1,2,3,4,5));

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

