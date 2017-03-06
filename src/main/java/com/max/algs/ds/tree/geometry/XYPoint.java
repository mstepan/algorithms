package com.max.algs.ds.tree.geometry;

import java.util.Comparator;

public class XYPoint implements java.io.Serializable {
	
	public static final Comparator<XYPoint> X_CMP = new Comparator<XYPoint>(){
		@Override
		public int compare(XYPoint first, XYPoint second) {			
			return Integer.compare(first.x, second.x);
		}		
	};
	
	public static final Comparator<XYPoint> Y_CMP = new Comparator<XYPoint>(){
		@Override
		public int compare(XYPoint first, XYPoint second) {			
			return Integer.compare(first.y, second.y);
		}		
	};

	private static final long serialVersionUID = 169126389859696350L;
	
	private final int x;
	private final int y;
	
	public XYPoint(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	@Override
	public String toString(){
		return "(" + x +", " + y + ")";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		XYPoint other = (XYPoint) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
}
