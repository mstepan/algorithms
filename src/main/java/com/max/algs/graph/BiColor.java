package com.max.algs.graph;

/**
 * Two color enum.
 */
enum BiColor {

    WHITE,
    BLACK;

    BiColor next() {
        BiColor[] allValues = values();
        int index = (ordinal() + 1) % allValues.length;
        return allValues[index];
    }
}
