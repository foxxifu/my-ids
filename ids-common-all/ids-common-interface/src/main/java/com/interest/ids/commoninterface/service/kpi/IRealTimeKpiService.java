package com.interest.ids.commoninterface.service.kpi;

import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.commoninterface.constant.realtimekpi.KpiItem;

import java.util.List;
import java.util.Map;

public interface IRealTimeKpiService {

    /**
     * 通过电站编号获取电站列表的Kpi总和
     * @param stationCodes
     *
     * @return Map<stationCode,<item,value>>
     */
    Map<String,Map<String,Object>> getRealTimeKpi(List<String> stationCodes);

    /**
     * 通过电站编号获取电站列表Kpi总和
     * @param stationCodes
     * @return Map<item,totalVal>
     */
    Map<String,Object> getRealTimeKpiTotal(List<String> stationCodes);

    /**
     * 获取等效利用小时数
     * @param stationInfoMs
     * @return
     */
    Map<String,Double> getEquivalentUserFulHour(List<StationInfoM> stationInfoMs);

    /**
     * 查询具体某项实时Kpi指标
     * @param stationInfoMs
     * @param item
     * @return
     */
    Map<String,Object> getKpiByItem(List<StationInfoM> stationInfoMs,KpiItem item);

    /**
     * 获取总发电量
     * @param stationCodes
     * @return
     */
    Map<String,Object> getCurrentTotalCapacityByStationCode(List<String> stationCodes);

}

