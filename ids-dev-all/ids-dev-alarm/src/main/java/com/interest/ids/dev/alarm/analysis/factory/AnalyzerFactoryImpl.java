package com.interest.ids.dev.alarm.analysis.factory;

import com.interest.ids.dev.alarm.analysis.AlarmAnalyzer;

/**
 * 
 * @author lhq
 *
 *
 */
public class AnalyzerFactoryImpl implements AnalyzerFactory{

	@Override
	public AlarmAnalyzer newAnalyzer(Class<? extends AlarmAnalyzer> clazz) {
		try {
			AlarmAnalyzer analyzer = clazz.newInstance();
			return analyzer;
		} catch (Exception e) {
			
		} 
		return null;
	}

}
