package com.max.huffman.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * File character iterator.
 */
public final class FileCharsIterator implements Iterator<Character>, AutoCloseable {

    private final BufferedReader reader;

    private char value = Character.MIN_VALUE;
    private String line;
    private int index;

    public FileCharsIterator(Path inPath) {
        try {
            reader = Files.newBufferedReader(inPath);
            readNextLine();
        }
        catch (IOException ioEx) {
            throw new IllegalStateException(ioEx);
        }
    }

    @Override
    public boolean hasNext() {
        return value != Character.MIN_VALUE;
    }

    @Override
    public Character next() {

        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        assert index < line.length();

        char chToReturn = value;
        ++index;

        // read one more line
        if (index == line.length()) {
            readNextLine();
        }
        else {
            value = line.charAt(index);
        }

        return chToReturn;
    }

    private void readNextLine() {
        try {
            line = reader.readLine();

            // no new lines, file ended
            if (line == null) {
                value = Character.MIN_VALUE;
            }
            // fully empty line detected, just try read next one
            else if (line.length() == 0) {
                readNextLine();
            }
            else {
                assert line.length() > 0 : "Empty line detected";
                index = 0;
                value = line.charAt(index);
            }
        }
        catch (IOException ioEx) {
            throw new IllegalStateException(ioEx);
        }
    }

    @Override
    public void close() {
        try {
            reader.close();
        }
        catch (IOException ioEx) {
            throw new IllegalStateException(ioEx);
        }
    }
}
