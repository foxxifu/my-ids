package com.interest.ids.biz.data.service.stationoverview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.common.project.constant.DeviceConstant.ConnectStatus;
import com.interest.ids.common.project.constant.StationInfoConstant;
import com.interest.ids.common.project.spring.context.SystemContext;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.common.project.utils.DateUtil;
import com.interest.ids.commoninterface.dao.station.StationInfoMMapper;
import com.interest.ids.commoninterface.dao.stationoverview.StationOverviewDaoMapper;
import com.interest.ids.commoninterface.service.stationoverview.IStationOverviewService;
import com.interest.ids.redis.caches.StationCache;
import com.interest.ids.redis.client.service.ConnStatusCacheClient;

@Service
public class StationOverviewServiceImpl implements IStationOverviewService {

	private static final Logger log = LoggerFactory
			.getLogger(StationOverviewServiceImpl.class);

	@Autowired
	private StationOverviewDaoMapper stationOverviewDaoMapper;

	@Autowired
	private StationInfoMMapper stationInfoMMapper;

	@Override
	public Map<String, Object> getStationDistribution(
			Map<String, Object> userInfo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> stationDist = new ArrayList<Map<String, Object>>();
		// 1、查询省级行政区域数量，如果当前用户下管理的电站所属行政区域省级别大于五个，则电站分布显示到省级别
		List<Map<String, Object>> provinceList = this.stationOverviewDaoMapper
				.listProvince(userInfo);
		int stationCount = 0;
		if (provinceList != null && provinceList.size() >= 5) {
			Map<String, Object> result = null;
			for (Map<String, Object> map : provinceList) {
				stationCount += Integer.valueOf(map.get("stationCount")
						.toString());
				result = new HashMap<String, Object>();
				result.put("name", map.get("province"));
				result.put("value", map.get("capacity"));
				stationDist.add(result);
			}
			resultMap.put("stationCount", stationCount);
			resultMap.put("stationDist", stationDist);
			return resultMap;
		}
		// 2、查询市级行政区域数量，如果当前用户下管理的电站所属行政区域市级别大于五个，则电站分布显示到市级别
		List<Map<String, Object>> cityList = this.stationOverviewDaoMapper
				.listCity(userInfo);
		if (cityList != null && cityList.size() >= 5) {
			Map<String, Object> result = null;
			for (Map<String, Object> map : cityList) {
				stationCount += Integer.valueOf(map.get("stationCount")
						.toString());
				result = new HashMap<String, Object>();
				result.put("name", map.get("city"));
				result.put("value", map.get("capacity"));
				stationDist.add(result);
			}
			resultMap.put("stationCount", stationCount);
			resultMap.put("stationDist", stationDist);
			return resultMap;
		}
		// 2、查询市级行政区域数量，如果当前用户下管理的电站所属行政区域市级别大于五个，则电站分布显示到市级别
		List<Map<String, Object>> countyList = this.stationOverviewDaoMapper
				.listCounty(userInfo);
		if (countyList != null) {
			Map<String, Object> result = null;
			for (Map<String, Object> map : countyList) {
				stationCount += Integer.valueOf(map.get("stationCount")
						.toString());
				result = new HashMap<String, Object>();
				result.put("name", map.get("county"));
				result.put("value", map.get("capacity"));
				stationDist.add(result);
			}
		}
		resultMap.put("stationCount", stationCount);
		resultMap.put("stationDist", stationDist);
		return resultMap;
	}

	@Override
	public List<Map<String, Object>> getPowerAndIncome(Map<String, Object> userInfo) {
		List<Map<String, Object>> queryList = this.stationOverviewDaoMapper.getPowerAndIncome(userInfo);
		if (queryList == null) {
			queryList = new ArrayList<Map<String, Object>>();
		}
		if ("month".equals(userInfo.get("timeDimension"))) {
			// 月份查询需要加上当天实时数据
			Map<String, Object> realKPI = this.stationOverviewDaoMapper.getRealtimeKPI(userInfo);
			if (realKPI != null) {
				Map<String, Object> currentData = new HashMap<String, Object>();
				currentData.put("producePower", realKPI.get("dayCapacity"));
				currentData.put("powerProfit", realKPI.get("powerProfit"));
				currentData.put("collectTime", DateUtil.getBeginOfDayTimeByMill(System.currentTimeMillis()));
				queryList.add(currentData);
			}
		}
		return queryList;
	}

