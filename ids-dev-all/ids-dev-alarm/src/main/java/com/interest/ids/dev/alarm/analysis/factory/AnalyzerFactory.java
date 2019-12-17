package com.interest.ids.dev.alarm.analysis.factory;

import com.interest.ids.dev.alarm.analysis.AlarmAnalyzer;


/**
 * 
 * @author lhq
 *
 *
 */
public interface AnalyzerFactory {
	
	public AlarmAnalyzer newAnalyzer(Class<? extends AlarmAnalyzer> clazz);

}
