package com.max.algs.string;

import org.junit.Test;

import static com.max.algs.string.EditDistance.canEditWithDistance;
import static com.max.algs.string.EditDistance.editDistance;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class EditDistanceTest {

    @Test
    public void canBeEditedUsingSpecifiedDistance() {

        assertFalse(canEditWithDistance("AGCATGC", "ACAATCC", 0));
        assertFalse(canEditWithDistance("AGCATGC", "ACAATCC", 1));
        assertFalse(canEditWithDistance("AGCATGC", "ACAATCC", 2));

        assertTrue(canEditWithDistance("AGCATGC", "ACAATCC", 3));
        assertTrue(canEditWithDistance("AGCATGC", "ACAATCC", 4));
        assertTrue(canEditWithDistance("AGCATGC", "ACAATCC", 10));

        assertTrue(canEditWithDistance("A8CATGC", "ACAAT2C", 3));

        assertTrue(canEditWithDistance("AAACCC", "AAA", 3));
        assertFalse(canEditWithDistance("AAACCCC", "AAA", 3));
    }

    @Test
    public void editDistanceNormalFlow() {

        assertEquals(3, editDistance("AGCATGC", "ACAATCC"));
        assertEquals(3, editDistance("AGCATGC", "ACAATCC"));
        assertEquals(3, editDistance("AGCATGC", "ACAATCC"));

        assertEquals(3, editDistance("AGCATGC", "ACAATCC"));
        assertEquals(3, editDistance("AGCATGC", "ACAATCC"));
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

    @SuppressWarnings("ConstantConditions")
    @Test(expected = IllegalArgumentException.class)
    public void editDistanceNullFirstParameterFails() {
        editDistance(null, "ACAATCC");
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = IllegalArgumentException.class)
    public void editDistanceNullSecondParameterFails() {
        editDistance("ACAATCC", null);
    }
}
