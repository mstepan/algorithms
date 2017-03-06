package com.max.algs.merger;


import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

public class MergeTree {


    private int size;
    private final int[][] data;
    private INode root;

    public MergeTree(int[][] matrix) {
        checkArgument(matrix != null, "NULL 'matrix' parameter passed");
        this.data = makeCopyAndSort(matrix);
        this.size = calculateSize(this.data);
        this.root = createTree();
    }

    public int extract() {
        checkState(!isEmpty(), "Can't extract element from empty tree");

        int retValue = root.getValue();
        --size;

        root.moveToNext();

        return retValue;
    }


    private INode createTree(){

        Deque<INode> queue = new ArrayDeque<>();

        for( int row = 0; row < data.length; row++ ){
            queue.add(new LeafNode(row, 0));
        }

        while( queue.size() > 1 ){
            INode node1 = queue.poll();
            INode node2 = queue.poll();
            queue.add(new CombinedNode(node1, node2));
        }

        return queue.poll();
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private int[][] makeCopyAndSort(int[][] matrix) {
        int[][] matrixCopy = new int[matrix.length][];

        int[] baseRow;
        for (int row = 0; row < matrix.length; row++) {
            baseRow = matrix[row];
            if (baseRow != null) {
                matrixCopy[row] = Arrays.copyOf(baseRow, baseRow.length);
                Arrays.sort(matrixCopy[row]);
            }
        }

        return matrixCopy;
    }

    private int calculateSize(int[][] matrix) {
        int total = 0;

        for (int[] rowArr : matrix ) {
            total += rowArr.length;
        }

        return total;
    }



    private interface INode {
        int getValue();
        void moveToNext();
    }

    private class CombinedNode implements INode {
        INode left;
        INode right;
        int value;

        CombinedNode(INode newLeft, INode newRight) {
            this.left = newLeft;
            this.right = newRight;
            this.value = Math.min(left.getValue(), right.getValue());
        }

        @Override
        public int getValue() {
            return value;
        }

        @Override
        public void moveToNext() {
            if( value == left.getValue() ){
                left.moveToNext();
            }
            else {
                right.moveToNext();
            }

            value = Math.min(left.getValue(), right.getValue());
        }


        @Override
        public String toString(){
            return "[" + getValue() + "]";
        }
    }

    private class LeafNode implements INode {

        final int row;
        int col;

        LeafNode(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public int getValue() {

            if (col >= MergeTree.this.data[row].length) {
                return Integer.MAX_VALUE;
            }

            return MergeTree.this.data[row][col];
        }

        @Override
        public void moveToNext() {
            ++col;
        }

        @Override
        public String toString(){
            return "[" + row + ", " + col + "] => " + getValue();
        }

    }

}
