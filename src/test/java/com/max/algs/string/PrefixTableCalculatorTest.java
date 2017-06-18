package com.max.algs.string;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class PrefixTableCalculatorTest {


    @Test
    public void contains() {
        assertTrue(PrefixTableCalculator.contains("abd", "abcabdc"));
        assertFalse(PrefixTableCalculator.contains("abd", "abcabcd"));
    }


}
