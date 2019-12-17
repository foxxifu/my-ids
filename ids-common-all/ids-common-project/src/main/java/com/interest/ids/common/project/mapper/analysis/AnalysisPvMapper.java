package com.interest.ids.common.project.mapper.analysis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.interest.ids.common.project.bean.analysis.AnalysisMatrixDayM;
import com.interest.ids.common.project.bean.analysis.AnalysisMatrixMonthM;
import com.interest.ids.common.project.bean.analysis.AnalysisMatrixYearM;
import com.interest.ids.common.project.bean.analysis.AnalysisPvM;
import com.interest.ids.common.project.bean.analysis.AnalysisPvMonthM;
import com.interest.ids.common.project.bean.kpi.KpiInverterDayM;
import com.interest.ids.common.project.dto.AnalysisDayGroupDto;

public interface AnalysisPvMapper {

    /**
     * 大机关联直流汇流箱
     * @param concInvIds
     * @return
     */
    List<Map<String, Object>> selectConcInvWithDcjs(List<Long> concInvIds);
    
    /**
     * 查询当前基准逆变器的最近时刻的日发电量
     * @param stationCode
     * @return
     */
    KpiInverterDayM selectCurrentBaseInvDayCap(@Param(value = "stationCode")String stationCode, @Param(value = "onlineType")Integer onlineType);
    
    /**
     * 查询当前基准逆变器的最近时刻的日发电量
     * @param id
     * @param devTypeId
     * @return
     */
    KpiInverterDayM selectBechmarkInvCurrentDayCap(@Param("devId")Long id, @Param("devTypeId")Integer devTypeId);
    
    /**
     * 保存组串分析结果
     * @param pvAnalyM
     */
    void savePvAnalyM(List<AnalysisPvM> pvAnalyM);
    
    /**
     * 更新组串分析结果
     * @param pvAnalyM
     */
    void updatePvAnalyM(AnalysisPvM pvAnalyM);
    
    /**
     * 查询哪些分析结果已经入库
     * @param param
     * @return
     */
    List<AnalysisPvM> selectPvAnalyM(List<AnalysisPvM> param);
    
    /**
     * 查询设备所在子阵信息
     * @param param
     * @return
     */
    List<AnalysisPvM> selectDevLocationInfo(List<AnalysisPvM> param);
    
    /**
     * 按电站获取某天的组串诊断数据
     * @param stationCode
     * @param startTime
     * @param endTime
     * @return
     */
    List<AnalysisPvM> selectPvAnalysisDay(@Param(value = "stationCode")String stationCode, 
            @Param(value = "startTime")Long startTime, @Param(value = "endTime")Long endTime);
    
    /**
     * 获取电站某月的组串诊断数据
     * @param stationCode
     * @param analysisTime
     * @return
     */
    List<AnalysisPvMonthM> selectPvAnalysisMonth(@Param(value = "stationCode")String stationCode, 
            @Param(value = "analysisTime")Long analysisTime);
    
    /**
     * 新增组串诊断月数据
     * @param analysisPvMonthMs
     */
    void insertAnalysisPvMonthM(List<AnalysisPvMonthM> analysisPvMonthMs);
    
    /**
     * 更新组串诊断月数据
     * @param analysisPvMonthMs
     */
    void upateAnalysisPvMonthM(AnalysisPvMonthM analysisPvMonthMs);
    
    /**
     * 获取组串诊断子阵级日数据
     * @param stationCode
     * @param analysisTime
     * @return
     */
    List<AnalysisMatrixDayM> selectAnalysisMatrixDay(@Param(value = "stationCode")String stationCode, 
            @Param(value = "analysisTime")Long analysisTime);
    
    /**
     * 更新组串诊断子阵日统计数据
     * @param analysisMatrixDayMs
     */
    void updateAnalysisMatrixDay(AnalysisMatrixDayM analysisMatrixDayMs);
    
    void insertAnalysisMatrixDay(List<AnalysisMatrixDayM> analysisMatrixDayMs);
    
    /**
     * 获取组串诊断子阵月统计数据
     * @param stationCode
     * @param analysisTime
     * @return
     */
    List<AnalysisMatrixMonthM> selectAnalysisMatrixMonth(@Param(value = "stationCode")String stationCode, 
            @Param(value = "analysisTime")Long analysisTime);
    
    void updateAnalysisMatrixMonth(AnalysisMatrixMonthM analysisMatrixMonthMs);
    
    void insertAnalysisMatrixMonth(List<AnalysisMatrixMonthM> analysisMatrixMonthMs);
    
    /**
     * 获取组串诊断子阵年统计数据
     * @param stationCode
     * @param analysisTime
     * @return
     */
    List<AnalysisMatrixYearM> selectAnalysisMatrixYear(@Param(value = "stationCode")String stationCode, 
            @Param(value = "analysisTime")Long analysisTime);
    
    void updateAnalysisMatrixYear(AnalysisMatrixYearM analysisMatrixYearMs);
    
    void insertAnalysisMatrixYear(List<AnalysisMatrixYearM> analysisMatrixYearMs);
    
    /**
     * 查询子阵日发电量
     * @param devIds
     * @param collectTime
     * @return
     */
    Double getMatrixDayProductPower(@Param(value = "devIds")List<Long> devIds, @Param(value = "collectTime")Long collectTime);
    /**
     * 根据单站获取当前电站下的损耗的信息
     * @param stationCode 电站编号
     * @param beginDate 开始时间
     * @param endDate 结束时间
     * @return
     */
	List<AnalysisDayGroupDto> getPvAnalysisOfGroupByMatrix(
			@Param(value = "stationCode")String stationCode, @Param(value = "beginDate")Long beginDate, @Param(value = "endDate")Long endDate);
}
