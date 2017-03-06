package com.max.algs.string;

import com.max.algs.annotations.ThreadSafe;
import com.max.algs.util.ArrayUtils;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Stateless utility class.
 *
 * @author mstepanenko
 */
@ThreadSafe(stateless = true)
public final class StringUtils {


    private static final Logger LOG = Logger.getLogger(StringUtils.class);

    private static final int MIN_ASCII_VALUE = 'a';
    private static final int MAX_ASCII_VALUE = 'z';

    private static final char EMPTY_CHARACTER = ' ';
    private static final int ASCII_MAX_CODE = 255;

    private static final int MIN_ASCII_PRINTABLE = 32;
    private static final int MAX_ASCII_PRINTABLE = 126;

    private static final int OFFSET_ASCII_PRINTABLE = 126 - 32 + 1;

    private static final Random RAND = ThreadLocalRandom.current();

    /**
     * Check is string 'perm' is permutation of string 'str' in parallel.
     * <p>
     * time: O(N*lnN / CPUs)
     * space: O(N)
     */
    public static boolean isPermutationParallel(String str, String perm) {
        checkNotNull(str);
        checkNotNull(perm);

        if (str == perm) {
            return true;
        }

        if (str.length() != perm.length()) {
            return false;
        }

        char[] strArr = str.toCharArray();
        Arrays.parallelSort(strArr);

        char[] permArr = perm.toCharArray();
        Arrays.parallelSort(permArr);

        final int threadsCount = Runtime.getRuntime().availableProcessors() - 1;

        @SuppressWarnings("unchecked")
        Future<Boolean>[] partialResults = new Future[threadsCount];

        ExecutorService exec = Executors.newFixedThreadPool(threadsCount);

        int from = 0;
        int chunkSize = (int) Math.ceil((double) strArr.length / threadsCount);

        AtomicBoolean globalStatus = new AtomicBoolean(Boolean.TRUE);

        for (int i = 0; i < threadsCount; ++i) {
            partialResults[i] = exec.submit(new ChunkTask(strArr, permArr, from, chunkSize, globalStatus));
            from += chunkSize;
        }

        boolean res = true;

        for (int i = 0; i < threadsCount; ++i) {
            try {
                if (!partialResults[i].get()) {
                    res = false;
                    break;
                }
            }
            catch (Exception ex) {
                ex.getCause().printStackTrace();
                res = false;
                break;
            }
        }

        exec.shutdownNow();

        return res;
    }

    private static class ChunkTask implements Callable<Boolean> {

        private final char[] strArrSorted;
        private final char[] permArrSorted;

        private final int from;
        private final int chunkSize;

        private final AtomicBoolean globalStatus;

        private static final int ITERATIONS_TO_CHECK_GLOBAL_STATUS = 1_000;

        ChunkTask(char[] strArrSorted, char[] permArrSorted, int from, int chunkSize,
                  AtomicBoolean globalStatus) {
            this.strArrSorted = strArrSorted;
            this.permArrSorted = permArrSorted;
            this.from = from;
            this.chunkSize = chunkSize;
            this.globalStatus = globalStatus;
        }

        @Override
        public Boolean call() throws Exception {

            final int to = Math.min(from + chunkSize, strArrSorted.length);

            int itCount = 0;

            for (int i = from; i < to && !Thread.currentThread().isInterrupted(); ++i, ++itCount) {

                // check global status every 1000 iterations.
                if (itCount == ITERATIONS_TO_CHECK_GLOBAL_STATUS) {
                    itCount = 0;

                    if (!globalStatus.get()) {
                        System.out.println("Premature stop");
                        return Boolean.TRUE;
                    }
                }

                if (strArrSorted[i] != permArrSorted[i]) {
                    globalStatus.set(false);
                    return Boolean.FALSE;
                }
            }

            return Boolean.TRUE;
        }
    }


