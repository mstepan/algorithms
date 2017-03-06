package com.max.algs.graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public final class SimpleIntGraph {
	
	private final boolean directed;
	
	private final Map<Integer, Set<Integer>> data = new HashMap<>();
	
	private SimpleIntGraph(boolean directed){
		this.directed = directed;
	}
	
	public static SimpleIntGraph create(){
		return new SimpleIntGraph(false);
	}
	
	public static SimpleIntGraph createDirected(){
		return new SimpleIntGraph(true);
	}
	
	
	public void bfs( IGraphNodeCallback<Integer> callback ){
		
		Set<Integer> visited = new HashSet<>();
		
		for( int curVertex : vertexes() ){
			
			if( visited.contains(curVertex) ){
				continue;
			}
			
			Map<Integer, Integer> parents = new HashMap<>();
			parents.put( curVertex, -1 );
						
			Queue<Integer> queue = new ArrayDeque<>();
			queue.add( curVertex );
			
			while( ! queue.isEmpty() ){
				int vertex = queue.poll();
				
				for( int adjVertex :  getAdjVertexes(vertex) ){
					
					if( parents.containsKey(adjVertex) || visited.contains(adjVertex) ){
						continue;
					}
					
					parents.put( adjVertex, vertex );
					queue.add( adjVertex );					
				}
				
				visited.add(vertex);
				
				boolean shouldContinue = callback.visit( vertex, parents.get(vertex) );
				
				if( !shouldContinue ){
					return;
				}
			}
			
		}
		
		
	}
	
	
	public boolean isBipartite(){
		
		if( isEmpty() ){
			return true;			
		}
		
		Set<Integer> visited = new HashSet<>();
		
		for( Integer singleVertex : vertexes() ){
		
			if( ! visited.contains(singleVertex) ){
				Queue<Integer> queue = new ArrayDeque<Integer>();
				queue.add( singleVertex );
				
				Map<Integer, Boolean> colorMap = new HashMap<Integer, Boolean>();
				colorMap.put( singleVertex, true );
				
				while( ! queue.isEmpty() ){					

					Integer vertex = queue.poll();
					boolean color = colorMap.get(vertex);
					
					for( Integer adjVer : getAdjVertexes(vertex) ){
						if( colorMap.containsKey(adjVer) ){
							boolean adjColor = colorMap.get(adjVer);
							
							if( adjColor == color ){
								return false;
							}							
						}
						else {
							colorMap.put(adjVer, !color);
							queue.add( adjVer );
						}
					}
					
					visited.add( vertex );
				}
			}
		}
		
		return true;
		
	}
	
	public List<Integer> topologicalSorting(){
		
		List<Integer> topology = new ArrayList<>();
		
		/**
		 * Create map with input degree count for each vertex.
		 */
		Map<Integer, Integer> inEdgesMap = new HashMap<>();
		
		for( Map.Entry<Integer, Set<Integer>> entry : data.entrySet() ){
			int ver = entry.getKey();
			
			if( ! inEdgesMap.containsKey(ver) ){
				inEdgesMap.put( ver, 0 );
			}
			
			for( Integer adjVer : entry.getValue() ){
				if( ! inEdgesMap.containsKey(adjVer) ){
					inEdgesMap.put( adjVer, 1 );
				}
				else {
					inEdgesMap.put( adjVer, inEdgesMap.get(adjVer)+1 );
				}
			}
		}
		/**
		 * Populate queue with free vertexes (vertexes with in-degree equals '0')
		 */
		Deque<Integer> freeVertexesQueue = new ArrayDeque<Integer>();
		
		for( Map.Entry<Integer, Integer> entry : inEdgesMap.entrySet() ){
			if( entry.getValue() == 0 ){
				freeVertexesQueue.add( entry.getKey() );
			}
		}		
		
		/**
		 * Create topological sorting order.
		 */
		while( ! freeVertexesQueue.isEmpty() ){
			int ver = freeVertexesQueue.poll();			
			topology.add( ver );
			
			for(Integer adjVer : data.get(ver) ){
				Integer curInCount = inEdgesMap.get(adjVer);				
				curInCount -= 1;				
				inEdgesMap.put( adjVer, curInCount );
				
				// new free edge found
				if( curInCount == 0 ){
					freeVertexesQueue.add( adjVer );
				}
			}
		}
		
		if( topology.size() != data.size() ){
			throw new IllegalStateException("Can't return topological sorting order, graph contains cycles.");
		}	

		return topology;
	}
	
	public void addEdge(int ver1, int ver2){
		
		if( ! data.containsKey(ver1) ){
			data.put(ver1, new HashSet<Integer>());
		}
		
		
		if( ! data.containsKey(ver2) ){
			data.put(ver2, new HashSet<Integer>());
		}
		
		
		data.get(ver1).add(ver2);
		
		if( ! directed ){
			data.get(ver2).add(ver1);
		}
	}
	
	public boolean isEmpty(){
		return data.isEmpty();
	}
	
	public SimpleIntGraph inverted(){
		SimpleIntGraph invertedGraph = SimpleIntGraph.createDirected();
		
		for(Map.Entry<Integer, Set<Integer>> entry : data.entrySet() ){
			
			for( Integer adjVer : entry.getValue() ){
				invertedGraph.addEdge( adjVer, entry.getKey() );
			}			
		}
		
		return invertedGraph;
	}
	
	public Set<Integer> vertexes(){
		return data.keySet();
	}
	
	public Set<Integer> getAdjVertexes( int vertex ){
		Set<Integer> adjVertexes = data.get( vertex );
		
		if( adjVertexes == null ){
			return new HashSet<Integer>();
		}
		
		return adjVertexes;
	}

	
	/**
	 * 
	 * Do BFS to find connected components.
	 * 
	 */
	public List<List<Integer>> getConnectedComponents(){
		
		List<List<Integer>> components = new ArrayList<>();
		
		if( isEmpty() ){
			return components; 
		}		
		
		if( directed ){
			return new StronglyConnectedComponentsFac(this).create();
		}		
		
		Set<Integer> visited = new HashSet<>();
		
		for( Integer ver : data.keySet() ){
			
			if( ! visited.contains(ver) ){
				
				List<Integer> connectedComponent = new ArrayList<>();				
				Set<Integer> marked = new HashSet<>();
				marked.add( ver );
				
				Queue<Integer> queue = new ArrayDeque<>();
				queue.add( ver );
								
				while( ! queue.isEmpty() ){
					
					int curVer = queue.poll();
								
					for( int adjVer : data.get(curVer) ){
						if( ! visited.contains(adjVer) && ! marked.contains(adjVer) ){
							queue.add( adjVer );
							marked.add( adjVer );
						}
					}
					
					visited.add( curVer );
					marked.remove( curVer );
					connectedComponent.add( curVer );
				}
				
				assert marked.isEmpty() : "marked isn't empty"; 
				
				components.add( connectedComponent );				
			}			
						
		}	
		
		return components;
	}
	
	
	@Override
	public String toString(){
		StringBuilder buf = new StringBuilder();
		
		for( Map.Entry<Integer, Set<Integer>> entry : data.entrySet() ){
			buf.append(entry.getKey()).append(" => ");
			
			for( Integer adjVer : entry.getValue() ){
				buf.append( adjVer ).append(", ");
			}
			
			buf.append( System.getProperty("line.separator"));			
		}
		
		return buf.toString();
	}

}
