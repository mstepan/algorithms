package com.max.concurrent.hash;


public interface ConcurrentSet {

    boolean add(int value) throws InterruptedException;

    boolean remove(int value)throws InterruptedException;

    boolean contains(int value) throws InterruptedException;

    int size();

    boolean isEmpty();
}
