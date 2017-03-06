package com.max.algs.ds.map;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.junit.Test;

public class SoftHashMapTest {

	private static final Logger LOG = Logger.getLogger(SoftHashMapTest.class);
	
	
	@Test
	public void put() throws InterruptedException {
		
		SoftHashMap<String, String> map = new SoftHashMap<>();
	
		assertEquals( 0, map.size() );		
		
		for( int i =0; i < 100; i++){
			map.put( new String("key_" + i), new String("value_" + i) );
		}
		
		String strongKey = "max";
		String strongValue = "max_value";
		
		map.put( strongKey, strongValue );
		
		System.gc();
		TimeUnit.SECONDS.sleep(2);
		
		LOG.info( map.getStatistics() );
		
		assertEquals( 1, map.size() );
		assertEquals( strongValue, map.get(strongKey) );
		
		


		
	}

}
