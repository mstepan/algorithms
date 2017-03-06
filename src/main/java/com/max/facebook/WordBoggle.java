package com.max.facebook;

import com.google.common.base.Objects;

import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Given a dictionary, a method to do lookup in dictionary and a M x N board where every cell has one character.
 * Find all possible words that can be formed by a sequence of adjacent characters. Note that we can move
 * to any of 8 adjacent characters, but a word should not have multiple instances of same cell.
 */
public class WordBoggle {

    private static final int[] CELL_OFFSETS = {0, 1, -1};

    private static final int BASE = 255;
    private static final int BIG_PRIME = 5_383_363;

    static {
        final long maxHashValuePossible = ((long) BASE) * (BIG_PRIME - 1);
        checkArgument(maxHashValuePossible <= Integer.MAX_VALUE,
                      "maxHashValuePossible > Integer.MAX_VALUE, %s > %s", maxHashValuePossible, Integer.MAX_VALUE);
    }

    private static final class Cell {
        final int row;
        final int col;

        Cell(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Cell cell = (Cell) o;

            return Objects.equal(row, cell.row)
                    && Objects.equal(col, cell.col);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(row, col);
        }
    }

    /**
     * time: O( 8^(n*m) )
     * space: O(maxWordLength) ~ O(1)
     */
    public static void printPossibleWords(char[][] arr, Set<String> dic) {

        checkNotNull(arr);
        checkNotNull(dic);

        int maxWordLength = 0;

        Set<Integer> dicHash = new HashSet<>();

        for (String singleWord : dic) {
            maxWordLength = Math.max(singleWord.length(), maxWordLength);
            dicHash.add(calculateHash(singleWord));
        }

        StringBuilder res = new StringBuilder(maxWordLength);
        Set<Cell> usedCells = new HashSet<>();

        int colsCount = arr[0].length;

        for (int row = 0, arrLength = arr.length; row < arrLength; ++row) {

            assert arr[row].length == colsCount : "arr[row].length != colsCount";

            for (int col = 0; col < colsCount; ++col) {
                printSolutionRec(0, new Cell(row, col), res, arr, dic, dicHash, usedCells, maxWordLength);
            }
        }

    }


    private static int calculateHash(String word) {
        int hash = 0;

        for (int i = 0; i < word.length(); ++i) {
            hash = ((hash * BASE) % BIG_PRIME + word.charAt(i)) % BIG_PRIME;
        }

        assert hash >= 0 : "negative 'hash' calculate from word";
        return hash;
    }

    private static int callsCount = 0;

    private static void printSolutionRec(int prevHashValue,
                                         Cell cur, StringBuilder res, char[][] arr, Set<String> dic, Set<Integer> dicHash,
                                         Set<Cell> usedCells, int maxWordLength) {

        int hashValue = ((prevHashValue * BASE) % BIG_PRIME + arr[cur.row][cur.col]) % BIG_PRIME;

        assert hashValue >= 0 : "Negative 'hashValue'";

        res.append(arr[cur.row][cur.col]);
        usedCells.add(cur);

        // compare hash value first
        if (dicHash.contains(hashValue)) {
            ++callsCount;

            // compare string value from StringBuilder using toString call (quite an expensive call)
            if (dic.contains(res.toString())) {
                System.out.println(res.toString());
            }
        }

        if (res.length() >= maxWordLength) {
            cleanCurState(cur, res, usedCells);
            return;
        }

        for (int rowOffset : CELL_OFFSETS) {
            for (int colOffset : CELL_OFFSETS) {

                // skip '0;0' offset
                if (rowOffset == 0 && colOffset == 0) {
                    continue;
                }

                int nextRow = cur.row + rowOffset;
                int nextCol = cur.col + colOffset;

                if (nextRow >= 0 && nextCol >= 0 && nextRow < arr.length && nextCol < arr[nextRow].length) {
                    printSolutionRec(hashValue, new Cell(nextRow, nextCol), res, arr, dic, dicHash, usedCells,
                                     maxWordLength);
                }
            }
        }

        assert res.length() < maxWordLength : "res.length() >= maxWordLength";

        cleanCurState(cur, res, usedCells);
    }

    private static void cleanCurState(Cell cur, StringBuilder res, Set<Cell> usedCells) {
        res.deleteCharAt(res.length() - 1);
        usedCells.remove(cur);
    }


}
