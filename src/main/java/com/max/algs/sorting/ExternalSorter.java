package com.max.algs.sorting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.max.algs.util.FileUtils;



public final class ExternalSorter {
	
	
	private static final Logger LOG = Logger.getLogger(ExternalSorter.class);
	
	private ExternalSorter(){
		super();
	}
	
	
	private static final int CHARS_LIMIT = 200;
	
	

	private static String clearWord(String word){
		
		int lastCh = 0;
		char[] arr = new char[word.length()];
		
		for( int i =0; i < word.length(); i++ ){	
			char ch = word.charAt(i);
			if( Character.isAlphabetic(ch) && ch != ' '){
				arr[lastCh] = Character.toLowerCase( ch );
				++lastCh;
			}
		}
		
		return new String(arr, 0, lastCh);
	}
	
	
	private static void writeToFileSorting( Path filePath, List<String> words ){
		
		Collections.sort( words );
		
		try{
			
			boolean alreadyExist = Files.exists(filePath); 
			
			if( !alreadyExist ){
				Files.createFile( filePath );
			}
			
			try( BufferedWriter writer = Files.newBufferedWriter(filePath, Charset.defaultCharset(), StandardOpenOption.APPEND) ){
				
				// write new line to file
				if( alreadyExist ){
					writer.newLine();
				}
				
				for( String word : words ){
					writer.write( word );
					writer.write(" ");
				}
			}
			
		}
		catch( IOException ioEx ){
			LOG.error(ioEx);
		}
	}
	
	public static void sort( Path mainFilePath ){
		
		Path[] tapes = new Path[]{
				Paths.get( mainFilePath.getParent().toString(), FileUtils.getFileNameAndExtension(mainFilePath)[0] + "_1.tmp"),
				Paths.get( mainFilePath.getParent().toString(), FileUtils.getFileNameAndExtension(mainFilePath)[0] + "_2.tmp"),
				Paths.get( mainFilePath.getParent().toString(), FileUtils.getFileNameAndExtension(mainFilePath)[0] + "_3.tmp"),
				Paths.get( mainFilePath.getParent().toString(), FileUtils.getFileNameAndExtension(mainFilePath)[0] + "_4.tmp")
		};
		
			
		if( ! Files.exists( mainFilePath ) ){
			throw new IllegalArgumentException("'" + mainFilePath + "' doesn't exist");
		}
		
		List<String> words = new ArrayList<>();		
		
		int fromTape = 0;		
		int toTape = tapes.length/2-1;		
		int curTape = 0;
		
		int totalChars = 0;
		
		try( BufferedReader reader = Files.newBufferedReader(mainFilePath, Charset.defaultCharset()) ){
			
			String line;
			
			while( (line = reader.readLine()) != null ){
				
				String[] wordsFromLine = line.split("\\s+");
				
				for( String singleWord : wordsFromLine ){
					
					singleWord = clearWord(singleWord); 
					
					if( singleWord.length() > 0 ){											
						if( totalChars + singleWord.length() > CHARS_LIMIT){							
							writeToFileSorting( tapes[curTape], words );							
							words.clear();
							totalChars = 0;
							++curTape;
							
							if( curTape > toTape ){
								curTape = fromTape;
							}
						}
						
						words.add( singleWord );
						totalChars += singleWord.length();		
					}
				}				
			}			
		}
		catch( IOException ioEx ){
			LOG.error(ioEx);
		}
		
		Path res = mergeTapes( tapes[0], tapes[1], tapes[2], tapes[3] );
		
		try{			
			String[] nameAndExtension = FileUtils.getFileNameAndExtension(mainFilePath);			
			Path dstFile = Paths.get(mainFilePath.getParent().toString(), nameAndExtension[0] + "_" + new Date().getTime() + "." + nameAndExtension[1] );		
			Files.copy( res, dstFile );
		}
		catch( IOException ioEx ){
			LOG.error(ioEx);
		}

	}
	
	private static Path mergeTapes( Path src1, Path src2, Path dst1, Path dst2 ){
		
		try{				
			SortedFileSequenceIterator it1  = new SortedFileSequenceIterator( src1 );
			SortedFileSequenceIterator it2  = new SortedFileSequenceIterator( src2 );
			
			try( BufferedWriter writer1 = Files.newBufferedWriter(dst1, Charset.defaultCharset()); 
					BufferedWriter writer2 = Files.newBufferedWriter(dst2, Charset.defaultCharset())){
				
				BufferedWriter curWriter = writer1;
				
				while( ! it1.fullyFinished() && ! it2.fullyFinished() ){		
					
					String word1 = it1.next();
					String word2 = it2.next();
					
					while( it1.hasNext() || it2.hasNext() ){
						
						if( word1 == null ){							
							curWriter.write( word2 );
							word2 = it2.hasNext() ? it2.next() : null;
						}
						else if( word2 == null ){
							curWriter.write( word1 );
							word1 = it1.hasNext() ? it1.next() : null;
						}
						else {
							if( word1.compareTo(word2) < 0 ){
								curWriter.write( word1 );
								word1 = it1.hasNext() ? it1.next() : null;
							}
							else {
								curWriter.write( word2 );
								word2 = it2.hasNext() ? it2.next() : null;
							}
						}						
					}	
					
					curWriter.write( System.lineSeparator() );
					
					if( curWriter == writer1 ){
						curWriter = writer2;						
					}
					else {
						curWriter = writer1;	
					}
				}				
			}			
		}
		catch( IOException ex ){
			LOG.error(ex);
		}
		
		return src1;
	}

	
	
	public static void main( String[] args ){			
		
		Path baseDir = Paths.get("D:/workspace-temp/incubator/algorithms/data");
		
		FileUtils.removeFilesWithExtension(baseDir, "tmp");		
		
		Path mainFile = Paths.get( baseDir.toString(), "in.txt"); 
		ExternalSorter.sort( mainFile );
		LOG.info( "'" + mainFile  + "' sorted");
		
		
	}

}
