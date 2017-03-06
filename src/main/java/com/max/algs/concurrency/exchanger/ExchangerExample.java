package com.max.algs.concurrency.exchanger;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public final class ExchangerExample {
	
	private static final Logger LOG = Logger.getLogger(ExchangerExample.class);
	
	private static final CountDownLatch ALL_TH_FINISHED = new CountDownLatch(2);
	
//	private static final Exchanger<ByteData> changer = new Exchanger<>();
	
	private static final CustomExchanger<ByteData>changer = new CustomExchanger<>();
	
	private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(2);
	
	private static class ByteData {
		private final byte[] data = new byte[5];
		private int filledBytes;
	}
	
	private static class Reader implements Runnable {

		@Override
		public void run() {
			
			try(InputStream inStream = ExchangerExample.class.getResourceAsStream("in.txt"); 
					BufferedInputStream bufStream = new BufferedInputStream(inStream) ){
				
				ByteData bytes = new ByteData();
				int readedBytes = -1;
				
				while( (readedBytes = bufStream.read( bytes.data )) != -1 ){
					bytes.filledBytes = readedBytes;
					bytes = changer.exchange( bytes );
				}
				
				bytes.filledBytes = -1;
				changer.exchange( bytes );
				
			}
			catch( IOException ioEx ){
				LOG.error(ioEx);
			}
			catch( InterruptedException interEx ){
				LOG.error(interEx);
				Thread.currentThread().interrupt();
			}
			finally {
				ALL_TH_FINISHED.countDown();
			}
			
		}
		
	}
	
	private static class Writer implements Runnable {

		@Override
		public void run() {
			
			ByteData bytes = new ByteData();
			
			
			try{
				while( true ){
					TimeUnit.MILLISECONDS.sleep(500);
					bytes = changer.exchange(bytes);
					
					// check for 'poison' exchange
					if( bytes.filledBytes < 0 ){
						break;
					}
					System.out.print( new String(bytes.data, 0, bytes.filledBytes) );
					System.out.flush();					
				}	
				LOG.info("");
			}
			catch( InterruptedException interEx ){
				LOG.error(interEx);
				Thread.currentThread().interrupt();
			}
			finally {
				ALL_TH_FINISHED.countDown();
			}
			
		}
		
	}
	

	private ExchangerExample() throws Exception {
		
		THREAD_POOL.execute( new Reader() );
		THREAD_POOL.execute( new Writer() );
		
		ALL_TH_FINISHED.await(30, TimeUnit.SECONDS);
		
		THREAD_POOL.shutdown();
		if( ! THREAD_POOL.awaitTermination(3, TimeUnit.SECONDS) ){
			THREAD_POOL.shutdownNow();
		}
		
		LOG.info( "Main done" );
	}


	public static void main(String[] args) {
		try {
			new ExchangerExample();
		}
		catch( Exception ex ){
			LOG.error(ex);
		}

	}

}
