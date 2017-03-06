package com.max.algs.string;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.max.algs.util.MatrixUtils;

/*
 * Given a 2d matrix with characters and a dictionary. 
 * Find all the valid words in the 2d matrix. 
 * The words can be towards right, left , up or down. Exact code to be given.
 * 
 * 
 * Check through boundary added.
 * 
 * add diagonal movement
 */
public class SearchWordsInMatrix {

	public List<String> findValidWords(char[][] data, String[] dictionary) {

		if (!MatrixUtils.isMatrix(data)) {
			throw new IllegalArgumentException("Not a matrix passed");
		}

		List<String> words = new ArrayList<>();

		Map<Character, List<String>> posMap = createPositionsMap(data);

		String[] rowColStr;

		for (String word : dictionary) {

			char firstCh = word.charAt(0);

			List<String> charLocations = posMap.get(firstCh);

			if (charLocations != null) {
				for (String location : charLocations) {

					rowColStr = location.split(";");

					if (hasFullWord(word.toCharArray(), 0, data,
							Integer.valueOf(rowColStr[0]),
							Integer.valueOf(rowColStr[1]))) {
						words.add(word);
						break;
					}
				}
			}
		}

		return words;
	}

	private boolean hasFullWord(char[] word, int baseIndex, char[][] data,
			int baseRow, int baseCol) {

		Deque<Integer> workingStack = new ArrayDeque<>();

		workingStack.push(baseCol);
		workingStack.push(baseRow);
		workingStack.push(baseIndex);

		int index;
		int row;
		int col;

		while (!workingStack.isEmpty()) {

			index = workingStack.pop();
			row = workingStack.pop();
			col = workingStack.pop();

			if (row < 0 || row >= data.length || col < 0
					|| col >= data[row].length) {
				continue;
			}

			if (index >= word.length) {
				return true;
			}

			if (word[index] != data[row][col]) {
				continue;
			}

			// left
			workingStack.push((col - 1 >= 0 ? col - 1 : data[row].length - 1));
			workingStack.push(row);
			workingStack.push(index + 1);

			// up
			workingStack.push(col);
			workingStack.push((row - 1 >= 0 ? row - 1 : data.length - 1));
			workingStack.push(index + 1);

			// right
			workingStack.push((col + 1) % data[row].length);
			workingStack.push(row);
			workingStack.push(index + 1);

			// down
			workingStack.push(col);
			workingStack.push((row + 1) % data.length);
			workingStack.push(index + 1);

			// diagonal. right down
			workingStack.push((col + 1) % data[row].length);
			workingStack.push((row + 1) % data.length);
			workingStack.push(index + 1);

			// diagonal, left down
			workingStack.push((col - 1) >= 0 ? col - 1 : data[row].length - 1);
			workingStack.push((row + 1) % data.length);
			workingStack.push(index + 1);

			// diagonal, left up
			workingStack.push((col - 1) >= 0 ? col - 1 : data[row].length - 1);
			workingStack.push((row - 1) >= 0 ? row - 1 : data.length - 1);
			workingStack.push(index + 1);

			// diagonal, right up
			workingStack.push((col + 1) % data[row].length);
			workingStack.push((row - 1) >= 0 ? row - 1 : data.length - 1);
			workingStack.push(index + 1);
		}

		return false;
	}

	private Map<Character, List<String>> createPositionsMap(char[][] data) {

		Map<Character, List<String>> charLocation = new HashMap<>();

		char ch;
		List<String> positions;

		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {

				ch = data[i][j];

				positions = charLocation.get(ch);

				if (positions == null) {
					positions = new ArrayList<>();
					charLocation.put(ch, positions);
				}

				positions.add(new String(i + ";" + j));
			}
		}

		return charLocation;

	}

}
