package com.max.algs.util;

import java.util.ArrayList;
import java.util.List;


public final class SetPartitionUtils {
	
	
	private SetPartitionUtils(){
		throw new IllegalStateException("Can't instantiate class '" + SetPartitionUtils.class.getName() + "'");
	}
	
	

	public static List<List<List<Integer>>>  partitionSet( List<Integer> set ){	
		
		if( set == null ){
			throw new IllegalArgumentException("NULL set passed for partition");
		}
		
		if( set.isEmpty() ){
			return new ArrayList<>();
		}
		
		List<List<List<Integer>>> partitions = new ArrayList<>();	
		
		List<List<Integer>> firstPartition = new ArrayList<>();		
		firstPartition.add( createList(set.get(0)) );
		
		partitions.add( firstPartition  );
		
		return partitionSetRec(set, 1, partitions );
	}
	

	private static List<List<List<Integer>>> partitionSetRec( List<Integer> set, int index, List<List<List<Integer>>> oldPartitions ){
		
		if( index >= set.size() ){
			return oldPartitions;
		}
		
		final int value = set.get(index);
		
		List<List<List<Integer>>> newPartitions = new ArrayList<>(); 
		
		for( List<List<Integer>> singlePartition : oldPartitions ){	
			
			for( int i =0; i < singlePartition.size(); i++ ){				
				List<List<Integer>> copied = deepCopy(singlePartition);				
				copied.get(i).add(value);				
				newPartitions.add( copied );
			}
			
			List<List<Integer>> copied = deepCopy(singlePartition);	
			copied.add( createList(value) );
			
			newPartitions.add( copied );
			
		}
						
		return partitionSetRec(set, index+1, newPartitions );

	}
	
	private static List<Integer> createList(int...values){
		
		List<Integer> list = new ArrayList<>();
		
		for( int val : values ){
			list.add( val );
		}
		
		return list;
	}
	
	
	private static List<List<Integer>> deepCopy(List<List<Integer>> partition){
		
		List<List<Integer>> copy = new ArrayList<>();		
		
		for( List<Integer> set : partition ){
			copy.add( new ArrayList<>(set));
		}
		
		return copy;
	}
}
