package com.max.algs.concurrency.exchanger;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 
 * Analog of java.util.concurrent.Exchanger<V>
 * 
 */
public class CustomExchanger<V> {

	private V data;
	
	private final Lock lock = new ReentrantLock();	
	private final Condition readyToExchange = lock.newCondition();	
	
	public V exchange( V value ) throws InterruptedException {	
		
		lock.lock();
		try {			
			if( data == null ){
				
				data = value; 
				
				while( data == value ){
					readyToExchange.await();
				}
				
				V retValue = data;		
				data = null;
				
				return retValue;				
			}
			else {
				V retValue = data;
				data = value;
				readyToExchange.signalAll();
				return retValue;
			}
			
			
		}
		finally {
			lock.unlock();
		}
	}

}
