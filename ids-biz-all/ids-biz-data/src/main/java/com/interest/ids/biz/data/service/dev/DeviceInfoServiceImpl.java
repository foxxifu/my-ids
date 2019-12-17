package com.interest.ids.biz.data.service.dev;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.interest.ids.common.project.bean.alarm.DeviceAlamDto;
import com.interest.ids.common.project.bean.device.DeviceDetail;
import com.interest.ids.common.project.bean.device.DeviceInfoDto;
import com.interest.ids.common.project.bean.device.DevicePvModuleDto;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.common.project.constant.DeviceConstant.ConnectStatus;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.common.project.constant.StationInfoConstant;
import com.interest.ids.common.project.dto.SearchDeviceDto;
import com.interest.ids.common.project.mapper.signal.DevInfoMapper;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.common.project.utils.MathUtil;
import com.interest.ids.commoninterface.dao.station.StationInfoMMapper;
import com.interest.ids.commoninterface.service.device.IDeviceInfoService;
import com.interest.ids.redis.caches.DeviceCache;
import com.interest.ids.redis.client.service.ConnStatusCacheClient;
import com.interest.ids.redis.client.service.SignalCacheClient;
import com.interest.ids.redis.client.service.UnbindDeviceClient;

@Component("deviceInfoService")
public class DeviceInfoServiceImpl implements IDeviceInfoService {

    @Autowired
    private DevInfoMapper devInfoMapper;
    @Autowired
    UnbindDeviceClient unbindDeviceClient;
    @Resource
    private StationInfoMMapper stationInfoMMapper;
    @Resource
    private ConnStatusCacheClient connCacheClient;
    @Resource
    private SignalCacheClient signalClient;

    @Override
    public List<DeviceInfo> queryDevicesByStationCodes(Collection<String> stationCodes, Collection<Integer> deviceTypes) {
        List<DeviceInfo> result = new ArrayList<>();

        if (CommonUtil.isNotEmpty(stationCodes) && CommonUtil.isNotEmpty(deviceTypes)) {
            result = devInfoMapper.selectDeviceBySidsAndDeviceTypeIds(stationCodes, deviceTypes);
        }
        return result;
    }

    @Override
    public List<DeviceInfo> queryDevicesBySIdDId(List<Long> devcieIds, Set<String> stationCodes) {
        List<DeviceInfo> result = new ArrayList<>();

        if (CommonUtil.isNotEmpty(stationCodes) && CommonUtil.isNotEmpty(devcieIds)) {
            result = devInfoMapper.selectDeviceBySidsAndDeviceIds(stationCodes, devcieIds);
        }
        return result;
    }

    @Override
    public List<DeviceInfo> queryDevicesByIds(Collection<Long> deviceIds) {
        List<DeviceInfo> result = new ArrayList<>();

        if (CommonUtil.isNotEmpty(deviceIds)) {
            result = devInfoMapper.selectDeviceByDeviceIds(deviceIds);
        }
        return result;
    }

    /**
     * 返回stationCode, deviceId map结果
     * 
     * @param stationCodes
     * @param deviceTypeId
     * @return
     */
    @Override
    public Map<String, List<Long>> queryStationDevicesMap(Collection<String> stationCodes, Integer deviceTypeId) {
        Map<String, List<Long>> result = new HashMap<>();

        if (CommonUtil.isNotEmpty(stationCodes) && deviceTypeId != null) {
            List<Map<String, Object>> queryResult = devInfoMapper.selectDevicesByStaionCodeAndDeviceType(stationCodes,
                    deviceTypeId);
            for (Map<String, Object> ele : queryResult) {
                String stationCode = MathUtil.formatString(ele.get("station_code"));
                List<Long> devices = result.get(stationCode);
                if (devices == null) {
                    devices = new ArrayList<>();
                }

                devices.add(MathUtil.formatLong(ele.get("device_id")));
                result.put(stationCode, devices);
            }
        }

        return result;
    }

    @Override
    public Map<String, List<Long>> queryAllDevicesMap(Collection<String> stationCodes) {
        Map<String, List<Long>> result = new HashMap<>();

        List<DeviceInfo> queryResult = devInfoMapper.selectDevicesWithStationCodes(stationCodes);

        if (queryResult != null) {
            for (DeviceInfo deviceInfo : queryResult) {
                String stationCode = MathUtil.formatString(deviceInfo.getStationCode());
                List<Long> devices = result.get(stationCode);
                if (devices == null) {
                    devices = new ArrayList<>();
                }

                devices.add(deviceInfo.getId());
                result.put(stationCode, devices);
            }
        }

        return result;
    }

    @Override
    public List<DeviceInfo> queryAllDeviceInfoMs() {

        return devInfoMapper.selectAllDeviceInfoMs();
    }

