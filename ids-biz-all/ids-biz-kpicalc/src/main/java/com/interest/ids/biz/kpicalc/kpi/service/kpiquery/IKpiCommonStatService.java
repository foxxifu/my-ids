package com.interest.ids.biz.kpicalc.kpi.service.kpiquery;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import com.interest.ids.biz.kpicalc.kpi.util.KpiCacheSet;
import com.interest.ids.common.project.bean.KpiBaseModel;
import com.interest.ids.common.project.bean.kpi.KpiDcCombinerDayM;
import com.interest.ids.common.project.bean.kpi.KpiStationDayM;
import com.interest.ids.common.project.bean.sm.KpiReviseT;

public interface IKpiCommonStatService {

    /**
     * 批量保存对象
     */
    <T> void saveOrUpdate(List<T> list);

    /**
     * 查询上一个时刻缓存的值
     * @param statTime 开始时间
     * @param sIds 电站ids
     * @param devTypeIds 设备类型ids
     * @param column 列
     * @param querTable 查询表
     * @return
     */
    Map<String, Double> getLastHourPowerMax(Long statTime, Collection<String> sIds, Collection<Integer> devTypeIds, String column,
                                                   String querTable);

    /**
     * 取得逆变器上一个小时pmax : sid + dId作为key ， pmax作为value
     */
    Map<String, Double> getLastHourTotalPower(Long hour, Collection<String> sIds, TimeZone timeZone, Collection<Integer> devTypeIds);

    /**
     * 根据配置获取设备kpi数据
     */
    List<Object[]> listDataByKpiConfig(KpiCacheSet setting, Long sTime, Long eTime, List<String> sIds);

    /**
     * 通过配置取出环境监测仪5分钟kpi数据（有共享关系）
     * @param setting
     * @param sTime
     * @param eTime
     * @param sIds
     * @return
     */
    List<Object[] > listEnvironmentMinDataByKpiConfig(KpiCacheSet setting, Long sTime, Long eTime, List<String> sIds);

    /**
     * 通过配置取出环境监测仪5分钟kpi数据（有共享关系）
     * @param setting
     * @param sTime
     * @param eTime
     * @param sIds
     * @return
     */
    List<Object[] > listEnvironmentHourDataByKpiConfig(KpiCacheSet setting, Long sTime, Long eTime, List<String> sIds);

    /**
     * 将上一次的计算数据转换成list object[]
     *
     * @param list
     * @return
     */
    List<Object[]> transListDataToListObjectArr(List<? extends KpiBaseModel> list, String cacheType);

    /**
     * 判断电站是否拥有某种设备, 如果传入多个电站若这些电站中有一个有返回true
     *
     * @param sIds 电站id
     * @param devTypeIds 设备类型id
     * @return
     */
    boolean checkDevIsExistsInStation(List<String> sIds, Collection<Integer> devTypeIds);

    /**
     * 判断多电站是否拥有某一些设备, 如果传入多个电站若这些电站中有一个有返回true
     *
     * @param sIds
     * @param devTypeIds
     * @return
     */
    Map<String, Boolean> getEveryDevIsExistsInStation(Collection<String> sIds, Collection<Integer> devTypeIds);


    /**
     * 取出超过某个限制的辐照量的所有时间集合(设备)
     */
    Map<Long, Set<Long>> getEnvRadiantOverLimitOnTime(Map<String, Long> emiMap, Double limitRadiant, Long sTime, Long eTime);

    Map<String, Integer> getSignalCodeOrder(Integer deviceTypeId, Collection<String> columnNames);

    /**
     * 给定设备编号列表及时间范围， 查询每个设备最后一个有效的日发电量
     * @param stationCode
     * @param startTime
     * @param endTime
     * @return
     */
    Map<Long, Double> getFinalDeviceDayCap(String stationCode, Long startTime, Long endTime);

    /**
     * 查询电站天累计数据：逆变器发电量、上网电量、辐照量 等
     * @param stationCodes
     * @param startTime
     * @param endTime
     * @return
     */
    List<KpiStationDayM> getFinalStationDayData(List<String> stationCodes, Long startTime, Long endTime);
    
    /**
     * 查询给定电站、时间维度、修正日期下的修正记录
     * @param stationCodes
     * @param timeDim
     * @param reviseTime
     * @return <stationCode, <kpiKey, KpiReviseT>>
     */
    Map<String, Map<String, KpiReviseT>> getKpiKeyReviseMap(List<String> stationCodes, String timeDim, Long reviseTime);

    /**
     * 查询电站下的所有的直流汇率箱天kpi数据
     * @param stationCode
     */
	List<KpiDcCombinerDayM> getKpiDcCombinerDayByStationCode(String stationCode, long startTime,
			long endTime);

	/**
     * 查询电站下的所有的直流汇率箱天kpi数据
     * @param stationCode
     */
	List<KpiDcCombinerDayM> getTopKpiDcCombinerDayByStationCode(
			String stationCode, long startTime,
			long endTime);
}
