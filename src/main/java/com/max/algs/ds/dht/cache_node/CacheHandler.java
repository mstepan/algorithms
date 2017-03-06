package com.max.algs.ds.dht.cache_node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class CacheHandler extends AbstractHandler {
	
	
	@SuppressWarnings("unused")
	private final String name;	
	
	
	CacheHandler(String name) {
		super();
		this.name = name;
	}

	
	
	private Map<String, String> data = new ConcurrentHashMap<>();
	
	private Map<Integer, List<String>> indexToKey = new ConcurrentHashMap<>();
	
	
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)  throws IOException, ServletException {
		
		baseRequest.setHandled(true);
		
		if( target.indexOf("remap")  != -1 ){			
			remap( baseRequest, response );
			return;
		}
				
		switch (baseRequest.getMethod() ) {
			case "GET": {
				getValue( extractKey(target), response); 
				return;
			}
			case "POST": {
				put(extractKey(target), baseRequest);
				return;
			}
			case "DELETE": {
				delete(extractKey(target), baseRequest, response);
				return;
			}
		}
			
		response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
	}
	
	
	private void delete( String key, Request request, HttpServletResponse response ) throws IOException {
		data.remove(key);
		response.setStatus(HttpServletResponse.SC_OK);	
	}
	
	
	private void put( String key, Request request ) throws IOException {
		
		String[] valueIndexStr = readLine(request).split(",");
		
		String value = valueIndexStr[0];
		int bucketIndex = Integer.valueOf( valueIndexStr[1] );
	
		List<String> indexKeys = indexToKey.get( bucketIndex );
			
		if( indexKeys == null ){
			indexKeys = new ArrayList<>();
			indexToKey.put( bucketIndex, indexKeys );
		}
			
		indexKeys.add( key );				
			
		data.put(key, value);
	}
	
	
	
	private void getValue( String key, HttpServletResponse response ) throws IOException {
		
		response.setContentType("text/plain;charset=utf-8");				
		
		String value = data.get(key);
		
		if( value == null ){
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().println( String.format("Error: 'key %s not found'", key) );
		}
		else {
			response.setStatus(HttpServletResponse.SC_OK);			
			response.getWriter().println( value );
		}
	}
	
	/**
	 * [ {'key': '1', 'value': 'one'}, {'key': '2', 'value': 'two'} ]
	 * 
	 */
	private void remap( Request request, HttpServletResponse response ) throws IOException {			
		
		String[] rangeStr = readLine(request).split(",");	
		
		int start = Integer.valueOf( rangeStr[0] );
		int end = Integer.valueOf( rangeStr[1] );
		
		List<KeyValue> remapped;
		
		if( start <= end ){
			remapped = remapRange(start, end);			
		}
		else {
			remapped = remapRange(start, 359);
			remapped.addAll( remapRange(0, end) );
		}
		
		response.getWriter().println( compressToString(remapped) );
		
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	
	private String compressToString(List<KeyValue> remapped) {
		
		StringBuilder buf = new StringBuilder();
		
		for( int i =0; i < remapped.size(); i++ ){			
			KeyValue entry = remapped.get(i);		
			
			if( i > 0 ){
				buf.append("|");
			}
			
			buf.append(entry.key).append(",").append(entry.value).append(",").append(entry.bucketIndex);
		}

		return buf.toString();
	}


	private List<KeyValue> remapRange( int start, int end ){
		
		List<KeyValue> remapped = new ArrayList<>();
		
		for(int index = start; index <= end; index++ ){
			
			List<String> keys = indexToKey.get(index);
			
			if( keys != null ){
				for( String key : keys ){					
					remapped.add( new KeyValue(key, data.get(key), index) );					
					data.remove(key);				
				}	
				
				indexToKey.remove(index);
			}
		}
		
		return remapped;
	}
	
	private String readLine( HttpServletRequest request ) throws IOException {
		byte[] buf = new byte[2048];
		
		int readedBytes = request.getInputStream().read(buf);
		
		return new String(buf, 0, readedBytes);
	}
	
	private String extractKey(String target){
		
		int lastSlash = target.lastIndexOf("/");
		
		String key = target.substring(lastSlash+1);
		
		return key;
		
	}
	
	static final class KeyValue {
		
		final String key;
		final String value;
		final int bucketIndex;
		
		KeyValue(String key, String value, int bucketIndex) {
			super();
			this.key = key;
			this.value = value;
			this.bucketIndex = bucketIndex;
		}
		
		
	}

}
