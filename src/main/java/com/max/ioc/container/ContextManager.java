package com.max.ioc.container;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

public enum ContextManager {

    INST;

    private static final Set<Class<?>> registeredComponents = new HashSet<>();

    public <T> void register(Class<T> clazz) {
        registeredComponents.add(clazz);
    }

    public <T> T create(Class<T> clazz) {

        checkComponentRegistered(clazz);

        Constructor singleConstructor = clazz.getDeclaredConstructors()[0];

        Class<?>[] parameters = singleConstructor.getParameterTypes();

        try {
            Object[] values = makeParameters(parameters);
            return (T) singleConstructor.newInstance(values);
        }
        catch (ReflectiveOperationException ex) {
            throw new IllegalStateException("Can't instantiate class of type " + clazz, ex);
        }
    }

    private Object[] makeParameters(Class<?>[] parameters) throws ReflectiveOperationException {

        Object[] values = new Object[parameters.length];

        for (int i = 0; i < parameters.length; ++i) {
            Class<?> singleParameter = parameters[i];

            checkComponentRegistered(singleParameter);

            values[i] = singleParameter.newInstance();
        }

        return values;
    }

    private void checkComponentRegistered(Class<?> clazz) {
        if (!registeredComponents.contains(clazz)) {
            throw new IllegalStateException("Unregistered class found: " + clazz.getCanonicalName());
        }
    }
}
