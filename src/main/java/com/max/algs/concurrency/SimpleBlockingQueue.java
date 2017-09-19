package com.max.algs.concurrency;

import java.util.LinkedList;
import java.util.List;


public final class SimpleBlockingQueue<T> {

    private final List<T> list = new LinkedList<>();

    public void add(T value) {
        synchronized (list) {
            list.add(value);
            list.notifyAll();
        }
    }

    public T poll() throws InterruptedException {
        while (true) {
            synchronized (list) {
                if (list.isEmpty()) {
                    list.wait();
                }
                else {
                    return list.remove(0);
                }
            }
        }
    }

    public int size() {
        synchronized (list) {
            return list.size();
        }
    }

}
