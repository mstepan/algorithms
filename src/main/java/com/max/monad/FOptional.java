package com.max.monad;

import java.util.function.Function;


public class FOptional<T> implements Functor<T, FOptional<?>> {

    private final T value;

    public FOptional(T value) {
        this.value = value;
    }

    public static FOptional<Integer> tryParse(String str) {
        try {
            int num = Integer.parseInt(str);
            return new FOptional<>(num);
        }
        catch (NumberFormatException ex) {
            return empty();
        }
    }

    public static <U> FOptional<U> empty() {
        return new FOptional<>(null);
    }

    public static <U> FOptional<U> of(U value) {
        return new FOptional<>(value);
    }

    @Override
    public <R> FOptional<R> map(Function<T, R> f) {

        if (value == null) {
            return empty();
        }

        return of(f.apply(value));
    }

    public T get() {
        if (value == null) {
            throw new IllegalStateException();
        }

        return value;
    }

}
