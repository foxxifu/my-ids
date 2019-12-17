package com.interest.ids.biz.web.report.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interest.ids.common.project.utils.MathUtil;
import com.interest.ids.commoninterface.dao.report.CompontAnalyzsisMapper;
import com.interest.ids.commoninterface.service.report.CompontAnalysisService;

@Service
public class CompontAnalysisServiceImpl implements CompontAnalysisService{

	@Autowired
	private CompontAnalyzsisMapper compontAnalyzsisDao;
	
	@Override
	public Integer selectStationMouthAnalysisCount(Map<String, Object> condition) {
		return compontAnalyzsisDao.selectStationMouthAnalysisCount(condition);
	}
	
	@Override
	public Map<String, Object> selectStationMouthAnalysis(Map<String,Object> condition) {
		
		List<Map<String,Object>> list =  compontAnalyzsisDao.selectStationMouthAnalysis(condition);
		if(null != list && list.size() > 0) {
			Map<String, Object> map = new HashMap<>();
			
			Map<String,Object> chartData = new HashMap<>();
			Map<String,Object> chart_Data = compontAnalyzsisDao.selectStationMouthChartData(condition);
			//组装数据
			if(null != chart_Data && chart_Data.size() > 0)
			{
				Map<String, Object> data = new HashMap<>();
				data.put("troublePvNum", chart_Data.get("troublePvNum"));
				data.put("troublePvCount", chart_Data.get("troublePvCount"));
				chartData.put("troublePv", data);
				
				data = new HashMap<>();
				data.put("ineffPvNum", chart_Data.get("ineffPvNum"));
				data.put("ineffPvCount", chart_Data.get("ineffPvCount"));
				chartData.put("ineffPv", data);
				
				data = new HashMap<>();
				data.put("hidPvNum", chart_Data.get("hidPvNum"));
				data.put("hidPvCount", chart_Data.get("hidPvCount"));
				chartData.put("hidPv", data);
			}
			
			map.put("chartData", chartData);
			map.put("tableData", list);
			
			return map;
		}else{
			return null;
		}             
	}

	@Override
	public Map<String, Object> selectCompontAnalysisMouth(Map<String, Object> condition) {
		List<Map<String,Object>> list =  compontAnalyzsisDao.selectCompontAnalysisMouth(condition);
		if(null != list && list.size() > 0) {
			Map<String, Object> map = new HashMap<>();
			
			List<Map<String,Object>> pvCountList =  compontAnalyzsisDao.monthPvCountNumOfState(condition);
			// 用于保存设备各种状态的数量的map <devId, <status, number>>
			Map<Long, Map<Integer, Integer>> pvCountMap = new HashMap<>();
			for (Map<String,Object> pvCount: pvCountList) {
				Long devId = pvCount.get("devId") == null ? null: Long.valueOf(pvCount.get("devId").toString());
				if (devId == null) {
					continue;
				}
				Integer pvState = pvCount.get("analysisState") == null ? null : Integer.valueOf(pvCount.get("analysisState").toString());
				if (pvState == null) {
					continue;
				}
				Map<Integer, Integer> tmpMap = pvCountMap.get(devId);
				if (tmpMap == null) {
					tmpMap = new HashMap<>();
					pvCountMap.put(devId, tmpMap);
				}
				tmpMap.put(pvState, pvCount.get("pvNum") == null ? 0 : Integer.valueOf(pvCount.get("pvNum").toString()));
			}
			// 因为list是对dev_id分组的所以，设备id肯定是唯一的
			
			map.put("tableData", list);
			Map<String,Object> chartData = new HashMap<>();
			Double troubleLostPower = 0D;
			Double ineffLostPower = 0D;
			Double hidLostPower = 0D;
			int troublePvNum = 0, ineffPvNum = 0, hidPvNum = 0;
			for (Map<String, Object> temp : list) {
				Long devId = temp.get("devId") == null ? -1L : Long.valueOf(temp.get("devId").toString());
				Map<Integer, Integer> tmpPvCountMap = pvCountMap.get(devId);
				if (tmpPvCountMap == null) { // 避免空指针异常
					tmpPvCountMap = new HashMap<>();
				}
				// 1:故障 2:低效 3:遮挡
				int tpv = tmpPvCountMap.get(1) == null ? 0 : tmpPvCountMap.get(1);
				int ipv = tmpPvCountMap.get(2) == null ? 0 : tmpPvCountMap.get(2);
				int hpv = tmpPvCountMap.get(3) == null ? 0 : tmpPvCountMap.get(3);
				temp.put("troublePvNum", tpv);
				temp.put("ineffPvNum", ipv);
				temp.put("hidPvNum", hpv);
				troubleLostPower += temp.get("troubleLostPower") != null ? Double.parseDouble(temp.get("troubleLostPower").toString()) : 0D;
				ineffLostPower += temp.get("ineffLostPower") != null ? Double.parseDouble(temp.get("ineffLostPower").toString()) : 0D;
				hidLostPower += temp.get("hidLostPower") != null ? Double.parseDouble(temp.get("hidLostPower").toString()) : 0D;
				
				troublePvNum += tpv;
				ineffPvNum += ipv;
				hidPvNum += hpv;
			}
			
			Map<String,Object> data = new HashMap<>();
			data.put("troublePvCount", troubleLostPower);
			data.put("troublePvNum", troublePvNum);
			chartData.put("troublePv", data);
			
			data = new HashMap<>();
			data.put("ineffPvCount", ineffLostPower);
			data.put("ineffPvNum", ineffPvNum);
			chartData.put("ineffPv", data);
			
			data = new HashMap<>();
			data.put("hidPvCount", hidLostPower);
			data.put("hidPvNum", hidPvNum);
			chartData.put("hidPv", data);
			
			map.put("chartData", chartData);
			
			return map;
		}else{
			return null;
		}
	}

