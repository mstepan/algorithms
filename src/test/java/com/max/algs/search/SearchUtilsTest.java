package com.max.algs.search;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SearchUtilsTest {


    @Test
    public void bsearch() {

        int[] arr = {0, 2, 3, 4, 5, 6, 7, 8, 9, 15};

        assertEquals(0, SearchUtils.bsearch(arr, 0));
        assertEquals(1, SearchUtils.bsearch(arr, 2));
        assertEquals(2, SearchUtils.bsearch(arr, 3));
        assertEquals(3, SearchUtils.bsearch(arr, 4));
        assertEquals(4, SearchUtils.bsearch(arr, 5));
        assertEquals(5, SearchUtils.bsearch(arr, 6));
        assertEquals(6, SearchUtils.bsearch(arr, 7));
        assertEquals(7, SearchUtils.bsearch(arr, 8));
        assertEquals(8, SearchUtils.bsearch(arr, 9));
        assertEquals(9, SearchUtils.bsearch(arr, 15));

        assertEquals(-1, SearchUtils.bsearch(arr, -5));
        assertEquals(-1, SearchUtils.bsearch(arr, -1));
        assertEquals(-1, SearchUtils.bsearch(arr, 10));
        assertEquals(-1, SearchUtils.bsearch(arr, 17));
    }

}
