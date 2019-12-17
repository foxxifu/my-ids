package com.interest.ids.biz.data.service.largeScreen;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.common.project.utils.DateUtil;
import com.interest.ids.commoninterface.dao.largescreen.LargeScreenDao4StationMapper;
import com.interest.ids.commoninterface.service.largeScreen.ILargeScreenService4Station;

@Service("largeScreenService4Station")
public class LargeScreenService4StationImpl implements
		ILargeScreenService4Station {
	
	private static final Logger log = LoggerFactory.getLogger(LargeScreenService4StationImpl.class);

	@Autowired
	private LargeScreenDao4StationMapper largeScreenDao4StationMapper;


	@Override
	public Map<String, Object> getActivePower(Map<String, Object> params) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		long startDay = DateUtil
				.getBeginOfDayTimeByMill(System.currentTimeMillis());
		long endDay = DateUtil.getLastOfDayTimeByMill(System
				.currentTimeMillis());
		params.put("startDate", startDay);
		params.put("endDate", endDay);
		List<Map<String, Object>> queryResult = this.largeScreenDao4StationMapper
				.getActivePower(params);
		for (Map<String, Object> map : queryResult) {
			resultMap.put(map.get("collectTime").toString(),
					map.get("activePower"));
		}
		return resultMap;
	}

	@Override
	public Double getCurrentActivePower(Map<String, Object> params) {
		return this.largeScreenDao4StationMapper.getCurrentActivePower(params);
	}

	@Override
	public Map<String, Object> getPowerTrends(Map<String, Object> params) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
//		int day = DateUtil.getDay(System.currentTimeMillis());
		// 获取当前月第一天的时间戳
		long firstDayOfMonth = 0l;
		// 获取当前月最后一天的时间戳
		long lastDayOfMonth = 0l;
//		if(day == 1){
//			firstDayOfMonth = DateUtil.getBeginOfMonthTimeByMill(System.currentTimeMillis()-86400000);
//			lastDayOfMonth = DateUtil.getLastOfMonthTimeByMill(System.currentTimeMillis()-86400000);
//		}else{
			firstDayOfMonth = DateUtil.getBeginOfMonthTimeByMill(System.currentTimeMillis());
			lastDayOfMonth = DateUtil.getLastOfMonthTimeByMill(System.currentTimeMillis());