	@Override
	public List<Map<String, Object>> getAlarmStatistics(
			Map<String, Object> userInfo) {
		List<Map<String, Object>> queryList = this.stationOverviewDaoMapper
				.getAlarmStatistics(userInfo);
		return queryList;
	}

	@Override
	public List<Map<String, Object>> getPRList(Map<String, Object> userInfo) {
		// 获取昨日时间
		Long yestodayTime = System.currentTimeMillis() - 86400000;
		userInfo.put("startTime", DateUtil.getBeginOfDayTimeByMill(yestodayTime));
		userInfo.put("endTime", DateUtil.getLastOfDayTimeByMill(yestodayTime));

		return this.stationOverviewDaoMapper.getPRList(userInfo);
	}

	@Override
	public List<Map<String, Object>> getPPRList(Map<String, Object> userInfo) {
		// 获取昨日时间
		Long yestodayTime = System.currentTimeMillis() - 86400000;
		userInfo.put("startTime", DateUtil.getBeginOfDayTimeByMill(yestodayTime));
		userInfo.put("endTime", DateUtil.getLastOfDayTimeByMill(yestodayTime));

		return this.stationOverviewDaoMapper.getPPRList(userInfo);
	}

	@Override
	public Map<String, Object> getStationStatus(Map<String, Object> userInfo) {
		Map<String, Object> resultMap  = new HashMap<String, Object>();
		// 根据用户和用户类型查询用户下所有电站列表
		List<StationInfoM> stationList = this.stationInfoMMapper.selectStationInfoMByUserId(userInfo);
		int disconnected = 0;
		int health = 0;
		int trouble = 0;
		if (stationList != null && stationList.size() > 0) {
			List<String> sids = new ArrayList<String>();
			for (StationInfoM station : stationList) {
				sids.add(station.getStationCode());
			}
			Map<String, String> stationStatus = StationCache.getStationHealthState(sids);
			if(stationStatus != null && stationStatus.size() > 0){
				for (String sid : stationStatus.keySet()) {
					if (StationInfoConstant.HEALTHY.toString().equals(stationStatus.get(sid))) {// 健康
						health++;
					} else if (StationInfoConstant.DISCONECTED.toString().equals(stationStatus.get(sid))) {// 通讯中断
						disconnected++;
					} else if (StationInfoConstant.TROUBLE.toString().equals(stationStatus.get(sid))) {// 故障
						trouble++;
					} else {
						log.info(sid + " has a wrong station status! station status is :" + stationStatus.get(sid));
					}
				}
				
			}else{
				disconnected = stationList.size();
			}

		}
		resultMap.put("health", health);
		resultMap.put("disconnected", disconnected);
		resultMap.put("trouble", trouble);
		return resultMap;
	}

	@Override
	public Map<String, Object> getRealtimeKPI(Map<String, Object> userInfo) {
		// 获取当月时间
		long monthTime = DateUtil.getBeginOfMonthTimeByMill(System.currentTimeMillis());
		// 获取当年时间
		long yearTime = DateUtil.getBeginOfYearTimeByMill(System.currentTimeMillis());
		// 获取当天时间
		long startTime = DateUtil.getBeginOfDayTimeByMill(System.currentTimeMillis());
		long endTime = DateUtil.getLastOfDayTimeByMill(System.currentTimeMillis());
		userInfo.put("monthTime", monthTime);
		userInfo.put("yearTime", yearTime);
		userInfo.put("startTime", startTime);
		userInfo.put("endTime", endTime);
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> queryResult = this.stationOverviewDaoMapper.getRealtimeKPI(userInfo);
		double dayCap = 0;
		double activePower = 0;
		double productPower = 0;
		double powerProfit = 0;
		double yearCap = 0;
		double capacity = 0;
		double yearPowerProfit = 0;
		double totalIncome = 0;
		if (queryResult != null && queryResult.size() > 0) {
			if (queryResult.containsKey("dayCapacity")) {// 当日发电量
				dayCap = Double.valueOf(queryResult.get("dayCapacity").toString());
			}
			if (queryResult.containsKey("activePower")) {// 实时功率
				activePower = Double.valueOf(queryResult.get("activePower").toString());
			}
			if (queryResult.containsKey("productPower")) {// 累计发电量
				productPower = Double.valueOf(queryResult.get("productPower").toString());

			}
			if (queryResult.containsKey("yearCap")) {// 年发电量
				yearCap = Double.valueOf(queryResult.get("yearCap").toString());

			}
			if (queryResult.containsKey("powerProfit")) {// 月收益
				powerProfit = Double.valueOf(queryResult.get("powerProfit").toString());

			}
			if (queryResult.containsKey("capacity")) {// 月收益
				capacity = Double.valueOf(queryResult.get("capacity").toString());

			}
			if (queryResult.containsKey("yearPowerProfit")) {// 月收益
				yearPowerProfit = Double.valueOf(queryResult.get("yearPowerProfit").toString());
			}
			if (queryResult.containsKey("totalIncome")) {// 月收益
				totalIncome = Double.valueOf(queryResult.get("totalIncome").toString());
			}
		}
		result.put("dayCapacity", dayCap);
		result.put("activePower", activePower);
		result.put("totalCapacity", productPower);
		result.put("yearCap", yearCap);
		result.put("dayIncome", powerProfit);
		result.put("yearIncome", yearPowerProfit);
		result.put("capacity", capacity);
		result.put("totalIncome", totalIncome);
		return result;
	}

