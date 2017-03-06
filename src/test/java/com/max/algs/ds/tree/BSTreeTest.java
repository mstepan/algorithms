package com.max.algs.ds.tree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

public class BSTreeTest {
	
	
	@Test(expected = IllegalArgumentException.class)
	public void getKLargestNegativeValue(){
		BSTree<Integer> tree = new BSTree<>();
		tree.getKLargest(-3);
	}
	
	@Test
	public void getKLargestZeroValue(){
		BSTree<Integer> tree = new BSTree<>();
		assertSame( Collections.emptyList(), tree.getKLargest(0) );
	}
	
	@Test
	public void getKLargest(){
		BSTree<Integer> tree = new BSTree<>();
		
		assertSame( Collections.emptyList(), tree.getKLargest(3) );
				
		tree.add(19);
		
		tree.add(7);
		tree.add(43);
		
		tree.add(3);
		tree.add(11);
		tree.add(23);
		tree.add(47);
		
		tree.add(2);
		tree.add(5);
		tree.add(17);
		tree.add(37);
		tree.add(53);
		
		tree.add(13);
		tree.add(29);
		tree.add(41);
		
		tree.add(31);		
		
		assertEquals( Arrays.asList(53), tree.getKLargest(1) );
		assertEquals( Arrays.asList(53, 47, 43), tree.getKLargest(3) );		
		assertEquals( Arrays.asList(53, 47, 43, 41, 37), tree.getKLargest(5) );
		
	}

}
