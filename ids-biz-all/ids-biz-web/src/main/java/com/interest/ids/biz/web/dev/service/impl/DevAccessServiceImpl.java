package com.interest.ids.biz.web.dev.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.interest.ids.biz.web.constant.ProtocolConstant;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.signal.SignalInfo;
import com.interest.ids.common.project.bean.signal.SignalModelInfo;
import com.interest.ids.common.project.bean.signal.SignalVersionInfo;
import com.interest.ids.common.project.bean.subarray.SubarrayInfoBean;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.common.project.mapper.signal.SignalInfoMapper;
import com.interest.ids.common.project.mapper.signal.SignalModelMapper;
import com.interest.ids.common.project.mapper.signal.SignalVersionInfoMapper;
import com.interest.ids.common.project.utils.RandomUtil;
import com.interest.ids.commoninterface.dao.device.DevAccessMapper;
import com.interest.ids.commoninterface.dao.station.SubarrayInfoMapper;
import com.interest.ids.commoninterface.service.device.IDevAccessService;

import tk.mybatis.mapper.entity.Example;


@Service("devAccessService")
public class DevAccessServiceImpl implements IDevAccessService {

	@Resource
	private DevAccessMapper devAccessMapper;
	
	@Autowired
	private SubarrayInfoMapper subarrayInfoMapper;
	
	@Autowired
	private SignalModelMapper signalModelMapper;
	
	@Autowired
	private SignalInfoMapper signalInfoMapper;
	
	@Autowired
	private SignalVersionInfoMapper signalVersionInfoMapper;


	@Override
	public void insertDevInfo(DeviceInfo devInfo) {
		// 0.设置设备的分区和子阵的id
		setDevSubAndAreaIds(devInfo);
		this.devAccessMapper.insertDevInfo(devInfo);
	}

	@Override
	public List<Map<String, Object>> getSignalVersionList(String protocolType) {
		List<Map<String, Object>> queryData = devAccessMapper.getSignalVersionList(protocolType);
		if(queryData != null && queryData.size() > 0){
			for(Map<String, Object> map : queryData){
				map.put("devTypeName", DevTypeConstant.DEV_TYPE_I18N_ID.get((int)map.get("devTypeId")));
			}
		}
		return queryData;
	}

	@Override
	public List<Map<String, Object>> getDevList(String protocolType, Integer devTypeId, String stationCode) {
		return this.devAccessMapper.getDevList(protocolType, devTypeId, stationCode);
	}

	@Override
	public int getCountBySnCode(String snCode) {
		Example snExample = new Example(DeviceInfo.class);
		snExample.createCriteria().andEqualTo("snCode", snCode);
		return this.devAccessMapper.selectCountByExample(snExample);
	}

	@Override
	public List<DeviceInfo> getDevInfoBySnCode(String snCode) {
		Example snExample = new Example(DeviceInfo.class);
		snExample.createCriteria().andEqualTo("snCode", snCode);
		return this.devAccessMapper.selectByExample(snExample);
	}

	@Override
	public List<String> getMqqtParentSnList(String stationCode) {
		DeviceInfo searchDev = new DeviceInfo();
		searchDev.setStationCode(stationCode);
		searchDev.setProtocolCode(ProtocolConstant.MQTT);
		List<DeviceInfo> list = devAccessMapper.select(searchDev);
		if(CollectionUtils.isEmpty(list)) {
			return Arrays.asList(); // 返回一个空的list
		}
		Set<String> snCodeSet = new HashSet<String>();
		for(DeviceInfo dev : list) {
			snCodeSet.add(dev.getParentSn());
		}
		return new ArrayList<String>(snCodeSet);
	}

	@Override
	public boolean valdateParentSnAndSecendAddress(String parentSn, Integer secendAddress) {
		DeviceInfo dev = new DeviceInfo();
		dev.setParentSn(parentSn);
		dev.setSecondAddress(secendAddress);
		dev.setIsLogicDelete(false);
		dev.setIsMonitorDev("0");
		List<DeviceInfo> list = devAccessMapper.select(dev);
		if(CollectionUtils.isEmpty(list)) { // 如果是空的集合,就证明不存在，就验证通过
			return true;
		}
		return false;
	}
	
