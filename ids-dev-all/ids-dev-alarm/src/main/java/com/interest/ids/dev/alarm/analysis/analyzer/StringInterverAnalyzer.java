package com.interest.ids.dev.alarm.analysis.analyzer;

import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.dev.alarm.analysis.AlarmAnalyzer;
import com.interest.ids.dev.alarm.analysis.context.AlarmAnalysisContext;


/**
 * 
 * @author lhq
 * 组串式逆变器分析
 *
 */
public class StringInterverAnalyzer extends AbstractInverterAnalyzer implements AlarmAnalyzer{
		
	
	@Override
	public void analysis(AlarmAnalysisContext context) {
		if(context.getStringInverter().size() > 0){
			super.analysis(context);
		}
		context.fireAnalysis();
	}
	

	@Override
	Integer getDevType() {
		
		return DevTypeConstant.INVERTER_DEV_TYPE;
	}
	
	
}
