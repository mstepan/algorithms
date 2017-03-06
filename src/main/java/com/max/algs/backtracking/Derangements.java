package com.max.algs.backtracking;

import java.util.BitSet;
import java.util.List;

public final class Derangements {
	
	
	private Derangements(){
		super();
	}

	public static void derangement( int[] elems, List<int[]> res ){
		derangement(elems, 0, new BitSet(elems.length+1), new int[elems.length], res );
	}
	
	private static void derangement( int[] elems, int index, BitSet used, int[] solution, List<int[]> res ){
		
		used.set(0);
		
		if( index >= elems.length ){
			//LOG.info( Arrays.toString(solution) );
			res.add( solution.clone() );
		}		
		
		for(int i = 1; i < elems.length+1; i++ ){
			
			if( i != index+1 && ! used.get(i) ){
				
				solution[index] = i;
				
				used.set(i);
				
				derangement(elems, index+1, used, solution, res);
				
				used.clear(i);				
			}
		}		
	}
	

}
