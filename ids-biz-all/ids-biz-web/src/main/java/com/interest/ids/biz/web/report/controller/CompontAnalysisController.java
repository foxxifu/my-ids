package com.interest.ids.biz.web.report.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.interest.ids.biz.web.report.constant.ReportConstant;
import com.interest.ids.biz.web.report.controller.params.CompontParams;
import com.interest.ids.biz.web.utils.DownloadUtil;
import com.interest.ids.biz.web.utils.ExcelUtil;
import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.common.project.il8n.ResourceBundleUtil;
import com.interest.ids.common.project.utils.DateUtil;
import com.interest.ids.commoninterface.service.report.CompontAnalysisService;

/**
 * 组件分析
 * @author xm
 *
 */
@Controller
@RequestMapping("/compontAnalysis")
public class CompontAnalysisController {
	
	@Autowired
	private CompontAnalysisService compontAnalysisService;
	
	private static final Logger log = LoggerFactory.getLogger(CompontAnalysisController.class);
	
	/**
	 * 查询电站月度组件分析数据
	 */
	@ResponseBody
    @RequestMapping(value = "/getStationMouthAnalysis", method = RequestMethod.POST)
	public Response<Map<String,Object>> getStationMouthAnalysis(@RequestBody CompontParams params){
		Response<Map<String,Object>> response = new Response<>();
		if(null != params && StringUtils.isEmpty(params.getStationCode())){
			response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            log.error("get station analysis data fail, stationCode is empty");
            return response;
		}
		Map<String,Object> condition = new HashMap<>();
		condition.put("stationCode", params.getStationCode());
		condition.put("countTime", "countTime");
		condition.put("startTime", DateUtil.getBeginOfMonthTimeByMill(params.getCountTime()));
		condition.put("endTime", DateUtil.getLastOfMonthTimeByMill(params.getCountTime()));
		
		Integer total = compontAnalysisService.selectStationMouthAnalysisCount(condition);
		Integer start = (params.getIndex()-1)*params.getPageSize();
		if(null != total && total < start)
		{
			start = total;
		}
		Map<String,Object> result = null;
		if(null == total || total == 0)
		{
			result = new HashMap<>();
			result.put("chartData", new HashMap<>());
			result.put("tableData", new ArrayList<>());
		}else {
			condition.put("start", start);
			condition.put("pageSize", params.getPageSize());
			result = compontAnalysisService.selectStationMouthAnalysis(condition);
			if(null == result) {
				result = new HashMap<>();
				result.put("chartData", new HashMap<>());
				result.put("tableData", new ArrayList<>());
			}
		}
		result.put("index", params.getIndex());
		result.put("pageSize", params.getPageSize());
		result.put("total", total);
		response.setCode(ResponseConstants.CODE_SUCCESS);
        response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
        response.setResults(result);
        log.info("et station analysis data success, stationCode is " + params.getStationCode());
		return response;
	}
	
	/**
	 * 查询电站子阵级别的月度分析数据
	 */
	@ResponseBody
    @RequestMapping(value = "/getCompontAnalysisMouth", method = RequestMethod.POST)
	public Response<Map<String,Object>> getCompontAnalysisMouth(@RequestBody CompontParams params){
		Response<Map<String,Object>> response = new Response<>();
		if(null != params && null == params.getMatrixId()){
			response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            log.error("get compont analysis data fail, matrixId is empty");
            return response;
		}
		Map<String,Object> condition = new HashMap<>();
		condition.put("matrixId", params.getMatrixId());
		condition.put("startTime", DateUtil.getBeginOfMonthTimeByMill(params.getCountTime()));
		condition.put("endTime", DateUtil.getLastOfMonthTimeByMill(params.getCountTime()));
		condition.put("start", (params.getIndex()-1)*params.getPageSize());
		condition.put("pageSize", params.getPageSize());
		
		
		Integer total = compontAnalysisService.selectCompontAnalysisMouthCount(condition);
		Integer start = (params.getIndex()-1)*params.getPageSize();
		if(null != total && total < start)
		{
			start = total;
		}
		Map<String,Object> result = null;
		if(null == total || total == 0)
		{
			result = new HashMap<>();
			result.put("chartData", new HashMap<>());
			result.put("tableData", new ArrayList<>());
		}else {
			condition.put("start", start);
			condition.put("pageSize", params.getPageSize());
			result = compontAnalysisService.selectCompontAnalysisMouth(condition);
			if(null == result) {
				result = new HashMap<>();
				result.put("chartData", new HashMap<>());
				result.put("tableData", new ArrayList<>());
			}
		}
		
		result.put("index", params.getIndex());
		result.put("pageSize", params.getPageSize());
		result.put("total", total);
		response.setCode(ResponseConstants.CODE_SUCCESS);
        response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
        response.setResults(result);
        log.info("et compont analysis data success, matrixId is " + params.getMatrixId());
		return response;
	}
	