	/**
	 * 新增设备信息
	 */
	@Override
	@Transactional // 控制事务的同时提交和回滚
	public void insertMqqtDev(DeviceInfo dev, Map<String, String> map) {
		// 0.设置设备的分区和子阵的id
		setDevSubAndAreaIds(dev);
		// 1.新增设备
		devAccessMapper.insert(dev); // 新增设备之后，设备就有id了
		// 获取设备信息
		DeviceInfo searchDev = new DeviceInfo();
		searchDev.setSignalVersion(dev.getSignalVersion());
		searchDev.setSecondAddress(dev.getSecondAddress());
		searchDev.setParentSn(dev.getParentSn());
		searchDev.setSnCode(dev.getSnCode());
		DeviceInfo hasIdDev = devAccessMapper.selectOne(searchDev); // 查询出来存在id的设备
		// 2.查询版本的信号点
		SignalModelInfo search = new SignalModelInfo();
		search.setSignalVersion(dev.getSignalVersion());
		// 获取版本信息
		SignalVersionInfo svi = new SignalVersionInfo();
		svi.setSignalVersion(dev.getSignalVersion());
		SignalVersionInfo version = signalVersionInfoMapper.selectOne(svi);
		if(version == null) {
			throw new RuntimeException(map.get("versionInformationNotExist"));//"版本信息不存在"
		}
		// 获取当前版本的信号点模板
		List<SignalModelInfo> signalModelList = signalModelMapper.select(search);
		if(CollectionUtils.isEmpty(signalModelList)) {
			throw new RuntimeException(map.get("devVersionNotHaveSignalPoints"));//"设备版本不存在任何的信号点"
		}
		// 3.新增设备的信号点,先一个一个的新增，后面考虑批量新增
		Date createDate = new Date();
		Long devId = hasIdDev.getId();
		dev.setId(devId);
		for(SignalModelInfo sm: signalModelList) {
			SignalInfo signalInfo = new SignalInfo();
			signalInfo.setBit(sm.getBit());
			signalInfo.setCreateDate(createDate);
			signalInfo.setDataType(sm.getDataType());
			signalInfo.setDeviceId(devId);
//			signalInfo.setDevName(dev.getDevName());
			signalInfo.setGain(sm.getGain());
//			signalInfo.setIsAlarmFlag(sm);
			signalInfo.setModelId(sm.getId()); // 版本对应的信号点id
			signalInfo.setOffset(sm.getOffset());
//			signalInfo.setProtocolCode(ProtocolConstant.MQTT);
			signalInfo.setRegisterNum(sm.getRegisterNum());
//			signalInfo.setRegisterType(sm.getR);
			signalInfo.setSignalAddress(sm.getSignalAddress());
			signalInfo.setSignalAlias(sm.getSignalAlias());
			signalInfo.setSignalGroup(sm.getSignalGroup());
			signalInfo.setSignalName(sm.getSignalName());
			signalInfo.setSignalType(sm.getSignalType());
			signalInfo.setSignalUnit(sm.getSignalUnit());
			signalInfo.setSignalVersion(sm.getSignalVersion());
			
			signalInfoMapper.insert(signalInfo);
		}
		// 4.判断账号是否已经存在，不存在的话，创建账号
		// 4.1.判断sn号是否存在
		int count = signalModelMapper.isExistMqqtUser(dev.getParentSn());
		// 4.2 如果不存在就新增
		if(count == 0) { // 没有查询到数据
			Map<String, Object> mqttUser = new HashMap<String, Object>();
			mqttUser.put("username",dev.getParentSn());
			mqttUser.put("password",RandomUtil.randomString(10)+dev.getParentSn().hashCode());
			mqttUser.put("is_superuser",0);
			mqttUser.put("clientid",dev.getParentSn());
			mqttUser.put("created", new Date());
			signalModelMapper.insertMqqtUser(mqttUser);
		}
	}

	/**
	 * 对于新增增的设备，需要查询电站下的分区和子阵
	 * 设置设备的分区和子阵的id
	 * @param dev 当前新增的设备信息
	 */
	private void setDevSubAndAreaIds(DeviceInfo dev) {
		// 根据电站code查询子阵和方阵id
		Example ex11 = new Example(SubarrayInfoBean.class);
		ex11.createCriteria().andEqualTo("stationCode", dev.getStationCode());
		List<SubarrayInfoBean> subarrayList = this.subarrayInfoMapper.selectByExample(ex11);
		Long subarrayId = null;
		Long phalanxId = null;
		if(subarrayList != null && subarrayList.size() > 0){
			subarrayId = subarrayList.get(0).getId();
			phalanxId = subarrayList.get(0).getPhalanxId();
		}
		dev.setPhalanxId(phalanxId);
		dev.setMatrixId(subarrayId);
	}

