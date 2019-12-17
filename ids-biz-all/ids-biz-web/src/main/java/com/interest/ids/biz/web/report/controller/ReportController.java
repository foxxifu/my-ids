package com.interest.ids.biz.web.report.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.interest.ids.biz.web.constant.UrlConstant;
import com.interest.ids.biz.web.report.constant.ReportConstant;
import com.interest.ids.biz.web.report.controller.params.InverterDetailParams;
import com.interest.ids.biz.web.report.controller.params.ReportParams;
import com.interest.ids.biz.web.report.service.ReportDownloadService;
import com.interest.ids.biz.web.report.service.ReportService;
import com.interest.ids.biz.web.report.vo.DevFaultReportVO;
import com.interest.ids.biz.web.report.vo.StationReportVO;
import com.interest.ids.biz.web.utils.CommonUtils;
import com.interest.ids.biz.web.utils.DownloadUtil;
import com.interest.ids.biz.web.utils.ExcelUtil;
import com.interest.ids.common.project.bean.PageData;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.common.project.il8n.ResourceBundleUtil;
import com.interest.ids.common.project.support.Response;
import com.interest.ids.common.project.support.Responses;
import com.interest.ids.common.project.utils.DateUtil;
import com.interest.ids.commoninterface.service.station.StationInfoMService;

/**
 * @Author: sunbjx
 * @Description:
 * @Date Created in 21:30 2018/1/21
 * @Modified By:
 */
