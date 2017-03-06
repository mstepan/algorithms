package com.max.algs.ds.queue;

import static org.junit.Assert.*;

import org.junit.Test;

public class DigitQueueTest {
	
	@Test(expected = IllegalArgumentException.class)
	public void addBigValue(){
		DigitQueue queue = new DigitQueue();
		queue.add(10);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void addSmallValue(){
		DigitQueue queue = new DigitQueue();
		queue.add(-1);
	}
	
	@Test
	public void add(){
		
		DigitQueue queue = new DigitQueue();
		assertTrue(queue.isEmpty());
		assertEquals(0, queue.size());
		
		for( int i = 0; i < 8; i++ ){
			queue.add(i);
			assertFalse(queue.isEmpty());
			assertEquals(i+1, queue.size());
		}		
		
		for( int i = 0; i < 8; i++ ){
			assertEquals( 8-i, queue.size());
			assertEquals( i, queue.poll() );
		}
		
		assertTrue(queue.isEmpty());
		assertEquals(0, queue.size());
	}
	
	@Test
	public void addAllZeros(){
		
		DigitQueue queue = new DigitQueue();
		
		for( int i =0; i < 8; i++ ){
			queue.add(0);
		}
		
		assertFalse(queue.isEmpty());
		assertEquals(8, queue.size());
		
		for( int i = 0; i < 8; i++ ){
			assertEquals( 8-i, queue.size());
			assertEquals( 0, queue.poll() );
		}
		
		assertTrue(queue.isEmpty());
		assertEquals(0, queue.size());
		
	}
		

}