	@Override
	public List<StationInfoM> getPowerStationList(Map<String, Object> paramMap) {
		// 根据用户和用户类型查询用户下所有电站列表
		List<StationInfoM> stationList = this.stationInfoMMapper.selectStationInfoMByUserId(paramMap);
		if (stationList != null && stationList.size() > 0) {
			List<String> sids = new ArrayList<String>();
			for (StationInfoM station : stationList) {
				sids.add(station.getStationCode());
			}
			Map<String, String> stationStatus = StationCache.getStationHealthState(sids);

			for (StationInfoM station : stationList) {
				station.setStationCurrentState(stationStatus.get(station.getStationCode()));
			}

		}
		return stationList;
	}

	@Override
	public Map<String, Object> getDevDistrition(Map<String, Object> userInfo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> queryMap = this.stationOverviewDaoMapper.getDevDistrition(userInfo);
		if (queryMap != null && queryMap.size() > 0) {
			int allDevCount = 0;
			for (Map<String, Object> map : queryMap) {
				allDevCount += map.containsKey("devTypeCount")?Integer.valueOf(map.get("devTypeCount").toString()):0;
				if ("pv".equals(map.get("devTypeId"))) {
					resultMap.put("zj", map.containsKey("devTypeCount")?map.get("devTypeCount"):0);
				}
				if(!"pv".equals(map.get("devTypeId"))){
					if (DevTypeConstant.INVERTER_DEV_TYPE.equals(Integer.valueOf(map.get("devTypeId").toString()))) {
						resultMap.put("nbq1", map.get("devTypeCount"));
					}
					if (DevTypeConstant.CENTER_INVERT_DEV_TYPE.equals(Integer.valueOf(map.get("devTypeId").toString()))) {
						resultMap.put("nbq2", map.get("devTypeCount"));
					}
					if (DevTypeConstant.DCJS_DEV_TYPE.equals(Integer.valueOf(map.get("devTypeId").toString()))) {
						resultMap.put("zlhlx", map.get("devTypeCount"));
					}
				}
			}
			resultMap.put("deviceTotalCount", allDevCount);
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> getContribution(Map<String, Object> userInfo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 当年发电量
		long year = DateUtil.getBeginOfYearTimeByMill(System.currentTimeMillis());
		userInfo.put("year", year);
		Map<String, Object> yearMap = this.stationOverviewDaoMapper.getYearSocialContribution(userInfo);
		if (yearMap == null) {
			resultMap.put("co2Year", 0);
			resultMap.put("treeYear", 0);
			resultMap.put("coalYear", 0);
		}else{
			resultMap.put("co2Year", yearMap.containsKey("carbonEmissions") ? yearMap.get("carbonEmissions") : 0);
			resultMap.put("treeYear", yearMap.containsKey("deforestation") ? yearMap.get("deforestation") : 0);
			resultMap.put("coalYear", yearMap.containsKey("savedCoal") ? yearMap.get("savedCoal") : 0);
		}
		// 累计发电量
		Map<String, Object> totalMap = this.stationOverviewDaoMapper.getTotalSocialContribution(userInfo);
		if (totalMap == null) {
			resultMap.put("coalTotal", 0);
			resultMap.put("co2Total", 0);
			resultMap.put("treeTotal", 0);
		}else{
			resultMap.put("co2Total", totalMap.containsKey("carbonEmissions") ? totalMap.get("carbonEmissions") : 0);
			resultMap.put("treeTotal", totalMap.containsKey("deforestation") ? totalMap.get("deforestation") : 0);
			resultMap.put("coalTotal", totalMap.containsKey("savedCoal") ? totalMap.get("savedCoal") : 0);
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> getAppPowerStationList(Map<String, Object> paramMap) {
		List<StationInfoM> allPageResult = new ArrayList<StationInfoM>();
		List<StationInfoM> queryResult = this.getPowerStationList(paramMap);
		for (StationInfoM stationInfo : queryResult) {
			if(paramMap.get("stationStatus") == null || 
					paramMap.get("stationStatus").equals(stationInfo.getStationCurrentState())){
				allPageResult.add(stationInfo);
			}
		}
		return this.getPageInfo(Integer.valueOf(paramMap.get("index").toString()), 
				Integer.valueOf(paramMap.get("pageSize").toString()), allPageResult);
	}
	
	/**
	 * 获取分页信息
	 * 
	 * @return
	 */
	private Map<String, Object> getPageInfo(int index, int pageSize,
			List<StationInfoM> allPageResult) {
		int startIndex = (index - 1) * pageSize;
		int endIndex = index * pageSize;
		Map<String, Object> pageResult = new HashMap<String, Object>();
		List<StationInfoM> pageList = new ArrayList<StationInfoM>();
		int count = 0;
		int allSize = 0;
		if(allPageResult != null && allPageResult.size() > 0){
			count = allPageResult.size();
			if(count % pageSize != 0){
				allSize = count / pageSize + 1;
			}else{
				allSize = count / pageSize;
			}
			for(int i=0; i<allPageResult.size(); i++){
				if(i >= startIndex && i < endIndex){
					pageList.add(allPageResult.get(i));
				}
			}
		}
		List<String> sids = new ArrayList<String>();
		for(StationInfoM stationInfo : pageList){
			sids.add(stationInfo.getStationCode());
		}
		List<Map<String, Object>> stationAndDev = this.stationInfoMMapper.getStationDevAndType(sids);
		List<Map<String, Object>> stationAndAlarm = this.stationInfoMMapper.getStationDevAndAlarm(sids);
		Map<String, Map<Integer, List<Long>>> stationDevAndType = new HashMap<String, Map<Integer,List<Long>>>();
		Map<Integer, List<Long>> devAndType = null;
		List<Long> devIds = null;
		for(Map<String, Object> map : stationAndDev){
			if(stationDevAndType.containsKey(map.get("stationCode"))){
				if(stationDevAndType.get(map.get("stationCode")).containsKey(map.get("devTypeId"))){
					stationDevAndType.get(map.get("stationCode")).get(map.get("devTypeId"))
					.add(Long.valueOf(map.get("id").toString()));
				}else{
					devIds = new ArrayList<Long>();
					devIds.add(Long.valueOf(map.get("id").toString()));
					stationDevAndType.get(map.get("stationCode")).put(Integer.valueOf(map.get("devTypeId").toString()), devIds);
				}
			}else{
				devAndType = new HashMap<Integer, List<Long>>();
				devIds = new ArrayList<Long>();
				devIds.add(Long.valueOf(map.get("id").toString()));
				devAndType.put(Integer.valueOf(map.get("devTypeId").toString()), devIds);
				stationDevAndType.put(map.get("stationCode").toString(), devAndType);
			}
		}
		
		Map<String, Map<Integer, Integer>> stationDevAndAlarm = new HashMap<String, Map<Integer,Integer>>();
		Map<Integer, Integer> devAndAlarm = null;
		for(Map<String, Object> map : stationAndAlarm){
			if(stationDevAndAlarm.containsKey(map.get("stationCode"))){
				stationDevAndAlarm.get(map.get("stationCode").toString()).put(Integer.valueOf(map.get("devTypeId").toString()), 
						Integer.valueOf(map.get("alarmCount").toString()));
			}else{
				devAndAlarm = new HashMap<Integer, Integer>();
				devAndAlarm.put(Integer.valueOf(map.get("devTypeId").toString()), 
						Integer.valueOf(map.get("alarmCount").toString()));
				stationDevAndAlarm.put(map.get("stationCode").toString(), devAndAlarm);
			}
		}
		for(StationInfoM stationInfo : pageList){
			/*
             * 根据电站状态判断设备状态： 1）当电站为断连时，电站下所有设备为断连状态 2）电站为正常，电站下所有设备为正常态
             * 3）当电站为故障时，分别看电站下设备类型是否为故障，如果有一个设备为故障则该类设备为故障态
             */
            Map<Integer, Integer> allDeviceTypeStatus = new HashMap<Integer, Integer>();
            Map<Integer, List<Long>> devAndTypes = stationDevAndType.get(stationInfo.getStationCode());
            Map<Integer, Integer> devAndAlarms = stationDevAndAlarm.get(stationInfo.getStationCode());
            if (StationInfoConstant.DISCONECTED.equals(stationInfo.getStationCurrentState())) {
                markDeviceTypeStatus(allDeviceTypeStatus, StationInfoConstant.DISCONECTED,
                		devAndTypes == null ? null : devAndTypes.keySet());
            } else if (StationInfoConstant.HEALTHY.equals(stationInfo.getStationCurrentState())) {
                markDeviceTypeStatus(allDeviceTypeStatus, StationInfoConstant.HEALTHY,
                		devAndTypes == null ? null : devAndTypes.keySet());
            } else {
            	if(devAndTypes != null){
            		for(Integer devTypeId : devAndTypes.keySet()){
            			if(this.isDisconnetted(devAndTypes.get(devTypeId))){
            				allDeviceTypeStatus.put(devTypeId, StationInfoConstant.DISCONECTED);
            			}else{
            				if(devAndAlarms != null && devAndAlarms.get(devTypeId) > 0){
            					allDeviceTypeStatus.put(devTypeId, StationInfoConstant.TROUBLE);
            				}else{
            					allDeviceTypeStatus.put(devTypeId, StationInfoConstant.HEALTHY);
            				}
            			}
            		}
            	}
            }
            stationInfo.setDeviceStatus(allDeviceTypeStatus);
		}
		pageResult.put("list", pageList);
		pageResult.put("allSize", allSize);
		pageResult.put("count", count);
		
		return pageResult;
	}
	
	/**
     * 将某类设备标记为同一运行状态
     */
    private void markDeviceTypeStatus(Map<Integer, Integer> deviceTypeStatus, Integer status,
            Set<Integer> deviceTypeIds) {
        if (CommonUtil.isNotEmpty(deviceTypeIds)) {

            if (deviceTypeIds.contains(DevTypeConstant.INVERTER_DEV_TYPE)
                    || deviceTypeIds.contains(DevTypeConstant.CENTER_INVERT_DEV_TYPE)
                    || deviceTypeIds.contains(DevTypeConstant.HOUSEHOLD_INVERTER_DEV_TYPE)) {
                deviceTypeStatus.put(DevTypeConstant.INVERTER_DEV_TYPE, status);
            }

            if (deviceTypeIds.contains(DevTypeConstant.ACJS_DEV_TYPE)) {
                deviceTypeStatus.put(DevTypeConstant.DCJS_DEV_TYPE, status);
            }

            if (deviceTypeIds.contains(DevTypeConstant.BOX_DEV_TYPE)) {
                deviceTypeStatus.put(DevTypeConstant.BOX_DEV_TYPE, status);
            }
        }
    }
    
    private boolean isDisconnetted(List<Long> devIds){
    	boolean isDisconnetted = false;
    	ConnStatusCacheClient statusCacheClient = (ConnStatusCacheClient) SystemContext.getBean("connCacheClient");
    	for(Long devId : devIds){
            ConnectStatus connectStatus = (ConnectStatus) statusCacheClient.get(devId);
    		if(ConnectStatus.DISCONNECTED.equals(connectStatus)){
    			isDisconnetted = true;
    			break; 
    		}
    	}
    	return isDisconnetted;
    }
	
}
