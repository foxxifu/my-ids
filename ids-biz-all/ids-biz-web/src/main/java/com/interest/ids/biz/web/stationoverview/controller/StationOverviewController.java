package com.interest.ids.biz.web.stationoverview.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.interest.ids.biz.web.utils.CommonUtils;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.common.project.enums.AlarmLevelEnum;
import com.interest.ids.common.project.utils.DateUtil;
import com.interest.ids.commoninterface.service.stationoverview.IStationOverviewService;

/**
 * 电站总览controller
 * 
 * @author claude
 *
 */
@Controller
@RequestMapping("/stationOverview")
public class StationOverviewController {

	private static final Logger log = LoggerFactory.getLogger(StationOverviewController.class);

	@Autowired
	private IStationOverviewService stationOverviewService;

	/**
	 * 电站总览电站分布功能
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return Response<Map<String, Object>>
	 */
	@ResponseBody
	@RequestMapping(value = "/getStationDistribution", method = RequestMethod.POST)
	public Response<Map<String, Object>> getStationDistribution(HttpServletRequest request) {
		Response<Map<String, Object>> response = new Response<Map<String, Object>>();
		Map<String, Object> resultMap = null;
		try {
			Map<String, Object> userInfo = CommonUtils.getCurrentUser(request);
			resultMap = this.stationOverviewService.getStationDistribution(userInfo);
		} catch (Exception e) {
			log.error("getStationDistribution error. error msg : " + e.getMessage());
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		response.setCode(ResponseConstants.CODE_SUCCESS);
		response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		response.setResults(resultMap);
		return response;
	}

	/**
	 * 电站总览发电量及收益功能
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return Response<Map<String, Object>>
	 */
	@ResponseBody
	@RequestMapping(value = "/getPowerAndIncome", method = RequestMethod.POST)
	public Response<List<Map<String, Object>>> getPowerAndIncome(
			@RequestBody JSONObject params, HttpServletRequest request) {
		// 获取时间维度(month、year、allYear)
		String timeDimension = params.getString("queryType");
		Response<List<Map<String, Object>>> response = new Response<List<Map<String, Object>>>();
		List<Map<String, Object>> resultMap = null;
		try {
			Map<String, Object> userInfo = CommonUtils.getCurrentUser(request);
			long startTime = 0l;
			long endTime = 9999999999999l;// 定义最大时间
			if ("month".equals(timeDimension)) {
//				int day = DateUtil.getDay(System.currentTimeMillis());
//				if(day == 1){
//					startTime = DateUtil.getBeginOfMonthTimeByMill(System.currentTimeMillis()-86400000);
//					endTime = DateUtil.getLastOfMonthTimeByMill(System.currentTimeMillis()-86400000);
//				}else{
					startTime = DateUtil.getBeginOfMonthTimeByMill(System.currentTimeMillis());
					endTime = DateUtil.getLastOfMonthTimeByMill(System.currentTimeMillis());
//				}
			} else if ("year".equals(timeDimension)) {
				int month = DateUtil.getMonth(System.currentTimeMillis());
				int day = DateUtil.getDay(System.currentTimeMillis());
				if(month == 1 && day == 1){
					// 获取当年的最大最小时间
					startTime = DateUtil.getBeginOfYearTimeByMill(System.currentTimeMillis()-86400000);
					endTime = DateUtil.getLastOfYearTimeByMill(System.currentTimeMillis()-86400000);
				}else{
					// 获取当年的最大最小时间
					startTime = DateUtil.getBeginOfYearTimeByMill(System.currentTimeMillis());
					endTime = DateUtil.getLastOfYearTimeByMill(System.currentTimeMillis());
				}
			}
			userInfo.put("startTime", startTime);
			userInfo.put("endTime", endTime);
			userInfo.put("timeDimension", timeDimension);
			resultMap = this.stationOverviewService.getPowerAndIncome(userInfo);
		} catch (Exception e) {
			log.error("getPowerAndIncome error. error msg : " + e.getMessage());
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		response.setCode(ResponseConstants.CODE_SUCCESS);
		response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		response.setResults(resultMap);
		return response;
	}

	/**
	 * 电站总览告警统计
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return Response<Map<String, Object>>
	 */
	@ResponseBody
	@RequestMapping(value = "/getAlarmStatistics", method = RequestMethod.POST)
	public Response<Map<String, Object>> getAlarmStatistics(
			HttpServletRequest request) {
		Response<Map<String, Object>> response = new Response<Map<String, Object>>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> queryList = null;
		try {
			Map<String, Object> userInfo = CommonUtils.getCurrentUser(request);
			queryList = this.stationOverviewService.getAlarmStatistics(userInfo);
			if (queryList != null && queryList.size() > 0) {
				for (Map<String, Object> map : queryList) {
					if(StringUtils.isEmpty(map.get("levelId"))){
						resultMap.put(
								AlarmLevelEnum.getMack(AlarmLevelEnum.SECONDARY.getValue()),
								map.get("alarmCount"));
						continue;
					}
					resultMap.put(
							AlarmLevelEnum.getMack(Integer.valueOf(map.get("levelId").toString())),
							map.get("alarmCount"));
				}
			}
		} catch (Exception e) {
			log.error("getAlarmStatistics error. error msg : " + e.getMessage());
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		response.setCode(ResponseConstants.CODE_SUCCESS);
		response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		response.setResults(resultMap);
		return response;
	}

	/**
	 * 电站总览电站PR排名
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return Response<Map<String, Object>>
	 */
	@ResponseBody
	@RequestMapping(value = "/getPRList", method = RequestMethod.POST)
	public Response<List<Map<String, Object>>> getPRList(@RequestBody JSONObject params,HttpServletRequest request) {
		String orderBy = params.getString("orderBy");
		Response<List<Map<String, Object>>> response = new Response<List<Map<String, Object>>>();
		List<Map<String, Object>> resultMap = null;
		try {
			Map<String, Object> userInfo = CommonUtils.getCurrentUser(request);
			userInfo.put("sortType", orderBy);
			resultMap = this.stationOverviewService.getPRList(userInfo);
		} catch (Exception e) {
			log.error("getPRList error. error msg : " + e.getMessage());
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		response.setCode(ResponseConstants.CODE_SUCCESS);
		response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		response.setResults(resultMap);
		return response;
	}

	/**
	 * 电站总览等效利用小时数排名
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return Response<Map<String, Object>>
	 */
	@ResponseBody
	@RequestMapping(value = "/getPPRList", method = RequestMethod.POST)
	public Response<List<Map<String, Object>>> getPPRList(@RequestBody JSONObject params,
			HttpServletRequest request) {
		String orderBy = params.getString("orderBy");
		Response<List<Map<String, Object>>> response = new Response<List<Map<String, Object>>>();
		List<Map<String, Object>> resultMap = null;
		try {
			Map<String, Object> userInfo = CommonUtils.getCurrentUser(request);
			userInfo.put("sortType", orderBy);
			resultMap = this.stationOverviewService.getPPRList(userInfo);
		} catch (Exception e) {
			log.error("getPPRList error. error msg : " + e.getMessage());
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		response.setCode(ResponseConstants.CODE_SUCCESS);
		response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		response.setResults(resultMap);
		return response;
	}

	/**
	 * 电站总览查询电站状态
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return Response<Map<String, Object>>
	 */
	@ResponseBody
	@RequestMapping(value = "/getStationStatus", method = RequestMethod.POST)
	public Response<Map<String, Object>> getStationStatus(
			HttpServletRequest request) {
		Response<Map<String, Object>> response = new Response<Map<String, Object>>();
		Map<String, Object> resultMap = null;
		try {
			Map<String, Object> userInfo = CommonUtils.getCurrentUser(request);
			resultMap = this.stationOverviewService.getStationStatus(userInfo);
		} catch (Exception e) {
			log.error("stationStatus error. error msg : " + e.getMessage());
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		response.setCode(ResponseConstants.CODE_SUCCESS);
		response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		response.setResults(resultMap);
		return response;
	}

	/**
	 * 获取人员管理电站的实时数据合计
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getRealtimeKPI", method = RequestMethod.POST)
	public Response<Map<String, Object>> getRealtimeKPI(
			HttpServletRequest request) {
		Response<Map<String, Object>> response = new Response<Map<String, Object>>();
		Map<String, Object> resultMap = null;
		try {
			Map<String, Object> userInfo = CommonUtils.getCurrentUser(request);
			resultMap = this.stationOverviewService.getRealtimeKPI(userInfo);
		} catch (Exception e) {
			log.error("getRealtimeKPI error. error msg : " + e.getMessage());
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		response.setCode(ResponseConstants.CODE_SUCCESS);
		response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		response.setResults(resultMap);
		return response;
	}

	/**
	 * 获取人员管理电站的列表
	 * 
	 * @param request
	 * @return Response<Object>
	 */
	@ResponseBody
	@RequestMapping(value = "/getPowerStationList", method = RequestMethod.POST)
	public Response<Object> getPowerStationList(HttpServletRequest request) {
		Response<Object> response = new Response<Object>();
		List<StationInfoM> resultMap = null;
		try {
			Map<String, Object> userInfo = CommonUtils.getCurrentUser(request);
			resultMap = this.stationOverviewService.getPowerStationList(userInfo);
		} catch (Exception e) {
			log.error("getPowerStationList error. error msg : " + e.getMessage());
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		response.setCode(ResponseConstants.CODE_SUCCESS);
		response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		response.setResults(resultMap);
		return response;
	}
	
	/**
	 * 电站总览设备分布
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return Response<Map<String, Object>>
	 */
	@ResponseBody
	@RequestMapping(value = "/getDevDistrition", method = RequestMethod.POST)
	public Response<Map<String, Object>> getDevDistrition(HttpServletRequest request) {
		Response<Map<String, Object>> response = new Response<Map<String, Object>>();
		Map<String, Object> resultMap = null;
		try {
			Map<String, Object> userInfo = CommonUtils.getCurrentUser(request);
			resultMap = this.stationOverviewService.getDevDistrition(userInfo);
		} catch (Exception e) {
			log.error("getDevDistrition error. error msg : " + e.getMessage());
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		response.setCode(ResponseConstants.CODE_SUCCESS);
		response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		response.setResults(resultMap);
		return response;
	}
	
	/**
	 * 电站总览社会贡献
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return Response<Map<String, Object>>
	 */
	@ResponseBody
	@RequestMapping(value = "/getContribution", method = RequestMethod.POST)
	public Response<Map<String, Object>> getContribution(HttpServletRequest request) {
		Response<Map<String, Object>> response = new Response<Map<String, Object>>();
		Map<String, Object> resultMap = null;
		try {
			Map<String, Object> userInfo = CommonUtils.getCurrentUser(request);
			resultMap = this.stationOverviewService.getContribution(userInfo);
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
