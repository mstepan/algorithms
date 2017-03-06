package com.max.algs.compression;


public class MatchedData {
	
	private final int offset;
	private final int length;
	private final int charBase;
	
	
	public static MatchedData notMatched( char ch ){
		return new MatchedData(0, 0, ch);
	}
	
	
	public MatchedData(int offset, int length, int charBase) {
		super();
		this.offset = offset;
		this.length = length;
		this.charBase = charBase;
	}
	
	public int getOffset() {
		return offset;
	}
	
	public int getLength() {
		return length;
	}
	
	public int getCharBase() {
		return charBase;
	}
	
	

}
