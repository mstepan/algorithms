package com.max.algs.concurrency.lock;

/**
 * Not reentrant lock.
 *
 */
public class SimpleLock implements ILock {

	private boolean locked = false;
	private final Object mutex = new Object();
	
	
	@Override
	public void lock() throws InterruptedException {	
		synchronized(mutex){
			
			while( locked ){
				mutex.wait();
			}
			
			locked = true;
		}
	}
	
	@Override
	public void unlock(){
		synchronized(mutex){
			locked = false;
			mutex.notifyAll();
		}
	}

}
