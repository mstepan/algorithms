package com.max.algs.codeforces;


import java.util.HashMap;
import java.util.Map;

/**
 * http://codeforces.com/problemset/problem/551/B
 */
public final class Puzzle_555_B {

    private static int maxCnt1 = 0;
    private static int maxCnt2 = 0;

    private static Map<Character, Integer> createMap(String str) {
        Map<Character, Integer> freqMap = new HashMap<>();

        for (int i = 0, length = str.length(); i < length; i++) {
            freqMap.compute(str.charAt(i), (key, value) -> value == null ? 1 : value + 1);
        }

        return freqMap;
    }

    private static void generateSolution(Map<Character, Integer> base, Map<Character, Integer> s1, int cnt1,
                                         Map<Character, Integer> s2, int cnt2) {

        if (canConstruct(base, s1)) {
            subtract(base, s1);
            generateSolution(base, s1, cnt1 + 1, s2, cnt2);
            addAll(base, s1);
        }

        if (canConstruct(base, s2)) {
            subtract(base, s1);
            generateSolution(base, s1, cnt1, s2, cnt2 + 1);
            addAll(base, s1);
        }

//        System.out.printf("Possible solution: cnt1 = %d, cnt2 = %d %n", cnt1, cnt2);

        if (cnt1 + cnt2 > maxCnt1 + maxCnt2) {
            maxCnt1 = cnt1;
            maxCnt2 = cnt2;
        }


    }

    private static void subtract(Map<Character, Integer> base, Map<Character, Integer> other) {
        for (Map.Entry<Character, Integer> entry : other.entrySet()) {
            base.compute(entry.getKey(), (key, value) -> value - 1);
        }
    }

    private static void addAll(Map<Character, Integer> base, Map<Character, Integer> other) {

        for (Map.Entry<Character, Integer> entry : other.entrySet()) {
            base.compute(entry.getKey(), (key, value) -> value + 1);
        }

    }

    private static boolean canConstruct(Map<Character, Integer> base, Map<Character, Integer> other) {

        if (base.size() < other.size()) {
            return false;
        }

        for (Map.Entry<Character, Integer> otherEntry : other.entrySet()) {

            Integer baseChFreq = base.get(otherEntry.getKey());

            if (baseChFreq == null || Integer.compare(baseChFreq, otherEntry.getValue()) < 0) {
                return false;
            }
        }

        return true;
    }

    private static String constructSolution(String a, int cnt1, String b, int cnt2,
                                            Map<Character, Integer> baseMap,
                                            Map<Character, Integer> aMap,
                                            Map<Character, Integer> bMap) {

        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < cnt1; i++) {
            buf.append(a);
            subtract(baseMap, aMap);
        }

        for (int i = 0; i < cnt2; i++) {
            buf.append(b);
            subtract(baseMap, bMap);
        }

        for (Map.Entry<Character, Integer> entry : baseMap.entrySet()) {

            char ch = entry.getKey();
            int charCnt = entry.getValue();

            while (charCnt != 0) {
                buf.append(ch);
                --charCnt;
            }

        }

        return buf.toString();
    }

    public static void main(String[] args) {

        String a = "ab";
        Map<Character, Integer> aMap = createMap(a);

        String b = "aca";
        Map<Character, Integer> bMap = createMap(b);

        String base = "abbbaaccca";
        Map<Character, Integer> baseMap = createMap(base);

        generateSolution(baseMap, aMap, 0, bMap, 0);

        System.out.println(constructSolution(a, maxCnt1, b, maxCnt2, baseMap, aMap, bMap));


        System.out.println("Main done...");
    }

}
