package com.max.algs;


import com.max.algs.util.MathUtils;

public final class AlgorithmsMain {

    private AlgorithmsMain() throws Exception {

        System.out.printf("Huffman codes: %.2f %n", (0.6 * 1 + 0.3 * 2 + 0.05 * 3 + (2 * 0.025 * 4)));

        System.out.printf("Fixed length: %.2f %n", (0.6 * 4 + 0.3 * 4 + 0.05 * 4 + (2 * 0.025 * 4)));

        double[] probabilities = {0.6, 0.3, 0.05, 0.025, 0.025};

        double res = 0.0;

        for (double p : probabilities) {
            res += p * MathUtils.log2(p);
        }

        res = -res;

        System.out.printf("entropy: %.2f %n", res);


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

