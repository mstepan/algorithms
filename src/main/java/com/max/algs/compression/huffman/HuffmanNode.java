package com.max.algs.compression.huffman;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

class HuffmanNode implements Comparable<HuffmanNode> {

	final int freq;
	char ch;
	HuffmanNode left; // '0' node
	HuffmanNode right; // '1' node

	HuffmanNode(HuffmanNode left, HuffmanNode right) {
		checkNotNull(left);
		checkNotNull(right);
		this.left = left;
		this.right = right;
		this.freq = left.freq + right.freq;
	}

	HuffmanNode(char ch, int freq) {
		checkArgument(freq >= 0, "Frequency can't be negative '%s'", freq);
		this.ch = ch;
		this.freq = freq;
	}

	boolean isLeaf() {
		return left == null;
	}

	@Override
	public String toString() {
		if (isLeaf()) {
			return ch + ": " + freq;
		}
		return "composite: " + freq;
	}

	@Override
	public int compareTo(HuffmanNode other) {
		return Integer.compare(freq, other.freq);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ch;
		result = prime * result + freq;
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
		HuffmanNode other = (HuffmanNode) obj;
		if (ch != other.ch)
			return false;
		if (freq != other.freq)
			return false;
		return true;
	}

}
