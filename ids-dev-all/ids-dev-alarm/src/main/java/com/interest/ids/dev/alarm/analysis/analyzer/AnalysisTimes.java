package com.interest.ids.dev.alarm.analysis.analyzer;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 
 * @author lhq
 *
 *
 */
public class AnalysisTimes {
	
	public static AnalysisTimes times = new AnalysisTimes();
	
	private AnalysisTimes(){}
	
	public static AnalysisTimes getInstance(){
		return times;
	}
	
	private AtomicLong num = new AtomicLong(0);
	
	public long increase(){
		return num.incrementAndGet();
	}
	
	public long getTimes(){
		return num.get();
	}

}
