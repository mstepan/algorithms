package com.max.algs.random;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Generic weighted number generator.
 */
public class WeightedRandom extends java.util.Random {

    private static final long serialVersionUID = 4647175862561982518L;

    private static final int DENSE_WEIGHTS_THRESHOLD = 5;

    private final java.util.Random impl;

    public WeightedRandom(int[] weights) {
        checkNotNull(weights, "null 'weights' passed");
        checkWeights(weights);

        if (isDense(weights)) {
            impl = new DenseWeightedRandom(weights);
            System.out.println("DenseWeightedRandom");
        }
        else {
            impl = new SparseWeightedRandom(weights);
            System.out.println("SparseWeightedRandom");
        }
    }

    @Override
    public int nextInt() {
        return impl.nextInt();
    }

    private static boolean checkWeights(int[] weights) {
        for (int singleWeight : weights) {
            checkArgument(singleWeight > 0, "negative weight found '%s', onnly positive weights supported", singleWeight);
        }

        return true;
    }

    private static boolean isDense(int[] weights) {

        for (int singleWeight : weights) {
            if (singleWeight > DENSE_WEIGHTS_THRESHOLD) {
                return false;
            }
        }

        return true;
    }
}
