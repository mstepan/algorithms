package com.max.functional.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

public final class ForkJoinPoolUtils {
	
	private static final int CPUS_COUNT = Runtime.getRuntime().availableProcessors();
	
	private ForkJoinPoolUtils(){}
	
	
	public static <T> T executeInDedicatedThreadPool(Callable<T> task){
		return executeInDedicatedThreadPool(task, CPUS_COUNT);
	}
	
	public static <T> T executeInDedicatedThreadPool(Callable<T> task, int threadsCount){
		try{
			return new ForkJoinPool(threadsCount).submit(task).get();
		}
		catch( ExecutionException execEx ){
			throw new IllegalStateException(execEx);
		}
		catch(InterruptedException interEx ){
			Thread.currentThread().interrupt();
			throw new IllegalStateException(interEx);
		}
	}

}
