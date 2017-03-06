package com.max.functional;

public final class CheckUtils {
	
	
	private CheckUtils(){}
	
	
	public static final <T> void assertNotNull(T valueToCheck, String errorMsg){
		if( valueToCheck == null ){
			throw new IllegalArgumentException(errorMsg);
		}
	}

}
