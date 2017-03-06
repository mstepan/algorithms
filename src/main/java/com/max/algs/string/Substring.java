package com.max.algs.string;

import java.util.NoSuchElementException;

public class Substring implements Comparable<Substring> {

	private final String str;
	private final int offset;

	public Substring(String str, int offset) {
		super();
		if (str == null) {
			throw new IllegalArgumentException("NULL 'str' passed");
		}

		if (offset < 0) {
			throw new IllegalArgumentException("'offset' can't be negative: "
					+ offset);
		}

		if (offset >= str.length()) {
			throw new IllegalArgumentException("'offset' is too big '" + offset
					+ "', should be in range [0;" + (str.length() - 1) + "]");
		}

		this.str = str;
		this.offset = offset;
	}

	public int getLength() {
		return str.length() - offset;
	}

	public char charAt(int index) {

		if (offset + index < 0 || offset + index >= str.length()) {
			throw new NoSuchElementException();
		}

		return str.charAt(offset + index);
	}

	@Override
	public int compareTo(Substring other) {

		int minLength = Math.min(getLength(), other.getLength());

		int i = 0;
		for (; i < minLength; i++) {

			int cmp = Character.compare(charAt(i), other.charAt(i));

			if (cmp != 0) {
				return cmp;
			}
		}

		if (i == minLength) {
			if (minLength == getLength()) {
				return -1;
			}
			return 1;
		}

		return 0;
	}

	public int commonLength(Substring other) {

		int count = 0;
		for (int i = 0; i < Math.min(getLength(), other.getLength()); i++) {

			if (charAt(i) != other.charAt(i)) {
				break;
			}

			++count;
		}

		return count;
	}

	@Override
	public String toString() {
		return str.substring(offset);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + offset;
		result = prime * result + ((str == null) ? 0 : str.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Substring other = (Substring) obj;
		if (offset != other.offset)
			return false;
		if (str == null) {
			if (other.str != null)
				return false;
		}
		else
			if (!str.equals(other.str))
				return false;
		return true;
	}

}
