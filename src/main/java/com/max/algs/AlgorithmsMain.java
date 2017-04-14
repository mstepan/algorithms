package com.max.algs;


import com.max.algs.ds.tree.BSTree;

public final class AlgorithmsMain {


    private AlgorithmsMain() throws Exception {

        BSTree<Integer> tree = BSTree.createFromSortedArray(new int[]{2, 3, 4, 5, 8, 12, 14});


        System.out.println(tree.lca(5, 5));

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    public static void main(String[] args) {
        try {
            new AlgorithmsMain();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

