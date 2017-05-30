package topcoder;


/**
 * Topcoder: http://community.topcoder.com/stat?c=problem_statement&pm=12200&rd=15181
 * 
 * Problem Statement for BlockTower
 * Problem Statement
 *     	Josh loves playing with blocks. Currently, he has N blocks, labeled 0 through N-1. The heights of all blocks are positive integers. More precisely, for each i, the height of block i is blockHeights[i]. Josh is interested in making the tallest block tower possible. He likes all his towers to follow three simple rules:
 *     The blocks must be stacked in a single column, one atop another. The height of the tower is simply the sum of heights of all its blocks.
 *     The labels of blocks used in the tower must increase from the bottom to the top. In other words, whenever Josh places box x on top of box y, we have x > y.
 *     Josh will never place a box of an even height on top of a box of an odd height.
 *     You are given the int[] blockHeights. Return the height of the tallest possible block tower Josh can build.
 *     
 *      Definition
 *
 *       Class:	BlockTower
 *       Method:	getTallest
 *       Parameters:	int[]
 *       Returns:	int
 *       Method signature:	int getTallest(int[] blockHeights)
 *       (be sure your method is public)
 */
public final class BlockTower {
	
	
	/**
	 * time: O(N^2)
	 * space: O(1)
	 */
	public static int getTallestBest( int[] blockHeights) {
		
	    int n = blockHeights.length;
	    int res = 0;
	    
	    // Pick a limit:
	    
	    //  O(n)
	    for (int limit = 0; limit <= n; limit++) {
	    	
	        int tot = 0;
	        
	        // Before the limit, we can only pick
	        // even blocks:
	        for (int i = 0; i < limit; i++) {
	            if (blockHeights[i] % 2 == 0) {
	                tot += blockHeights[i];
	            }
	        }
	        // After the limit, we can only pick
	        // odd blocks:
	        for (int i = limit; i < n; i++) {
	            if (blockHeights[i] % 2 != 0) {
	                tot += blockHeights[i];
	            }
	        }
	        // Remember the best result
	        res = Math.max(res, tot);
	    }
	    return res;
	}
	
	
	/**
	 * time: O(N)
	 * space: O(1)
	 */
	public static int getTallest(int[] blockHeights){
		
		if( blockHeights == null ){
			throw new IllegalArgumentException("'blockHeights' array is NULL");
		}
		
		int opt = 0;
		int optEven = 0;

		for( int i = 0; i < blockHeights.length; i++){			
			if( (blockHeights[i] & 1) == 0 ){			
				optEven += blockHeights[i];
				opt = Math.max( opt, optEven );				
			}
			else {	
				opt += blockHeights[i];
			}			
		}		
		
		return opt;
	}
	
	private BlockTower() {
		super();
	}

	
}
