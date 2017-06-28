package com.max.algs.epi.string;


import org.apache.log4j.Logger;

import static com.google.common.base.Preconditions.checkArgument;

public class TestPalindromicity {

    private static final Logger LOG = Logger.getLogger(TestPalindromicity.class);

    private TestPalindromicity() throws Exception {

        String str = "A man, a plan, a canal, Panama";

        System.out.printf("isPalindrome = %b %n", isPalindrome(str));

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * 7.5. Test palindromicity.
     * <p>
     * time: O(N)
     * space: O(1)
     */
    public static boolean isPalindrome(String str) {
        checkArgument(str != null, "'null' str parameter passed");

        int left = 0;
        int right = str.length() - 1;

        while (left < right) {
            if (!Character.isLetterOrDigit(str.charAt(left))) {
                ++left;
            }
            else if (!Character.isLetterOrDigit(str.charAt(right))) {
                --right;
            }
            else {
                char ch1 = Character.toLowerCase(str.charAt(left));
                char ch2 = Character.toLowerCase(str.charAt(right));

                if (ch1 != ch2) {
                    return false;
                }

                ++left;
                --right;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        try {
            new TestPalindromicity();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }


}
