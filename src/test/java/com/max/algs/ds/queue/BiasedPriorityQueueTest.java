package com.max.algs.ds.queue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;



public class BiasedPriorityQueueTest {
	
	
	@Test
	public void add(){
		BiasedPriorityQueue<Integer> queue = new BiasedPriorityQueue<>();
		
		assertEquals(0, queue.size());
		assertTrue(queue.isEmpty());
		
		queue.add(18);		
		assertEquals(1, queue.size());
		assertFalse(queue.isEmpty());
		
		queue.add(6);		
		assertEquals(2, queue.size());
		assertFalse(queue.isEmpty());
		
		queue.add(7);		
		assertEquals(3, queue.size());
		assertFalse(queue.isEmpty());
		
		assertEquals( new Integer(18), queue.extractMax() );			
		assertEquals(2, queue.size());
		assertFalse(queue.isEmpty());
		
		assertEquals( new Integer(7), queue.extractMax() );
		assertEquals(1, queue.size());
		assertFalse(queue.isEmpty());
		
		assertEquals( new Integer(6), queue.extractMax() );
		assertEquals(0, queue.size());
		assertTrue(queue.isEmpty());
	}

	@Test
	public void addMoreData(){

		BiasedPriorityQueue<Integer> queue = new BiasedPriorityQueue<>();
		
		queue.add(18);			
		queue.add(6);		
		queue.add(7);			
		queue.add(23);		
		queue.add(20);
		
		assertEquals( new Integer(23), queue.extractMax() );
		assertEquals( new Integer(20), queue.extractMax() );
		assertEquals( new Integer(18), queue.extractMax() );
		assertEquals( new Integer(7), queue.extractMax() );
		assertEquals( new Integer(6), queue.extractMax() );
	}
	
	
	@Test
	public void addRandomData(){

		Random rand = new Random();
		
		for( int itCount = 0; itCount < 10; itCount ++ ){
			int[] arr = new int[1000]; 
			
			BiasedPriorityQueue<Integer> queue = new BiasedPriorityQueue<>();
			
			for( int i =0; i < arr.length; i++ ){
				int value = rand.nextInt();
				arr[i] = value;
				queue.add( value );
			}
			
			assertEquals( arr.length, queue.size() );
			assertFalse( queue.isEmpty() );
			
			Arrays.sort(arr);
			
			for( int i = arr.length-1; i >= 0; i-- ){			
				assertEquals( new Integer(arr[i]), queue.extractMax() );				
			}
			
			assertEquals( 0, queue.size() );
			assertTrue( queue.isEmpty() );
		}

	}	
	
	
}