	@Override
	public Integer selectCompontAnalysisMouthCount(Map<String, Object> condition) {
		return compontAnalyzsisDao.selectCompontAnalysisMouthCount(condition);
	}
	
	@Override
	public Map<String, Object> selectStationYearAnalysis(Map<String, Object> condition) {
		List<Map<String,Object>> list =  compontAnalyzsisDao.selectStationYearAnalysis(condition);
		List<Map<String,Object>> mouthList =  compontAnalyzsisDao.selectCompontAnalysisYearChartByStation(condition);
		if(null != list && mouthList != null){
			Map<String, Object> map = new HashMap<>();
			map.put("tableData", list);
			map.put("chartData", mouthList);
			return map;
		}
		return null;
	}

	@Override
	public Map<String, Object> selectCompontAnalysisYear(Map<String, Object> condition) {
		List<Map<String,Object>> yearList =  compontAnalyzsisDao.selectCompontAnalysisYear(condition);
		if(yearList != null){
			Map<String, Object> map = new HashMap<>();
			map.put("chartData", yearList);
			map.put("tableData", yearList);
			return map;
		}
		return null;
	}

	@Override
	public List<Long> selectComponetCountTime(Long matrixId) {
		return compontAnalyzsisDao.selectComponetCountTime(matrixId);
	}

