package hackerrank;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * https://www.hackerrank.com/challenges/two-strings
 */
public class TwoStrings {


    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int testCases = Integer.valueOf(sc.nextLine());

        for (int i = 0; i < testCases; i++) {
            String a = sc.nextLine();
            String b = sc.nextLine();

            if (hasCommonSubstring(a, b)) {
                System.out.println("YES");
            }
            else {
                System.out.println("NO");
            }
        }
    }

    /**
     * time: O(N)
     * space: O(N)
     */
    private static boolean hasCommonSubstring(String a, String b) {

        String smaller = a;
        String str = b;

        if (b.length() < a.length()) {
            smaller = b;
            str = a;
        }

        Set<Character> chars = new HashSet<>();
        for (int i = 0; i < smaller.length(); i++) {
            chars.add(smaller.charAt(i));
        }


        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);

            if (chars.contains(ch)) {
                return true;
            }
        }

        return false;
    }


}
