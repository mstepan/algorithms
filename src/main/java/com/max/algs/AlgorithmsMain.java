package com.max.algs;


import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.locks.AbstractQueuedLongSynchronizer;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;


public final class AlgorithmsMain {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());


    static class SimpleLatch {

        private final Synch synch;

        public SimpleLatch(int count) {
            synch = new Synch(count);
        }

        public void await() throws InterruptedException {
            synch.acquireSharedInterruptibly(0L);
        }

        public void countDown() {
            synch.releaseShared(0L);
        }

        private static final class Synch extends AbstractQueuedLongSynchronizer {

            public Synch(int count) {
                setState(count);
            }

            @Override
            protected long tryAcquireShared(long ignored) {
                // 0 - open state
                return getState() == 0L ? 1L : -1L;
            }

            @Override
            protected boolean tryReleaseShared(long ignored) {
                while (true) {
                    long state = getState();
                    if (compareAndSetState(state, state - 1)) {
                        return true;
                    }
                }
            }
        }

    }

    static class CountingSemaphore {

        private final Synch synch;

        CountingSemaphore(int permissions) {
            this.synch = new Synch(permissions);
        }

        public void acquire() throws InterruptedException {
            synch.acquireSharedInterruptibly(0);
        }

        public void release() {
            synch.releaseShared(0);
        }

        public void printStat() {
            LOG.info(synch.getFinalState());
        }

        private static final class Synch extends AbstractQueuedSynchronizer {

            Synch(int permissions) {
                setState(permissions);
            }

            @Override
            protected int tryAcquireShared(int arg) {
                int state = getState();

                if (state > 0) {
                    int newState = state - 1;

                    if (compareAndSetState(state, newState)) {
                        return newState;
                    }
                }

                return -1;
            }

            @Override
            protected boolean tryReleaseShared(int arg) {
                while (true) {
                    int state = getState();

                    if (compareAndSetState(state, state + 1)) {
                        return true;
                    }
                }
            }

            private int getFinalState() {
                return getState();
            }
        }

    }

    static class MyUser implements java.io.Serializable {
        final String username;

        public MyUser(String username) {
            this.username = username;
        }

        @Override
        public String toString() {
            return username;
        }
    }

    private AlgorithmsMain() throws Exception {

        //-javaagent:/Users/mstepan/repo/SWAT/serial-whitelist-application-trainer/target/serial-whitelist-application-trainer-0.0.1-SNAPSHOT-jar-with-dependencies.jar

        Path path = Paths.get("/Users/mstepan/repo/algorithms/src/main/java/com/max/algs/my-user.bin");

//        try (OutputStream out = new FileOutputStream(path.toFile());
//             ObjectOutputStream objOut = new ObjectOutputStream(out)) {
//
//            objOut.writeObject(new MyUser("max"));
//        }

        try (InputStream in = new FileInputStream(path.toFile());
             ObjectInputStream objIn = new ObjectInputStream(in)) {

            MyUser user = (MyUser) objIn.readObject();

            LOG.info(user);
        }

        LOG.info("AlgorithmsMain done...");
    }


    public static void main(String[] args) {
        try {
            new AlgorithmsMain();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }


}
