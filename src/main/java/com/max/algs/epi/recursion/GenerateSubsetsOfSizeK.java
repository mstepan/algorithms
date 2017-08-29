package com.max.algs.epi.recursion;

import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Generate all subsets of size K.
 */
public class GenerateSubsetsOfSizeK {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());

    private static final class PartialCall {
        final int val;
        final SharedLinkedList<Integer> subset;

        PartialCall(int val, SharedLinkedList<Integer> subset) {
            this.val = val;
            this.subset = subset;
        }
    }

    private static final class SharedLinkedList<T> {

        T value;
        SharedLinkedList<T> next;
        int elemsCount;

        int size() {
            return elemsCount;
        }

        SharedLinkedList<T> add(T value) {
            SharedLinkedList<T> head = new SharedLinkedList<>();
            head.value = value;

            head.next = (this.elemsCount == 0 ? null : this);
            head.elemsCount = this.elemsCount + 1;

            return head;
        }

        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder();

            SharedLinkedList<T> cur = this;

            while (cur != null) {
                buf.append(cur.value).append(", ");
                cur = cur.next;
            }

            return buf.toString();
        }

    }

    private static void checkPreconditions(int n, int k) {
        checkArgument(n > 0);
        checkArgument(k > 0);
        checkArgument(k <= n);
    }

    /**
     * time: O(n! / (k! * (n-k)!))
     * space: O(n! / (k! * (n-k)!))
     */
    public static List<SharedLinkedList<Integer>> generateSubsetsIterative(int n, int k) {

        checkPreconditions(n, k);

        List<SharedLinkedList<Integer>> res = new ArrayList<>();

        Deque<PartialCall> executionStack = new ArrayDeque<>();

        executionStack.push(new PartialCall(1, new SharedLinkedList<>()));

        while (!executionStack.isEmpty()) {

            PartialCall call = executionStack.pop();

            if (call.subset.size() == k) {
                res.add(call.subset);
            }
            else if (call.val <= n) {

                // call without 'val'
                executionStack.push(new PartialCall(call.val + 1, call.subset));

                // call with 'val', use immutable 'SharedLinkedList' with structural sharing to
                // reduce memory footprint
                SharedLinkedList<Integer> subsetWithElem = call.subset.add(call.val);
                executionStack.push(new PartialCall(call.val + 1, subsetWithElem));
            }
            else {
                assert call.subset.size() < k : "call.subset.size() >= k";
                assert call.val > n : "call.val <= n ";
            }
        }

        return res;
    }


    /**
     * time: O(n! / (k! * (n-k)!))
     * space: O(n! / (k! * (n-k)!))
     */
    public static List<List<Integer>> generateSubsets(int n, int k) {

        checkPreconditions(n, k);

        List<List<Integer>> res = new ArrayList<>();

        generateSubsetsRec(1, new ArrayDeque<>(), n, k, res);

        return res;
    }

    private static void generateSubsetsRec(int val, Deque<Integer> subset, int n, int k, List<List<Integer>> res) {
        if (subset.size() == k) {
            res.add(new ArrayList<>(subset));
            return;
        }

        if (val > n) {
            return;
        }

        subset.push(val);
        generateSubsetsRec(val + 1, subset, n, k, res);

        subset.pop();
        generateSubsetsRec(val + 1, subset, n, k, res);
    }

    private GenerateSubsetsOfSizeK() {

        int n = 5;
        int k = 2;

        List<SharedLinkedList<Integer>> subsetsIt = generateSubsetsIterative(n, k);
        List<List<Integer>> subsetsRec = generateSubsets(n, k);

        assert subsetsIt.size() == subsetsRec.size();

        LOG.info("subsets count: " + subsetsIt.size());

        for (SharedLinkedList<Integer> singleSubset : subsetsIt) {
            LOG.info(singleSubset);
        }

        LOG.info("GenerateSubsetsOfSizeK done...");
    }

    public static void main(String[] args) {
        try {
            new GenerateSubsetsOfSizeK();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}
