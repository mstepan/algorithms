package com.max.algs.sorting;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.Iterator;

import org.apache.log4j.Logger;

public class SortedFileSequenceIterator implements Iterator<String> {

	private static final Logger LOG = Logger
			.getLogger(SortedFileSequenceIterator.class);

	private int curPos = 0;
	private int readedBytes = -1;

	private final byte[] buf = new byte[1024];

	private final RandomAccessFile randFile;

	private long handledBytesCount = 0;
	private String word;

	public SortedFileSequenceIterator(Path path) {
		try {
			randFile = new RandomAccessFile(path.toFile(), "r");
			next();
		}
		catch (FileNotFoundException ex) {
			throw new IllegalStateException(ex);
		}

	}

	public boolean fullyFinished() {
		try {
			return handledBytesCount == randFile.length();
		}
		catch (IOException ioEx) {
			LOG.error(ioEx);
		}

		return true;
	}

	@Override
	public boolean hasNext() {
		return word == null;
	}

	@Override
	public String next() {

		String retValue = word;

		word = extractWord();

		return retValue;
	}

	private String extractWord() {

		if (curPos < readedBytes) {

			StringBuilder wordBuf = new StringBuilder();

			while (true) {

				if (buf[curPos] == ' ') {
					break;
				}

				if (buf[curPos] == System.lineSeparator().charAt(0)) {
				}

				wordBuf.append(buf[curPos]);
				++curPos;
			}

			return wordBuf.toString();
		}

		// try top read next chunk
		else {
			readNextChunk();

			if (readedBytes < 0) {
				return null;
			}

			return "";
		}
	}

	private void readNextChunk() {
		try {
			readedBytes = randFile.read(buf);
		}
		catch (IOException ioEx) {
			throw new IllegalStateException(ioEx);
		}
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
