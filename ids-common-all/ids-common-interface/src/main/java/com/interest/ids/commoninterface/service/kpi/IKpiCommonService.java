package com.interest.ids.commoninterface.service.kpi;

import java.util.List;
import java.util.Map;

import com.interest.ids.common.project.bean.kpi.CombinerDc;
import com.interest.ids.common.project.bean.kpi.InverterString;
import com.interest.ids.common.project.bean.kpi.KpiRealTimeM;
import com.interest.ids.common.project.bean.kpi.KpiStationHourM;
import com.interest.ids.common.project.bean.kpi.KpiStationMonthM;
import com.interest.ids.common.project.bean.kpi.KpiStationYearM;
import com.interest.ids.common.project.bean.kpi.KpiMeterMonthM;

public interface IKpiCommonService {

    /**
     * 查询kpi电站小时
     * @param stationCodes 电站编号
     * @param startTime 开始时间（毫秒）
     * @param endTime 结束时间（毫秒）
     * @return
     */
    List<KpiStationHourM> queryStationHourKpi(List<String> stationCodes, Long startTime, Long endTime);

    /**
     * 查询电站月KPI数据
     * @param stationCodes 
     * @param startTime 
     * @param endTime
     * @return
     */
    List<KpiStationMonthM> queryStationMonthKpi(List<String> stationCodes, Long startTime, Long endTime);

    /**
     * 查询电站月KPI数据
     * @param stationCodes 
     * @param statTime
     * @return
     */
    List<KpiStationMonthM> queryStationMonthKpi(List<String> stationCodes, Long statTime);

    /**
     * 查询电站年KPI数据
     * @param stationCodes
     * @param startTime 
     * @param endTime
     * @return
     */
    List<KpiStationYearM> queryStationYearKpi(List<String> stationCodes, Long startTime, Long endTime);

    /**
     * 查询电站年KPI数据
     * @param stationCodes 
     * @param statTime
     * @return
     */
    List<KpiStationYearM> queryStationYearKpi(List<String> stationCodes, Long statTime);

    /**
     * 统计电站下电表某KPI的累计，以电站进行分组
     * @param stationCodes
     * @param filed
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    Map<String, Double> queryMeterDataByField(List<String> stationCodes, List<Integer> deviceTypeIds, String filed, Long startTime,
                                              Long endTime);

    /**
     * 查询直流汇流箱的性能数据
     * @param stationCode
     * @param startTime
     * @param endTime
     * @return
     */
    List<CombinerDc> getCombinerDCMs(String stationCode, Long startTime, Long endTime);
    
    /**
     * 查询直流汇流箱各支路最大电流、最大电压
     * @param stationCode
     * @param startTime
     * @param endTime
     * @return
     */
    List<Map<String, Object>> getCombinerPvMaxData(String stationCode, Long startTime, Long endTime);
    
    /**
     * 查询组串式逆变器性能数据
     * @param stationCode
     * @param startTime
     * @param endTime
     * @return
     */
    List<InverterString> getStringInverterData(String stationCode, Long startTime, Long endTime);
    
    void saveKpiRealTimeData(List<KpiRealTimeM> list);

    /**
     * 删除小于当前时间的实时数据
     * 
     * @param realTime
     *                 实时数据
     *     
     */
    void deleteSmallerRealTimeData(List<KpiRealTimeM> realTime);
    
    List<Integer> selectStationDeviceTypes(String stationCode);

    /**
     * 根据时间范围统计逆变器的月数据
     * @param stationCode
     * @param startTime
     * @param endTime
     * @return
     */
	List<KpiMeterMonthM> queryMeterDataByTime(String stationCode,
			long startTime, long endTime);

}

