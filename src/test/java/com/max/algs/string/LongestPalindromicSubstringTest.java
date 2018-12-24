package com.max.algs.string;

import org.junit.Test;

import java.util.Random;

import static com.max.algs.string.LongestPalindromicSubstring.find;
import static com.max.algs.string.LongestPalindromicSubstring.findBruteforce;
import static org.junit.Assert.assertEquals;

public class LongestPalindromicSubstringTest {

    @Test
    public void findPalindrome() {
        assertEquals("babcbab", find("babcbabc"));
        assertEquals("abcbabcba", find("babcbabcbaccba"));
    }

    @Test
    public void findPalindromeBruteforce() {
        assertEquals("babcbab", findBruteforce("babcbabc"));
        assertEquals("abcbabcba", findBruteforce("babcbabcbaccba"));
    }

    @Test
    public void findPalindromeRandomStrings() {

        RandomStringGenerator generator = new RandomStringGenerator("abcdefg");

        for (int it = 0; it < 1000; ++it) {
            String str = generator.generate();
            assertEquals(findBruteforce(str), find(str));
        }
    }

    private static final class RandomStringGenerator {

        private static final Random RAND = new Random();

        final char[] alphabet;

        RandomStringGenerator(String alphabet) {
            this.alphabet = alphabet.toCharArray();
        }

        String generate() {
            final int length = 50 + RAND.nextInt(200);

            char[] buf = new char[length];

            for (int i = 0; i < length; ++i) {
                buf[i] = alphabet[RAND.nextInt(alphabet.length)];
            }

            return String.valueOf(buf);
        }
    }

}
