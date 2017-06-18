package com.max.algs.ds.compact;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


@Ignore
public class CompactIntArrayTest {

    @Test
    public void setAndGetValueOnBoundary() {

        CompactIntArray compactArr = new CompactIntArray(10, 22);

        compactArr.set(6, 11);

        assertEquals(11, compactArr.get(6));
    }

    @Test
    public void setAndGetOnValues() {

        CompactIntArray compactArr = new CompactIntArray(10, 22);

        compactArr.set(0, 20);
        compactArr.set(1, 18);
        compactArr.set(2, 22);
        compactArr.set(3, 22);
        compactArr.set(4, 16);
        compactArr.set(5, 21);
        compactArr.set(6, 11);

        assertEquals(20, compactArr.get(0));
        assertEquals(18, compactArr.get(1));
        assertEquals(22, compactArr.get(2));
        assertEquals(22, compactArr.get(3));
        assertEquals(16, compactArr.get(4));
        assertEquals(21, compactArr.get(5));
        assertEquals(11, compactArr.get(6));
    }

    @Test
    public void overwriteValues() {

        CompactIntArray compactArr = new CompactIntArray(10, 22);

        compactArr.set(0, 10);
        compactArr.set(1, 20);
        compactArr.set(3, 30);

        compactArr.set(0, 11);
        compactArr.set(1, 21);
        compactArr.set(2, 31);

        assertEquals(11, compactArr.get(0));
        assertEquals(21, compactArr.get(1));
        assertEquals(31, compactArr.get(2));

    }

    @Test
    public void setAndGetValuesFromArray() {

        int[] arr = {20, 18, 22, 22, 16, 21, 11, 22, 21, 21};

        CompactIntArray compactArr = CompactIntArray.create(arr);

        assertEquals(arr.length, compactArr.length());

        for (int i = 0; i < arr.length; ++i) {
            assertEquals("Elements at index = " + i + " are different, arr[i] = " + arr[i] +
                            ", compactArr[i] = " + compactArr.get(i),
                    arr[i], compactArr.get(i));
        }

    }
}
