package com.max.algs.dstree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.max.algs.ds.dstree.DSTree;
import com.max.algs.ds.dstree.DigitizableInteger;

public class DSTreeTest {
	
	
	@Test
	public void replaceValue(){
		
		DSTree<DigitizableInteger, Integer> tree = new DSTree<DigitizableInteger, Integer>();	
				
		tree.put( DigitizableInteger.ZERO, 0 );		
		tree.put( new DigitizableInteger(4), 4 );			
		
		tree.put( DigitizableInteger.ZERO, 133 );
		
		assertEquals( Integer.valueOf(133), tree.get(DigitizableInteger.ZERO) );
	}
	
	
	@Test
	public void addWorstCase(){
		
		DSTree<DigitizableInteger, Integer> tree = new DSTree<DigitizableInteger, Integer>();	
				
		tree.put(new DigitizableInteger(0), 0);		
		int expectedSize = 1;
		
		int mask = 1;
		
		for( int i =0; i < 31; i++ ){
			assertSame( null, tree.put(new DigitizableInteger(mask), mask) );
			mask = (mask << 1) | 1;
			++expectedSize;
		}
		
		assertSame( expectedSize, tree.size() );	
		
		
		DigitizableInteger.printStat();
		
	}

	@Test
	public void add1(){
		
		final String TEN = "ten";
		final String TWENTY = "twenty";
		
		DSTree<DigitizableInteger, String> tree = new DSTree<DigitizableInteger, String>();		
		
		assertTrue( tree.isEmpty() );
		assertSame( 0, tree.size() );
		
		tree.put( new DigitizableInteger(10), TEN );		
		assertFalse( tree.isEmpty() );
		assertSame( 1, tree.size() );
		assertEquals( TEN, tree.get( new DigitizableInteger(10) ));
		assertSame( null, tree.get( new DigitizableInteger(20) ));
		
		tree.put( new DigitizableInteger(20), TWENTY );
		assertFalse( tree.isEmpty() );
		assertSame( 2, tree.size() );
		assertEquals( TEN, tree.get( new DigitizableInteger(10) ));
		assertEquals( TWENTY, tree.get( new DigitizableInteger(20) ));
		
	}
	
	@Test
	public void add2(){
		
		final String ZERO = "zero";
		final String TWO = "two";
		final String THREE = "three";
		
		DSTree<DigitizableInteger, String> tree = new DSTree<DigitizableInteger, String>();		
		
		tree.put( new DigitizableInteger(0), ZERO );	
		tree.put( new DigitizableInteger(2), TWO );
		tree.put( new DigitizableInteger(3), THREE );
		
		assertEquals( ZERO, tree.get( new DigitizableInteger(0) ));
		assertEquals( TWO, tree.get( new DigitizableInteger(2) ));
		assertEquals( THREE, tree.get( new DigitizableInteger(3) ));
		
	}
	
	
	@Test
	public void remove(){
		
		DSTree<DigitizableInteger, Integer> tree = new DSTree<DigitizableInteger, Integer>();		
		
		for( int i =0; i < 8; i++ ){		
			tree.put( new DigitizableInteger(i), i );
		}
		
		assertEquals( 8, tree.size() );
		assertFalse( tree.isEmpty() );
		
		assertEquals( Integer.valueOf(1), tree.remove( new DigitizableInteger(1)) );		
		assertEquals( 7, tree.size() );
		assertFalse( tree.isEmpty() );		
		
		assertEquals( null, tree.get( new DigitizableInteger(1) ));
		assertEquals( Integer.valueOf(0), tree.get( new DigitizableInteger(0) ));		
		for( int i = 2; i < 8; i++ ){
			assertEquals( Integer.valueOf(i), tree.get( new DigitizableInteger(i) ));
		}
		
		
		assertEquals( Integer.valueOf(0), tree.remove( new DigitizableInteger(0)) );
		assertEquals( null, tree.get( new DigitizableInteger(0) ));
		assertEquals( null, tree.get( new DigitizableInteger(1) ));
		assertEquals( 6, tree.size() );
		assertFalse( tree.isEmpty() );			
		for( int i = 2; i < 8; i++ ){
			assertEquals( Integer.valueOf(i), tree.get( new DigitizableInteger(i) ));
		}
		
		assertEquals( Integer.valueOf(5), tree.remove( new DigitizableInteger(5)) );
		assertEquals( 5, tree.size() );
		assertFalse( tree.isEmpty() );
		assertEquals( null, tree.get( new DigitizableInteger(0) ));
		assertEquals( null, tree.get( new DigitizableInteger(1) ));		
		assertEquals( Integer.valueOf(2), tree.get( new DigitizableInteger(2) ));
		assertEquals( Integer.valueOf(3), tree.get( new DigitizableInteger(3) ));
		assertEquals( Integer.valueOf(4), tree.get( new DigitizableInteger(4) ));
		assertEquals( null, tree.get( new DigitizableInteger(5) ));
		assertEquals( Integer.valueOf(6), tree.get( new DigitizableInteger(6) ));
		assertEquals( Integer.valueOf(7), tree.get( new DigitizableInteger(7) ));
	}

}
