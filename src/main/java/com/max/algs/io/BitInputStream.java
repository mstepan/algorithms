package com.max.algs.io;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;


public class BitInputStream extends InputStream {
	
	private final InputStream in;
	
	private int chunkSizeInBits = -1;
	private int curBitIndex = -1;
	
	private byte[] chunk;

	public BitInputStream(InputStream in) {
		checkNotNull(in);
		this.in = in;
	}


	@Override
	public int read() throws IOException {
		
		if( chunkSizeInBits == -1 || curBitIndex == chunkSizeInBits ){
			
			int readByte = in.read();
			
			if( readByte == -1 || readByte == 0 ){
				throw new EOFException();
			}
			
			chunkSizeInBits = readByte & 0xFF;
			curBitIndex = 0;
			
			chunk = new byte[chunkSizeInBytes(chunkSizeInBits)];
			in.read(chunk);
		}
		
		int chunkIndex = curBitIndex >> 3; // curBitIndex / 8
		
		int curByte = chunk[chunkIndex];
		int bitWithinByteIndex = curBitIndex & 0x07; // curBitIndex % 8

		int byteValue = 0;
		
		/** last byte, check if it was not fully filled */
		if( chunkIndex == chunk.length-1 && (chunkSizeInBits & 0x07) != 0 ){
			int offset = (chunkSizeInBits & 0x07) - 1;
			byteValue = (curByte >>> (offset-bitWithinByteIndex)) & 1;
		}
		else {
		
			/** extract single bit from byte, bits are stored in reverse order */
			byteValue = (curByte >>> (7-bitWithinByteIndex)) & 1;
		}
		
		++curBitIndex;		

		return byteValue;
	}
	
	private int chunkSizeInBytes(int sizeInBits){
		int sizeInBytes = sizeInBits/8;
		
		if( (sizeInBits & 0x07) != 0 ){
			++sizeInBytes;
		}
		return sizeInBytes;
	}

}
