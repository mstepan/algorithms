package com.max.algs.string;

import org.junit.Test;

import static com.max.algs.string.EditDistance.editDistance;
import static org.junit.Assert.assertEquals;

public class EditDistanceTest {

    @Test
    public void editDistanceNormalFlow() {

        assertEquals(3, editDistance("AGCATGC", "ACAATCC"));
        assertEquals(3, editDistance("A8CATGC", "ACAAT2C"));

        assertEquals(0, editDistance("AAAA", "AAAA"));
        assertEquals(0, editDistance("", ""));
        assertEquals(1, editDistance("", " "));
        assertEquals(1, editDistance(" ", ""));
        assertEquals(1, editDistance("AAAA", "AABA"));
        assertEquals(3, editDistance("AAAAXXX", "AAAA"));
        assertEquals(3, editDistance("AAAA", "AAAAXXX"));

        assertEquals(7, editDistance("", "ACAATCC"));
        assertEquals(7, editDistance("AGCATGC", ""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void editDistanceNullFirstParameterFails() {
        editDistance(null, "ACAATCC");
    }

    @Test(expected = IllegalArgumentException.class)
    public void editDistanceNullSecondParameterFails() {
        editDistance("ACAATCC", null);
    }
}
