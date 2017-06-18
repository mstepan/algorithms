package com.max.algs.util;

import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Silently create object without calling constructor.
 * Used inside object deserialization code.
 */
public final class SilentObjectCreator {

    private SilentObjectCreator() {
        throw new IllegalStateException("Utility only class '" + SilentObjectCreator.class.getCanonicalName() +
                "' was instantiated.");
    }

    public static <T> T create(Class<T> clazz) {
        return create(clazz, Object.class);
    }

    public static <T> T create(Class<T> clazz, Class<? super T> parentClazz) {
        try {
            ReflectionFactory reflectionFac =
                    ReflectionFactory.getReflectionFactory();

            Constructor parentConstructor = parentClazz.getDeclaredConstructor();
            Constructor objConstructor = reflectionFac.newConstructorForSerialization(clazz, parentConstructor);

            return clazz.cast(objConstructor.newInstance());
        }
        catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            throw new IllegalStateException("Can't silently instantiate class '" + clazz + "'", ex);
        }
    }

    public static void main(String[] args) {
        try {
            MySingleton obj = SilentObjectCreator.create(MySingleton.class);
            System.out.printf("obj.value = %s %n", obj.value);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static final class MySingleton {

        private final String value;

        private MySingleton(String value) {
            this.value = checkNotNull(value);
            System.out.println("MySingleton constructor called");
        }

        public String getValue() {
            return value;
        }
    }
}
