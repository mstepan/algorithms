package com.max.algs.offheap;

import com.max.system.UnsafeUtils;
import sun.misc.Unsafe;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.HashSet;
import java.util.Set;

/**
 * Off-heap cleaner daemon thread.
 */
public class OffHeapCleanerThread extends Thread {

    private static final ReferenceQueue REFERENCE_QUEUE = new ReferenceQueue();
    private static final Set<OffHeapCleaner<?>> CLEANER_REFERENCES = new HashSet<>();

    private final Unsafe unsafe;

    public OffHeapCleanerThread() {
        this.unsafe = UnsafeUtils.getUnsafe();
        setDaemon(true);
    }

    public static <T> void register(T ref, long address) {
        CLEANER_REFERENCES.add(new OffHeapCleaner<>(ref, REFERENCE_QUEUE, address));
    }

    @Override
    public void run() {
        System.out.println("OhhHeapCleanerThread started");

        while (!Thread.currentThread().isInterrupted()) {
            try {
                OffHeapCleaner cleaner = (OffHeapCleaner) REFERENCE_QUEUE.remove();

                if (cleaner != null) {
                    System.out.println("========> Cleaning up memory at address: " + Long.toHexString(cleaner.getAddress()));
                    unsafe.freeMemory(cleaner.getAddress());
                    CLEANER_REFERENCES.remove(cleaner);
                }
            }
            catch (InterruptedException interEx) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("OhhHeapCleanerThread ended");
    }

    /**
     * Off-heap cleaner. Just a PhantomReference object which contains address that need to be cleaned up.
     */
    private static final class OffHeapCleaner<T> extends PhantomReference<T> {

        private final long address;

        OffHeapCleaner(T referent, ReferenceQueue<T> refQueue, long address) {
            super(referent, refQueue);
            this.address = address;
        }

        public long getAddress() {
            return address;
        }
    }

}
