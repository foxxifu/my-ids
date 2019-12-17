package com.interest.ids.biz.web.report.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.interest.ids.biz.web.report.constant.ReportConstant;
import com.interest.ids.biz.web.report.service.ReportService;
import com.interest.ids.biz.web.report.vo.SubarrayReportVO;
import com.interest.ids.biz.web.utils.DownloadUtil;
import com.interest.ids.biz.web.utils.ExcelUtil;
import com.interest.ids.common.project.bean.sm.Page;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.common.project.il8n.ResourceBundleUtil;
import com.interest.ids.common.project.utils.DateUtil;

/**
 * 子阵运行报表
 * 
 * @author zl
 * @date 2018-1-22
 */

@RequestMapping("/produceMng")
@Controller
public class SubarrayRunReportController {

    private static final Logger logger = LoggerFactory.getLogger(SubarrayRunReportController.class);

    @Resource
    private ReportService reportService;

    /**
     * 报表查询
     * 
     * @return
     */
    @RequestMapping(value = "/listSubarrayRpt", method = RequestMethod.POST)
    @ResponseBody
    public Response<Page<SubarrayReportVO>> listInverterRpt(@RequestBody JSONObject jsonObject,
            HttpServletRequest request) {

        Response<Page<SubarrayReportVO>> response = new Response<>();

//        JSONArray stationCodesArray = jsonObject.containsKey("stationCodes") ? jsonObject.getJSONArray("stationCodes") : null;
        String stationCode = jsonObject.containsKey("stationCode") && jsonObject.getString("stationCode") != null ? jsonObject
                .getString("stationCode") : "";
        
        String timeDim = jsonObject.containsKey("timeDim") && jsonObject.getString("timeDim") != null ? jsonObject
                .getString("timeDim") : ReportConstant.TIMEDIM_DAY;

        Long time = jsonObject.containsKey("time") ? jsonObject.getLong("time") : null;
        if (time == null) {
            if (ReportConstant.TIMEDIM_DAY.equals(timeDim)) {
                time = DateUtil.getBeginOfDayTimeByMill(System.currentTimeMillis() - 86400000);
            } else if (ReportConstant.TIMEDIM_MONTH.equals(timeDim)) {
                time = DateUtil.getBeginOfMonthTimeByMill(System.currentTimeMillis() - 86400000);
            } else {
                time = DateUtil.getBeginOfYearTimeByMill(System.currentTimeMillis() - 86400000);
            }
        }

        Integer pageIndex = jsonObject.containsKey("index") && jsonObject.getInteger("index") != null ? jsonObject
                .getInteger("index") : Page.DEFAULT_PAGE_INDEX;
        Integer pageSize = jsonObject.containsKey("pageSize") && jsonObject.getInteger("pageSize") != null ? jsonObject
                .getInteger("pageSize") : Page.DEFAULT_PAGE_SIZE;

        logger.info("[listSubarrayRpt] arguments={stationCode:" + stationCode + ", time:" + time + ", timeDim:" + timeDim
                + "}");
        
        Map<String, Object> params = getQueryParams(request, stationCode, time, timeDim);

        try {
            int count = reportService.countSubarrayReportResult(params);
            int totalPage = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
            
            int startIndex = (pageIndex - 1) * pageSize;
            params.put("startIndex", startIndex);
            params.put("length", pageSize);
            List<SubarrayReportVO> pageResult = reportService.listSubarrayRpt(params);

            Page<SubarrayReportVO> page = new Page<>();
            page.setCount(count);
            page.setAllSize(totalPage);
            page.setIndex(pageIndex);
            page.setPageSize(pageSize);
            page.setList(pageResult);

            response.setResults(page);
            response.setCode(ResponseConstants.CODE_SUCCESS);

        } catch (Exception e) {
            logger.error("[listSubarrayRpt] get error.", e);

            response.setMessage("can't get report data.");
            response.setCode(ResponseConstants.CODE_FAILED);

        }

        return response;
    }