    /**
     * Check is string 'perm' is permutation of stirng 'str'.
     * <p>
     * time: O(N)
     * space: O(N)
     */
    public static boolean isPermutation(String str, String perm) {

        checkNotNull(str);
        checkNotNull(perm);

        if (str.length() != perm.length()) {
            return false;
        }

        if (str.equals(perm)) {
            return true;
        }

        Map<Character, Integer> charsFreq = new HashMap<>();

        for (int i = 0, strLength = str.length(); i < strLength; ++i) {
            charsFreq.compute(str.charAt(i), (symbol, cnt) -> cnt == null ? 1 : cnt + 1);
        }

        char ch;
        for (int i = 0, permLength = perm.length(); i < permLength; ++i) {

            ch = perm.charAt(i);

            Integer freq = charsFreq.get(ch);

            if (freq == null) {
                return false;
            }

            // remove character from map
            if (freq == 1) {
                charsFreq.remove(ch);
            }
            else {
                charsFreq.put(ch, freq - 1);
            }
        }

        return true;
    }

    /**
     * Given two strings 'base' and 'other', determine if they are isomorphic.
     * Two strings are isomorphic if the characters in 'base' can be replaced to get 'other'.
     * All occurrences of a character must be replaced with another character while preserving the order of characters.
     * No two characters may map to the same character but a character may map to itself.
     * <p>
     * For example,
     * Given "egg", "add", return true.
     * Given "foo", "bar", return false.
     * Given "paper", "title", return true.
     * Given "ab", "aa", return false.
     * <p>
     * time: O(N)
     * space: O(N)
     */
    public static boolean isIsomorphic(String baseStr, String otherStr) {

        checkArgument(baseStr != null, "null 'baseStr' parameter passed");
        checkArgument(otherStr != null, "null 'otherStr' parameter passed");

        if (baseStr.length() != otherStr.length()) {
            return false;
        }

        Map<Character, Character> map = new HashMap<>();
        Set<Character> alreadyMapped = new HashSet<>();

        for (int i = 0; i < baseStr.length(); i++) {
            char baseCh = baseStr.charAt(i);
            char otherCh = otherStr.charAt(i);

            if (map.containsKey(baseCh)) {
                if (map.get(baseCh) != otherCh) {
                    return false;
                }
            }
            else {
                if (!alreadyMapped.add(otherCh)) {
                    return false; // case 'ab', 'aa' will return false here
                }
                map.put(baseCh, otherCh);
            }
        }

        return true;
    }

    public static String randomLowerCase(int length) {
        StringBuilder buf = new StringBuilder(length);

        final int fromChar = 'a';
        final int toChar = 'z';
        final int charsCount = toChar - fromChar + 1;

        for (int i = 0; i < length; i++) {
            buf.append((char) (fromChar + RAND.nextInt(charsCount)));
        }

        return buf.toString();
    }

    /**
     * Given s string, Find max size of a sub-string, in which no duplicate
     * chars present.
     * <p>
     * time: O(N) space: O(windowSize)
     * <p>
     * Use sliding window to detect duplicate characters. Window [from;to] never
     * contains duplicate characters.
     */

    public static String maxSubstrWithoutDuplicates(String str) {

        if (str == null) {
            throw new IllegalArgumentException("NULL string passed'");
        }

        Set<Character> window = new HashSet<>();

        int from = 0;
        int to = 0;

        int maxLength = 0;
        int maxStartIndex = 0;

        while (to < str.length()) {

            char ch = str.charAt(to);

            if (!window.contains(ch)) {
                window.add(ch);
            }
            else {
                while (str.charAt(from) != ch) {
                    window.remove(str.charAt(from));
                    ++from;
                }
                ++from;
            }

            int curLength = to - from + 1;

            if (maxLength < curLength) {
                maxLength = curLength;
                maxStartIndex = from;
            }
            ++to;
        }

        return str.substring(maxStartIndex, maxStartIndex + maxLength);

    }

