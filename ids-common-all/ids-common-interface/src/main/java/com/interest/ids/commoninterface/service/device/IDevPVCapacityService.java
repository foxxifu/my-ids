package com.interest.ids.commoninterface.service.device;

import com.interest.ids.common.project.bean.device.PvCapacityM;

import java.util.List;
import java.util.Map;

public interface IDevPVCapacityService {

    /**
     * 根据电站编号和设备类型获取组串容量信息
     * @param stationCode
     * @param devTypeId
     * @return
     */
    List<PvCapacityM> queryPVCapByType(String stationCode,Integer devTypeId);

    Double getDevAllPVCap(Long devId);

    Double getStationAllPVCap(String stationCode);

    Map<String,Double> getStationPVCapByList(List<String> stationCodes);
    
    /**
     * 批量查询电站容量
     * @param stationCode
     * @param devTypeId
     * @return
     */
    List<PvCapacityM> queryPVCapBySIdsAndType(List<String> stationCode,Integer devTypeId);

    void initDevPvInfoCache();
    
    /**
     * 获取系统总接入容量 (单位： kw)
     * @return
     */
    Double countAllInstalledCapacity();

}
