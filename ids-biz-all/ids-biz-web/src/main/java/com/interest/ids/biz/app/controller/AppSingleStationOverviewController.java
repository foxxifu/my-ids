package com.interest.ids.biz.app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.common.project.enums.AlarmLevelEnum;
import com.interest.ids.commoninterface.service.station.StationInfoMService;
import com.interest.ids.commoninterface.service.stationoverview.ISingleStationOverviewService;

/**
 * 单电站总览
 * 
 * @author claude
 *
 */
@Controller
@RequestMapping("/app/singleStation")
public class AppSingleStationOverviewController {

	private Logger log = LoggerFactory.getLogger(AppSingleStationOverviewController.class);
	
	@Autowired
	private ISingleStationOverviewService singleStationOverviewService;
	
	@Autowired
	private StationInfoMService stationInfoMService;

	/**
	 * 单电站总览查询电站概况信息
	 * 
	 * @param params
	 *            电站编号
	 * @return Response<Map<String, Object>>
	 */
	@ResponseBody
	@RequestMapping(value = "/getSingleStationInfo", method = RequestMethod.POST)
	public Response<Map<String, Object>> getSingleStationInfo(
			@RequestBody JSONObject params) {
		String stationCode = params.getString("stationCode");
		Response<Map<String, Object>> response = new Response<Map<String, Object>>();
		try {
			Assert.notNull(stationCode);// 通过spring的Assert判空抛出异常
			Map<String, Object> result = this.singleStationOverviewService
					.getSingleStationInfo(stationCode);
			response.setCode(ResponseConstants.CODE_SUCCESS);
			response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
			response.setResults(result);
		} catch (Exception e) {
			log.error("App getSingleStationInfo faild. station code is : "
					+ stationCode + " . error msg : " + e.getMessage());
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		return response;
	}

	/**
	 * 单电站总览查询电站实时数据
	 * 
	 * @param params
	 *            电站编号
	 * @return Response<Map<String, Object>>
	 */
	@ResponseBody
	@RequestMapping(value = "/getSingleStationCommonData", method = RequestMethod.POST)
	public Response<Map<String, Object>> getSingleStationCommonData(
			@RequestBody JSONObject params) {
		String stationCode = params.getString("stationCode");
		Response<Map<String, Object>> response = new Response<Map<String, Object>>();
		try {
			Assert.notNull(stationCode);// 通过spring的Assert判空抛出异常
			Map<String, Object> result = this.singleStationOverviewService
					.getSingleStationRealtimeKPI(stationCode);
			response.setCode(ResponseConstants.CODE_SUCCESS);
			response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
			response.setResults(result);
		} catch (Exception e) {
			log.error("App getSingleStationCommonData faild. station code is : "
					+ stationCode + " . error msg : " + e.getMessage());
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		return response;
	}

	/**
	 * 单电站总览查询发电量及收益
	 * 
	 * @param params
	 *            stationCode：电站编号；queryType：month、year、allYear
	 * @return Response<Map<String, Object>>
	 */
	@ResponseBody
	@RequestMapping(value = "/getSingleStationPowerAndIncome", method = RequestMethod.POST)
	public Response<List<Map<String, Object>>> getSingleStationPowerAndIncome(
			@RequestBody JSONObject params) {
		String stationCode = params.getString("stationCode");
		String queryType = params.getString("queryType");// month、year、allYear
		String queryTime = null;
		if (params.containsKey("queryTime") && !StringUtils.isEmpty(params.getString("queryTime"))) {
			queryTime = params.getString("queryTime");
		}
		Response<List<Map<String, Object>>> response = new Response<List<Map<String, Object>>>();
		try {
			Assert.notNull(stationCode);// 通过spring的Assert判空抛出异常
			Assert.notNull(queryType);// 通过spring的Assert判空抛出异常
			List<Map<String, Object>> result = this.singleStationOverviewService.getSingleStationPowerAndIncome(stationCode, queryType, queryTime);
			response.setCode(ResponseConstants.CODE_SUCCESS);
			response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
			response.setResults(result);
		} catch (Exception e) {
			log.error("App getSingleStationInfo faild. station code is : "
					+ stationCode + " . error msg : " + e.getMessage());
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		return response;
	}

	/**
	 * 获取单电站告警分类统计
	 * 
	 * @param params
	 *            电站id
	 * @return Response<Map<String, Object>>
	 */
	@ResponseBody
	@RequestMapping(value = "/getSingleStationAlarmStatistics", method = RequestMethod.POST)
	public Response<Map<String, Object>> getSingleStationAlarmStatistics(
			@RequestBody JSONObject params) {
		Response<Map<String, Object>> response = new Response<Map<String, Object>>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String stationCode = params.getString("stationCode");
		try {
			Assert.notNull(stationCode);// 通过spring的Assert判空抛出异常
			List<Map<String, Object>> result = this.singleStationOverviewService
					.getSingleStationAlarmStatistics(stationCode);
			if (result != null && result.size() > 0) {
				for (Map<String, Object> map : result) {
					if(StringUtils.isEmpty(map.get("levelId"))){
						continue;
					}
					resultMap.put(
							AlarmLevelEnum.getMack(Integer.valueOf(map.get("levelId").toString())),
							map.get("alarmCount"));
				}
			}
			response.setCode(ResponseConstants.CODE_SUCCESS);
			response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
			response.setResults(resultMap);
		} catch (Exception e) {
			log.error("App getSingleStationInfo faild. station code is : "
					+ stationCode + " . error msg : " + e.getMessage());
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		return response;
	}

	/**
	 * 获取单电站日负荷曲线
	 * 
	 * @param params
	 *            电站id
	 * @return Response<List<Map<String, Object>>>
	 */
	@ResponseBody
	@RequestMapping(value = "/getSingleStationActivePower", method = RequestMethod.POST)
	public Response<List<Map<String, Object>>> getSingleStationActivePower(
			@RequestBody JSONObject params) {
		Response<List<Map<String, Object>>> response = new Response<List<Map<String, Object>>>();
		String stationCode = params.getString("stationCode");
		try {
			Assert.notNull(stationCode);// 通过spring的Assert判空抛出异常
			List<Map<String, Object>> result = this.singleStationOverviewService
					.getSingleStationActivePower(stationCode);
			response.setCode(ResponseConstants.CODE_SUCCESS);
			response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
			response.setResults(result);
		} catch (Exception e) {
			log.error("App getSingleStationActivePower faild. station code is : "
					+ stationCode + " . error msg : " + e.getMessage());
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		return response;
	}
	
	/**
	 * 获取单电站
	 * 
	 * @param params
	 *            电站id
	 * @return Response<List<Map<String, Object>>>
	 */
	@ResponseBody
	@RequestMapping(value = "/getStationInfo", method = RequestMethod.POST)
	public Response<StationInfoM> getStationInfo(@RequestBody JSONObject params) {
		Response<StationInfoM> response = new Response<StationInfoM>();
		String stationCode = params.getString("stationCode");
		try {
			Assert.notNull(stationCode);// 通过spring的Assert判空抛出异常
			StationInfoM result = this.stationInfoMService.getStationByCode(stationCode);
			response.setCode(ResponseConstants.CODE_SUCCESS);
			response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
			response.setResults(result);
		} catch (Exception e) {
			log.error("App getStationInfo faild. station code is : "
					+ stationCode + " . error msg : " + e.getMessage());
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		return response;
	}
	
	/**
	 * 电站总览社会贡献
	 * 
	 * @param params
	 *            JSONObject
	 * @return Response<Map<String, Object>>
	 */
	@ResponseBody
	@RequestMapping(value = "/getContribution", method = RequestMethod.POST)
	public Response<Map<String, Object>> getContribution(@RequestBody JSONObject params) {
		String stationCode = params.getString("stationCode");
		Response<Map<String, Object>> response = new Response<Map<String, Object>>();
		Map<String, Object> resultMap = null;
		try {
			resultMap = this.singleStationOverviewService.getContribution(stationCode);
		} catch (Exception e) {
			log.error("getContribution error. error msg : " + e.getMessage());
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		response.setCode(ResponseConstants.CODE_SUCCESS);
		response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		response.setResults(resultMap);
		return response;
	}
	
}
