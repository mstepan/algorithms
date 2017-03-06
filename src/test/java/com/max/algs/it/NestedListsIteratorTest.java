package com.max.algs.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Test;


public class NestedListsIteratorTest {
	
	
	
	@Test( expected = IllegalArgumentException.class )
	public void constructItFromNullList(){
		new NestedListsIterator<Integer>( null );
	}
	
	
	
	@Test( expected = NoSuchElementException.class )
	public void itThrowExceptionIfOutOfBounds(){
		Iterator<Integer> it = new NestedListsIterator<Integer>( new ArrayList<Object>() );
		assertFalse( it.hasNext() );
		it.next();
	}
	
	
	@Test( expected = UnsupportedOperationException.class )
	public void removeThrowException(){
		Iterator<Integer> it = new NestedListsIterator<Integer>( new ArrayList<Object>() );
		it.remove();
	}
	
	
	
	@Test
	public void itOverComplexList(){		
		// ( (), 1 , (()), 2, (3), (((4, 7)), 5) )
		
		
		List<Object> inner1 = new ArrayList<Object>();

		List<Object> inner2 = new ArrayList<Object>();
		inner2.add( Arrays.asList() );
		
		List<Object> inner3 = new ArrayList<Object>();
		inner3.add( 3 );
		
		List<Object> inner5 = new ArrayList<Object>();
		inner5.add( Arrays.asList(4, 7) );
		
		List<Object> inner4 = new ArrayList<Object>();
		inner4.add( inner5 );
		inner4.add( 5 );

		
		List<Object> data = new ArrayList<Object>();
		data.add( inner1 );		
		data.add(1);
		data.add( inner2 );
		data.add(2);
		data.add( inner3 );
		data.add( inner4 );
		
		Iterator<Integer> it = new NestedListsIterator<Integer>( data );
		
		assertTrue( it.hasNext() );
		assertEquals( Integer.valueOf(1), it.next() );
		
		assertTrue( it.hasNext() );
		assertEquals( Integer.valueOf(2), it.next() );
		
		assertTrue( it.hasNext() );
		assertEquals( Integer.valueOf(3), it.next() );
		
		assertTrue( it.hasNext() );
		assertEquals( Integer.valueOf(4), it.next() );
		
		assertTrue( it.hasNext() );
		assertEquals( Integer.valueOf(7), it.next() );
		
		assertTrue( it.hasNext() );
		assertEquals( Integer.valueOf(5), it.next() );
		
		assertFalse( it.hasNext() );
	}
	
	
	
	@Test
	public void itOverDeepNestedLists(){		
		// ((((5))))
		
		List<Object> inner3 = new ArrayList<Object>();
		inner3.add(5);
		
		List<Object> inner2 = new ArrayList<Object>();
		inner2.add( inner3 );
		
		List<Object> inner1 = new ArrayList<Object>();
		inner1.add( inner2 );
		
		List<Object> data = new ArrayList<Object>();		
		data.add( inner1 );
		
		Iterator<Integer> it = new NestedListsIterator<Integer>( data );
		
		assertTrue( it.hasNext() );
		assertEquals( Integer.valueOf(5), it.next() );
		
		assertFalse( it.hasNext() );
	}
	
	
	
	@Test
	public void itOverEmptyListsAndElement(){
		// (1, (), (), 3)
		
		List<Object> data = new ArrayList<Object>();
		
		data.add( 1 );
		data.add( Arrays.asList() );
		data.add( Arrays.asList() );
		data.add( 3 );
		
		Iterator<Integer> it = new NestedListsIterator<Integer>( data );
		
		assertTrue( it.hasNext() );
		assertEquals( Integer.valueOf(1), it.next() );
		
		assertTrue( it.hasNext() );
		assertEquals( Integer.valueOf(3), it.next() );
		
		assertFalse( it.hasNext() );
	}
	
	
	@Test
	public void itOverEmptyListAndElement(){
		// ((), 1)
		
		List<Object> data = new ArrayList<Object>();
		
		data.add( Arrays.asList() );
		data.add( 1 );
		
		Iterator<Integer> it = new NestedListsIterator<Integer>( data );
		
		assertTrue( it.hasNext() );
		assertEquals( Integer.valueOf(1), it.next() );
		
		assertFalse( it.hasNext() );
	}	
	
	
	@Test
	public void itOverElementAndEmptyList(){
		// (1, ())
		
		List<Object> data = new ArrayList<Object>();
		
		data.add(1);
		data.add( Arrays.asList() );
		
		Iterator<Integer> it = new NestedListsIterator<Integer>( data );
		
		assertTrue( it.hasNext() );
		assertEquals( Integer.valueOf(1), it.next() );
		
		assertFalse( it.hasNext() );
	}
	
	
	@Test
	public void iterateOverNestedList(){
		// (1, (2, 3), (4, (5, 6), 7))
		
		List<Object> data = new ArrayList<Object>();
		
		data.add(1);
		data.add( Arrays.asList(2,3) );
		
		List<Object> innerData = new ArrayList<Object>();
		innerData.add(4);
		innerData.add( Arrays.asList(5,6) );
		innerData.add(7);
		
		data.add( innerData );
		
		Iterator<Integer> it = new NestedListsIterator<Integer>( data );
		
		assertTrue( it.hasNext() );
		assertEquals( Integer.valueOf(1), it.next() );
		
		assertTrue( it.hasNext() );
		assertEquals( Integer.valueOf(2), it.next() );
		
		assertTrue( it.hasNext() );
		assertEquals( Integer.valueOf(3), it.next() );
		
		assertTrue( it.hasNext() );
		assertEquals( Integer.valueOf(4), it.next() );
		
		assertTrue( it.hasNext() );
		assertEquals( Integer.valueOf(5), it.next() );
		
		assertTrue( it.hasNext() );
		assertEquals( Integer.valueOf(6), it.next() );
		
		assertTrue( it.hasNext() );
		assertEquals( Integer.valueOf(7), it.next() );
		
		assertFalse( it.hasNext() );
		
	}

}