    public void initDeviceInfoCach() {
        // 获取所有的设备
        List<DeviceInfo> allDevices = queryAllDeviceInfoMs();

        if (CommonUtil.isNotEmpty(allDevices)) {
            // 清空设备缓存
            DeviceCache.removeAll();

            // 放入缓存
            DeviceCache.putDevs(allDevices);
        }
    }

    @Override
    public Map<String, Object> getDeviceInfoBySN(String snCode) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        SearchDeviceDto devInfo = unbindDeviceClient.get(snCode);// 从redis内存中通过设备SN号获取设备信息
        if (devInfo != null) {// 如果缓存中存在此设备sn的设备信息，则需要再判断此设备信息是否已经入库
            DeviceInfo deviceInfoInDB = this.devInfoMapper.getDeviceInfoBySN(snCode);
            if (deviceInfoInDB != null) {
                returnMap.put("code", ResponseConstants.CODE_ERROR_INUSERDEVICE);
                returnMap.put("message", ResponseConstants.CODE_ERROR_INUSERDEVICE_VALUE);
                returnMap.put("deviceInfo", deviceInfoInDB);
            } else {
                returnMap.put("code", ResponseConstants.CODE_SUCCESS);
                returnMap.put("message", ResponseConstants.CODE_SUCCESS_VALUE);
                returnMap.put("deviceInfo", devInfo);
            }
        } else {// 不存在设备
            returnMap.put("code", ResponseConstants.CODE_ERROR_NODEVICE);
            returnMap.put("message", ResponseConstants.CODE_ERROR_NODEVICE_VALUE);
            returnMap.put("deviceInfo", null);
        }
        return returnMap;
    }

    @Override
    public List<Map<String, Object>> getModelVersionCodeList() {
        return devInfoMapper.getModuleVersionCodeList();
    }

    @Override
    @Transactional
    public void insertDevInfos(List<DeviceInfo> devList) {
        this.devInfoMapper.insertDevInfos(devList);
    }

    @Override
    public List<DeviceInfo> getDevicesByStationCode(String stationCode) {
        return this.devInfoMapper.getDevicesByStationCode(stationCode);
    }

    @Override
    @Transactional
    public void updateDevInfos(DeviceInfo deviceInfo) {
        this.devInfoMapper.updateDevInfos(deviceInfo);
    }

    @Override
    @Transactional
    public void deleteDeviceInfos(Object[] devIds) {
        this.devInfoMapper.deleteDeviceInfos(devIds);
    }

    @Override
    public List<Map<Integer, Long>> countDeviceByStationCode(String stationCode) {
        return devInfoMapper.countDeviceByStationCode(stationCode);
    }

    @Override
    public List<DeviceInfoDto> getDeviceVenders(String stationCode) {
        return devInfoMapper.getDeviceVenders(stationCode);
    }

    @Override
    public Long countDCJS(String stationCode) {
        return devInfoMapper.countDCJS(stationCode);
    }

    @Override
    public Integer selectAllCount(DeviceInfoDto dto) {
        return devInfoMapper.selectAllCount(dto);
    }

    @Override
    public List<DeviceInfoDto> selectDeviceByCondtion(DeviceInfoDto dto) {
    	PageHelper.startPage(dto.getIndex(), dto.getPageSize()); // 使用分页助手
        List<DeviceInfoDto> queryResult = devInfoMapper.selectDeviceByCondtion(dto);
        
        if (null != queryResult) {
            for (DeviceInfoDto deviceInfoDto : queryResult) {
                deviceInfoDto.setDevTypeName(DevTypeConstant.DEV_TYPE_I18N_ID.get(deviceInfoDto.getDevTypeId()));// 设置设备的名称
               
                String longtude = deviceInfoDto.getLongitude() != null ? (deviceInfoDto.getLongitude() + "") : "-";
                String latitude = deviceInfoDto.getLatitude() != null ? (deviceInfoDto.getLatitude() + "") : "-";
                deviceInfoDto.setLaLongtude(longtude + " " + latitude);
                
                Double signalValue = null;
                if (DevTypeConstant.INVERTER_DEV_TYPE == deviceInfoDto.getDevTypeId() || 
                        DevTypeConstant.CENTER_INVERT_DEV_TYPE == deviceInfoDto.getDevTypeId() ||
                        DevTypeConstant.BOX_DEV_TYPE == deviceInfoDto.getDevTypeId()) {
                    
                    signalValue = MathUtil.formatDouble(signalClient.get(deviceInfoDto.getId(), "active_power"));
                }else if (DevTypeConstant.EMI_DEV_TYPE_ID == deviceInfoDto.getDevTypeId()) {
                    
                    signalValue = MathUtil.formatDouble(signalClient.get(deviceInfoDto.getId(), "irradiation_intensity"));
                }else if (DevTypeConstant.GATEWAYMETER_DEV_TYPE_ID == deviceInfoDto.getDevTypeId() || 
                        DevTypeConstant.BOXTRANFORM_DEV_TYPE_ID == deviceInfoDto.getDevTypeId() ||
                        DevTypeConstant.COLSTAMETER_DEV_TYPE_ID == deviceInfoDto.getDevTypeId() ||
                        DevTypeConstant.PRODUCTIONMETER_DEV_TYPE_ID == deviceInfoDto.getDevTypeId() ||
                        DevTypeConstant.NONPRODUCTIONMETER_DEV_TYPE_ID == deviceInfoDto.getDevTypeId()) {
                    
                    signalValue = MathUtil.formatDouble(signalClient.get(deviceInfoDto.getId(), "active_capacity"));
                }else if (DevTypeConstant.DCJS_DEV_TYPE == deviceInfoDto.getDevTypeId()) {
                    
                    signalValue = MathUtil.formatDouble(signalClient.get(deviceInfoDto.getId(), "photc_u"));
                }
                
                deviceInfoDto.setActivePower(signalValue);
                deviceInfoDto.setSignaData(signalValue);
            }
            
            setDevsStatus(queryResult);
        } 
        
        return queryResult;
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
            deviceDetail.setSignalVersion(device.getSignalVersion());

            // 查询电站的名称
            StationInfoM station = stationInfoMMapper.selectStationInfoMByStationCode(device.getStationCode());
            if (null != station) {
                // 设置设备的地址
                // deviceDetail.setDevAddr(station.getStationName() + device.getDevName()); 设备安装地址修改为电站地址
                deviceDetail.setDevAddr(station.getStationAddr());
                deviceDetail.setStationName(station.getStationName());
            }

            // 设备的厂家名称
            String venderName = devInfoMapper.selectVenderName(device.getSignalVersion());
            deviceDetail.setVenderName(venderName);

            // 组串容量配置
            List<DevicePvModuleDto> pvInfos = devInfoMapper.selectDevicePvInfos(id);
            deviceDetail.setPvInfo(pvInfos);
            deviceDetail.setPvNum(pvInfos == null ? 0 : pvInfos.size());

            // 设备状态
            setDevStatus(deviceDetail);
        }

        return deviceDetail;
    }

    @Override
    public Long countAlarmByDevId(DeviceAlamDto condition) {
        return devInfoMapper.countAlarmByDevId(condition);
    }

    @Override
    public List<DeviceAlamDto> selectDevAlarm(DeviceAlamDto condition) {
        return devInfoMapper.selectDevAlarm(condition);
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
    private void setDevsStatus(List<DeviceInfoDto> devDtoList) {
        if (devDtoList != null && devDtoList.size() > 0) {
            List<Long> deviceIdList = new ArrayList<>(); // 设备的id
//            List<String> stationCodes = new ArrayList<>(); // 电站编号信息
            for (DeviceInfoDto dto : devDtoList) {
                deviceIdList.add(dto.getId());
//                if (!stationCodes.contains(dto.getStationCode())) {
//                    stationCodes.add(dto.getStationCode());
//                }
            }
//            Map<String, String> stationState = StationCache.getStationHealthState(stationCodes); // 获取电站的状态
            List<Long> deviceAlarmCount = devInfoMapper.countCritAlarmsByDeviceIds(deviceIdList);

            for (DeviceInfoDto dto : devDtoList) {
                ConnectStatus connStatus = connCacheClient.get(dto.getId());
//                String state = stationState.get(dto.getStationCode());
                if (connStatus == null || ConnectStatus.DISCONNECTED.equals(connStatus)
                		// || String.valueOf(StationInfoConstant.DISCONECTED).equals(state)
                		) { // 如果没有获取到设备的状态,将设备设置为断连状态
                    dto.setDevStatus(StationInfoConstant.DISCONECTED);
                } else {
                    if (deviceAlarmCount.contains(dto.getId())) {
                        dto.setDevStatus(StationInfoConstant.TROUBLE);
                    } else {
                        dto.setDevStatus(StationInfoConstant.HEALTHY);
                    }
                }
            }
        }
    }

    @Override
    public DeviceDetail selectDeviceWithChild(Long id) {
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
            deviceDetail.setSignalVersion(device.getSignalVersion());

            // 查询电站的名称
            StationInfoM station = stationInfoMMapper.selectStationInfoMByStationCode(device.getStationCode());
            if (null != station) {
                // 设置设备的地址
                // deviceDetail.setDevAddr(station.getStationName() + device.getDevName());
            	deviceDetail.setDevAddr(station.getStationAddr()); // 设备安装地址修改为电站地址
                deviceDetail.setStationName(station.getStationName());
            }

            // 设备的厂家名称
            String venderName = devInfoMapper.selectVenderName(device.getSignalVersion());
            deviceDetail.setVenderName(venderName);

            //子设备
            List<DeviceDetail> subDevices = devInfoMapper.selectDevWithChild(id);
            deviceDetail.setChildDevices(subDevices);
            
            // 设备状态
            setDevStatus(deviceDetail);
        }

        return deviceDetail;
    }
}
