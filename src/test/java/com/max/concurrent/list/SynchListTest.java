package com.max.concurrent.list;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.log4j.Logger;
import org.jboss.netty.util.internal.ConcurrentHashMap;
import org.junit.Test;



public class SynchListTest {
	
	private static final Logger LOG = Logger.getLogger(SynchListTest.class);
	
	@Test
	public void add(){
		SynchList list = new SynchList();
		
		assertFalse( list.contains(133) );
		assertFalse( list.contains(0) );
		
		assertFalse( list.contains(1) );
		assertFalse( list.contains(7) );
		assertFalse( list.contains(5) );		
		
		assertTrue( list.add(1) );		
		assertTrue( list.contains(1) );		
		assertFalse( list.add(1) );	
		
		assertTrue( list.add(7) );
		assertTrue( list.add(5) );
		
		assertTrue( list.contains(1) );	
		assertTrue( list.contains(5) );	
		assertTrue( list.contains(7) );	
	}
	
	@Test 
	public void addConcurrently(){
		
		ThreadFactory threadFactory = new ThreadFactory() {
			
			@Override
			public Thread newThread(Runnable task) {
				Thread th =  new Thread(task);
				th.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
					@Override
					public void uncaughtException(Thread t, Throwable ex) {
						LOG.info("Thread-" + t.getId() + ", exception: " + ex);
					}				    
				});
				th.setDaemon(true);
				return th;
			}
		}; 
		
		for( int times = 0; times < 10; times++ ){
		
			int threadsCount = 10;
			
			final CountDownLatch allFinished = new CountDownLatch(threadsCount);
			ExecutorService pool = Executors.newFixedThreadPool(threadsCount, threadFactory);
			
			
			final Random rand = ThreadLocalRandom.current();		
			final Map<Integer, Integer> uniqueElems = new ConcurrentHashMap<>();
			
			final SynchList list = new SynchList();
			
			for( int i =0; i < threadsCount; i++ ){
				pool.submit(new Runnable(){
					@Override
					public void run() {
						try{
							for( int i =0; i < 10_000 && ! Thread.currentThread().isInterrupted(); i++ ){
								int value = rand.nextInt(1000);							
								uniqueElems.put(value, value);
								list.add(value);
								
								if( rand.nextBoolean() ){
									uniqueElems.remove(value);
									list.remove(value);
								}
							}
							
							assertTrue( 1== 2);
							
							for( Integer key : uniqueElems.keySet() ){
								assertTrue( list.contains(key) );
							}
							
						}
						finally {
							allFinished.countDown();
						}
					}				
				});
			}
			
			
			try{
				allFinished.await();
			}
			catch( InterruptedException ex ){
				Thread.currentThread().interrupt();
			}
			pool.shutdown();
			
			for( Integer key : uniqueElems.keySet() ){
				assertTrue( list.contains(key) );
			}
		
		}
		
	}

}
