package com.max.algs.ds.tree.geometry;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Balanced static K-D tree implementation. Not thread safe. See:
 * http://en.wikipedia.org/wiki/K-d_tree
 * 
 * All children to the left are less than the divide plane ( < ). All children
 * to the right are greater or equal to the divide plane ( >= ).
 * 
 * @author Maksym Stepanenko
 *
 */
public class KDTree {

	private final KDEntry root;

	/**
	 * K - dimensions count (in case of XYPoint, K = 2) N - points count
	 * 
	 * time: O(K*N*LgN) space: O(N)
	 */
	public KDTree(XYPoint[] points) {
		if (points == null) {
			throw new IllegalArgumentException("NULL 'points' passed");
		}
		if (points.length == 0) {
			throw new IllegalArgumentException("EMPTY 'points' passed");
		}

		XYPoint[] xOrdered = Arrays.copyOf(points, points.length);
		Arrays.sort(xOrdered, XYPoint.X_CMP);

		XYPoint[] yOrdered = Arrays.copyOf(points, points.length);
		Arrays.sort(yOrdered, XYPoint.Y_CMP);

		root = createTree(xOrdered, yOrdered, 0, xOrdered.length - 1, Axis.X);
	}

	protected KDEntry getRoot() {
		return root;
	}

	/**
	 * Searching for a nearest neighbour.
	 */
	public XYPoint findNearest(XYPoint base) {
		return null;
	}

	private KDEntry createTree(XYPoint[] xOrdered, XYPoint[] yOrdered,
			int from, int to, Axis axis) {

		if (from > to) {
			return null;
		}

		if (from == to) {
			return new KDEntry(
					axis == Axis.X ? xOrdered[from] : yOrdered[from], axis);
		}

		int middle = from + ((to - from) >>> 1);

		KDEntry entry = new KDEntry(axis == Axis.X ? xOrdered[middle]
				: yOrdered[middle], axis);

		if (axis == Axis.X) {
			changeArrays(yOrdered, xOrdered, from, middle, to);
		}
		else {
			changeArrays(xOrdered, yOrdered, from, middle, to);
		}

		XYPoint[] temp = xOrdered;
		xOrdered = yOrdered;
		yOrdered = temp;

		entry.left = createTree(xOrdered, yOrdered, from, middle - 1,
				axis.next());
		entry.right = createTree(xOrdered, yOrdered, middle + 1, to,
				axis.next());

		return entry;
	}

	private void changeArrays(XYPoint[] src, XYPoint[] dest, int from,
			int middle, int to) {

		Set<XYPoint> leftSet = new HashSet<>();

		for (int i = from; i < middle; i++) {
			leftSet.add(dest[i]);
		}

		Set<XYPoint> rightSet = new HashSet<>();

		for (int i = middle; i <= to; i++) {
			rightSet.add(dest[i]);
		}

		XYPoint middlePoint = dest[middle];
		boolean middleSet = false;

		int left = from;
		int right = middle + 1;

		for (int i = from; i <= to; i++) {

			XYPoint point = src[i];

			if (point.equals(middlePoint) && !middleSet) {
				middleSet = true;
			}
			else
				if (leftSet.contains(point)) {
					dest[left] = point;
					++left;
				}
				else
					if (rightSet.contains(point)) {
						dest[right] = point;
						++right;
					}
		}
	}

	private static enum Axis {
		X, Y;

		public Axis next() {
			Axis[] all = values();
			return all[(ordinal() + 1) % all.length];
		}
	}

	protected static final class KDEntry {

		final XYPoint point;
		final Axis axis;
		KDEntry left;
		KDEntry right;

		KDEntry(XYPoint point, Axis axis) {
			super();
			this.point = point;
			this.axis = axis;
		}

		@Override
		public String toString() {
			return String.valueOf(point) + ", axis: " + axis;
		}

	}

}
