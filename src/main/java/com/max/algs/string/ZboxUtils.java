package com.max.algs.string;



public final class ZboxUtils {
	
	
	private ZboxUtils() {
		super();
		throw new IllegalStateException("Can't instantiate utility class");		
	}
	
	
	
	
	/**
	 * time: O(N)
	 * space: O(1)
	 */
	public static int[] calculateZboxes(String str){
		
		if( str == null ){
			throw new IllegalArgumentException("NULL string passed"); 
		}
		
		int[] zboxesArr = new int[ str.length() ];	
		
		int l = 0;
		int r = 0;
		
		for( int k = 1; k < str.length(); k++ ){
			if( k > r ){
				
				int zLength = findMatchedLength( str, 0, k );				
				
				zboxesArr[k] = zLength;
				
				if( zLength > 0 ){
					l = k;
					r = k + zLength - 1;
				}			
			}
			
			else {		
			
				int betaK = k-l;
				int betaLength = r-k+1;
				
				if( zboxesArr[betaK] < betaLength ){
					zboxesArr[k] = zboxesArr[betaK]; 
				}
				else {					
					
					int zLength = findMatchedLength( str, 0, r+1 );
					
					zboxesArr[k] = betaLength + zLength;
					l = k;
					r = r + zLength;					
				}				
			}
		}		
		
		return zboxesArr;		
	}
	
	
	
	private static int findMatchedLength(String str, int first, int second){
			
		int zLength = 0;

		while( second < str.length() && str.charAt(first) == str.charAt(second) ){
			++first;
			++second;
			++zLength;
		}
		return zLength;
	}
	
	
	/**
	 * time: O(N^2)
	 * space: O(1)
	 */
	public static int[] calculateZboxesNaive(String str){
		
		if( str == null ){
			throw new IllegalArgumentException("NULL string passed"); 
		}
		
		int[] zboxesArr = new int[ str.length() ];		
		
		for( int i = 1; i < str.length(); i++ ){
			
			int first = 0;
			int second= i;
			int curZboxLength = 0;
			
			while( second < str.length() && str.charAt(first) == str.charAt(second) ){
				++first;
				++second;
				++curZboxLength;
			}
			
			zboxesArr[i] = curZboxLength;			
		}
		
		return zboxesArr;		
	}

}
