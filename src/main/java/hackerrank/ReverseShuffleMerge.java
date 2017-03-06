package hackerrank;

import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * https://www.hackerrank.com/challenges/reverse-shuffle-merge
 */
public class ReverseShuffleMerge {

    /*
    bdabaceadaedaaaeaecdeadababdbeaeeacacaba
    ..a.a..a.a..aaa.a..d..d.b.bdbe.ee.......

    IN:  bdabaceadaedaaaeaecdeadababdbeaeeacacaba
    OUT: aaaaaabaaceededecbdb

    IN2:  djjcddjggbiigjhfghehhbgdigjicafgjcehhfgifadihiajgciagicdahcbajjbhifjiaajigdgdfhdiijjgaiejgegbbiigida
          djjcddjggbiigjhfg.ehhbgdigjic.fgjcehhfgi..di.i.j..i..i.....ba........aa..............a.............a

    OUT2: aaaaab[cc]igicgjihidfiejfijgidgbhhehgfhjgiibggjddjjd
          aaaaab[ii]jiidigfhecjgfcijgidgbhhehgfhjgiibggjddcjjd


     */


    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
//        String str = sc.nextLine();
        String str = "djjcddjggbiigjhfghehhbgdigjicafgjcehhfgifadihiajgciagicdahcbajjbhifjiaajigdgdfhdiijjgaiejgegbbiigida";

        char[] arr = str.toCharArray();

        Map<Character, Integer> map = createFreqMap(arr);

        System.out.println(map);

        char[] res = new char[arr.length];

        int boundary = res.length;

        for (Map.Entry<Character, Integer> entry : map.entrySet()) {
            boundary = place(entry, boundary, arr, res);
        }

        StringBuilder buf = new StringBuilder(res.length/2);

        for( int i = res.length-1; i >=0; --i){
            char ch = res[i];
            if( ch != '\u0000' ){
                buf.append(ch);
            }
        }

        System.out.println(buf);

    }

    private static int place(Map.Entry<Character, Integer> entry, int boundary, char[] original, char[] res) {

        char ch = entry.getKey();
        int count = entry.getValue();

        int newBoundary = boundary;

        // move 'left' from boundary
        for (int i = boundary - 1; i >= 0 && count !=0; --i, --newBoundary) {
            if (original[i] == ch) {
                res[i] = ch;
                --count;
            }
        }

        // move 'right' from boundary
        for (int i = boundary; i < original.length && count != 0; ++i) {
            if (original[i] == ch) {
                res[i] = ch;
                --count;
            }
        }

        return newBoundary;
    }

    private static Map<Character, Integer> createFreqMap(char[] arr) {
        Map<Character, Integer> map = new TreeMap<>();

        for (char ch : arr) {
            Integer freq = map.get(ch);

            if (freq == null) {
                freq = 1;
            }
            else {
                freq += 1;
            }

            map.put(ch, freq);
        }

        for (Map.Entry<Character, Integer> entry : map.entrySet()) {
            entry.setValue(entry.getValue() / 2);
        }

        return map;
    }
}
