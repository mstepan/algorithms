package com.max.algs;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.SynchronousQueue;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Iterator for interleaving strings.
 * <p>
 * str1 = AB
 * str2 = CD
 */
public class DelayedIterator implements Iterator<String> {

    private static final String END_MSG = "END";

    private static int globalCounter = 0;

    private String curValue;
    private final SynchronousQueue<String> res = new SynchronousQueue<>();
    private final Thread iteratorThread;

    public DelayedIterator(String str1, String str2) {
        checkNotNull(str1);
        checkNotNull(str2);

        this.iteratorThread = new Thread(() -> {
            findRec(str1, 0, str2, 0, new ArrayDeque<>());
            putNextValue(END_MSG);
        });

        iteratorThread.setName("DelayedIterator-" + globalCounter + " thread");
        iteratorThread.setDaemon(true);
        iteratorThread.start();

        ++globalCounter;

        readNextValue();
    }

    private void readNextValue() {
        try {
            curValue = res.take();
        }
        catch (InterruptedException interEx) {
            Thread.currentThread().interrupt();
        }
    }

    private void putNextValue(String value) {
        try {
            res.put(value);
        }
        catch (InterruptedException interEx) {
            Thread.currentThread().interrupt();
        }
    }

    private void findRec(String str1, int i, String str2, int j, Deque<Character> partialResult) {
        if (i >= str1.length() && j >= str2.length()) {
            putNextValue(combineResult(partialResult));
            return;
        }

        if (i < str1.length()) {
            partialResult.add(str1.charAt(i));
            findRec(str1, i + 1, str2, j, partialResult);
            partialResult.pollLast();
        }

        if (j < str2.length()) {
            partialResult.add(str2.charAt(j));
            findRec(str1, i, str2, j + 1, partialResult);
            partialResult.pollLast();
        }
    }

    private static String combineResult(Deque<Character> partialResult) {
        StringBuilder buf = new StringBuilder(partialResult.size());
        partialResult.forEach(buf::append);
        return buf.toString();
    }

    @Override
    public boolean hasNext() {
        return curValue != null;
    }

    @Override
    public String next() {

        if (curValue == null) {
            throw new NoSuchElementException();
        }

        String retValue = curValue;

        readNextValue();
        if (curValue.equals(END_MSG)) {
            curValue = null;
            iteratorThread.interrupt();
        }

        return retValue;
    }
}
