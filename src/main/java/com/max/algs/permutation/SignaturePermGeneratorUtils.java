package com.max.algs.permutation;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;

/*
 * 	 
 * You are given an array of n elements [1,2,....n]. For example {3,2,1,6,7,4,5}.
 * Now we generate a signature of this array by comparing every consecutive pir of elements.
 * If they increase, write I else write D. For example for the above array, the signature would be "DDIIDI". 
 * The signature thus has a length of N-1. Now the question is given a signature, compute the lexicographically smallest permutation of [1,2,....n]. 
 * Write the below function in language of your choice.
 * vector* FindPermute(const string& signature);	  
 */
public final class SignaturePermGeneratorUtils {

    private SignaturePermGeneratorUtils() {
        super();
    }

    /**
     * Construct permutation by signature using backtracking.
     */
    public static List<Integer> findPermute(String signature) {

        List<Integer> numbers = generateSortedSequence(signature.length());
        BitSet usedNumbers = new BitSet(numbers.size() + 1);

        for (int i = 0; i < numbers.size(); i++) {

            int curNum = numbers.get(i);

            List<Integer> seq = new ArrayList<Integer>();
            seq.add(curNum);
            usedNumbers.set(curNum);

            List<Integer> available = new ArrayList<Integer>(numbers);
            available.remove(i);

            List<Integer> genSeq = generatePermutation(seq, available,
                    signature, 0);

            // sequence fully generated
            if (genSeq.size() == numbers.size()) {
                return genSeq;
            }
        }

        return Collections.emptyList();
    }

    public static boolean isSignatureMatched(List<Integer> data,
                                             String signature) {

        if (data == null || signature == null) {
            throw new IllegalArgumentException(
                    "'arr' or 'signature' parameter is NULL");
        }

        if (data.size() != signature.length() + 1) {
            throw new IllegalArgumentException(
                    "'arr' length != 'signature' length + 1: array length = "
                            + data.size() + ", signature length = "
                            + signature.length());
        }

        signature = signature.toUpperCase();

        char chSig;
        final int dataSize = data.size();
        for (int i = 1; i < dataSize; i++) {
            chSig = signature.charAt(i - 1);
            if ((chSig == 'D' && data.get(i - 1) < data.get(i))
                    || (chSig == 'I' && data.get(i - 1) > data.get(i))) {
                return false;
            }
        }

        return true;
    }

    private static List<Integer> generatePermutation(
            List<Integer> constuctedSeq, List<Integer> availableNumbers,
            String signature, int sigIndex) {

        if (sigIndex >= signature.length()) {
            return constuctedSeq;
        }

        char di = signature.charAt(sigIndex);

        for (int i = 0; i < availableNumbers.size(); i++) {

            int lastElem = constuctedSeq.get(constuctedSeq.size() - 1);
            int curElem = availableNumbers.get(i);

            if ((di == 'D' && lastElem > curElem)
                    || (di == 'I' && lastElem < curElem)) {

                List<Integer> newSeq = new ArrayList<Integer>(constuctedSeq);
                newSeq.add(curElem);

                List<Integer> newAvailNumbers = new ArrayList<Integer>(
                        availableNumbers);
                newAvailNumbers.remove(i);

                List<Integer> partPerm = generatePermutation(newSeq,
                        newAvailNumbers, signature, sigIndex + 1);

                if (partPerm.size() > 0) {
                    return partPerm;
                }
            }
        }

        return new ArrayList<Integer>();
    }

    private static List<Integer> generateSortedSequence(int sigLength) {
        List<Integer> numbers = new ArrayList<Integer>();

        for (int i = 0; i < sigLength; i++) {
            numbers.add(i + 1);
        }

        numbers.add(sigLength + 1);

        return numbers;
    }

}