    /**
     * time: O(N*M) space: O(N*M)
     * <p>
     * Suppose you are given three strings of characters: X, Y ,and Z,where |X|
     * = n, |Y | = m,and |Z| = n + m. Z is said to be a shuffle of X and Y iﬀ Z
     * can be formed by interleaving the characters from X and Y in a way that
     * maintains the left-to-right ordering of the characters from each string.
     * (a) Show that "cchocohilaptes" is a shuffle of chocolate and chips, but
     * "chocochilatspe" is not. (b) Give an efficient dynamic-programming
     * algorithm that determines whether Z is a shuffle of X and Y . Hint: the
     * values of the dynamic programming matrix you construct should be Boolean,
     * not numeric.
     */
    public static boolean isShuffle(String x, String y, String z) {

        boolean[][] sol = new boolean[x.length() + 1][y.length() + 1];

        sol[0][0] = false;

        for (int i = 0; i < y.length(); i++) {

            if (y.charAt(i) != z.charAt(i)) {
                break;
            }
            sol[0][i + 1] = true;
        }

        for (int i = 0; i < x.length(); i++) {

            if (x.charAt(i) != z.charAt(i)) {
                break;
            }
            sol[i + 1][0] = true;
        }

        for (int i = 1; i < sol.length; i++) {
            for (int j = 1; j < sol[i].length; j++) {

                int k = i + j;

                char zCh = z.charAt(k - 1);
                char xCh = x.charAt(i - 1);
                char yCh = y.charAt(j - 1);

                if (xCh != zCh && yCh != zCh) {
                    sol[i][j] = false;
                }
                else {

                    if ((xCh == zCh) && (xCh == yCh)) {
                        sol[i][j] = sol[i][j - 1] || sol[i - 1][j];
                    }
                    else if (xCh == zCh) {
                        sol[i][j] = sol[i - 1][j];
                    }
                    else if (yCh == zCh) {
                        sol[i][j] = sol[i][j - 1];
                    }
                }
            }
        }

        int lastRow = sol.length - 1;
        int lastColumn = sol[lastRow].length - 1;

        return sol[lastRow][lastColumn];
    }

    public static boolean isKPalindrome(String str, int k) {
        char[] arr = str.toCharArray();
        return isKPalindromeRec(arr, k, 0, arr.length - 1);
    }

    private static boolean isKPalindromeRec(char[] arr, int k, int left,
                                            int right) {

        if (left >= right) {
            return k >= 0;
        }

        if (arr[left] == arr[right]) {
            return isKPalindromeRec(arr, k, left + 1, right - 1);
        }

        return isKPalindromeRec(arr, k - 1, left + 1, right)
                || isKPalindromeRec(arr, k - 1, left, right - 1);
    }

    public static String shuffle(String x, String y) {

        final int totalLength = x.length() + y.length();

        StringBuilder buf = new StringBuilder(totalLength);

        int i = 0;
        int j = 0;

        for (int k = 0; k < totalLength; k++) {

            if (i >= x.length()) {
                buf.append(y.charAt(j));
                ++j;
            }
            else if (j >= y.length()) {
                buf.append(x.charAt(i));
                ++i;
            }
            else {
                if (RAND.nextBoolean()) {
                    buf.append(x.charAt(i));
                    ++i;
                }
                else {
                    buf.append(y.charAt(j));
                    ++j;
                }
            }

        }

        return buf.toString();

    }

    /**
     * time: O(n) space: O(1)
     */
    public static boolean isSorted(String str) {

        if (str == null) {
            throw new IllegalArgumentException("NULL 'str' parameter passed");
        }

        if (str.length() < 2) {
            return true;
        }

        for (int i = 1; i < str.length(); i++) {
            if (str.charAt(i - 1) > str.charAt(i)) {
                return false;
            }
        }

        return true;
    }

