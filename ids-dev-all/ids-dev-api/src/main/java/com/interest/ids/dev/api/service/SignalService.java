package com.interest.ids.dev.api.service;

import io.netty.util.collection.IntObjectHashMap;

import java.util.List;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.signal.SignalInfo;

/**
 * 
 * @author lhq
 *
 *
 */
public interface SignalService {
	
	//获取所有信号点
	List<SignalInfo> getAllSignal();
    //根据SN获取铁牛数采下的所有信号点
	List<SignalInfo> getSignalsBySn(String sn);
	//根据设备ID获取信号点
	List<SignalInfo> getSignalsByDeviceId(Long deviceId);
	
	List<SignalInfo> getSignalsByDeviceIdAndModelId(Long deviceId, List<Long> modelIds);
	
	IntObjectHashMap<SignalInfo> getMutipleSignals(Long devId);
	
	IntObjectHashMap<SignalInfo> getDcSignals(String esn);
	
	List<SignalInfo> getSignalsByEsn(String esn);
	
	List<SignalInfo> getSignalsByDev(DeviceInfo dev);
	//根据设备ID和信号点地址获取信号点信息
	SignalInfo  getSignalByDevSnAndAddress(String sn,Integer SigAddress);
	//根据版本获取

}
