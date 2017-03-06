package com.max.algs.concurrency;

import java.util.ArrayDeque;
import java.util.Queue;

import org.eclipse.jetty.util.component.Container;

public class PooledThread extends Thread {	
	
	
	private final Object lock = new Object();	
	private final Queue<PooledThread> idle = new ArrayDeque<>();
	
	public void assign(Container container) {
		
		  synchronized (lock) {
			  
		    PooledThread thread = null;
		    
		    do {
		    	
		      while ( idle.isEmpty() && ! Thread.currentThread().isInterrupted() ) {
		        try {
		          lock.wait();
		        } 
		        catch (InterruptedException ie) {
		          Thread.currentThread().interrupt();
		        }
		      }
		      
		      if ( !idle.isEmpty() ){
		        thread = idle.peek();
		      }
		    } 
		    while ((thread==null) || (!thread.isRunning()));
		    
		    moveToRunning(thread);
		    thread.start();
		    lock.notify();
		  }
		}
	
	private boolean isRunning(){
		return false;
	}
	
	private void moveToRunning(PooledThread thread){
		
	}

}
