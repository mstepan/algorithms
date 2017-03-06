package com.max.algs.maze;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import org.apache.log4j.Logger;


public class MazePathFinder {
	
	private static final Logger LOG = Logger.getLogger(MazePathFinder.class);	
	
	private final boolean[][] maze = {
			{false, true, true,  true, true, true, false, false, true, true},
			{true,  true, false, true, true, true, true,  true,  true, true},
			{false, true, false, true, true, false,false, true,  false,false},
			{true,  true, true, false, false, false, true,  true,  false, true},
			{true,  false, false, true, true, true, true,  true,  true, true},
			{true,  false, false, true, true, false, true,  false, false, true},
			{true,  true, true, true, false, true, true,  true, true, true},
			{false, true, false, true, false, true, false,  true, true, true},
			{false, true, false, false, true, true, true,  false, false, false},
			{true,  true, true,  true,  true, true, true,  false, false, true}
	};
	
	private MazePathFinder() throws Exception {
		for( int row = 0; row < maze.length; row++ ){
			StringBuilder rowStr = new StringBuilder();
			for(int col = 0; col < maze[row].length; col++ ){
				if( maze[row][col] ){
					rowStr.append("-");
				}
				else {
					rowStr.append("*");
				}
			}
			
			LOG.info( rowStr );
		}
		
		
		Point lastPoint = new Point(0, maze[0].length-1);
		
		Set<Point> visited = new HashSet<>();
		Set<Point> marked = new HashSet<>();
		
		Point first = new Point(maze.length-1, 0);
		Queue<PointAndPath> q = new ArrayDeque<>();		
		q.add(new PointAndPath(first, "", 1));
		marked.add(first);
		
		while( ! q.isEmpty() ){
			PointAndPath pointAndPath = q.poll();			
			
			if( pointAndPath.point.equals(lastPoint) ){
				LOG.info("shortest path(" + pointAndPath.hops + "):  " + pointAndPath.path);
				break;
			}
			
			int row = pointAndPath.point.row;
			int col = pointAndPath.point.col;
			
			// right
			checkNewPoint(maze, pointAndPath, row, col+1, visited, marked, q);
			// left
			checkNewPoint(maze, pointAndPath, row, col-1, visited, marked, q);
			
			// top
			checkNewPoint(maze, pointAndPath, row-1, col, visited, marked, q);
			// bottom
			checkNewPoint(maze, pointAndPath, row+1, col, visited, marked, q);				
			
			
			visited.add( pointAndPath.point);			
		}
	}
	
	private void checkNewPoint(boolean[][] maze, PointAndPath base, int row, int col, 
			Set<Point> visited, Set<Point> marked, Queue<PointAndPath> q  ){
		
		if( row >= 0 && row < maze.length && col >= 0 && col < maze[row].length && maze[row][col]){			
			Point cur = new Point(row, col);
			if( !( visited.contains(cur) || marked.contains(cur) ) ){
				q.add(new PointAndPath(cur, base.path, base.hops+1));
				marked.add(cur);
			}			
		}
		
	}

	public static void main(String[] args) {
		try {
			new MazePathFinder();
		}
		catch (Exception ex) {
			LOG.error("Exception occurred", ex);
		}
	}
	
}