	@Override
	public Map<String, Object> getCompontAnalysisDay(Map<String,Object> condition) {
		//1. 查询子阵日统计分析
		List<Map<String, Object>> listMap = compontAnalyzsisDao.getMatrixAnalysisDay(condition);
		//2. 查询组串诊断日分析
		List<Map<String,Object>> list = compontAnalyzsisDao.getCompontAnalysisDay(condition);
		Map<String, Object> map = new HashMap<>();
		Map<String,Object> chartData = new HashMap<>();
		
		//组装chart数据的格式
		if(null != listMap && listMap.size() > 0) {
			for (Map<String, Object> temp : listMap) {
				Map<String, Object> data = new HashMap<>();
				if("1".equals(temp.get("analysisState").toString()))
				{ // 故障
					data.put("troublePvCount", temp.get("lostPower"));
					data.put("troublePvNum", temp.get("pvCodeCount"));
					chartData.put("troublePv", data);
				}else if ("2".equals(temp.get("analysisState").toString()))
				{ // 低效
					data.put("ineffPvCount", temp.get("lostPower"));
					data.put("ineffPvNum", temp.get("pvCodeCount"));
					chartData.put("ineffPv", data);
				}else if("3".equals(temp.get("analysisState").toString()))
				{ // 遮挡
					data.put("hidPvCount", temp.get("lostPower"));
					data.put("hidPvNum", temp.get("pvCodeCount"));
					chartData.put("hidPv", data);
				}
			}
		}
		
		//组装table表格显示的格式
		List<Map<String, Object>> tmpResult = new ArrayList<>();
		if(null != list && list.size() > 0) {
			Map<String, Map<String, Object>> zjMap = new HashMap<>();
			
			for (Map<String, Object> datas : list) {
				Object key = datas.get("devId");
				Object pvNum = datas.get("pvCode");
				Object pvType = datas.get("analysisState"); // 目前只会是0 1 2 3 其中0不会出现，即只会是1 2 3
				if (key == null || pvNum == null || pvType == null) {
					continue;
				}
				String devId = key.toString();
				String pvCode = "pv" + pvNum;
				Map<String, Object> tmpData = zjMap.get(devId);
				if (tmpData == null) {
					tmpData = new HashMap<>();
					zjMap.put(devId, tmpData);
					tmpData.put("devAlias", datas.get("devAlias"));
				}
				// 每一种对应的损失发电量
				tmpData.put(pvCode + "_" + pvType, MathUtil.formatDouble(datas.get("lostPower"), 0d));
			}
			// 当前配置了的组串的串数
			Integer maxPv = condition.get("maxPv") == null ? 0 : Integer.decode(condition.get("maxPv").toString());
			// 组装前台需要的数据格式
			for (String devId : zjMap.keySet()) {
				Map<String, Object> vl = zjMap.get(devId);
				Map<String, Object> r1 = new HashMap<>();
				// 1.根据配置的pv信息获取每一串的pv信息
				for (int i = 1; i <= maxPv; i++) { // 放每一串的组串
					String pvCode = "pv" + i;
					String [] detailInfoArr = new String[3];
					String key1 = pvCode + "_1"; // 故障
					if (vl.get(key1) == null) {
						detailInfoArr[0] = "0|0";
					} else {
						detailInfoArr[0] = vl.get(key1) + "|1";
					}
					String key2 = pvCode + "_2"; // 低效
					if (vl.get(key2) == null) {
						detailInfoArr[1] = "0|0";
					} else {
						detailInfoArr[1] = vl.get(key2) + "|2";
					}
					String key3 = pvCode + "_3"; // 遮挡
					if (vl.get(key3) == null) {
						detailInfoArr[2] = "0|0";
					} else {
						detailInfoArr[2] = vl.get(key3) + "|3";
					}
					// 每一串的发电量情况
					r1.put(pvCode, detailInfoArr);
				}
				r1.put("devId", devId); // 做排序的字段
				r1.put("devAlias", vl.get("devAlias"));
				tmpResult.add(r1);
			}
			// 做一个排序，现在先根据设备的id来做排序
			Collections.sort(tmpResult, new Comparator<Map<String, Object>>() {
				@Override
				public int compare(Map<String, Object> o1, Map<String, Object> o2) {
					Object devId1 = o1.get("devId");
					Object devId2 = o2.get("devId");
					if (devId1 == null) {
						return devId2 == null ? 0 : -1;
					}
					if (devId2 == null) {
						return 1;
					}
					long tmp = Long.valueOf(devId1.toString()) - Long.valueOf(devId2.toString());
					if (tmp == 0l) {
						return 0;
					}
					return tmp > 0l ? 1 : -1;
				}
			});
		}
		
		map.put("tableData", tmpResult);
		map.put("chartData", chartData);
		
		return map;
	}

	@Override
	public Integer selectStationYearAnalysisCount(Map<String, Object> condition) {
		return compontAnalyzsisDao.selectStationYearAnalysisCount(condition);
	}

