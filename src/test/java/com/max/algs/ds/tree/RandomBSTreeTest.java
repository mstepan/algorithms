package com.max.algs.ds.tree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.log4j.Logger;
import org.junit.Test;


public class RandomBSTreeTest {
	
	private static final Logger LOG = Logger.getLogger(RandomBSTreeTest.class);
	
	@Test
	public void addSortedData(){
		
		RandomBSTree<Integer> treeAsc = new RandomBSTree<>();
		
		for( int i = 0; i < 1_000_000; i++ ){			
			treeAsc.add(i);
		}
		
		LOG.info( "Sorted ASC values heigh: " + treeAsc.heigh() );
		
		
		RandomBSTree<Integer> treeDesc = new RandomBSTree<>();
		
		for( int i = 0; i < 1_000_000; i++ ){			
			treeDesc.add(i);
		}
		
		LOG.info( "Sorted DESC values heigh: " + treeDesc.heigh() );
	}
	
	@Test
	public void addRandomData(){
		Random rand = ThreadLocalRandom.current();
		
		RandomBSTree<Integer> tree = new RandomBSTree<>();
		Set<Integer> allValues = new HashSet<>();
		
		for( int i =0; i < 2567; i++ ){
			int value = rand.nextInt();
			
			boolean setInsertRes = allValues.add( value );
			boolean treeInsertRes = tree.add(value);
			
			assertEquals( setInsertRes, treeInsertRes );
		}
		
		assertEquals( allValues.size(), tree.size() );
		
		for( int valueFromSet : allValues ){
			assertTrue( tree.contains(valueFromSet) );
		}
	}
	
	
	@Test
	public void add(){
		
		RandomBSTree<Integer> tree = new RandomBSTree<>();
		
		assertTrue( tree.isEmpty() );
		assertEquals( 0, tree.size() );
		
		tree.add( 6 );
		
		assertFalse( tree.isEmpty() );
		assertEquals( 1, tree.size() );
		assertTrue( tree.contains(6) );
		
		
		tree.add( 3 );
		
		assertFalse( tree.isEmpty() );
		assertEquals( 2, tree.size() );
		assertTrue( tree.contains(6) );
		assertTrue( tree.contains(3) );
		
		
		tree.add( 5 );
		tree.add( 9 );
		tree.add( 7 );
		
		assertFalse( tree.isEmpty() );
		assertEquals( 5, tree.size() );
		assertTrue( tree.contains(6) );
		assertTrue( tree.contains(3) );
		assertTrue( tree.contains(5) );
		assertTrue( tree.contains(9) );
		assertTrue( tree.contains(7) );
		
	}

}