	/**
	 * 电站年报分析
	 */
	@ResponseBody
    @RequestMapping(value = "/getStationYearAnalysis", method = RequestMethod.POST)
	public Response<Map<String,Object>> getStationYearAnalysis(@RequestBody CompontParams params){
		Response<Map<String,Object>> response = new Response<>();
		if(null != params && StringUtils.isEmpty(params.getStationCode())){
			response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            log.error("get station analysis data fail, stationCode is empty");
            return response;
		}
		Map<String,Object> condition = new HashMap<>();
		condition.put("countYearTime", "countYearTime");
		condition.put("stationCode", params.getStationCode());
		condition.put("startTime", DateUtil.getBeginOfYearTimeByMill(params.getCountTime()));
		condition.put("endTime", DateUtil.getLastOfYearTimeByMill(params.getCountTime()));
		
		Integer total = compontAnalysisService.selectStationYearAnalysisCount(condition);
		Integer start = (params.getIndex()-1)*params.getPageSize();
		Map<String,Object> result = null;
		if(null == total ||total == 0)
		{
			result = new HashMap<>();
			result.put("chartData", new HashMap<>());
			result.put("tableData", new ArrayList<>());
		}else {
			condition.put("start", start);
			condition.put("pageSize", params.getPageSize());
			result = compontAnalysisService.selectStationYearAnalysis(condition);
			if(null == result) {
				result = new HashMap<>();
				result.put("chartData", new HashMap<>());
				result.put("tableData", new ArrayList<>());
			}
		}
		
		result.put("index", params.getIndex());
		result.put("pageSize", params.getPageSize());
		result.put("total", total);
		response.setCode(ResponseConstants.CODE_SUCCESS);
        response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
        response.setResults(result);
        log.info("et station analysis data success, stationCode is " + params.getStationCode());
		return response;
	}
	
	/**
	 * 电站子阵报告分析
	 */
	@ResponseBody
    @RequestMapping(value = "/getCompontAnalysisYear", method = RequestMethod.POST)
	public Response<Map<String,Object>> getCompontAnalysisYear(@RequestBody CompontParams params){
		Response<Map<String,Object>> response = new Response<>();
		if(null != params && null == params.getMatrixId()){
			response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            log.error("get compont analysis data fail, matrixId is empty");
            return response;
		}
		Map<String,Object> condition = new HashMap<>();
		condition.put("matrixId", params.getMatrixId());
		condition.put("startTime", DateUtil.getBeginOfYearTimeByMill(params.getCountTime()));
		condition.put("endTime", DateUtil.getLastOfYearTimeByMill(params.getCountTime()));
		condition.put("start", (params.getIndex()-1)*params.getPageSize());
		condition.put("pageSize", params.getPageSize());
		
		Integer total = compontAnalysisService.selectCompontAnalysisYearCount(condition);
		Integer start = (params.getIndex()-1)*params.getPageSize();
		Map<String,Object> result = null;
		if(null == total ||total == 0)
		{
			result = new HashMap<>();
			result.put("chartData", new HashMap<>());
			result.put("tableData", new ArrayList<>());
		}else {
			condition.put("start", start);
			condition.put("pageSize", params.getPageSize());
			result = compontAnalysisService.selectCompontAnalysisYear(condition);
			if(null == result) {
				result = new HashMap<>();
				result.put("chartData", new HashMap<>());
				result.put("tableData", new ArrayList<>());
			}
		}
		
		result.put("index", params.getIndex());
		result.put("pageSize", params.getPageSize());
		result.put("total", total);
		response.setCode(ResponseConstants.CODE_SUCCESS);
        response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
        response.setResults(result);
        log.info("et compont analysis data success, matrixId is " + params.getMatrixId());
		return response;
	}
	
