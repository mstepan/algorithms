package com.max.algs.graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Tarjan algorithm for strongly connected components.
 * 
 * See: http://en.wikipedia.org/wiki/Tarjan
 * 's_strongly_connected_components_algorithm
 * 
 */
public class StronglyConnectedComponentsFac {

	private final SimpleIntGraph graph;

	private final Map<Integer, Integer> indexMap = new HashMap<>();
	private final Map<Integer, Integer> lowMap = new HashMap<>();
	private final List<List<Integer>> components = new ArrayList<>();

	private final Deque<Integer> stack = new ArrayDeque<Integer>();
	private final Set<Integer> inStack = new HashSet<Integer>();

	private int index = 0;

	public StronglyConnectedComponentsFac(SimpleIntGraph graph) {
		super();
		this.graph = graph;
	}

	public List<List<Integer>> create() {

		for (Integer ver : graph.vertexes()) {

			if (!indexMap.containsKey(ver)) {
				stronglyConnect(ver);
			}
		}

		return components;
	}

	private void stronglyConnect(int vertex) {

		indexMap.put(vertex, index);
		lowMap.put(vertex, index);
		++index;
		stack.push(vertex);
		inStack.add(vertex);

		for (Integer w : graph.getAdjVertexes(vertex)) {
			if (!indexMap.containsKey(w)) {
				stronglyConnect(w);

				int vLow = lowMap.get(vertex);
				int wLow = lowMap.get(w);

				lowMap.put(vertex, Math.min(vLow, wLow));
			}
			else
				if (inStack.contains(w)) {
					int vLow = lowMap.get(vertex);
					int wIndex = indexMap.get(w);

					lowMap.put(vertex, Math.min(vLow, wIndex));
				}
		}

		if (indexMap.get(vertex).equals(lowMap.get(vertex))) {

			List<Integer> newComponent = new ArrayList<>();

			while (true) {
				int newVer = stack.pop();
				inStack.remove(newVer);

				newComponent.add(newVer);

				if (newVer == vertex) {
					break;
				}
			}

			components.add(newComponent);

		}

	}

}
