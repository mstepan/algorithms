package com.max.algs.graph;

public interface IGraphNodeCallback<T> {
	
	
	boolean visit(T node, T parent);

}
