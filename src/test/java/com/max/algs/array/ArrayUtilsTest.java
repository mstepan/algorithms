package com.max.algs.array;


import com.max.algs.util.ArrayUtils;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ArrayUtilsTest {


//	@Test(expected = IllegalArgumentException.class)
//	public void maxProductOfThreeElementsWithNullArray(){
//		ArrayUtils.maxProductOfThreeElements( null );	
//	}
//	
//	@Test
//	public void maxProductOfThreeElementsWithSmallArray(){		
//		assertEquals( -10, ArrayUtils.maxProductOfThreeElements( new int[]{-2, 5} ) );
//		assertEquals( -40, ArrayUtils.maxProductOfThreeElements( new int[]{-2, 4, 5} ) );
//		assertEquals( 5, ArrayUtils.maxProductOfThreeElements( new int[]{5} ) );
//		assertEquals( 0, ArrayUtils.maxProductOfThreeElements( new int[]{} ) );
//	}


    @Test
    public void maxProductOfThreeElements() {
        assertEquals(420, ArrayUtils.maxProductOfThreeElements(new int[]{-2, 0, 4, -10, 5, 6, -5, -7}));
        assertEquals(120, ArrayUtils.maxProductOfThreeElements(new int[]{-2, 0, 4, 5, 6, -5, -3}));
    }


    @Test
    public void findOrder() {


        int[] arr = new int[]{1, 3, 4, 3, 6, 5, 8, 7, 5, 0};

        assertEquals(0, ArrayUtils.findOrder(arr, 0));
        assertEquals(1, ArrayUtils.findOrder(arr, 1));
        assertEquals(3, ArrayUtils.findOrder(arr, 2));
        assertEquals(3, ArrayUtils.findOrder(arr, 3));

        assertEquals(4, ArrayUtils.findOrder(arr, 4));
        assertEquals(5, ArrayUtils.findOrder(arr, 5));
        assertEquals(5, ArrayUtils.findOrder(arr, 6));
        assertEquals(6, ArrayUtils.findOrder(arr, 7));
        assertEquals(7, ArrayUtils.findOrder(arr, 8));
        assertEquals(8, ArrayUtils.findOrder(arr, 9));


        arr = new int[]{7, 9, 6, 1, 3, 1, 6, 9, 6, 4};

        assertEquals(1, ArrayUtils.findOrder(arr, 0));
        assertEquals(1, ArrayUtils.findOrder(arr, 1));
        assertEquals(3, ArrayUtils.findOrder(arr, 2));
        assertEquals(4, ArrayUtils.findOrder(arr, 3));
        assertEquals(6, ArrayUtils.findOrder(arr, 4));
        assertEquals(6, ArrayUtils.findOrder(arr, 5));
        assertEquals(6, ArrayUtils.findOrder(arr, 6));
        assertEquals(7, ArrayUtils.findOrder(arr, 7));
        assertEquals(9, ArrayUtils.findOrder(arr, 8));
        assertEquals(9, ArrayUtils.findOrder(arr, 9));
    }


}
