package com.interest.ids.dev.api.service;

import java.util.List;

import com.interest.ids.common.project.bean.signal.SignalInfo;
import com.interest.ids.common.project.bean.signal.SubscribeSignal;

/**
 * 
 * @author lhq
 *
 *
 */
public interface SubscribeSignalService {
	
	
	List<SubscribeSignal> getSubSignalsByModelCode(String modelCode);
	
	List<SignalInfo> getSubSignals(String modelCode);

}