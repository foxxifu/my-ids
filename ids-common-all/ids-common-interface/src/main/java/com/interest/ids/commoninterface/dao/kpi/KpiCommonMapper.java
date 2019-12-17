package com.interest.ids.commoninterface.dao.kpi;

import java.util.List;
import java.util.Map;

import com.interest.ids.common.project.bean.kpi.KpiMeterMonthM;
import com.interest.ids.common.project.bean.kpi.KpiStation;
import com.interest.ids.common.project.bean.kpi.StationAndEnterprise;
import com.interest.ids.commoninterface.service.kpi.IKpiCommonService;

import org.apache.ibatis.annotations.Param;

public interface KpiCommonMapper extends IKpiCommonService{

    // 获取累计发电量
    KpiStation getKpiStationAllProductPower(
            StationAndEnterprise stationEnterprise);

    // 获取集团的安全运行天数
    String getEnterpriseSafeDays(Long enterpriseId);

    // 获取最新一个月的每日发电量- 发展趋势
    List<KpiStation> getJfreeKpiStationByDay(
            StationAndEnterprise stationEnterprise);

    // 获取当日发电量
    Double getCurrentDayPower(Long userId);

    // 供电负荷 --- 暂时未做

    // 根据地区统计每个地区的装机容量
    Map<String, Double> getProductPowerByDomain(Long userId);

    // 按年度查询社会贡献值
    KpiStation getKpiStationByYear(Long userId);

    // 统计历史社会贡献值
    KpiStation getAllKpiStation(Long userId);

    // 企业实时功率 - 户用逆变器
    Double getHourseholdActivePowerByEnterpriseId(Long userId);

    // 企业实时功率 - 组串逆变器
    Double getStringActivePowerByEnterpriseId(Long userId);

    // 企业实时功率 - 集中式逆变器
    Double getConcActivePowerByEnterpriseId(Long userId);

    // 获取日发电量
    KpiStation getKpiStationProductPowerByDay(
            StationAndEnterprise stationEnterprise);

    // 获取年发电量
    KpiStation getKpiStationProductPowerByYear(
            StationAndEnterprise stationEnterprise);

    // 获取月发电量
    KpiStation getKpiStationProductPowerByMonth(
            StationAndEnterprise stationEnterprise);

    // 获取日发电量和收益
    List<KpiStation> getJFreeKpiStationByDay(
            StationAndEnterprise stationEnterprise);

    // 获取月发电量和收益
    List<KpiStation> getJFreeKpiStationByMonth(
            StationAndEnterprise stationEnterprise);

    // 获取年发电量和收益
    List<KpiStation> getJFreeKpiStationByYear(
            StationAndEnterprise stationEnterprise);

    // 电站排名(pr)
    Map<String, Double> getStationOrderByPr(
            StationAndEnterprise stationEnterprise);

    // 电站排名(等效利用小时)
    Map<String, Double> getStationOrderByEh(
            StationAndEnterprise stationEnterprise);

    // 单站户用逆变器实时功率
    Double getStationHourseholdActivePower(String stationCode);

    // 单站组串逆变器实时功率
    Double getStationStringActivePower(String stationCode);

    // 单站集中式逆变器实时功率
    Double getStationConcActivePower(String stationCode);

    // 按照地区分布统计/电站统计
    Map<String, Integer> getStationByDomain(Long userId);


    /* ===================================================================== */
    List<Map<String, Object>> selectMeterDataByField(Map<String, Object> params);

    List<Map<String, Object>> selectThisYearSocialContributionData(@Param("stationCodes") List<String> stationCodes,
                                                                   @Param("startTime") Long startTime,
                                                                   @Param("endTime") Long endTime);

    List<Map<String, Object>> selectTotalStationSocialKpi(@Param("stationCodes") List<String> stationCodes);

    Map<String, Object> selectSocialContributionByEnterpriseId(@Param("enterpriseId") Long enterpriseId,
                                                               @Param("startTime") Long startTime,
                                                               @Param("endTime") Long endTime);

    Map<String, Object> selectTotalSocialContributionByEnterpriseId(@Param("enterpriseId") Long enterpriseId);
    
    List<Integer> selectStationDeviceTypes(String stationCode);
    
    /**
     * 根据时间范围统计逆变器的月数据
     * @param stationCode
     * @param startTime
     * @param endTime
     * @return
     */
	List<KpiMeterMonthM> queryMeterDataByTime(@Param("stationCode") String stationCode,
			@Param("startTime") long startTime,@Param("endTime") long endTime);
}