    /**
     * 导出子阵运行报表
     */
    @RequestMapping(value = "/exportSubarrayRpt", method = RequestMethod.GET)
    @ResponseBody
    public Response<String> exportInverterRpt(@RequestParam(name = "stationCode", required = false) String stationCode,
            @RequestParam(name = "time", required = false) Long time,
            @RequestParam(name = "timeDim", required = false) String timeDim, HttpServletRequest request,
            HttpServletResponse response) {

        logger.info("[exportSubarrayRpt] arguments={stationCode:" + stationCode + ", time:" + time + ", timeDim:" + timeDim
                + "}");

        Response<String> result = new Response<>();

        try {
        	String lang = request.getParameter(ResponseConstants.REQ_LANG);
            String dateFormat = DateUtil.DATE_FORMAT_TO_DAY;
            
            timeDim = timeDim != null && timeDim.trim().length() > 0 ? timeDim : ReportConstant.TIMEDIM_DAY;
            
            if (time == null){
                if (ReportConstant.TIMEDIM_DAY.equals(timeDim)) {
                    time = DateUtil.getBeginOfDayTimeByMill(System.currentTimeMillis() - 24 * 3600 * 1000);
                } else if (ReportConstant.TIMEDIM_MONTH.equals(timeDim)) {
                    time = DateUtil.getBeginOfMonthTimeByMill(System.currentTimeMillis() - 24 * 3600 * 1000);
                } else {
                    time = DateUtil.getBeginOfYearTimeByMill(System.currentTimeMillis() - 24 * 3600 * 1000);
                }
            }

            // 查询导出数据
            Map<String, Object> params = getQueryParams(request, stationCode, time, timeDim);
            params.put("startIndex", 0);
            params.put("length", -1);
            List<SubarrayReportVO> pageResult = reportService.listSubarrayRpt(params);
            
            // 创建标题
            boolean isDayRpt = false;
            StringBuilder title = new StringBuilder(ResourceBundleUtil.getBizKey(lang, "subarrayRunReportController.sub.array.operation")); //子阵运行
     //       StringBuilder reportName = new StringBuilder(ResourceBundleUtil.getBizKey(lang, "subarrayRunReportController.sub.array.operation.data"));//"子阵运行报表-"
     //       if (ReportConstant.TIMEDIM_DAY.equals(timeDim)) {
     //           title.append(ResourceBundleUtil.getBizKey(lang, "inverterRunReportController.day")); //"日"
     //           isDayRpt = true;
     //       } else if (ReportConstant.TIMEDIM_MONTH.equals(timeDim)) {
     //           title.append(ResourceBundleUtil.getBizKey(lang, "inverterRunReportController.mouth")); //"月"
     //           dateFormat = DateUtil.DATE_FORMAT_TO_MONTH;
     //       } else {
     //           title.append(ResourceBundleUtil.getBizKey(lang, "inverterRunReportController.year")); //"年
     //           dateFormat = DateUtil.DATE_FORMAT_TO_YEAR;
     //       }
     //       title.append(ResourceBundleUtil.getBizKey(lang, "inverterRunReportController.data")); //报表
            title.append(ResourceBundleUtil.getBizKey(lang, "inverterRunReportController.data")); //报表
            SimpleDateFormat myDateFormat = null;
			if (ReportConstant.TIMEDIM_MONTH.equals(timeDim)) {
				myDateFormat = new SimpleDateFormat(ResourceBundleUtil.getBizKey(lang, "reportController.station.month"));
				title.append(ResourceBundleUtil.getBizKey(lang, "reportDownloadServiceImpl.plant.month"));
			} else if (ReportConstant.TIMEDIM_YEAR.equals(timeDim)) {
				myDateFormat = new SimpleDateFormat(ResourceBundleUtil.getBizKey(lang, "reportController.station.year"));
				title.append(ResourceBundleUtil.getBizKey(lang, "reportDownloadServiceImpl.plant.year"));
			} else {
				myDateFormat = new SimpleDateFormat(ResourceBundleUtil.getBizKey(lang, "reportController.station.day"));
				title.append(ResourceBundleUtil.getBizKey(lang, "reportDownloadServiceImpl.plant.day"));
			}
			myDateFormat.format(new Date(time));
			StringBuilder reportName = new StringBuilder();
			reportName = reportName.append(ResourceBundleUtil.getBizKey(lang, "subarrayRunReportController.sub.array.operation.data.export", myDateFormat.format(new Date(time))));

            // 创建列头
            List<String> headerColumn = new ArrayList<>();
            headerColumn.add(ResourceBundleUtil.getBizKey(lang, "inverterRunReportController.plant.name")); //电站名称
            headerColumn.add(ResourceBundleUtil.getBizKey(lang, "reportController.acquisition.time")); //采集时间
            headerColumn.add(ResourceBundleUtil.getBizKey(lang, "subarrayRunReportController.sub.array.name")); //子阵名称
            headerColumn.add(ResourceBundleUtil.getBizKey(lang, "subarrayRunReportController.installed.capacity")); //装机容量(kW)
            headerColumn.add(ResourceBundleUtil.getBizKey(lang, "inverterRunReportController.generation.capacity")); //发电量(kWh)
            headerColumn.add(ResourceBundleUtil.getBizKey(lang, "subarrayRunReportController.equivalent.utilization.hours")); //等效利用小时(h)
            headerColumn.add(ResourceBundleUtil.getBizKey(lang, "inverterRunReportController.peak.power")); //峰值功率(kW)

            List<Object[]> excelData = new ArrayList<>();
            int columnNum = headerColumn.size();
            for (SubarrayReportVO inverterVo : pageResult) {
                Object[] rowData = new Object[columnNum];
                rowData[0] = inverterVo.getStationName();
                rowData[1] = DateUtil.formatMillsToDateStr(inverterVo.getCollectTime(), dateFormat);
                rowData[2] = inverterVo.getSubarrayName();
                rowData[3] = inverterVo.getRealCapacity();
                rowData[4] = inverterVo.getProductPower();
                rowData[5] = inverterVo.getPerPowerRatio();
                rowData[6] = inverterVo.getPeakPower();
                excelData.add(rowData);
            }

            // 获得excel对象
            HSSFWorkbook excel = ExcelUtil.createCommonExcel(title.toString(), headerColumn, excelData);

            // 下载到客户端
//            reportName.append(DateUtil.formatMillsToDateStr(time, dateFormat));
            String fileName = reportName.toString() + ReportConstant.EXCEL_SUFIX;
            DownloadUtil.downLoadExcel(excel, fileName, response);

            result.setResults("download success!");
            return result;
        } catch (Exception e) {
            logger.error("[exportSubarrayRpt] get error.", e);
        }

        return result;
    }
    
    /**
     * 获取查询参数
     * @param stationCodes
     * @return
     */
    private Map<String, Object> getQueryParams(HttpServletRequest request, String stationCode, 
            Long time, String timeDim){
        Map<String, Object> params = new HashMap<>();
        
        HttpSession session = request.getSession();
        Object obj = session.getAttribute("user");
        if (obj == null){
            return null;
        }
        
        UserInfo user = (UserInfo)obj;
        params.put("userType", user.getType_());
        params.put("userId", user.getId());
        params.put("time", time);
        params.put("timeDim", timeDim);
        params.put("stationCode", stationCode);
        params.put("enterpriseId", user.getEnterpriseId());
        
        return params;
    }
}
