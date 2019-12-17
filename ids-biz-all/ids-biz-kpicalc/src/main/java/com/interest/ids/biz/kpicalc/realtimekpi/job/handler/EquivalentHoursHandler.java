package com.interest.ids.biz.kpicalc.realtimekpi.job.handler;

import com.interest.ids.common.project.utils.MathUtil;
import com.interest.ids.commoninterface.constant.realtimekpi.KpiItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 等效小时利用数
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class EquivalentHoursHandler extends JobHandler {
	private static Logger log = LoggerFactory.getLogger(EquivalentHoursHandler.class);
	@Override
	public Map excuteJob(Map<String, Object> params) {
		//  取电站的当日发电量/装机容量
		Map<String, Double> dayCaptial = (Map<String, Double>) params
				.get(KpiItem.DAYCAPACITY.getVal());
		Map<String, Double> hourFactortMap = new HashMap<String, Double>();
		List<String> sids = (List<String>)params.get("STATIONCODES");
		Map<String,Double> installCapacityMap = getDevPVCapacityService().getStationPVCapByList(sids);
		log.info("installCapacityMap:"+installCapacityMap);
		for (String stationCode : sids) {
			Double daycaptial = dayCaptial.get(stationCode);
			Double capacity = installCapacityMap !=null && installCapacityMap.get(stationCode) != null ? installCapacityMap.get(stationCode) : 0d;
			Double hourFactor;
			if (null != capacity && MathUtil.notEquals(capacity, 0D)) {
				hourFactor = daycaptial / capacity;
			} else {
				hourFactor = 0D;
			}
			hourFactortMap.put(stationCode, hourFactor);
		}
		return hourFactortMap;
	}

	public EquivalentHoursHandler() {
	}

	public EquivalentHoursHandler(String prex) {
		super(prex);
	}
}
