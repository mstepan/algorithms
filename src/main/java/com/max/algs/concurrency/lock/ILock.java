package com.max.algs.concurrency.lock;

public interface ILock {
	
	void lock()throws InterruptedException;
	
	void unlock();

}
