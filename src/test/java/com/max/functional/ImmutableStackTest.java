package com.max.functional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Test;

public class ImmutableStackTest {
	
	
	@Test
	public void testIterator(){
		ImmutableStack<Integer> stack = new ImmutableStack<>();
		stack = stack.push(1);
		stack = stack.push(2);
		stack = stack.push(3);
		
		Iterator<Integer> it = stack.iterator();
		
		assertTrue(it.hasNext());
		assertEquals( Integer.valueOf(3), it.next() );
		
		assertTrue(it.hasNext());
		assertEquals( Integer.valueOf(2), it.next() );
		
		assertTrue(it.hasNext());
		assertEquals( Integer.valueOf(1), it.next() );
		
		assertFalse( it.hasNext() );
	}
	
	@Test(expected = NoSuchElementException.class)
	public void emptyIterator(){
		ImmutableStack<Integer> stack = new ImmutableStack<>();		
		
		Iterator<Integer> it = stack.iterator();
		
		assertFalse(it.hasNext());
		
		it.next(); // should throw exception
	}
	

	@SuppressWarnings("unchecked")
	@Test
	public void testSerialisation() throws IOException, ClassNotFoundException {

		ImmutableStack<Integer> stack = new ImmutableStack<>();

		stack = stack.push(10);
		stack = stack.push(20);
		stack = stack.push(30);
		stack = stack.push(40);

		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream objectOut = new ObjectOutputStream(byteOut);
		objectOut.writeObject(stack);

		ObjectInputStream inStream = new ObjectInputStream(
				new ByteArrayInputStream(byteOut.toByteArray()));

		ImmutableStack<Integer> newStack = (ImmutableStack<Integer>) inStream
				.readObject();

		assertNotNull(newStack);

		assertFalse(newStack.isEmpty());
		assertEquals(4, newStack.size());

		assertEquals(Integer.valueOf(40), stack.top());

		stack = stack.pop();
		assertEquals(Integer.valueOf(30), stack.top());

		stack = stack.pop();
		assertEquals(Integer.valueOf(20), stack.top());

		stack = stack.pop();
		assertEquals(Integer.valueOf(10), stack.top());

		stack = stack.pop();
		assertTrue(stack.isEmpty());
		assertEquals(0, stack.size());

	}

	@Test
	public void reverseArrayUsingStack() {
		ImmutableStack<Integer> stack = new ImmutableStack<>();

		int[] arr = new int[10];

		for (int i = 0; i < arr.length; i++) {
			arr[i] = i;
		}

		for (int i = 0; i < arr.length; i++) {
			stack = stack.push(arr[i]);
		}

		int[] newArrFromStack = new int[stack.size()];
		int index = 0;

		while (!stack.isEmpty()) {
			newArrFromStack[index] = stack.top();
			stack = stack.pop();
			++index;
		}

		reverseArray(arr);

		assertTrue(Arrays.equals(arr, newArrFromStack));
	}

	private void reverseArray(int[] arr) {
		int left = 0;
		int right = arr.length - 1;

		while (left < right) {
			int temp = arr[left];
			arr[left] = arr[right];
			arr[right] = temp;
			++left;
			--right;
		}
	}

	@Test
	public void push() {
		ImmutableStack<Integer> stack = new ImmutableStack<>();

		assertTrue(stack.isEmpty());
		assertEquals(0, stack.size());

		stack = stack.push(133);

		assertFalse(stack.isEmpty());
		assertEquals(1, stack.size());
		assertEquals(Integer.valueOf(133), stack.top());

		stack = stack.push(155);

		assertFalse(stack.isEmpty());
		assertEquals(2, stack.size());
		assertEquals(Integer.valueOf(155), stack.top());

		stack = stack.pop();

		assertFalse(stack.isEmpty());
		assertEquals(1, stack.size());
		assertEquals(Integer.valueOf(133), stack.top());

		stack = stack.pop();

		assertTrue(stack.isEmpty());
		assertEquals(0, stack.size());
	}

	@Test(expected = IllegalStateException.class)
	public void popEmptyStack() {
		new ImmutableStack<>().pop();
	}

	@Test(expected = IllegalStateException.class)
	public void topEmptyStack() {
		new ImmutableStack<>().top();
	}

}
