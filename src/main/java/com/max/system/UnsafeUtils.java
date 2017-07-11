package com.max.system;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.Buffer;

public final class UnsafeUtils {

    private static final int JVM_32 = 4;
    private static final int JVM_64 = 8;

    private static long byteBufferAddressFildOffset;


    private UnsafeUtils() {
        super();
    }


    @SuppressWarnings("restriction")
    public static Unsafe getUnsafe() {
        try {
            Field instanceField = Unsafe.class.getDeclaredField("theUnsafe");
            instanceField.setAccessible(true);
            Unsafe inst = (Unsafe) instanceField.get(null);
            byteBufferAddressFildOffset = inst.objectFieldOffset(Buffer.class.getDeclaredField("address"));
            return inst;

        }
        catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }


    public static long getBufferAddress(Buffer buf) {
        return getUnsafe().getLong(buf, byteBufferAddressFildOffset);
    }

    /**
     * Creates an object bypassing constructor initialization.
     * Creates object in typical java heap, so subject to normal GC.
     * <p>
     * This method can be useful when you need to skip object initialization phase or bypass security checks
     * in constructor or you want instance of that class but don't have any public constructor.
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<T> clazz) {
        try {
            return (T) getUnsafe().allocateInstance(clazz);
        }
        catch (InstantiationException ex) {
            throw new IllegalStateException("Can't allocate an instance of " + clazz + " class");
        }
    }


    public static void freeMemory(long address) {
        getUnsafe().freeMemory(address);
    }


    /**
     * Get address of reference in memory.
     * <p>
     * 1. Create generic array and put value 'obj' as 0 element.
     * 2. Get array offset.
     * 3. Depending on jvm (32/64) get address of 0 objects from array.
     */
    public static long addressOf(Object obj) {

        if (obj == null) {
            return 0L;
        }

        Unsafe unsafe = getUnsafe();

        Object[] arr = new Object[]{obj};

        long baseOffset = unsafe.arrayBaseOffset(Object[].class);
        int addressSize = unsafe.addressSize();

        switch (addressSize) {
            case JVM_32:
                return unsafe.getInt(arr, baseOffset);
            case JVM_64:
                return unsafe.getLong(arr, baseOffset);
            default:
                throw new IllegalArgumentException("Unsupported address size: " + addressSize);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getElementByAddress(long address) {

        if (address == 0L) {
            return null;
        }

        Unsafe unsafe = getUnsafe();

        Object[] arr = new Object[]{null};

        long baseOffset = unsafe.arrayBaseOffset(Object[].class);

        unsafe.putLong(arr, baseOffset, address);

        return (T) arr[0];

    }

}
