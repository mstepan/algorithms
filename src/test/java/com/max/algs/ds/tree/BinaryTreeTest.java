package com.max.algs.ds.tree;

import static org.junit.Assert.*;

import java.util.Comparator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.max.algs.ds.tree.BinaryTree.NodeDirection;

public class BinaryTreeTest {
	
	
	private BinaryTree<Integer> tree;
	
	@Before
	public void setUp(){
		tree = new BinaryTree<>();
	}
	
	@After
	public void tearDown(){
		tree = null;
	}
	
	@Test
	public void findMax()  {
		
		Comparator<Integer> cmp = new Comparator<Integer>(){
			@Override
			public int compare(Integer value1, Integer value2) {
				return value1.compareTo(value2);
			}				
		};	
		
		tree.add( 10 );
		assertEquals( Integer.valueOf(10), tree.findMax(cmp) );
		
		tree.add( 8);
		assertEquals( Integer.valueOf(10), tree.findMax(cmp) );
		
		tree.add( 13);
		assertEquals( Integer.valueOf(13), tree.findMax(cmp) );
		
		tree.add( 17);
		assertEquals( Integer.valueOf(17), tree.findMax(cmp) );
		
		tree.add( 33 );
		assertEquals( Integer.valueOf(33), tree.findMax(cmp) );
		
		tree.add( 25 );		
		assertEquals( Integer.valueOf(33), tree.findMax(cmp) );
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void addNullValue(){
		tree.add( null );
	}
	
	
	@Test
	public void add(){
		assertTrue( tree.isEmpty() );
		assertEquals( 0, tree.size() );		
		assertEquals( null,  tree.root() );			

		tree.add( 10 );
		assertEquals( Integer.valueOf(10), tree.root().value );	
		assertEquals( null, tree.root().left );
		assertEquals( null, tree.root().right );
		
		tree.add( 8);
		assertEquals( Integer.valueOf(10), tree.root().value );	
		
		assertEquals( Integer.valueOf(8), tree.root().left.value );
		assertEquals( null, tree.root().left.left );
		assertEquals( null, tree.root().left.right );
		
		assertEquals( null, tree.root().right );

		
		tree.add( 13);
		assertEquals( Integer.valueOf(10), tree.root().value );	
		
		assertEquals( Integer.valueOf(8), tree.root().left.value );
		assertEquals( null, tree.root().left.left );
		assertEquals( null, tree.root().left.right );
		
		assertEquals( Integer.valueOf(13), tree.root().right.value );
		assertEquals( null, tree.root().right.left );
		assertEquals( null, tree.root().right.right );

		
		tree.add( 17);
		assertEquals( Integer.valueOf(10), tree.root().value );	
		
		assertEquals( Integer.valueOf(8), tree.root().left.value );
		assertEquals( Integer.valueOf(17), tree.root().left.left.value );
		assertEquals( null, tree.root().left.right );
		
		assertEquals( Integer.valueOf(13), tree.root().right.value );
		assertEquals( null, tree.root().right.left );
		assertEquals( null, tree.root().right.right );

		
		tree.add( 33 );
		assertEquals( Integer.valueOf(10), tree.root().value );	
		
		assertEquals( Integer.valueOf(8), tree.root().left.value );
		assertEquals( Integer.valueOf(17), tree.root().left.left.value );
		assertEquals( Integer.valueOf(33), tree.root().left.right.value );
		
		assertEquals( Integer.valueOf(13), tree.root().right.value );
		assertEquals( null, tree.root().right.left );
		assertEquals( null, tree.root().right.right );

		
		tree.add( 25 );	
		assertEquals( Integer.valueOf(10), tree.root().value );	
		
		assertEquals( Integer.valueOf(8), tree.root().left.value );
		assertEquals( Integer.valueOf(17), tree.root().left.left.value );
		assertEquals( Integer.valueOf(33), tree.root().left.right.value );
		
		assertEquals( Integer.valueOf(13), tree.root().right.value );
		assertEquals( Integer.valueOf(25), tree.root().right.left.value );
		assertEquals( null, tree.root().right.right );

	}
	
	@Test
	public void containsInMinFirst(){
		BinaryTree<Integer> tree = new BinaryTree<>(); 
		
		assertFalse( tree.containsInMinFirst(2) );
		
    	tree.add(2);    	
    	assertTrue( tree.containsInMinFirst(2) );
    	
    	tree.add(3, 2, NodeDirection.LEFT);
    	tree.add(13, 2, NodeDirection.RIGHT);
    	assertTrue( tree.containsInMinFirst(2) );
    	assertTrue( tree.containsInMinFirst(3) );
    	assertTrue( tree.containsInMinFirst(13) );
    	
    	
    	tree.add(5, 3, NodeDirection.RIGHT);
    	tree.add(7, 5, NodeDirection.LEFT);
    	tree.add(11, 5, NodeDirection.RIGHT);
    	
    	tree.add(17, 13, NodeDirection.LEFT);
    	tree.add(19, 13, NodeDirection.RIGHT);
    	tree.add(23, 19, NodeDirection.LEFT);
    	
    	assertTrue( tree.containsInMinFirst(2) );
    	assertTrue( tree.containsInMinFirst(3) );
    	assertTrue( tree.containsInMinFirst(13) );
    	
    	assertTrue( tree.containsInMinFirst(5) );
    	assertTrue( tree.containsInMinFirst(7) );
    	assertTrue( tree.containsInMinFirst(11) );
    	
    	assertTrue( tree.containsInMinFirst(17) );
    	assertTrue( tree.containsInMinFirst(19) );
    	assertTrue( tree.containsInMinFirst(23) );
    	
    	assertFalse( tree.containsInMinFirst(1) );
    	assertFalse( tree.containsInMinFirst(-100) );
    	assertFalse( tree.containsInMinFirst(6) );
    	assertFalse( tree.containsInMinFirst(12) );
    	
    	assertFalse( tree.containsInMinFirst(18) );
    	assertFalse( tree.containsInMinFirst(15) );
    	assertFalse( tree.containsInMinFirst(100) );
    	
    	
    	assertEquals( "2,3,5,7,11,13,17,19,23", tree.sortedOrderForMinFirst() );
	}

}
