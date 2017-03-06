package com.max.algs.string;

public final class PrefixTableCalculator {

	/**
	 * N - text length M - pattern length
	 * 
	 * O(M^2) + O(N+M+1)
	 * 
	 * @param pattern
	 * @param text
	 * @return
	 */
	public static boolean contains(String pattern, String text) {

		String combined = pattern + "$" + text;

		int[] prefixes = prefixTable(combined);

		for (int singlePrefix : prefixes) {
			if (singlePrefix == pattern.length()) {
				return true;
			}
		}

		return false;

	}

	/**
	 * time: O(N^2) space: O(1)
	 * 
	 * @param str
	 * @return
	 */
	public static int[] prefixTable(String str) {

		if (str == null) {
			throw new IllegalArgumentException("NULL 'str' parameter passed");
		}

		if (str.length() == 0) {
			return new int[] {};
		}

		final int strLength = str.length();
		int[] prefixTable = new int[strLength];
		prefixTable[0] = 0;

		for (int i = 1; i < str.length(); i++) {

			int patternIndex = i;
			int baseIndex = 0;

			while (patternIndex < strLength
					&& str.charAt(baseIndex) == str.charAt(patternIndex)) {
				++patternIndex;
				++baseIndex;
			}

			prefixTable[i] = baseIndex;
		}

		return prefixTable;
	}

	private PrefixTableCalculator() {
		super();
	}

}
