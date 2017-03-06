package com.max.algs.dynamic.formatter;

import java.util.ArrayList;
import java.util.List;

public class FormattedText {

	public static final FormattedText EMPTY = new FormattedText(
			new ArrayList<String>(), 0);

	private final List<String> lines;
	private final int cost;

	private static final String LINE_SEPARATOR = System
			.getProperty("line.separator");

	public FormattedText(String str, List<String> prevLines, int lineSize) {
		lines = new ArrayList<>();
		lines.add(str);
		lines.addAll(prevLines);
		cost = calculateCost(lines, lineSize);
	}

	public FormattedText(List<String> lines, int lineSize) {
		super();
		this.lines = lines;
		this.cost = calculateCost(lines, lineSize);
	}

	public List<String> getLines() {
		return lines;
	}

	public int getCost() {
		return cost;
	}

	private int calculateCost(List<String> lines, int lineSize) {

		int newCost = 0;

		for (String singleLine : lines) {
			newCost += lineSize - singleLine.length();
		}

		return newCost;
	}

	@Override
	public String toString() {

		StringBuilder buf = new StringBuilder();

		if (lines.isEmpty()) {
			return "";
		}

		buf.append(lines.get(0));

		for (int i = 1; i < lines.size(); i++) {
			buf.append(LINE_SEPARATOR).append(lines.get(i));
		}

		return buf.toString();
	}

}
