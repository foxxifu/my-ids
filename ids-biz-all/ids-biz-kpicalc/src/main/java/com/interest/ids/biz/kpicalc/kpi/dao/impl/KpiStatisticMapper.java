package com.interest.ids.biz.kpicalc.kpi.dao.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.interest.ids.common.project.bean.kpi.KpiStationDayM;
import com.interest.ids.common.project.bean.sm.KpiReviseT;

public interface KpiStatisticMapper {

    /**
     * 查询设备编号
     * @param stationCodes 电站编号集
     * @param devTypeIds 电站类型集
     * @return
     */
    List<Long> listDevicesByStationCoedsAndDevTypes(@Param("stationCodes") Collection<String> stationCodes,
                                                      @Param("devTypeIds") Collection<Integer> devTypeIds);

    /**
     * 查询上一小时的设备有效发电量
     * @param lastHour 上一小时时刻
     * @param deviceIds 设备编号
     * @param tableName 表名（可指定多个设备性能数据表）
     * @param columnName 列名（同一含义，字段名可能不一样）
     * @return
     */
    List<Map<String, Object>> queryLastHourPowerMax(@Param("lastHour") Long lastHour,
                                            @Param("deviceIds") Collection<Long> deviceIds,
                                            @Param("tableName") String tableName,
                                            @Param("columnName") String columnName);

    /**
     * 查询性能数据（Map 中封装了电站编号、表名、开始时间、结束时间、列名）
     * @param args
     * @return
     */
    List<Map<String, Object>> listDataByKpiConfig(Map<String, Object> args);

    /**
     * 检查电站下是否存在该类型集中的设备
     * @param stationCodes
     * @param deviceTypeIds
     * @return
     */
    Integer countDevicesByDeviceType(@Param("stationCodes") Collection<String> stationCodes,
                                 @Param("deviceTypeIds") Collection<Integer> deviceTypeIds);

    /**
     * 返回电站与对应设备类型的设备数 key-value
     * @param sIds
     * @param devTypeIds
     * @return
     */
    List<Map<String, Object>> countDevicesByStationGroup(@Param("stationCodes") Collection<String> sIds,
                                                    @Param("deviceTypeIds") Collection<Integer> devTypeIds);

    /**
     * 查询辐照强度越线的记录
     * @param args
     * @return
     */
    List<Map<String, Object>> queryRadiantOverLimitOnTime(Map<String, Object> args);

    List<Long> queryDelStationDevice(@Param("stationCodes") Collection<String> sIds,
                                     @Param("deviceTypeIds") Collection<Integer> devTypeId);

    /**
     * 查询归一化的信号点配置，获取信号对应的顺序
     * @param deviceTypeId
     * @param columnNames
     * @return
     */
    List<Map<String, Object>> queryUnificationSignal(@Param("deviceTypeId") Integer deviceTypeId,
                                                     @Param("columnNames") Collection<String> columnNames);

    List<Map<String, Object>> queryFinalDeviceDayCap(@Param("stationCode") String stationCode,
                                             @Param("startTime") Long startTime, @Param("endTime") Long endTime);

    List<KpiStationDayM> queryFinalStationDayData(@Param("stationCodes") List<String> stationCodes,
                                                  @Param("startTime") Long startTime, @Param("endTime") Long endTime);

    /**
     * 查询电站下某日/月/年是否配置有修正记录
     * @param stationCodes
     * @param timeDim
     * @param reviseTime
     * @return
     */
    List<KpiReviseT> selectKpiRevise(@Param("stationCodes")List<String> stationCodes, 
            @Param(value = "timeDim")String timeDim, @Param(value = "reviseTime")Long reviseTime);
}
