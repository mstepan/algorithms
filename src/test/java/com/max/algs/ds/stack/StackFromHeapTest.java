package com.max.algs.ds.stack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StackFromHeapTest {

	@Test
	public void push() {
		StackFromHeap stack = new StackFromHeap();

		assertTrue(stack.isEmpty());
		assertEquals(0, stack.size());

		stack.push(3);

		assertFalse(stack.isEmpty());
		assertEquals(1, stack.size());
		assertEquals(3, stack.top());

		stack.push(5);
		stack.push(2);
		stack.push(7);

		assertFalse(stack.isEmpty());
		assertEquals(4, stack.size());
		assertEquals(7, stack.top());

		// pop => 7
		assertEquals(7, stack.pop());
		assertFalse(stack.isEmpty());
		assertEquals(3, stack.size());
		assertEquals(2, stack.top());

		// pop => 2
		assertEquals(2, stack.pop());
		assertFalse(stack.isEmpty());
		assertEquals(2, stack.size());
		assertEquals(5, stack.top());

		// pop => 5
		assertEquals(5, stack.pop());
		assertFalse(stack.isEmpty());
		assertEquals(1, stack.size());
		assertEquals(3, stack.top());

		// pop => 3
		assertEquals(3, stack.pop());
		assertTrue(stack.isEmpty());
		assertEquals(0, stack.size());
	}

}
