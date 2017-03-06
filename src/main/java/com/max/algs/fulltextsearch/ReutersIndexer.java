package com.max.algs.fulltextsearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

import org.apache.log4j.Logger;

public class ReutersIndexer {

	private static final Logger LOG = Logger.getLogger(ReutersIndexer.class);
	
	public static void main(String[] args) {
		try {
			
			Path dir = Paths.get("/Users/admin/repo/incubator/data/reuters-cleaned");
			
			InMemoryIndex index = new InMemoryIndex();
			Files.list( dir ).forEach(new ReutersIndexerConsumer(index) );
			
			index.printStatistics();
			
			LOG.info("ReutersIndexer completed");
			
		}
		catch( Exception ex ){
			LOG.error("Indexing failed", ex);
		}

	}
	
	private static class ReutersIndexerConsumer implements Consumer<Path> {

		private final InMemoryIndex index;

		ReutersIndexerConsumer(InMemoryIndex index) {
			this.index = index;
		}

		@Override
		public void accept(Path path) {
			
			try( BufferedReader reader = Files.newBufferedReader(path)) {
				String line = null;
				
				while( (line = reader.readLine()) != null ){
					
					// case folding and splitting
					String[] words = line.toLowerCase().split("\\s+");
					
					for( String singleWord : words ){
						
						singleWord = singleWord.trim();
																		
						if( "".equals(singleWord) ){
							continue;
						}
						
						if( singleWord.charAt(singleWord.length()-1) == '.' ){
							singleWord = singleWord.substring(0, singleWord.length()-1);
						}
						
						// do stemming
						singleWord = SnowballStemmer.INST.stemm(singleWord);
						
						if( singleWord.length() < 3 ){
							continue;
						}
						
						// skip 'stop words'
						if( ! StopWords.INST.isStopWord(singleWord) ){
							index.add(singleWord, path.getFileName().toString());
						}
					}
				}
			}
			catch( IOException ioEx ){
				LOG.error("Can't properly index file", ioEx);
			}
		}
		
	}

}
