package com.interest.ids.dev.alarm.utils;

import java.util.Calendar;


/**
 * 
 * @author lhq
 *
 *
 */
public class LocalTimeZone {
	
	private long lastCalcTime = 0;
	
	private long DEFAULT_RANGE = 3600 * 1000;
	
	private int localTimeZone;
	
	private LocalTimeZone(){}
	
	private static LocalTimeZone timeZone = new LocalTimeZone();
	
	public static final LocalTimeZone getInstance(){
		return timeZone;
	}
	
	public int getLocalOPTimeZone(){
		long currentTime = System.currentTimeMillis();
		long range = currentTime - lastCalcTime;
		if(range > DEFAULT_RANGE){
			
			calcTimeZone();
		}
		return localTimeZone;
	}
	
	private void calcTimeZone(){
		
		Calendar cal = Calendar.getInstance();
        int offset = cal.get(Calendar.ZONE_OFFSET);
        cal.add(Calendar.MILLISECOND, -offset);
        Long timeStampUTC = cal.getTimeInMillis();
        Long timeZone = (System.currentTimeMillis() - timeStampUTC) / (1000 * 3600);
        localTimeZone = timeZone.intValue();
	}

}
