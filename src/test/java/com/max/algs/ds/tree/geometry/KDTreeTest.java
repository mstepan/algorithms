package com.max.algs.ds.tree.geometry;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Random;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.max.algs.ds.tree.geometry.KDTree.KDEntry;

public class KDTreeTest {
	
	private static final Logger LOG = Logger.getLogger(KDTreeTest.class);
	
	@Test
	public void constructWithDuplicates(){
		XYPoint[] points = { 
				new XYPoint(1, 9), 
				new XYPoint(0, 6), 
				new XYPoint(9, 8), 
				new XYPoint(9, 8), 
				new XYPoint(4, 4), 
				new XYPoint(3, 0), 
				new XYPoint(3, 7), 
				new XYPoint(6, 2), 
				new XYPoint(2, 7), 
				new XYPoint(7, 9)
		};		
		
		KDTree tree = new KDTree(points);
		
		KDEntry root = tree.getRoot();
		
		assertEquals( new XYPoint(3, 7), root.point );
	}
	
	@Test
	public void constructFrom10Points(){
		XYPoint[] points = { 
				new XYPoint(9, 5), 
				new XYPoint(5, 4), 
				new XYPoint(4, 8), 
				new XYPoint(3, 3), 
				new XYPoint(4, 7), 
				new XYPoint(4, 1), 
				new XYPoint(1, 1), 
				new XYPoint(5, 1), 
				new XYPoint(6, 9), 
				new XYPoint(3, 4)
			};
		
		KDTree tree = new KDTree(points);
		
		KDEntry root = tree.getRoot();
		
		assertEquals( new XYPoint(4, 7), root.point );
		
		// check left subtree
		assertEquals( new XYPoint(3, 3), root.left.point );
		assertEquals( new XYPoint(1, 1), root.left.left.point );		
		assertEquals( new XYPoint(3, 4), root.left.right.point );
		assertEquals( new XYPoint(4, 8), root.left.right.right.point );
		
		// check right subtree
		assertEquals( new XYPoint(5, 4), root.right.point );		
		assertEquals( new XYPoint(4, 1), root.right.left.point );
		assertEquals( new XYPoint(5, 1), root.right.left.right.point );		
		assertEquals( new XYPoint(9, 5), root.right.right.point );
		assertEquals( new XYPoint(6, 9), root.right.right.right.point );
	}
	

	@Test
	public void constructRandom(){
		
		Random random = new Random();
		XYPoint[] points = new XYPoint[10];
		
		for( int i =0; i < points.length; i++ ){
			points[i] = new XYPoint(random.nextInt(10), random.nextInt(10));
		}
		
		LOG.info(Arrays.toString(points) );
		
		@SuppressWarnings("unused")
		KDTree tree = new KDTree(points);
		
		
		
	}

	
	
	
	@Test
	public void constructFrom6Points(){
		
		XYPoint[] points = {
				new XYPoint(7, 4),  
				new XYPoint(6, 1), 
				new XYPoint(2, 9), 
				new XYPoint(10, 2),  
				new XYPoint(1, 2), 
				new XYPoint(9, 3)
		}; 
		
		
		KDTree tree = new KDTree(points);
		
		KDEntry root = tree.getRoot();
		
		assertEquals( new XYPoint(6, 1), root.point);
		
		// check left subtree
		assertEquals( new XYPoint(1, 2), root.left.point);
		assertEquals( new XYPoint(2, 9), root.left.right.point);
		
		//check right subtree
		assertEquals( new XYPoint(9, 3), root.right.point);
		assertEquals( new XYPoint(10, 2), root.right.left.point);
		assertEquals( new XYPoint(7, 4), root.right.right.point);
	}

}
