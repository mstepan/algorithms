package com.max.algs.util;

public final class GeneralUtils {


    private GeneralUtils() {
        super();
    }


    public static <T> void checkForNull(T value) {
        if (value == null) {
            throw new IllegalArgumentException("NULL value detected");
        }
    }

}
