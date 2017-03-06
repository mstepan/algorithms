package com.max.algs.number;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;


public class BigNumber {
	
	
	private static final int POS_SIGN = 1;
	private static final int NEG_SIGN = -1;
	
	private final int sign;
	private final byte[] data;
	
	/**
	 * Construct BigNumber from string.
	 */
	public BigNumber(String str){
		
		int offset = 0;
		if( str.charAt(0) == '-' ){
			sign = -1;
			offset = 1;
		}
		else {		
			sign = 1;		
		}
		
		data = new byte[str.length()-offset];
		
		for( int i = 0; i < data.length; i++ ){
			
			char ch = str.charAt(i+offset);
			
			if( ! Character.isDigit(ch) ){
				throw new IllegalArgumentException("Not a number: " + str);
			}
			
			data[i] = (byte)(str.charAt(i+offset) - '0');
		}
	}
	
	private BigNumber(byte[] data, int sign){
		assert data != null : "data is NULL";
		this.sign = sign;
		this.data = data;
	}
	
	/**
	 * Multiply two BigNumbers.
	 * time: O(N*M)
	 * space: O(N*M)
	 */
	public BigNumber mul(BigNumber other){
		
		if( other == null ){
			throw new IllegalArgumentException("NULL 'other' passed");
		}		
		
		Queue<byte[]> partialSums = new ArrayDeque<byte[]>();
		
		int shiftSize = 0;
		for( int i = other.data.length-1; i >= 0; i--, shiftSize++ ){
			int otherDigit = other.data[i];
			
			byte[] mulValue = mulForDigit(otherDigit);	
			
			byte[] shifted = shiftLeft(mulValue, shiftSize);
			
			partialSums.add( shifted );
		}
		
		byte[] res = addAll(partialSums);
		
		return new BigNumber(res, isSameSign(other) ? POS_SIGN : NEG_SIGN);
	}
	
	private boolean isSameSign(BigNumber other){
		return sign == other.sign;
	}
	
	/**
	 * Shift byte[] array to the left adding zeros.
	 */
	private byte[] shiftLeft(byte[] mulValue, int shiftSize) {
		
		byte[] shifted = new byte[mulValue.length + shiftSize];
		
		System.arraycopy(mulValue, 0, shifted, 0, mulValue.length);
		
		for( int i = mulValue.length; i < shifted.length; i++ ){
			shifted[i] = 0;
		}
		
		return shifted;
	}

	/**
	 * Multiply this for one decimal digit.
	 */
	private byte[] mulForDigit(int otherDigit) {
		
		byte[] res = new byte[data.length+1];
		int carry = 0;
		
		for( int i = data.length-1; i >=0; i-- ){
			int temp = carry + data[i] * otherDigit;
			res[i+1] = (byte)(temp % 10);
			carry = temp/10;
		}
			

		return removeMostSignificantZeros(res);
	}
	
	private byte[] removeMostSignificantZeros(byte[] data){
		
		int i = 0;
		
		while( i < data.length-1 && data[i] == 0 ){
			++i;
		}
		
		if( i == 0 ){
			return data;
		}
		
		return Arrays.copyOfRange(data, i, data.length);		
	}

	private byte[] addAll(Queue<byte[]> numbers){
		
		
		while( numbers.size() != 1 ){
			byte[] num1 = numbers.poll();
			byte[] num2 = numbers.poll();
			
			byte[] sum = addNumbers(num1, num2);
			numbers.add(sum);
		}
		
		return numbers.poll();
	}
	
	private byte[] addNumbers(byte[] num1, byte[] num2){
		
		byte[] res = new byte[ Math.max(num1.length, num2.length) + 1];
		
		int carry = 0;
		int i = num1.length-1;
		int j = num2.length-1;
		int resIndex = res.length-1;
		
		while( i >= 0 || j >= 0 ){
			
			int digit1 = (i >= 0) ? num1[i--] : 0;			
			int digit2 = (j >= 0) ? num2[j--] : 0;
			
			int sum = carry + digit1 + digit2;
			
			res[resIndex--] = (byte)(sum % 10);
			carry = sum / 10;			
		}
		
		return removeMostSignificantZeros(res);
	}
	
	@Override
	public String toString(){
		StringBuilder buf = new StringBuilder(data.length);
		
		if( sign == NEG_SIGN ){
			buf.append("-");
		}
		
		for( byte digit : data ){
			buf.append(digit);
		}
		
		return buf.toString();
	}

}
