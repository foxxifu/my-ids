package com.interest.ids.dev.api.service;

import java.util.List;
import java.util.Map;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.signal.NormalizedInfo;
import com.interest.ids.common.project.bean.signal.NormalizedModel;




public interface NormalizedService {
	
	List<NormalizedModel> getUnModelsByDeviceType(Integer deviceType);
	
	List<NormalizedInfo>  getUnInfoBySignalVersion(String signalVersion);
	
	Map<NormalizedInfo,NormalizedModel> getInfo2ModelMap(Integer deviceType,String signalVersion);
	
	Map<Integer,String> getAddress2Column(Integer deviceType,String signalVersion);
	
	Map<Integer,List<String>> getModelId2Column(Integer deviceType,String signalVersion);
	/**
	 * 获取所有的设备信号点的id转换为归一化的列的对应关系
	 * @param deviceType 设备类型
	 * @param signalVersion 版本号
	 * @return Map<Integer, String> <signalId(信号点的id), columName(列名)>
	 */
	Map<Integer, List<String>> getAllModelId2Column(Integer deviceType, String signalVersion);
	
	Map<Integer,List<String>> getAddress2Column(DeviceInfo dev);

}
