package com.interest.ids.biz.data.service.stationoverview;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.interest.ids.common.project.bean.signal.DeviceInfo;
import com.interest.ids.common.project.constant.DevTypeConstant;
import com.interest.ids.common.project.utils.DateUtil;
import com.interest.ids.commoninterface.dao.largescreen.LargeScreenDao4StationMapper;
import com.interest.ids.commoninterface.dao.stationoverview.SingleStationOverviewDaoMapper;
import com.interest.ids.commoninterface.service.device.IDeviceInfoService;
import com.interest.ids.commoninterface.service.stationoverview.ISingleStationOverviewService;
import com.interest.ids.redis.caches.StationCache;
import com.interest.ids.redis.client.service.SignalCacheClient;

@Service
public class SingleStationOverviewServiceImpl implements
		ISingleStationOverviewService {
	
	private static final Logger log = LoggerFactory.getLogger(SingleStationOverviewServiceImpl.class);
	
	@Autowired
	private SignalCacheClient signalCacheClient;
	
	@Autowired
	private IDeviceInfoService deviceInfoService;

	@Autowired
	private SingleStationOverviewDaoMapper singleStationOverviewDaoMapper;
	
	@Autowired
	private LargeScreenDao4StationMapper largeScreenDao4StationMapper;

	@Override
	public List<Map<String, Object>> getSingleStationPowerAndIncome(
			String stationCode, String queryType, String queryTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		long startTime = 0;
		long endTime = 0;
		if ("month".equals(queryType)) {// 查询类型为月则查询当月每一天的发电量及收益
			if(StringUtils.isEmpty(queryTime)){
				startTime = DateUtil.getBeginOfMonthTimeByMill(System.currentTimeMillis());
				endTime = DateUtil.getLastOfMonthTimeByMill(System.currentTimeMillis());
			} else {
				startTime = DateUtil.getBeginOfMonthTimeByMill(Long.valueOf(queryTime));
				endTime = DateUtil.getLastOfMonthTimeByMill(Long.valueOf(queryTime));
			}
		} else if ("year".equals(queryType)) {// 查询类型为年则查询当年每一月的发电量及收益
			if(StringUtils.isEmpty(queryTime)){
				startTime = DateUtil.getBeginOfYearTimeByMill(System.currentTimeMillis());
				endTime = DateUtil.getLastOfYearTimeByMill(System.currentTimeMillis());
			} else {
				startTime = DateUtil.getBeginOfYearTimeByMill(Long.valueOf(queryTime));
				endTime = DateUtil.getLastOfYearTimeByMill(Long.valueOf(queryTime));
			}
		} else {// 查询生命期类所有年发电量
			startTime = 0;
			endTime = 999999999999999999l;
		}
		long currentDayTime = DateUtil.getBeginOfDayTimeByMill(System.currentTimeMillis());
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("stationCode", stationCode);
		params.put("queryType", queryType);
		params.put("currentDayTime", currentDayTime);
		List<Map<String, Object>> queryResult = this.singleStationOverviewDaoMapper.getSingleStationPowerAndIncome(params);
		return queryResult;
	}

	@Override
	public List<Map<String, Object>> getSingleStationAlarmStatistics(
			String stationCode) {
		return this.singleStationOverviewDaoMapper
				.getSingleStationAlarmStatistics(stationCode);
	}

	@Override
	public Map<String, Object> getSingleStationRealtimeKPI(String stationCode) {
		Map<String, Object> params = new HashMap<String, Object>();
		// 获取当日最小时间戳
		long currentDay = DateUtil.getBeginOfDayTimeByMill(System
				.currentTimeMillis());
		// 获取当年时间戳
		long currentYear = DateUtil.getBeginOfYearTimeByMill(System
				.currentTimeMillis());
		params.put("currentDay", currentDay);
		params.put("currentYear", currentYear);
		params.put("stationCode", stationCode);

		Map<String, Object> queryResult = this.singleStationOverviewDaoMapper
				.getSingleStationRealtimeKPI(params);

		return queryResult;
	}

	@Override
	public Map<String, Object> getSingleStationInfo(String stationCode) {
		Map<String, Object> params = new HashMap<String, Object>();
		// 获取当日最小时间戳
		long currentDay = DateUtil.getBeginOfDayTimeByMill(System.currentTimeMillis());
		// 获取当月时间戳
		long currentMonth = DateUtil.getBeginOfMonthTimeByMill(System.currentTimeMillis());
		// 获取当年时间戳
		long currentYear = DateUtil.getBeginOfYearTimeByMill(System.currentTimeMillis());
		params.put("currentDay", currentDay);
		params.put("currentMonth", currentMonth);
		params.put("currentYear", currentYear);
		params.put("stationCode", stationCode);
		String safeRunDays = this.largeScreenDao4StationMapper.getSafeRunDays(stationCode);// 暂时取消
		Map<String, Object> queryResult = this.singleStationOverviewDaoMapper.getSingleStationInfo(params);

		if (queryResult != null) {
			Map<String, String> statusCache = StationCache.getStationHealthState(Arrays.asList(stationCode));
			queryResult.put("stationStatus", statusCache.get(stationCode));
			long runDays = 0;
			if (!StringUtils.isEmpty(safeRunDays)) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				long safeRunTime = System.currentTimeMillis();// 安全运行开始时间毫秒
				try {
					safeRunTime = sdf.parse(safeRunDays).getTime();
				} catch (ParseException e) {
					log.error("parse error, error msg : " + e);
				}
				long currentTime = System.currentTimeMillis();// 当前时间毫秒
				// 计算天数
				runDays = (currentTime - safeRunTime) / 1000 / 3600 / 24;
			}
			queryResult.put("runDays", runDays);
		}
		return queryResult;
	}

	@Override
	public List<Map<String, Object>> getSingleStationActivePower(
			String stationCode) {
		Map<String, Object> params = new HashMap<String, Object>();
		long startTime = DateUtil.getBeginOfDayTimeByMill(System.currentTimeMillis());
		long endTime = DateUtil.getLastOfDayTimeByMill(System.currentTimeMillis());
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("stationCode", stationCode);
		return this.singleStationOverviewDaoMapper.getSingleStationActivePower(params);
	}

	@Override
	public Map<String, Object> getDevPowerStatus(String stationCode) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<DeviceInfo> devList = deviceInfoService.getDevicesByStationCode(stationCode);
		// devTypeId：1、组串式逆变器；14、集中式逆变器；17、地面式关口表；47、户用电表。
		// 逆变器获取输入总功率：mppt_power、有功功率：active_power；电表获取输出功率：active_power
		Double mpptPower = 0D;
		Double activePower = 0D;
		Double meterActivePower = 0D;
		List<Long> inverterList = new ArrayList<Long>();
		List<Long> meterList = new ArrayList<Long>();
		for(DeviceInfo dev : devList){
			if(DevTypeConstant.INVERTER_DEV_TYPE.equals(dev.getDevTypeId()) || 
					DevTypeConstant.CENTER_INVERT_DEV_TYPE.equals(dev.getDevTypeId())){
				// 电站下逆变器的数据
				inverterList.add(dev.getId());
				continue;
			}
			if(DevTypeConstant.GATEWAYMETER_DEV_TYPE_ID.equals(dev.getDevTypeId()) || 
					DevTypeConstant.HOUSEHOLD_METER.equals(dev.getDevTypeId())){
				// 电站下电表的数据
				meterList.add(dev.getId());
				continue;
			}
		}
		if(inverterList != null){
			for(Long devId : inverterList){
				mpptPower += signalCacheClient.get(devId, "mppt_power") == null ? 0D :
						Double.valueOf(signalCacheClient.get(devId, "mppt_power").toString());
				activePower += signalCacheClient.get(devId, "active_power") == null ? 0D : 
						Double.valueOf(signalCacheClient.get(devId, "active_power").toString());
			}
		}
		if(meterList != null){
			for(Long devId : meterList){
				meterActivePower += signalCacheClient.get(devId, "active_power") == null ? 0D :
						Double.valueOf(signalCacheClient.get(devId, "active_power").toString());
			}
		}
		returnMap.put("mpptPower", mpptPower);
		returnMap.put("activePower", activePower);
		returnMap.put("meterActivePower", meterActivePower);
		return returnMap;
	}

	@Override
	public Map<String, Object> getContribution(String stationCode) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 当年发电量
		long year = DateUtil.getBeginOfYearTimeByMill(System.currentTimeMillis());
		long month = DateUtil.getBeginOfMonthTimeByMill(System.currentTimeMillis());
		Map<String, Object> dataMap = this.singleStationOverviewDaoMapper.getContribution(stationCode, year, month);
		if (dataMap == null) {
			resultMap.put("co2Month", 0);
			resultMap.put("treeMonth", 0);
			resultMap.put("coalMonth", 0);
			resultMap.put("co2Year", 0);
			resultMap.put("treeYear", 0);
			resultMap.put("coalYear", 0);
			resultMap.put("coalTotal", 0);
			resultMap.put("co2Total", 0);
			resultMap.put("treeTotal", 0);
		}else{
			resultMap.put("co2Month", dataMap.containsKey("carbonEmissionsMonth") ? dataMap.get("carbonEmissionsMonth") : 0);
			resultMap.put("treeMonth", dataMap.containsKey("deforestationMonth") ? dataMap.get("deforestationMonth") : 0);
			resultMap.put("coalMonth", dataMap.containsKey("savedCoalMonth") ? dataMap.get("savedCoalMonth") : 0);
			resultMap.put("co2Year", dataMap.containsKey("carbonEmissionsYear") ? dataMap.get("carbonEmissionsYear") : 0);
			resultMap.put("treeYear", dataMap.containsKey("deforestationYear") ? dataMap.get("deforestationYear") : 0);
			resultMap.put("coalYear", dataMap.containsKey("savedCoalYear") ? dataMap.get("savedCoalYear") : 0);
			resultMap.put("co2Total", dataMap.containsKey("carbonEmissionsTotal") ? dataMap.get("carbonEmissionsTotal") : 0);
			resultMap.put("treeTotal", dataMap.containsKey("deforestationTotal") ? dataMap.get("deforestationTotal") : 0);
			resultMap.put("coalTotal", dataMap.containsKey("savedCoalTotal") ? dataMap.get("savedCoalTotal") : 0);
		}
		return resultMap;
	}
}
