package com.max.algs.string;

import java.util.AbstractList;
import java.util.Arrays;

import org.apache.log4j.Logger;

/**
 * Not thread safe mutable string implementation.
 * 
 * @author Maksym Stepanenko.
 *
 */
public class MutableString extends AbstractList<Character> implements CharSequence, java.io.Serializable {

	private static final Logger LOG = Logger.getLogger(MutableString.class);

	private static final long serialVersionUID = 7632227735689807023L;
	
	private static final int DEFAULT_CAPACITY = 16;
	
	private char[] arr;
	private int size; 
	
	public MutableString() {
		arr = new char[DEFAULT_CAPACITY];
		size = 0;
	}
	
	public MutableString(String str) {
		checkNull(str);
		arr = str.toCharArray();
		size = arr.length;
	}
	
	private MutableString(char[] baseArr, int from, int to) {
		assert baseArr != null;
		assert from >=0;
		assert to < baseArr.length;
		assert from <= to;
		
		size = to-from+1;
		arr = new char[size];
		
		int index = 0;
		for( int i = from; i <= to; i++ ){
			arr[index] = baseArr[i];
		}
	}

	@Override
	public int length() {
		return size;
	}

	@Override
	public char charAt(int index) {		
		checkBoundary(index);
		return arr[index];
	}

	public MutableString append(String newStr){
		
		checkNull(newStr);
		
		int index = size;
		for(int i = 0; i < newStr.length(); i++, index++ ){
			
			if( index >= arr.length ){
				resize();
			}
			
			arr[index] = newStr.charAt(i);
			++size;
		}
		
		return this;
	}		 
	
	@Override
	public CharSequence subSequence(int start, int end) {
		checkBoundary(start);
		checkBoundary(end);
		return new MutableString(arr, start ,end);
	}
	
	@Override
	public void clear(){
		arr = new char[DEFAULT_CAPACITY];
		size = 0;
	}
	
	@Override
	public Character get(int index) {
		checkBoundary(index);
		return arr[index];
	}

	@Override
	public int size() {
		return size;
	}
		
	@Override
	public String toString(){
		return new String(arr, 0, size);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(arr);
		result = prime * result + size;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		if (obj == null || getClass() != obj.getClass()){
			return false;
		}
		MutableString other = (MutableString) obj;
		if (!Arrays.equals(arr, other.arr))
			return false;
		if (size != other.size)
			return false;
		return true;
	}
	
	private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
		in.defaultReadObject();

		arr = new char[in.readInt()];
		size = in.readInt();
		
		for( int i =0; i < size; i++ ){
			arr[i] = in.readChar();
		}
	}
	
	
	private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
		out.defaultWriteObject();
		out.writeInt(arr.length);
		out.writeInt(size);
		
		for( int i =0; i < size; i++ ){
			out.writeChar(arr[i]);
		}
	}
	
	private void resize(){
		char[] tempArr = arr;		
		arr = new char[arr.length<<1];		
		System.arraycopy(tempArr, 0, arr, 0, tempArr.length);
		LOG.info("'arr' resized from " + tempArr.length + " to " + arr.length);
	}

	private void checkNull(String str){
		if( str == null ){
			throw new IllegalArgumentException("NULL string parameter passed");
		}
	}

	private void checkBoundary(int index){
		if( index < 0 || index >= size ){
			throw new IndexOutOfBoundsException();
		}
	}

}
