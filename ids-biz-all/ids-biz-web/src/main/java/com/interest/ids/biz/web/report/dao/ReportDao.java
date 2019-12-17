package com.interest.ids.biz.web.report.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.interest.ids.biz.web.report.controller.params.ReportParams;
import com.interest.ids.biz.web.report.vo.DevFaultReportVO;
import com.interest.ids.biz.web.report.vo.DeviceVO;
import com.interest.ids.biz.web.report.vo.InverterDetailReportVO;
import com.interest.ids.biz.web.report.vo.InverterReportVO;
import com.interest.ids.biz.web.report.vo.StationReportVO;
import com.interest.ids.biz.web.report.vo.SubarrayReportVO;

/**
 * @Author: sunbjx
 * @Description:
 * @Date Created in 22:16 2018/1/21
 * @Modified By:
 */
public interface ReportDao {

    /**
     * @param params
     * @return
     */
    List<DevFaultReportVO> listDevFault(@Param("params") ReportParams params);

    /**
     * @param params
     * @return
     */
    List<StationReportVO> listStation(ReportParams params);

    /**
     * 通过设备类型ID和级别id获取频率最多故障
     *
     * @param devTypeId
     * @param levId
     * @return
     */
    String getMaxTroubleName(@Param("devTypeId") Integer devTypeId, @Param("levId") Integer levId);

    /**
     * 查询逆变器运行报表，根据时间维度（日、月、年）、设备编号
     *
     * @param stationCode
     * @param time
     * @param timeDim
     * @param startIndex
     * @param endIndex
     * @return
     */
    List<InverterReportVO> selectInverterRpt(Map<String, Object> queryParams);

    /**
     * 统计逆变器运行报表总记录数
     *
     * @param deviceIds
     * @param time
     * @param timeDim
     * @return
     */
    int countInverterReportResult(Map<String, Object> queryParams);
    
    /**
     * 查询子阵运行报表，根据时间维度（日、月、年）、设备编号
     *
     * @param stationCode
     * @param time
     * @param timeDim
     * @param startIndex
     * @param endIndex
     * @return
     */
    List<SubarrayReportVO> selectSubarrayRpt(Map<String, Object> queryParams);

    /**
     * 统计子阵运行报表总记录数
     *
     * @param deviceIds
     * @param time
     * @param timeDim
     * @return
     */
    int countSubarrayReportResult(Map<String, Object> queryParams);
    
    /**
     * 查询逆变器详细运行报表，5分钟数据
     *
     * @param stationCode
     * @param collectTime
     * @param deviceIds
     * @return
     */
    List<InverterDetailReportVO> selectInverterDetailRpt(Map<String, Object> queryParams);
    
    /**
     * 查询逆变器详细运行报表，5分钟数据
     *
     * @param stationCode
     * @param collectTime
     * @param deviceIds
     * @return
     */
    List<Map<String, Object>> exportInverterDetailRpt(Map<String, Object> queryParams);

    /**
     * 查询逆变器详细运行报表，5分钟数据
     *
     * @param deviceIds
     * @param collectTime
     * @param deviceIds
     * @return
     */
    int countInverterDetailReportResult(Map<String, Object> queryParams);
    
    /**
     * 查询该电站下的设备（主要是逆变器）
     * @param queryParams
     * @return
     */
    List<DeviceVO> selectDevices(Map<String, Object> queryParams);
}
