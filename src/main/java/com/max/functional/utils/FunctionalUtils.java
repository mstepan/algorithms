package com.max.functional.utils;


import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class FunctionalUtils {

    private FunctionalUtils() {
    }

    /**
     * Reduce binary function (x, y) to unary (x) -> (x, param)
     */
    public static <T> UnaryOperator<T> createCarrier(BinaryOperator<T> binaryFunc, T param) {
        Supplier<UnaryOperator<T>> carrier = () -> y -> binaryFunc.apply(param, y);
        return carrier.get();
    }

    /**
     * Reduce unary function (x) to 0 arguments function () -> (param)
     */
    public static <T> Supplier<T> createCarrier(UnaryOperator<T> func, T param) {

        return () -> func.apply(param);
    }

}
