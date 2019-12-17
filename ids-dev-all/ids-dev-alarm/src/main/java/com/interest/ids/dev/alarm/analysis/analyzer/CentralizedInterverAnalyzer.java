package com.interest.ids.dev.alarm.analysis.analyzer;

import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.dev.alarm.analysis.AlarmAnalyzer;
import com.interest.ids.dev.alarm.analysis.context.AlarmAnalysisContext;


/**
 * 
 * @author lhq
 * 集中式逆变器分析
 *
 */
public class CentralizedInterverAnalyzer extends AbstractInverterAnalyzer implements AlarmAnalyzer{
	
	
	
	
	@Override
	public void analysis(AlarmAnalysisContext context) {
		if(context.getCentInverter().size() > 0){
			super.analysis(context);
		}
		context.fireAnalysis();
	}
	
	@Override
	Integer getDevType() {
		
		return DevTypeConstant.CENTER_INVERT_DEV_TYPE;
	}
}
