package com.max.algs.util;

import java.util.Calendar;
import java.util.Date;

public final class DateUtils {
	
	private DateUtils(){
		throw new IllegalStateException( "Can't instantiate utility class " + DateUtils.class.getCanonicalName() );
	}
	
	
	
	private static final int DEGREES_PER_CLOCK_DIVISION = 6;
	private static final int MINUTES_DIVISIONS_PER_HOUR = 5;
	
	/**
	 * 
	 * Calculate angle between hours and minutes.
	 * 
	 */
	public static int calculateAngle( Calendar calendar ){ 
		
		if( calendar == null ){
			throw new IllegalArgumentException("NULL 'calendar' parameter passed");
		}
		
		
		int angle = Math.abs( calendar.get(Calendar.HOUR) * MINUTES_DIVISIONS_PER_HOUR -  calendar.get(Calendar.MINUTE)) * DEGREES_PER_CLOCK_DIVISION;
		
		assert angle < 360 : "angle is greater then 360: '" + angle + "'";
		
		return angle;
	}
	
	
	public static int calculateAngle( Date date ){ 
		
		if( date == null ){
			throw new IllegalArgumentException("NULL 'date' parameter passed");
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime( date );
		
		return calculateAngle( calendar );
	}

}
