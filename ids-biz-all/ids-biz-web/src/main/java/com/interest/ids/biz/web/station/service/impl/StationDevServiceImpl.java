package com.interest.ids.biz.web.station.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.interest.ids.common.project.bean.device.DeviceModuleQuery;
import com.interest.ids.common.project.bean.device.StationDev;
import com.interest.ids.common.project.bean.device.StationDevicePvModule;
import com.interest.ids.common.project.bean.device.DevicePvCapacity;
import com.interest.ids.common.project.bean.device.StationPvModule;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.commoninterface.dao.station.StationDevMapper;
import com.interest.ids.commoninterface.service.station.IStationDevService;
import com.interest.ids.redis.client.service.UnbindDeviceClient;

@Service("stationDevService")
public class StationDevServiceImpl implements IStationDevService 
{
    @Resource
    private UnbindDeviceClient unBindDeviceClient;
    
    @Resource
    private StationDevMapper stationDevMapper;
    
    @Override
    public boolean selectDevExistByEsn(String esn)
    {
        DeviceInfo dev = null;// unBindDeviceClient.get(esn);
        return null != dev;
    }

    @Override
    public List<String> selectDevModelVersion(Long enterpriseId) 
    {
        return stationDevMapper.selectDevModelVersion(enterpriseId);
    }

    @Override
    public int saveStationDev(StationDev stationDev)
    {
        DeviceInfo dev = null;// unBindDeviceClient.get(stationDev.getStationCode());
        if(null != dev)
        {
            dev.setStationCode(stationDev.getStationCode());
            dev.setSignalVersion(stationDev.getDevModelVersion());
            dev.setDevName(stationDev.getDevName());
            return stationDevMapper.insetStationDev(dev);
        }else
        {
            return 0;
        }
    }

    @Override
    public List<DeviceInfo> selectStationDevsByStationCode(String stationCode)
    {
        return stationDevMapper.selectStationDevsByStationCode(stationCode);
    }

    @Override
    public StationPvModule selectStationPvModuleDetail(Long id) {
        return stationDevMapper.selectStationPvModuleDetail(id);
    }

    @Override
    public String insertStationPvModule(List<StationDevicePvModule> list) 
    {
        String result = "-1";
        if(null != list && list.size() > 0)
        {
            /*result = stationDevMapper.insertStationPvModule(list) +"";
              DeviceInfo device = stationDevMapper.selectStationDevsByStationId(list.get(0).getDeviceId());
            List<Double> pv = new ArrayList<Double>();
            for (StationDevicePvModule l : list) {
                pv.add(l.getFixedPower());
            }
            
            DevicePvCapacity capacity = new DevicePvCapacity();
            capacity.setBusiCode(device.getBusiCode());
            capacity.setEnterpriseId(device.getEnterpriseId());
            capacity.setDeviceTypeId(device.getDeviceTypeId());
            capacity.setDeviceId(list.get(0).getDeviceId());
            capacity.setPvs(pv);
            
            stationDevMapper.insertDeviceCapacity(capacity);*/
            
        }
        return result;
    }

    @Override
    public List<Map<String,String>> selectModulesByEsn(DeviceModuleQuery deviceModuleQuery) {
        return stationDevMapper.selectModulesByEsn(deviceModuleQuery);
    }

    @Override
    public List<StationDevicePvModule> selectStationDevicePvModules(String esn) {
        return stationDevMapper.selectStationDevicePvModules(esn);
    }

    @Override
    public String updateDeviceModule(List<StationDevicePvModule> list) 
    {
        //更新设备组件关联关系
        Integer index = stationDevMapper.updateDeviceModule(list);
        
        //同步数据到容量表
        List<Double> pv = new ArrayList<Double>();
        for (StationDevicePvModule l : list) {
            pv.add(l.getFixedPower());
        }
        DevicePvCapacity capacity = new DevicePvCapacity();
        capacity.setDeviceId(list.get(0).getDevId());
        capacity.setPvs(pv);
        Integer index1 = stationDevMapper.updateDeviceModuleCapacity(capacity);
        return index+"";
    }

}
