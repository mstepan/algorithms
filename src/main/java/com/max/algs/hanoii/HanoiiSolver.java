package com.max.algs.hanoii;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class HanoiiSolver {
	
	
	private static final Logger LOG = Logger.getLogger(HanoiiSolver.class);
	
	private HanoiiSolver(){}
	
	public static void moveHanoii(int count){
		
		Map<Deque<Integer>, String> pegNames = new HashMap<>();
		
		Deque<Integer> from = new ArrayDeque<>();
		
		pegNames.put(from, "peg1");

		
		for( int i = count; i > 0; i-- ){
			from.push(i);
		}
		
		Deque<Integer> to = new ArrayDeque<>();
		pegNames.put(to, "peg2");
		
		Deque<Integer> temp = new ArrayDeque<>();
		pegNames.put(temp, "peg3");

		
		checkStackSorted(from);
		checkStackSorted(temp);
		checkStackSorted(to);
		
		moveHanoiiRec(from, to, temp, count, pegNames);	

		
		checkStackSorted(from);
		checkStackSorted(temp);
		checkStackSorted(to);
	}
	
	private static void moveHanoiiRec(Deque<Integer> from, Deque<Integer> to, Deque<Integer> temp, int count, 
			Map<Deque<Integer>, String> pegs){
		
		checkStackSorted(from);
		checkStackSorted(temp);
		checkStackSorted(to);
		
		if( count == 1 ){
			int value = from.pop();
			to.push(value);
			LOG.info( pegs.get(from) + "=>[" + value + "]=>" + pegs.get(to) );

			return;
		}
		moveHanoiiRec(from, temp, to, count-1, pegs);		
		
		int value = from.pop();
		to.push(value);
		LOG.info( pegs.get(from) + "=>[" + value + "]=>" + pegs.get(to) );
		
		moveHanoiiRec(temp, to, from, count-1, pegs);
		
	}
	
	private static void checkStackSorted(Deque<Integer> stack){
		
		Integer prevValue = null;
		for( Integer value : stack ){			
			if( prevValue != null && prevValue > value ){
				throw new IllegalStateException("Stack isn't sorted");
			}
			prevValue = value;
		}
	}

}