	@Override
	public Integer selectCompontAnalysisDayCount(Map<String,Object> condition) {
		return compontAnalyzsisDao.selectCompontAnalysisDayCount(condition);
	}

	@Override
	public Integer selectStationDayAnalysisCount(Map<String, Object> condition) {
		return compontAnalyzsisDao.selectStationDayAnalysisCount(condition);
	}

	@Override
	public Map<String, Object> selectStationDayAnalysis(
			Map<String, Object> condition) {
		
		Map<String,Object> chart_Data = compontAnalyzsisDao.selectStationDayAnalysis(condition);
		List<Map<String,Object>> mouthList =  compontAnalyzsisDao.selectStationDayAnalysisByPage(condition);
		if(null != chart_Data && mouthList != null){
			Map<String,Object> chartData = new HashMap<>();
			//组装数据
			if(null != chart_Data && chart_Data.size() > 0)
			{
				Map<String, Object> data = new HashMap<>();
				data.put("troublePvNum", chart_Data.get("troublePvNum"));
				data.put("troublePvCount", chart_Data.get("troubleLostPower"));
				chartData.put("troublePv", data);
				
				data = new HashMap<>();
				data.put("ineffPvNum", chart_Data.get("ineffPvNum"));
				data.put("ineffPvCount", chart_Data.get("ineffLostPower"));
				chartData.put("ineffPv", data);
				
				data = new HashMap<>();
				data.put("hidPvNum", chart_Data.get("hidPvNum"));
				data.put("hidPvCount", chart_Data.get("hidLostPower"));
				chartData.put("hidPv", data);
			}
			
			Map<String, Object> map = new HashMap<>();
			map.put("tableData", mouthList);
			map.put("chartData", chartData);
			return map;
		}
		return null;
	}

	@Override
	public Integer selectCompontAnalysisYearCount(Map<String, Object> condition) {
		return compontAnalyzsisDao.selectCompontAnalysisYearCount(condition);
	}

	@Override
	public Integer selectMaxPv(Map<String, Object> condition) {
		return compontAnalyzsisDao.selectMaxPv(condition);
	}

	@Override
	public Integer selectInverterDiscreteRateCount(Map<String, Object> condition) {
		return compontAnalyzsisDao.selectInverterDiscreteRateCount(condition);
	}

	@Override
	public Map<String, Object> selectInverterDiscreteRate(
			Map<String, Object> condition) {
		List<Map<String,Object>> _chartData = compontAnalyzsisDao.selectInverterDiscreteRateChartData(condition);
		Map<String,Object> chartData = new HashMap<>();
		
		if(null != _chartData && _chartData.size() > 0) 
		{
			for(int i =0; i<_chartData.size(); i++)
			{
				chartData.put(_chartData.get(i).get("discreteRate").toString(),_chartData.get(i).get("number"));
			}
		}
		
		List<Map<String,Object>> tableData = compontAnalyzsisDao.selectInverterDiscreteRateTableData(condition);
		
		Map<String,Object> map = new HashMap<>();
		map.put("chartData", chartData);
		map.put("tableData", tableData);
		
		return map;
	}

	@Override
	public Integer selectCombinerdcDiscreteRateCount(
			Map<String, Object> condition) {
		return compontAnalyzsisDao.selectCombinerdcDiscreteRateCount(condition);
	}

	@Override
	public Map<String, Object> selectCombinerdcDiscreteRate(
			Map<String, Object> condition) {
		List<Map<String,Object>> _chartData = compontAnalyzsisDao.selectCombinerdcDiscreteRateChartData(condition);
		List<Map<String,Object>> tableData = compontAnalyzsisDao.selectCombinerdcDiscreteRateTableData(condition);
		
		Map<String,Object> chartData = new HashMap<>();
		
		if(null != _chartData && _chartData.size() > 0) 
		{
			for(int i =0; i<_chartData.size(); i++)
			{
				chartData.put(_chartData.get(i).get("discreteRate").toString(),_chartData.get(i).get("number"));
			}
		}
		
		Map<String,Object> map = new HashMap<>();
		map.put("chartData", chartData);
		map.put("tableData", tableData);
		
		return map;
	}

