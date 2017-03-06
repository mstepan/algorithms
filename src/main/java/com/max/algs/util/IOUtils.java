package com.max.algs.util;

import java.io.Closeable;
import java.io.IOException;

import org.apache.log4j.Logger;

public final class IOUtils {
	
	private static final Logger LOG = Logger.getLogger(IOUtils.class); 
	
	public static void closeSilently( Closeable stream ){
		try{
			if( stream != null ){
				stream.close();
			}
		}
		catch( IOException ioEx ){
			LOG.error(ioEx);
		}
	}
	
	
	private IOUtils(){
		super();
	}

}
