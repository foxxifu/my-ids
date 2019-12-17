package com.interest.ids.dev.starter.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.PageHelper;
import com.interest.ids.common.project.bean.device.DevUpgradeDto;
import com.interest.ids.common.project.bean.device.DeviceDetail;
import com.interest.ids.common.project.bean.device.DeviceInfoDto;
import com.interest.ids.common.project.bean.device.DevicePvCapacity;
import com.interest.ids.common.project.bean.device.DevicePvModuleDto;
import com.interest.ids.common.project.bean.device.PvCapacityM;
import com.interest.ids.common.project.bean.device.StationDevicePvModule;
import com.interest.ids.common.project.bean.device.StationPvModule;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.signal.SignalInfo;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.common.project.constant.DeviceConstant.ConnectStatus;
import com.interest.ids.common.project.constant.StationInfoConstant;
import com.interest.ids.common.project.dto.DevUpgradeSearchParams;
import com.interest.ids.common.project.mapper.signal.DevInfoMapper;
import com.interest.ids.common.project.mapper.signal.SignalInfoMapper;
import com.interest.ids.common.project.mapper.signal.SignalModelMapper;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.commoninterface.dao.device.DevPvCapacityMMapper;
import com.interest.ids.commoninterface.dao.station.StationDevMapper;
import com.interest.ids.commoninterface.dao.station.StationInfoMMapper;
import com.interest.ids.dev.api.localcache.DeviceLocalCache;
import com.interest.ids.dev.starter.dto.ScDevBindParams;
import com.interest.ids.dev.starter.service.DeviceService;
import com.interest.ids.redis.caches.DeviceCache;
import com.interest.ids.redis.client.service.ConnStatusCacheClient;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * @Author: sunbjx
 * @Description:
 * @Date Created in 10:52 2017/12/19
 * @Modified By:
 */
