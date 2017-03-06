package com.max.algs.ds.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MruCacheTest {
	
	
	@Test
	public void add(){
		MruCache<Integer, String> cache = new MruCache<>(5);
		
		assertNotNull( cache.head );
		assertSame( cache.head, cache.head.next );
		assertSame( cache.head, cache.head.prev );
		
		assertSequenceOfNodesCorrect(cache, new int[]{});
		
		cache.add(5, "5");		
		assertSequenceOfNodesCorrect(cache, new int[]{5});
		
		cache.add(3, "3");		
		assertSequenceOfNodesCorrect(cache, new int[]{3, 5});
		
		cache.add(10, "10");
		cache.add(7, "7");	
		cache.add(4, "4");	
		assertSequenceOfNodesCorrect(cache, new int[]{4, 7, 10, 3, 5});
		
		cache.add(11, "11");
		assertSequenceOfNodesCorrect(cache, new int[]{11, 7, 10, 3, 5});		
		
		cache.add(5, "5.1");
		assertSequenceOfNodesCorrect(cache, new int[]{5, 11, 7, 10, 3});
		
		assertEquals( "5.1", cache.get(5) );
		assertSequenceOfNodesCorrect(cache, new int[]{5, 11, 7, 10, 3});
		
		assertEquals( "10", cache.get(10) );
		assertSequenceOfNodesCorrect(cache, new int[]{10, 5, 11, 7, 3});
		
		cache.add(17, "17");
		assertSequenceOfNodesCorrect(cache, new int[]{17, 5, 11, 7, 3});
		
	}
	
	
	private void assertSequenceOfNodesCorrect( MruCache<Integer, String> cache, int[] arr ){
		
		assertEquals(arr.length, cache.size());
		
		if( arr.length > 0 ){
			assertTrue( "MruCache is empty", ! cache.isEmpty() );
		}
		
		CacheNode<Integer, String> head = cache.head;
		
		CacheNode<Integer, String> cur = head.next;
		int i = 0;
		
		while( cur != head ){
			
			if( i >= arr.length ){
				throw new AssertionError("MRU sequence too long");
			}

			
			assertEquals( Integer.valueOf(arr[i]), cur.key);			
			cur = cur.next;
			++i;
		}
		
		assertEquals( "Sequnce is bigger then MRU nodes", i,  arr.length );
		
		// check 'prev' references
		
		cur = head.prev;
		i = i-1;
		
		while( cur != head ){
			assertEquals( Integer.valueOf(arr[i]), cur.key);
			cur = cur.prev;
			--i;
		}
		
	}



}
