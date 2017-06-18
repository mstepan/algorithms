package com.max.algs.ds.tree;


public interface ITreeVisitor<T> {

    boolean visit(T value, int level);

}