//		}
		params.put("firstDayOfMonth", firstDayOfMonth);
		params.put("lastDayOfMonth", lastDayOfMonth);

		List<Map<String, Object>> queryList = this.largeScreenDao4StationMapper
				.getPowerTrends(params);
		if (queryList == null || queryList.size() == 0) {
			queryList = new ArrayList<Map<String, Object>>();
		}
		// 查询当天数据
		long currentDay = DateUtil.getBeginOfDayTimeByMill(System.currentTimeMillis());
		params.put("currentDay", currentDay);
		Map<String, Object> dayInfo = this.largeScreenDao4StationMapper.getCurrentDayCap(params);
		Map<String, Object> currentDayData = new HashMap<String, Object>();
		double dayCap = 0d;
		double dayIncome = 0d;
		if (dayInfo != null) {
			dayCap = dayInfo.containsKey("dayCapacity") ? Double.valueOf(dayInfo.get("dayCapacity").toString()) : 0d;
			dayIncome = dayInfo.containsKey("dayIncome") ? Double.valueOf(dayInfo.get("dayIncome").toString()) : 0d;
		}
		currentDayData.put("collectTime", currentDay);
		currentDayData.put("productPower", dayCap);
		currentDayData.put("powerProfit", dayIncome);
		queryList.add(currentDayData);
		resultMap.put("dayCap", dayCap);
		resultMap.put("powerTrends", queryList);
		return resultMap;
	}

	@Override
	public Map<String, Object> getCommonData(Map<String, Object> params) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String safeRunDays = this.largeScreenDao4StationMapper.getSafeRunDays(params.get("stationCode").toString());// 暂时取消
		Map<String, Object> queryMap = this.largeScreenDao4StationMapper.getCommonData(params);
		if (StringUtils.isEmpty(safeRunDays)) {
			resultMap.put("safeRunDays", 0);
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			long safeRunTime = System.currentTimeMillis();// 安全运行开始时间毫秒
			try {
				safeRunTime = sdf.parse(safeRunDays).getTime();
			} catch (ParseException e) {
				log.error("parse error, error msg : " + e);
			}
			long currentTime = System.currentTimeMillis();// 当前时间毫秒
			// 计算天数
			long days = (currentTime - safeRunTime) / 1000 / 3600 / 24;
			resultMap.put("safeRunDays", days);
		}
		if(queryMap != null){
			resultMap.put("totalCap", queryMap.containsKey("totalCapacity") ? queryMap.get("totalCapacity") : 0);
			resultMap.put("capacity", queryMap.containsKey("capacity") ? queryMap.get("capacity") : 0);
			resultMap.put("allTaskCount", queryMap.containsKey("allTaskCount") ? queryMap.get("allTaskCount") : 0);
			resultMap.put("notFinishTaskCount", queryMap.containsKey("notFinishTaskCount") ? queryMap.get("notFinishTaskCount") : 0);
		}

		return resultMap;
	}

	@Override
	public Map<String, Object> getListStationInfo(Map<String, Object> params) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> stationList = new HashMap<String, Object>();
		List<Map<String, Object>> queryList = this.largeScreenDao4StationMapper
				.getListStationInfo(params);
		if (queryList == null || queryList.size() == 0) {
			stationList.put("s0to10", 0);
			stationList.put("s100to500", 0);
			stationList.put("s10to100", 0);
			stationList.put("s500up", 0);
		} else {
			for (Map<String, Object> map : queryList) {
				stationList.put(map.get("stationLevel").toString(),
						map.get("stationCount"));
			}
		}
		resultMap.put("stationInfoList", stationList);
		return resultMap;
	}

	@Override
	public Map<String, Object> getSocialContribution(Map<String, Object> params) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 当年发电量
		long year = DateUtil.getBeginOfYearTimeByMill(System.currentTimeMillis());
		params.put("year", year);
		Map<String, Object> yearMap = this.largeScreenDao4StationMapper
				.getYearSocialContribution(params);
		if (yearMap == null) {
			yearMap = new HashMap<String, Object>();
			yearMap.put("carbonEmissions", 0);
			yearMap.put("deforestation", 0);
			yearMap.put("savedCoal", 0);
		}
		// 累计发电量
		Map<String, Object> totalMap = this.largeScreenDao4StationMapper
				.getTotalSocialContribution(params);
		if (totalMap == null) {
			totalMap = new HashMap<String, Object>();
			totalMap.put("carbonEmissions", 0);
			totalMap.put("deforestation", 0);
			totalMap.put("savedCoal", 0);
		}
		resultMap.put("year", yearMap);
		resultMap.put("accumulativeTotal", totalMap);
		return resultMap;
	}

	@Override
	public Map<String, Object> getDeviceCount(Map<String, Object> params) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> queryMap = this.largeScreenDao4StationMapper
				.getDeviceCount(params);
		if (queryMap != null && queryMap.size() > 0) {
			int allDevCount = 0;
			for (Map<String, Object> map : queryMap) {
				allDevCount += map.containsKey("devTypeCount")?Integer.valueOf(map.get("devTypeCount")
						.toString()):0;
				if ("pv".equals(map.get("devTypeId"))) {
					resultMap.put("pvCount", map.containsKey("devTypeCount")?map.get("devTypeCount"):0);
				}
				if(!"pv".equals(map.get("devTypeId"))){
					if (DevTypeConstant.INVERTER_DEV_TYPE.equals(Integer.valueOf(map.get("devTypeId").toString()))) {
						resultMap.put("inverterStringCount", map.get("devTypeCount"));
					}
					if (DevTypeConstant.CENTER_INVERT_DEV_TYPE.equals(Integer.valueOf(map.get("devTypeId").toString()))) {
						resultMap.put("inverterConcCount", map.get("devTypeCount"));
					}
					if (DevTypeConstant.DCJS_DEV_TYPE.equals(Integer.valueOf(map.get("devTypeId").toString()))) {
						resultMap.put("combinerBoxCount", map.get("devTypeCount"));
					}	
				}
			}
			resultMap.put("deviceTotalCount", allDevCount);
		}
		return resultMap;
	}

	@Override
	public List<Map<String, Object>> getAlarmStatistics(String stationCode) {
		return this.largeScreenDao4StationMapper.getAlarmStatistics(stationCode);
	}

	@Override
	public Map<String, Object> getPowerGeneration(Map<String, Object> params) {
		return this.largeScreenDao4StationMapper.getPowerGeneration(params);
	}

	@Override
	public Map<String, Object> getTaskStatistics(Map<String, Object> params) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("todo", 0);
		resultMap.put("doing", 0);
		resultMap.put("done", 0);
		List<Map<String, Object>> queryMap = this.largeScreenDao4StationMapper.getTaskStatistics(params);
		if(queryMap != null){
			for(Map<String, Object> map : queryMap){
				if("todo".equals(map.get("defectStat"))) {
					resultMap.put("todo", map.get("defectCount"));
				} else if("doing".equals(map.get("defectStat"))) {
					resultMap.put("doing", map.get("defectCount"));
				} else {
					resultMap.put("done", map.get("defectCount"));
				}
			}
		}
		return resultMap;
	}

}
