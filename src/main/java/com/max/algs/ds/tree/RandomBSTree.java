package com.max.algs.ds.tree;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.max.algs.util.GeneralUtils;

/**
 * 
 * Don't allow NULL values and duplicates.
 * 
 */
public class RandomBSTree<T extends Comparable<T>> {

	private Node<T> root;

	private int heigh = -1;

	private static final Random RAND = ThreadLocalRandom.current();

	public boolean add(T newValue) {

		GeneralUtils.checkForNull(newValue);

		if (contains(newValue)) {
			return false;
		}

		if (isEmpty()) {
			root = new Node<T>(newValue);
			return true;
		}

		Node<T> cur = root;
		Node<T> newNode = new Node<T>(newValue);

		Node<T> rotateTillElem = null;

		while (true) {

			if (rotateTillElem == null) {

				boolean insertInRoot = (RAND.nextInt(cur.elemsCount + 1) == 0);

				if (insertInRoot) {
					rotateTillElem = cur;
					// LOG.info( "Will insert value: " + newNode.value
					// + ", to: " + rotateTillElem.value );
				}
			}

			cur.elemsCount += 1;

			if (cur.value.compareTo(newNode.value) > 0) {

				if (cur.left == null) {
					cur.left = newNode;
					newNode.parent = cur;
					break;
				}
				cur = cur.left;
			}
			else {
				if (cur.right == null) {
					cur.right = newNode;
					newNode.parent = cur;
					break;
				}
				cur = cur.right;
			}
		}

		if (rotateTillElem != null) {
			rotateTillElement(newNode, rotateTillElem);
		}

		heigh = -1;
		return true;

	}

	public boolean delete(T value) {
		GeneralUtils.checkForNull(value);

		if (!contains(value)) {
			return false;
		}

		heigh = -1;
		return true;

	}

	public boolean contains(T newValue) {

		GeneralUtils.checkForNull(newValue);

		Node<T> cur = root;

		while (cur != null) {
			int compRes = cur.value.compareTo(newValue);

			if (compRes == 0) {
				return true;
			}

			if (compRes > 0) {
				cur = cur.left;
			}
			else {
				cur = cur.right;
			}
		}

		return false;
	}

	/**
	 * Should be in range: [lgN; N]
	 * 
	 * lgN - best balanced N - worst balanced
	 * 
	 */
	public int heigh() {
		if (heigh < 0) {

			if (isEmpty()) {
				heigh = 0;
			}

			heigh = heighRec(root) - 1;
		}

		return heigh;
	}

	private int heighRec(Node<T> cur) {
		if (cur == null) {
			return 0;
		}

		return 1 + Math.max(heighRec(cur.left), heighRec(cur.right));

	}

	public int size() {
		return root == null ? 0 : root.elemsCount;
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	// ==== PRIVATE ====

	private void rotateTillElement(Node<T> cur, Node<T> end) {

		while (true) {

			Node<T> parent = cur.parent;

			if (parent.parent == null) {
				root = cur;
			}
			else
				if (parent.parent.left == parent) {
					parent.parent.left = cur;
				}
				else {
					parent.parent.right = cur;
				}

			// rotate right
			if (parent.right == cur) {

				parent.right = cur.left;

				if (cur.left != null) {
					cur.left.parent = parent;
				}

				cur.left = parent;
			}
			// rotate left
			else {

				parent.left = cur.right;

				if (cur.right != null) {
					cur.right.parent = parent;
				}

				cur.right = parent;
			}

			cur.parent = parent.parent;
			parent.parent = cur;

			parent.recalculateElemsCount();
			cur.recalculateElemsCount();

			if (parent == end) {
				break;
			}
		}

	}

	// ==== NESTED ====

	private static final class Node<U> {

		final U value;
		Node<U> left;
		Node<U> right;
		Node<U> parent;
		int elemsCount;

		Node(U value) {
			super();
			assert value != null : "NULL 'value' passed";
			this.value = value;
			this.elemsCount = 1;
		}

		void recalculateElemsCount() {
			this.elemsCount = 1 + (left == null ? 0 : left.elemsCount)
					+ (right == null ? 0 : right.elemsCount);
		}

		@Override
		public String toString() {
			return String.valueOf(value);
		}

	}

}