@RestController
@RequestMapping("/produceMng")
public class ReportController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ReportController.class);

	@Autowired
	private ReportService reportService;
	@Autowired
	private ReportDownloadService reportDownloadService;
	@Autowired
	private StationInfoMService stationInfoService;

	@RequestMapping(value = "/listDeviceTroubleRpt", method = RequestMethod.POST)
	@ResponseBody
	public Response devFaultRpt(@RequestBody ReportParams params,
			HttpServletRequest request) {

		UserInfo user = CommonUtils.getSigninUser(request);
		if (null == user) {
			return Responses.FAILED().setResults(
					ResponseConstants.CODE_FAILED_SESSION_FAILURE);
		}
		params.setUserId(user.getId());
		params.setUserType(user.getType_());

		List<DevFaultReportVO> result = reportService.listDevFault(params.init(
				params.getTimeDim(), params.getTime()));
		if (result != null) {
			PageData<DevFaultReportVO> page = new PageData<DevFaultReportVO>();
			page.setList(result);
			page.setCount(0 == result.size() ? 0 : new PageInfo<>(result).getTotal());
			return Responses.SUCCESS().setResults(page);
		}
		return Responses.FAILED();
	}

	@RequestMapping(value = "/listStationRpt", method = RequestMethod.POST)
	@ResponseBody
	public Response stationRpt(@RequestBody ReportParams params,
			HttpServletRequest request) {

		PageData<StationReportVO> page = new PageData<StationReportVO>();
		UserInfo user = CommonUtils.getSigninUser(request);
		if (null == user) {
			return Responses.FAILED().setResults(ResponseConstants.CODE_FAILED_SESSION_FAILURE);
		}
		// 判断需要查询的电站数量，单站查询当前时间维度下维度的数据，多电站则查询当前维度的电站对比
		if(params.getStationCodes() == null || params.getStationCodes().size() == 0){
			// 根据用户查询当前用户下管理的电站数量
			List<StationInfoM> userStations = this.stationInfoService.getStationByUser(user);
			List<String> stationCodes = new ArrayList<String>();
			if(userStations != null && userStations.size() != 0){
				for(StationInfoM station : userStations){
					stationCodes.add(station.getStationCode());
				}
			}
			params.setStationCodes(stationCodes);
		}
		if(params.getStationCodes() == null || params.getStationCodes().size() == 0){
			// 无任何电站可供查询
			page.setList(new ArrayList<StationReportVO>());
			page.setCount(0L);
			return Responses.SUCCESS().setResults(page);
		}else if(params.getStationCodes().size() == 1){
			params.setReportDataType("1");
		}else{
			params.setReportDataType("2");
		}
		// 初始化查询数据
		params.init(params.getTimeDim(), params.getTime());
		List<StationReportVO> result = reportService.listStation(params);
		if (result != null) {
			page.setList(result);
			page.setCount(0 == result.size() ? 0 : new PageInfo<>(result).getTotal());
		}else{
			page.setList(new ArrayList<StationReportVO>());
			page.setCount(0L);
		}
		Response response = Responses.SUCCESS().setResults(page);
		response.setMessage(params.getReportDataType());
		return response;
	}

	/**
	 * dto可与其他dto 以组合的方式扩展达到支持的文件格式 通用下载
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/report/download/{type}", method = RequestMethod.GET)
	public Response reportDownload(
			@RequestParam(required = false) String deviceTypeId,
			@RequestParam(required = false) List<String> stationCodes,
			@RequestParam(required = false) String timeDim,
			@RequestParam(required = false) Long time,
			@PathVariable String type, HttpServletRequest request,
			HttpServletResponse response) {
		String lang = request.getParameter(ResponseConstants.REQ_LANG);
		ReportParams params = new ReportParams();
		params.init(timeDim, time);
		UserInfo user = CommonUtils.getSigninUser(request);
		if (null == user){
			return Responses.FAILED().setResults(
					ResponseConstants.CODE_FAILED_SESSION_FAILURE);
		}
		// 判断需要查询的电站数量，单站查询当前时间维度下维度的数据，多电站则查询当前维度的电站对比
		if(stationCodes == null || stationCodes.size() == 0){
			// 根据用户查询当前用户下管理的电站数量
			List<StationInfoM> userStations = this.stationInfoService.getStationByUser(user);
			stationCodes = new ArrayList<String>();
			if(userStations != null && userStations.size() != 0){
				for(StationInfoM station : userStations){
					stationCodes.add(station.getStationCode());
				}
			}
		}
		params.setStationCodes(stationCodes);
		// 初始化查询数据
		params.init(params.getTimeDim(), params.getTime());
		if(params.getStationCodes() == null || params.getStationCodes().size() == 0){
			// 无任何电站可供查询
		}else if(params.getStationCodes().size() == 1){
			params.setReportDataType("1");
		}else{
			params.setReportDataType("2");
		}
		params.setUserId(user.getId());
		params.setUserType(user.getType_());
		params.setDeviceTypeId(deviceTypeId);
		params.setStationCodes(stationCodes);
		params.setTimeDim(timeDim);
		params.setTime(time);
		params.setDownload(true);
		// xxx报表2019年05月06日 ==> {0}yyyy年MM月dd日
//		String dateFormat = DateUtil.DATE_FORMAT_TO_DAY;
		StringBuilder fileName = new StringBuilder();
		// xxx报表2019年05月
		// xxx报表2019年
//		fileName = ResourceBundleUtil.getBizKey(lang, "", replaceData)
//		if (ReportConstant.TIMEDIM_MONTH.equals(timeDim)) {
//			dateFormat = DateUtil.DATE_FORMAT_TO_MONTH;
//		} else if (ReportConstant.TIMEDIM_YEAR.equals(timeDim)) {
//			dateFormat = DateUtil.DATE_FORMAT_TO_YEAR;
//		}
//		String date = DateUtil.formatMillsToDateStr(time, dateFormat);
//		if(date.length() == 4){
//			date = date + ("-");//年
//		}else if(date.length() == 6){
//			date = date.substring(0, 4) + ("-") + date.substring(4, 6) + ("-"); // 年 月
//		}else{
//			date = date.substring(0, 4) + ("-") + date.substring(4, 6) + ("-") + date.substring(6, 8) + ("-"); // 年 月  日
//		}
		
//		fileName.append(date).append(ReportConstant.EXCEL_SUFIX);
		HSSFWorkbook wb = null;
		try {
			switch (type) {
			case UrlConstant.DEV_ALARM:
				fileName.insert(0, ResourceBundleUtil.getBizKey(lang, "reportController.fault.report")); //"设备故障报表-"
				Map<String, String> in18Map=new HashMap<>();
				in18Map.put("equipmentFailure", ResourceBundleUtil.getBizKey(lang, "reportController.equipment.failure"));
				in18Map.put("devTypeName", ResourceBundleUtil.getBizKey(lang, "reportController.dev.type.name"));
				in18Map.put("occurrenceFrequency", ResourceBundleUtil.getBizKey(lang, "reportController.occurrence.frequency"));
				in18Map.put("level", ResourceBundleUtil.getBizKey(lang, "reportController.level"));
				in18Map.put("maximumDurationFailure", ResourceBundleUtil.getBizKey(lang, "reportController.maximum.duration.failure"));
				in18Map.put("mostFrequencyFaults", ResourceBundleUtil.getBizKey(lang, "reportController.most.frequency.faults"));
				in18Map.put("faultDuration", ResourceBundleUtil.getBizKey(lang, "reportController.fault.duration"));
				in18Map.put("day", ResourceBundleUtil.getBizKey(lang, "reportDownloadServiceImpl.day"));
				in18Map.put("month", ResourceBundleUtil.getBizKey(lang, "reportDownloadServiceImpl.month"));
				in18Map.put("year", ResourceBundleUtil.getBizKey(lang, "reportDownloadServiceImpl.year"));
				in18Map.put("reportForm", ResourceBundleUtil.getBizKey(lang, "reportDownloadServiceImpl.report.form"));
				wb = reportDownloadService.createDevAlarmExcel(params,
						reportService.listDevFault(params),in18Map);
				break;

			case UrlConstant.STATION_RUNNING:
				// 电站运行报表-yyyy年MM月dd日
				SimpleDateFormat myDateFormat = null;
				if (ReportConstant.TIMEDIM_MONTH.equals(timeDim)) {
					myDateFormat = new SimpleDateFormat(ResourceBundleUtil.getBizKey(lang, "reportController.station.month"));
				} else if (ReportConstant.TIMEDIM_YEAR.equals(timeDim)) {
					myDateFormat = new SimpleDateFormat(ResourceBundleUtil.getBizKey(lang, "reportController.station.year"));
				} else {
					myDateFormat = new SimpleDateFormat(ResourceBundleUtil.getBizKey(lang, "reportController.station.day"));
				}
				myDateFormat.format(new Date(time));
				fileName = fileName.append(ResourceBundleUtil.getBizKey(lang, "reportController.station.title", myDateFormat.format(new Date(time))));
//				fileName.insert(0, ResourceBundleUtil.getBizKey(lang, "reportController.operation.report.plant")); //"电站运行报表-"
				Map<String, String> map=new HashMap<>();
				map.put("plantRunning", ResourceBundleUtil.getBizKey(lang, "reportController.plant.running"));
				map.put("plantName", ResourceBundleUtil.getBizKey(lang, "reportDownloadServiceImpl.plantName"));
				map.put("acquisitionTime", ResourceBundleUtil.getBizKey(lang, "reportDownloadServiceImpl.acquisition.time"));
				map.put("radiationDose", ResourceBundleUtil.getBizKey(lang, "reportDownloadServiceImpl.radiation.dose"));
				map.put("theoreticalPowerGeneration", ResourceBundleUtil.getBizKey(lang, "reportDownloadServiceImpl.the.oretical.power.generation"));
				map.put("generationCapacity", ResourceBundleUtil.getBizKey(lang, "reportDownloadServiceImpl.generation.capacity"));
				map.put("profit", ResourceBundleUtil.getBizKey(lang, "reportDownloadServiceImpl.profit"));
				map.put("equivalentUtilizationHours", ResourceBundleUtil.getBizKey(lang, "reportDownloadServiceImpl.equivalent.utilization.hours"));
				map.put("ongridEnergy", ResourceBundleUtil.getBizKey(lang, "reportDownloadServiceImpl.ongrid.energy"));
				map.put("selfConsumption", ResourceBundleUtil.getBizKey(lang, "reportDownloadServiceImpl.self.consumption"));
				map.put("day", ResourceBundleUtil.getBizKey(lang, "reportDownloadServiceImpl.plant.day"));
				map.put("month", ResourceBundleUtil.getBizKey(lang, "reportDownloadServiceImpl.plant.month"));
				map.put("year", ResourceBundleUtil.getBizKey(lang, "reportDownloadServiceImpl.plant.year"));
				map.put("reportForm", ResourceBundleUtil.getBizKey(lang, "reportDownloadServiceImpl.report.form"));
				wb = reportDownloadService.createStationRuningExcel(params,
						reportService.listStation(params),map);
				break;
			}
			fileName.append(ReportConstant.EXCEL_SUFIX);
			// 下载到客户端
			DownloadUtil.downLoadExcel(wb, fileName.toString(), response);
			return Responses.SUCCESS();

		} catch (Exception e) {
			LOGGER.error("Download error: ", e);
			return Responses.FAILED();
		}
	}
	@RequestMapping(value = "/getYestodayTime", method = RequestMethod.POST)
	@ResponseBody
	public Response getYestodayTime() {
		Long yestodayTime = DateUtil.getBeginOfDayTimeByMill(System.currentTimeMillis()-86400000);
		return Responses.SUCCESS().setResults(yestodayTime);
	}
	
	@RequestMapping(value = "/listInverterDetailRpt", method = RequestMethod.POST)
	@ResponseBody
	public Response InverterDetailRpt(@RequestBody InverterDetailParams params,
			HttpServletRequest request) {
		LOGGER.info("/listInverterDetailRpt begin");
		Map<String, Object> result = new HashMap<String, Object>();
		Response response = new Response();
		try {
			result = reportService.listInverterDetailRpt(params);
			response.setCode(1);
			response.setMessage("");
			response.setResults(result);
		} catch (Exception e) {
			LOGGER.error("/listInverterDetailRpt error: ", e);
			response.setCode(-1);
			response.setMessage("");
			response.setResults(null);
		}
		LOGGER.info("/listInverterDetailRpt end");
		return response;
	}
	
	@RequestMapping(value = "/exportInverterDetailRpt", method = RequestMethod.GET)
	@ResponseBody
	public Response exportInverterDetail(@RequestParam(name = "stationCode", required = true) String stationCode,
			@RequestParam(name = "stationName", required = true) String stationName,
			@RequestParam(name = "beginTime", required = true) Long beginTime,
			@RequestParam(name = "endTime", required = true) Long endTime,
			@RequestParam(name = "deviceIds", required = true) List<String> deviceIds,
			HttpServletResponse response, HttpServletRequest request) {
		String lang = request.getParameter(ResponseConstants.REQ_LANG);
		InverterDetailParams params = new InverterDetailParams();
		params.setStationCode(stationCode);
		params.setBeginTime(beginTime);
		params.setEndTime(endTime);
		params.setDeviceIds(deviceIds);
		String title = stationName + ResourceBundleUtil.getBizKey(lang, "reportController.detailed.operation.report.inverter"); //"逆变器详细运行报表"
		List<Object[]> excelData = reportService.exportInverterDetailRpt(params);
		//"设备名称","采集时间","有功功率(kW)","温度(℃)","电网频率(Hz)",
		//"A相电压(V)","B相电压(V)","C相电压(V)","A相电流(A)","B相电流(A)","C相电流(A)",
		//"PV1电压(V)","PV2电压(V)","PV3电压(V)","PV4电压(V)","PV5电压(V)","PV6电压(V)","PV7电压(V)",
		//"PV8电压(V)","PV9电压(V)","PV10电压(V)","PV11电压(V)","PV12电压(V)","PV13电压(V)","PV14电压(V)",
		//"PV1电流(A)","PV2电流(A)","PV3电流(A)","PV4电流(A)","PV5电流(A)","PV6电流(A)","PV7电流(A)",
		//"PV8电流(A)","PV9电流(A)","PV10电流(A)","PV11电流(A)","PV12电流(A)","PV13电流(A)","PV14电流(A)"
		List<String> headerColumn = Arrays.asList(
				ResourceBundleUtil.getBizKey(lang, "reportController.dev.name"),
				ResourceBundleUtil.getBizKey(lang, "reportController.acquisition.time"),
				ResourceBundleUtil.getBizKey(lang, "reportController.dev.day.capacity"),
				ResourceBundleUtil.getBizKey(lang, "reportController.dev.total.capacity"),
				ResourceBundleUtil.getBizKey(lang, "reportController.active.power"),
				ResourceBundleUtil.getBizKey(lang, "reportController.temperature"),
				ResourceBundleUtil.getBizKey(lang, "reportController.grid.frequency"),
				ResourceBundleUtil.getBizKey(lang, "reportController.phase.aVoltage"),
				ResourceBundleUtil.getBizKey(lang, "reportController.phase.bVoltage"),
				ResourceBundleUtil.getBizKey(lang, "reportController.phase.cVoltage"),
				ResourceBundleUtil.getBizKey(lang, "reportController.phase.aCurrent"),
				ResourceBundleUtil.getBizKey(lang, "reportController.phase.bCurrent"),
				ResourceBundleUtil.getBizKey(lang, "reportController.phase.cCurrent"),
				ResourceBundleUtil.getBizKey(lang, "reportController.pV1.voltage"),
				ResourceBundleUtil.getBizKey(lang, "reportController.pV2.voltage"),
				ResourceBundleUtil.getBizKey(lang, "reportController.pV3.voltage"),
				ResourceBundleUtil.getBizKey(lang, "reportController.pV4.voltage"),
				ResourceBundleUtil.getBizKey(lang, "reportController.pV5.voltage"),
				ResourceBundleUtil.getBizKey(lang, "reportController.pV6.voltage"),
				ResourceBundleUtil.getBizKey(lang, "reportController.pV7.voltage"),
				ResourceBundleUtil.getBizKey(lang, "reportController.pV8.voltage"),
				ResourceBundleUtil.getBizKey(lang, "reportController.pV9.voltage"),
				ResourceBundleUtil.getBizKey(lang, "reportController.pV10.voltage"),
				ResourceBundleUtil.getBizKey(lang, "reportController.pV11.voltage"),
				ResourceBundleUtil.getBizKey(lang, "reportController.pV12.voltage"),
				ResourceBundleUtil.getBizKey(lang, "reportController.pV13.voltage"),
				ResourceBundleUtil.getBizKey(lang, "reportController.pV14.voltage"),
				ResourceBundleUtil.getBizKey(lang, "reportController.pV1.current"),
				ResourceBundleUtil.getBizKey(lang, "reportController.pV2.current"),
				ResourceBundleUtil.getBizKey(lang, "reportController.pV3.current"),
				ResourceBundleUtil.getBizKey(lang, "reportController.pV4.current"),
				ResourceBundleUtil.getBizKey(lang, "reportController.pV5.current"),
				ResourceBundleUtil.getBizKey(lang, "reportController.pV6.current"),
				ResourceBundleUtil.getBizKey(lang, "reportController.pV7.current"),
				ResourceBundleUtil.getBizKey(lang, "reportController.pV8.current"),
				ResourceBundleUtil.getBizKey(lang, "reportController.pV9.current"),
				ResourceBundleUtil.getBizKey(lang, "reportController.pV10.current"),
				ResourceBundleUtil.getBizKey(lang, "reportController.pV11.current"),
				ResourceBundleUtil.getBizKey(lang, "reportController.pV12.current"),
				ResourceBundleUtil.getBizKey(lang, "reportController.pV13.current"),
				ResourceBundleUtil.getBizKey(lang, "reportController.pV14.current"));
		
		// 获得excel对象
        HSSFWorkbook excel = ExcelUtil.createCommonExcel(title.toString(), headerColumn, excelData);

        StringBuilder reportName = new StringBuilder(stationName + ResourceBundleUtil.getBizKey(lang, "reportController.detailed.operation.report.inverter2")); //"逆变器详细运行报表-"
        String dateFormat = DateUtil.DATE_FORMAT_TO_DAY;
        
        // 下载到客户端
        reportName.append(DateUtil.formatMillsToDateStr(beginTime, dateFormat));
        String fileName = reportName.toString() + ReportConstant.EXCEL_SUFIX;
        try {
			DownloadUtil.downLoadExcel(excel, fileName, response);
			return Responses.SUCCESS();
		} catch (Exception e) {
			LOGGER.error("Download error: ", e);
			return Responses.FAILED();
		}
	}
	
	@RequestMapping(value = "/listInverterDevices", method = RequestMethod.POST)
	@ResponseBody
	public Response InverterDevices(@RequestBody InverterDetailParams params,
			HttpServletRequest request) {
		return new Response(1,"", reportService.selectDevices(params));
	}
}
