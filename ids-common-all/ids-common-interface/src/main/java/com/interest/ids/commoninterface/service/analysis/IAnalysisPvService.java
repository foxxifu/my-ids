package com.interest.ids.commoninterface.service.analysis;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.interest.ids.common.project.bean.analysis.AnalysisMatrixDayM;
import com.interest.ids.common.project.bean.analysis.AnalysisMatrixMonthM;
import com.interest.ids.common.project.bean.analysis.AnalysisMatrixYearM;
import com.interest.ids.common.project.bean.analysis.AnalysisPvM;
import com.interest.ids.common.project.bean.analysis.AnalysisPvMonthM;
import com.interest.ids.common.project.bean.kpi.KpiInverterDayM;
import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.dto.AnalysisDayGroupDto;

public interface IAnalysisPvService {

    /**
     * 查询大机关联的直流汇流箱
     * @param concInvIds
     * @return
     */
    Map<Long, List<Long>> getConcInvWithDcjs(List<Long> concInvIds);
    
    Map<Long, List<Long>> getConcInvWithDcjsByDevInfo(Collection<DeviceInfo> concInvIds);
    
    /**
     * 查询基准逆变器日发电量
     * @param stationCode
     * @return
     */
    KpiInverterDayM getCurrentBaseInvDayCap(String stationCode, Integer onlineType);
    
    /**
     * 查询标杆逆变器日发电量
     * @param devId
     * @param devTypeId
     * @return
     */
    KpiInverterDayM getBechmarkInvCurrentDayCap(Long devId, Integer devTypeId);
    
    /**
     * 持久化组串分析结果
     * @param pvAnalyM
     */
    void saveOrUpdatePvAnlayM(List<AnalysisPvM> pvAnalyM);
    
    List<AnalysisPvM> getExistedAnalyPvM(List<AnalysisPvM> pvAnalyM);
    
    /**
     * 按电站获取某天的组串诊断数据
     * @param stationCode
     * @param startTime
     * @param endTime
     * @return
     */
    List<AnalysisPvM> getPvAnalysisDay(String stationCode, Long startTime, Long endTime);
    
    /**
     * 获取电站某月的组串诊断数据
     * @param stationCode
     * @param analysisTime
     * @return
     */
    List<AnalysisPvMonthM> getPvAnalysisMonth(String stationCode, Long analysisTime);
    
    /**
     * 新增组串诊断月数据
     * @param analysisPvMonthMs
     */
    void saveAnalysisPvMonthM(List<AnalysisPvMonthM> analysisPvMonthMs);
    
    /**
     * 更新组串诊断月数据
     * @param analysisPvMonthMs
     */
    void upateAnalysisPvMonthM(List<AnalysisPvMonthM> analysisPvMonthMs);
    
    /**
     * 获取组串诊断子阵级日数据
     * @param stationCode
     * @param analysisTime
     * @return
     */
    List<AnalysisMatrixDayM> getAnalysisMatrixDay(String stationCode, Long analysisTime);
    
    /**
     * 更新组串诊断子阵日统计数据
     * @param analysisMatrixDayMs
     */
    void updateAnalysisMatrixDay(List<AnalysisMatrixDayM> analysisMatrixDayMs);
    
    void saveAnalysisMatrixDay(List<AnalysisMatrixDayM> analysisMatrixDayMs);
    
    /**
     * 获取组串诊断子阵月统计数据
     * @param stationCode
     * @param analysisTime
     * @return
     */
    List<AnalysisMatrixMonthM> getAnalysisMatrixMonth(String stationCode, Long analysisTime);
    
    void updateAnalysisMatrixMonth(List<AnalysisMatrixMonthM> analysisMatrixMonthMs);
    
    void saveAnalysisMatrixMonth(List<AnalysisMatrixMonthM> analysisMatrixMonthMs);
    
    /**
     * 获取组串诊断子阵年统计数据
     * @param stationCode
     * @param analysisTime
     * @return
     */
    List<AnalysisMatrixYearM> getAnalysisMatrixYear(String stationCode, Long analysisTime);
    
    void updateAnalysisMatrixYear(List<AnalysisMatrixYearM> analysisMatrixYearMs);
    
    void saveAnalysisMatrixYear(List<AnalysisMatrixYearM> analysisMatrixYearMs);
    
    Double getMatrixDayProductPower(List<Long> devIds, Long collectTime);
    /**
     * 根据传递的开始和结束时间，获取根据子阵分组的数据
     * @param stationCode
     * @param beginDate
     * @param endDate
     * @return
     */
	List<AnalysisDayGroupDto> getPvAnalysisOfGroupByMatrix(String stationCode, Long beginDate, Long endDate);
}