	/**
	 * 组件日分析报告推荐日期
	 */
	@ResponseBody
    @RequestMapping(value = "/getComponetCountTime", method = RequestMethod.POST)
	public Response<List<Long>> getComponetCountTime(@RequestParam Long matrixId){
		Response<List<Long>> response = new Response<>();
		if(null == matrixId){
			response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            log.error("get compont analysis time fail, matrixId is empty");
            return response;
		}
		List<Long> times = compontAnalysisService.selectComponetCountTime(matrixId);
		response.setCode(ResponseConstants.CODE_SUCCESS);
        response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
        response.setResults(times);
        log.info("et compont analysis time success, matrixId is " + matrixId);
		return response;
	}
	
	/**
	 * 组件日子阵分析数据报告
	 */
	@ResponseBody
    @RequestMapping(value = "/getCompontAnalysisDay", method = RequestMethod.POST)
	public Response<Map<String,Object>> getCompontAnalysisDay(@RequestBody CompontParams params) {
		Response<Map<String,Object>> response = new Response<>();
		//测试写死
		// params.setMatrixId(123456L);
		if(null != params && null == params.getMatrixId()){
			response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            log.error("get compont analysis time fail, matrixId is empty");
            return response;
		}
		Map<String,Object> condition = new HashMap<>();
		
		
		condition.put("matrixId", params.getMatrixId());
		condition.put("countTime", "countTime");
		condition.put("startTime", DateUtil.getBeginOfDayTimeByMill(params.getCountTime()));
		condition.put("endTime", DateUtil.getLastOfDayTimeByMill(params.getCountTime()));
		
		Integer total = compontAnalysisService.selectCompontAnalysisDayCount(condition);
		
		Integer maxPv = compontAnalysisService.selectMaxPv(condition);
		condition.put("maxPv", maxPv);
		
		Integer start = (params.getIndex()-1)*params.getPageSize();
		Map<String,Object> result = null;
		if(null == total ||total == 0)
		{
			result = new HashMap<>();
			result.put("chartData", new HashMap<>());
			result.put("tableData", new ArrayList<>());
		}else {
			condition.put("start", start);
			condition.put("pageSize", params.getPageSize());
			result = compontAnalysisService.getCompontAnalysisDay(condition);
			if(null == result) {
				result = new HashMap<>();
				result.put("chartData", new HashMap<>());
				result.put("tableData", new ArrayList<>());
			}
		}
		
		result.put("index", params.getIndex());
		result.put("pageSize", params.getPageSize());
		result.put("total", total);
		result.put("pvCode", maxPv);
		response.setCode(ResponseConstants.CODE_SUCCESS);
        response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
        response.setResults(result);
        log.info("compont analysis time success, matrixId is " + params.getMatrixId());
		return response;
	}
	
	/**
	 * 电站日分析数据
	 */
	@ResponseBody
    @RequestMapping(value = "/getStationAnalysisDay", method = RequestMethod.POST)
	public Response<Map<String,Object>> getStationAnalysisDay(@RequestBody CompontParams params) 
	{
		Response<Map<String,Object>> response = new Response<>();
		if(null != params && StringUtils.isEmpty(params.getStationCode())){
			response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            log.error("get station analysis data fail, stationCode is empty");
            return response;
		}
		Map<String,Object> condition = new HashMap<>();
		condition.put("stationCode", params.getStationCode());
		condition.put("startTime", DateUtil.getBeginOfDayTimeByMill(params.getCountTime()));
		condition.put("endTime", DateUtil.getLastOfDayTimeByMill(params.getCountTime()));
		
		Integer total = compontAnalysisService.selectStationDayAnalysisCount(condition);
		Integer start = (params.getIndex()-1)*params.getPageSize();
		Map<String,Object> result = null;
		if(null == total || total == 0)
		{
			result = new HashMap<>();
			result.put("chartData", new HashMap<>());
			result.put("tableData", new ArrayList<>());
		}else {
			condition.put("start", start);
			condition.put("pageSize", params.getPageSize());
			result = compontAnalysisService.selectStationDayAnalysis(condition);
			if(null == result) {
				result = new HashMap<>();
				result.put("chartData", new HashMap<>());
				result.put("tableData", new ArrayList<>());
			}
		}
		
		result.put("index", params.getIndex());
		result.put("pageSize", params.getPageSize());
		result.put("total", total);
		response.setCode(ResponseConstants.CODE_SUCCESS);
        response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
        response.setResults(result);
        log.info("et station analysis data success, stationCode is " + params.getStationCode());
		return response;
	}
	
