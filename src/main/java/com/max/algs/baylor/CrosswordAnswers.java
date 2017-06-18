package com.max.algs.baylor;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Crossword Answers puzzle "From Baylor to Baylor" book.
 */
public final class CrosswordAnswers {

    private static final char DELIMITER = '*';

    private CrosswordAnswers() throws Exception {

        char[][] data = {
                "AIM*DEN".toCharArray(),
                "*ME*ONE".toCharArray(),
                "UPON*TO".toCharArray(),
                "SO*ERIN".toCharArray(),
                "*SA*OR*".toCharArray(),
                "IES*DEA".toCharArray()
        };

        Map<Direction, List<String>> words = getAllWords(data);

        for (Direction direction : Direction.values()) {
            System.out.println(direction.name().toLowerCase());
            words.get(direction).forEach(System.out::println);
        }
    }

    private static Map<Direction, List<String>> getAllWords(char[][] data) {

        Map<Direction, List<String>> allWords = new EnumMap<>(Direction.class);

        List<String> acrossWords = new ArrayList<>();
        allWords.put(Direction.ACROSS, acrossWords);

        List<String> downWords = new ArrayList<>();
        allWords.put(Direction.DOWN, downWords);

        final int rows = data.length;
        final int cols = data[0].length;
        int index = 0;
        char ch;

        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {

                ch = data[row][col];

                if (ch == DELIMITER) {
                    continue;
                }

                if (hasAcrossWord(data, row, col) || hasDownWord(data, row, col)) {
                    ++index;
                }

                // read by horizontal
                if (hasAcrossWord(data, row, col)) {
                    acrossWords.add(readHorizontalWord(data, row, col, index));
                }
                // read by vertical
                if (hasDownWord(data, row, col)) {
                    downWords.add(readVerticalWord(data, row, col, index));
                }
            }
        }

        return allWords;
    }

    private static boolean hasAcrossWord(char[][] data, int row, int col) {
        return col == 0 || (col > 0 && data[row][col - 1] == DELIMITER);
    }

    private static boolean hasDownWord(char[][] data, int row, int col) {
        return row == 0 || (row > 0 && data[row - 1][col] == DELIMITER);
    }

    private static String readHorizontalWord(char[][] data, int curRow, int curCol, int index) {

        StringBuilder buf = new StringBuilder().append(index).append(": ");
        for (int col = curCol; col < data[curRow].length; ++col) {
            char ch = data[curRow][col];

            if (ch == DELIMITER) {
                break;
            }

            buf.append(ch);
        }
        return buf.toString();
    }

    private static String readVerticalWord(char[][] data, int curRow, int curCol, int index) {

        StringBuilder buf = new StringBuilder().append(index).append(": ");

        for (int row = curRow; row < data.length; ++row) {
            char ch = data[row][curCol];

            if (ch == DELIMITER) {
                break;
            }

            buf.append(ch);
        }
        return buf.toString();
    }

    public static void main(String[] args) {
        try {
            new CrosswordAnswers();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private enum Direction {
        ACROSS,
        DOWN
    }
}
