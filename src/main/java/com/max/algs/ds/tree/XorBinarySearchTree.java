package com.max.algs.ds.tree;

import com.max.system.UnsafeUtils;
import sun.misc.Unsafe;

import java.util.ArrayList;
import java.util.List;

/**
 * Xor binary tree.
 */
public class XorBinarySearchTree {


    private Node root;

    private List<Node> nodeHolder = new ArrayList<>();

    private int size;


    public boolean contains(int value){

        if( root == null ){
            return false;
        }

        long parentAdr = 0;
        Node cur = root;

        while( cur != null ){

            if( cur.value == value ){
                return true;
            }

            long newParentAdr = UnsafeUtils.addressOf( cur );

            try {
                // go left
                if (value < cur.value) {
                    cur = UnsafeUtils.getElementByAddress(cur.left ^ parentAdr);
                }
                // go right
                else {
                    cur = UnsafeUtils.getElementByAddress(cur.right ^ parentAdr);
                }
            }
            catch(ClassCastException ex){
                Object obj = UnsafeUtils.getElementByAddress(cur.right ^ parentAdr);
                int x = 10;
            }

            parentAdr = newParentAdr;
        }

        return false;
    }

    public boolean add(int value){

        if(root == null ){
            root = new Node(value);
            ++size;
            return true;
        }

        long parentAdr = 0L;
        Node cur = root;

        while( true ){

            if( cur.value == value ){
                return false;
            }

            // go left
            if( value < cur.value ){
                Node newCur = UnsafeUtils.getElementByAddress(cur.left ^ parentAdr);

                if( newCur == null ){

                    Node newNode = new Node(value, UnsafeUtils.addressOf(cur));

                    cur.left ^= UnsafeUtils.addressOf(newNode);

                    ++size;
                    return true;
                }

                parentAdr = UnsafeUtils.addressOf(cur);
                cur = newCur;

            }

            // go right
            else {

                Node newCur = UnsafeUtils.getElementByAddress(cur.right ^ parentAdr);

                if( newCur == null ){

                    Node newNode = new Node(value, UnsafeUtils.addressOf(cur));

                    cur.right ^= UnsafeUtils.addressOf(newNode);

                    nodeHolder.add(newNode);
                    ++size;
                    return true;
                }

                parentAdr = UnsafeUtils.addressOf(cur);
                cur = newCur;

            }
        }

    }


    public int size(){
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }


    private static final class Node {

        final int value;
        long left;
        long right;

        public Node(int value) {
            this(value, 0);
        }

        public Node(int value, long parentAdr) {
            this.value = value;
            left ^= parentAdr;
            right ^= parentAdr;
        }

        @Override
        public String toString(){
            return String.valueOf(value);
        }
    }
}
