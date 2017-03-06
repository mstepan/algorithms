package com.max.algs.maze;


final class PointAndPath {
	
	final Point point;
	final String path;
	final int hops;
	
	PointAndPath(Point point, String prevPath, int hops) {
		this.point = point;
		this.path = (prevPath.length() == 0) ? String.valueOf(point) : prevPath + "->" + String.valueOf(point);
		this.hops = hops;
	}

}