	/***
	 * 逆变器离散率分析
	 */
	@ResponseBody
    @RequestMapping(value = "/getInverterDiscreteRate", method = RequestMethod.POST)
	public Response<Map<String,Object>> getInverterDiscreteRate(@RequestBody CompontParams params)
	{
		Response<Map<String,Object>> response = new Response<>();
		if(null != params && StringUtils.isEmpty(params.getStationCode())){
			response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            log.error("get InverterDiscreteRate analysis data fail, stationCode is empty");
            return response;
		}
		
		Map<String,Object> condition = new HashMap<>();
		condition.put("stationCode", params.getStationCode());
		condition.put("startTime", DateUtil.getBeginOfDayTimeByMill(params.getCountTime()));
		condition.put("endTime", DateUtil.getLastOfDayTimeByMill(params.getCountTime()));
		
		Integer total = compontAnalysisService.selectInverterDiscreteRateCount(condition);
		
		//查询最大的组串个数
		condition.put("type", "inverter");
		Integer pvCode = compontAnalysisService.getInverterMaxPv(condition);
		
		Integer start = (params.getIndex()-1)*params.getPageSize();
		Map<String,Object> result = null;
		if(null == total || total == 0)
		{
			result = new HashMap<>();
			result.put("chartData", new HashMap<>());
			result.put("tableData", new ArrayList<>());
		}else {
			condition.put("start", start);
			condition.put("pageSize", params.getPageSize());
			result = compontAnalysisService.selectInverterDiscreteRate(condition);
			if(null == result) {
				result = new HashMap<>();
				result.put("chartData", new HashMap<>());
				result.put("tableData", new ArrayList<>());
			}
		}
		
		result.put("index", params.getIndex());
		result.put("pageSize", params.getPageSize());
		result.put("total", total);
		result.put("pvCode", pvCode);
		response.setCode(ResponseConstants.CODE_SUCCESS);
        response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
        response.setResults(result);
        log.info("et InverterDiscreteRate analysis data success, stationCode is " + params.getStationCode());
		return response;
	}
	
	/***
	 * 直流汇率箱离散率分析
	 */
	@ResponseBody
    @RequestMapping(value = "/getCombinerdcDiscreteRate", method = RequestMethod.POST)
	public Response<Map<String,Object>> getCombinerdcDiscreteRate(@RequestBody CompontParams params)
	{
		Response<Map<String,Object>> response = new Response<>();
		if(null != params && StringUtils.isEmpty(params.getStationCode())){
			response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            log.error("get CombinerdcDiscreteRate analysis data fail, stationCode is empty");
            return response;
		}
		
		Map<String,Object> condition = new HashMap<>();
		condition.put("stationCode", params.getStationCode());
		condition.put("startTime", DateUtil.getBeginOfDayTimeByMill(params.getCountTime()));
		condition.put("endTime", DateUtil.getLastOfDayTimeByMill(params.getCountTime()));
		
		Integer total = compontAnalysisService.selectCombinerdcDiscreteRateCount(condition);
		//查询最大的组串个数
		condition.put("type", "combinerdc");
		Integer pvCode = compontAnalysisService.getInverterMaxPv(condition);
		Integer start = (params.getIndex()-1)*params.getPageSize();
		Map<String,Object> result = null;
		if(null == total || total == 0)
		{
			result = new HashMap<>();
			result.put("chartData", new HashMap<>());
			result.put("tableData", new ArrayList<>());
		}else {
			condition.put("start", start);
			condition.put("pageSize", params.getPageSize());
			result = compontAnalysisService.selectCombinerdcDiscreteRate(condition);
			if(null == result) {
				result = new HashMap<>();
				result.put("chartData", new HashMap<>());
				result.put("tableData", new ArrayList<>());
			}
		}
		
		result.put("index", params.getIndex());
		result.put("pageSize", params.getPageSize());
		result.put("total", total);
		result.put("pvCode", pvCode);
		response.setCode(ResponseConstants.CODE_SUCCESS);
        response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
        response.setResults(result);
        log.info("et CombinerdcDiscreteRate analysis data success, stationCode is " + params.getStationCode());
		return response;
	}
	
