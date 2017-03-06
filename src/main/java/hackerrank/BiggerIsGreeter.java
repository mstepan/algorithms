package hackerrank;

import java.util.Arrays;
import java.util.Scanner;

/**
 * https://www.hackerrank.com/challenges/bigger-is-greater
 */
public class BiggerIsGreeter {


    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int testCasesCount = Integer.valueOf(sc.nextLine());

        for (int i = 0; i < testCasesCount; i++) {
            String str = sc.nextLine();

            String nextStr = next(str);

            System.out.println(nextStr);
        }

    }

    /**
     * time: O(N*lgN)
     * space: O(N)
     */
    private static String next(String str) {

        // O(N) space
        char[] arr = str.toCharArray();

        // O(N)
        for (int i = arr.length - 2; i >= 0; --i) {
            if (arr[i] < arr[i + 1]) {
                int index = nextCharIndex(arr, i);
                swap(arr, i, index);

                // O(N*lgN)
                Arrays.sort(arr, i + 1, arr.length);

                return new String(arr);
            }
        }

        return "no answer";

    }

    private static void swap(char[] arr, int from, int to) {
        char temp = arr[from];
        arr[from] = arr[to];
        arr[to] = temp;
    }

    private static int nextCharIndex(char[] arr, int index) {

        int candidateIndex = -1;
        char candidate = Character.MAX_VALUE;

        char base = arr[index];

        for (int i = index + 1; i < arr.length; i++) {

            char ch = arr[i];
            if (ch > base && ch <= candidate) {

                candidate = ch;
                candidateIndex = i;

            }
        }

        return candidateIndex;
    }


}
