package com.max.algs.concurrency.lock;


/**
 * Reentrant lock.
 *
 */
public class ReentrantLock implements ILock {

	private final java.util.concurrent.locks.Lock lock = new java.util.concurrent.locks.ReentrantLock();
	
	private final java.util.concurrent.locks.Condition notFree = lock.newCondition();
	
	private long owner = -1L;
	private int holdCount = 0;
	
	
	@Override
	public void lock() throws InterruptedException {	
		
		long curThread = Thread.currentThread().getId();
		
		lock.lock();
		
		try{
			if( owner == curThread){
				++holdCount;
				return;
			}
			
			while( holdCount != 0 ){
				notFree.await();
			}

			owner = curThread;
			holdCount = 1;
			
		}
		finally {
			lock.unlock();
		}

	}
	
	@Override
	public void unlock(){
		
		lock.lock();
		
		try{
			if( holdCount == 0 || owner != Thread.currentThread().getId() ){
				throw new IllegalMonitorStateException();
			}
			
			--holdCount;
			
			if( holdCount == 0 ){
				notFree.signalAll();
			}
		}
		finally {
			lock.unlock();
		}

	}

}
