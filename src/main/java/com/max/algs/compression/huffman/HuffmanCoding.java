package com.max.algs.compression.huffman;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.max.algs.ds.heap.BinaryHeap;
import com.max.algs.io.BitInputStream;
import com.max.algs.io.BitOutputStream;


/**
 * Encode text file using Huffman coding.
 * 
 * @author Maksym Stepanenko
 *
 */
public class HuffmanCoding {
	
	private static final Logger LOG = Logger.getLogger(HuffmanCoding.class);
	
	private static final char[] LINE_SEPARATORS = System.getProperty("line.separator").toCharArray();
	
	private HuffmanNode root;
	
	public void encode(Path srcPath, Path destPath) {

		Map<Character, PrefixCode> codingMap = null;

		try (BufferedReader reader = Files.newBufferedReader(srcPath) ){

			Map<Character, Integer> frequency = new HashMap<>();

			String line = null;

			while ((line = reader.readLine()) != null) {
				updateCharFrequency(frequency, line);
			}

			BinaryHeap<HuffmanNode> minHeap = createMinHeap(frequency);

			root = createTree(minHeap);

			codingMap = createCodingMap();

		} 
		catch (IOException ioEx) {
			LOG.error("Can't read from file '" + srcPath + "'", ioEx);
		} 

		encodeFile(srcPath, destPath, codingMap);
	}
	
	private void encodeFile(Path srcPath, Path destPath, Map<Character, PrefixCode> codingMap) {
		
		try (BufferedReader reader =  Files.newBufferedReader(srcPath);
				FileOutputStream fileOut = new FileOutputStream(destPath.toFile());
				BitOutputStream bitOut = new BitOutputStream(fileOut)){
			
			String line = null;
			
			while( (line = reader.readLine()) != null ){
				for( char ch : line.toCharArray() ){
					writeCodedSingleChar(ch, codingMap, bitOut);
				}
				
				for( char lastCh : LINE_SEPARATORS ){
					writeCodedSingleChar(lastCh, codingMap, bitOut);
				}
			}
		}
		catch( IOException ioEx ){
			LOG.error(ioEx);
		}
	}
	
	private void writeCodedSingleChar(char ch, Map<Character, PrefixCode> codingMap, BitOutputStream bitOut )
			throws IOException {
		PrefixCode code = codingMap.get(ch);
		
		if( code == null ){
			throw new IllegalStateException("Unknown for encodign character found '" + ch + "'"); 
		}
		bitOut.write(code.bits, code.length);
	}

	private Map<Character, PrefixCode> createCodingMap() {
		
		Map<Character, PrefixCode> codingMap = new HashMap<Character, PrefixCode>();
		
		visitRec(root, 0, codingMap, 0);

		return codingMap;
	}
	
	
	private void visitRec(HuffmanNode node, int code, Map<Character, PrefixCode> codingMap, int bitsCount){
		
		checkNotNull(node);
		
		if( node.isLeaf() ){			
			codingMap.put(node.ch, new PrefixCode(code, bitsCount));				
			return;
		}
		
		visitRec(node.left, code<<1, codingMap, bitsCount+1);
		visitRec(node.right, (code<<1) | 1, codingMap, bitsCount+1);		
	}

	private HuffmanNode createTree(BinaryHeap<HuffmanNode> minHeap){
		
		while( minHeap.size()  != 1 ){
			HuffmanNode node1 = minHeap.poll();
			HuffmanNode node2 = minHeap.poll();
			
			HuffmanNode combinedNode = new HuffmanNode(node1, node2);
			
			minHeap.add(combinedNode);
		}
		
		return minHeap.poll();
	}

	private BinaryHeap<HuffmanNode> createMinHeap(Map<Character, Integer> frequency) {
		BinaryHeap<HuffmanNode> minHeap = BinaryHeap.minHeap();
		
		for( Map.Entry<Character, Integer> entry : frequency.entrySet() ){
			minHeap.add( new HuffmanNode(entry.getKey(), entry.getValue()));
		}
		return minHeap;
	}


	private void updateCharFrequency(Map<Character, Integer> frequency, String line) {
		
		/** Write main line */
		for( char ch : line.toCharArray() ){
			updateSingleCharFreq(frequency, ch);
		}
		
		/** Write line separators */
		for( char lastCh : LINE_SEPARATORS ){
			updateSingleCharFreq(frequency, lastCh);
		}
	}

	private void updateSingleCharFreq(Map<Character, Integer> frequency, char ch) {
		Integer count = frequency.get(ch);
		
		if( count == null ){
			count = Integer.valueOf(1);
		}
		else {
			count += 1;
		}
		
		frequency.put(ch, count);
	}
	
	
	public void decode(Path srcPath, Path destPath) {
		
		try( FileInputStream fileIn = new FileInputStream(srcPath.toFile());
				BitInputStream in = new BitInputStream(fileIn);
				OutputStream out = Files.newOutputStream(destPath)){
			
			HuffmanNode cur = root;
			
			while( true ){
				
				if( cur.isLeaf() ){					
					out.write( cur.ch );
					cur = root;
				}
				
				int singleBit = in.read();
				
				if( singleBit == 0 ){
					cur = cur.left;
				}
				else {
					cur = cur.right;
				}

			}
			
		}
		catch( EOFException eofEx ){
			// do nothing here stream completed
		}
		catch( IOException ioEx ){
			LOG.error(ioEx);
		}
		
		LOG.info( "Huffman completed !!!" );
	}
	
	public static void main(String[] args){
		try {
			
			Path srcPath = Paths.get(HuffmanCoding.class.getResource("in-huffman.txt").getFile());
			
			Path destPath = Paths.get("src/main/java/com/max/algs/compression/huffman/out-huffman-coded.txt");
			
			Files.deleteIfExists(destPath);			
			Files.createFile(destPath);
			
			HuffmanCoding huffman = new HuffmanCoding();			
			huffman.encode(srcPath, destPath);
			
			Path decodedPath = Paths.get("src/main/java/com/max/algs/compression/huffman/out-original.txt");
			
			Files.deleteIfExists(decodedPath);			
			Files.createFile(decodedPath);
			
			huffman.decode(destPath, decodedPath);
		}
		catch(final Exception ex){
			LOG.error(ex);
		}
	}

}
