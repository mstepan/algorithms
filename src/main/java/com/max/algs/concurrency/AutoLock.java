package com.max.algs.concurrency;

import java.util.concurrent.locks.Lock;

public final class AutoLock implements AutoCloseable {
	
	
	private final Lock mutex;
	
	
	public static AutoLock lock(Lock mutex){
		return new AutoLock(mutex);
	}
	
	public AutoLock(final Lock mutex){
		this.mutex = mutex;
		mutex.lock();
	}
	
	@Override
	public void close(){
		mutex.unlock();
	}

}
