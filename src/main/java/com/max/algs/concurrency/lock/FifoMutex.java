package com.max.algs.concurrency.lock;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

public class FifoMutex {
	
	private final AtomicBoolean used = new AtomicBoolean(false);
	private final Queue<Thread> waitingQueue = new ConcurrentLinkedQueue<>();
	
	public void acquire(){
		
		Thread current = Thread.currentThread();
		waitingQueue.add( current );
		
		while( waitingQueue.peek() != current || ! used.compareAndSet(false, true) ){
			LockSupport.park(this);
		}
		
		waitingQueue.remove();		
	}
	
	public void release(){
		used.set(false);
		LockSupport.unpark( waitingQueue.peek() );
	}

}
