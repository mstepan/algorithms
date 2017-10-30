package com.max.algs.hashing.universal;

public interface UniversalHashFunction<T> {

    int hash(T value);

    int getA();

    int getB();
}
