package com.max.algs.topcoder;

/*
*
* URL: http://community.topcoder.com/stat?c=problem_statement&pm=12325
*
*/
public final class FauxPalindromes {


    /**
     *
     * time: O(N)
     * space: O(N)
     *
     */
    public static String classifyIt(String word){

        if( isPalindrome( word) ){
            return "PALINDROME";
        }

        word = replaceRepeatedCharsWithOne( word );

        if( isPalindrome( word) ){
            return "FAUX";
        }

        return "NOT EVEN FAUX";
    }

    private static String replaceRepeatedCharsWithOne(String word) {

        StringBuilder buf = new StringBuilder();

        for(int i = 1; i < word.length(); i++ ){
            if( word.charAt(i-1) != word.charAt(i) ){
                buf.append( word.charAt(i-1) );
            }
        }

        buf.append( word.charAt( word.length()-1) );

        return buf.toString();
    }


    private static boolean isPalindrome( String str ){
        int i =0;
        int j = str.length()-1;


        while( i < j ){

            if( str.charAt(i) != str.charAt(j) ){
                return false;
            }

             ++i;
            --j;
        }

        return true;
    }
}
