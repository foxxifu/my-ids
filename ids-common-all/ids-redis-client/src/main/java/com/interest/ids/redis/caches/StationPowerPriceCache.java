package com.interest.ids.redis.caches;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.interest.ids.common.project.bean.analysis.PowerPriceM;
import com.interest.ids.common.project.bean.sm.StationInfoM;
import com.interest.ids.common.project.utils.DateUtil;

/**
 * 电站分时电价缓存
 * 
 * @author claude
 *
 */
public class StationPowerPriceCache {

	public static ConcurrentHashMap<String, List<PowerPriceM>> powerPriceList = new ConcurrentHashMap<String, List<PowerPriceM>>();

	/**
	 * 刷新所有电站电价缓存
	 * 
	 * @param powerPrices
	 */
	public static void refreshAllData(List<PowerPriceM> powerPrices) {
		List<PowerPriceM> stationPowerPrices = null;
		for (PowerPriceM powerPrice : powerPrices) {
			if (powerPriceList.containsKey(powerPrice.getStationCode())) {
				powerPriceList.get(powerPrice.getStationCode()).add(powerPrice);
			} else {
				stationPowerPrices = new ArrayList<PowerPriceM>();
				stationPowerPrices.add(powerPrice);
				powerPriceList.put(powerPrice.getStationCode(), stationPowerPrices);
			}
		}
	}

	/**
	 * 更新并新增记录
	 * 
	 * @param powerPrices
	 */
	public static void refreshStationData(List<PowerPriceM> powerPrices) {
		String stationCode = powerPrices.get(0).getStationCode();
		powerPriceList.remove(stationCode);
		powerPriceList.put(stationCode, powerPrices);
	}

	/**
	 * 传入电站编号和时间，查询该时间电站的电价，当返回为NULL时则以电站信息表中的默认电价进行计算
	 * 
	 * @param stationCode
	 *            电站编号
	 * @param collectTime
	 *            采集时间
	 * @return 电价
	 */
	public static Double getStationPowerPrice(String stationCode, Long collectTime) {
		
		Double currentPowerPrice = 0d;
		
		StationInfoM stationInfo = StationCache.getStation(stationCode);
		// 根据stationCode在缓存中获取电站的默认电价
		if(stationInfo != null && stationInfo.getStationPrice() != null){
			currentPowerPrice = stationInfo.getStationPrice();
		}
		
		if (powerPriceList.containsKey(stationCode)) {
			List<PowerPriceM> powerPrices = powerPriceList.get(stationCode);
			Long startTime = null;// 分时电价最小开始时间：开始日期 + 开始时间
			Long endTime = null;// 分时电价最大结束时间：结束日期 + 结束时间
			Integer hourOfTime = null;
			for (PowerPriceM powerPrice : powerPrices) {
				startTime = powerPrice.getStartDate() + powerPrice.getStartTime() * 3600000;
				endTime = powerPrice.getEndDate() + powerPrice.getEndTime() * 3600000;
				if (collectTime > startTime && startTime < endTime) {
					hourOfTime = DateUtil.getHour(collectTime);
					if (hourOfTime > powerPrice.getStartTime() && hourOfTime < powerPrice.getEndTime()) {
						// 在分时电价列表中查询是否在此时间设置了分时电价，如果设置了分时电价则使用分时电价返回
						currentPowerPrice = powerPrice.getPrice();
						break;
					}
				}
			}
		}
		return currentPowerPrice;
	}

}
