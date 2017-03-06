package com.max.algs.ds.cache;

import java.util.HashMap;
import java.util.Map;

public class MruCache<K, V> {

	private static final int NUM_OF_ELEMENTS_TO_EVICT = 1;
	protected final CacheNode<K, V> head;

	protected final Map<K, CacheNode<K, V>> elems = new HashMap<>();

	private final int capacity;
	private int size;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MruCache(int capacity) {
		this.capacity = capacity;
		head = new CacheNode(null, null);
		head.next = head;
		head.prev = head;
	}

	public void add(K key, V value) {

		CacheNode<K, V> existedNode = elems.get(key);

		// 'key' already exists in cache
		if (existedNode != null) {
			existedNode.value = value;
			deleteNode(existedNode);
			addAfter(head, existedNode);
		}
		else {

			if (size == capacity) {
				evict(NUM_OF_ELEMENTS_TO_EVICT);
				--size;
			}

			CacheNode<K, V> node = new CacheNode<>(key, value);
			addAfter(head, node);

			elems.put(key, node);
			++size;
		}
	}

	public V get(K key) {

		CacheNode<K, V> node = elems.get(key);

		if (node == null) {
			return null;
		}

		deleteNode(node);
		addAfter(head, node);

		return node.value;
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	private void evict(int count) {

		if (size == 0) {
			return;
		}

		for (int i = 0; i < count; i++) {
			deleteNode(head.next);
		}
	}

	private void deleteNode(CacheNode<K, V> node) {
		node.prev.next = node.next;
		node.next.prev = node.prev;

		node.next = null;
		node.prev = null;
	}

	private void addAfter(CacheNode<K, V> base, CacheNode<K, V> node) {

		node.next = base.next;
		node.prev = base;

		base.next.prev = node;
		base.next = node;
	}

}
