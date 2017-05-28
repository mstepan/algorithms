package com.max.algs.ds.rope;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for {@see RopeString}
 */
public class RopeStringTest {

    @Test
    public void createAndGetChars() {
        String str = "hello";

        RopeString rope = new RopeString(str);
        assertEquals(str.length(), rope.length());

        for (int i = 0; i < rope.length(); ++i) {
            assertEquals("Chars from string and rope aren't equal", str.charAt(i), rope.charAt(i));
        }
    }

    @Test
    public void concatenateTwoStringsAndGetChars() {
        String str1 = "hello";
        String str2 = " world.";

        RopeString rope = new RopeString(str1);
        rope = rope.append(str2);

        String combinedStr = str1 + str2;

        assertEquals(combinedStr.length(), rope.length());

        for (int i = 0; i < rope.length(); ++i) {
            assertEquals("Chars from string and rope aren't equal: index = '" + i + "'" +
                            ", expected = '" + combinedStr.charAt(i) + "'" +
                            ", actual = '" + rope.charAt(i) + "'",
                    combinedStr.charAt(i),
                    rope.charAt(i));
        }
    }

    @Test
    public void concatenateMultipleStringsAndGetChars() {

        String[] arr = {
                "hello",
                " wonderful",
                " world.",
                " some ",
                "other ",
                "strings!!!"
        };


        RopeString rope = new RopeString(arr[0]);
        StringBuilder combinedBuf = new StringBuilder(arr[0]);

        for (int i = 1; i < arr.length; ++i) {
            String str = arr[i];
            rope = rope.append(str);
            combinedBuf.append(str);
        }

        assertEquals(combinedBuf.length(), rope.length());

        for (int i = 0; i < rope.length(); ++i) {
            assertEquals("Chars from string and rope aren't equal: index = '" + i + "'" +
                            ", expected = '" + combinedBuf.charAt(i) + "'" +
                            ", actual = '" + rope.charAt(i) + "'",
                    combinedBuf.charAt(i),
                    rope.charAt(i));
        }
    }

}
