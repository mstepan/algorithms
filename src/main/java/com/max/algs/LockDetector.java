package com.max.algs;

import com.max.algs.graph.Graph;
import com.max.algs.graph.GraphUtils;
import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.lang.management.*;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by mstepan on 8/17/17.
 */
public final class LockDetector extends Thread {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());

    private static final String LOCK_DETECTOR_THREAD_NAME = "LockDetector";

    private static final Set<String> SKIP_THREADS = new HashSet<>();

    static {
        SKIP_THREADS.add("Signal Dispatcher");
        SKIP_THREADS.add("Finalizer");
        SKIP_THREADS.add("Reference Handler");
        SKIP_THREADS.add(LOCK_DETECTOR_THREAD_NAME);
    }

    public LockDetector() {
        super(LOCK_DETECTOR_THREAD_NAME);
    }

    @Override
    public void run() {

        LOG.info("LockDetector started");

        ThreadMXBean bean = ManagementFactory.getThreadMXBean();

        while (!Thread.currentThread().isInterrupted()) {

            Graph<String> lockDependencyGraph = Graph.createDirectedGraph();

            try {

                ThreadInfo[] allThreadsInfo = bean.getThreadInfo(bean.getAllThreadIds(), true, true);

                for (ThreadInfo threadInfo : allThreadsInfo) {

                    String threadName = threadInfo.getThreadName();

                    if( SKIP_THREADS.contains(threadName) ){
                        continue;
                    }

                    LockInfo waitingForLockInfo = threadInfo.getLockInfo();

                    if (waitingForLockInfo != null) {
                        lockDependencyGraph.addEdge(threadName,
                                String.valueOf(waitingForLockInfo.getIdentityHashCode()));
                    }

                    for (LockInfo lockInfo : threadInfo.getLockedSynchronizers()) {
                        lockDependencyGraph.addEdge(String.valueOf(lockInfo.getIdentityHashCode()), threadName);
                    }

                    for (MonitorInfo monitorInfo : threadInfo.getLockedMonitors()) {
                        lockDependencyGraph.addEdge(String.valueOf(monitorInfo.getIdentityHashCode()), threadName);
                    }
                }

                if (GraphUtils.hasCycle(lockDependencyGraph)) {
                    LOG.info("Cycle detected");
                }
                else {
                    LOG.info("No cycles");
                }

                TimeUnit.SECONDS.sleep(3);
            }
            catch (InterruptedException interEx) {
                Thread.currentThread().interrupt();
            }
        }

        LOG.info("LockDetector completed");
    }
}
