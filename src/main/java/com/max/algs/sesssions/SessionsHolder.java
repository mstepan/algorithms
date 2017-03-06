package com.max.algs.sesssions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public enum SessionsHolder {
	
	INST;
	
	private static final Logger LOG = Logger.getLogger(SessionsHolder.class);
	
	private final Thread CLEANER_THREAD = new Thread( new SessionsCleaner() );
	
	private SessionsHolder(){
		new Thread(new SessionsCleaner()).start();
	}
	
	private final BlockingQueue<UserSesssion> sessions = new DelayQueue<>();
	private final ConcurrentMap<String, UserSesssion> sessionsMap = new ConcurrentHashMap<>();
	
	
	
	public List<String> getOnlineUsers( List<String> usersToCheck ){
		
		final List<String> onlineUsers = new ArrayList<>();
		
		for( String user : usersToCheck ){			
			if( sessionsMap.containsKey(user) ){
				onlineUsers.add( user );
			}
		}
		
		return onlineUsers;
	}
	
	
	public void addOrUpdateSession( String id ){
		
		if( CLEANER_THREAD.isAlive() ){
			CLEANER_THREAD.start();
		}
		
		if( sessionsMap.containsKey(id) ){
			UserSesssion session = sessionsMap.get(id);
			session.updateTimestamp();
		}
		else {		
			
			UserSesssion newUserSession = new UserSesssion(id);	
			
			UserSesssion prevSession = sessionsMap.putIfAbsent(id, newUserSession);
			
			if( prevSession == null ){
				sessions.add( newUserSession );
			}
		}
		
	}
	
	
	
	private static final class SessionsCleaner implements Runnable {
		
		private static final int POLL_DELAY = 3_000; // 3 seconds
		private static final int MAX_ITERATIONS = 3; // max iterations count withotu work
		

		private static final String THREAD_NAME = "SessionsCleaner";
		
		@Override
		public void run() {
			
			Thread.currentThread().setName(THREAD_NAME);
			
			LOG.info( THREAD_NAME + " started" );
			
			int iterationsPassed = 0;
			
			try{
				
				while( ! Thread.currentThread().isInterrupted() ){	
					
					UserSesssion expiredSession = SessionsHolder.INST.sessions.poll(POLL_DELAY, TimeUnit.MILLISECONDS);					
					
					if( expiredSession != null  ){	

						SessionsHolder.INST.sessionsMap.remove( expiredSession.getId(), expiredSession );
						
						LOG.info( "Session expired for id '" + expiredSession.getId() + "', sessions: " + SessionsHolder.INST.sessionsMap  );
						
						iterationsPassed = 0;
					}
					else {
						
						++iterationsPassed;
						
						if( iterationsPassed >= MAX_ITERATIONS ){					
							LOG.info( THREAD_NAME + ": Shutdown after '" + iterationsPassed + "' iterations.");
							break;
						}
						else {
							LOG.info( THREAD_NAME + ": No activity, iterations count: " + iterationsPassed + "." );
						}
					}					
				}
			}
			catch( InterruptedException interEx ){
				LOG.info( THREAD_NAME+ " was interrupted" );
				Thread.currentThread().interrupt();
			}
			
			LOG.info( THREAD_NAME + " finished, sessions: " + SessionsHolder.INST.sessionsMap );
			
		}
		
	}

}
