package com.max.algs.backtracking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;


public final class MultisetPermutations {

	
	private MultisetPermutations(){
		super();
	}
	
	
	public static List<int[]> generate( int[] elems ){
		
		Arrays.sort(elems);
		
		List<int[]> res = new ArrayList<>();		
		subsets( elems, 0, new BitSet(elems.length), new int[elems.length], res );		
		return res;
	}
	
	
	public static void subsets( int[] elems, int index, BitSet used, int[] solution, List<int[]> res ){
		
		if( index >= elems.length ){			
			res.add( solution.clone() );
			return;
		}
		
		Integer prev = null;
		
		for( int i = 0; i < elems.length; i++ ){
			
			if( ! used.get(i) && ( prev == null || prev != elems[i] ) ){
				
				prev = elems[i];
				
				solution[index] = elems[i];
				
				used.set(i);
				
				subsets( elems, index+1, used, solution, res);
				
				used.clear(i);
				
			}
			
			
		}
		
		return;		
		
	}
	
	

}
