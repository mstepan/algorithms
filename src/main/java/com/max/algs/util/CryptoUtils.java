package com.max.algs.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class CryptoUtils {
	
	
	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

	
	private CryptoUtils(){
		super();
	}
	
	
	public static String toSha256(String str ){	
		try{			
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");	
	
			messageDigest.update( str.getBytes() );		
	
			// sha-256 => 64 hex chars
			return toHexString(messageDigest.digest()) ;
		}
		catch( NoSuchAlgorithmException ex ){
			throw new IllegalStateException("Can't find digest algorithm 'SHA-256'");
		}

	}	
	
	
	
	private static String toHexString( byte[] data ){
		
		StringBuilder buf = new StringBuilder();
		
		for( byte singleByte : data ){
			buf.append( HEX_DIGITS[ (singleByte>>>4) & 0x0F] );
			buf.append( HEX_DIGITS[singleByte & 0x0F] );			
		}
		
		return buf.toString();
	}

}
