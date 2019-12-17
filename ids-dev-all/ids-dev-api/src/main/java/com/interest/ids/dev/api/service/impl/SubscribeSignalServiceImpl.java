package com.interest.ids.dev.api.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.interest.ids.common.project.bean.signal.SignalInfo;
import com.interest.ids.common.project.bean.signal.SubscribeSignal;
import com.interest.ids.common.project.mapper.signal.SignalInfoMapper;
import com.interest.ids.common.project.mapper.signal.SubscribeSignalMapper;
import com.interest.ids.dev.api.service.SubscribeSignalService;

@Service("subscribeSignalService")
public class SubscribeSignalServiceImpl implements SubscribeSignalService{
	
	@Resource
	private SubscribeSignalMapper subMapper;
	
	@Resource
	private SignalInfoMapper signalMapper;

	@Override
	public List<SubscribeSignal> getSubSignalsByModelCode(String modelCode) {
		Example ex = new Example(SubscribeSignal.class);
		Example.Criteria criteria = ex.createCriteria();
		criteria.andEqualTo("modelVersionCode", modelCode);
		return subMapper.selectByExample(ex);
	}

	@Override
	public List<SignalInfo> getSubSignals(String modelCode) {
		List<SubscribeSignal> subSignals = getSubSignalsByModelCode(modelCode);
		
		if(subSignals != null && subSignals.size() > 0){
			List<Integer> list = new ArrayList<Integer>();
			for(SubscribeSignal signal : subSignals){
				list.add(signal.getSignalAddress());
			}
			Example ex = new Example(SignalInfo.class);
			Example.Criteria criteria = ex.createCriteria();
			criteria.andEqualTo("modelVersionCode", modelCode);
			criteria.andIn("signalAddress", list);
			return signalMapper.selectByExample(ex);
		}
		
		return null;
	}
	
	
	
	

}