    /**
     * time: O(N*N) space: O(N)
     */
    public static String longestPalindromeSubstring(String str) {

        String reverted = reverse(str);

        int[] maxSub = new int[str.length()];

        int maxStrLength = 1;
        int maxStrEnd = 0;

        for (int i = 0; i < str.length(); i++) {

            int[] newSub = new int[str.length()];

            char baseCh = str.charAt(i);

            newSub[0] = (baseCh == reverted.charAt(0) ? 1 : 0);

            for (int j = 1; j < reverted.length(); j++) {

                char revCh = reverted.charAt(j);

                if (revCh == baseCh) {

                    newSub[j] = 1 + maxSub[j - 1];

                    if (newSub[j] > maxStrLength) {
                        maxStrEnd = j;
                        maxStrLength = newSub[j];
                    }
                }
            }

            maxSub = newSub;
        }

        StringBuilder maxStrBuf = new StringBuilder(maxStrLength);

        int index = maxStrEnd;

        while (maxStrLength > 0) {
            maxStrBuf.append(reverted.charAt(index));
            --maxStrLength;
            --index;
        }

        return maxStrBuf.toString();
    }

    /**
     * Time: O(N) Space: O(1), actually O(N), because we return new String(arr)
     */
    public static String encodeSpaces(char[] arr) {

        if (arr == null) {
            throw new IllegalArgumentException("NULL 'arr' parameter passed");
        }

        int index = arr.length - 1;

        int spacesAtEnd = 0;
        int spacesInMiddle = 0;

        int lastChIndex = -1;

        while (index >= 0) {

            if (arr[index] != ' ') {

                if (lastChIndex < 0) {
                    lastChIndex = index;
                }
            }
            else if (arr[index] == ' ') {
                if (lastChIndex != -1) {
                    ++spacesInMiddle;
                }
                else {
                    ++spacesAtEnd;
                }
            }
            --index;
        }

        if (2 * spacesInMiddle > spacesAtEnd) {
            throw new IllegalArgumentException(
                    "Spaces count at the end of array isn't enought: spaces count in the end = "
                            + spacesAtEnd + ", min spaces needed = " + 2
                            * spacesInMiddle);
        }

        int pos = lastChIndex + 2 * spacesInMiddle;
        final int resLength = pos + 1;

        for (int i = lastChIndex; i >= 0; i--) {
            if (arr[i] == ' ') {
                arr[pos] = '0';
                arr[pos - 1] = '2';
                arr[pos - 2] = '%';
                pos -= 3;
            }
            else {
                arr[pos] = arr[i];
                --pos;
            }
        }

        return new String(arr, 0, resLength);

    }

    /**
     * Replace space character with '%20' character.
     * <p>
     * time: O(N) space: O(N)
     */
    public static String encodeSpaces(String str) {

        checkNotNull(str);

        int spacesCount = 0;

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (ch == ' ') {
                ++spacesCount;
            }
        }

        if (spacesCount == 0) {
            return str;
        }

        char[] arr = new char[str.length() + 2 * spacesCount];

        int arrIndex = 0;
        for (int i = 0; i < str.length(); i++) {

            char ch = str.charAt(i);

            if (ch == ' ') {
                arr[arrIndex] = '%';
                arr[arrIndex + 1] = '2';
                arr[arrIndex + 2] = '0';
                arrIndex += 3;
            }
            else {
                arr[arrIndex] = ch;
                ++arrIndex;
            }
        }

