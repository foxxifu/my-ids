package com.interest.ids.commoninterface.service.station;

import java.util.List;
import java.util.Map;

import com.interest.ids.common.project.bean.device.DeviceModuleQuery;
import com.interest.ids.common.project.bean.device.StationDev;
import com.interest.ids.common.project.bean.device.StationDevicePvModule;
import com.interest.ids.common.project.bean.device.StationPvModule;
import com.interest.ids.common.project.bean.signal.DeviceInfo;

public interface IStationDevService 
{
    //设备接入 -- esn号校验设备是否存在
    boolean selectDevExistByEsn(String esn);

    //查询所有设备的版本号 (根据企业号去查询)
    List<String> selectDevModelVersion(Long enterpriseId);

    //设备接入保持
    int saveStationDev(StationDev stationDev);

    //查看电站下关联的设备
    List<DeviceInfo> selectStationDevsByStationCode(String stationCode);

   /* //组串接入厂家信息
    public List<StationPvModule> selectStationPvModel();*/

    //根据厂家的id查询某一个厂家的详细信息
    StationPvModule selectStationPvModuleDetail(Long id);

    //给设备添加组串
    String insertStationPvModule(List<StationDevicePvModule> list);

    //查询设备下的组串信息
    List<Map<String,String>> selectModulesByEsn(DeviceModuleQuery deviceModuleQuery);

    //查询设备关联的组件
    List<StationDevicePvModule> selectStationDevicePvModules(String esn);

    //更新设备组串信息
    String updateDeviceModule(List<StationDevicePvModule> list);
}
