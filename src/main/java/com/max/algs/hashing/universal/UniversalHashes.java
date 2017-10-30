package com.max.algs.hashing.universal;

public final class UniversalHashes {

    private UniversalHashes() {
        throw new IllegalStateException("Can't instantiate utility only class");
    }

    public static <T> UniversalHashFunction<T> generate() {
        return UniversalHashFunctionImpl.generate();
    }
}
