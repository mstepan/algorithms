package com.max.algs.regengine.automat;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.max.algs.regengine.char_class.CharClass;
import com.max.algs.regengine.char_class.DotCharClass;
import com.max.algs.regengine.char_class.UnconditionalCharClass;

public final class NfaToDfaConvertorUtils {
	
	

	private NfaToDfaConvertorUtils() {
		super();
		throw new IllegalStateException("Can't instantiate utility class '" + NfaToDfaConvertorUtils.class.getName() + "'");
	}
	
	
	
	public static DfaState convert( NfaState nfa ){
		
		DfaState empty = new DfaState("EMPTY");
		
		Map<String, DfaState> existedStates = new HashMap<>(); 
		
		DfaState startDfaState = new DfaState( getStartNfaStates(nfa) );	
		
		Set<CharClass> allInputs = determineAllInputs( nfa );	
		
		Queue<DfaState> queue = new ArrayDeque<>();
		queue.add( startDfaState );
		
		while( ! queue.isEmpty() ){
			
			DfaState curState = queue.poll();
			
			Set<NfaState> dotTransitions = curState.getDotTransitions();
			
			if( ! dotTransitions.isEmpty() ){
				DfaState newState = new DfaState( dotTransitions );	
				curState.addTransition( DotCharClass.INST, newState );	
				
				existedStates.put( newState.getName() , newState );
				queue.add( newState );
				
			}
			else {			
				for( CharClass charCl : allInputs ){
					
					Set<NfaState> nextNfaStates = getNextNfaStates(curState, charCl);
					
					// forward to EMPTY state	
					if( nextNfaStates.isEmpty() ){									
						curState.addTransition( charCl, empty );					

					}
					else {
						
						DfaState newState = new DfaState( nextNfaStates );					
						DfaState existedState = existedStates.get( newState.getName() );
						
						if( existedState == null ){
							existedStates.put( newState.getName() , newState );
							queue.add( newState );
							curState.addTransition( charCl, newState );
						}
						else {
							curState.addTransition( charCl, existedState );
						}
					}
				}			
			}
		}
		
		return startDfaState;		
	}
	

	private static Set<NfaState> getStartNfaStates(NfaState nfa){
		Set<NfaState> startNfaStates = nfa.getUnconditionalRecursive();	
		startNfaStates.add( nfa );
		return startNfaStates;
	}
	
	private static Set<NfaState> getNextNfaStates( DfaState dfaState, CharClass charCl ){
		
		Set<NfaState> nextNfaStates = new HashSet<>();
		
		for( NfaState nfaState : dfaState.getNfaStates() ){
			
			NfaState nextState = nfaState.getTransition().get( charCl );
			
			if( nextState != null ){					
				nextNfaStates.add( nextState );						
				nextNfaStates.addAll( nextState.getUnconditionalRecursive() );						
			}			
		}
		return nextNfaStates;		
	}
	
	
	private static Set<CharClass> determineAllInputs(NfaState nfa){
		
		Set<CharClass> allCharClasses = new HashSet<>();
		
		Set<NfaState> visited = new HashSet<>();
		Queue<NfaState> queue = new ArrayDeque<>();
		queue.add( nfa );		
		
		while( ! queue.isEmpty() ){
			
			NfaState curState = queue.poll();
			visited.add( curState );
			
			allCharClasses.addAll( curState.getTransition().keySet() );				
			
			for( NfaState nextState : curState.getTransition().values() ){
				
				if( ! visited.contains(nextState) ){
					queue.add( nextState );
				}				
			}			
		}
		
		allCharClasses.remove( UnconditionalCharClass.INST );
		allCharClasses.remove( DotCharClass.INST );
		return allCharClasses;	
	}

}
