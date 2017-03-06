package com.max.algs.patterns.singleton;


/**
 * Singleton design pattern using ThreadLocal variables. Very similar to double-checked locking.
 */
public final class OsVariables {

    private static final Object DUMMY = new Object();
    private static final ThreadLocal<Object> threadLocal = new ThreadLocal<>();

    private static OsVariables INST;

    private OsVariables() {
        System.out.println("OsVariables instantiated");
    }

    public static OsVariables getInstance() {
        if (threadLocal.get() == null) {
            createInstance();
        }

        return INST;
    }

    private static void createInstance() {
        synchronized (OsVariables.class) {
            if (INST == null) {
                INST = new OsVariables();
            }
        }

        threadLocal.set(DUMMY);
    }

}
