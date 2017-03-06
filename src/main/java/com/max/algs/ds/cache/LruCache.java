package com.max.algs.ds.cache;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class LruCache {

	private int size;
	private final int capacity;

	private final Node head;

	private final Map<Integer, Node> data = new HashMap<>();

	public LruCache(int capacity) {
		super();
		this.capacity = capacity;
		head = new Node(-1);
		head.next = head;
		head.previous = head;

	}

	public void add(int value) {

		Node newNode = new Node(value);

		newNode.next = head.next;
		head.next.previous = newNode;
		newNode.previous = head;
		head.next = newNode;

		data.put(value, newNode);

		++size;
	}

	public int get(int value) {
		return 0;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();

		Node cur = head.next;

		while (cur != head) {
			buf.append(cur.value).append(", ");
			cur = cur.next;
		}

		return buf.toString();
	}

	private static class Node {

		private Node next;
		private Node previous;
		private final int value;

		public Node(int value) {
			super();
			this.value = value;
		}

		@Override
		public String toString() {
			return String.valueOf(value);
		}

		@Override
		public int hashCode() {
			return com.google.common.base.Objects.hashCode(value);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null || getClass() != obj.getClass()) {
				return false;
			}
			Node other = (Node) obj;
			return com.google.common.base.Objects.equal(value, other.value);
		}
	}

}
