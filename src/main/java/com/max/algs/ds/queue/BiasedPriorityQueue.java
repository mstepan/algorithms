package com.max.algs.ds.queue;

import java.util.Iterator;

/**
 * Max priority queue.
 * 
 * Implemented as a height-biased leftist tree.
 *
 */
public class BiasedPriorityQueue<T extends Comparable<T>> implements
		Iterable<T> {

	private Node<T> root;

	private int size;

	public BiasedPriorityQueue() {
		super();
	}

	public BiasedPriorityQueue(BiasedPriorityQueue<T> other) {
		super();

		for (T value : other) {
			add(value);
		}

	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public void merge(BiasedPriorityQueue<T> other) {

		if (other == null) {
			throw new IllegalArgumentException("Can't merge with NULL queue");
		}

		BiasedPriorityQueue<T> otherCopy = new BiasedPriorityQueue<>(other);

		root = mergeRec(root, otherCopy.root);
		size += otherCopy.size;
	}

	public void add(T value) {

		if (root == null) {
			root = new Node<>(value);
		}
		else {
			Node<T> newNode = new Node<>(value);
			root = mergeRec(root, newNode);
		}

		++size;
	}

	public T extractMax() {

		if (size == 0) {
			throw new IndexOutOfBoundsException("Priority queue is empty");
		}

		T maxValue = root.value;

		Node<T> left = root.left;
		Node<T> right = root.right;

		root.left = null;
		root.right = null;
		root = null;

		root = mergeRec(left, right);

		--size;

		return maxValue;
	}

	private Node<T> mergeRec(Node<T> left, Node<T> right) {

		if (left == null) {
			return right;
		}

		if (right == null) {
			return left;
		}

		Node<T> retNode = null;

		if (left.value.compareTo(right.value) > 0) {
			left.right = mergeRec(left.right, right);
			retNode = left;
		}
		else {
			right.right = mergeRec(left, right.right);
			retNode = right;
		}

		int leftS = Node.getS(retNode.left);
		int rightS = Node.getS(retNode.right);

		if (leftS < rightS) {
			retNode.changeLeftToRight();
		}

		retNode.s = Math.min(leftS, rightS) + 1;

		return retNode;
	}

	@Override
	public Iterator<T> iterator() {
		return new PeOrderIterator();
	}

	private final class PeOrderIterator implements Iterator<T> {

		@Override
		public boolean hasNext() {
			return false;
		}

		@Override
		public T next() {
			return null;
		}

		@Override
		public void remove() {

		}

	}

	private static final class Node<U extends Comparable<U>> {

		final U value;
		Node<U> left;
		Node<U> right;

		int s;

		Node(U value) {
			super();
			this.value = value;
			this.s = 1;
		}

		void changeLeftToRight() {
			Node<U> tempLeft = left;
			left = right;
			right = tempLeft;
		}

		static <K extends Comparable<K>> int getS(Node<K> node) {
			if (node == null) {
				return 0;
			}

			return node.s;
		}

		@Override
		public String toString() {
			return String.valueOf(value) + ", s: " + s;
		}

	}

}
