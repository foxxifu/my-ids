package com.interest.ids.biz.data.service.kpi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.interest.ids.commoninterface.dao.kpi.*;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.interest.ids.common.project.bean.kpi.CombinerDc;
import com.interest.ids.common.project.bean.kpi.InverterString;
import com.interest.ids.common.project.bean.kpi.KpiMeterMonthM;
import com.interest.ids.common.project.bean.kpi.KpiRealTimeM;
import com.interest.ids.common.project.bean.kpi.KpiStationHourM;
import com.interest.ids.common.project.bean.kpi.KpiStationMonthM;
import com.interest.ids.common.project.bean.kpi.KpiStationYearM;
import com.interest.ids.common.project.constant.KpiConstants;
import com.interest.ids.common.project.mapper.kpi.CombinerDcMapper;
import com.interest.ids.common.project.mapper.kpi.InverterStringMapper;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.common.project.utils.MathUtil;
import com.interest.ids.commoninterface.dao.kpi.KpiRealTimeMapper;
import com.interest.ids.commoninterface.service.kpi.IKpiCommonService;

@Service("kpiCommonService")
public class KpiCommonServiceImpl implements IKpiCommonService {

    @Autowired
    private KpiCommonMapper kpiCommonMapper;

    @Autowired
    private KpiStationHourMapper kpiStationHourMMapper;
    @Autowired
    private KpiStationMonthMapper kpiStationMonthMMapper;
    @Autowired
    private KpiStationYearMapper kpiStationYearMMapper;
    @Autowired
    private CombinerDcMapper combinerDcMapper;
    @Autowired
    private InverterStringMapper inverterStringMapper;
    @Autowired
    private KpiRealTimeMapper kpiRealTimeMMapper;

    @Override
    public List<KpiStationHourM> queryStationHourKpi(List<String> sIds, Long sTime, Long eTime) {
        Map<String, Object> params = organizeQueryParam(sIds, sTime, eTime, KpiConstants.CACHE_TABLE_STATION_HOUR);

        return kpiStationHourMMapper.selectStationHourKpi(params);
    }

    @Override
    public List<KpiStationMonthM> queryStationMonthKpi(List<String> sIds, Long sTime, Long eTime) {

        Map<String, Object> params = organizeQueryParam(sIds, sTime, eTime, KpiConstants.CACHE_TABLE_STATION_DAY);
        return kpiStationMonthMMapper.selectStationMonthKpi(params);
    }

    @Override
    public List<KpiStationMonthM> queryStationMonthKpi(List<String> sIds, Long statTime) {

        return queryStationMonthKpi(sIds, statTime, null);
    }

    @Override
    public List<KpiStationYearM> queryStationYearKpi(List<String> sIds, Long sTime, Long eTime) {
        Map<String, Object> params = organizeQueryParam(sIds, sTime, eTime, KpiConstants.CACHE_TABLE_STATION_YEAR);
        return kpiStationYearMMapper.selectStationYearKpi(params);
    }
    
    @Override
    public List<KpiStationYearM> queryStationYearKpi(List<String> sIds, Long statTime) {

        return queryStationYearKpi(sIds, statTime, null);
    }

    @Override
    public Map<String, Double> queryMeterDataByField(List<String> sIds, List<Integer> deviceTypeIds, String field,
            Long sTime, Long eTime) {
        Map<String, Double> result = new HashMap<>();

        if (field != null && StringUtils.isNotEmpty(field) && sTime != null && eTime != null
                && CommonUtil.isNotEmpty(sIds)) {
            Map<String, Object> params = new HashMap<>();
            params.put("stationCodes", sIds);
            params.put("field", field);
            params.put("startTime", sTime);
            params.put("endTime", eTime);
            params.put("deviceTypeIds", deviceTypeIds);

            List<Map<String, Object>> queryResult = kpiCommonMapper.selectMeterDataByField(params);
            for (Map<String, Object> ele : queryResult) {
                result.put(MathUtil.formatString(ele.get("device_id")), MathUtil.formatDouble(ele.get(field)));
            }
        }

        return result;
    }

    private Map<String, Object> organizeQueryParam(List<String> sIds, Long sTime, Long eTime, String tableName) {

        Map<String, Object> params = new HashMap<>();
        params.put("stationCodes", sIds);
        params.put("startTime", sTime);
        params.put("endTime", eTime);
        params.put("tableName", tableName);

        return params;
    }

    @Override
    public List<CombinerDc> getCombinerDCMs(String stationCode, Long startTime, Long endTime) {
        List<CombinerDc> result = new ArrayList<>();

        if (stationCode != null && startTime != null && endTime != null) {
            result = combinerDcMapper.selectCombinerDCMs(stationCode, startTime, endTime);
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> getCombinerPvMaxData(String stationCode, Long startTime, Long endTime) {
        List<Map<String, Object>> result = new ArrayList<>();

        if (stationCode != null && startTime != null && endTime != null) {
            result = combinerDcMapper.selectCombinerPvMaxData(stationCode, startTime, endTime);
        }

        return result;
    }

    @Override
    public List<InverterString> getStringInverterData(String stationCode, Long startTime, Long endTime) {
        List<InverterString> result = new ArrayList<>();

        if (stationCode != null && startTime != null && endTime != null) {
            result = inverterStringMapper.selectInverterStrings(stationCode, startTime, endTime);
        }

        return result;
    }
    
    @Override
    @Transactional
    public void saveKpiRealTimeData(List<KpiRealTimeM> list) {
        
        if(CommonUtil.isNotEmpty(list)){
            kpiRealTimeMMapper.batchInsertRealTimeKpiData(list);
        }
        // 新增之后删除小于当前时间的数据
        this.deleteSmallerRealTimeData(list);
        // 删除当前数据库中最大值三天之前的数据
        kpiRealTimeMMapper.deleteDatabefore3Days();
    }

    @Override
    public void deleteSmallerRealTimeData(List<KpiRealTimeM> realTime) {
        for(KpiRealTimeM realtime : realTime){
            kpiRealTimeMMapper.deleteSmallerRealTimeData(realtime);
        }
    }

    @Override
    public List<Integer> selectStationDeviceTypes(String stationCode) {
        
        List<Integer> result = null;
        
        if (CommonUtil.isNotEmpty(stationCode)){
            result = kpiCommonMapper.selectStationDeviceTypes(stationCode);
        }
        
        return result;
    }

	@Override
	public List<KpiMeterMonthM> queryMeterDataByTime(String stationCode,
			long startTime, long endTime) 
	{
		return kpiCommonMapper.queryMeterDataByTime(stationCode,startTime,endTime);
	}
}