	@Override
	public Integer getInverterMaxPv(Map<String, Object> condition) {
		return compontAnalyzsisDao.getInverterMaxPv(condition);
	}

	@Override
	public List<Object[]> exportStationMouthData(Map<String, Object> condition) {
		List<Map<String,Object>> listMap = compontAnalyzsisDao.exportStationMouthData(condition);
		if(null != listMap && listMap.size() > 0)
		{
			List<Object[]> list = parseMouthData(listMap);
			return list;
		}
		return null;
	}

	@Override
	public List<Object[]> exportSubarrayMouthData(Map<String, Object> condition) {
		List<Map<String,Object>> listMap = compontAnalyzsisDao.exportSubarrayMouthData(condition);
		
		if(null != listMap && listMap.size() > 0)
		{
			List<Object[]> list = new ArrayList<>();
			Map<String,Object> map = null;
			Object[] objs = null;
			int j;
			for (int i = 0; i < listMap.size(); i++) {
				j = 0;
				map = listMap.get(i);
				objs = new Object[map.size()];
				objs[j++] = map.get("devAlias");
				objs[j++] = map.get("pvCapacity");
				objs[j++] = map.get("matrixName");
				objs[j++] = map.get("troubleLostPower");
				objs[j++] = map.get("ineffLostPower");
				objs[j++] = map.get("hidLostPower");
				list.add(objs);
			}
			return list;
		}
		return null;
	}

	@Override
	public List<Object[]> exportStationYearData(Map<String, Object> condition) {
		List<Map<String,Object>> listMap = compontAnalyzsisDao.exportStationYearData(condition);
		
		if(null != listMap && listMap.size() > 0)
		{
			List<Object[]> list = new ArrayList<>();
			Map<String,Object> map = null;
			Object[] objs = null;
			int j;
			for (int i = 0; i < listMap.size(); i++) {
				j = 0;
				map = listMap.get(i);
				objs = new Object[map.size()];
				objs[j++] = map.get("matrixName");
//				objs[j++] = map.get("analysisTime");
				objs[j++] = map.get("installedCapacity");
				objs[j++] = map.get("pvNum");
				objs[j++] = map.get("productPower");
				objs[j++] = map.get("hidLostPower");
				objs[j++] = map.get("ineffLostPower");
				objs[j++] = map.get("troubleLostPower");
				list.add(objs);
			}
			return list;
		}
		return null;
	}

