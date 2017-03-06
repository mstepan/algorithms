package com.max.algs.permutation;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;



public final class PermutationGeneratorUtils {
	
	
	
	public static List<List<Integer>> generate( int[] arr ){
		
		if( arr == null || arr.length == 0 ){
			throw new IllegalArgumentException("NULL or EMPTY 'arr' parameter passed"); 
		}
		
		/** 
		 * TRUE  <- (left)
		 * FALSE  -> (right)
		 */
		final BitSet direction = new BitSet( arr.length ); 
		direction.set( 0, arr.length );
		
		/** 
		 *  <-  <-  <-
		 *  1,  2,  3 
		 */ 			
		final List<List<Integer>> permutations = new ArrayList<>();
		
		while( true ){		
			
			int maxMobileIndex = -1;
			
			permutations.add( makeCopy(arr) );
		
			for( int i = arr.length-1; i >= 0; i-- ){			
				if( isMobile(arr, direction, i) ){
					if( maxMobileIndex == -1 ){
						maxMobileIndex = i;
					}
					else if( arr[maxMobileIndex] < arr[i] ){
						maxMobileIndex = i;
					}
				}			
			}
			
			
			if( maxMobileIndex == -1 ){
				break;
			}
			
			final int maxMobileElement = arr[maxMobileIndex]; 
			
			changeMobile( arr, direction, maxMobileIndex );
			
			revertDirectionForGreaterElements( arr, direction, maxMobileElement);
		}
		
		return permutations;
		
	}
	

	private static List<Integer> makeCopy(int[] arr){
		List<Integer> data = new ArrayList<>( arr.length );
		
		for( int value : arr ){
			data.add( value );
		}
		
		return data;
		
	}
	
	private static void revertDirectionForGreaterElements(int[] arr, BitSet direction, int maxMobileElement){
		
		for( int i = 0; i < arr.length; i++ ){
			if( arr[i] > maxMobileElement ){
				direction.flip( i );
			}
		}
		
	}
	
	
	private static void changeMobile(int[] arr, BitSet direction, int index){
		
		int temp = arr[index];
		
		if( direction.get(index) ){		
			arr[index] = arr[index-1];
			arr[index-1] = temp;
			
			if( direction.get(index-1) ){
				direction.set( index );
			}
			else {
				direction.clear( index );
			}
			direction.set( index-1 );
		}
		else {
			arr[index] = arr[index+1];
			arr[index+1] = temp;
			
			if( direction.get(index+1) ){
				direction.set( index );
			}
			else {
				direction.clear( index );
			}
			direction.clear( index+1 );
		}		
		
	}
	
	private static boolean isMobile(int[] arr, BitSet direction, int index){
		
		if( direction.get(index) && index == 0 ){
			return false;
		}
		
		if( ! direction.get(index) && index == arr.length-1 ){
			return false;
		}
		
		if( direction.get(index) ){
			if( arr[index-1] >  arr[index] ){
				return false;
			}
		}
		else {
			if( arr[index+1] > arr[index] ){
				return false;
			}
		}
		
		return true;
		
	}
	
	private PermutationGeneratorUtils(){
		super();
	}

}
