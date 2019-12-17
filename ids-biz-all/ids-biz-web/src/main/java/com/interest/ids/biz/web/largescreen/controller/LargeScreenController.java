package com.interest.ids.biz.web.largescreen.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.internal.core.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.interest.ids.biz.web.utils.CommonUtils;
import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.common.project.enums.AlarmLevelEnum;
import com.interest.ids.common.project.utils.DateUtil;
import com.interest.ids.commoninterface.service.largeScreen.ILargeScreenService4Comp;
import com.interest.ids.commoninterface.service.largeScreen.ILargeScreenService4Domain;
import com.interest.ids.commoninterface.service.largeScreen.ILargeScreenService4Station;

/**
 * 查询大屏信息controller
 * 
 * @author claude
 *
 */
@Controller
@RequestMapping("/largeScreen")
public class LargeScreenController {
	
	private static final Logger log = LoggerFactory.getLogger(LargeScreenController.class);

	@Autowired
	private ILargeScreenService4Comp largeScreenService4Comp;
	@Autowired
	private ILargeScreenService4Domain largeScreenService4Domain;
	@Autowired
	private ILargeScreenService4Station largeScreenService4Station;

	/**
	 * 大屏供电量负荷查询接口
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getActivePower", method = RequestMethod.POST)
	public Response<Map<String, Object>> getActivePower(
			@RequestBody JSONObject param, HttpServletRequest request) {
		Response<Map<String, Object>> response = new Response<Map<String, Object>>();
		String queryId = param.getString("queryId");// 后续增加区域和电站下钻时开启，传入区域或电站ID
		// queryType：1、代表大屏点击的是企业或者为初始化界面；2、代表点击的是区域；3、代表点击的是电站
		String queryType = param.getString("queryType");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> activePower = null;
		Double currentActivePower = 0d;
		Map<String, Object> params = null;
		try {

			params = CommonUtils.getCurrentUser(request);
			if (StringUtils.isEmpty(queryType) || "1".equals(queryType)) {
				params.put("enterpriseId", queryId);
				activePower = largeScreenService4Comp.getActivePower(params);
				currentActivePower = largeScreenService4Comp.getCurrentActivePower(params);
			} else if ("2".equals(queryType)) {// 传入数据为区域id，查询区域对应的数据
				params.put("domainId", queryId);
				activePower = largeScreenService4Domain.getActivePower(params);
				currentActivePower = largeScreenService4Domain.getCurrentActivePower(params);
			} else if ("3".equals(queryType)) {// 查询电站信息
				params.put("stationCode", queryId);
				activePower = largeScreenService4Station.getActivePower(params);
				currentActivePower = largeScreenService4Station.getCurrentActivePower(params);
			}
		} catch (Exception e) {// 否则参数错误
			log.error("getActivePower error. error msg : " + e.getMessage());
			response.setCode(ResponseConstants.CODE_ERROR_PARAM);
			response.setMessage(ResponseConstants.CODE_ERROR_PARAM_VALUE);
			return response;
		}
		resultMap.put("currentPower", currentActivePower == null ? 0
				: currentActivePower);
		resultMap.put("dayActivePowerList", activePower);
		response.setCode(ResponseConstants.CODE_SUCCESS);
		response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		response.setResults(resultMap);
		return response;
	}

	/**
	 * 大屏电站简介查询接口
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getCompdisc", method = RequestMethod.POST)
	public Response<Map<String, String>> getCompdisc(
			@RequestBody JSONObject param, HttpServletRequest request) {
		Response<Map<String, String>> response = new Response<Map<String, String>>();
		Map<String, String> resultMap = new HashMap<String, String>();
		String compDisc = null;
		Map<String, Object> params = null;
		try {
			params = CommonUtils.getCurrentUser(request);
			// 通过用户判定大屏企业信息显示情况。1：系统用户则显示系统配置中的企业描述和企业图片；2：企业用户则显示企业描述及企业图片
			compDisc = largeScreenService4Comp.getCompdisc(params);
		} catch (Exception e) {
			log.error("getCompdisc error. error msg : " + e.getMessage());
			response.setCode(ResponseConstants.CODE_ERROR_PARAM);
			response.setMessage(ResponseConstants.CODE_ERROR_PARAM_VALUE);
			return response;
		}
		resultMap.put("compDisc", compDisc == null ? "" : compDisc);
		response.setCode(ResponseConstants.CODE_SUCCESS);
		response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		response.setResults(resultMap);
		return response;
	}

	/**
	 * 大屏发电量趋势查询接口
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getPowerTrends", method = RequestMethod.POST)
	public Response<Map<String, Object>> getPowerTrends(
			@RequestBody JSONObject param, HttpServletRequest request) {
		Response<Map<String, Object>> response = new Response<Map<String, Object>>();
		String queryId = param.getString("queryId");
		// queryType：1、代表大屏点击的是企业或者为初始化界面；2、代表点击的是区域；3、代表点击的是电站
		String queryType = param.getString("queryType");
		Map<String, Object> params = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {

			params = CommonUtils.getCurrentUser(request);
			if (StringUtils.isEmpty(queryType) || "1".equals(queryType)) {// 查询该用户所在企业的信息
				params.put("enterpriseId", queryId);
				resultMap = largeScreenService4Comp.getPowerTrends(params);
			} else if ("2".equals(queryType)) {// 传入数据为区域id，查询区域对应的数据
				params.put("domainId", queryId);
				resultMap = largeScreenService4Domain.getPowerTrends(params);
			} else if ("3".equals(queryType)) {// 查询电站信息
				params.put("stationCode", queryId);
				resultMap = largeScreenService4Station.getPowerTrends(params);
			}
		} catch (Exception e) {
			log.error("getPowerTrends error. error msg : " + e.getMessage());
			response.setCode(ResponseConstants.CODE_ERROR_PARAM);
			response.setMessage(ResponseConstants.CODE_ERROR_PARAM_VALUE);
			return response;
		}
		response.setCode(ResponseConstants.CODE_SUCCESS);
		response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		response.setResults(resultMap);
		return response;
	}

	/**
	 * 大屏地图显示数据查询接口
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getMapShowData", method = RequestMethod.POST)
	public Response<Map<String, Object>> getMapShowData(
			@RequestBody JSONObject param, HttpServletRequest request) {
		Response<Map<String, Object>> response = new Response<Map<String, Object>>();
		String queryId = param.getString("queryId");
		// queryType：1、代表大屏点击的是企业或者为初始化界面；2、代表点击的是区域；3、代表点击的是电站
		String queryType = param.getString("queryType");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> params = null;
		try {
			params = CommonUtils.getCurrentUser(request);
			if (StringUtils.isEmpty(queryType) || "1".equals(queryType)) {// 查询该用户所在企业的信息
				params.put("enterpriseId", queryId);
				resultMap = largeScreenService4Comp.getMapShowData(params);
			} else if ("2".equals(queryType)) {// 传入数据为区域id，查询区域对应的数据
				resultMap = largeScreenService4Domain.getMapShowData(queryId, params);
			}
		} catch (Exception e) {
			log.error("getMapShowData error. error msg : " + e.getMessage());
			response.setCode(ResponseConstants.CODE_ERROR_PARAM);
			response.setMessage(ResponseConstants.CODE_ERROR_PARAM_VALUE);
			return response;
		}
		response.setCode(ResponseConstants.CODE_SUCCESS);
		response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		response.setResults(resultMap);
		return response;
	}

	/**
	 * 大屏常用数据查询接口
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getCommonData", method = RequestMethod.POST)
	public Response<Map<String, Object>> getCommonData(
			@RequestBody JSONObject param, HttpServletRequest request) {
		Response<Map<String, Object>> response = new Response<Map<String, Object>>();
		String queryId = param.getString("queryId");
		// queryType：1、代表大屏点击的是企业或者为初始化界面；2、代表点击的是区域；3、代表点击的是电站
		String queryType = param.getString("queryType");
		Map<String, Object> resultMap = null;
		Map<String, Object> params = null;
		try {

			params = CommonUtils.getCurrentUser(request);
			if (StringUtils.isEmpty(queryType) || "1".equals(queryType)) {// 查询该用户所在企业的信息
				params.put("enterpriseId", queryId);
				resultMap = largeScreenService4Comp.getCommonData(params);// 查询当前系统的安全运行天
			} else if ("2".equals(queryType)) {// 传入数据为区域id，查询区域对应的数据
				params.put("domainId", queryId);
				resultMap = largeScreenService4Domain.getCommonData(params);
			} else if ("3".equals(queryType)) {// 查询电站信息
				params.put("stationCode", queryId);
				resultMap = largeScreenService4Station.getCommonData(params);
			}
		} catch (Exception e) {
			log.error("getCommonData error. error msg : " + e.getMessage());
			response.setCode(ResponseConstants.CODE_ERROR_PARAM);
			response.setMessage(ResponseConstants.CODE_ERROR_PARAM_VALUE);
			return response;
		}
		resultMap.put("currentTime", System.currentTimeMillis());
		response.setCode(ResponseConstants.CODE_SUCCESS);
		response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		response.setResults(resultMap);
		return response;
	}

	/**
	 * 大屏电站规模查询接口
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getListStationInfo", method = RequestMethod.POST)
	public Response<Map<String, Object>> getListStationInfo(
			@RequestBody JSONObject param, HttpServletRequest request) {
		Response<Map<String, Object>> response = new Response<Map<String, Object>>();
		String queryId = param.getString("queryId");
		// queryType：1、代表大屏点击的是企业或者为初始化界面；2、代表点击的是区域；3、代表点击的是电站
		String queryType = param.getString("queryType");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> params = null;
		try {

			params = CommonUtils.getCurrentUser(request);
			if (StringUtils.isEmpty(queryType) || "1".equals(queryType)) {// 查询该用户所在企业的信息
				params.put("enterpriseId", queryId);
				resultMap = largeScreenService4Comp.getListStationInfo(params);
			} else if ("2".equals(queryType)) {// 传入数据为区域id，查询区域对应的数据
				params.put("domainId", queryId);
				resultMap = largeScreenService4Domain.getListStationInfo(params);
			} else if ("3".equals(queryType)) {// 查询电站信息
				params.put("stationCode", queryId);
				resultMap = largeScreenService4Station.getListStationInfo(params);
			}
		} catch (Exception e) {
			log.error("getListStationInfo error. error msg : " + e.getMessage());
			response.setCode(ResponseConstants.CODE_ERROR_PARAM);
			response.setMessage(ResponseConstants.CODE_ERROR_PARAM_VALUE);
			return response;
		}

		response.setCode(ResponseConstants.CODE_SUCCESS);
		response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		response.setResults(resultMap);
		return response;
	}

	/**
	 * 大屏社会贡献查询接口
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getSocialContribution", method = RequestMethod.POST)
	public Response<Map<String, Object>> getSocialContribution(
			@RequestBody JSONObject param, HttpServletRequest request) {
		Response<Map<String, Object>> response = new Response<Map<String, Object>>();
		String queryId = param.getString("queryId");
		// queryType：1、代表大屏点击的是企业或者为初始化界面；2、代表点击的是区域；3、代表点击的是电站
		String queryType = param.getString("queryType");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> params = null;
		try {

			params = CommonUtils.getCurrentUser(request);
			if (StringUtils.isEmpty(queryType) || "1".equals(queryType)) {// 查询该用户所在企业的信息
				params.put("enterpriseId", queryId);
				resultMap = largeScreenService4Comp.getSocialContribution(params);
			} else if ("2".equals(queryType)) {// 传入数据为区域id，查询区域对应的数据
				params.put("domainId", queryId);
				resultMap = largeScreenService4Domain.getSocialContribution(params);
			} else if ("3".equals(queryType)) {// 查询电站信息
				params.put("stationCode", queryId);
				resultMap = largeScreenService4Station.getSocialContribution(params);

			}
		} catch (Exception e) {
			log.error("getSocialContribution error. error msg : " + e.getMessage());
			response.setCode(ResponseConstants.CODE_ERROR_PARAM);
			response.setMessage(ResponseConstants.CODE_ERROR_PARAM_VALUE);
			return response;
		}

		response.setCode(ResponseConstants.CODE_SUCCESS);
		response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		response.setResults(resultMap);
		return response;
	}

	/**
	 * 大屏设备分布数据查询接口
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getDeviceCount", method = RequestMethod.POST)
	public Response<Map<String, Object>> getDeviceCount(
			@RequestBody JSONObject param, HttpServletRequest request) {
		Response<Map<String, Object>> response = new Response<Map<String, Object>>();
		String queryId = param.getString("queryId");
		// queryType：1、代表大屏点击的是企业或者为初始化界面；2、代表点击的是区域；3、代表点击的是电站
		String queryType = param.getString("queryType");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> params = null;
		try {

			params = CommonUtils.getCurrentUser(request);
			if (StringUtils.isEmpty(queryType) || "1".equals(queryType)) {// 查询该用户所在企业的信息
				params.put("enterpriseId", queryId);
				resultMap = largeScreenService4Comp.getDeviceCount(params);// 查询当前系统的安全运行天
			} else if ("2".equals(queryType)) {// 传入数据为区域id，查询区域对应的数据
				params.put("domainId", queryId);
				resultMap = largeScreenService4Domain.getDeviceCount(params);
			} else if ("3".equals(queryType)) {// 查询电站信息
				params.put("stationCode", queryId);
				resultMap = largeScreenService4Station.getDeviceCount(params);
			}
		} catch (Exception e) {
			log.error("getDeviceCount error. error msg : " + e.getMessage());
			response.setCode(ResponseConstants.CODE_ERROR_PARAM);
			response.setMessage(ResponseConstants.CODE_ERROR_PARAM_VALUE);
			return response;
		}

		response.setCode(ResponseConstants.CODE_SUCCESS);
		response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		response.setResults(resultMap);
		return response;
	}
	
	/**
	 * 大屏电站告警统计
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return Response<Map<String, Object>>
	 */
	@ResponseBody
	@RequestMapping(value = "/getAlarmStatistics", method = RequestMethod.POST)
	public Response<Map<String, Object>> getAlarmStatistics(@RequestBody JSONObject params) {
		Response<Map<String, Object>> response = new Response<Map<String, Object>>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String stationCode = params.getString("stationCode");
		List<Map<String, Object>> queryList = null;
		try {
			Assert.isNotNull(stationCode, "station code is null!");
			queryList = this.largeScreenService4Station.getAlarmStatistics(stationCode);
			if (queryList != null && queryList.size() > 0) {
				String alarmType = null;
				for (Map<String, Object> map : queryList) {
					alarmType = AlarmLevelEnum.getMack(Integer.valueOf(map.get("levelId").toString()));
					resultMap.put(alarmType, map.get("alarmCount"));
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
	 * 大屏发电状态数据查询接口
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getPowerGeneration", method = RequestMethod.POST)
	public Response<Map<String, Map<String, Object>>> getPowerGeneration(
			@RequestBody JSONObject param, HttpServletRequest request) {
		Response<Map<String, Map<String, Object>>> response = new Response<Map<String, Map<String, Object>>>();
		String queryId = param.getString("queryId");
		// queryType：1、代表大屏点击的是企业或者为初始化界面；2、代表点击的是区域；3、代表点击的是电站
		String queryType = param.getString("queryType");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> params = null;
		try {

			params = CommonUtils.getCurrentUser(request);
			Long today = DateUtil.getBeginOfDayTimeByMill(System.currentTimeMillis());
			Long year = DateUtil.getBeginOfYearTimeByMill(System.currentTimeMillis());
			params.put("today", today);
			params.put("year", year);
			if (StringUtils.isEmpty(queryType) || "1".equals(queryType)) {// 查询该用户所在企业的信息
				params.put("enterpriseId", queryId);
				resultMap = largeScreenService4Comp.getPowerGeneration(params);
			} else if ("2".equals(queryType)) {// 传入数据为区域id，查询区域对应的数据
				params.put("domainId", queryId);
				resultMap = largeScreenService4Domain.getPowerGeneration(params);
			} else if ("3".equals(queryType)) {// 查询电站信息
				params.put("stationCode", queryId);
				resultMap = largeScreenService4Station.getPowerGeneration(params);

			}
		} catch (Exception e) {
			log.error("getPowerGeneration error. error msg : " + e.getMessage());
			response.setCode(ResponseConstants.CODE_ERROR_PARAM);
			response.setMessage(ResponseConstants.CODE_ERROR_PARAM_VALUE);
			return response;
		}
		Map<String, Map<String, Object>> returnMap = this.initReturnMapForPowerGeneration(resultMap);
		response.setCode(ResponseConstants.CODE_SUCCESS);
		response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		response.setResults(returnMap);
		return response;
	}
	
	private Map<String, Map<String, Object>> initReturnMapForPowerGeneration(Map<String, Object> resultMap){
		Map<String, Map<String, Object>> returnMap = new HashMap<String, Map<String, Object>>();
		if(resultMap != null){
			Object capacity = resultMap.containsKey("capacity") ? resultMap.get("capacity") : 0;
			Map<String, Object> activePowerMap = new HashMap<String, Object>();
			if(resultMap.containsKey("activePower")){
				activePowerMap.put("value", resultMap.get("activePower"));
				activePowerMap.put("max", capacity);
			}else{
				activePowerMap.put("value", 0);
				activePowerMap.put("max", 0);
			}
			returnMap.put("activePower", activePowerMap);
			
			Map<String, Object> dayCapMap = new HashMap<String, Object>();
			if(resultMap.containsKey("dayCap")){
				dayCapMap.put("value", resultMap.get("dayCap"));
				dayCapMap.put("max", Double.valueOf(capacity.toString()) * 6);
			}else{
				dayCapMap.put("value", 0);
				dayCapMap.put("max", 0);
			}
			returnMap.put("dayCap", dayCapMap);
			
			Map<String, Object> dayIncomeMap = new HashMap<String, Object>();
			if(resultMap.containsKey("dayIncome")){
				dayIncomeMap.put("value", resultMap.get("dayIncome"));
				dayIncomeMap.put("max", Double.valueOf(capacity.toString()) * 6);
			}else{
				dayIncomeMap.put("value", 0);
				dayIncomeMap.put("max", 0);
			}
			returnMap.put("dayIncome", dayIncomeMap);
			
			Map<String, Object> ongridPowerMap = new HashMap<String, Object>();
			if(resultMap.containsKey("ongridPower")){
				ongridPowerMap.put("value", resultMap.get("ongridPower"));
				ongridPowerMap.put("max", null);
			}else{
				ongridPowerMap.put("value", 0);
				ongridPowerMap.put("max", 0);
			}
			returnMap.put("ongridPower", ongridPowerMap);
			
			Map<String, Object> totalPowerMap = new HashMap<String, Object>();
			if(resultMap.containsKey("totalPower")){
				totalPowerMap.put("value", resultMap.get("totalPower"));
				totalPowerMap.put("max", null);
			}else{
				totalPowerMap.put("value", 0);
				totalPowerMap.put("max", 0);
			}
			returnMap.put("totalPower", totalPowerMap);
			
			Map<String, Object> totalIncomeMap = new HashMap<String, Object>();
			if(resultMap.containsKey("totalIncome")){
				totalIncomeMap.put("value", resultMap.get("totalIncome"));
				totalIncomeMap.put("max", null);
			}else{
				totalIncomeMap.put("value", 0);
				totalIncomeMap.put("max", 0);
			}
			returnMap.put("totalIncome", totalIncomeMap);
		}
		return returnMap;
	}
	
	/**
	 * 电站任务工单统计
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getTaskStatistics", method = RequestMethod.POST)
	public Response<Map<String, Object>> getTaskStatistics(
			@RequestBody JSONObject param, HttpServletRequest request) {
		Response<Map<String, Object>> response = new Response<Map<String, Object>>();
		String queryId = param.getString("queryId");
		// queryType：1、代表大屏点击的是企业或者为初始化界面；2、代表点击的是区域；3、代表点击的是电站
		String queryType = param.getString("queryType");
		Map<String, Object> resultMap = null;
		Map<String, Object> params = null;
		try {

			params = CommonUtils.getCurrentUser(request);
			if (StringUtils.isEmpty(queryType) || "1".equals(queryType)) {// 查询该用户所在企业的信息
				// 当前大屏只有电站级模块才显示任务工单数，企业级数据暂不体现
			} else if ("2".equals(queryType)) {// 传入数据为区域id，查询区域对应的数据
				// 当前大屏只有电站级模块才显示任务工单数，区域级数据暂不体现
			} else if ("3".equals(queryType)) {// 查询电站信息
				params.put("stationCode", queryId);
				resultMap = largeScreenService4Station.getTaskStatistics(params);

			}
		} catch (Exception e) {
			log.error("getTaskStatistics error. error msg : " + e.getMessage());
			response.setCode(ResponseConstants.CODE_ERROR_PARAM);
			response.setMessage(ResponseConstants.CODE_ERROR_PARAM_VALUE);
			return response;
		}
		response.setCode(ResponseConstants.CODE_SUCCESS);
		response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		response.setResults(resultMap);
		return response;
	}

}
