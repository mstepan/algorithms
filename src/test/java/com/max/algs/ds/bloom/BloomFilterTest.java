package com.max.algs.ds.bloom;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class BloomFilterTest {

    @Test
    public void addAndContains() {

        BloomFilter<String> filter = new BloomFilter<>(100);

        assertFalse(filter.contains("111"));
        assertFalse(filter.contains("222"));

        filter.add("111");
        assertTrue(filter.contains("111"));
        assertFalse(filter.contains("222"));
    }

    @Test(expected =  UnsupportedOperationException.class)
    public void remove(){
        BloomFilter<String> filter = new BloomFilter<>(100);
        filter.add("111");
        filter.remove("111");
    }

}