	@Override
	public void insertModbusDev(DeviceInfo dev) {
		// 0.设置设备的分区和子阵的id
		setDevSubAndAreaIds(dev);
		// 1.新增设备
		devAccessMapper.insert(dev); // 新增设备之后，设备就有id了
		// 获取设备信息
		DeviceInfo searchDev = new DeviceInfo();
		searchDev.setSignalVersion(dev.getSignalVersion());
		searchDev.setSecondAddress(dev.getSecondAddress());
		searchDev.setParentSn(dev.getParentSn());
//		searchDev.setSnCode(dev.getSnCode());
		DeviceInfo hasIdDev = devAccessMapper.selectOne(searchDev); // 查询出来存在id的设备
		// 2.查询版本的信号点
		SignalModelInfo search = new SignalModelInfo();
		search.setSignalVersion(dev.getSignalVersion());
		// 获取版本信息
		SignalVersionInfo svi = new SignalVersionInfo();
		svi.setSignalVersion(dev.getSignalVersion());
		SignalVersionInfo version = signalVersionInfoMapper.selectOne(svi);
		if(version == null) {
			throw new RuntimeException("版本信息不存在");//"版本信息不存在"
		}
		// 获取当前版本的信号点模板
		List<SignalModelInfo> signalModelList = signalModelMapper.select(search);
		if(CollectionUtils.isEmpty(signalModelList)) {
			throw new RuntimeException("设备版本不存在任何的信号点");//"设备版本不存在任何的信号点"
		}
		// 如果父设备没有StationCoe，就在第一个添加到他的子设备所在的电站设置为他的电站
		Example emp = new Example(DeviceInfo.class);
		emp.createCriteria().andEqualTo("snCode", dev.getParentSn()).andIsNull("stationCode").andIn("devTypeId", Arrays.asList(2, 3));
		List<DeviceInfo> parentDevs = this.devAccessMapper.selectByExample(emp);
		if (!CollectionUtils.isEmpty(parentDevs)) { // 如果存在，就更新父设备的电站为当前子设备的电站
			DeviceInfo d = parentDevs.get(0);
			d.setStationCode(dev.getStationCode());
			this.devAccessMapper.updateByPrimaryKey(d); // 更新数采的SN
		}
		// 3.新增设备的信号点,先一个一个的新增，后面考虑批量新增
		Date createDate = new Date();
		Long devId = hasIdDev.getId();
		dev.setId(devId);
		for(SignalModelInfo sm: signalModelList) {
			SignalInfo signalInfo = new SignalInfo();
			signalInfo.setBit(sm.getBit());
			signalInfo.setCreateDate(createDate);
			signalInfo.setDataType(sm.getDataType());
			signalInfo.setDeviceId(devId);
//					signalInfo.setDevName(dev.getDevName());
			signalInfo.setGain(sm.getGain());
//					signalInfo.setIsAlarmFlag(sm);
			signalInfo.setModelId(sm.getId()); // 版本对应的信号点id
			signalInfo.setOffset(sm.getOffset());
//					signalInfo.setProtocolCode(ProtocolConstant.MQTT);
			signalInfo.setRegisterNum(sm.getRegisterNum());
//					signalInfo.setRegisterType(sm.getR);
			signalInfo.setSignalAddress(sm.getSignalAddress());
			signalInfo.setSignalAlias(sm.getSignalAlias());
			signalInfo.setSignalGroup(sm.getSignalGroup());
			signalInfo.setSignalName(sm.getSignalName());
			signalInfo.setSignalType(sm.getSignalType());
			signalInfo.setSignalUnit(sm.getSignalUnit());
			signalInfo.setSignalVersion(sm.getSignalVersion());
			
			signalInfoMapper.insert(signalInfo);
		}
	}

	@Override
	public List<String> getModbusParentSnList(String stationCode) {
		// 协议类型需要什么??
		List<Integer> devTypes = Arrays.asList(2, 3);
		Example example = new Example(DeviceInfo.class);
		example.createCriteria().andEqualTo("stationCode", stationCode)
		.andIn("devTypeId", devTypes).andEqualTo("protocolCode", ProtocolConstant.MODBUS).andEqualTo("isMonitorDev", "0")
		.andEqualTo("isLogicDelete", false)
		.andIsNotNull("snCode"); // 数采和数据棒
		// 或者查询电站编号为空的数据
		example.or(example.createCriteria().andIsNull("stationCode").andIn("devTypeId", devTypes).
				andEqualTo("protocolCode", ProtocolConstant.MODBUS).andEqualTo("isMonitorDev", "0").andEqualTo("isLogicDelete", false).andIsNotNull("snCode"));
		List<DeviceInfo> list = this.devAccessMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(list)) {
			return new ArrayList<>();
		}
		List<String> result = new ArrayList<>();
		for(DeviceInfo d : list) {
			result.add(d.getSnCode());
		}
		return result;
	}
}
