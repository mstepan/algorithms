package merger.iterator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.google.common.io.Files;

public final class FileIterator implements Iterator<String>, AutoCloseable {
	

	private BufferedReader reader; 
	private String line;
	
	
	public FileIterator(Path path) {
		if( path == null ){
			throw new IllegalArgumentException("NULL 'path' parameter passed");
		}
		
		try{
			reader = Files.newReader(path.toFile(), Charset.defaultCharset());	
			line = readNextLine();
		}
		catch( FileNotFoundException fileNotFoundEx ){
			throw new IllegalStateException("Can't create reader for file: '" + path + "', file not found.", fileNotFoundEx );
		}
		
	}


	@Override
	public boolean hasNext() {
		return line != null;
	}

	@Override
	public String next() {
		
		if( !hasNext() ){
			throw new NoSuchElementException();
		}
		
		String retLine = line;		
		line = readNextLine();		
		return retLine;
	}
	
	private String readNextLine(){
		try {
			return reader.readLine();
		}
		catch( IOException ioEx ){
			throw new IllegalStateException(ioEx);
		}
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("'remove' doesn't supported by this iterator type");			
	}
	
	
	@Override
	public void close() throws Exception {
		if( reader != null ){
			reader.close();
		}		
	}
	
}
