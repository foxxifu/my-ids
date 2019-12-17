package com.interest.ids.dev.api.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.interest.ids.common.project.bean.alarm.AlarmModel;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.signal.SignalVersionInfo;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.common.project.mapper.signal.AlarmModelMapper;
import com.interest.ids.common.project.mapper.signal.DevInfoMapper;
import com.interest.ids.common.project.mapper.signal.SignalVersionInfoMapper;
import com.interest.ids.dev.api.service.DevDeviceService;

import tk.mybatis.mapper.entity.Example;


/**
 * 
 * @author lhq
 *
 *
 */

@Service("devService")
public class DevDeviceServiceImpl implements DevDeviceService{
	
	
	@Resource
	private DevInfoMapper devMapper;
	
	@Resource
	private AlarmModelMapper alarmModelmapper;
	
	@Resource
	private SignalVersionInfoMapper signalVersionInfoMapper; // 信号点版本
	
	public List<DeviceInfo> getDevicesByParentSn(String parentSn){
		
		Example ex = new Example(DeviceInfo.class);
		Example.Criteria criteria = ex.createCriteria();
		criteria.andEqualTo("parentSn", parentSn);
		criteria.andEqualTo("isLogicDelete",0);
		return devMapper.selectByExample(ex);
	}

	@Override
	public DeviceInfo getDeviceBySn(String sn) {
		Example ex = new Example(DeviceInfo.class);
		Example.Criteria criteria = ex.createCriteria();
		criteria.andEqualTo("snCode", sn);
		//数据如缓存之前，判断设备是否是逻辑删除
		criteria.andEqualTo("isLogicDelete", 0);
		List<DeviceInfo> devs = devMapper.selectByExample(ex);
		if(devs != null && devs.size()>0){
			return devs.get(0);
		}
		return null;
	}

	@Override
	public DeviceInfo getDevByParentSnAndSecondAddr(String parentSn,
			Integer secondAddr) {
		
		Example ex = new Example(DeviceInfo.class);
		Example.Criteria criteria = ex.createCriteria();
		criteria.andEqualTo("parentSn", parentSn);
		criteria.andEqualTo("secondAddress", secondAddr);
		List<DeviceInfo> devs = devMapper.selectByExample(ex);
		if(devs != null && devs.size()>0){
			return devs.get(0);
		}
		return null;
	}

	@Override
	public void updateDevice(DeviceInfo device) {
		devMapper.updateByPrimaryKeySelective(device);
	}

	@Override
	public List<DeviceInfo> getDevicesByLinkedHostAndType(String linkedHost,Integer type) {
		
		Example ex = new Example(DeviceInfo.class);
		Example.Criteria criteria = ex.createCriteria();
		criteria.andEqualTo("devTypeId", type);
		List<DeviceInfo> devs = devMapper.selectByExample(ex);
		return devs;
	}

	@Override
	public List<DeviceInfo> getChildDevices(Long parentId) {
		
		Example ex = new Example(DeviceInfo.class);
		Example.Criteria criteria = ex.createCriteria();
		criteria.andEqualTo("parentId", parentId);
		List<DeviceInfo> devs = devMapper.selectByExample(ex);
		return devs;
	}

	@Override
	public List<DeviceInfo> getDeviceByStationAndType(String stationCode,
			Integer devType) {
		Example ex = new Example(DeviceInfo.class);
		Example.Criteria criteria = ex.createCriteria();
		criteria.andEqualTo("stationCode", stationCode);
		criteria.andEqualTo("devTypeId", devType);
		List<DeviceInfo> devs = devMapper.selectByExample(ex);
		return devs;
	}

	@Override
	public List<DeviceInfo> getDeviceByStationAndTypeAndBox(String stationCode,
			Integer devType, List<Long> boxes) {
		Example ex = new Example(DeviceInfo.class);
		Example.Criteria criteria = ex.createCriteria();
		criteria.andEqualTo("stationCode", stationCode);
		criteria.andEqualTo("devTypeId", devType);
		criteria.andNotIn("relatedDeviceId", boxes);
		List<DeviceInfo> devs = devMapper.selectByExample(ex);
		
		
		return devs;
	}
	
	public List<DeviceInfo> getDevsBySpecialType(String stationCode){
		Example ex = new Example(DeviceInfo.class);
		Example.Criteria criteria = ex.createCriteria();
		criteria.andEqualTo("stationCode",stationCode);
		List<Integer> list = new ArrayList<Integer>();
		list.add(DevTypeConstant.INVERTER_DEV_TYPE);
		list.add(DevTypeConstant.BOX_DEV_TYPE);
		list.add(DevTypeConstant.CENTER_INVERT_DEV_TYPE);
		list.add(DevTypeConstant.DCJS_DEV_TYPE);
		criteria.andIn("devTypeId",list );
		List<DeviceInfo> devs = devMapper.selectByExample(ex);
		return devs;
	}

	@Override
	public DeviceInfo getDeviceByID(Long id) {
		
		return devMapper.selectByPrimaryKey(id);
	}
	
	/**
	 * 根据模型点表的模型ID查询告警模型，主要针对遥信转告警
	 * @param modelId
	 * @return
	 */
	public AlarmModel getAlarmModel(Long modelId,String stationCode){
		Example ex = new Example(AlarmModel.class);
		Example.Criteria criteria = ex.createCriteria();
		criteria.andEqualTo("modelId", modelId);
		criteria.andEqualTo("stationCode", stationCode);
		List<AlarmModel> alarmModels = this.alarmModelmapper.selectByExample(ex);
		if(alarmModels != null && alarmModels.size() > 0){
			return alarmModels.get(0);
		}
		return null;
	}

	@Override
	public List<DeviceInfo> getDcDevices() {
		Example ex = new Example(DeviceInfo.class);
		Example.Criteria criteria = ex.createCriteria();
		Set<Integer> devTypes = new HashSet<>();
		devTypes.add(DevTypeConstant.MULTIPURPOSE_DEV_TYPE);
		devTypes.add(DevTypeConstant.TN_DAU);
//		devTypes.add(DevTypeConstant.COLLCUD);
		criteria.andIn("devTypeId", devTypes);
		criteria.andEqualTo("isMonitorDev", "0"); // 查询不是监控传递上来的通管机
		List<DeviceInfo> devs = devMapper.selectByExample(ex);
		return devs;
	}

	@Override
	public List<SignalVersionInfo> getVersionByProcode(String procode) {
		// 查询所有当前协议类型的版本信息
		SignalVersionInfo search = new SignalVersionInfo();
		search.setProtocolCode(procode);
		return signalVersionInfoMapper.select(search);
	}

	@Override
	public List<DeviceInfo> getByMqttVesionDev(String versionCode) {
		DeviceInfo search = new DeviceInfo();
		search.setSignalVersion(versionCode);
		search.setProtocolCode(DevTypeConstant.MQTT);
		search.setIsLogicDelete(false); // 没有被删除的设备
		search.setIsMonitorDev("0"); // 集维的设备 1：监控传递过来的设备
		return devMapper.select(search);
	}

	@Override
	public void insertDeviceList(List<DeviceInfo> devices) {
		devMapper.insertDevInfos(devices);
	}

	@Override
	public long insertAndGetId(DeviceInfo device) {
		return devMapper.insertAndGetId(device);
	}

}
