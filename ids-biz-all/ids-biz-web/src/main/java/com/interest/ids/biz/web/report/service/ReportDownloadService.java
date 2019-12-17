package com.interest.ids.biz.web.report.service;

import com.interest.ids.biz.web.report.controller.params.ReportParams;
import com.interest.ids.biz.web.report.vo.DevFaultReportVO;
import com.interest.ids.biz.web.report.vo.StationReportVO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.List;
import java.util.Map;

/**
 * @Author: sunbjx
 * @Description: EXCEL 数据报表生成接口
 * @Date: Created in 下午2:18 18-1-27
 * @Modified By:
 */
public interface ReportDownloadService {

    /**
     * 生成设备故障 EXCEL 报表
     * @param datas 报表数据
     * @return
     */
    HSSFWorkbook createDevAlarmExcel(ReportParams dto, List<DevFaultReportVO> datas, Map<String, String> map);

    /**
     * 生成电站运行分析 EXCEL 报表
     * @param datas 报表数据
     * @return
     */
    HSSFWorkbook createStationRuningExcel(ReportParams dto, List<StationReportVO> datas,Map<String, String> map);
}
