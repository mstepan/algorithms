package com.max.algs.graph;

import java.util.Comparator;

public class Edge<U> implements Comparable<U> {

	public static final Comparator<Edge<?>> WEIGHT_ASC_CMP = new Comparator<Edge<?>>() {
		@Override
		public int compare(Edge<?> edge1, Edge<?> edge2) {
			return Integer.compare(edge1.weight, edge2.weight);
		}
	};

	final int weight;
	final U src;
	final U dest;

    public Edge(Edge<U> other) {
        this(other.src, other.dest, other.weight);
    }

	public Edge(U src, U dest, int weight) {
		super();
		this.src = src;
		this.dest = dest;
		this.weight = weight;

	}

    public int getWeight() {
        return weight;
    }

    @Override
	public int compareTo(U obj) {
		return String.valueOf(dest).compareTo(String.valueOf(obj));
	}

	@Override
	public String toString() {
		return "[" + String.valueOf(src) + "->" + String.valueOf(dest)
				+ ", w: " + weight + "]";
	}

	@Override
	public int hashCode() {
		return com.google.common.base.Objects.hashCode(src, dest, weight);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		Edge<U> other = (Edge<U>) obj;

		return com.google.common.base.Objects.equal(src, other.src)
				&& com.google.common.base.Objects.equal(dest, other.dest)
				&& com.google.common.base.Objects.equal(weight, other.weight);

	}

}
