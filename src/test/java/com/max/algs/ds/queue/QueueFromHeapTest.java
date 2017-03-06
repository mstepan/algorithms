package com.max.algs.ds.queue;

import static org.junit.Assert.*;

import org.junit.Test;


public class QueueFromHeapTest {
	
	@Test
	public void add(){
		QueueFromHeap queue = new QueueFromHeap();
		
		assertTrue(queue.isEmpty());
		assertEquals(0, queue.size());
		
		
		queue.add(7);
		
		assertFalse(queue.isEmpty());
		assertEquals(1, queue.size());
		assertEquals(7, queue.peek());
		
		queue.add(2);
		queue.add(5);
		queue.add(3);
		
		assertFalse(queue.isEmpty());
		assertEquals(4, queue.size());
		assertEquals(7, queue.peek());
		
		assertEquals( 7, queue.peek() );
		assertEquals( 7, queue.poll() );
		
		assertEquals( 2, queue.peek() );
		assertEquals( 2, queue.poll() );
		
		assertEquals( 5, queue.peek() );
		assertEquals( 5, queue.poll() );
		
		assertEquals( 3, queue.peek() );
		assertEquals( 3, queue.poll() );
		
		assertTrue(queue.isEmpty());
		assertEquals(0, queue.size());
		
	}

}
