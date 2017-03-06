package com.max.algs.backtracking;

import java.util.ArrayList;
import java.util.List;



public final class StringPermutationUtils {

	
	private StringPermutationUtils() {
		throw new IllegalStateException("Can't instantiate utility class '" + StringPermutationUtils.class.getName() + "'");
	}
	
	
	/**
	 * Write code to generate all possible case combinations of a given lower-cased string. 
	 * "0ab" -> ["0ab", "0aB", "0Ab", "0AB"])
	 * 
	 * N - number of alphabetic characters.
	 * 
	 * time: O(2^N)
	 * space: O(2^N)
	 */
	public static List<String> generatePermutations(String str){
		List<String> permutations = new ArrayList<>();		
		genPermutationsRec(str.toCharArray(), 0, permutations);
		return permutations;
	}
	

	private static void genPermutationsRec(char[] arr, int index, List<String> res){
		if( index >= arr.length ){
			res.add( new String(arr) );
			return;
		}
		
		if( ! Character.isAlphabetic(arr[index]) ){
			genPermutationsRec(arr, index+1, res);
			return;
		}
		
		arr[index] = Character.toLowerCase(arr[index]);		
		genPermutationsRec(arr, index+1, res);
		
		arr[index] = Character.toUpperCase(arr[index]);		
		genPermutationsRec(arr, index+1, res);
		
	}

}
