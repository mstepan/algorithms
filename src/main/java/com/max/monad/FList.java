package com.max.monad;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;


public class FList<T> implements Functor<T, FList<?>> {

    private final List<T> data;

    public FList(List<T> data) {
        checkArgument(data != null);
        this.data = new ArrayList<>(data);
    }

    @Override
    public <R> FList<R> map(Function<T, R> func) {

        List<R> newData = new ArrayList<>(data.size());

        for (T value : data) {
            newData.add(func.apply(value));
        }

        return new FList<>(newData);
    }

    public List<T> get() {
        return data;
    }
}
