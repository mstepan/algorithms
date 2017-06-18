package com.max.algs.hashing.universal;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class UniversalHashFunction<T> {


    private static final int BIG_PRIME = 492_876_847;
    private static final Random RAND = ThreadLocalRandom.current();

    private final int a;
    private final int b;


    public UniversalHashFunction(int a, int b) {
        this.a = a;
        this.b = b;
    }


    public static <T> UniversalHashFunction<T> generate() {
        int a = 1 + RAND.nextInt(BIG_PRIME - 1);  // 'a' in range [1; p-1]
        int b = RAND.nextInt(BIG_PRIME);          // 'b' in range [0; p-1]
        return new UniversalHashFunction<T>(a, b);
    }

    public int getA() {
        return a;
    }


    public int getB() {
        return b;
    }


    public int hash(T value) {
        return (a * value.hashCode() + b) % BIG_PRIME;
    }


    @Override
    public String toString() {
        return "a = " + a + ", b = " + b + ", prime = " + BIG_PRIME;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UniversalHashFunction<?> that = (UniversalHashFunction<?>) o;

        if (a != that.a) {
            return false;
        }
        if (b != that.b) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = a;
        result = 31 * result + b;
        return result;
    }
}
