package com.max.algs.fulltextsearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.max.algs.ds.Pair;
import com.max.algs.ds.heap.BinaryHeap;

public class InMemoryIndex {
	
	private static final Logger LOG = Logger.getLogger(InMemoryIndex.class);
	
	private final Map<String, List<String>> terms = new HashMap<>();
	
	public void add(String word, String documentId){
		
		List<String> documents = terms.get(word);
		
		if( documents == null ){
			documents = new ArrayList<>();
			terms.put(word, documents);
		}
		
		documents.add(documentId);		
	}
	
	
	static class FreqPair extends Pair<String, Integer> implements Comparable<FreqPair> {


		private static final long serialVersionUID = -8673707049695877808L;

		public FreqPair(String word, Integer frequency) {
			super(word, frequency);
		}

		@Override
		public int compareTo(FreqPair other) {
			return Integer.compare(second, other.second);

		}
		
	}
	
	public void printStatistics(){		

		int pointersCount = 0;
		int size = 100;
		BinaryHeap<FreqPair> minHeap = BinaryHeap.minHeap(size+1);
		
		for( Map.Entry<String, List<String>> entry : terms.entrySet() ){	
			
			pointersCount += entry.getValue().size();
			
			if( minHeap.size() > size ){
				minHeap.poll();
			}
			
			minHeap.add(new FreqPair(entry.getKey(), entry.getValue().size()));
		}
		
		minHeap.poll();
		
		while( ! minHeap.isEmpty() ){
			FreqPair pair = minHeap.poll();
			LOG.info("Most frequent word is '" + pair.getFirst() + "', with frequency = " + pair.getSecond() + ", " + 
					pair.getFirst().length());
		}
		
		LOG.info("Total words count: " + terms.size() + ", files pointers count: " + pointersCount);
	}

}
