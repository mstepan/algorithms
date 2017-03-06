package com.max.algs.ds.heap;


import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Ignore;
import org.junit.Test;

public class BinaryHeapTest {
	
	
	@Test(expected = ConcurrentModificationException.class)
	public void failFastIteration1(){
		BinaryHeap<Integer> minHeap = BinaryHeap.minHeap();
		minHeap.add(3);
	    minHeap.add(3);
	    minHeap.add(4); 
		
		Iterator<Integer> it1 = minHeap.iterator();		
		Iterator<Integer> it2 = minHeap.iterator();
		
		it1.next();
		it2.next();
		it1.remove();
		it2.next();
	}
	
	@Test(expected = ConcurrentModificationException.class)
	public void failFastIteration2(){
		BinaryHeap<Integer> minHeap = BinaryHeap.minHeap();
		minHeap.add(3);
	    minHeap.add(3);
	    minHeap.add(4); 
		
		Iterator<Integer> it = minHeap.iterator();	
		minHeap.add(8); 
		it.next();
			
	}
	
	
	@Test
	public void iterateEmptyHeap(){
		BinaryHeap<Integer> minHeap = BinaryHeap.minHeap();
		Iterator<Integer> it = minHeap.iterator();
		assertFalse( it.hasNext() );
	}
	
	
	@Test(expected = NoSuchElementException.class)
	public void iterateOverBoundaries(){
		BinaryHeap<Integer> minHeap = BinaryHeap.minHeap();
	     minHeap.add(3);
	     minHeap.add(3);
	     minHeap.add(4);
	     
	     Iterator<Integer> it = minHeap.iterator();
	     
	     for( int i =0; i < minHeap.size(); i++ ){
	    	 it.next();
	     }
	     
	     assertFalse( it.hasNext() );
	     it.next();
	}
	
	@Test
	public void iterate(){
		 BinaryHeap<Integer> minHeap = BinaryHeap.minHeap();

	     minHeap.add(3);
	     minHeap.add(3);
	     minHeap.add(4);        
	     minHeap.add(7);
	     minHeap.add(8);        
	     minHeap.add(6);
	     minHeap.add(5);
	     
	     int[] heapArr = {3, 3, 4, 7, 8, 6, 5};
	     
	     Iterator<Integer> it = minHeap.iterator();
	     
	     for( int value : heapArr ){
	    	assertTrue( it.hasNext() );
	     	assertEquals( Integer.valueOf(value), it.next() );
	     }
		
	}


	@Ignore
    @Test
    public void maxHeapToMin(){

        BinaryHeap<Integer> minHeap = BinaryHeap.minHeap();

        minHeap.add(3);
        minHeap.add(3);
        minHeap.add(4);        
        minHeap.add(7);
        minHeap.add(8);        
        minHeap.add(6);
        minHeap.add(5);

        assertEquals(7, minHeap.size());
        assertArrayEquals( new Comparable[]{3, 3, 4, 7, 8, 6, 5, null}, minHeap.getInternalArray() );

        BinaryHeap<Integer> maxHeap = minHeap.inverseHeap();

        assertEquals(7, maxHeap.size());
        assertArrayEquals( new Comparable[]{8, 7, 6, 5, 4, 3, 3, null}, maxHeap.getInternalArray() );

        BinaryHeap<Integer> minHeap2 = maxHeap.inverseHeap();
        assertEquals(7, minHeap2.size());
        assertArrayEquals(new Comparable[]{ 3, 3, 4, 5, 6, 7, 8, null}, minHeap2.getInternalArray() );
    }





	@Test
    public void addToMinHeap(){

        BinaryHeap<Integer> heap = BinaryHeap.minHeap();

        heap.add(7);
        assertEquals( 1, heap.size() );
        assertArrayEquals( new Comparable[]{7, null, null, null, null, null, null, null}, heap.getInternalArray() );


        heap.add(4);
        assertEquals( 2, heap.size() );
        assertArrayEquals( new Comparable[]{4, 7, null, null, null, null, null, null}, heap.getInternalArray() );

        heap.add(3);
        assertEquals( 3, heap.size() );
        assertArrayEquals( new Comparable[]{3, 7, 4, null, null, null, null, null}, heap.getInternalArray() );

        heap.add(8);
        assertEquals( 4, heap.size() );
        assertArrayEquals( new Comparable[]{3, 7, 4, 8, null, null, null, null}, heap.getInternalArray() );


        heap.add(2);
        assertEquals( 5, heap.size() );
        assertArrayEquals( new Comparable[]{2, 3, 4, 8, 7, null, null, null}, heap.getInternalArray() );

    }



        @Test
    public void addToMaxHeap(){

        BinaryHeap<Integer> heap = BinaryHeap.maxHeap();

        heap.add(4);
        assertEquals( 1, heap.size() );
        assertArrayEquals( new Comparable[]{4, null, null, null, null, null, null, null}, heap.getInternalArray() );

        heap.add(3);
        assertEquals( 2, heap.size() );
        assertArrayEquals( new Comparable[]{4, 3, null, null, null, null, null, null}, heap.getInternalArray() );

        heap.add(5);
        assertEquals( 3, heap.size() );
        assertArrayEquals( new Comparable[]{5, 3, 4, null, null, null, null, null}, heap.getInternalArray() );

        heap.add(6);
        assertEquals( 4, heap.size() );
        assertArrayEquals( new Comparable[]{6, 5, 4, 3, null, null, null, null}, heap.getInternalArray() );

        heap.add(7);
        assertEquals( 5, heap.size() );
        assertArrayEquals( new Comparable[]{7, 6,  4, 3, 5, null, null, null}, heap.getInternalArray() );

        heap.add(1);
        assertEquals( 6, heap.size() );
        assertArrayEquals( new Comparable[]{7, 6,  4, 3, 5, 1, null, null}, heap.getInternalArray() );

        heap.add(2);
        assertEquals( 7, heap.size() );
        assertArrayEquals( new Comparable[]{7, 6,  4, 3, 5, 1, 2, null}, heap.getInternalArray() );

        heap.add(3);
        assertEquals( 8, heap.size() );
        assertTrue( Arrays.equals(new Comparable[]{7, 6,  4, 3, 5, 1, 2, 3}, heap.getInternalArray()) );

        heap.add(8);
        assertEquals( 9, heap.size() );
        assertArrayEquals( new Comparable[]{8, 7, 4, 6, 5, 1, 2, 3, 3, null, null, null, null, null, null, null}, heap.getInternalArray() );
    }


        private void assertArrayEquals(Comparable<?>[] comparables, Comparable<Integer>[] internalArray) {
        	
        	if( comparables.length != internalArray.length ){
        		throw new AssertionError("Arrays aren't equal, expected: " + Arrays.toString(comparables) + ", actual: " + Arrays.toString(internalArray) );
        	}
        	
        	if( ! Arrays.equals(comparables, internalArray) ){
        		throw new AssertionError("Arrays aren't equal, expected: " + Arrays.toString(comparables) + ", actual: " + Arrays.toString(internalArray) );
        	}		
    	}

}
