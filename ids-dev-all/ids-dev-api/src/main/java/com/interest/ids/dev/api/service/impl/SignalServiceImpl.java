package com.interest.ids.dev.api.service.impl;

import io.netty.util.collection.IntObjectHashMap;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.signal.SignalInfo;
import com.interest.ids.common.project.mapper.signal.SignalInfoMapper;
import com.interest.ids.dev.api.localcache.DeviceLocalCache;
import com.interest.ids.dev.api.service.DevDeviceService;
import com.interest.ids.dev.api.service.SignalService;



@Service("signalService")
public class SignalServiceImpl implements SignalService{
	
	@Resource
	private SignalInfoMapper signalMapper;
	@Resource
	private DevDeviceService devService;
	
	@Override
	public List<SignalInfo> getAllSignal(){
		
		return signalMapper.selectAll();
	}
	
	
	public List<SignalInfo> getSignalsBySn(String sn){
		DeviceInfo device = DeviceLocalCache.getData(sn);
		if(device != null){
			Example ex = new Example(DeviceInfo.class);
			Example.Criteria criteria = ex.createCriteria();
			criteria.andEqualTo("signalVersion", device.getSignalVersion());
			
			List<SignalInfo> signals = signalMapper.selectByExample(ex);
			return signals;
		}
		return null;
	}


	@Override
	public List<SignalInfo> getSignalsByDeviceId(Long deviceId) {
		Example ex = new Example(SignalInfo.class);
		Example.Criteria criteria = ex.createCriteria();
		criteria.andEqualTo("deviceId", deviceId);
		ex.setOrderByClause("signal_address asc");
		List<SignalInfo> signals = signalMapper.selectByExample(ex);
		return signals;
	}
	
	@Override
	public List<SignalInfo> getSignalsByDeviceIdAndModelId(Long deviceId,List<Long> modelIds){
		
		Example ex = new Example(SignalInfo.class);
		Example.Criteria criteria = ex.createCriteria();
		criteria.andEqualTo("deviceId", deviceId);
		criteria.andIn("modelId", modelIds);
		
		List<SignalInfo> signals = signalMapper.selectByExample(ex);
		
		return signals;
	}


	@Override
	public IntObjectHashMap<SignalInfo> getMutipleSignals(Long devId) {
		List<DeviceInfo> childDevs = devService.getChildDevices(devId);
		if(childDevs != null){
			IntObjectHashMap<SignalInfo> signals = new IntObjectHashMap<SignalInfo>();
			for(DeviceInfo childDev : childDevs){
				List<SignalInfo> childSignals = getSignalsByDeviceId(childDev.getId());
				for(SignalInfo signal:childSignals){
					signals.put(signal.getSignalAddress(), signal);
				}
			}
			return signals;
		}
		return null;
	}
	
	@Override
	public IntObjectHashMap<SignalInfo> getDcSignals(String sn){
		DeviceInfo dev = DeviceLocalCache.getData(sn);
		if(dev != null){
			return getMutipleSignals(dev.getId());
		}
		return null;
	}
	
	public List<SignalInfo> getSignalsByEsn(String esn){
		DeviceInfo device = DeviceLocalCache.getData(esn);
		if(device != null){
			Example ex = new Example(SignalInfo.class);
			Example.Criteria criteria = ex.createCriteria();
			criteria.andEqualTo("signalVersion", device.getSignalVersion());
			
			List<SignalInfo> signals = signalMapper.selectByExample(ex);
			return signals;
		}
		return null;
	}
	
	public List<SignalInfo> getSignalsByDev(DeviceInfo dev){
		if(dev != null){
			Example ex = new Example(SignalInfo.class);
			Example.Criteria criteria = ex.createCriteria();
			criteria.andEqualTo("signalVersion", dev.getSignalVersion());
			
			List<SignalInfo> signals = signalMapper.selectByExample(ex);
			return signals;
		}
		return null;
	}


	@Override
	public SignalInfo getSignalByDevSnAndAddress(String sn,
			Integer SigAddress) {
		DeviceInfo device = DeviceLocalCache.getData(sn);
		Example ex = new Example(SignalInfo.class);
		Example.Criteria criteria = ex.createCriteria();
		criteria.andEqualTo("deviceId", device.getId());
		criteria.andEqualTo("signalAddress",SigAddress);
		List<SignalInfo> signals = signalMapper.selectByExample(ex);
		if(signals!=null&&signals.size()>0)
			return signals.get(0);
		return null;
	}
}
