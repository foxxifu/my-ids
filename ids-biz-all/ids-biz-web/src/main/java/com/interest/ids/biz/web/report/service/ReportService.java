package com.interest.ids.biz.web.report.service;

import java.util.List;
import java.util.Map;

import com.interest.ids.biz.web.report.controller.params.InverterDetailParams;
import com.interest.ids.biz.web.report.controller.params.ReportParams;
import com.interest.ids.biz.web.report.vo.DevFaultReportVO;
import com.interest.ids.biz.web.report.vo.InverterReportVO;
import com.interest.ids.biz.web.report.vo.StationReportVO;
import com.interest.ids.biz.web.report.vo.SubarrayReportVO;

/**
 * @Author: sunbjx
 * @Description:
 * @Date Created in 21:48 2018/1/21
 * @Modified By:
 */
public interface ReportService {

    /**
     * 设备故障分类统计报表接口：默认查询所有设备
     *
     * @param params
     * @return
     */
    List<DevFaultReportVO> listDevFault(ReportParams params);

    /**
     * 电站运行报表接口： 默认查询登陆用户的所有电站，时间维度天
     *
     * @param params
     * @return
     */
    List<StationReportVO> listStation(ReportParams params);

    /**
     * 查询逆变器运行报表，根据时间维度（日、月、年）、设备编号
     *
     * @param deviceIds
     * @param time
     * @param timeDim
     * @param startIndex
     * @param endIndex
     * @return
     */
    List<InverterReportVO> listInverterRpt(Map<String, Object> params);

    /**
     * 统计逆变器运行报表总记录数
     *
     * @param deviceIds
     * @param time
     * @param timeDim
     * @return
     */
    int countInverterReportResult(Map<String, Object> params);
    
    /**
     * 查询子阵运行报表，根据时间维度（日、月、年）、设备编号
     *
     * @param deviceIds
     * @param time
     * @param timeDim
     * @param startIndex
     * @param endIndex
     * @return
     */
    List<SubarrayReportVO> listSubarrayRpt(Map<String, Object> params);

    /**
     * 统计子阵运行报表总记录数
     *
     * @param deviceIds
     * @param time
     * @param timeDim
     * @return
     */
    int countSubarrayReportResult(Map<String, Object> params);
    
    /**
     * 查询逆变器运行报表，详细5分钟数据
     *
     * @param stationCode
     * @param collectTime
     * @param deviceIds
     * @return
     */
    Map<String, Object> listInverterDetailRpt(InverterDetailParams params);
    
    /**
     * 查询逆变器运行报表，详细5分钟数据
     *
     * @param stationCode
     * @param collectTime
     * @param deviceIds
     * @return
     */
    List<Object[]> exportInverterDetailRpt(InverterDetailParams params);

    /**
     * 统计子阵运行报表总记录数
     *
     * @param stationCode
     * @param collectTime
     * @param deviceIds
     * @return
     */
    int countInverterDetailReportResult(InverterDetailParams params);
    
    /**
     * 获取电站逆变器设备信息
     * @return
     */
    Map<String, Object> selectDevices(InverterDetailParams params); 
}