	/**
	 * 智慧应用的数据导出 
	 */
	@ResponseBody
    @RequestMapping(value = "/exportData", method = RequestMethod.GET)
	public Response<String> exportData(HttpServletRequest request, HttpServletResponse resp)
	{
		String stationCode = request.getParameter("stationCode");
		String type  = request.getParameter("type");
		String stationName  = request.getParameter("stationName");
		Long time  = Long.parseLong(request.getParameter("time"));
		Response<String> response = new Response<>();
		String lang = request.getParameter(ResponseConstants.REQ_LANG); // 当前请求使用的国际化语言
		if(null != stationCode && StringUtils.isEmpty(stationCode)){
			response.setCode(ResponseConstants.CODE_FAILED);
            response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
            log.error("get exportData data fail, stationCode is empty");
            return response;
		}
		
		List<Object[]> excelData = null;
		Map<String,Object> condition = new HashMap<>();
		String fileName = null;
		SimpleDateFormat dateFormat = null;
		List<String> headerColumn = new ArrayList<>();
		if(null != type && "station_year".equals(type))
		{
			condition.put("stationCode", stationCode);
			condition.put("startTime", DateUtil.getBeginOfYearTimeByMill(time));
			condition.put("endTime", DateUtil.getLastOfYearTimeByMill(time));
			dateFormat = new SimpleDateFormat(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.year")); // yyyy年
			// 拼接文件名称：测试电站2019年组件分析报告 = stationName + dateFormat.format(new Date(time)) + 组件分析报告
			fileName = ResourceBundleUtil.getBizKey(lang, "compontAnalysis.year.title", stationName, dateFormat.format(new Date(time))); // xxx电站xxxx年组件分析报告
			
			headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.year.subarry")); // 子阵名称
			headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.year.cap")); // 装机容量(KW)
			headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.year.pvnum")); // 组串总数(个)
			headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.year.totalPower")); // 当年发电量(kWh)
			headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.year.hiddenLosePower")); // 遮挡损失电量(kWh)
			headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.year.lowerLosePower")); // 低效损失电量(kWh)
			headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.year.faultLosePower")); // 故障损失电量(kWh)
			
			excelData = compontAnalysisService.exportStationYearData(condition);
			
		}else if(null != type && "subarray_year".equals(type))
		{
			condition.put("matrixId", stationCode);
			condition.put("startTime", DateUtil.getBeginOfYearTimeByMill(time));
			condition.put("endTime", DateUtil.getLastOfYearTimeByMill(time));
			dateFormat = new SimpleDateFormat(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.year")); // yyyy年
//			fileName = stationName + dateFormat.format(new Date(time)) + "组件分析报告";
			fileName = ResourceBundleUtil.getBizKey(lang, "compontAnalysis.year.title", stationName, dateFormat.format(new Date(time))); // xxx电站xxxx年组件分析报告
			
			headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.year.statiTime")); // 统计时间
			headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.year.cap")); // 装机容量(KW)
			headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.year.pvnum")); // 组串总数(个)
			headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.year.totalPower")); // 当年发电量(kWh)
			headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.year.hiddenLosePower")); // 遮挡损失电量(kWh)
			headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.year.lowerLosePower")); // 低效损失电量(kWh)
			headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.year.faultLosePower")); // 故障损失电量(kWh)
			
			excelData = compontAnalysisService.exportSubarrayYearData(condition);
			
		}else if(null != type && "station_month".equals(type))
		{
			condition.put("stationCode", stationCode);
			condition.put("startTime", DateUtil.getBeginOfMonthTimeByMill(time));
			condition.put("endTime", DateUtil.getLastOfMonthTimeByMill(time));
			dateFormat = new SimpleDateFormat(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.month")); // yyyy年MM月
//			fileName = stationName + dateFormat.format(new Date(time)) + "组件分析报告";
			fileName = ResourceBundleUtil.getBizKey(lang, "compontAnalysis.year.title", stationName, dateFormat.format(new Date(time))); // xxx电站xxxx年XX月组件分析报告
			
			mouthHeaderColumn(headerColumn, lang);
			
			excelData = compontAnalysisService.exportStationMouthData(condition);
			
			
		}else if(null != type && "subarray_month".equals(type))
		{
			condition.put("matrixId", stationCode);
			condition.put("startTime", DateUtil.getBeginOfMonthTimeByMill(time));
			condition.put("endTime", DateUtil.getLastOfMonthTimeByMill(time));
			dateFormat = new SimpleDateFormat(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.month")); // yyyy年MM月
//			fileName = stationName + dateFormat.format(new Date(time)) + "组件分析报告";
			fileName = ResourceBundleUtil.getBizKey(lang, "compontAnalysis.year.title", stationName, dateFormat.format(new Date(time))); // xxx电站xxxx年XX月组件分析报告
			
			headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.month.devname")); // 设备名称
			headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.month.devname")); // 组串容量
			headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.year.subarry")); // 子阵名称
			headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.year.faultLosePower")); // 故障损失电量(kWh)
			headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.year.lowerLosePower")); // 低效损失电量(kWh)
			headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.year.hiddenLosePower")); // 遮挡损失电量(kWh)
			
			
			excelData = compontAnalysisService.exportSubarrayMouthData(condition);
			
		}else if(null != type && "station_date".equals(type))
		{
			condition.put("stationCode", stationCode);
			condition.put("startTime", DateUtil.getBeginOfDayTimeByMill(time));
			condition.put("endTime", DateUtil.getLastOfDayTimeByMill(time));
			dateFormat = new SimpleDateFormat(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.day")); // yyyy年MM月dd日
//			fileName = stationName + dateFormat.format(new Date(time)) + "组件分析报告";
			fileName = ResourceBundleUtil.getBizKey(lang, "compontAnalysis.year.title", stationName, dateFormat.format(new Date(time))); // xxx电站xxxx年XX月xx日组件分析报告
			
			mouthHeaderColumn(headerColumn, lang);
			
			excelData = compontAnalysisService.exportStationDayData(condition);
			
		}else if(null != type && "subarray_date".equals(type))
		{
			condition.put("matrixId", stationCode);
			condition.put("startTime", DateUtil.getBeginOfDayTimeByMill(time));
			condition.put("endTime", DateUtil.getLastOfDayTimeByMill(time));
			dateFormat = new SimpleDateFormat(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.day")); // yyyy年MM月dd日
//			fileName = stationName + dateFormat.format(new Date(time)) + "组件分析报告";
			fileName = ResourceBundleUtil.getBizKey(lang, "compontAnalysis.year.title", stationName, dateFormat.format(new Date(time))); // xxx电站xxxx年XX月xx日组件分析报告
			Integer maxPv = compontAnalysisService.selectMaxPv(condition);
			headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.month.devname")); // 设备名称
			if(null != maxPv && maxPv > 0)
			{
				for (int i = 1; i <= maxPv; i++) {
					headerColumn.add("pv" + i);
				}
			}
			condition.put("maxPv", maxPv);
			condition.put("normal", ResourceBundleUtil.getBizKey(lang, "compontAnalysisController.normal")); //正常
			excelData = compontAnalysisService.exportSubarrayDayData(condition);
		}else if(null != type && "scatter".equals(type))
		{
			condition.put("stationCode", stationCode);
			condition.put("startTime", DateUtil.getBeginOfDayTimeByMill(time));
			condition.put("endTime", DateUtil.getLastOfDayTimeByMill(time));
			dateFormat = new SimpleDateFormat(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.day")); // yyyy年MM月dd日
//			fileName = stationName + dateFormat.format(new Date(time)) + "逆变器分析报告";
			fileName = ResourceBundleUtil.getBizKey(lang, "compontAnalysis.day.title", stationName, dateFormat.format(new Date(time))); // xxx电站xx年xx月xx日逆变器分析报告
			condition.put("type", "inverter");
			Integer maxPv = compontAnalysisService.getInverterMaxPv(condition);
			
			headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.month.devname")); // 设备名称
			headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.day.power")); // 日发电量(kWh)
			headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.day.effect")); // 转换效率(%)
			headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.day.eqhour")); // 等效利用小时数(h)
			headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.day.maxchpower")); // 最大交流功率(kW)
			headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.day.pvdis")); // 组串离散率(%)
			headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.day.isayas")); // 是否分析
			if(null != maxPv && maxPv > 0)
			{
				String pviModel = ResourceBundleUtil.getBizKey(lang, "compontAnalysis.day.avgpvandi"); // {0}平均电压(V)/电流(A)
				for (int i = 1; i <= maxPv; i++) {
					headerColumn.add(StringUtils.replace(pviModel, "{0}", "pv" + i)); // "pv" + i + "平均电压(V)/电流(A)"
				}
			}
			condition.put("maxPv", maxPv);
			// 这里需要将对于转换了的国际化给条件转换
			setGjhData(condition, lang); // 导出内容文字的国际化处理
			excelData = compontAnalysisService.exportCcatterData(condition);
		}else if(null != type && "scatterDC".equals(type))
		{
			condition.put("stationCode", stationCode);
			condition.put("startTime", DateUtil.getBeginOfDayTimeByMill(time));
			condition.put("endTime", DateUtil.getLastOfDayTimeByMill(time));
			dateFormat = new SimpleDateFormat(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.day")); // yyyy年MM月dd日
//			fileName = stationName + dateFormat.format(new Date(time)) + "直流汇率箱分析报告";
			fileName = ResourceBundleUtil.getBizKey(lang, "compontAnalysis.day.dctitle", stationName, dateFormat.format(new Date(time))); // xx电站xx年xx月xx日直流汇流箱分析报告
			condition.put("type", "combinerdc");
			Integer maxPv = compontAnalysisService.getInverterMaxPv(condition);
			
			headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.month.devname")); // 设备名称
			headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.day.pvdis")); // 组串离散率(%)
			headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.day.isayas")); // 是否分析
			headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.day.avgpv")); // 平均电压(V)
			if(null != maxPv && maxPv > 0)
			{
				String pviModel = ResourceBundleUtil.getBizKey(lang, "compontAnalysis.day.avgpvandi"); // {0}平均电压(V)/电流(A)
				for (int i = 1; i <= maxPv; i++) {
					headerColumn.add(StringUtils.replace(pviModel, "{0}", "pv" + i)); // "pv" + i + "平均电压(V)/电流(A)"
				}
			}
			
			condition.put("maxPv", maxPv);
			// 未分析，异常，已分析的国际化处理
			setGjhData(condition, lang); // 导出内容文字的国际化处理
			excelData = compontAnalysisService.exportScatterDCData(condition);
		}
		 // 获得excel对象
        HSSFWorkbook excel = ExcelUtil.createCommonExcel(fileName, headerColumn, excelData);

        // 下载到客户端
        try {
			DownloadUtil.downLoadExcel(excel, fileName + ReportConstant.EXCEL_SUFIX, resp);
		} catch (Exception e) {
			e.printStackTrace();
		}

        response.setResults("download success!");
		return response;
	}
	/**
	 * 对于分析和未分析的国际化处理
	 * @param condition
	 */
	private void setGjhData(Map<String,Object> condition, String lang) {
		// 这里是为了国际化翻译sql查询出来的文字做的一个映射关系(文字-->国际化的对应文字)
		condition.put("未分析", ResourceBundleUtil.getBizKey(lang, "compontAnalysis.wfx")); // 未分析
		condition.put("异常", ResourceBundleUtil.getBizKey(lang, "compontAnalysis.yc")); // 异常
		condition.put("已分析", ResourceBundleUtil.getBizKey(lang, "compontAnalysis.yfx")); // 已分析
	}

	private void mouthHeaderColumn(List<String> headerColumn, String lang) {
		headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.year.subarry")); // 子阵名称
		headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.month.subcap")); // 子阵装机容量(KW)
		headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.year.pvnum")); // 组串总数(个)
		headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.month.power")); // 当月发电量(kWh)
		headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.month.hiddenpvnum")); // 遮挡组串数(个)
		headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.year.hiddenLosePower")); // 遮挡损失电量(kWh)
		headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.month.lowerpvnum")); // 低效组串数(个)
		headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.year.lowerLosePower")); // 低效损失电量(kWh)
		headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.month.faultpvnum")); // 故障组串数(个)
		headerColumn.add(ResourceBundleUtil.getBizKey(lang, "compontAnalysis.year.faultLosePower")); // 故障损失电量(kWh)
	}
}
