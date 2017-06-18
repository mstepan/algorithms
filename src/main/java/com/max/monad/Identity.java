package com.max.monad;

import java.util.function.Function;


public class Identity<T> implements Functor<T, Identity<?>> {

    private final T value;

    public Identity(T value) {
        this.value = value;
    }

    @Override
    public <R> Identity<R> map(Function<T, R> func) {
        R res = func.apply(value);
        return new Identity<>(res);
    }

    @Override
    public String toString() {
        return String.format("value = %s of class '%s'", String.valueOf(value),
                value == null ? "null" : value.getClass().getCanonicalName());
    }
}
