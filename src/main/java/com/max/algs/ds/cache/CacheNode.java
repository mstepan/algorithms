package com.max.algs.ds.cache;

final class CacheNode<K, V> {

	public final K key;
	public V value;

	public CacheNode<K, V> next;
	public CacheNode<K, V> prev;

	public CacheNode(K key, V value) {
		super();
		this.key = key;
		this.value = value;
	}

	@Override
	public int hashCode() {
		return com.google.common.base.Objects.hashCode(key, value);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		CacheNode other = (CacheNode) obj;

		return com.google.common.base.Objects.equal(key, other.key)
				&& com.google.common.base.Objects.equal(value, other.value);

	}

	@Override
	public String toString() {
		return String.format("%s=%s", key, value);
	}

}