@Service
public class DeviceServiceImpl implements DeviceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceServiceImpl.class);

    @Autowired
    private DevInfoMapper devInfoMapper;
    @Resource
    private DevPvCapacityMMapper devPvCapacityMMapper;
    @Resource
    private StationDevMapper stationDevMapper;
    @Resource
    private StationInfoMMapper stationInfoMMapper;
    @Resource
    private ConnStatusCacheClient connCacheClient;
    @Resource
    private SignalModelMapper signalModelMapper;
    @Resource
    private SignalInfoMapper signalInfoMapper;
    
    @Override
    public List<DeviceInfo> getByDeviceTypeId(Integer deviceTypeId) {
        List<DeviceInfo> deviceInfoList = new ArrayList<DeviceInfo>();
        Example example = new Example(DeviceInfo.class);
        example.createCriteria().andEqualTo("devTypeId", deviceTypeId);

        try {
            deviceInfoList = devInfoMapper.selectByExample(example);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return deviceInfoList;
    }

    @Override
    public int update(Long id, String ip, Integer port) {

        DeviceInfo deviceInfo = devInfoMapper.selectByPrimaryKey(id);
        if (deviceInfo != null) {
            deviceInfo.setDevIp(ip);
            deviceInfo.setDevPort(port);
        }

        try {
            devInfoMapper.updateByPrimaryKey(deviceInfo);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            return 0;
        }

        return 1;
    }

    @Override
    public List<DeviceInfo> getByEsn(String parentEsnCode) {
        List<DeviceInfo> deviceInfoList = new ArrayList<DeviceInfo>();

        Example example = new Example(DeviceInfo.class);
        example.createCriteria().andEqualTo("parentSn", parentEsnCode);

        try {
            deviceInfoList = devInfoMapper.selectByExample(example);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }

        return deviceInfoList;
    }

    @Override
    public Integer selectAllCount(DeviceInfoDto dto) {
        return devInfoMapper.selectAllCount(dto);
    }

    @Override
    public List<DeviceInfoDto> selectDeviceByCondtion(DeviceInfoDto dto) {
    	PageHelper.startPage(dto.getIndex(), dto.getPageSize()); // 启用分页助手
        List<DeviceInfoDto> queryResult = devInfoMapper.selectDeviceByCondtion(dto);
        if (null != queryResult) {
            
            for (DeviceInfoDto deviceInfoDto : queryResult) {
                deviceInfoDto.setDevTypeName(DevTypeConstant.DEV_TYPE_I18N_ID
                                .get(deviceInfoDto.getDevTypeId()));// 设置设备的名称
                String longtude = deviceInfoDto.getLongitude() != null ? (deviceInfoDto.getLongitude() + "") : "-";
                String latitude = deviceInfoDto.getLatitude() != null ? (deviceInfoDto.getLatitude() + "") : "-";
                deviceInfoDto.setLaLongtude(longtude + " " + latitude);
            }
            
            // 设置设备状态
            setDevsStatus(queryResult);
        }
        
        return queryResult;
    }

    @Override
    public DeviceInfoDto selectDeviceById(Long id) {
    	DeviceInfoDto result = devInfoMapper.selectDeviceById(id);
    	String parentSn = result.getParentEsnCode();
		if(null != parentSn){
			String mqttUserPassword = signalModelMapper.getMqttUserPassword(parentSn);
			result.setMqttPassword(mqttUserPassword);
			return result;
		}
        return result;
    }

    @Override
    public int updateDeviceById(DeviceInfo deviecInfo) {
        return devInfoMapper.updateDeviceById(deviecInfo);
    }

    @Override
    public List<DeviceInfo> selectDeviceByIds(String[] ids) {
        List<DeviceInfo> list = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            DeviceInfo dev = DeviceCache.getDevById(Long.parseLong(ids[i]));
            if (null != dev) {
                list.add(dev);
            } else {
                DeviceInfoDto dto = selectDeviceById(Long.parseLong(ids[i]));
                dev = new DeviceInfo();
                if (null != dto) {
                    BeanUtils.copyProperties(dto, dev);
                    dev.setDevTypeId(dto.getDevTypeId());
                    dev.setStationCode(dto.getStationCode());
                    dev.setEnterpriseId(dto.getEnterpriseId());
                    list.add(dev);
                }
            }
        }
        return list;
    }

    @Override
    public int insertCapacity(List<DevicePvCapacity> insertCapacity) {
        int result = -1;
        for (int i = 0; i < insertCapacity.size(); i++) {
            result = devPvCapacityMMapper.insertinsertCapacity(insertCapacity.get(i));
        }
        return result;
    }

    @Override
    public List<PvCapacityM> getPvCapacityMByDeviceId(long deviceId) {
        return devPvCapacityMMapper.selectPvCapacityMByDeviceId(deviceId);
    }

    @Override
    public int updateCapacity(List<DevicePvCapacity> insertCapacity) {
        int result = -1;
        for (int i = 0; i < insertCapacity.size(); i++) {
            PvCapacityM cap = new PvCapacityM();
            cap.setId(insertCapacity.get(i).getId());
            cap.setNum(insertCapacity.get(i).getNum());
            cap.setPv1(insertCapacity.get(i).getPvs().get(0) != null ? insertCapacity.get(i).getPvs().get(0)
                    .longValue() : null);
            cap.setPv2(insertCapacity.get(i).getPvs().get(1) != null ? insertCapacity.get(i).getPvs().get(1)
                    .longValue() : null);
            cap.setPv3(insertCapacity.get(i).getPvs().get(2) != null ? insertCapacity.get(i).getPvs().get(2)
                    .longValue() : null);
            cap.setPv4(insertCapacity.get(i).getPvs().get(3) != null ? insertCapacity.get(i).getPvs().get(3)
                    .longValue() : null);
            cap.setPv5(insertCapacity.get(i).getPvs().get(4) != null ? insertCapacity.get(i).getPvs().get(4)
                    .longValue() : null);
            cap.setPv6(insertCapacity.get(i).getPvs().get(5) != null ? insertCapacity.get(i).getPvs().get(5)
                    .longValue() : null);
            cap.setPv7(insertCapacity.get(i).getPvs().get(6) != null ? insertCapacity.get(i).getPvs().get(6)
                    .longValue() : null);
            cap.setPv8(insertCapacity.get(i).getPvs().get(7) != null ? insertCapacity.get(i).getPvs().get(7)
                    .longValue() : null);
            cap.setPv9(insertCapacity.get(i).getPvs().get(8) != null ? insertCapacity.get(i).getPvs().get(8)
                    .longValue() : null);
            cap.setPv10(insertCapacity.get(i).getPvs().get(9) != null ? insertCapacity.get(i).getPvs().get(9)
                    .longValue() : null);
            cap.setPv11(insertCapacity.get(i).getPvs().get(10) != null ? insertCapacity.get(i).getPvs().get(10)
                    .longValue() : null);
            cap.setPv12(insertCapacity.get(i).getPvs().get(11) != null ? insertCapacity.get(i).getPvs().get(11)
                    .longValue() : null);
            cap.setPv13(insertCapacity.get(i).getPvs().get(12) != null ? insertCapacity.get(i).getPvs().get(12)
                    .longValue() : null);
            cap.setPv14(insertCapacity.get(i).getPvs().get(13) != null ? insertCapacity.get(i).getPvs().get(13)
                    .longValue() : null);
            cap.setPv15(insertCapacity.get(i).getPvs().get(14) != null ? insertCapacity.get(i).getPvs().get(14)
                    .longValue() : null);
            cap.setPv16(insertCapacity.get(i).getPvs().get(15) != null ? insertCapacity.get(i).getPvs().get(15)
                    .longValue() : null);
            cap.setPv17(insertCapacity.get(i).getPvs().get(16) != null ? insertCapacity.get(i).getPvs().get(16)
                    .longValue() : null);
            cap.setPv18(insertCapacity.get(i).getPvs().get(17) != null ? insertCapacity.get(i).getPvs().get(17)
                    .longValue() : null);
            cap.setPv19(insertCapacity.get(i).getPvs().get(18) != null ? insertCapacity.get(i).getPvs().get(18)
                    .longValue() : null);
            cap.setPv20(insertCapacity.get(i).getPvs().get(19) != null ? insertCapacity.get(i).getPvs().get(19)
                    .longValue() : null);
            cap.setPv21(insertCapacity.get(i).getPvs().get(20) != null ? insertCapacity.get(i).getPvs().get(20)
            		.longValue() : null);
            cap.setPv22(insertCapacity.get(i).getPvs().get(21) != null ? insertCapacity.get(i).getPvs().get(21)
            		.longValue() : null);
            cap.setPv23(insertCapacity.get(i).getPvs().get(22) != null ? insertCapacity.get(i).getPvs().get(22)
            		.longValue() : null);
            cap.setPv24(insertCapacity.get(i).getPvs().get(23) != null ? insertCapacity.get(i).getPvs().get(23)
            		.longValue() : null);
            result = devPvCapacityMMapper.updateByPrimaryKeySelective(cap);
        }
        return result;
    }

    @Override
    public Map<String, List<StationPvModule>> selectStationPvModule() {
        List<StationPvModule> list = stationDevMapper.selectStationPvModel();
        Map<String, List<StationPvModule>> map = new HashMap<>();
        for (StationPvModule md : list) {
            String name = md.getManufacturer();
            List<StationPvModule> temp = map.get(name);
            if (temp == null) {
                temp = new ArrayList<>();
                map.put(name, temp);
            }
            temp.add(md);
        }
        return map;
    }

    @Override
    public StationPvModule selectStationPvModuleDetail(Long id) {
        return stationDevMapper.selectStationPvModuleDetail(id);
    }

    @Override
    public int saveStationPvModule(List<DeviceInfo> list, List<StationDevicePvModule> modules, Integer num) {
        DeviceInfo device = null;
        StationDevicePvModule module = null;
        List<StationDevicePvModule> moduleList = null;
        int result = -1;
        List<Double> pvs = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            device = list.get(i);
            moduleList = new ArrayList<>();
            // 删除原来的组串容量详情配置
            stationDevMapper.deleteStationPvModule(device.getId());
            stationDevMapper.deleteStationPvCapacity(device.getId());
            // 添加组串详情配置
            for (int j = 0; j < modules.size(); j++) {
                module = new StationDevicePvModule();
                module.setCreateTime(System.currentTimeMillis());
                module.setDevId(device.getId());
                module.setSnCode(device.getSnCode());
                module.setFixedPower(modules.get(j).getFixedPower());
                module.setIsDefault("0");// 是否预置
                module.setModuleProductionDate(System.currentTimeMillis());// 投产日期
                module.setModulesNumPerString(modules.get(j).getModulesNumPerString());
                module.setPvIndex(j + 1);
                module.setPvModuleId(modules.get(j).getPvModuleId());
                module.setStationCode(device.getStationCode());
                module.setUpdateTime(System.currentTimeMillis());
                module.setCreateTime(System.currentTimeMillis());
                moduleList.add(module);
                pvs.add(modules.get(j).getFixedPower().doubleValue());
            }
            for (int j = 0; j < moduleList.size(); j++) {
                result = stationDevMapper.insertStationPvModule(moduleList.get(j));

            }
            /* result = stationDevMapper.insertStationPvModule(moduleList); */
            // 同步更新ids_pv_capacity_t
            while (pvs.size() < 24) {
                pvs.add(null);
            }
            DevicePvCapacity cap = new DevicePvCapacity();
            cap.setDeviceId(device.getId());
            List<Double> devicePvs = new ArrayList<>();
            devicePvs.add(pvs.get(0));
            devicePvs.add(pvs.get(1));
            devicePvs.add(pvs.get(2));
            devicePvs.add(pvs.get(3));
            devicePvs.add(pvs.get(4));
            devicePvs.add(pvs.get(5));
            devicePvs.add(pvs.get(6));
            devicePvs.add(pvs.get(7));
            devicePvs.add(pvs.get(8));
            devicePvs.add(pvs.get(9));
            devicePvs.add(pvs.get(10));
            devicePvs.add(pvs.get(11));
            devicePvs.add(pvs.get(12));
            devicePvs.add(pvs.get(13));
            devicePvs.add(pvs.get(14));
            devicePvs.add(pvs.get(15));
            devicePvs.add(pvs.get(16));
            devicePvs.add(pvs.get(17));
            devicePvs.add(pvs.get(18));
            devicePvs.add(pvs.get(19));
            devicePvs.add(pvs.get(20));
            devicePvs.add(pvs.get(21));
            devicePvs.add(pvs.get(22));
            devicePvs.add(pvs.get(23));
            cap.setPvs(devicePvs);
            cap.setDevName(device.getDevAlias());
            cap.setDevTypeId(device.getDevTypeId());
            cap.setNum(num);
            cap.setStationCode(device.getStationCode());
            cap.setEnterpriseId(device.getEnterpriseId());
            devPvCapacityMMapper.insertinsertCapacity(cap);
        }
        return result;
    }

    @Override
    public DeviceDetail selectDeviceDetail(Long id) {
        DeviceInfo device = devInfoMapper.selectByPrimaryKey(id);
        DeviceDetail deviceDetail = new DeviceDetail();
        if (null != device) {
            deviceDetail.setId(device.getId());
            deviceDetail.setDevAlias(device.getDevName());
            deviceDetail.setDeviceTypeId(device.getDevTypeId());
            deviceDetail.setDevIp(device.getDevIp());
            deviceDetail.setSnCode(device.getSnCode());
            deviceDetail.setLongitude(device.getLongitude() != null ? device.getLongitude().doubleValue() : null);
            deviceDetail.setLatitude(device.getLatitude() != null ? device.getLatitude().doubleValue() : null);
            deviceDetail.setStationCode(device.getStationCode());

            // 查询电站的名称
            StationInfoM station = stationInfoMMapper.selectStationInfoMByStationCode(device.getStationCode());
            if (null != station) {
                // 设置设备的地址
                deviceDetail.setDevAddr(station.getStationName() + device.getDevName());
            }

            // 设备的厂家名称
            String venderName = devInfoMapper.selectVenderName(device.getSignalVersion());
            deviceDetail.setVenderName(venderName);
            // 配置组件相关的信息
            List<DevicePvModuleDto> pvInfos = devInfoMapper.selectDevicePvInfos(id);
            deviceDetail.setPvInfo(pvInfos);
            deviceDetail.setPvNum(pvInfos == null ? 0 : pvInfos.size());

            // 设备状态
            setDevStatus(deviceDetail);
        }
        return deviceDetail;
    }

    /**
     * 设置设备状态
     * 
     * @param deviceDetail
     */
    private void setDevStatus(DeviceDetail deviceDetail) {
        Long id = deviceDetail.getId();
        // 设备状态
        List<Long> devWithAlarm = devInfoMapper.countCritAlarmsByDeviceIds(CommonUtil.createListWithElements(id));

        ConnectStatus connStatus = connCacheClient.get(id);
        if (ConnectStatus.DISCONNECTED.equals(connStatus)) {
            deviceDetail.setDevStatus(StationInfoConstant.DISCONECTED);
        } else {
            if (devWithAlarm != null && devWithAlarm.contains(id)) {
                deviceDetail.setDevStatus(StationInfoConstant.TROUBLE);
            } else {
                deviceDetail.setDevStatus(StationInfoConstant.HEALTHY);
            }
        }
    }
    
    /**
     * 设置设备的状态
     */
    private void setDevsStatus(List<DeviceInfoDto> devDtoList){
        if (devDtoList != null && devDtoList.size() > 0) {
            List<Long> deviceIdList = new ArrayList<>();
            // List<String> stationCodes = new ArrayList<>();
            for (DeviceInfoDto dto : devDtoList) {
                deviceIdList.add(dto.getId());
//                if (!stationCodes.contains(dto.getStationCode())) {
//                    stationCodes.add(dto.getStationCode());
//                }
            }
            
            // Map<String, String> stationState = StationCache.getStationHealthState(stationCodes);
            
            List<Long> deviceAlarmCount = devInfoMapper.countCritAlarmsByDeviceIds(deviceIdList);
            
            for (DeviceInfoDto dto : devDtoList) {
                ConnectStatus connStatus = connCacheClient.get(dto.getId());
                // String state = stationState.get(dto.getStationCode());
                if (connStatus == null || ConnectStatus.DISCONNECTED.equals(connStatus)
                		 // ||String.valueOf(StationInfoConstant.DISCONECTED).equals(state)
                        ) {
                    dto.setDevStatus(StationInfoConstant.DISCONECTED);
                }else{
                    if (deviceAlarmCount.contains(dto.getId())) {
                        dto.setDevStatus(StationInfoConstant.TROUBLE);
                    }else {
                        dto.setDevStatus(StationInfoConstant.HEALTHY);
                    }
                }
            }
        }
    }

	@Override
	public List<DeviceInfoDto> getDCJSIdAndName(Long enterpriseId)
	{
		return devInfoMapper.getDCJSIdAndName(enterpriseId);
	}

	@Override
	public List<Map<String,Object>> getDCJSDetail(String id) {
		return devInfoMapper.getDCJSDetail(id);
	}

	@Override
	public Integer selectAllDCJSCount(DeviceInfoDto dto) {
		return devInfoMapper.selectAllDCJSCount(dto);
	}

	@Override
	public List<DeviceInfoDto> selectDCJSByCondtion(DeviceInfoDto dto) {
		dto.setDevTypeId(15);
		return devInfoMapper.selectDCJSByCondtion(dto);
	}
	
	public void deleteCenterVerDetailByCenterId(Long id) {
		// 1.考虑已经保存了的要做删除的操作
		devInfoMapper.deleteCenterVerDetailByCenterId(id);
	}

	@Override
	public void insertCenterVerDetail(DeviceInfoDto dto) {
		Map<String,Object> map = new HashMap<>();
		map.put("centerVertId", dto.getId());
		map.put("dcjsDevIds", dto.getIds().split(","));
		devInfoMapper.insertCenterVerDetail(map);
	}

	@Override
	public Map<String, Object> getDCJSByShip(Long id) {
		Long[] ids = devInfoMapper.getDCJSByShip(id);
		Map<String, Object> map = new HashMap<>();
		if(null != ids && ids.length > 0 )
		{
			StringBuffer sb = new StringBuffer();
			for (Long _id : ids) {
				sb.append(_id+"").append(",");
			}
			if(sb.toString().endsWith(",")) {
				map.put("ids", sb.toString().substring(0, sb.toString().length()-1));
			}else {
				map.put("ids", sb.toString());
			}
		}
		return map;
	}

	@Override
	public List<DeviceInfoDto> getBindedById(Long id) {
		return devInfoMapper.getBindedById(id);
	}
	
	@Override
	public Integer countDCJS(String ids) {
		return devInfoMapper.dCJSCount(ids);
	}

	@Override
	public List<Long> getDevIdsByVersionId(List<Long> versionIdList) {
		return devInfoMapper.getDevIdsByVersionId(versionIdList);
	}

	@Override
	public DeviceInfo getTGJDevByVersionId (Long vesionId) {
        List<DeviceInfo> list = devInfoMapper.getTGJByVersionId(vesionId);
        if (list == null || list.isEmpty()) {
        	return null;
        }
		return list.get(0);
	}
	@Override
	public List<DevUpgradeDto> getDevUpgradeListByCondition(
			Map<String, Object> params) {
		int index = (int) params.get("index");
		int pageSize = (int) params.get("pageSize");
		PageHelper.startPage(index, pageSize);// 启用分页助手
		List<DevUpgradeDto> result = devInfoMapper
				.selectUpgradeDevByCoudition(params);
		if (null != result) {
			for (DevUpgradeDto tempResult : result) {
				tempResult.setDevTypeName(DevTypeConstant.DEV_TYPE_I18N_ID
						.get(tempResult.getDevTypeId()));// 设置设备的名称
			}
		}
		return result;
	}
	@Override
	public int updateMqttDevPassword(String username, String password) {
		if(null == username){
			throw new RuntimeException("mqtt username is null .....");
		}
		int count = signalModelMapper.updateMqttPWByUsername(username, password);
		return count;
	}

	@Override
	public List<DeviceInfoDto> getChildDevListByParentDevId(Long parentDevId) {
		
		return devInfoMapper.getChildDevList(parentDevId);
	}

	@Override
	public DeviceInfo getDeviceInfoById(Long devId) {
		DeviceInfo deviceInfo = devInfoMapper.selectByPrimaryKey(devId);
		return deviceInfo;
	}

	@Override
	public List<DeviceInfo> findAllModbusUnbindScDevs() {
		Example example = new Example(DeviceInfo.class);
		example.createCriteria()
		.andEqualTo("devTypeId", DevTypeConstant.DC_DEV_TYPE)
		.andEqualTo("protocolCode", DevTypeConstant.MODBUS)
		.andEqualTo("isLogicDelete", 0)
		.andEqualTo("isMonitorDev", false)
		.andIsNull("stationCode");
		return devInfoMapper.selectByExample(example);
	}

	@Transactional
	@Override
	public void bindScDevToStation(ScDevBindParams params) {
		Example example = new Example(DeviceInfo.class);
		example.createCriteria()
		.andEqualTo("parentId", params.getScDevId()) // 数采下挂设备
		.orEqualTo("id", params.getScDevId());// 数采设备
		// 1.修改设备列表
		// 1.1 更新数采
		// 1.2 更新数采下挂设备
		DeviceInfo updateInfo = new DeviceInfo();
		updateInfo.setStationCode(params.getStationCode());
		devInfoMapper.updateByExampleSelective(updateInfo, example);
		
		// 2.更新设备redis缓存
		// 查询所有的设备
		List<DeviceInfo> devList = devInfoMapper.selectByExample(example);
		if (!CollectionUtils.isEmpty(devList)) {
			DeviceCache.putDevs(devList);
		}
	}

	@Override
	public List<DeviceInfo> findAllModbusBindScDevs() {
		Example example = new Example(DeviceInfo.class);
		example.createCriteria()
		.andEqualTo("devTypeId", DevTypeConstant.DC_DEV_TYPE)
		.andEqualTo("protocolCode", DevTypeConstant.MODBUS)
		.andEqualTo("isLogicDelete", false)
		.andEqualTo("isMonitorDev", false)
		.andIsNotNull("stationCode");
		return devInfoMapper.selectByExample(example);
	}

	@Override
	public List<DeviceInfo> findDevsFromDbByIds(String[] devIds) {
		List<String> ids = new ArrayList<>();
		for(String id : devIds) {
			ids.add(id);
		}
		Example example = new Example(DeviceInfo.class);
		example.createCriteria()
			.andIn("id", ids);
		return devInfoMapper.selectByExample(example);
	}

	@Override
	public int updateDevsUpgradeByDevList(String[] devIds, String upgradePath) {
		List<String> ids = new ArrayList<>();
		for(String id : devIds) {
			ids.add(id);
		}
		Example example = new Example(DeviceInfo.class);
		example.createCriteria()
			.andIn("id", ids);
		DeviceInfo dev = new DeviceInfo();
		dev.setUpgradeFileName(upgradePath);
		dev.setUpgradeStatus(1);
		dev.setUpgradeProcess(0);
		
		return devInfoMapper.updateByExampleSelective(dev, example);
	}

	@Override
	public List<DeviceInfo> findUpgradeDevInfos(DevUpgradeSearchParams devUpgradeSearchParams) {
		Example example = new Example(DeviceInfo.class);
		Criteria criteria = example.createCriteria()
				.andEqualTo("protocolCode", DevTypeConstant.MODBUS)
				.andEqualTo("isLogicDelete",0);
		if (StringUtils.isNotBlank(devUpgradeSearchParams.getDevName())) {
			criteria.andLike("devAlias", "%%" + devUpgradeSearchParams.getDevName() + "%%");
		}
		if (devUpgradeSearchParams.getDevTypeId() != null) {
			criteria.andEqualTo("devTypeId", devUpgradeSearchParams.getDevTypeId());
		}
		if (devUpgradeSearchParams.getScDevId() != null) {
			criteria.andEqualTo("parentId", devUpgradeSearchParams.getScDevId());
		}
		if (devUpgradeSearchParams.getUpgradeStatus() != null) {
			criteria.andEqualTo("upgradeStatus", devUpgradeSearchParams.getUpgradeStatus());
		}
		return devInfoMapper.selectByExample(example);
	}

	@Override
	public void deleteModbusDevInSns(List<String> devSns) {
		if(devSns.size() > 0){
			Example example = new Example(DeviceInfo.class);
			example.createCriteria()
			.andIn("snCode", devSns)
			.andEqualTo("isLogicDelete", 1);
			// 根据sn码查询出已经逻辑删除的设备，目的是获取id,删除数据库中的信号点
			List<DeviceInfo> modbusDev = devInfoMapper.selectByExample(example);
			for(DeviceInfo dev : modbusDev){
				SignalInfo signalInfo = new SignalInfo();
				signalInfo.setDeviceId(dev.getId());
				signalInfoMapper.delete(signalInfo);
			}
		}
	}
}