        return new String(arr);
    }

    /**
     * Check is str1 is permutation of str2.
     * <p>
     * Time: O(N) Space: O(1)
     */
    public static boolean isSamePermutaion(String str1, String str2) {

        checkNotNull(str1);
        checkNotNull(str2);

        if (str1.length() != str2.length()) {
            return false;
        }

        int xored = 0;

        int length = str1.length();

        for (int i = 0; i < length; i++) {
            xored = xored ^ str1.charAt(i) ^ str2.charAt(i);
        }

        return xored == 0;

    }

    /**
     * Random shuffle a string.
     * <p>
     * time: O(N)
     * space: (N)
     */
    public static String shuffle(String str) {
        checkNotNull(str, "'null' string passed");
        char[] arr = str.toCharArray();

        int index;
        char ch;

        for (int i = 0, arrLength = arr.length; i < arrLength - 1; i++) {

            index = i + RAND.nextInt(arrLength - i);

            ch = arr[i];
            arr[i] = arr[index];
            arr[index] = ch;
        }

        return new String(arr);
    }

    /**
     * time: O(N) space: O(N), cause we need to copy string value to a new array
     */
    public static String reverse(String str) {
        checkNotNull(str);

        char[] arr = str.toCharArray();

        int left = 0;
        int right = arr.length - 1;

        while (left < right) {
            ArrayUtils.swap(arr, left++, right--);
        }

        return new String(arr);
    }

    public static String removeDuplicatedChars(String str) {

        checkNotNull(str);

        char[] arr = str.toCharArray();

        int lastPos = -1;

        BitSet foundChars = new BitSet(OFFSET_ASCII_PRINTABLE);

        for (int i = 0; i < arr.length; i++) {

            int code = arr[i] - MIN_ASCII_PRINTABLE;

            if (!foundChars.get(code)) {
                foundChars.set(code);
                arr[lastPos + 1] = arr[i];
                ++lastPos;
            }
        }

        return new String(arr, 0, lastPos + 1);
    }

    private static final char[] DNA_CHARS = "ACTG".toCharArray();

    /**
     * Generate random DNA string with specified length.
     */
    public static String generateDNAString(int length) {

        StringBuilder buf = new StringBuilder(length);

        for (int i = 0; i < length; ++i) {
            buf.append(DNA_CHARS[RAND.nextInt(DNA_CHARS.length)]);
        }

        return buf.toString();
    }

    /**
     * Generate random string with printable ASCII characters.
     * <p>
     * 32 - 126 => printable ASCII characters
     *
     * @return
     */
    public static String generateAsciiString(int length) {

        if (length < 0) {
            throw new IllegalArgumentException(
                    "Negative string length passed '" + length + "'");
        }

        StringBuilder buf = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int chCode = MIN_ASCII_PRINTABLE
                    + RAND.nextInt(OFFSET_ASCII_PRINTABLE);

            if (chCode < MIN_ASCII_PRINTABLE || chCode > MAX_ASCII_PRINTABLE) {
                throw new IllegalStateException(
                        "Non printable ASCII character generated, chCode '"
                                + chCode + "'");
            }

            buf.append((char) chCode);

        }

        return buf.toString();
    }

    /**
     * time: O(N) space: O(N)
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean isCircular(String a, String b) {

        if (a == null || b == null) {
            throw new IllegalArgumentException("NULL string parameter passed");
        }

        if (a.length() != b.length()) {
            return false;
        }

        if (a.length() == 0) {
            return true;
        }

        String combinedB = b + b;

        if (combinedB.contains(a)) {
            return true;
        }

        return false;
    }

    /**
     * time: O(N) space: O(N)
     *
     * @param str
     * @param distance
     * @return
     */
    public static String rotateString(String str, int distance) {

        if (str == null) {
            throw new IllegalArgumentException("NULL 'str' parameter passed");
        }

        if (distance < 0) {
            throw new IllegalArgumentException("Negative 'distance' passed: "
                                                       + distance);
        }

        if (distance > str.length()) {
            throw new IllegalArgumentException(
                    "'distance' to rotate is greater then string length: "
                            + distance + " > " + str.length());
        }

        if (distance == 0) {
            return str;
        }

        StringBuilder buf = new StringBuilder(str.length());

        int index = distance;

        while (true) {

            buf.append(str.charAt(index));

            index = (index + 1) % str.length();

            if (index == distance) {
                break;
            }
        }

        return buf.toString();

    }

    /**
     * Given some random letters , For example "a,e,o,g,z,k,l,j,w,n" and a
     * dictionary. Try to find a word in the dictionary that has most letters
     * given
     * <p>
     * Space: O(N), because of 'word.toLowerCase().toCharArray() ' Time: O(N*M +
     * M*lgM), where: N - word.size() M - chars.length
     */
    public static String getMostSimilar(char[] chars, Set<String> words) {

        if (chars == null) {
            throw new IllegalArgumentException("NULL 'chars' parameter passed");
        }

        /** O(M) */
        for (int i = 0; i < chars.length; i++) {
            chars[i] = Character.toLowerCase(chars[i]);
        }

        /** O(M*logM) */
        Arrays.sort(chars);

        String mostSimilarWord = null;
        int maxSimilarity = -1;

        /** O(N) */
        for (String word : words) {

            /** skip words with 'length' < 'maxSimilarity' */
            if (word.length() > maxSimilarity) {

                /** O(M + K*lgK), where K - word.length() */
                int curSimilarity = calculateSimilarity(chars, word
                        .toLowerCase().toCharArray());

                LOG.info(word + ", similarity: " + curSimilarity);

                if (curSimilarity > maxSimilarity) {
                    maxSimilarity = curSimilarity;
                    mostSimilarWord = word;
                }
            }
        }

        return mostSimilarWord;
    }

    private static int calculateSimilarity(char[] chars, char[] word) {

        /** O(M *lgM) */
        Arrays.sort(word);

        int i = 0;
        int j = 0;

        int similarity = 0;

        while (i < chars.length && j < word.length) {

            if (chars[i] == word[j]) {
                ++similarity;
                ++i;
                ++j;
            }
            else if (chars[i] > word[j]) {
                ++j;
            }
            else {
                ++i;
            }

        }

        return similarity;

    }

    /**
     * time: O(N*lgN) space: O(N)
     */
    public static boolean hasUniqueCharsSorting(String str) {

        char[] arr = str.toCharArray();

        Arrays.sort(arr);

        for (int i = 1; i < arr.length; i++) {
            if (arr[i - 1] == arr[i]) {
                return false;
            }
        }

        return true;
    }

    public static boolean hasUniqueCharsBitset(String str) {

        int minValue = 0;
        int maxValue = 0;

        for (int i = 0; i < str.length(); i += 2) {

            int ch1 = str.charAt(i);

            if (i + 1 >= str.length()) {
                minValue = Math.min(ch1, minValue);
                maxValue = Math.max(ch1, maxValue);
            }
            else {
                int ch2 = str.charAt(i + 1);

                if (ch1 > ch2) {
                    int temp = ch1;
                    ch1 = ch2;
                    ch2 = temp;
                }

                // 'ch1' < 'ch2'
                minValue = Math.min(ch1, minValue);
                maxValue = Math.max(ch2, maxValue);
            }
        }

        final BitSet chars = new BitSet(maxValue - minValue + 1);

        for (int i = 0; i < str.length(); i++) {

            int chValue = str.charAt(i) - minValue;

            if (chars.get(chValue)) {
                return false;
            }

            chars.set(chValue);
        }

        return true;
    }

    public static boolean hasUniqueCharactersHashing(String str) {
        final int strLength = str.length();

        Set<Character> chars = new HashSet<>();

        char ch;
        for (int i = 0; i < strLength; i++) {
            ch = str.charAt(i);
            if (chars.contains(ch)) {
                return false;
            }
            chars.add(ch);
        }

        return true;
    }

    /**
     * time: O(N^2) space: O(1)
     */
    public static String longestStringWithoutRepBruteforce(String str) {

        String maxStr = "";

        for (int i = 0; i < str.length(); i++) {
            for (int j = i + 1; j < str.length() + 1; j++) {
                String curStr = str.substring(i, j);
                if (hasUniqueCharactersHashing(curStr)
                        && curStr.length() > maxStr.length()) {
                    maxStr = curStr;
                }
            }
        }

        return maxStr;
    }

    /**
     * Given an ASCII string, return the longest substring with unique
     * characters. Ex: dabcade => Ans: bcade.
     * <p>
     * time: O(N) space: O(N)
     */
    public static String longestStringWithoutRep(String str) {

        if (str == null) {
            throw new IllegalArgumentException("NULL 'str' parameter passed");
        }

        if ("".equals(str)) {
            return "";
        }

        final int[] prevRep = new int[str.length()];
        final Map<Character, Integer> lastRepPosMap = new HashMap<>();
        char ch;

        for (int i = 0; i < prevRep.length; i++) {

            ch = str.charAt(i);

            Integer lastRepPos = lastRepPosMap.get(ch);

            if (lastRepPos == null) {
                prevRep[i] = -1;
            }
            else {
                prevRep[i] = lastRepPos;
            }

            lastRepPosMap.put(ch, i);
        }

        int prevOpt = 0;
        int opt;

        int optMax = 0;
        int maxIndex = 0;

        for (int i = 0; i < str.length(); i++) {

            opt = 1 + Math.min(prevOpt, i - prevRep[i] - 1);

            if (opt > optMax) {
                maxIndex = i;
                optMax = opt;
            }

            prevOpt = opt;
        }

        final int fromIndex = maxIndex - optMax + 1;
        final int toIndex = maxIndex + 1;

        return str.substring(fromIndex, toIndex);
    }

    /**
     * time: O(N) space: O(1)
     * <p>
     * All character in range [0 ... 255]
     *
     * @param str
     * @return
     */
    public static char firstNonRepeatedCharAscii(String str) {
        checkNotNull(str);

        int[] charsCountArr = new int[ASCII_MAX_CODE + 1];

        int strLength = str.length();
        char ch;

        for (int i = 0; i < strLength; i++) {
            ch = str.charAt(i);
            ++charsCountArr[ch];
        }

        for (int i = 0; i < strLength; i++) {
            ch = str.charAt(i);
            if (charsCountArr[ch] == 1) {
                return ch;
            }
        }

        return EMPTY_CHARACTER;
    }

    /**
     * Check if string contains only ASCII characters, up to extended ASCII [0
     * ... 255]
     *
     * @param str
     * @return true - if string contains only ASCII characters, false -
     * otherwise
     */
    public static boolean isAscciString(String str) {

        int strLength = str.length();
        char ch;

        for (int i = 0; i < strLength; i++) {
            ch = str.charAt(i);

            int code = ch;

            if (code < 0 || code > 255) {
                return false;
            }
        }

        return true;

    }

    /**
     * First non repeating character from a given string
     */
    public static char firstNonRepeatedChar(String str) {
        checkNotNull(str);

        if (isAscciString(str)) {
            return firstNonRepeatedCharAscii(str);
        }

        Map<Character, Integer> charsCount = new HashMap<Character, Integer>();

        int strLength = str.length();
        char ch;

        for (int i = 0; i < strLength; i++) {
            ch = str.charAt(i);

            int code = ch;

            if (code >= 0 && code <= 255) {

            }

            Integer curChCount = charsCount.get(ch);

            if (curChCount == null) {
                curChCount = 1;
            }
            else {
                ++curChCount;
            }

            charsCount.put(ch, curChCount);
        }

        for (int i = 0; i < strLength; i++) {
            ch = str.charAt(i);

            if (charsCount.get(ch) == 1) {
                return ch;
            }
        }

        return EMPTY_CHARACTER;
    }

    public static String generateRandomString(int length) {

        final Random randGen = new Random();
        final StringBuilder buf = new StringBuilder(length);

        while (length > 0) {
            buf.append((char) randGen.nextInt());
            --length;
        }

        return buf.toString();
    }

    public static boolean isEquals(String str1, String str2) {

        if (str1 == str2) {
            return true;
        }

        if (str1 == null) {
            return str2 == null ? true : false;
        }

        if (str1.length() != str2.length()) {
            return false;
        }

        int res = 0;

        for (int i = 0; res == 0 && i < str1.length(); i++) {
            res |= str1.charAt(i) ^ str2.charAt(i);
        }

        return res == 0;
    }

    public static String createASCIIString(int length) {
        char[] arr = new char[length];

        int offset = MAX_ASCII_VALUE - MIN_ASCII_VALUE + 1;
        int charValue;
        for (int i = 0; i < arr.length; i++) {
            charValue = MIN_ASCII_VALUE + RAND.nextInt(offset);
            arr[i] = (char) charValue;
        }

        return new String(arr);
    }

    private StringUtils() {
        super();
    }
}