	@Override
	public List<Object[]> exportSubarrayYearData(Map<String, Object> condition) {
		List<Map<String,Object>> listMap = compontAnalyzsisDao.exportSubarrayYearData(condition);
		
		if(null != listMap && listMap.size() > 0)
		{
			List<Object[]> list = new ArrayList<>();
			Map<String,Object> map = null;
			Object[] objs = null;
			int j;
			for (int i = 0; i < listMap.size(); i++) {
				j = 0;
				map = listMap.get(i);
				objs = new Object[map.size()];
				objs[j++] = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(map.get("analysisTime").toString())));
				objs[j++] = map.get("installedCapacity");
				objs[j++] = map.get("pvNum");
				objs[j++] = map.get("productPower");
				objs[j++] = map.get("hidLostPower");
				objs[j++] = map.get("ineffLostPower");
				objs[j++] = map.get("troubleLostPower");
				list.add(objs);
			}
			return list;
		}
		
		return null;
	}

	@Override
	public List<Object[]> exportStationDayData(Map<String, Object> condition) {
		List<Map<String,Object>> listMap = compontAnalyzsisDao.exportStationDayData(condition);
		if(null != listMap && listMap.size() > 0)
		{
			List<Object[]> list = parseMouthData(listMap);
			return list;
		}
		return null;
	}

	private List<Object[]> parseMouthData(List<Map<String, Object>> listMap) {
		List<Object[]> list = new ArrayList<>();
		Map<String,Object> map = null;
		Object[] objs = null;
		int j;
		for (int i = 0; i < listMap.size(); i++) {
			j = 0;
			map = listMap.get(i);
			objs = new Object[map.size()];
			objs[j++] = map.get("matrixName");
			objs[j++] = map.get("installedCapacity");
			objs[j++] = map.get("pvNum");
			objs[j++] = map.get("productPower");
			objs[j++] = map.get("hidPvNum");
			objs[j++] = map.get("hidLostPower");
			objs[j++] = map.get("ineffPvNum");
			objs[j++] = map.get("ineffLostPower");
			objs[j++] = map.get("troublePvNum");
			objs[j++] = map.get("troubleLostPower");
			list.add(objs);
		}
		return list;
	}

	@Override
	public List<Object[]> exportSubarrayDayData(Map<String, Object> condition) {
		List<Map<String,Object>> listMap = compontAnalyzsisDao.exportSubarrayDayData(condition);
		List<Object[]> list = new ArrayList<>();
		Object[] objs = null;
		String str = null;
		String[] strs = null;
		Integer maxPv = Integer.parseInt(condition.get("maxPv") != null ? condition.get("maxPv").toString() : "0");
		for (int i = 0; i < listMap.size(); i++) {
			objs = new Object[maxPv+1];
			for (int j = 1; j < objs.length; j++) {
				objs[j] = condition.get("nomal"); // 正常
			}
			
			Map<String,Object> data = listMap.get(i);
			objs[0] = data.get("devAlias");
			for (int j = 1; j <= maxPv; j++) {
				if(null != data.get("pv" + (j)))
				{
					str = data.get("pv" + (j)).toString();
					strs = str.split("@");
					objs[Integer.parseInt(strs[0])] = strs[1];
				}
			}
			
			list.add(objs);
		}
		return list;
	}

	@Override
	public List<Object[]> exportCcatterData(Map<String, Object> condition) {
		List<Map<String,Object>> listMap = compontAnalyzsisDao.exportCcatterData(condition);
		
		List<Object[]> list = new ArrayList<>();
		Object[] objs = null;
		Integer maxPv = Integer.parseInt(condition.get("maxPv") != null ? condition.get("maxPv").toString() : "0");
		int k = 0;
		for (int i = 0; i < listMap.size(); i++) {
			objs = new Object[maxPv+7];
			k = 0;
			Map<String,Object> data = listMap.get(i);
			objs[k++] = data.get("devAlias");
			objs[k++] = data.get("productPower");
			objs[k++] = data.get("efficiency");
			objs[k++] = data.get("equivalentHour");
			objs[k++] = data.get("peakPower");
			if(null != data.get("discreteRate"))
			{
				objs[k++] = data.get("discreteRate").toString().equals("-1.00") ? condition.get("未分析") : data.get("discreteRate");
			}
			objs[k++] = condition.get(data.get("isAnalysis")); // 国际化的处理map<>{未分析:国际化未分析,异常：国际化异常, 已分析：国际化后的已分析}
			
			for (int j = 1; j <= maxPv; j++) {
				objs[k++] = data.get("pv" + j);
			}
			
			list.add(objs);
		}
		
		return list;
	}

	@Override
	public List<Object[]> exportScatterDCData(Map<String, Object> condition) {
		List<Map<String,Object>> listMap = compontAnalyzsisDao.exportScatterDCData(condition);
		List<Object[]> list = new ArrayList<>();
		Object[] objs = null;
		Integer maxPv = Integer.parseInt(condition.get("maxPv") != null ? condition.get("maxPv").toString() : "0");
		int k = 0;
		for (int i = 0; i < listMap.size(); i++) {
			k = 0;
			objs = new Object[maxPv+4];
			Map<String,Object> data = listMap.get(i);
			objs[k++] = data.get("devAlias");
			objs[k++] = data.get("discreteRate");
			objs[k++] = condition.get(data.get("isAnalysis")); // 对返回文字的国际化处理, condition=Map<>{未分析：未分析的国际化，异常：异常的国际化，已分析：已分析的国际化}
			objs[k++] = data.get("avgU");
			
			for (int j = 1; j <= maxPv; j++) {
				objs[k++] = data.get("pv"+j);
			}
			
			list.add(objs);
		}
		return list;
	}
}
