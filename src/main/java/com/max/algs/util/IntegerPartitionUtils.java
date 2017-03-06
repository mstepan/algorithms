package com.max.algs.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public final class IntegerPartitionUtils {
	
	
	private IntegerPartitionUtils(){
		super();
	}
	
	
	/**
	 * 
	 * Generate partitions of integer value.
	 * See: https://en.wikipedia.org/wiki/Partition_(number_theory)
	 * 
	 */
	public static List<List<Integer>> generatePartitions( final int value ){
		
		List<List<Integer>> partitions = new ArrayList<>();
		
		List<Integer> oneElenentList = new ArrayList<>();
		oneElenentList.add(value);
		partitions.add(oneElenentList);
		
		while( true ){
			
			List<Integer> lastPartition = partitions.get( partitions.size()-1 );
			
			if( allOnes(lastPartition) ){
				break;
			}
			
			List<Integer> newPartition = new ArrayList<>();
			
			int lastBigElemIndex = -1;
			
			for ( int i = lastPartition.size()-1; i >=0; i-- ){
				
				int elem = lastPartition.get(i);
				
				if( elem > 1 ){
					lastBigElemIndex = i;
					break;
				}
				
				newPartition.add( elem );
			}
			
			int elemToSplit = lastPartition.get(lastBigElemIndex);
			
			newPartition.add(1);
			newPartition.add(elemToSplit-1);
			
			for( int i = lastBigElemIndex-1; i >=0; i-- ){
				newPartition.add( lastPartition.get(i) );
			}			

			Collections.reverse( newPartition );
			
			if( elemToSplit-1 > 1 ){
				normalize(newPartition, elemToSplit-1);
			}
			
			partitions.add( newPartition );			
			
		}
		
		return partitions; 
	}
	
	private static void normalize( List<Integer> data, int baseValue ){
		
		ListIterator<Integer> it = data.listIterator();
		
		while( it.hasNext() ){
			
			int value = it.next();
			
			if( value == 1 ){
				it.previous();
				break;
			}
			
		}
		
		int reduced = 0 ; 	
		
		while( it.hasNext() ){
		
			int value = it.next();
			it.remove();
		
			if( reduced == baseValue ){	
				it.add(reduced);
				reduced = 0;				
			}
			
			reduced += value;
		}
		
		if( reduced > 0 ){
			data.add(reduced);
		}	
		
	}

	private static boolean allOnes( List<Integer> elems ){
		
		for( int val : elems ){
			if( val != 1 ){
				return false;
			}
		}
		
		return true;
	}
	

}
